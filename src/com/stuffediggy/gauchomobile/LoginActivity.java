package com.stuffediggy.gauchomobile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.content.SharedPreferences;


public class LoginActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    setContentView(R.layout.login);
	    setProgressBarIndeterminateVisibility(false);
	}
	
	 public void loginToGauchoSpace(View v) {
		 Log.v("DEBUG", "Logging in...");
		 new HttpTask().execute("");
     }
	
	public final class HttpTask extends AsyncTask<String, Boolean, String> {

		private AndroidHttpClient client = AndroidHttpClient.newInstance("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/534.55.3 (KHTML, like Gecko) Version/5.1.3 Safari/534.53.10");
		
		@Override
		protected String doInBackground(String... params) {
			publishProgress(true);
			// Do the usual httpclient thing to get the result
			client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_0);
			HttpPost httppost = new HttpPost("https://gauchospace.ucsb.edu/courses/login/index.php");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        TextView username = (TextView) findViewById(R.id.username);
		        TextView password = (TextView) findViewById(R.id.password);
		        nameValuePairs.add(new BasicNameValuePair("username", username.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = client.execute(httppost);
		        String LocationHeader = response.getFirstHeader("location").getValue();

		        // To get the BODY I would have to parse that again - since its not REDIRECTING automatically
		        AndroidHttpClient httpclient2 = AndroidHttpClient.newInstance("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/534.55.3 (KHTML, like Gecko) Version/5.1.3 Safari/534.53.10");
		        HttpPost httppost2 = new HttpPost(LocationHeader);
		        response = httpclient2.execute(httppost2);
		        String result = EntityUtils.toString(response.getEntity());
		        return result;
		        
		    } catch (Exception e) {
		       Log.v("DEBUG", "Exception when logging in: " + e.toString());
		    }
		    
			return "";
		}

		@Override
		protected void onProgressUpdate(Boolean... progress) {
			// line below coupled with 
			//    getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS) 
			//    before setContentView 
			// will show the wait animation on the top-right corner
			LoginActivity.this.setProgressBarIndeterminateVisibility(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			publishProgress(false);
			
		    //Store the now validated username and password in the data source
		    TextView username = (TextView) findViewById(R.id.username);
	        TextView password = (TextView) findViewById(R.id.password);
		    DataSource ds = DataSource.getInstance();
		    ds.setUsername(username.getText().toString());
		    ds.setPassword(password.getText().toString());
			
			String[] lines = result.split("\n");
			for (int i = 0; i < lines.length; i++) {
				String thisLine = lines[i];
				if (thisLine.contains("name=\"sesskey\" value=\"")) {
					//Extract the session key from the body of the page
					thisLine = thisLine.substring(thisLine.indexOf("name=\"sesskey\" value=\"") + 22);
					thisLine = thisLine.substring(0, 10);
					
					//Store the session key, username and password in preferences
					SharedPreferences preferences = getPreferences(MODE_PRIVATE);
					SharedPreferences.Editor editor = preferences.edit();
				    editor.putString("sessionkey", thisLine);
				    editor.putString("username", username.getText().toString());
				    editor.putString("password", password.getText().toString());
				    editor.commit();
				    
					Log.v("DEBUG", "Session key: " + thisLine);
				}
			}
		}

	}

}
