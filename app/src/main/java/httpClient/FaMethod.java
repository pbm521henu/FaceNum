package httpClient;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FaMethod {
	private static final String DETECTION_DETECT                 = "detection/detect";
	private static final String DETECTION_LANDMARK               = "detection/landmark";
	private static final String DETECTION_LANDMARK68             = "detection/landmark68";
	private static final String DETECTION_FEATURE                = "detection/feature";
	private static final String DETECTION_ATTRIBUTES             = "detection/attributes";
	private static final String DETECTION_BEAUTY                 = "detection/beauty";
	private static final String FACE_GET_INFO                    = "face/get_info";
	private static final String FACE_SET_LABEL                   = "face/set_label";
	private static final String IMAGE_GET_INFO                   = "image/get_info";
	private static final String IMAGE_GET_LIST                   = "image/get_list";
	private static final String IMAGE_GET_FILE                   = "image/get_file";
	private static final String FACESET_CREATE                   = "faceset/create";
	private static final String FACESET_DELETE                   = "faceset/delete";
	private static final String FACESET_ADD_FACES                = "faceset/add_faces";
	private static final String FACESET_REMOVE_FACES             = "faceset/remove_faces";
	private static final String FACESET_GET_INFO                 = "faceset/get_info";
	private static final String FACESET_SET_INFO                 = "faceset/set_info";
	private static final String FACESET_TRAIN                    = "faceset/train";
	private static final String FACESET_GET_LIST                 = "faceset/get_list";
	private static final String RECOGNITION_CLUSTER              = "recognition/cluster";
	private static final String RECOGNITION_COMPARE_FACE         = "recognition/compare_face";
	private static final String RECOGNITION_COMPARE_FACE_FACESET = "recognition/compare_face_faceset";
	private static final String RECOGNITION_CELEBRITY            = "recognition/celebrity";
	private static final String OBJECT_ROCOGNIZE                 = "object/recognize";
	private static final String OBJECT_PORN_ROCOGNITION          = "object/porn_recognition";
	 
	public String apiKey     = null;
	public String apiSecret  = null;
	public String apiVersion = null;
	private FaApi api        = null;  
	
	
	//构造方法
    public FaMethod(String apiKey, String apiSecret, String apiVersion)
    {
    	this.apiKey    =apiKey;
    	this.apiSecret =apiSecret;
    	this.apiVersion=apiVersion;
    	api            =new FaApi(apiKey,apiSecret,apiVersion);
    }
	//********************************************* detecet *********************************************
	public JSONObject detection_detect(File FILE,String img_url,String img_base64,String attributes)
	{	   
		 List<PostParameter> params = new ArrayList<PostParameter>();
		 params.add(new PostParameter<String>("api_key", this.apiKey));
		 params.add(new PostParameter<String>("api_secret", this.apiSecret));
		 params.add(new PostParameter<File>("img_file", FILE));
		 params.add(new PostParameter<String>("img_url", img_url));
		 params.add(new PostParameter<String>("img_base64", img_base64));
		 params.add(new PostParameter<String>("attributes", attributes));
		 return api.executeMultipart(DETECTION_DETECT, params);
	}
	
	public JSONObject detection_landmark(String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("face_id", face_id));
		return api.executeGet(DETECTION_LANDMARK, params);	
	}

	public JSONObject detection_landmark68(String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("face_id", face_id));
		return api.executeGet(DETECTION_LANDMARK68, params);	
	}

	public JSONObject detection_feature(String face_id, String return_feature)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("face_id", face_id));
 		params.add(new BasicNameValuePair("return_feature", return_feature));
		return api.executeGet(DETECTION_FEATURE, params);	
	}

	public JSONObject detection_attributes(String face_id, String limit)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("face_id", face_id));
		params.add(new BasicNameValuePair("limit",limit));
		return api.executeGet(DETECTION_ATTRIBUTES, params);	
	}
    
	public JSONObject detection_beauty(String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("face_id", face_id));
		return api.executeGet(DETECTION_BEAUTY, params);	
	}
    //********************************************* face *********************************************

	public JSONArray face_get_info( String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("face_id", face_id));
		return api.executeGetArray(FACE_GET_INFO, params);	
	}

	public JSONObject face_set_label(String face_id,String label)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("face_id", face_id));
		params.add(new BasicNameValuePair("label", label));
		return api.executeGet(FACE_GET_INFO, params);
	}
	
    //********************************************* image *********************************************	

	public JSONArray image_get_info(String image_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("image_id", image_id));
		return api.executeGetArray(IMAGE_GET_INFO, params);	
	}

	public JSONArray image_get_list(String skip,String limit)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("skip", skip));
		params.add(new BasicNameValuePair("limit", limit));
		return api.executeGetArray(IMAGE_GET_LIST, params);	
	}


	public InputStream image_get_file(String image_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("image_id", image_id));
		return api.executeGetFile(IMAGE_GET_FILE, params);	
	}

    //********************************************* faceset *********************************************	
	public JSONObject faceset_create(String faceset_name)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("faceset_name", faceset_name));
		return api.executeGet(FACESET_CREATE, params);	
	}
	
	public JSONObject faceset_delete(String faceset_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("faceset_id", faceset_id));
		return api.executeGet(FACESET_DELETE, params);	
	}
	
	public JSONObject faceset_add_faces(String faceset_id, String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("faceset_id", faceset_id));
		params.add(new BasicNameValuePair("face_id", face_id));
		return api.executeGet(FACESET_ADD_FACES, params);	
	}
	
	public JSONObject faceset_remove_faces(String faceset_id, String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("faceset_id", faceset_id));
		params.add(new BasicNameValuePair("face_id", face_id));
		return api.executeGet(FACESET_REMOVE_FACES, params);	
	}
	
	public JSONObject faceset_get_info(String faceset_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("faceset_id", faceset_id));
		return api.executeGet(FACESET_GET_INFO, params);	
	}
	
	public JSONObject faceset_set_info(String faceset_id, String faceset_name)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("faceset_id", faceset_id));
		params.add(new BasicNameValuePair("faceset_name", faceset_name));
		return api.executeGet(FACESET_SET_INFO, params);	
	}
	
	public JSONObject faceset_train(String faceset_id,String type, String async)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("faceset_id", faceset_id));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("async", async));
		return api.executeGet(FACESET_TRAIN, params);	
	}
	
	public JSONArray faceset_get_list()
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		return api.executeGetArray(FACESET_GET_LIST, params);	
	}
	
    //********************************************* recognition *********************************************	

	public JSONObject recognition_cluster(String faceset_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("faceset_id", faceset_id));
		return api.executeGet(RECOGNITION_CLUSTER, params);	
	}
	
	public JSONObject recognition_compare_face(String face_id1,String face_id2,String type)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("face_id1", face_id1));
		params.add(new BasicNameValuePair("face_id2", face_id2));
		params.add(new BasicNameValuePair("type", type));
		return api.executeGet(RECOGNITION_COMPARE_FACE, params);	
	}
	
	public JSONObject recognition_compare_face_faceset(String face_id, String faceset_id,String limit)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("face_id", face_id));
		params.add(new BasicNameValuePair("faceset_id", faceset_id));
		params.add(new BasicNameValuePair("limit", limit));
		return api.executeGet(RECOGNITION_COMPARE_FACE_FACESET, params);	
	}
	
	public JSONObject recognition_celebrity(String face_id)
	{
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		params.add(new BasicNameValuePair("face_id", face_id));
		return api.executeGet(RECOGNITION_CELEBRITY, params);	
	}
	
    //********************************************* object *********************************************	

	public JSONObject object_recognize(File FILE)
	{
		 List<PostParameter> params = new ArrayList<PostParameter>();
		 params.add(new PostParameter<String>("api_key", this.apiKey));
		 params.add(new PostParameter<String>("api_secret", this.apiSecret));
		 params.add(new PostParameter<File>("img_file", FILE));
//		 params.add(new PostParameter<String>("limit", limit));
		 return api.executeMultipart(OBJECT_ROCOGNIZE, params);
	}

	public JSONObject object_porn_recognition(File FILE)
	{
		 List<PostParameter> params = new ArrayList<PostParameter>();
		 params.add(new PostParameter<String>("api_key", this.apiKey));
		 params.add(new PostParameter<String>("api_secret", this.apiSecret));
		 params.add(new PostParameter<File>("img_file", FILE));
		 return api.executeMultipart(OBJECT_PORN_ROCOGNITION, params);
	}	

}