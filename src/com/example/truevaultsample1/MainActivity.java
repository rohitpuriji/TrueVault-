package com.example.truevaultsample1;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.evon.utills.Constants;
import com.evon.utills.JsonParser;


public class MainActivity extends Activity {

	private EditText etUserName;
	private EditText etUserPswd;
	private Button btnCreateUser;
	private Button btnGetUser;
	private String user_name;
	private String user_pass;
	private String KEY_USERNAME = "username";
	private String KEY_PASSWORD = "password";
	private String KEY_ATTRIBUTES = "attributes";
	private String KEY_SCHEMAID = "schemaId";
	private String TAG_STATUS = "result";
	private String TAG_TRANSACTION_ID = "transaction_id";
	private String TAG_ACCESS_TOKEN = "access_token";
	private String TAG_ACCOUNT_ID = "account_id";
	private String TAG_API_KEY = "api_key";
	private String TAG_ID = "id";
	private String TAG_USER_STATUS= "status";
	private String TAG_USER_ID = "user_id";
	private String TAG_USER_NAME = "username";
	private String userId;
	private String userApiKey;






	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  
		setContentView(R.layout.activity_main);

		etUserName = (EditText)findViewById(R.id.etUserName);
		etUserPswd = (EditText)findViewById(R.id.etUserPswd);
		btnCreateUser = (Button)findViewById(R.id.btnCreateUser);
		btnGetUser = (Button)findViewById(R.id.btnGetUser);

		btnCreateUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new CreateUserTask(etUserName.getText().toString(), etUserPswd.getText().toString()).execute();
				Log.e("clicked", "check 1");
			}
		});


		btnGetUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new ReadUserTask().execute();

			}
		});

	}

	public class ReadUserTask extends AsyncTask<String, Void, JSONObject>{


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true); 

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			entityBuilder.addTextBody(TAG_USER_ID, userId);

			Log.e("clicked", "check 2");
			
			JSONObject jsonObject = JsonParser.postDataToUrl(Constants.trueVault_getUser_url,entityBuilder);

			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject JSONObject) {
			super.onPostExecute(JSONObject);

			setProgressBarIndeterminateVisibility(false); 

			if(JSONObject != null) {
				try {

					String result = JSONObject.getString(TAG_STATUS);
					Log.e("Response from true vault getting user", "res = " + result);

				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}




	public class CreateUserTask extends AsyncTask<String, Void, JSONObject>{

		public CreateUserTask(String userName,String userPswd) {
			user_name = userName;
			user_pass = userPswd;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true); 

		}

		@Override
		protected JSONObject doInBackground(String... params) {

			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			entityBuilder.addTextBody(KEY_USERNAME, user_name);
			entityBuilder.addTextBody(KEY_PASSWORD, user_pass);
			entityBuilder.addTextBody(KEY_ATTRIBUTES, "");
			entityBuilder.addTextBody(KEY_SCHEMAID, "");

			Log.e("clicked", "check 2");

			JSONObject jsonObject = JsonParser.postDataToUrl(Constants.trueVault_createUser_url,entityBuilder);

			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject JSONObject) {
			super.onPostExecute(JSONObject);

			setProgressBarIndeterminateVisibility(false); 

			if(JSONObject != null) {
				try {

					String result = JSONObject.getString(TAG_STATUS);
					Log.e("Response from true vault", "res = " + result);
					userId = JSONObject.getJSONObject("user").getString(TAG_USER_ID);				
					userApiKey = JSONObject.getJSONObject("user").getString(TAG_API_KEY);
					Constants.user_apiKey = userApiKey;
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}




}
