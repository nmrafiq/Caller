/* This is the overlay activity which will be called if the incoming phone number matches with 
 * the db contact. The overlay comes over the stock dialer. And on dissconnecting or answering the phone   
 * the activity finishes
 */
package com.example.practocaller;

import com.practo.dbhelper.SqlDBHelper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactOverlay extends Activity {
	public static final String PHONE_NUMBER_EXTRA_TAG = "phone_tag";
	private String phoneNumber;
	private SqlDBHelper sqlDBHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_overlay);
		Log.d("Inside onCreate", "ContactOverlay");
		Bundle bundle = this.getIntent().getBundleExtra(PHONE_NUMBER_EXTRA_TAG);
		Log.d("Inside onCreate", "ContactOverlay " +phoneNumber);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		mTelephonyManager.listen(new CustomPhoneStateListener(this), PhoneStateListener.LISTEN_CALL_STATE);
		
		TextView txtName = (TextView)findViewById(R.id.textView1);
		txtName.setText(bundle.getString(SqlDBHelper.contactName));
		TextView txtAddress = (TextView)findViewById(R.id.textView2);
		txtAddress.setText(bundle.getString(SqlDBHelper.contactAddress));
		ImageView imgView = (ImageView)findViewById(R.id.imageView1);
		if(bundle.get(SqlDBHelper.contactSex).equals(PractoCaller.MALE)){
			imgView.setBackgroundResource(R.drawable.user_icon);
		}else{
			imgView.setBackgroundResource(R.drawable.user_female);
		}
	}
	
	
	class CustomPhoneStateListener extends PhoneStateListener{
		Context context;
		public CustomPhoneStateListener(Context context){
			this.context = context;
		}
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			 switch (state) {
		        case TelephonyManager.CALL_STATE_IDLE:
		            //when Idle i.e no call
		            finish();
		            break;
		        case TelephonyManager.CALL_STATE_OFFHOOK:
		            finish();
		}
	}
	}
}
