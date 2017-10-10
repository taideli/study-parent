package com.tdl.study.elasticsearch;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

public class RestMain {
    public static void main(String[] args) throws IOException {
        HttpHost host = new HttpHost("172.16.16.232", 9200, "http");
        RestClient client = RestClient.builder(host).setMaxRetryTimeoutMillis(5000).build();

        Response response = client.performRequest("GET", "/_cluster/health");
        System.out.println(EntityUtils.toString(response.getEntity()));

//        response = client.performRequest("GET", "/", Collections.singletonMap("pretty", "true"));
//        System.out.println(EntityUtils.toString(response.getEntity()));

        // index a doc
        String doc =
                "{\n" +
                "    \"user\" : \"kimchy\",\n" +
                "    \"post_date\" : \"" + new Date().toString() + "\",\n" +
                "    \"message\" : \"trying out Elasticsearch\"\n" +
                "}";
        HttpEntity entity = new NStringEntity(doc, ContentType.APPLICATION_JSON);
//        response = client.performRequest("PUT", "/twitter/tweet/1", Collections.<String, String>emptyMap(), entity);
        response = client.performRequest("POST", "/twitter/tweet", Collections.<String, String>emptyMap(), entity);
        System.out.println(EntityUtils.toString(response.getEntity()));

        client.close();
    }
}
