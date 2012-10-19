/* this service class queries for the phone number in an async task and if there are results calls the\
 * ContactOverlay activity with contact details sent as bundle
 */
package com.example.practocaller;

import com.practo.dbhelper.SqlDBHelper;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.os.IBinder;
import android.util.Log;

public class PhoneContactService extends Service {
	private SqlDBHelper sqlDBHelper;
	private FindContactDetailsTask findContactDetailTask;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("On Create ", "On Create ");			
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
			Log.d("On start ", "On Start ");
			findContactDetailTask = new FindContactDetailsTask();
			sqlDBHelper = new SqlDBHelper(PhoneContactService.this);
			sqlDBHelper.getReadableDatabase();
			String phoneNumber = intent.getStringExtra(ContactOverlay.PHONE_NUMBER_EXTRA_TAG);
			Log.d("Phone Number in Service ", "Phone Number Service " + phoneNumber);
			if(findContactDetailTask.getStatus()==Status.RUNNING){
				findContactDetailTask.cancel(true);
			}
			findContactDetailTask = new FindContactDetailsTask();
			findContactDetailTask.execute(phoneNumber);
			return START_NOT_STICKY;
	}
	private class FindContactDetailsTask extends AsyncTask<String, Void, Bundle>{

		@Override
		protected Bundle doInBackground(String... phoneNumber) {
			// TODO Auto-generated method stub
			Bundle contactBundle = sqlDBHelper.getContactDetails(phoneNumber[0]);
			return contactBundle;
		}
		@Override
		protected void onPostExecute(Bundle contactBundle) {
			// TODO Auto-generated method stub
			super.onPostExecute(contactBundle);
			sqlDBHelper.close();
			Log.d("Cursor Count ", "Cursor Count " + contactBundle);
			String contactName = contactBundle.getString(SqlDBHelper.contactName);
			if(contactName.length()>0){
				Intent toPop = new Intent(PhoneContactService.this,ContactOverlay.class);
				toPop.putExtra(ContactOverlay.PHONE_NUMBER_EXTRA_TAG, contactBundle);
		        toPop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(toPop);
			}
		}
	}

}
