package com.example.githubapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener{	
	EditText etLogin,etPass;	
	Button btnLogin;
	String pass,user,result,responseString;
	TextView tvRepos;
	LinearLayout llMain;
	LinearLayout.LayoutParams lParams;
	StatusLine statusLine;
	int status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		llMain=(LinearLayout)findViewById(R.id.llMain);
		etLogin=(EditText)findViewById(R.id.etLogin);
		etPass=(EditText)findViewById(R.id.etPass);		
		btnLogin = (Button) findViewById(R.id.btnLogin);
	    btnLogin.setOnClickListener(this);	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void onClick(View v) {		
		switch(v.getId()){		
    		case R.id.btnLogin:
    			user=etLogin.getText().toString();
    			pass=etPass.getText().toString();
    			if(pass.equals("")){
    				Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();    				
    			}    			
    			else   			
    				new RequestTask().execute("https://api.github.com/user");    			 				    				    			    			
    			break;
		}
	}
	class RequestTask extends AsyncTask<String, String, String> {    	            		    	    	    		
		@Override
        protected void onPreExecute() {	                            	                            
                super.onPreExecute();
                }
		@Override
	    protected String doInBackground(String... params) {
			String result=new String("");
			try{
				HttpResponse response;
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(params[0]);				
				get.setHeader("Authorization: Basic ", 
						Base64.encodeToString((user+":"+pass).getBytes(), Base64.NO_WRAP));	
				
				get.addHeader("Content-Type: ","application/json;");																			
				response= client.execute(get);
				statusLine = response.getStatusLine();				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();                    								
			} 
			catch (org.apache.http.client.ClientProtocolException e) {
				result = "ClientProtocolException: " + e.getMessage();
			} 
			catch (IOException e) {
				result = "IOException: " + e.getMessage();
			} 
			catch (Exception e) {
				result = "Exception: " + e.getMessage();
			}
	        return null;
        }
		@Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent intent;
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            	intent=new Intent(MainActivity.this, ListRepos.class);
            	intent.putExtra("username", user);
            	startActivity(intent);								
			}
			else{
				Toast.makeText(MainActivity.this, "Wrong data", Toast.LENGTH_LONG).show();	
			}            
        }
	}
	
      
}
