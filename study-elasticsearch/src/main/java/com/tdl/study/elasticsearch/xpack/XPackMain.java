package com.tdl.study.elasticsearch.xpack;

import jdk.nashorn.internal.parser.JSONParser;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.xpack.XPackClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.elasticsearch.xpack.notification.email.EmailTemplate;
import org.elasticsearch.xpack.watcher.WatcherBuild;
import org.elasticsearch.xpack.watcher.WatcherState;
import org.elasticsearch.xpack.watcher.actions.ActionStatus;
import org.elasticsearch.xpack.watcher.actions.email.EmailAction;
import org.elasticsearch.xpack.watcher.client.WatchSourceBuilder;
import org.elasticsearch.xpack.watcher.client.WatchSourceBuilders;
import org.elasticsearch.xpack.watcher.client.WatcherClient;
import org.elasticsearch.xpack.watcher.condition.CompareCondition;
import org.elasticsearch.xpack.watcher.condition.ScriptCondition;
import org.elasticsearch.xpack.watcher.execution.ActionExecutionMode;
import org.elasticsearch.xpack.watcher.input.search.SearchInput;
import org.elasticsearch.xpack.watcher.support.search.WatcherSearchTemplateRequest;
import org.elasticsearch.xpack.watcher.support.xcontent.XContentSource;
import org.elasticsearch.xpack.watcher.transport.actions.ack.AckWatchResponse;
import org.elasticsearch.xpack.watcher.transport.actions.activate.ActivateWatchResponse;
import org.elasticsearch.xpack.watcher.transport.actions.delete.DeleteWatchResponse;
import org.elasticsearch.xpack.watcher.transport.actions.execute.ExecuteWatchResponse;
import org.elasticsearch.xpack.watcher.transport.actions.get.GetWatchRequest;
import org.elasticsearch.xpack.watcher.transport.actions.get.GetWatchResponse;
import org.elasticsearch.xpack.watcher.transport.actions.put.PutWatchResponse;
import org.elasticsearch.xpack.watcher.transport.actions.service.WatcherServiceResponse;
import org.elasticsearch.xpack.watcher.transport.actions.stats.WatcherStatsResponse;
import org.elasticsearch.xpack.watcher.trigger.TriggerBuilders;
import org.elasticsearch.xpack.watcher.trigger.schedule.Schedules;
import org.elasticsearch.xpack.watcher.watch.WatchStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.builder.SearchSourceBuilder.searchSource;
import static org.elasticsearch.xpack.watcher.actions.ActionBuilders.emailAction;
import static org.elasticsearch.xpack.watcher.actions.ActionBuilders.indexAction;
import static org.elasticsearch.xpack.watcher.client.WatchSourceBuilders.watchBuilder;
import static org.elasticsearch.xpack.watcher.input.InputBuilders.searchInput;
import static org.elasticsearch.xpack.watcher.trigger.TriggerBuilders.schedule;
import static org.elasticsearch.xpack.watcher.trigger.schedule.Schedules.cron;
import static org.elasticsearch.xpack.watcher.trigger.schedule.Schedules.interval;

public class XPackMain {

    public static void main(String[] args) {
        String[] indices = new String[]{"logs"};
        String[] types = new String[]{"event"};
//        String watchId = "tdl_watcher_java";
        String watchId = "tdl_query_match_watcher";

        TransportClient client = new PreBuiltXPackTransportClient(
                Settings.builder()
                        .put("cluster.name", "pseudo-elasticsearch")
                        .build()
//        ).addTransportAddresses(
        ).addTransportAddress(
                new InetSocketTransportAddress(
                        new InetSocketAddress("172.16.16.232", 9300) // notes by taidl@2017/10/13_17:16 must 9300
//                        new InetSocketAddress("192.168.1.104", 9300) // notes by taidl@2017/10/13_17:16 must 9300
                )
        );

        XPackClient xpackClient = new XPackClient(client);
        WatcherClient watcherClient = xpackClient.watcher();

        printClusterHealth(client);

//        putWatch(watcherClient, watchId, indices, types);
        getWatch(watcherClient, watchId);
//        deleteWatch(watcherClient, watchId);

        client.close();
    }

