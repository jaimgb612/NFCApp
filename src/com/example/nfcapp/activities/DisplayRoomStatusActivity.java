package com.example.nfcapp.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.nfcapp.R;
import com.example.nfcapp.utilClasses.SocketClass;

public class DisplayRoomStatusActivity extends Activity {

	public boolean serverUnavailableFlag = false;
	public String textField = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_room_status);
		//setupActionBar();
		
		@SuppressWarnings("unused")
		Intent receivedIntent = getIntent();
		displayRoomStatus();
	}
	
	public void displayRoomStatus() {
		TextView dataField = (TextView) findViewById(R.id.displayRoomStatusHeader);
 		dataField.setText("Room Occupancy Table");
		RequestDataFromServerTask task = new RequestDataFromServerTask();
		task.execute();
	}
	
	private class RequestDataFromServerTask extends AsyncTask<Void , Void, Void> {
		 
		public String createMessage() {
			StringBuilder data = new StringBuilder("MESSAGEB");
			//data.append(" ");
			//data.append(MainActivity.USERNAME);
			System.out.println(data);
			return data.toString();
		}
		
		public void parseMessageAndCreateLayout(String text) {
			JSONObject jsonObj = null;
			JSONArray results = null;
			try {
				jsonObj = new JSONObject(text);
				results = jsonObj.getJSONArray("data");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("String to JSON or to Array error");
			}
			
		}
		
		protected Void doInBackground(Void... args) {
		        String message = null;
		        String receivedText = null;
		        StringBuilder receivedMessage = new StringBuilder();
				SocketClass clientSocket = new SocketClass();

				if (clientSocket.createSocket() == -1) {
					serverUnavailableFlag = true;
					return null;
				}
				
				clientSocket.openOutputStream();
				//objUtil.openInputStream();
				
				message = new String(createMessage());
				System.out.println(message);
				clientSocket.writeToSocket(message);
				
				clientSocket.openInputStream();
				if ((receivedText = clientSocket.readFromSocket()) != null) {
					receivedMessage.append(receivedText);
				}
				
				System.out.println("JSON: " + receivedMessage);
				
				//textField = new String("{\"data\":" + receivedMessage + "}");
				
				//Below two lines are meant to be used, correct output on server and use below code
				//textField = new String(receivedMessage);
				parseMessageAndCreateLayout(receivedMessage.toString());
				
				//close
				clientSocket.closeSocket();
				clientSocket = null;
				message = null;
			    return null;
		 }

	     protected void onPostExecute(Void arg) {
	    	 setupDisplayAfterLoad();
	     }
	     
	     public void setupDisplayAfterLoad() {
	 		TextView dataField = (TextView) findViewById(R.id.displayRoomStatusHeader);
	 		
	 		if (serverUnavailableFlag == true) {
	 			dataField.setText("Server unavailable");
	 			serverUnavailableFlag = false;
	 		} else {
	 			dataField = (TextView) findViewById(R.id.displayRoomStatusDetails);
	 			dataField.setText(textField);
	 		}
	 	}
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	/*private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_room_status, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				NavUtils.navigateUpFromSameTask(this);
				return true;
			
			case R.id.action_refresh:
				/*Intent refreshIntent = new Intent(this, DisplayRoomStatusActivity.class);
				startActivity(refreshIntent);
				finish();*/
				displayRoomStatus();
				break;
			
			case R.id.action_about:
				Intent aboutIntent = new Intent(this, AboutActivity.class);
				startActivity(aboutIntent);
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}


}
