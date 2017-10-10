package com.tdl.study.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.percolator.PercolateQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class EsMain {

    public static void main(String[] args) throws IOException {

        String index = "tdl_percolate_java_index";
        // create a client
        Settings settings = Settings.builder().put("cluster.name", "pseudo").build();
        TransportClient client = new PreBuiltTransportClient(settings);
//        client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("172.16.16.232", 9200)));
        client.addTransportAddresses(new InetSocketTransportAddress(
                new InetSocketAddress("172.16.16.232", 9300))); // notes by taidl@2017/10/10_12:02 9300 not use 9200

        // Before the percolate query can be used an percolator mapping should be added and a document containing a percolator query should be indexed:
        // create an index with a percolator field with the name 'query':
        client.admin().indices().prepareCreate(index)
                .addMapping("query", "query", "type=percolator")
                .addMapping("docs", "content", "type=text")
                .get();


        //This is the query we're registering in the percolator
        QueryBuilder qb = termQuery("content", "amazing");

        //Index the query = register it in the percolator
        client.prepareIndex(index, "query", "id_1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("query", qb) // Register the query
                        .endObject())
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE) // Needed when the query shall be available immediately
                .get();
        client.prepareIndex(index, "query", "id_2")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("query", termQuery("content", "another keyword")) // Register the query
                        .endObject())
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE) // Needed when the query shall be available immediately
                .get();

        //Build a document to check against the percolator
        XContentBuilder docBuilder = XContentFactory.jsonBuilder().startObject();
        docBuilder.field("content", "This is amazing!, and has another keyword");
        docBuilder.endObject(); //End of the JSON root object

        PercolateQueryBuilder percolateQuery =
                new PercolateQueryBuilder("query", "docs", docBuilder.bytes());

        // Percolate, by executing the percolator query in the query dsl:
        SearchResponse response = client.prepareSearch(index)
                .setQuery(percolateQuery)
        .get();
        //Iterate over the results
        for(SearchHit hit : response.getHits()) {
            // Percolator queries as hit
            System.out.println();
            System.out.println("ID:     " + hit.getId());
            System.out.println("Index:  " + hit.getIndex());
            System.out.println("Source: " + hit.getSourceAsString());
        }

        client.close();
    }

}
