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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListRepos extends Activity {
	String user,responseString,selection;
	Intent i;
	ArrayList<String> repository=new ArrayList<String>();
	ListView lvMain;	
	ArrayAdapter<String> adapter;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listrep);
		lvMain=(ListView)findViewById(R.id.lvMain);
		Bundle extras = getIntent().getExtras();		
		if (extras != null) {
		    user = extras.getString("username");		    
		}		
		new RequestTaskRep().execute("https://api.github.com/users/"+user+"/repos");		
	}
	class RequestTaskRep extends AsyncTask<String, String, String>{    	
	    @Override
	    protected void onPreExecute()
	    {
	    	repository.clear();
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
            adapter= new ArrayAdapter<String>(ListRepos.this,
            		R.layout.my_list, repository);
            lvMain.setAdapter(adapter);
            lvMain.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                	selection = lvMain.getItemAtPosition(position).toString();
                	i=new Intent(ListRepos.this, ShowProperty.class);
                	i.putExtra("name", selection);
                	i.putExtra("username", user);
                	startActivity(i);
                }
              });
        }
    }
	  void parseDataRep(String data) throws JSONException {
      	JSONObject jsonObj;
      	JSONArray json_array=new JSONArray(data);    	
      	for(int i=0; i<json_array.length();i++){
      		jsonObj=json_array.getJSONObject(i);
          	repository.add(jsonObj.getString("name"));            		        	            		
      	}    		    			    			    			            		                                       		    	       		    		    
      }
}
