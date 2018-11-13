package com.sjz.es;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteAction;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EsClientHelper {

    /**
     * 初始化es的client
     * @return
     */
    public static RestHighLevelClient initEsClient(){

        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.106.110", 9200),
                        new HttpHost("192.168.106.111", 9200),
                        new HttpHost("192.168.106.112", 9200)
                ));

        return restHighLevelClient;
    }

    /**
     * 关闭client
     * @param client
     */
    public static void closeClient(RestHighLevelClient client) throws IOException {
        client.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        syncGetRequest();
//        asyncGetRequest();
//        exist();
//        deleteRequest();

//        updateRequest();

//        bulkRequest();

        deleteRequestBuilder();

        System.exit(0);
    }

    public static void bulkRequest() throws IOException {

        RestHighLevelClient client = initEsClient();
        BulkRequest bulkRequest = new BulkRequest();

        bulkRequest.add(new IndexRequest("posts", "doc", "3").source(XContentType.JSON,"name","zhagnsan","age","30"));
        bulkRequest.add(new IndexRequest("posts", "doc", "4").source(XContentType.JSON,"name","zhagnsan","age","400"));

        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponse.toString());

        System.out.println("status:--->" + bulkResponse.status());

        BulkItemResponse[] bulkResponseItems = bulkResponse.getItems();
        for (BulkItemResponse bulkItemResponse : bulkResponseItems) {

            if(bulkItemResponse.isFailed()){
                BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                System.out.println("失败调用--->"+ failure.toString());
                continue;
            }

            DocWriteResponse itemResponse = bulkItemResponse.getResponse();
            if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                    || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                IndexResponse indexResponse = (IndexResponse) itemResponse;

                System.out.println("indexResponse --> "+indexResponse.toString());

            } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                System.out.println("updateResponse --> "+updateResponse.toString());

            } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                System.out.println("deleteResponse --> "+deleteResponse.toString());
            }
        }

        closeClient(client);
    }

    /**
     * 更新
     */
    public static void updateRequest() throws IOException {
        RestHighLevelClient client = initEsClient();

        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        xContentBuilder.startObject();
        xContentBuilder.timeField("update", new Date());
        xContentBuilder.field("reson", "daily update");

        xContentBuilder.endObject();

        String jsonString = "{\"created\":\"2017-01-01\"}";
        UpdateRequest updateRequest = new UpdateRequest("posts", "doc", "2").doc(xContentBuilder)
                .upsert(jsonString, XContentType.JSON);

//        request.upsert(jsonString, XContentType.JSON);
//          If the document does not already exist, it is possible to define some content that will be inserted as a new document using the upsert method:

        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.toString());

        GetResult result = updateResponse.getGetResult();
        if(result != null && result.isExists()){
            String s = result.sourceAsString();
            System.out.println(s);
        }

        closeClient(client);
    }




    /**
     * 删除索引
     * @throws IOException
     */
    public static void deleteRequest() throws IOException {
        RestHighLevelClient client = initEsClient();

//        DeleteRequestBuilder是低级客户端使用，高级客户端使用DeleteRequest
        DeleteRequest deleteRequest = new DeleteRequest("posts", "doc", "11").routing("post1");
        System.out.println("请求体==="+deleteRequest.toString());
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println("==="+deleteResponse.toString());
        if(DocWriteResponse.Result.NOT_FOUND == deleteResponse.getResult()){
            System.out.println("文件不存在");
        }
        closeClient(client);
    }


    public static void deleteRequestBuilder() throws IOException {

        RestHighLevelClient client = initEsClient();
        DeleteRequestBuilder deleteRequestBuilder = DeleteAction.INSTANCE.newRequestBuilder((ElasticsearchClient) client);
        deleteRequestBuilder.setIndex("posts").setType("doc").setId("4");


        ActionFuture<DeleteResponse> execute = deleteRequestBuilder.execute();

        DeleteResponse deleteResponse = execute.actionGet();

        System.out.println("==="+deleteResponse.toString());
        if(DocWriteResponse.Result.NOT_FOUND == deleteResponse.getResult()){
            System.out.println("文件不存在");
        }

        closeClient(client);
    }

    public static void syncGetRequest() throws IOException {
        RestHighLevelClient client = initEsClient();
        GetRequest getRequest = new GetRequest("posts", "doc", "1")
//                .fetchSourceContext(new FetchSourceContext(true, new String[]{"age"}, new String[]{"field"}));
                .fetchSourceContext(new FetchSourceContext(false, new String[]{"age"}, new String[]{"field"}));

//        FetchSourceContext fetchSourceContext = new FetchSourceContext(false);//Disable fetching _source，默认true

        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse);

        boolean isExist = getResponse.isExists(); //数据是否存在
        if(isExist){
            String sourceAsString = getResponse.getSourceAsString();
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            byte[] sourceAsBytes = getResponse.getSourceAsBytes();
        }else{
            System.out.println("数据不存在");
            return;
        }

        Map<String, Object> resultDataMap = getResponse.getSource();
        System.out.println("=============="+resultDataMap.get("age"));


        closeClient(client);
    }


    public static void asyncGetRequest() throws IOException, InterruptedException {
        RestHighLevelClient client = initEsClient();

        GetRequest getRequest = new GetRequest("posts", "doc", "1")
                .fetchSourceContext(new FetchSourceContext(true, new String[]{"age"}, new String[]{"field"}));

        client.getAsync(getRequest, RequestOptions.DEFAULT, new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse response) {
                System.out.println(response.toString());
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("查询异常");
            }
        });

        System.out.println("异步等待数据输出。。。");
        TimeUnit.SECONDS.sleep(10);

        closeClient(client);
    }

    public static void exist() throws IOException {
        RestHighLevelClient client = initEsClient();
        //判断数据是否存在，该方法效率较高
        GetRequest getRequest = new GetRequest("posts", "doc", "10")
                .fetchSourceContext(new FetchSourceContext(false)) //Disable fetching _source.
                .storedFields("_none_"); //Disable fetching stored fields.

        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println("数据是否存在"+exists);
    }



    /**
     * 生成indexRequest
     * @return
     * @throws IOException
     */
    public static IndexRequest initIndexRequest() throws IOException {
        IndexRequest indexRequest = new IndexRequest("posts", "doc" ,"1");

        //method1
//        String jsonString = "{" +
//                "\"user\":\"kimchy\"," +
//                "\"postDate\":\"2018-11-9\"," +
//                "\"message\":\"trying out Elasticsearch\"" +
//                "}";
//
//        IndexRequest request = indexRequest.source(jsonString, XContentType.JSON);

        //method2
//        Map<String,Object> jsonMap = new HashMap<>();
//        jsonMap.put("user", "kimchy");
//        jsonMap.put("postDate", "2018-11-9");
//        jsonMap.put("message", "trying out Elasticsearch");
//
//        IndexRequest request = indexRequest.source(jsonMap);


        //method3
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();

        builder.field("user", "kimchy");
        builder.timeField("postDate", new Date());
        builder.field("message", "trying out Elasticsearch");

        builder.endObject();

        IndexRequest request = indexRequest.source(builder);
        request.routing("post1");//路由到制定的node
        request.timeout(TimeValue.timeValueSeconds(2));

//        request.versionType(VersionType.EXTERNAL);
//        request.version(2);//设置外部版本号

//        request.setPipeline("Pipeline1");

        request.opType(DocWriteRequest.OpType.CREATE);

        return request;
    }


