### 安装

解压后，要创建相应data、log目录，并且创建普通帐户 useradd es
把es根目录，数据目录，日志目录 修改所有者 chown -r es elasticsearch xx_data xx_log
修改config下的配置文件，指定host datadir logdir

修改/etc/security/limits.conf，添加如下内容(可能要重启才能生效，还没找到立刻生效的方法)
```text
*       soft    nofile  65536
*       hard    nofile  131072
*       soft    nproc   2048
*       hard    nproc   4096
```


vim /etc/sysctl.conf
添加vm.max_map_count=655360
再应用 sysctl -p

切换到es用户，调用bin下的启动脚本即可


可以像zookeeper 一样配置单机多节点集群


### 一份教程
http://blog.csdn.net/cnweike/article/details/33736429

percolate-query
https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-specialized-queries.html#java-query-percolate-query
https://www.elastic.co/guide/en/elasticsearch/reference/5.6/query-dsl-percolate-query.html

官方 权威指南
https://www.elastic.co/guide/cn/elasticsearch/guide/current/intro.html
https://www.gitbook.com/book/looly/elasticsearch-the-definitive-guide-cn/details


### 基本操作
- 集群健康<br/>
[es@taidl104 taidl]$ curl '192.168.1.104:9201/_cat/health?v'
epoch      timestamp cluster              status node.total node.data shards pri relo init unassign pending_tasks max_task_wait_time active_shards_percent 
1507356222 14:03:42  pseudo-elasticsearch green           3         3     40  20    0    0        0             0                  -                100.0% 
- 集群节点列表<br/>
[es@taidl104 taidl]$ curl '192.168.1.104:9201/_cat/nodes?v'
host          ip            heap.percent ram.percent load node.role master name   
192.168.1.104 192.168.1.104           17          45 0.46 d         *      node_1 
192.168.1.104 192.168.1.104           11          45 0.46 d         m      node_3 
192.168.1.104 192.168.1.104           16          45 0.46 d         m      node_2 
- 创建一个名为customer的索引<br/>
[es@taidl104 taidl]$ curl -XPUT '192.168.1.104:9201/customer?pretty'
{
  "acknowledged" : true
}
- 列出所有索引<br/>
[es@taidl104 taidl]$ curl '192.168.1.104:9201/_cat/indices?v'
health status index                   pri rep docs.count docs.deleted store.size pri.store.size 
green  open   percolate                 5   1          4            0     23.4kb         11.7kb 
green  open   my-index                  5   1          1            0      6.8kb          3.4kb 
green  open   test-index-1              5   1          0            0      1.5kb           795b 
green  open   customer                  5   1          0            0      1.2kb           650b
- 把一个文档索引到customer的external类型下，其id为1，用PUT方法<br/>
[es@taidl104 taidl]$ curl -XPUT '192.168.1.104:9201/customer/external/1?pretty' -d '{"name": "John Doe"}'
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "1",
  "_version" : 1,
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "created" : true
}
- 把一个文档索引到customer的external类型下，其id为es自动生成，需要用POST方法<br/>
[es@taidl104 taidl]$ curl -XPOST '192.168.1.104:9201/customer/external?pretty' -d '{"name": "John Smith"}'
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "AV72NOYmMyHFnQNragVK",
  "_version" : 1,
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "created" : true
}
- 把刚刚索引的文档取出来 _source字段为原始文档<br/>
[es@taidl104 taidl]$ curl -XGET '192.168.1.104:9201/customer/external/1?pretty'
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "1",
  "_version" : 1,
  "found" : true,
  "_source" : {
    "name" : "John Doe"
  }
}
- 使用同一个id提交文档时会替换原有文档，使用不同id时会新增一个文档<br/>
[es@taidl104 taidl]$ curl -XPUT '192.168.1.104:9201/customer/external/1?pretty' -d '{"name": "New York"}'
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "1",
  "_version" : 2,
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "created" : false    
}
[es@taidl104 taidl]$ curl -XGET '192.168.1.104:9201/customer/external/1?pretty'
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "1",
  "_version" : 2,
  "found" : true,
  "_source" : {
    "name" : "New York"
  }
}
- 更新文档<br/>
[es@taidl104 taidl]$ curl -XPOST '192.168.1.104:9201/customer/external/1/_update' -d '{"doc":{"name":"Updated Name"}}'
  {"_index":"customer","_type":"external","_id":"1","_version":3,"_shards":{"total":2,"successful":2,"failed":0}}
