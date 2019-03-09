import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Application {
  
  //The config parameters for the connection
  private static final String HOST = "localhost";
  private static final int PORT_ONE = 9200;
  private static final int PORT_TWO = 9201;
  private static final String SCHEME = "http";
  private static final String INDEX = "persondata";
  private static final String TYPE = "person";
  private static RestHighLevelClient restHighLevelClient;
  private static ObjectMapper objectMapper = new ObjectMapper();
  
  /**
   * Implemented Singleton pattern here
   * so that there is just one connection at a time.
   *
   * @return RestHighLevelClient
   */
  private static synchronized RestHighLevelClient makeConnection() {
    
    if (restHighLevelClient == null) {
      restHighLevelClient = new RestHighLevelClient(
              RestClient.builder(
                      new HttpHost(HOST, PORT_ONE, SCHEME),
                      new HttpHost(HOST, PORT_TWO, SCHEME)));
    }
    
    return restHighLevelClient;
  }
  
  private static synchronized void closeConnection() throws IOException {
    restHighLevelClient.close();
    restHighLevelClient = null;
  }
  
  private static Person insertPerson(Person person) {
    person.setPersonId(UUID.randomUUID().toString());
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("personId", person.getPersonId());
    dataMap.put("name", person.getName());
    IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, person.getPersonId())
            .source(dataMap);
    try {
      IndexResponse response = restHighLevelClient.index(indexRequest);
    } catch (ElasticsearchException e) {
      e.getDetailedMessage();
    } catch (java.io.IOException ex) {
      ex.getLocalizedMessage();
    }
    return person;
  }
  
  private static Person getPersonById(String id) {
    GetRequest getPersonRequest = new GetRequest(INDEX, TYPE, id);
    GetResponse getResponse = null;
    try {
      getResponse = restHighLevelClient.get(getPersonRequest);
    } catch (java.io.IOException e) {
      e.getLocalizedMessage();
    }
    return getResponse != null ?
            objectMapper.convertValue(getResponse.getSourceAsMap(), Person.class) : null;
  }
  
  private static Person updatePersonById(String id, Person person) {
    UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
            .fetchSource(true);    // Fetch Object after its update
    try {
      String personJson = objectMapper.writeValueAsString(person);
      updateRequest.doc(personJson, XContentType.JSON);
      UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
      return objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), Person.class);
    } catch (JsonProcessingException e) {
      e.getMessage();
    } catch (java.io.IOException e) {
      e.getLocalizedMessage();
    }
    System.out.println("Unable to update person");
    return null;
  }
  
  private static void deletePersonById(String id) {
    DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
    try {
      DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
    } catch (java.io.IOException e) {
      e.getLocalizedMessage();
    }
  }
  
  public static void main(String[] args) throws IOException {
    
    makeConnection();
    
    System.out.println("Inserting a new Person with name Shubham...");
    Person person = new Person();
    person.setName("Shubham");
    person = insertPerson(person);
    System.out.println("Person inserted --> " + person);
    
    System.out.println("Changing name to `Shubham Aggarwal`...");
    person.setName("Shubham Aggarwal");
    updatePersonById(person.getPersonId(), person);
    System.out.println("Person updated  --> " + person);
    
    System.out.println("Getting Shubham...");
    Person personFromDB = getPersonById(person.getPersonId());
    System.out.println("Person from DB  --> " + personFromDB);
    
    System.out.println("Deleting Shubham...");
    //deletePersonById(personFromDB.getPersonId());
    System.out.println("Person Deleted");
    
    closeConnection();
  }
}
