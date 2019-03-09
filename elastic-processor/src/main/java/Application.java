import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class Application {
  
  // The config parameters for the connection
  private static final String HOST = "localhost";
  private static final int PORT_ONE = 9200;
  private static final int PORT_TWO = 9201;
  private static final String SCHEME = "http";
  private static final String INDEX = "persondata";
  private static final String TYPE = "person";
  private static RestHighLevelClient restHighLevelClient;

  /**
   * Implemented Singleton pattern here so that there is just one connection at a time.
   *
   * @return RestHighLevelClient
   */
  private static synchronized RestHighLevelClient makeConnection() {

    if (restHighLevelClient == null) {
      restHighLevelClient =
              new RestHighLevelClient(
              RestClient.builder(
                      new HttpHost(HOST, PORT_ONE, SCHEME), new HttpHost(HOST, PORT_TWO, SCHEME)));
    }

    return restHighLevelClient;
  }

  private static synchronized void closeConnection() throws IOException {
    restHighLevelClient.close();
    restHighLevelClient = null;
  }
  
  private static void insertPerson(String json) {
    
    
    IndexRequest request = new IndexRequest(
            INDEX, TYPE,
            "42");
    
    
    request.source(json, XContentType.JSON);
    
    
    /*
    person.setPersonId(UUID.randomUUID().toString());
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("personId", person.getPersonId());
    dataMap.put("name", person.getName());
    IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, person.getPersonId()).source(dataMap);
    System.out.println(indexRequest);
    try {
      IndexResponse response = restHighLevelClient.index(indexRequest);
    } catch (ElasticsearchException e) {
      e.getDetailedMessage();
    } catch (java.io.IOException ex) {
      ex.getLocalizedMessage();
    }
    return person;
    */
  }

  public static void main(String[] args) throws IOException {
  
    // connection to broker
    // client asincrono

    makeConnection();
  
  
    String json = "{\"name\":\"pippo\",\"personId\":\"42\"}";
  
    insertPerson(json);

    closeConnection();
  }
}
