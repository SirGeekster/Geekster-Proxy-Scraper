package geekster.proxy.scraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class ProxyParser {
    public static final ArrayList<String> proxyList = new ArrayList<>();
    
    public static ArrayList<String> getProxyList() {
        return ProxyParser.proxyList;
    }
}

class ProxyParser_ProxyScrape extends Thread {
    private final String proxyScrapeJSON;

    public ProxyParser_ProxyScrape(String proxyScrapeJSON) {
        this.proxyScrapeJSON = proxyScrapeJSON;
    }

    @Override
    public void run() {
        try {
            parseProxyScrape();
        } catch (IOException ex) {
            Logger.getLogger(ProxyParser_ProxyScrape.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseProxyScrape() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(proxyScrapeJSON);

        if (rootNode.has("proxies") && rootNode.get("proxies").isArray()) {
            for (JsonNode proxyNode : rootNode.get("proxies")) {
                String protocol = proxyNode.has("protocol") ? proxyNode.get("protocol").asText() : "";
                String ip = proxyNode.has("ip") ? proxyNode.get("ip").asText() : "";
                String port = proxyNode.has("port") ? proxyNode.get("port").asText() : "";

                String ip_port = ip + ":" + port;
                String type_ip_port = protocol + "://" + ip_port;

                ProxyParser.proxyList.add(type_ip_port);
            }

            GUI.addListToOutput();
        }
    }

}

class ProxyParser_Geonode extends Thread {
    private final String geonodeJSON;

    public ProxyParser_Geonode(String genodeJSON) {
        this.geonodeJSON = genodeJSON;
    }

    @Override
    public void run() {
        try {
            parseGeonode();
        } catch (IOException ex) {
            Logger.getLogger(ProxyParser_Geonode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseGeonode() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(geonodeJSON);

        if (rootNode.has("data") && rootNode.get("data").isArray()) {
            for (JsonNode proxyNode : rootNode.get("data")) {
                String ip = proxyNode.has("ip") ? proxyNode.get("ip").asText() : "";
                String port = proxyNode.has("port") ? proxyNode.get("port").asText() : "";
                String protocol = (proxyNode.has("protocols") && proxyNode.get("protocols").isArray() && proxyNode.get("protocols").size() > 0)
                                  ? proxyNode.get("protocols").get(0).asText() : "";

                String ip_port = ip + ":" + port;
                String type_ip_port = protocol + "://" + ip_port;

                ProxyParser.proxyList.add(type_ip_port);
               
            }
            GUI.addListToOutput();
        }
    }


}

class ProxyParser_ProxyListDownload extends Thread {
    private final String response;

    public ProxyParser_ProxyListDownload(String response) {
        this.response = response;
    }

    @Override
    public void run() {
        try {
            parseProxyListDownload();
        } catch (IOException ex) {
            Logger.getLogger(ProxyParser_ProxyListDownload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseProxyListDownload() throws IOException {
         String[] proxies = response.split("\n");
         for(String proxy : proxies){
             ProxyParser.proxyList.add(proxy);  
         }
         GUI.addListToOutput();
         
         
    }


}
class ProxyParser_OpenProxyList extends Thread {
    private final String response;

    public ProxyParser_OpenProxyList(String response) {
        this.response = response;
    }

    @Override
    public void run() {
        try {
            parseOpenProxyList();
        } catch (IOException ex) {
            Logger.getLogger(ProxyParser_ProxyListDownload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void parseOpenProxyList() throws IOException {
         String[] proxies = response.split("\n");
         for(String proxy : proxies){
             ProxyParser.proxyList.add(proxy);  
         }
         GUI.addListToOutput();
         
         
    }


}