- 更新的时候可以同时增加字段<br/>
[es@taidl104 taidl]$ GET '192.168.1.104:9201/customer/external/1?pretty'
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "1",
  "_version" : 4,
  "found" : true,
  "_source" : {
    "name" : "John Smith"
  }
}
[es@taidl104 taidl]$ curl -XPOST '192.168.1.104:9201/customer/external/1/_update' -d '{"doc":{"name":"John Smith", "age":42}}'
{"_index":"customer","_type":"external","_id":"1","_version":5,"_shards":{"total":2,"successful":2,"failed":0}}[es@taidl104 taidl]$ GET '192.168.1.104:9201/customer/external/1?pretty'
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "1",
  "_version" : 5,
  "found" : true,
  "_source" : {
    "name" : "John Smith",
    "age" : 42
  }
}
- 使用script更新<br/>
[es@taidl104 taidl]$ curl -XPOST '192.168.1.104:9201/customer/external/1/_update?pretty' -d '{"script":"ctx._source.age += 5"}'
- 根据id删除文档<br/>
[es@taidl104 taidl]$ curl -XDELETE '192.168.1.104:9201/customer/external/1'
{"found":true,"_index":"customer","_type":"external","_id":"1","_version":6,"_shards":{"total":2,"successful":2,"failed":0}}
- 删除符合查询条件的文档<br/>
[es@taidl104 taidl]$ curl -XDELETE '192.168.1.104:9201/customer/external/_query?pretty' -d '
{
 "query": {
   "match": {
     "name" : "John Smith"
   }
 }
}'
- 批量操作<br/>
批量插入<br/>
[es@taidl104 taidl]$ curl -XPOST '192.168.1.104:9201/customer/external/_bulk?pretty' -d '
> {"index":{"_id":"1"}}
> {"name": "John Doe" }
> {"index":{"_id":"2"}}
> {"name": "Jane Doe" }
> '

{
  "took" : 18,
  "errors" : false,
  "items" : [ {
    "index" : {
      "_index" : "customer",
      "_type" : "external",
      "_id" : "1",
      "_version" : 1,
      "_shards" : {
        "total" : 2,
        "successful" : 2,
        "failed" : 0
      },
      "status" : 201
    }
  }, {
    "index" : {
      "_index" : "customer",
      "_type" : "external",
      "_id" : "2",
      "_version" : 1,
      "_shards" : {
        "total" : 2,
        "successful" : 2,
        "failed" : 0
      },
      "status" : 201
    }
  } ]
}
[es@taidl104 taidl]$ curl -XGET '192.168.1.104:9201/customer/external/1?pretty'     
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "1",
  "_version" : 1,
  "found" : true,
  "_source" : {
    "name" : "John Doe"
  }
}
[es@taidl104 taidl]$ curl -XGET '192.168.1.104:9201/customer/external/2?pretty'
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "2",
  "_version" : 1,
  "found" : true,
  "_source" : {
    "name" : "Jane Doe"
  }
}
<br/>更新第一个，删除第二个<br/>
[es@taidl104 taidl]$ curl -XPOST '192.168.1.104:9201/customer/external/_bulk?pretty' -d '
{"update":{"_id":"1"}}
        {"doc": {"name":"John Doe becomes Jane Doe"} }
        {"delete":{"_id":"2"}}
        '                    
{
  "took" : 138,
  "errors" : false,
  "items" : [ {
    "update" : {
      "_index" : "customer",
      "_type" : "external",
      "_id" : "1",
      "_version" : 2,
      "_shards" : {
        "total" : 2,
        "successful" : 2,
        "failed" : 0
      },
      "status" : 200
    }
  }, {
    "delete" : {
      "_index" : "customer",
      "_type" : "external",
      "_id" : "2",
      "_version" : 2,
      "_shards" : {
        "total" : 2,
        "successful" : 2,
        "failed" : 0
      },
      "status" : 200,
      "found" : true
    }
  } ]
}
[es@taidl104 taidl]$ curl -XGET '192.168.1.104:9201/customer/external/2?pretty'
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "2",
  "found" : false
}
[es@taidl104 taidl]$ curl -XGET '192.168.1.104:9201/customer/external/1?pretty'
{
  "_index" : "customer",
  "_type" : "external",
  "_id" : "1",
  "_version" : 2,
  "found" : true,
  "_source" : {
    "name" : "John Doe becomes Jane Doe"
  }
}
- 加载json文本到es中<br/>
curl -XPOST '192.168.1.104:9201/bank/account/_bulk?pretty' --data-binary @accounts.json
....可以看到成功加载了1000条数据
[es@taidl104 ~]$ curl '192.168.1.104:9201/_cat/indices?pretty'
green open bank                    5 1   1000   0 884.3kb 442.1kb 
### 搜索文档
- 检出所有文档<br/>
[es@taidl104 ~]$ curl '192.168.1.104:9201/bank/_search?q=*&pretty'
<br/>或<br/>
curl -XPOST '192.168.1.104:9201/bank/_search?pretty' -d '{"query":{"match_all":{}}}'
</br>检出并按字段account_number排序</br>
[es@taidl104 ~]$ curl -XPOST '192.168.1.104:9201/bank/_search?pretty' -d '
{
  "query": {
    "bool": {
      "must": {
        "match_all": {}
      }
    }
  },
  "from": 900,"size": 100,
  "sort": {
    "account_number": {
      "order": "asc"
    }
  }
}
'


