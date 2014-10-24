package com.example.githubapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ShowProperty extends Activity {
	String name,responseString,user,nameRepos,created_at,default_branch,description;
	int size;
	TextView tvShow;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.property);
		tvShow=(TextView)findViewById(R.id.tvShow);
		Bundle extras = getIntent().getExtras();		
		if (extras != null) {
		    name = extras.getString("name");
		    user=extras.getString("username");
		}		
		new RequestTaskRep().execute("https://api.github.com/users/"+user+"/repos");
	}
	class RequestTaskRep extends AsyncTask<String, String, String>{    	
	    @Override
	    protected void onPreExecute()
	    {	    	
	    };      
        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;            
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();                    
                    parseDataRep(responseString);                    
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } 
            catch (Exception e) {               
            }
            return responseString;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvShow.setText("Name of repository: "+name+
            		"\nSize: "+size+"Kb"+"\nCreated at: "+created_at+
            		"\nDefault branch: "+default_branch+
            		"\nDescription: "+description);
        }
    }
	  void parseDataRep(String data) throws JSONException {
		  JSONObject jsonObj;
		  JSONArray json_array=new JSONArray(data);    	
		  for(int i=0; i<json_array.length();i++){
			  jsonObj=json_array.getJSONObject(i);
			  nameRepos=jsonObj.getString("name");
			  if(nameRepos.equals(name)){
				  created_at=jsonObj.getString("created_at");
				  default_branch=jsonObj.getString("default_branch");
				  size=jsonObj.getInt("size");
				  description=jsonObj.getString("description");
			  }
		  }    		    			    			    			            		                                       		    	       		    		    
	  }
}
