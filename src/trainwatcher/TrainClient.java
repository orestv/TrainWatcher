/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trainwatcher;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ovoloshchuk
 */
public class TrainClient {
    private final static String URL_LOGIN = "http://www.e-kvytok.com.ua/wps/portal/!ut/p/c5/04_SB8K8xLLM9MSSzPy8xBz9CP0os3gjCyNPD0dnAw8DY283A0_XkECDQDNXAwNfE_1wkA6zeAMcwNFA388jPzdVvyA7rxwAhYnqvg!!/dl3/d3/L0lDUWtpQ1NTUW9LVVFBISEvb0lvZ0FFQ1FRREdJUXBURE9DNEpuQSEhLzRDd2lSLXJmbTE2SWt5WGlnRUEhLzdfQ0dBSDQ3TDAwMDY2QzAyQjU2NlI5QTMwTTUvd3BzLnBvcnRsZXRzLmxvZ2lu/";
    //private final static String URL_FIND_STATION = "http://www.e-kvytok.com.ua/wps/myportal/!ut/p/c5/04_SB8K8xLLM9MSSzPy8xBz9CP0os3gDf1MjYzcXYws3A0cvc08LSx8DKADKR2LKmxrD5fHrDgfZh18_WB4HcDTArt8CYYOfR35uqn5BboRBZkC6IgCaRiDN/dl3/d3/L0lDU0lKSWdrbUNTUS9JUFJBQUlpQ2dBek15cXpHWUEhIS80QkVqOG8wRmxHaXQtYlhwQUh0Qi83XzkxSEcxMjgyOE9WSUIwMkZWN1RVSVMyMEcwL3dIMTZ2MTIyMzAwMDMvc2EucmV6ZXJ2YXRpb24uRm9ybUFjdGlvbg!!/#7_91HG12828OVIB02FV7TUIS20G0";
    private final static String URL_FIND_STATION = "http://www.e-kvytok.com.ua/wps/myportal/!ut/p/c5/04_SB8K8xLLM9MSSzPy8xBz9CP0os3gDf1MjYzcXYws3A0cvc08LSx8DKADKR2LKmxrD5fHrDgfZh18_WB4HcDTArt8CYYOfR35uqn6kfpR5vKWhh7uhkYWRhX-Yp5OBkVuYeUioZ7CRgbuBfmROanpicqV-QW6EQWZARiAA_7KIxQ!!/dl3/d3/L0lJSklna21DU1EhIS9JRGpBQU15QUJFUkNKRXFnLzRGR2dzbzBWdnphOUlBOW9JQSEhLzdfOTFIRzEyODI4T1ZJQjAyRlY3VFVJUzIwRzAvUll3dl8zODU3MDAxNS9zYS5yZXplcnZhdGlvbi5Gb3JtQWN0aW9u/";
    private final static String URL_FIND_STATION_BASE = "http://www.e-kvytok.com.ua/wps/myportal/!ut/p/c5/04_SB8K8xLLM9MSSzPy8xBz9CP0os3gDf1MjYzcXYws3A0cvc08LSx8DKADKR2LKmxrD5fHrDgfZh18_WB4HcDTArt8CYYOfR35uqn5BboRBZkC6IgCaRiDN/dl3/d3/L2dJQSEvUUt3QS9ZQnZ3LzZfME81MjNGRDM4RjBBSjdJODhMMDAwMDAwMDA!/";
    
    private final static String FORM_LOGIN_LOGIN = "wps.portlets.userid";
    private final static String FORM_LOGIN_PASSWORD = "password";
    private final static String FORM_LOGIN_ACTION = "ns_7_CGAH47L00066C02B566R9A30M5__login";
    
    private final static String FORM_FIND_FROM = "PC_7_91HG12828OVIB02FV7TUIS20G0000000_rezervation.EditFormTAG0";
    private final static String FORM_FIND_ACTION = "PC_7_91HG12828OVIB02FV7TUIS20G0000000_rezervation.SubmitSp1";
    