### percolator query (趴克累特)
1. 为 my-type的message 字段创建一个带有mapping映射、名为my-index的索引<br/>
curl -XPUT '192.168.1.104:9201/my-index' -d '{"mappings":{"my-type":{"properties":{"message":{"type":"string"}}}}}'
响应：<br/>
{"acknowledged":true}

2. 在percolator中注册一个查询<br/>
curl -XPUT '192.168.1.104:9201/my-index/.percolator/1' -d '{"query":{"match":{"message":"bonsai tree"}}}'
<br/>响应：<br/>
{"_index":"my-index","_type":".percolator","_id":"1","_version":1,"_shards":{"total":2,"successful":2,"failed":0},"created":true}

3. 若插入一个匹配的doc<br/>
 curl -XGET '192.168.1.104:9201/my-index/my-type/_percolate' -d '{"doc":{"message":"A new bonsai tree in the office"}}'
 <br/>响应：matches字段表明匹配到了哪些查询<br/>
{"took":11,"_shards":{"total":5,"successful":5,"failed":0},"total":1,"matches":[{"_index":"my-index","_id":"1"}]}

4. 若插入一个为匹配的doc<br/>
curl -XGET '192.168.1.104:9201/my-index/my-type/_percolate' -d '{"doc":{"message":"A new bonsaddi tddree in the office"}}'
<br/>响应： 没有匹配的查询，matches字段为空<br/>
{"took":10,"_shards":{"total":5,"successful":5,"failed":0},"total":0,"matches":[]}

5. 可以看到用的是GET方法，文档并不会被储存，只是会返回percolate的结果，匹配多个的话matches会包含多条匹配的结果<br/>

6. Percolate queries are stored as documents in a specific format and in an arbitrary index under a reserved type with the name .percolator. The query itself is placed as is in a JSON object under the top level field query.
 <br/>Percolate查询以一种特殊的格式作为一个文档存储在任意一种索引下的保留类型'.percolate'类型中;查询条件本身是以JSON格式放在顶层的query字段中

7. 删除一个percolate查询<br/>
[es@taidl104 ~]$ curl -XDELETE '192.168.1.104:9201/my-index/.percolator/3?pretty'

8. 只返回匹配的查询的个数，不包含具体内容<br/>
[es@taidl104 ~]$ curl -XGET '192.168.1.104:9201/my-index/my-type/_percolate/count?pretty' -d '{"doc":{"message":"A new bonsai tree in the office"}}'
{
  "took" : 7,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "total" : 2
}

