package com.kryptogram.mainactivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.encryptext.mainactivity.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SetPassword extends Activity{
	
	public static String PASSWORD = "";
	public static String PASSWORD_FILE = "password.txt";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		super.setTheme(android.R.style.Theme_Holo_NoActionBar);
		setContentView(R.layout.activity_set_password);
		Button passwordKey = (Button) findViewById(R.id.button_10);
		if (getPassword().equals("")) {
			passwordKey.setText(R.string.set_password);
		} else {
			passwordKey.setText(R.string.remove_password);
		}
		showToast(getResources().getString(R.string.select_pattern), R.dimen.toastGetMsgLocation2, Toast.LENGTH_LONG, R.dimen.textsize_med);
    }
	
	public void onBackPressed() {
		PASSWORD = "";
		finish();
	}
	
	public void button1(View v) {
		Button passwordKey = (Button) findViewById(R.id.button_10);
		passwordKey.setText(R.string.set_password);
        PASSWORD += "1";
    }

    public void button2(View v) {
    	Button passwordKey = (Button) findViewById(R.id.button_10);
		passwordKey.setText(R.string.set_password);
        PASSWORD += "2";
    }

    public void button3(View v) {
    	Button passwordKey = (Button) findViewById(R.id.button_10);
		passwordKey.setText(R.string.set_password);
        PASSWORD += "3";
    }

    public void button4(View v) {
    	Button passwordKey = (Button) findViewById(R.id.button_10);
		passwordKey.setText(R.string.set_password);
        PASSWORD += "4";
    }

    public void button5(View v) {
    	Button passwordKey = (Button) findViewById(R.id.button_10);
		passwordKey.setText(R.string.set_password);
        PASSWORD += "5";
    }

    public void button6(View v) {
    	Button passwordKey = (Button) findViewById(R.id.button_10);
		passwordKey.setText(R.string.set_password);
        PASSWORD += "6";
    }

    public void button7(View v) {
    	Button passwordKey = (Button) findViewById(R.id.button_10);
		passwordKey.setText(R.string.set_password);
        PASSWORD += "7";
    }

    public void button8(View v) {
    	Button passwordKey = (Button) findViewById(R.id.button_10);
		passwordKey.setText(R.string.set_password);
        PASSWORD += "8";
    }

    public void button9(View v) {
    	Button passwordKey = (Button) findViewById(R.id.button_10);
		passwordKey.setText(R.string.set_password);
        PASSWORD += "9";
    }
    
    public void button10(View v) {
    	setPassword();
    	if (PASSWORD.equals("")) {
			showToast(getResources().getString(R.string.password_removed),
					R.dimen.toastGetMsgLocation2, Toast.LENGTH_SHORT,
					R.dimen.textsize_small);
		} else {
			showToast(getResources().getString(R.string.password_set),
					R.dimen.toastGetMsgLocation2, Toast.LENGTH_SHORT,
					R.dimen.textsize_small);
		}
		PASSWORD = "";
    	finish();
    }
    
    public String getPassword() {

        try {
            InputStream inputStream = openFileInput(StartActivity.PASSWORD_FILE);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                StartActivity.SET_PASSWORD = stringBuilder.toString();
            }
        } 
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return StartActivity.SET_PASSWORD;
    }
    
    public void setPassword(){
    	try {
			FileOutputStream fos = openFileOutput(PASSWORD_FILE, Context.MODE_PRIVATE);
			fos.write(PASSWORD.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