    public static void printClusterHealth(TransportClient client) {
        client.admin().cluster().health(new ClusterHealthRequest(), new ActionListener<ClusterHealthResponse>() {
            @Override
            public void onResponse(ClusterHealthResponse clusterHealthResponse) {
                System.out.println(clusterHealthResponse.toString());
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    /**
     * 用法
     * Use TriggerBuilders and Schedules classes to define the trigger
     * Use InputBuilders class to define the input
     * Use ConditionBuilders class to define the condition
     * Use ActionBuilders to define the actions
     * <p>
     * 不美观的版本
     */
    public void putWatchUnaesthetic(WatcherClient watcherClient, String watchId, String[] indices, String[] types) {
        WatchSourceBuilder wsBuilder = watchBuilder();
        // set the trigger
        wsBuilder.trigger(schedule(cron("0 0/1 * * * ?")));

        // create the search request to use for the input
        SearchRequest request = Requests.searchRequest(indices).source(
                searchSource().query(boolQuery()
                        .must(matchQuery("response", 404))
                        .filter(rangeQuery("date").gt("{{ctx.trigger.scheduled_time}}"))
                        .filter(rangeQuery("date").lt("{{ctx.execution_time}}"))
                )
        );

        // create the search input
        SearchInput sInput = new SearchInput(new WatcherSearchTemplateRequest(indices, types, SearchType.DEFAULT,
                WatcherSearchTemplateRequest.DEFAULT_INDICES_OPTIONS, new BytesArray(request.source().toString())), null, null, null);
        // set the input
        wsBuilder.input(sInput);

        // set the condition
        wsBuilder.condition(new ScriptCondition(new Script("ctx.payload.hits.total > 1")));

        // create the email template to use for the action
        EmailTemplate.Builder emailBuilder = EmailTemplate.builder();
        emailBuilder.to("1007811421@domain.host.com");
        emailBuilder.subject("404 recently encountered");
        EmailAction.Builder emailActionBuilder = EmailAction.builder(emailBuilder.build());
        // add the action
        wsBuilder.addAction("email_someone", emailActionBuilder);

        PutWatchResponse putWatchResponse = watcherClient.preparePutWatch("my-watch")
                .setSource(wsBuilder).get();
    }

    /**
     * 用法
     * Use TriggerBuilders and Schedules classes to define the trigger
     * Use InputBuilders class to define the input
     * Use ConditionBuilders class to define the condition
     * Use ActionBuilders to define the actions
     * <p>
     * 简洁版
     */
    public static void putWatch(WatcherClient watcherClient, String watchId, String[] indices, String[] types) {
        PutWatchResponse putWatchResponse2 = watcherClient.preparePutWatch(watchId).setSource(watchBuilder()
//                        .trigger(schedule(cron("0 0/1 * * * ?")))
                        .trigger(schedule(interval("10s")))
                        .input(searchInput(new WatcherSearchTemplateRequest(indices, null, SearchType.DEFAULT,
                                WatcherSearchTemplateRequest.DEFAULT_INDICES_OPTIONS, searchSource().query(
                                matchQuery("message", "error")
//                                boolQuery().must(matchQuery("message", "error"))
//                                .filter(rangeQuery("date").gt("{{ctx.trigger.scheduled_time}}"))
//                                .filter(rangeQuery("date").lt("{{ctx.execution_time}}"))
                        ).buildAsBytes(XContentType.JSON))))
                        .condition(new CompareCondition("ctx.payload.hits.total", CompareCondition.Op.GT, 1L))
                /*.addAction("email_someone", emailAction(EmailTemplate.builder()
                        .to("1007811421@domain.host.com")
                        .subject("404 recently encountered")
                        .textBody("404 occur")
                ))*/
                        .addAction("tdl_into_es_action",
                                indexAction("tdl_alert_match_index_java", "tdl_alert_match_type_java")
//                                        .setExecutionTimeField("????")
                        )
//                        .addAction("jdbc-action", new JdbcAction.Builder()) // notes by taidl@2017/10/13_16:41 has not finished
        ).get();
        System.out.println("put watch result:\n" + putWatchResponse2.getId() + "\n===============");

    }

    public static void updateWatch(WatcherClient watcherClient, String watchId) {
        //查出来再修改
    }

    public static void getWatch(WatcherClient watcherClient, String watchId) {
        /*异步版本*/
/*        watcherClient.getWatch(new GetWatchRequest(watchId), new ActionListener<GetWatchResponse>() {
            @Override
            public void onResponse(GetWatchResponse getWatchResponse) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });*/
        // get a watch with id
        GetWatchResponse getWatchResponse = watcherClient.prepareGetWatch(watchId).get();
        // access the watch definition by access of the response
        XContentSource source = getWatchResponse.getSource();
        // explore the source
        if (null == source) {
            System.out.println("no watcher with id: " + watchId);
            return;
        }
        Map<String, Object> map = source.getAsMap();

        System.out.println("get watch " + watchId + ":===========\n");
        map.forEach((k, v) -> System.out.println(k + "   ->     " + v));
        System.out.println("end of get watch " + watchId + ":===========\n");
    }

    public static void deleteWatch(WatcherClient watcherClient, String watchId) {
        // delete a watch ,只能通过下面接口，不能用curl删除.watches下内容，删除watch不会影响之前生成的结果
        DeleteWatchResponse deleteWatchResponse = watcherClient.prepareDeleteWatch(watchId).get();
    }

    /**
     * This API enables on-demand execution of a watch stored in the .watches index. It can be used to
     * test a watch without executing all its actions or by ignoring its condition. The response contains
     * a BytesReference that represents the record that would be written to the .watcher-history index
     *
     * @param watcherClient
     * @param watchId
     */
    public static void executeWatchApi(WatcherClient watcherClient, String watchId) throws IOException {
        ExecuteWatchResponse executeWatchResponse = watcherClient.prepareExecuteWatch(watchId)
                // execute the actions, ignoring the watch condition
                .setIgnoreCondition(true)
                // a map containing alternative(可选的) input to use instead of the output of the watch's input
                .setAlternativeInput(new HashMap<>())
                // Trigger data to use (Note that "scheduled_time" is not provided to the
                // ctx.trigger by this execution method so you may want to include it here)
                .setTriggerData(new HashMap<>()) // may throw a exception
                // Simulating the "email_admin" action while ignoring its throttle state. Use
                // "_all" to set the action execution mode to all actions
                .setActionMode("_all", ActionExecutionMode.FORCE_SIMULATE)
                // If the execution of this watch should be written to the `.watcher-history`
                // index and reflected in the persisted Watch
                .setRecordExecution(false)
                // Indicates whether the watch should execute in debug mode. In debug mode the
                // returned watch record will hold the execution vars
                .setDebug(true)
                .get();

        XContentSource source = executeWatchResponse.getRecordSource();
        String actionId = source.getValue("result.actions.0.id");
    }


    public static void ackWatchApi(WatcherClient watcherClient, String watchId, String actionId) {
        GetWatchResponse getWatchResponse = watcherClient.prepareGetWatch(watchId).get();
        ActionStatus.AckStatus.State state = getWatchResponse.getStatus().actionStatus(actionId).ackStatus().state();
        // or
        AckWatchResponse ackResponse = watcherClient.prepareAckWatch(watchId).setActionIds(actionId).get();
        WatchStatus status = ackResponse.getStatus();
        ActionStatus actionStatus = status.actionStatus(actionId);
        ActionStatus.AckStatus ackStatus = actionStatus.ackStatus();
        ActionStatus.AckStatus.State ackState = ackStatus.state();
        // can acknowledge multiple actions
        AckWatchResponse ackWatchResponse = watcherClient.prepareAckWatch(watchId).setActionIds(actionId, "action-id-2").get();
        // or acknowledge all actions
        AckWatchResponse ackAllWatchResponse = watcherClient.prepareAckWatch(watchId).get();

    }

    /**
     * activate watch
     */
    public static void activateAndInactiveWatchApi(WatcherClient watcherClient, String watchId) {
        boolean active;
        // 1 get watch active status
        GetWatchResponse getWatchResponse = watcherClient.prepareGetWatch(watchId).get();
        active = getWatchResponse.getStatus().state().isActive();
        // 2  activate a watch
        ActivateWatchResponse activateWatchResponse = watcherClient.prepareActivateWatch(watchId, true).get();
        active = activateWatchResponse.getStatus().state().isActive();
        // 3  inactivate a watch
        ActivateWatchResponse inactivateWatchResponse = watcherClient.prepareActivateWatch(watchId, false).get();
        active = inactivateWatchResponse.getStatus().state().isActive();
    }

    public static void statsApi(WatcherClient watcherClient) {
        WatcherStatsResponse watcherStatsResponse = watcherClient.prepareWatcherStats().get();
        WatcherBuild build = watcherStatsResponse.getBuild();

        // The current size of the watcher execution queue
        long executionQueueSize = watcherStatsResponse.getThreadPoolQueueSize();

        // The maximum size the watch execution queue has grown to
        long executionQueueMaxSize = watcherStatsResponse.getThreadPoolQueueSize();

        // The total number of watches registered in the system
        long totalNumberOfWatches = watcherStatsResponse.getWatchesCount();

        // {watcher} state (STARTING,STOPPED or STARTED)
        WatcherState watcherState = watcherStatsResponse.getWatcherState();
        watcherState.name();
        watcherState.getId();
    }

    public static void serviceApi(WatcherClient watcherClient) {
        WatcherServiceResponse response = watcherClient.prepareWatchService()
                .start()     // start
//                .stop()      // stop
//                .restart()   // restart
                .get();
    }
}
