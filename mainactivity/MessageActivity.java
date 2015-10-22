package com.kryptogram.mainactivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.encryptext.mainactivity.R;
import com.kryptogram.project.decrypt.Decryption;
import com.kryptogram.project.encrypt.Encryption;

public class MessageActivity extends Activity {
	private static final int REQUEST_CONTACT_NUMBER = 123456789;
	public static String STEALTH_MODE = "0";
	private ImageButton contactButton;
	private ImageButton decryptEye;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String savedPassword = StartActivity.SET_PASSWORD;
		if (!savedPassword.equals("")) {
			if (!StartActivity.PASSWORD.equals(savedPassword)) {
				StartActivity.PASSWORD = "";
				finish();
			}
		}
		StartActivity.PASSWORD = "";
		super.setTheme(android.R.style.Theme_Holo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setEditTextSizes();
		setContactButtonListener();
		setEyeButtonListner();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		String password = getPassword();
		if (password.equals("")) {
			getMenuInflater().inflate(R.menu.main, menu);
		} else {
			getMenuInflater().inflate(R.menu.main2, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.help) {
			Intent intent = new Intent(this, HelpPage.class);
			startActivity(intent);
			return super.onOptionsItemSelected(item);
		} else if (id == R.id.stealthmode) {
			if (STEALTH_MODE.equals("0")) {
				showToast(getResources().getString(R.string.stealth_enanble),R.dimen.toastGetMsgLocation2, Toast.LENGTH_SHORT, R.dimen.textsize_small);
				STEALTH_MODE = "1";
			} else {
				showToast(getResources().getString(R.string.stealth_disable),R.dimen.toastGetMsgLocation2, Toast.LENGTH_SHORT, R.dimen.textsize_small);
				STEALTH_MODE = "0";
			}
			return super.onOptionsItemSelected(item);
		} else if (id == R.id.setpassword){
			Intent intent = new Intent(this, SetPassword.class);
			startActivity(intent);
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void setContactButtonListener() {
		final Vibrator buttonVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		contactButton = (ImageButton) findViewById(R.id.button1);
		contactButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
			    case MotionEvent.ACTION_DOWN:
			        buttonVib.vibrate(12);
			        break;
			    case MotionEvent.ACTION_UP:
			    	// opens the "choose contact" window
			    	Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Phone.CONTENT_URI);
					startActivityForResult(contactPickerIntent, REQUEST_CONTACT_NUMBER);
			        break;
			    default:
			        break;
			    }
			    return true;
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (data != null && requestCode == REQUEST_CONTACT_NUMBER) {
				Uri uriOfPhoneNumberRecord = data.getData();
				String idOfPhoneRecord = uriOfPhoneNumberRecord.getLastPathSegment();
				Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, new String[] { Phone.NUMBER }, Phone._ID + "=?", new String[] { idOfPhoneRecord }, null);
				if (cursor != null) {
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						String formattedPhoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
						EditText textView = ((EditText) findViewById(R.id.editText1));
						textView.setText(formattedPhoneNumber);
					}
					cursor.close();
				}
			} else {
				Log.w("TestActivity", "WARNING: Corrupted request response");
			}
		} else if (resultCode == RESULT_CANCELED) {
			Log.i("TestActivity", "Popup canceled by user.");
		} else {
			Log.w("TestActivity", "WARNING: Unknown resultCode");
		}
	}

	public void sendButton(View v) {
		final EditText destinationAddress = (EditText) findViewById(R.id.editText1);
		final EditText messageText = (EditText) findViewById(R.id.editText2);

		String phoneNumber = destinationAddress.getText().toString();
		String text = messageText.getText().toString();
		Encryption encryption = new Encryption();
		String encryptMsg = encryption.encrypt(text, readFileToString("cypher.txt"));
		
		if (text.equals("")) {
			showToast(getResources().getString(R.string.empty_message), R.dimen.toastGetMsgLocation2, Toast.LENGTH_SHORT, R.dimen.textsize_small);
		} else {
			if (phoneNumber.equals("")) {
				showToast(getResources().getString(R.string.empty_contact), R.dimen.toastGetMsgLocation2, Toast.LENGTH_SHORT, R.dimen.textsize_small);
			} else {
				SmsManager sms = SmsManager.getDefault();
				String appName = getResources().getString(R.string.app_name);
				ArrayList<String> longSMS = sms.divideMessage(appName + "!\n" + encryptMsg);
				sms.sendMultipartTextMessage(phoneNumber, null, longSMS, null, null);
				showToast(getResources().getString(R.string.message_sent), R.dimen.toastGetMsgLocation2, Toast.LENGTH_SHORT, R.dimen.textsize_small);
				messageText.setText("");
			}
		}
	}
	
	public void getLastEncryptedText(View v) {
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
		cursor.moveToFirst();
		String messageString = null;
		String replyAddress = null;
		String replyPerson = null;

		boolean checkingForEncryption = true;
		int position = 0;
		while (checkingForEncryption) {
			if (position < 100) {
				messageString = cursor.getString(cursor.getColumnIndex("body"));
				replyAddress = cursor.getString(cursor
						.getColumnIndex("address"));
				replyPerson = getContactName(getApplicationContext(),
						replyAddress);
				String[] messageWords = messageString.split("\n");
				if (messageWords[0].equals("KryptoGram!")) {
					Decryption decryption = new Decryption();
					String justEncryption = messageString.substring(12);
					String decryptedMsg = decryption.decrypt(justEncryption,
							readFileToString("cypher.txt"));
					String finalMessage = replyPerson + ": " + decryptedMsg;

					showToast(finalMessage, R.dimen.toastGetMsgLocation1,
							Toast.LENGTH_LONG, R.dimen.textsize_med);
					EditText textView = ((EditText) findViewById(R.id.editText1));
					textView.setText(replyAddress);
					checkingForEncryption = false;
				} else {
					position++;
					cursor.moveToPosition(position);
				}
			} else {
				showToast(getResources().getString(R.string.no_avail_msgs),
						R.dimen.toastGetMsgLocation1, Toast.LENGTH_SHORT, R.dimen.textsize_small);
				break;
			}
		}
	}
	
    public String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
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
		
	public String readFileToString(String fileName) {
		StringBuffer fileData = new StringBuffer("");
		try {
			InputStream fileIn = getAssets().open(fileName);
			InputStreamReader streamReader = new InputStreamReader(fileIn);
			BufferedReader buffreader = new BufferedReader(streamReader);

			String readString = buffreader.readLine();
			while (readString != null) {
				fileData.append(readString);
				readString = buffreader.readLine();
			}

			streamReader.close();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return fileData.toString();
	}
	
	public void setEditTextSizes() {
		EditText contactText = (EditText) findViewById(R.id.editText1);
		EditText decryptText = (EditText) findViewById(R.id.decryptEditText);
		EditText msgText = (EditText) findViewById(R.id.editText2);
		contactText.setTextSize(getResources().getDimension(R.dimen.textsize_med));
		decryptText.setTextSize(getResources().getDimension(R.dimen.textsize_med));
		msgText.setTextSize(getResources().getDimension(R.dimen.textsize_med));
	}
	
	public void setEyeButtonListner() {
		final Vibrator buttonVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		decryptEye = (ImageButton) findViewById(R.id.button4);
		decryptEye.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
			    case MotionEvent.ACTION_DOWN:
			        buttonVib.vibrate(12);
			        break;
			    case MotionEvent.ACTION_UP:
			    	final EditText encryptedMsg = (EditText) findViewById(R.id.decryptEditText);
					String encryptedText = encryptedMsg.getText().toString();
					String encryptedString = null;
					String subString;
					try {
						subString = encryptedText.substring(0, 12);
					} catch (Exception e) {
						subString = encryptedText;
						e.printStackTrace();
					}
					String decryptedMsg = null;
					Decryption decryption = new Decryption();
					
					if (subString.equals("KryptoGram!\n")) {
						encryptedString = encryptedText.substring(12);
					} else {
						encryptedString = encryptedText;
					}

					if (decryption.decrypt(encryptedString, readFileToString("cypher.txt")) != null) {
						decryptedMsg = decryption.decrypt(encryptedString, readFileToString("cypher.txt"));
						showToast(decryptedMsg, R.dimen.toastGetMsgLocation1, Toast.LENGTH_LONG, R.dimen.textsize_med);
					} else {
						showToast(getResources().getString(R.string.try_again), R.dimen.toastGetMsgLocation1, Toast.LENGTH_LONG, R.dimen.textsize_small);
					}
			        break;
			    default:
			        break;
			    }
			    return true;
			}
		});
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
	
}
