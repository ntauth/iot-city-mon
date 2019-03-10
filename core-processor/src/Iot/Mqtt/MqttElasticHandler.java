package Iot.Mqtt;

import org.apache.http.HttpHost;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class MqttElasticHandler implements MqttCallback
{
    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9300;
    private static final String SCHEME = "http";
    private static final String INDEX = "collector";
    private static final String TYPE = "collector";
    private static RestHighLevelClient restHighLevelClient;

    public MqttElasticHandler() {
        makeConnection();
    }

    private static synchronized RestHighLevelClient makeConnection()
    {
        if (restHighLevelClient == null)
        {
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(HOST, 9200, "http")));
        }

        return restHighLevelClient;
    }

    private static synchronized void closeConnection() throws IOException
    {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }

    public void connectionLost(Throwable throwable)
    {
        throwable.printStackTrace();
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
    {
        String msg = mqttMessage.toString();
        String indexName = String.format("%s-%s", INDEX, topic.split("/")[1]);

        CreateIndexRequest createRequest = new CreateIndexRequest(indexName);
        IndexRequest request = new IndexRequest(indexName, TYPE,"1");
        request.source(msg, XContentType.JSON);

        System.out.println(msg);

//        _executor.submit(() -> {
//            boolean exists = false;
//
//            try {
//                exists = restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
//            } catch (IOException e) {}
//
//
//            if (exists)
//                System.out.println("Exists");
//            else
//                System.out.println("Does not exist");
//        });
//        boolean exists = restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);

//        if (exists)
//            System.out.println("Exists");
//        else
//            System.out.println("Does not exist");
//        restHighLevelClient.indexAsync(request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
//            public void onResponse(IndexResponse indexResponse) {
//                System.out.println(indexResponse.toString());
//            }
//
//            public void onFailure(Exception e) {
//                System.out.println(e.getLocalizedMessage());
//            }
//        });
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken)
    {
    }
}
