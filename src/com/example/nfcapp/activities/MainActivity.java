package com.example.nfcapp.activities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nfcapp.R;

public class MainActivity extends Activity {

	public final static String SEND_DATA_TO_SERVER = "com.example.nfcapp.SEND_DATA_TO_SERVER";
	public final static String TRIGGER = "com.example.nfcapp.TRIGGER";
	public final static String NFC_DATA = "com.example.nfcapp.NFC_DATA";
	
	public final static int APP_OPEN_TRIGGER = 1;
	public final static int TAP_TRIGGER = 2;
	public final static int ERROR = -1;
	
	public static String USERNAME;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		start();
	}
	
	public void start() {
		Intent intent = getIntent();
		
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			onTap(intent);
		} else {
			appOpened();
		}
	}
	
	public void onTap(Intent intent) {
		int status = -1;
		String scannedString = getNFCTagData(intent);
		if (scannedString == null) {
			Toast.makeText(this, "ERROR: Problem with NFC tag", Toast.LENGTH_SHORT).show();
			finish();
		}
		status = checkIfUserIsRegistered();
		if (status == -1) {
			makeUserRegister(TAP_TRIGGER, scannedString);
		} else {
			sendDataToServer(scannedString);
		}
		finish();
	}
	
	public String getNFCTagData(Intent intent) {
		StringBuffer dispString = new StringBuffer();
		String holderString = new String();
		Toast.makeText(this, "NFC Tag detected", Toast.LENGTH_SHORT).show();
		
	    Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	    if (rawMsgs != null) {
	    	NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
	        for (int i = 0; i < rawMsgs.length; i++) {
	        	msgs[i] = (NdefMessage) rawMsgs[i];
	            try {
	            	holderString = readText(msgs[i]);
				} catch (UnsupportedEncodingException e) {
					//e.printStackTrace();
					Toast.makeText(this, "ERROR with TAG", Toast.LENGTH_SHORT).show();
				}
	            dispString.append(holderString);
	        }
	        return dispString.toString();
	    } else {
	    	return null;
	    }
		//setLayout(dispString);
	}
	
	String readText(NdefMessage msgs) throws UnsupportedEncodingException {
		NdefRecord[] myRecs = msgs.getRecords();
		
		for (NdefRecord ndefRecord : myRecs) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            	byte[] payload = ndefRecord.getPayload();
            	String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            	int languageCodeLength = payload[0] & 0063;
            	return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            }
        }
		return "ERROR";
	}
	
	public void sendDataToServer(String text) {
		Intent intentToSend = new Intent(this, SendDataToServerActivity.class);
		intentToSend.putExtra(SEND_DATA_TO_SERVER, text);
		startActivity(intentToSend);
	}
	
	void setLayout(StringBuffer text) {
		TextView textView = new TextView(this);
		textView.setText(text.toString());
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
		textView.setGravity(LinearLayout.HORIZONTAL | LinearLayout.VERTICAL);
		
		setContentView(textView);
	}
	
	public void appOpened() {
		//Toast.makeText(this, "App has been opened", Toast.LENGTH_SHORT).show();
		
		int status = -1;
		status = checkIfUserIsRegistered();
		if (status == 0) {
			displayRoomStatus();
		} else {
			makeUserRegister(APP_OPEN_TRIGGER);
		}
		finish();
	}
	
	public int checkIfUserIsRegistered() {
		String filename = new String("NFCAppPrivateData");
		byte[] buffer = new byte[64];
		String holder;
		String[] holders = null;
		FileInputStream fileStream = null;
		
		try {
			fileStream = openFileInput(filename);
			if (fileStream.read(buffer, 0, 64) < 0) {
				Toast.makeText(this, "ERROR: Please register again", Toast.LENGTH_SHORT).show();
			}
			holder = new String(buffer);
			holders = holder.split("\n");
			USERNAME = new String(holders[0]);
		} catch (FileNotFoundException e) {
			return -1;
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		try {
			fileStream.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		filename = null;
		holder = null;
		holders = null;
		buffer = null;
		return 0;
	}
	
	public void makeUserRegister(int trigger) {
		Intent intent = new Intent(this, UserRegistrationActivity.class);
		intent.putExtra(TRIGGER, trigger);
		startActivity(intent);
	}
	
	public void makeUserRegister(int trigger, String text) {
		Intent intent = new Intent(this, UserRegistrationActivity.class);
		intent.putExtra(TRIGGER, trigger);
		intent.putExtra(NFC_DATA, text);
		startActivity(intent);
	}
	
	public void displayRoomStatus() {
		//Toast.makeText(this, "Displaying Room Status", Toast.LENGTH_SHORT).show();
		Intent intentToDisplay = new Intent(this, DisplayRoomStatusActivity.class);
		startActivity(intentToDisplay);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