//    public static void main(String[] args) throws IOException {
//        RestHighLevelClient client = initEsClient();
//        try {
////            syncCreateIndex(client);
////            asyncCreateIndex(client);
//
//            IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
//                    .source("field","value","age","12")
//                    .version(2)
//                    .versionType(VersionType.EXTERNAL);
//
//            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
//            System.out.println(indexResponse.toString());
//
//        }catch (Exception e){
//            if(e instanceof ElasticsearchException){
//                ElasticsearchException esException = (ElasticsearchException) e;
//                if(RestStatus.CONFLICT == esException.status()){
//                    System.out.println("版本号冲突");
//                }
//            }
//        }finally {
//            closeClient(client);
//        }
//    }



    /**
     * 同步创建索引
     * @param client
     * @return
     * @throws IOException
     */
    public static IndexResponse syncCreateIndex(RestHighLevelClient client) throws IOException {

        IndexRequest request = initIndexRequest();
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

//        DocWriteResponse.Result responseResult = response.getResult();
//
//        if(DocWriteResponse.Result.CREATED == responseResult){
//
//        }

        ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
        if(shardInfo.getTotal() != shardInfo.getSuccessful()){ //分片的数量 != 成功的数量
            int failed = shardInfo.getFailed();//错误数量
            ReplicationResponse.ShardInfo.Failure[] failures = shardInfo.getFailures(); //错误信息
            for (ReplicationResponse.ShardInfo.Failure failure : failures) {
                String reason = failure.reason(); //打印错误结果
                System.out.println("错误原因"+reason);
            }

        }

        String s = response.toString();

            System.out.println(s);
            return response;
    }

    /**
     * 异步创建索引
     * @param client
     * @throws IOException
     */
    public static void asyncCreateIndex(RestHighLevelClient client) throws IOException {
        try {
            IndexRequest request = initIndexRequest();

            client.indexAsync(request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    System.out.println(indexResponse.toString());
                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println("异常操作");
                    System.out.println(e.getMessage());
                }
            });
            System.out.println("异步获取打印结果。。。");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeClient(client);
        }
    }
}
