package httpClient;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

public class FaApi {

    private  final static String URL = "http://api.faceall.cn:80/";
    private final static String TAG  = "FaApi";
    private String apiKey            = null;
    private String apiSecret         = null;
    private String version           = null;
    private HttpParams httpParams; 
    private HttpClient httpClient;

    public FaApi(String apiKey, String apiSecret, String version) {
        this.apiKey    = apiKey;
        this.apiSecret = apiSecret;
        this.version   = version;
        this.getHttpClient();
    }

    public JSONObject executeMultipart(String method , List<PostParameter> params)
    {
    	JSONObject jsob = new JSONObject();
    	try {
            MultipartPost post = new MultipartPost(params);
            String result      = post.send(URL+this.version+"/"+method);
            jsob               = new JSONObject(result);
    	} catch (Exception e) {
    		Log.e(TAG, "sendPostRequest", e);
    		Log.d(TAG, "==================================================");
		}
    	return jsob;
    }
    
    public JSONArray executeMultipartArray(String method , List<PostParameter> params)
    {
    	JSONArray jary = new JSONArray();
    	try {
            MultipartPost post = new MultipartPost(params);
            String result      = post.send(URL+this.version+"/"+method);
            jary               = new JSONArray(result);		
    	} catch (Exception e) {
    		Log.e(TAG, "sendPostRequest", e);
    		Log.d(TAG, "==================================================");
		}
    	return jary;
    }
    
    public JSONObject executeGet(String method , List<BasicNameValuePair> params)
    {
    	JSONObject jsob = null;
    	try {
            String param     = URLEncodedUtils.format(params, "UTF-8");

            String stringUrl = URL+this.version+"/"+method;
            stringUrl        = stringUrl+"?"+param;
			
            HttpGet httpGet           = new HttpGet(stringUrl);
            HttpResponse httpResponse = this.httpClient.execute(httpGet);
            String response           = "";
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity, "utf-8");
				jsob     = new JSONObject(response);
				}
    		}catch (Exception e) {
    			Log.e(TAG,"=====================Exception error exists========================");
    			e.printStackTrace();
			}
		return jsob;
    	
    }
    
    public JSONArray executeGetArray(String method , List<BasicNameValuePair> params)
    {
    	JSONArray jary = null;
    	try {
            String param     = URLEncodedUtils.format(params, "UTF-8");
            Log.d(TAG,param);
            //baseUrl 
            String stringUrl = URL+this.version+"/"+method;
            stringUrl        = stringUrl+"?"+param;

			HttpGet httpGet = new HttpGet(stringUrl);
			HttpResponse httpResponse = this.httpClient.execute(httpGet);
			String response = "";
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity, "utf-8");
				jary = new JSONArray(response);
				}
    		}catch (Exception e) {
				e.printStackTrace();
			}
		return jary;
    	
    }
    
    public InputStream executeGetFile(String method , List<BasicNameValuePair> params)
    {
    	InputStream file = null;
    	try {
            String param     = URLEncodedUtils.format(params, "UTF-8");
            Log.d(TAG,param);
            //baseUrl 
            String stringUrl = URL+this.version+"/"+method;
            stringUrl        = stringUrl+"?"+param;

			HttpGet httpGet = new HttpGet(stringUrl);
			HttpResponse httpResponse = this.httpClient.execute(httpGet);
			String response = "";
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				file = entity.getContent();
				}
    		}catch (Exception e) {
				e.printStackTrace();
			}
		return file;
    	
    }
    
    public void getHttpClient() {  
        this.httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);  
        HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);  
        HttpConnectionParams.setSocketBufferSize(httpParams, 1024*20000*20000);
        HttpConnectionParams.setLinger(httpParams, 5*1000);
        HttpConnectionParams.setTcpNoDelay(httpParams, false);
 
        HttpClientParams.setRedirecting(httpParams, true);
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";  
        HttpProtocolParams.setUserAgent(httpParams, userAgent);
        this.httpClient = new DefaultHttpClient(httpParams);
    }     
}
