package com.tdl.study.core.utils;

import org.jooq.lambda.tuple.Tuple2;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class URIs {
    static String SLASH = "/", BACKSLASH = "\\", COMMA = ",", SEMICOLON = ";", COLON = ":",
            AT = "@", QUESTION_MARK = "?", AMPERSAND = "&", EQUAL = "=", JINHAO = "#", XINHAO = "*";
    private static String DEFAULT_ENCODE = "UTF-8";

    private String uri;

    private String schema;
    private String username;
    private String password;
    private List<Tuple2<String, Integer>> hosts = new ArrayList<>();
    private List<String> paths = new ArrayList<>();
    private Map<String, String> parameters = new HashMap<>();
    private String fragment;

    public URIs(String uri) throws UnsupportedEncodingException {
        this.uri = uri;
        parse();
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
            this.fragment = (null == fragment) ? null : URLDecoder.decode(fragment, DEFAULT_ENCODE);
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
        this.schema = URLDecoder.decode(schema, DEFAULT_ENCODE);
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
        String[] hostsAndPaths = remain.split(SLASH, 2);
        parseHostAndPort(hostsAndPaths[0]);
        String paths = hostsAndPaths.length < 2 ? null : hostsAndPaths[1];
        parsePaths(paths);
        //  elasticsearch://username:password@192.168.1.108:9200,192.168.1.109:9200,192.169.1.110:9200/path1/path2/path3?param1=value&&param2=value2&param3&param4&#fragment
    }

    private void parseParameters(String query) throws UnsupportedEncodingException {
        if (null == query || "".equals(query)) return;

        for (String s : query.split(AMPERSAND)) {
            if (s == null || s.isEmpty()) continue;
            String[] pair = s.split(EQUAL, 2);
            String key = URLDecoder.decode(pair[0], DEFAULT_ENCODE);
            String value = pair.length < 2 ? null : URLDecoder.decode(pair[1], DEFAULT_ENCODE);
            parameters.put(key, value);
        }
    }

    private void parseAuthority(String authority) throws UnsupportedEncodingException {
        if (null == authority || authority.isEmpty()) return;
        String[] up = authority.split(COLON, 2);
        username = URLDecoder.decode(up[0], DEFAULT_ENCODE);
        password = up.length < 2 ? null : URLDecoder.decode(up[1], DEFAULT_ENCODE);
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
            array[i][0] = URLDecoder.decode(array[i][0], DEFAULT_ENCODE);
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
            paths.add(URLDecoder.decode(s, DEFAULT_ENCODE));
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

    public static void main(String[] args) throws UnsupportedEncodingException {
        URIs uri = parse("zookeeper://user+name:pass%21word@172.16.16.232:2181,172.16.16.232,172.16.16.232:2182/namespace/table?contry=%E6%B1%89%E5%AD%97#fragment");
        System.out.println(uri);
        System.out.println(uri.getSchema());
        System.out.println(uri.getUsername());
        System.out.println(uri.getPassword());
        System.out.println(uri.getHostsAsString());
        System.out.println(uri.getParametersAsString());
        System.out.println(uri.getFragment());

        System.out.println();
        System.out.println(uri.getHostAt(1));
        System.out.println(uri.getParameter("contry"));

        uri = URIs.builder()
                .schema("elastic search")
                .username("pseudo-elasticsearch")
                .password("!@#QAZ123qaz")
                .host("172.16.16.232", 9200)
                .host("172.16.16.233", null)
                .host("172.16.16.234", 9300)
                .path("pa/th")
                .path("/to")
                .path("your")
                .path("dest")
                .parameter("batch", 2000)
                .parameter("limit", 10000000)
                .parameter("haha", "hehe!@")
                .fragment("标签")
                .build();

        System.out.println(uri);
        System.out.println(uri.toString(true));
        System.out.println(uri.toString(false));
        System.out.println(uri.getPathAt(0));
    }

    private static String decode(String origin, boolean encoded) {
        try {
            return encoded ? URLDecoder.decode(origin, DEFAULT_ENCODE) : origin;
        } catch (UnsupportedEncodingException e) {
            return origin;
        }
    }

    private static String encode(String origin, String charset) throws UnsupportedEncodingException {
        return URLEncoder.encode(origin, charset);
    }

    @Override
    public String toString() {
        return toString(false);
    }
    
    public String toString(boolean decoded) {
        StringBuilder sb = new StringBuilder();
        sb.append(decode(schema, decoded)).append(COLON).append(SLASH).append(SLASH);
        if (null != getAuthority()) sb.append(decode(getAuthority(), decoded)).append(AT);
        if (null != getHostsAsString()) sb.append(decode(getHostsAsString(), decoded));
        if (null != getPathsAsString()) sb.append(decode(getPathsAsString(), decoded));
        if (null != getParametersAsString()) sb.append(QUESTION_MARK).append(getParametersAsString());
        if (null != getFragment()) sb.append(JINHAO).append(decode(getFragment(), decoded));
        return sb.toString();
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

        /**
         * add path once a level
         * @param path
         * @return
         */
        public Builder path(String path) {
            if (null == path || path.isEmpty()) return this;
            paths.add(path.replaceAll("^/+", "").replaceAll("/+$", ""));
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


        public URIs build() throws UnsupportedEncodingException {
            String charset = URIs.DEFAULT_ENCODE;
            StringBuilder sb = new StringBuilder();
            sb.append(encode(schema, charset)).append(COLON).append(SLASH).append(SLASH);
            if (null != username) {
                sb.append(encode(username, charset));
                if (null != password) sb.append(COLON).append(encode(password, charset));
                sb.append(AT);
            }
            for (int i = 0 ; i < hosts.size(); i++) {
                Tuple2<String, Integer> tuple = hosts.get(i);
                sb.append(encode(tuple.v1(), charset));
                if (null != tuple.v2()) sb.append(COLON).append(tuple.v2);
                if (i != hosts.size() - 1) sb.append(COMMA);
            }
            if (paths.size() > 0) {
                StringBuilder pathBuilder = new StringBuilder();
                for (String p : paths) {
                    pathBuilder.append(SLASH).append(encode(p, charset));
                }
                sb.append(pathBuilder);
            }
            if (parameters.size() > 0) {
                sb.append(QUESTION_MARK);
                List<Map.Entry<String, Object>> entries = new ArrayList<>(parameters.entrySet());
                for (int i = 0; i < entries.size(); i++) {
                    Map.Entry<String, Object> entry = entries.get(i);
                    sb.append(encode(entry.getKey(), charset));
                    if (null != entry.getValue())
                        sb.append(EQUAL).append(encode(entry.getValue().toString(), charset));
                    if (i != entries.size() - 1) sb.append(AMPERSAND);
                }
            }
            if (null != fragment) sb.append(JINHAO).append(encode(fragment, charset));
            return new URIs(sb.toString());
        }
    }
}
