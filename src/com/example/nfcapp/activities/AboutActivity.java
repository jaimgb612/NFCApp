package com.example.nfcapp.activities;

import com.example.nfcapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		@SuppressWarnings("unused")
		Intent receivedIntent = getIntent();
	}
}