9. 对已存在的文档执行percolate query<br/>
[root@pseudo kibana]# curl -XPUT '172.16.17.203:39200/tdl_percolate_test/type_1/doc1?pretty' -d '{"field_11":"elastic search percolate doc"}'
{
  "_index" : "tdl_percolate_test",
  "_type" : "type_1",
  "_id" : "doc1",
  "_version" : 1,
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "created" : true
}
[root@pseudo kibana]# curl -XPUT '172.16.17.203:39200/tdl_percolate_test/type_1/doc2?pretty' -d '{"field_11":"elasticsearch is a good search engine"}'
{
  "_index" : "tdl_percolate_test",
  "_type" : "type_1",
  "_id" : "doc2",
  "_version" : 1,
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "created" : true
}
[root@pseudo kibana]# curl -XPUT '172.16.17.203:39200/tdl_percolate_test/type_1/doc2?pretty' -d '{"field_11":"elasticsearch is a good search engine, key: keyword_11"}'
{
  "_index" : "tdl_percolate_test",
  "_type" : "type_1",
  "_id" : "doc2",
  "_version" : 2,
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "created" : false
}
[root@pseudo kibana]# curl -XPUT '172.16.17.203:39200/tdl_percolate_test/type_1/doc1?pretty' -d '{"field_12":"a doc with keyword_12"}'
{
  "_index" : "tdl_percolate_test",
  "_type" : "type_1",
  "_id" : "doc1",
  "_version" : 2,
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "created" : false
}
[root@pseudo kibana]# curl -XPUT '172.16.17.203:39200/tdl_percolate_test/type_1/doc1?pretty' -d '{"field_11":"elastic search percolate doc"}'
{
  "_index" : "tdl_percolate_test",
  "_type" : "type_1",
  "_id" : "doc1",
  "_version" : 3,
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "created" : false
}
[root@pseudo kibana]# curl -XPUT '172.16.17.203:39200/tdl_percolate_test/type_1/doc3?pretty' -d '{"field_12":"a doc with keyword_12"}'
{
  "_index" : "tdl_percolate_test",
  "_type" : "type_1",
  "_id" : "doc3",
  "_version" : 1,
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "created" : true
}
[root@pseudo kibana]# 
[root@pseudo kibana]# 
[root@pseudo kibana]# curl -XPUT '172.16.17.203:39200/tdl_percolate_test/type_2/doc3?pretty' -d '{"field_22":"a doc in type_2 with keyword_22"}'
{
  "_index" : "tdl_percolate_test",
  "_type" : "type_2",
  "_id" : "doc3",
  "_version" : 1,
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "created" : true
}
[root@pseudo kibana]# curl -XPUT '172.16.17.203:39200/tdl_percolate_test/type_2/doc4?pretty' -d '{"field_21":"a doc in type_2 with keyword_21"}'
{
  "_index" : "tdl_percolate_test",
  "_type" : "type_2",
  "_id" : "doc4",
  "_version" : 1,
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "created" : true
}
[root@pseudo kibana]# 
[root@pseudo kibana]# 
[root@pseudo kibana]# curl -XGET '172.16.17.203:39200/tdl_percolate_test/type_2/doc4/_percolate?pretty'
{
  "took" : 407,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "total" : 1,
  "matches" : [ {
    "_index" : "tdl_percolate_test",
    "_id" : "21"
  } ]
}
[root@pseudo kibana]# 
[root@pseudo kibana]# 
[root@pseudo kibana]# 
[root@pseudo kibana]# 
[root@pseudo kibana]# curl -XGET '172.16.17.203:39200/tdl_percolate_test/type_2/doc1/_percolate?pretty'
{
  "error" : {
    "root_cause" : [ {
      "type" : "document_missing_exception",
      "reason" : "[type_2][doc1]: document missing"
    } ],
    "type" : "document_missing_exception",
    "reason" : "[type_2][doc1]: document missing"
  },
  "status" : 404
}
[root@pseudo kibana]# curl -XGET '172.16.17.203:39200/tdl_percolate_test/type_1/doc1/_percolate?pretty'
{
  "took" : 52,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "total" : 0,
  "matches" : [ ]
}
[root@pseudo kibana]# curl -XGET '172.16.17.203:39200/tdl_percolate_test/type_2/doc1/_percolate?pretty'
{
  "error" : {
    "root_cause" : [ {
      "type" : "document_missing_exception",
      "reason" : "[type_2][doc1]: document missing"
    } ],
    "type" : "document_missing_exception",
    "reason" : "[type_2][doc1]: document missing"
  },
  "status" : 404
}
[root@pseudo kibana]# curl -XGET '172.16.17.203:39200/tdl_percolate_test/type_1/doc2/_percolate?pretty'
{
  "took" : 36,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "total" : 2,
  "matches" : [ {
    "_index" : "tdl_percolate_test",
    "_id" : "13"
  }, {
    "_index" : "tdl_percolate_test",
    "_id" : "11"
  } ]
}
[root@pseudo kibana]# 



2.x版本文档
https://www.elastic.co/guide/en/elasticsearch/reference/2.4/search-percolate.html#search-percolate

```json
{
    "mappings": { //定义字段及其类型
      "my-type": {
        "properties": { //配置字段，可以有多个字段
          "message": {
            "type": "string"   //指定字段的数据类型为string，这时message只能存储string类型的数据
          }
        }
      }
    }
  }
```

