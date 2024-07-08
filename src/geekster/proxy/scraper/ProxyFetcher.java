package geekster.proxy.scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.logging.Level;
import java.util.logging.Logger;

class ProxyFetcher_ProxyScrape extends Thread {
    @Override
    public void run() {
        try {
            fetchProxyScrape();
        } catch (ProtocolException ex) {
            Logger.getLogger(ProxyFetcher_ProxyScrape.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProxyFetcher_ProxyScrape.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void fetchProxyScrape() throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL("https://api.proxyscrape.com/v3/free-proxy-list/get?request=displayproxies&proxy_format=protocolipport&format=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        ProxyParser_ProxyScrape parserThread = new ProxyParser_ProxyScrape(response.toString());
        parserThread.start();
    }
}

class ProxyFetcher_Geonode extends Thread {
    @Override
    public void run() {
        try {
            fetchGeonode();
        } catch (ProtocolException ex) {
            Logger.getLogger(ProxyFetcher_ProxyScrape.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProxyFetcher_ProxyScrape.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void fetchGeonode() throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL("https://proxylist.geonode.com/api/proxy-list?limit=500&page=1&sort_by=lastChecked&sort_type=desc");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        ProxyParser_Geonode parserThread = new ProxyParser_Geonode(response.toString());
        parserThread.start();
    }
}

class ProxyFetcher_ProxyList extends Thread {
    @Override
    public void run() {
        try {
            fetchedProxyListDownload();
            
        } catch (ProtocolException ex) {
            Logger.getLogger(ProxyFetcher_ProxyScrape.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProxyFetcher_ProxyScrape.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String fetchProxyListDownloadHTTP() throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL("https://www.proxy-list.download/api/v1/get?type=http");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append("http://" + line + "\n");
        }

        
        reader.close();
        return response.toString();
    }
    public static String fetchProxyListDownloadSOCKS4() throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL("https://www.proxy-list.download/api/v1/get?type=socks4");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append("socks4://"+ line + "\n");
        }
        reader.close();
        return response.toString();
    }
    
    private static void fetchedProxyListDownload() throws ProtocolException, IOException{
        
        String httpProxies = fetchProxyListDownloadHTTP();
        String socks4Proxies = fetchProxyListDownloadSOCKS4();
        String allProxies = ( httpProxies + socks4Proxies);
                
        
        ProxyParser_ProxyListDownload parserThread = new ProxyParser_ProxyListDownload(allProxies);
        parserThread.start();
        
    }
}

