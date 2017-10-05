package com.tdl.study.elasticsearch;

import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;

import java.io.IOException;
import java.net.InetAddress;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class Main {
    /*

    curl -XPUT '192.168.1.104:9201/percolate' -d '{
        "mappings": {
          "my_type": {
            "properties":{
              "message":{
               "type":"string"
              }
             }
           }

          }
        }'

    */
    public static void main(String[] args) throws IOException {
        String indexName = "percolate";
        String indexType = "message";

        Settings settings = Settings.builder()
                .put("cluster.name", "pseudo-elasticsearch")
//                .put("client.transport.sniff", true)
//                .put("client.transport.ignore_cluster_name", true)
                .build();

        TransportClient client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.104"), 9301))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.104"), 9302))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.104"), 9303));

        //This is the query we're registering in the percolator
        QueryBuilder qb = termQuery("message", "amazing");

        //Index the query = register it in the percolator
//        client.prepareIndex(indexName, ".percolator", "666")
        client.prepareIndex(indexName, "xxx", "666")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("query", qb) // Register the query
                        .endObject())
                .setRefresh(true) // Needed when the query shall be available immediately
                .execute().actionGet();

        //Build a document to check against the percolator
        XContentBuilder docBuilder = XContentFactory.jsonBuilder().startObject();
        docBuilder.field("doc").startObject(); //This is needed to designate the document
        docBuilder.field("message", "This is amazing!");
        docBuilder.endObject(); //End of the doc field
        docBuilder.endObject(); //End of the JSON root object

        //Percolate 查询
        PercolateResponse response = client.preparePercolate()
                .setIndices(indexName)
                .setDocumentType(indexType)
                .setSource(docBuilder)
                .execute().actionGet();

        //Iterate over the results
        for(PercolateResponse.Match match : response) {
            //Handle the result which is the name of
            //the query in the percolator
            System.out.println("percolate ID: " + match.getId());
            System.out.println("percolate Index name: " + match.getIndex());
            System.out.println("");
        }

        client.close();
    }

}
