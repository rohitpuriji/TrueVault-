package com.evon.utills;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

public class JsonParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String jsonString = "";
	private static final String TAG = "JSONParser";

	// constructor
	public JsonParser() {

	}

	public static JSONObject postDataToUrl(String strUrl,MultipartEntityBuilder multipartEntityBuilder) {
		JSONObject jsonObject = null;

		Log.e("clicked", "check 3");

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(strUrl);

		String basicAuth = "Basic " + Base64.encodeToString((Constants.user_apiKey+":"+"").getBytes(), Base64.NO_WRAP);
		httpPost.setHeader("Authorization",basicAuth);

		httpPost.setEntity(multipartEntityBuilder.build());

		StringBuilder builder = new StringBuilder();

		try {
			HttpResponse response = httpClient.execute(httpPost);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			Log.e("Response status code", "res = " + statusCode);
			Log.e("clicked", "check 4");

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				Log.e("Response", "res = " + builder.toString());

				try {
					jsonObject = new JSONObject(builder.toString());
				} 
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 

			else {
				Log.e(TAG, "statusCode" +statusCode);
			}

		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();
		}

		return jsonObject;
	}

	public static JSONObject putDataToUrl(String strUrl, List<NameValuePair> params) {
		JSONObject jsonObject = null;

		HttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(strUrl);
		Log.d("Login url",strUrl);
		// Url Encoding the PUT parameters
		try {
			httpPut.setEntity(new UrlEncodedFormEntity(params));
		} 
		catch (UnsupportedEncodingException e) {
			// writing error to Log
			Log.d(TAG, "Unsupported Encoding Exception.");
			e.printStackTrace();
		}

		StringBuilder builder = new StringBuilder();
		// Making HTTP Request
		try {
			HttpResponse response = httpClient.execute(httpPut);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			Log.i(TAG, "Request URL : " + strUrl);
			Log.i(TAG, "Parameters : " + httpPut.getEntity());
			Log.i(TAG, "Request Headders : " + httpPut.getAllHeaders());
			Log.i(TAG, "Response code : " + statusCode);
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				Log.e(TAG, "response: " + builder.toString());

				try {

					jsonObject = new JSONObject(builder.toString());
				} 
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else {
				Log.e(TAG, "statusCode  = " +statusCode);
				Log.e(TAG, "response: " + builder.toString());
			}

		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();
		}

		return jsonObject;
	}

	public static JSONObject getJSONFromUrl(String url) {
		// Making HTTP request
		Log.d("url", url);
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			jsonString = sb.toString();
			Log.d(TAG, "response: " + sb.toString());

		} catch (Exception e) {
			Log.e(TAG, "Buffer Error : Error converting result " + e.toString());
		}
		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(jsonString);
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing data " + e.toString());
		}
		// return JSON String
		return jObj;
	}

	
	public static String getStringFromJSONObject(JSONObject jsonObject, String getTag) {
		String tagValue = "";

		try {
			tagValue = jsonObject.getString(getTag);
			if(tagValue.equalsIgnoreCase("null")) {
				tagValue = "";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return tagValue;
	}
}
