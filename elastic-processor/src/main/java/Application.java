import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

class Application {
  
  public static void main(String[] args) throws UnknownHostException {
    // on startup
    
    TransportClient client =
            new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress., 9300));
    
    Settings settings = Settings.builder().put("cluster.name", "myClusterName").build();
    TransportClient client = new PreBuiltTransportClient(settings);
    // Add transport addresses and do something with the client...
    
    // on shutdown
    
    client.close();
  }
}
