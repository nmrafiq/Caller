package com.practo.dbhelper;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.SyncStateContract.Columns;
import android.util.Log;
import android.widget.Toast;
public class SqlDBHelper extends SQLiteOpenHelper {
	private Context context;
	private SQLiteDatabase practoDB;
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "practo_db";
	public static final String TABLE_NAME = "contacts";
	public static final String contactID="_id";
	public static final String contactName="Name";
	public static final String contactNumber="Number";
	public static final String contactAddress="Address";
	public static final String contactSex = "Sex";

	public SqlDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
		this.context = context;
		practoDB = context.openOrCreateDatabase(DB_NAME, 0, null); //db creation
	}
	public boolean insertIntoTable(ContentValues contentValues){ // to insert the values into the table
		String toastMessage;
		boolean isSuccess;
		String phoneNumber = (String)contentValues.get(contactNumber);
		Cursor c = practoDB.query(TABLE_NAME, null, contactNumber+"=?", 
								new String[]{phoneNumber}, null, null, null);
		Log.d("Cursor Count ", "Cursor Count " + c.getCount());
		if(c.getCount()!=0){
			toastMessage = "There already exists a contact with this number";
			isSuccess = false;
		}else{
			try{
				String sqlInsertQuery = "INSERT INTO " +  TABLE_NAME +  " VALUES ( null, ?, ?, ?, ?)";
				practoDB.execSQL(sqlInsertQuery, new String[]{(String)contentValues.get(contactName),
								(String)contentValues.get(contactNumber),
								(String)contentValues.get(contactAddress),
								(String)contentValues.get(contactSex)});

				toastMessage = "Contact Inserted Successfully";
				isSuccess = true;
			}catch (android.database.SQLException e) {
				// TODO: handle exception
				Log.d("Sql exception", e.toString());
				toastMessage = "Sorry an error occured";
				isSuccess = false;
			}
		}
		Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();	
		c.close();
		return isSuccess;
		
	}
	public Bundle getContactDetails(String phoneNumber) { // gets contact details from db based on phone number
		// TODO Auto-generated method stub
		String name = "";
		String address = "";
		String sex = "";
		Cursor cursor = practoDB.query(TABLE_NAME, null, contactNumber+"=?", new String[]{phoneNumber}, null, null, null);
		if (cursor.moveToFirst()){
			   do{
			      name = cursor.getString(cursor.getColumnIndex(contactName));
			      address = cursor.getString(cursor.getColumnIndex(contactAddress));
			      sex = cursor.getString(cursor.getColumnIndex(contactSex));
			      // do what ever you want here
			   }while(cursor.moveToNext());
			}
			cursor.close();
			practoDB.close();
			Bundle bundle = new Bundle();
			bundle.putString(contactName, name);
			bundle.putString(contactAddress, address);
			bundle.putString(contactSex, sex);
		return bundle;
	}
	@Override
	public void onCreate(SQLiteDatabase db) { // creates table if not created
		// TODO Auto-generated method stub
		Log.d("On create ", "On Create of helper class");
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME +
                " ( " + contactID + " INTEGER PRIMARY KEY  AUTOINCREMENT, " + 
                contactName + " TEXT, "+ contactNumber + " TEXT, " + contactAddress + " TEXT, " + contactSex + " TEXT);" );
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
