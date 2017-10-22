package com.tdl.study.elasticsearch.mapping;


import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsAction;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class Employee {
    public static XContentBuilder mapping() {
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
                    .startObject()
                        .startObject("name")
                            .field("type", "string")
                            .field("enabled", true)
                        .endObject()
                        .startObject("age")
                            .field("type", "integer")
                        .endObject()
                        .startObject("gender")
                            .field("type", "string")
                        .endObject()
                        .startObject("address")
                            .field("type", "string")
                        .endObject()
                    .endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }


    public static void main(String[] args) throws UnknownHostException {
        String indices = "test-index-1";
        String type = "test-type-1";
        PutMappingRequest mappingRequest = Requests.putMappingRequest(indices).type(type).source(mapping());

        createIfIndexNotExist(indices);

        client().admin().indices().putMapping(mappingRequest).actionGet();
    }

    private static void createIfIndexNotExist(String index) throws UnknownHostException {
        client().admin().indices().exists(new IndicesExistsRequest(index), new ActionListener<IndicesExistsResponse>() {
            @Override
            public void onResponse(IndicesExistsResponse indicesExistsResponse) {
                boolean exist = indicesExistsResponse.isExists();
                if (!exist) {
                    try {
                        client().admin().indices().create(new CreateIndexRequest(index), new ActionListener<CreateIndexResponse>() {
                            @Override
                            public void onResponse(CreateIndexResponse createIndexResponse) {
                                if (!createIndexResponse.isAcknowledged()) {
                                    System.out.println("create failed");
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                System.out.println("create failed, for " + e.getMessage());
                            }
                        });
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    private static Client client() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "pseudo-elasticsearch")
//                .put("client.transport.sniff", true)
//                .put("client.transport.ignore_cluster_name", true)
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.104"), 9301))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.104"), 9302))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.104"), 9303));

        return client;
    }

}
