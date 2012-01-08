/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trainwatcher;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ovoloshchuk
 */
public class TrainWatcher {
    
    static String request(String sUrl, HashMap<String, String> sCookies) throws IOException {
        URL u = new URL(sUrl);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(false);
        conn.connect();


        String sHeaderName;
        String sLocation = null;
        for (int i = 1; (sHeaderName = conn.getHeaderFieldKey(i)) != null; i++) {
            if (sHeaderName.equals("Set-Cookie")) {
                String[] cookie = conn.getHeaderField(i).split("=");
                if (cookie[1].startsWith("deleted")) {
                    sCookies.remove(cookie[1]);
                    continue;
                }
                sCookies.put(cookie[0], cookie[1]);
            } else if (sHeaderName.equals("Location")){
                sLocation = conn.getHeaderField(i);
            }
        }
        return sLocation;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            HashMap<String, String> sCookies = new HashMap<String, String>();
            String sLocation = request("http://localhost/test/index.php", sCookies);
            if (sLocation != null) {
                sLocation = request(sLocation, sCookies);
            }
            
            for (String sKey : sCookies.keySet()) {
                System.out.println(String.format("%s: %s", sKey, sCookies.get(sKey)));
            }
        } catch (IOException ex) {
            Logger.getLogger(TrainWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
