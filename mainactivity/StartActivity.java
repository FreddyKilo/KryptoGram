package com.kryptogram.mainactivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.encryptext.mainactivity.R;

public class StartActivity extends Activity {
	public static final int FLIP = 7;
	public static final int ENCRYPT_VAR_1 = 2;
	public static final int ENCRYPT_VAR_2 = 7;
	public static String PASSWORD = "";
	public static String SET_PASSWORD = "";
	public static String PASSWORD_FILE = "password.txt";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        String password = getPassword();
        if (password.equals("")) {
			onBackPressed();
		}
        if (!password.equals("")) {
			super.setTheme(android.R.style.Theme_Holo_NoActionBar);
			if (MessageActivity.STEALTH_MODE.equals("0")) {
				setContentView(R.layout.activity_password);
				showToast(getResources().getString(R.string.enter_password), R.dimen.toastGetMsgLocation2, Toast.LENGTH_SHORT, R.dimen.textsize_small);
			}else{
				setContentView(R.layout.activity_password_stealth);
				showToast(getResources().getString(R.string.you_know), R.dimen.toastGetMsgLocation2, Toast.LENGTH_SHORT, R.dimen.textsize_small);
			}
		}
    }
    
    public void onBackPressed(){
        gotoMessageActivity();
        finish();
    }
    
    public void button1(View v) {
		PASSWORD += "1";
		if (PASSWORD.equals(getPassword())) {
			onBackPressed();
		}
    }

    public void button2(View v) {
        PASSWORD += "2";
        if (PASSWORD.equals(getPassword())) {
            onBackPressed();
        }
    }

    public void button3(View v) {
        PASSWORD += "3";
        if (PASSWORD.equals(getPassword())) {
            onBackPressed();
        }
    }

    public void button4(View v) {
        PASSWORD += "4";
        if (PASSWORD.equals(getPassword())) {
            onBackPressed();
        }
    }

    public void button5(View v) {
        PASSWORD += "5";
        if (PASSWORD.equals(getPassword())) {
            onBackPressed();
        }
    }

    public void button6(View v) {
        PASSWORD += "6";
        if (PASSWORD.equals(getPassword())) {
            onBackPressed();
        }
    }

    public void button7(View v) {
        PASSWORD += "7";
        if (PASSWORD.equals(getPassword())) {
            onBackPressed();
        }
    }

    public void button8(View v) {
        PASSWORD += "8";
        if (PASSWORD.equals(getPassword())) {
            onBackPressed();
        }
    }

    public void button9(View v) {
        PASSWORD += "9";
        if (PASSWORD.equals(getPassword())) {
            onBackPressed();
        }
    }
    
    public void gotoMessageActivity() {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }
    
    public String getPassword() {

        try {
            InputStream inputStream = openFileInput(PASSWORD_FILE);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                SET_PASSWORD = stringBuilder.toString();
            }
        } 
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return SET_PASSWORD;
    }
    
    public void showToast(String message, int location, int time, int size){
    	Toast toastMsg;
		toastMsg = Toast.makeText(this, message, time);
		LinearLayout toastLayout = (LinearLayout) toastMsg.getView();
		TextView toastText = (TextView) toastLayout.getChildAt(0);
		toastText.setTextSize(getResources().getDimension(size));
		toastMsg.setGravity(Gravity.BOTTOM, 0, (int) getResources().getDimension(location));
		toastMsg.show();
		
    }
}
