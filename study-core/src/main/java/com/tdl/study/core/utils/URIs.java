package com.tdl.study.core.utils;

import org.jooq.lambda.tuple.Tuple2;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * the URIs defines a connection to a resource
 * <br/>schema://username:password@host1:port1,host2:port2/path1/path2?param1=value1&param2=value2#fragment
 * <br/>a uri contains some fields :
 * <br/>schema - required
 * <br/>username - optional
 * <br/>password - optional
 * <br/>hosts - required
 * <br/>ports - at least one port must be specified, if a host with no port assigned, then use the front host's port, else the back host's port, if no port assigned, an exception will throw out
 * <br/>path - optional
 * <br/>query - optional
 * <br/>fragment - optional
 * <br/>
 * <br/>all the fields should URL encoded, otherwise analysis may get wrong values
 */
public class URIs {
    static String SLASH = "/", BACKSLASH = "\\", COMMA = ",", SEMICOLON = ";", COLON = ":",
            AT = "@", QUESTION_MARK = "?", AMPERSAND = "&", EQUAL = "=", JINHAO = "#", XINHAO = "*";
    private static String DEFAULT_CHARSET = "UTF-8";
    private String charset;
    private String uri;

    private String schema;
    private String username;
    private String password;
    private List<Tuple2<String, Integer>> hosts = new ArrayList<>();
    private List<String> paths = new ArrayList<>();
    private Map<String, String> parameters = new HashMap<>();
    private String fragment;

    public URIs(String uri) {
        this(uri, DEFAULT_CHARSET);
    }

