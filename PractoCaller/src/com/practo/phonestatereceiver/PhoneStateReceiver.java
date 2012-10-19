/* The receiver class which will listen to the incoming call and get the number 
 * from the extras and start a service with the contact number sent as a extra along with 
 * the intent
 */
package com.practo.phonestatereceiver;

import com.example.practocaller.ContactOverlay;
import com.example.practocaller.PhoneContactService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
public class PhoneStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle phoneStatExtras = intent.getExtras();
		Log.d("PhoneStateReceiver ", "PhoneStateReceiver " );
	    if (phoneStatExtras != null) {
	      String state = phoneStatExtras.getString(TelephonyManager.EXTRA_STATE);
	      Log.w("DEBUG", state);
	      if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
	        String phoneNumber = phoneStatExtras
	            .getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
	        try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        context.startService(new Intent(context,PhoneContactService.class).putExtra(ContactOverlay.PHONE_NUMBER_EXTRA_TAG,phoneNumber));
	        Log.d("Incoming Number ","No" +  phoneNumber);
	      }
	    }
	  }
}
