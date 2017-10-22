package com.tdl.study.elasticsearch;

import java.io.IOException;

public class Main2 {
    public static void main(String[] args) throws IOException {
/*
        String index = "percolate";
        
        Settings settings = Settings.builder()
                .put("cluster.name", "pseudo-elasticsearch")
                .put("client.transport.sniff", true)
//                .put("client.transport.ignore_cluster_name", true)
                .build();

        System.out.println("=============" + InetAddress.getByName("192.168.1.104"));
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("192.168.1.104"), 9201),
                new InetSocketTransportAddress(InetAddress.getByName("192.168.1.104"), 9202),
                new InetSocketTransportAddress(InetAddress.getByName("192.168.1.104"), 9203));
*/


        /**Before the percolate query can be used an percolator mapping should be added
         * and a document containing a percolator query should be indexed:*/
        // create an index with a percolator field with the name 'query':
/*
        client.admin().indices().prepareCreate(index)
                .addMapping("query", "query", "type=percolator")
                .addMapping("docs", "content", "type=text")
                .get();

        //This is the query we're registering in the percolator
        QueryBuilder qb = termQuery("content", "amazing");

        //Index the query = register it in the percolator
        client.prepareIndex(index, "query", "myDesignatedQueryName")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("query", qb) // Register the query
                        .endObject())
                .setRefreshPolicy(IMMEDIATE) // Needed when the query shall be available immediately
                .get();
*/



        //Build a document to check against the percolator
/*
        XContentBuilder docBuilder = XContentFactory.jsonBuilder().startObject();
        docBuilder.field("content", "This is amazing!");
        docBuilder.endObject(); //End of the JSON root object

        PercolateQueryBuilder percolateQuery = new PercolateQueryBuilder("query", "docs", docBuilder.bytes());

        // Percolate, by executing the percolator query in the query dsl:
//        SearchResponse response = client().prepareSearch(index)
        SearchResponse response = client.prepareSearch(index)
                .setQuery(percolateQuery)
                .get();
        //Iterate over the results
        for(SearchHit hit : response.getHits()) {
            // Percolator queries as hit
            System.out.println(hit.getSourceAsString());
        }

        client.close();
*/

        /*//把查询条件添加到索引中，myDesignatedQueryName为定义的查询名
        QueryBuilder qb = termQuery("message", "bonsai");
        client.prepareIndex("percolate", "percolator", "myDesignatedQueryName")
                .setSource(jsonBuilder().startObject()
                        // Register the query，添加查询记录
                        .field("query", qb)
                        .endObject())
                .setRefreshPolicy(IMMEDIATE)
                .execute().actionGet();
        //上面的term查询定义名为：myDesignatedQueryName

        XContentBuilder contentBuilder = jsonBuilder().startObject();
        contentBuilder.field("doc").startObject();
        contentBuilder.field("message", "A new bonsai tree in the office");
        contentBuilder.endObject();
        contentBuilder.endObject();

        // Percolate 查询
        PercolateQueryBuilder builder = new PercolateQueryBuilder("field", "string", "1", "string")
//        PercolatorPlugin*/
    }



    public static void fun2(String[] args) {

    }
}
