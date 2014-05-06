package com.example.nfcapp.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfcapp.R;
import com.example.nfcapp.utilClasses.SocketClass;

public class DisplayRoomStatusActivity extends Activity {

	public boolean serverUnavailableFlag = false;
	public ArrayList<String> list = null;
	private int trigger = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_room_status);
		
		Intent receivedIntent = getIntent();
		trigger = receivedIntent.getIntExtra(SendDataToServerActivity.TRIGGER, 0);
		displayRoomStatus();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	    finish();
	}
	
	public void displayRoomStatus() {
		TextView dataField = (TextView) findViewById(R.id.displayRoomStatusHeader);
 		dataField.setText("Loading...");
 		list = null;
		RequestDataFromServerTask task = new RequestDataFromServerTask();
		task.execute();
	}
	
	public void createLayoutForDisplay() {
		ListView listView = (ListView) findViewById(R.id.displayRoomStatusDetails);
		StableArrayAdapter adapter = new StableArrayAdapter(this, 
			        android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		
		if (trigger == 1) {
			if (SendDataToServerActivity.serverResponse != null) {
				String ack = new String(SendDataToServerActivity.serverResponse);
				System.out.println(ack);
				/*TextView myTextView = (TextView) findViewById(R.id.displayAcknowledgement);
				myTextView.setText(ack);*/
				Toast.makeText(this, ack, Toast.LENGTH_SHORT).show();
			} else {
				System.out.println("ERROR: Didn't receive acknowledgement from server.");
			}
		}
	}
	
	private class RequestDataFromServerTask extends AsyncTask<Void , Void, Void> {
		 
		public String createMessage() {
			StringBuilder data = new StringBuilder(getResources().getString(R.string.room_status_message));
			data.append(" ");
			data.append(MainActivity.USERNAME);
			System.out.println(data);
			return data.toString();
		}
		
		public void parseMessageAndCreateLayout(String text) {
			JSONObject jsonObj = null;
			JSONArray results = null;
			int i = 0;
			System.out.println(text);
			try {
				jsonObj = new JSONObject(text);
				results = jsonObj.getJSONArray("Library_db");
				list = new ArrayList<String>();
			} catch (JSONException e) {
				System.out.println("String to JSON or to Array error: " + e.getMessage());
			}
			while (!results.isNull(i)) {
				try {
					JSONObject result = results.getJSONObject(i);
					String roomNo = (String) result.get("Room_no");
					roomNo.trim();
					if ((Integer) result.get("Flag") == 0) {
						list.add(new String("            " + roomNo + "      :        Free            "));
					} else {
						list.add(new String("            " + roomNo + "      :      Occupied          "));
					}
				} catch (JSONException e) {
					System.out.println("ERROR: " + e.getMessage());
				}
				i++;
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
				
				message = new String(createMessage());
				//System.out.println(message);
				clientSocket.writeToSocket(message);
				
				clientSocket.openInputStream();
				
				if ((receivedText = clientSocket.readFromSocket()) != null) {
					receivedMessage.append(receivedText);
				}
				
				System.out.println("JSON: " + receivedMessage);
				
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
	 			dataField.setText(R.string.server_unavailable);
	 			serverUnavailableFlag = false;
	 		} else {
	 			dataField.setText("Room Occupancy Details");
	 			createLayoutForDisplay();
	 		}
	 	}
	}
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }
	}
	
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
				
			case R.id.action_logout:
				deleteFile("NFCAppPrivateData");
				Intent logoutIntent = new Intent(this, MainActivity.class);
				startActivity(logoutIntent);
				finish();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
