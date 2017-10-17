package com.tdl.study.core.utils;

import org.jooq.lambda.tuple.Tuple2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class URIs {
    private String uri;
    static String SLASH = "/", BACKSLASH = "\\", COMMA = ",", SEMICOLON = ";", COLON = ":",
            AT = "@", QUESTION_MARK = "?", AMPERSAND = "&", EQUAL = "=";
    private String schema;
    private String username;
    private String password;
    private List<Tuple2<String, Integer>> hosts = new ArrayList<>();
    private List<String> paths = new ArrayList<>();
    private Map<String, String> parameters = new HashMap<>();
    public URIs(String uri) {
        this.uri = uri;
        parse();
    }

    private void parse() {
        String remain = uri;
        String separator = null;
        // schema
        separator = COLON + SLASH + SLASH;
        int index = remain.indexOf(separator);
        schema = remain.substring(0, index);
        remain = remain.substring(index + separator.length());
        // authority
        separator = AT;
        index = remain.indexOf(separator);
        String authority = remain.substring(0, index);
        remain = remain.substring(index + separator.length());
        separator = COLON;
        index = authority.indexOf(separator);
        username = authority.substring(0, index);
        password = authority.substring(index + separator.length());
        // hots
        separator = SLASH;
        index = remain.indexOf(separator);
        String hostsAndPorts = remain.substring(0, index);
        parseHostAndPort(hostsAndPorts);
        remain = remain.substring(index + separator.length());
        // paths
        separator = QUESTION_MARK;
        index = remain.indexOf(separator);
        String paths = remain.substring(0, index);
        remain = remain.substring(index + separator.length());
        this.paths = Arrays.asList(paths.split(SLASH));
        // parameters
        String parameters = remain;
        separator = AMPERSAND;
        Stream.of(parameters.split(separator)).forEach(pair -> {
            int idx = pair.indexOf(EQUAL);
            this.parameters.put(pair.substring(0, idx), pair.substring(idx + EQUAL.length()));
        });
        // zookeeper://username:password@172.16.16.232:2181,172.16.16.232:2182/namespace/table?param1=xxx&param2=yyy
        // jdbc:mysql://
    }

    private void parseHostAndPort(String hostAndPort) {
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

    public static URIs parse(String uri) {
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

    /**
     * starts from zero
     * @param level
     * @return
     */
    public String pathAt(int level) {
        return level >= paths.size() ? null :paths.get(level);
    }



    public static void main(String[] args) {
        URIs uri = parse("zookeeper://username:password@172.16.16.232:2181,172.16.16.232:2182/namespace/table?param1=xxx&param2=yyy");
        System.out.println(uri);

    }


    @Override
    public String toString() {
        List<String> hostAndPort = hosts.stream().map(t -> t.v1() + ":" + t.v2()).collect(Collectors.toList());
        String s =Strings.join(",", hostAndPort);
        return schema + COLON + SLASH + SLASH + username + COLON + password + AT
        + s;
    }
}