    private String sLogin;
    private String sPassword;
    private String sCookies = "";
    
    private Map<String, String> cookies;
    
    public TrainClient() {
        cookies = new HashMap<String, String>();
    }

    public TrainClient(String sLogin, String sPassword) {
        this();
        this.sLogin = sLogin;
        this.sPassword = sPassword;
    }
    
    public boolean login() throws IOException {
        String sParams = formParameters(FORM_LOGIN_LOGIN, sLogin,
                FORM_LOGIN_PASSWORD, sPassword,
                FORM_LOGIN_ACTION, "Log in");
        HttpURLConnection conn = request(URL_LOGIN, sParams, false);
        
        return (conn.getResponseCode() == 302);
    }
    
    private static String formParameters(String... strings) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < strings.length; i += 2){
            String sName = strings[i];
            String sValue = strings[i+1];
            try {
                sb.append(sName).append("=").append(URLEncoder.encode(sValue, "utf-8")).append("&");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(TrainClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return sb.toString();
    }
    
    public String[] getCities(String sCityName) throws IOException {
        HttpURLConnection conn1 = request(URL_FIND_STATION_BASE, "", true);
        String sParams = formParameters(FORM_FIND_FROM, sCityName,
                FORM_FIND_ACTION, "Знайти станцію",
                "PC_7_91HG12828OVIB02FV7TUIS20G0000000_rezervation.EditFormTAG1", "",
                "PC_7_91HG12828OVIB02FV7TUIS20G0000000_rezervation.EditFormTAG2", "30.12.2011",
                "PC_7_91HG12828OVIB02FV7TUIS20G0000000_rezervation.EditFormTAG5", "",
                "PC_7_91HG12828OVIB02FV7TUIS20G0000000_rezervation.EditFormTAG6", "");
        HttpURLConnection conn = request(URL_FIND_STATION, sParams, true);
        String sResponse = getResponse(conn);
        return null;
    }
    
    private HttpURLConnection request(String sUrl, String sParams, boolean bFollowRedirect) throws MalformedURLException, IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(sUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(sParams.getBytes().length));
        conn.setInstanceFollowRedirects(bFollowRedirect);
        
        conn.setRequestProperty("Accept-Charset", "utf-8");
        conn.setRequestProperty("Cache-Control", "max-age=0");
        conn.addRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Host", "www.e-kvytok.com.ua");
        conn.setRequestProperty("Origin", "http://www.e-kvytok.com.ua");
        //conn.setRequestProperty("Referer", sUrl);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");        
        
        if (!cookies.isEmpty())
            conn.setRequestProperty("Cookie", getCookieLine(cookies));        
                
        conn.setUseCaches (false);
        conn.setDoOutput(true);
        
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        dos.writeBytes(sParams);
        dos.flush();
        dos.close();        
                        
        String sHeaderName;
        String sCookie = null;
        for (int i = 1; (sHeaderName = conn.getHeaderFieldKey(i)) != null; i++)
            if (sHeaderName.equals("Set-Cookie")) {
                sCookie = conn.getHeaderField(i);
                String[] split = sCookie.split("=");
                String sCookieName = split[0], sCookieValue = split[1];
                cookies.put(sCookieName,  sCookieValue);
            }
        
        return conn;        
    }
    
    private static String getResponse(HttpURLConnection conn) throws IOException {
        StringBuilder sb = new StringBuilder();
        
        String sLine;
        InputStream is;
        if (conn.getErrorStream() == null)
            is = conn.getInputStream();
        else
            is = conn.getErrorStream();
        BufferedReader  rdr = new BufferedReader(new InputStreamReader(is));
        while ((sLine = rdr.readLine()) != null)
            sb.append(sLine);
        
        return sb.toString();
    }
    
    private static String getCookieLine(Map<String, String> cookies) {
        StringBuilder sb = new StringBuilder();
        
        for(String sName : cookies.keySet()) {
            sb.append(String.format("%s=%s", sName, cookies.get(sName)));
        }
        
        return sb.toString();
    }
}