    public URIs(String uri, String charset) {
        this.uri = uri;
        this.charset = charset;
        try {
            parse();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void parse() throws UnsupportedEncodingException {
        String remain = uri;
        String locator;
        int index;

        // parse fragment
        locator = JINHAO;
        index = remain.lastIndexOf(locator);
        if (index > 0) { // has fragment
            String fragment = remain.length() == locator.length() + index ? null : remain.substring(index + locator.length());
            this.fragment = translate(fragment, charset, Action.DECODE);
            remain = remain.substring(0, index);
        }

        // parse parameters
        locator = QUESTION_MARK;
        index = remain.indexOf(locator);
        if (index > 0) {
            String query = remain.length() == locator.length() + index ? null : remain.substring(index + locator.length());
            parseParameters(query);
            remain = remain.substring(0, index);
        }

        // parse schema
        locator = COLON + SLASH + SLASH;
        index = remain.indexOf(locator);
        String schema;
        if (index <= 0 || (schema = remain.substring(0, index)).isEmpty())
            throw new IllegalStateException("schema is not set");
        this.schema = translate(schema, charset, Action.DECODE);
        remain = remain.substring(index + locator.length());

        // parse authority
        locator = AT;
        index = remain.indexOf(locator);
        if (index > 0) {
            String authority = remain.substring(0, index);
            parseAuthority(authority);
        }
        remain = remain.substring(index + locator.length());

        // parse host and port
        locator = SLASH;
        String[] hostsAndPaths = remain.split(locator, 2);
        parseHostAndPort(hostsAndPaths[0]);
        String paths = hostsAndPaths.length < 2 ? null : hostsAndPaths[1];
        parsePaths(paths);
        //  elasticsearch://username:password@192.168.1.108:9200,192.168.1.109:9200,192.169.1.110:9200/path1/path2/path3?param1=value&&param2=value2&param3&param4&#fragment
    }

    private void parseParameters(String query) throws UnsupportedEncodingException {
        if (Strings.empty(query)) return;

        for (String s : query.split(AMPERSAND)) {
            if (Strings.empty(s)) continue;
            String[] pair = s.split(EQUAL, 2);
            String key = translate(pair[0], charset, Action.DECODE);
            String value = pair.length < 2 ? null : translate(pair[1], charset, Action.DECODE);
            parameters.put(key, value);
        }
    }

    private void parseAuthority(String authority) throws UnsupportedEncodingException {
        if (Strings.empty(authority)) return;
        String[] up = authority.split(COLON, 2);
        username = Strings.empty(up[0]) ? null : translate(up[0], charset, Action.DECODE);
        password = up.length < 2 ? null : translate(up[1], charset, Action.DECODE);
    }

    private void parseHostAndPort(String hostAndPort) throws UnsupportedEncodingException {
        String[] hAndPs = hostAndPort.split(COMMA, -1);
        String[][] array = new String[hAndPs.length][2];
        for (int i = 0; i < hAndPs.length; i++) {
            String[] hAndP = hAndPs[i].split(COLON, -1);
            array[i][0] = hAndP[0];
            array[i][1] = (hAndP.length == 1 || hAndP[1].length() == 0) ? (i > 0 ? array[i-1][1] : null) : hAndP[1];
        }
        int port;
        for (int i = array.length - 1; i >= 0; i--) {
            if (null == array[i][1]) {
                if (i == array.length - 1) throw new RuntimeException("No port assigned.");
                array[i][1] = array[i + 1][1];
            }
            array[i][0] = translate(array[i][0], charset, Action.DECODE);
            try {
                port = Integer.parseInt(array[i][1]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Can not parse port '" + array[i][1] + "' for host " + array[i][0]);
            }
            hosts.add(new Tuple2<>(array[i][0], port));
        }
        Collections.reverse(hosts);
        System.out.println();
    }

    private void parsePaths(String path) throws UnsupportedEncodingException {
        if (null == path || path.isEmpty()) return;
        for (String s : path.split(SLASH)) {
            if (null == s || s.isEmpty()) continue;
            paths.add(translate(s, charset, Action.DECODE));
        }
    }

    public static URIs parse(String uri) throws UnsupportedEncodingException {
        return new URIs(uri);
    }

    public String getSchema() {
        return schema;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthority() {
        StringBuilder sb = new StringBuilder();
        if (null != username) sb.append(username);
        if (null != password) sb.append(COLON).append(password);
        return sb.toString();
    }

    public List<Tuple2<String, Integer>> getHosts() {
        return new ArrayList<>(hosts);
    }

    public Tuple2<String, Integer> getHostAt(int index) {
        return hosts.get(index);
    }

    public String getHostsAsString() {
        return hosts.stream().map(tuple -> tuple.v1() + COLON + tuple.v2()).collect(Collectors.joining(COMMA));
    }

    public List<String> getPaths() {
        return new ArrayList<>(paths);
    }

    public String getPathAt(int level) {
        return level >= paths.size() ? null :paths.get(level);
    }

    public String getPathsAsString() {
        return paths.stream().reduce("", (s1, s2) -> s1 + SLASH + s2);
    }

    public Map<String, String> getParameters() {
        return new HashMap<>(parameters);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getParametersAsString() {
        return parameters.entrySet().stream()
                .map(entry -> entry.getKey() + ((null != entry.getValue()) ? EQUAL + entry.getValue() : ""))
                .collect(Collectors.joining(AMPERSAND));
    }

    public String getFragment() {
        return fragment;
    }

    /**
     *  encode or decode a string
     * @param origin the string to translate
     * @param charset charset
     * @param action if true  encode; else decode
     * @return encoded/decoded string , if exception happens ,then origin
     */
    private static String translate(String origin, String charset, Action action) {
        if (Action.DO_NOTHING == action) return origin;
        try {
            return Action.ENCODE == action ? URLEncoder.encode(origin, charset) : URLDecoder.decode(origin, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * the URIs string value with URL encoded
     * @return
     */
    @Override
    public String toString() {
        return toString(true);
    }

    /**
     * URIs string value
     * @param encoded
     * @return true - string with url encoded, false - url decoded
     */
    public String toString(boolean encoded) {
        return encoded ? buildString(Action.ENCODE) : buildString(Action.DO_NOTHING);
    }
    
    private String buildString(Action action) {
        StringBuilder sb = new StringBuilder();
        sb.append(translate(schema, charset, action)).append(COLON).append(SLASH).append(SLASH);
        if (null != getUsername()) {
            sb.append(translate(getUsername(), charset, action));
            if (null != getPassword()) sb.append(COLON).append(translate(getPassword(), charset, action));
            sb.append(AT);
        }
        for (int i = 0; hosts.size() > 0 && i < hosts.size(); i++) {
            sb.append(translate(hosts.get(i).v1(), charset, action)).append(COLON).append(hosts.get(i).v2());
            if (i != hosts.size() - 1) sb.append(COMMA);
        }
        for (int i = 0; paths.size() > 0 && i < paths.size(); i++) {
            sb.append(SLASH).append(translate(paths.get(i), charset, action));
        }
        if (parameters.size() > 0) {
            sb.append(QUESTION_MARK);
            List<Tuple2<String, String>> params = parameters.entrySet().stream().map(entry -> new Tuple2<String, String>(entry.getKey(), entry.getValue())).collect(Collectors.toList());
            for (int i = 0; i < params.size(); i++) {
                Tuple2<String, String> tuple = params.get(i);
                sb.append(translate(tuple.v1(), charset, action));
                if (null != tuple.v2()) sb.append(EQUAL).append(translate(tuple.v2(), charset, action));
                if (i != params.size() - 1) sb.append(AMPERSAND);
            }
        }
        if (null != getFragment()) sb.append(JINHAO).append(translate(getFragment(), charset, action));
        return sb.toString();
    }

    private enum Action {
        ENCODE,
        DECODE,
        DO_NOTHING
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String schema;
        private String username;
        private String password;
        private List<Tuple2<String, Integer>> hosts = new ArrayList<>();
        private List<String> paths = new ArrayList<>();
        private Map<String, Object> parameters = new HashMap<>();
        private String fragment;

        private Builder() {}

        public Builder schema(String schema) {
            this.schema = schema;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder host(String host, Integer port) {
            hosts.add(new Tuple2<String, Integer>(host, port));
            return this;
        }

        public Builder path(String path) {
            if (Strings.empty(path)) return this;
            Stream.of(path.split(SLASH)).filter(p -> !Strings.empty(p)).forEach(p -> paths.add(p));
            return this;
        }

        public Builder parameter(String key, Object value) {
            parameters.put(key, value);
            return this;
        }

        public Builder parameters(Map<String, Object> entries) {
            parameters.putAll(entries);
            return this;
        }

        public Builder fragment(String fragment) {
            this.fragment = fragment;
            return this;
        }

        public URIs build() {
            return build(DEFAULT_CHARSET);
        }

        public URIs build(String charset) {
            StringBuilder sb = new StringBuilder();
            sb.append(translate(schema, charset, Action.ENCODE)).append(COLON).append(SLASH).append(SLASH);
            /*if (null != username) {
                sb.append(translate(username, charset, Action.ENCODE));
                if (null != password) sb.append(COLON).append(translate(password, charset, Action.ENCODE));
                sb.append(AT);
            }*/
            if (null != username) sb.append(translate(username, charset, Action.ENCODE));
            if (null != password) sb.append(COLON).append(translate(password, charset, Action.ENCODE));
            if (null != username || null != password) sb.append(AT);

            for (int i = 0 ; i < hosts.size(); i++) {
                Tuple2<String, Integer> tuple = hosts.get(i);
                sb.append(translate(tuple.v1(), charset, Action.ENCODE));
                if (null != tuple.v2()) sb.append(COLON).append(tuple.v2);
                if (i != hosts.size() - 1) sb.append(COMMA);
            }
            if (paths.size() > 0) {
                StringBuilder pathBuilder = new StringBuilder();
                for (String p : paths) {
                    pathBuilder.append(SLASH).append(translate(p, charset, Action.ENCODE));
                }
                sb.append(pathBuilder);
            }
            if (parameters.size() > 0) {
                sb.append(QUESTION_MARK);
                List<Map.Entry<String, Object>> entries = new ArrayList<>(parameters.entrySet());
                for (int i = 0; i < entries.size(); i++) {
                    Map.Entry<String, Object> entry = entries.get(i);
                    sb.append(translate(entry.getKey(), charset, Action.ENCODE));
                    if (null != entry.getValue())
                        sb.append(EQUAL).append(translate(entry.getValue().toString(), charset, Action.ENCODE));
                    if (i != entries.size() - 1) sb.append(AMPERSAND);
                }
            }
            if (null != fragment) sb.append(JINHAO).append(translate(fragment, charset, Action.ENCODE));
            return new URIs(sb.toString(), charset);
        }
    }
}
