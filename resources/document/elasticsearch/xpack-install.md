
### 安装
1. root@pseudo elasticsearch]# ./bin/elasticsearch-plugin install file:///taidl/tars/x-pack-5.2.0.zip 确认权限
2. 在elasticsearch.yml和kibana.yml中配置全能的特性，因为默认全部特性都启用的
```yaml
xpack.security.enabled: false
xpack.monitoring.enabled: false
xpack.graph.enabled: false
xpack.watcher.enabled: true
#xpack.reporting.enabled: false # with error
```

### 原理 
watcher 包含四个部分
schedule
query
condition
actions


#### input
有四种
simple 加载静态数据到执行上下文 
search 加载search结果到执行上下文 https://www.elastic.co/guide/en/x-pack/5.2/input-search.html
http   加载http请求的结果到执行上下文
chain  加载一系列输入到执行上下文,支持多个
```text
"input" : {
  "search" : {
    "request" : {
      "indices" : [ "logs" ], # 可以有表达式 "indices" : [ "<stock-quotes-{now/d}>" ],
      "types" : [ "event" ],
      "body" : {
        "query" : { "match_all" : {}}
      }
    }
    ,
    "extract": ["hits.total"]
  }
}
```
#### triggers
目前仅支持基于时间调度(schedule)的触发器
hourly
```text
# 每小时的30分执行一下 12:30 13:30 14:30 ...
{
  "trigger" : {
    "schedule" : {
      "hourly" : { "minute" : 30 }
    }
  }
}

# 每小时的指定分钟执行一下 12:00 12:15 12:30 12:45 13:00... 
{
  "trigger" : {
    "schedule" : {
      "hourly" : { "minute" : [0, 15, 30, 45] }
    }
  }
}
```
daily
weekly
monthly
yearly
cron
```text
# linux 标准写法
<seconds> <minutes> <hours> <day_of_month> <month> <day_of_week> [year]
13 5 9 * * ?  #Trigger at 9:05：13 AM every day.
```
interval
间隔执行 单位有 s秒 m分 h时 d天 w周
```text
# 间隔5分钟执行一下
{
  "trigger" : {
    "schedule" : {
      "interval" : "5m"
    }
  }
}
```
#### conditions
当watch被触发后，conditions决定是否执行相应的action，有以下几种
- always (默认值)
- never
- compare
- array_compare
- script

script,compare,array_compare可以使用执行上下文, ctx.payload.*
compare 支持如下运算符 eq not_eq gt gte lt lte
```text
# 一直执行
"condition" : {
  "always" : {}
}
# 永不执行
"condition" : {
  "never" : {}
}

# 当匹配的多于5个时执行
"condition" : {
    "compare" : { "ctx.payload.hits.total" : { "gt" : 5 }}
}

#可以引用的内容
ctx.watch_id   The id of the watch that is currently executing.
ctx.execution_time   The time execution of this watch started.
ctx.trigger.triggered_time  The time this watch was triggered.
ctx.trigger.scheduled_time  The time this watch was supposed to be triggered.
ctx.metadata.* Any metadata associated with the watch.
ctx.payload.*  The payload data loaded by the watch’s input.
```

#### actions
email action 发送邮件
webhook action 发送一个http/https请求
index action 写一个文档到index/type
logging action 写到es日志中
hipCat action  一种内部群聊工具
slack action 一种群聊工具
pagerDuty action 一款IT警报系统工具,通告服务
Jira action  创建一个Jira问题


##### 一些示例
https://github.com/elastic/examples