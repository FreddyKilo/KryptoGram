package com.kryptogram.mainactivity;

import com.encryptext.mainactivity.R;

import android.app.Activity;
import android.os.Bundle;

public class HelpPage extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.setTheme(android.R.style.Theme_Holo_NoActionBar);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.options_help);
		
	}
}
