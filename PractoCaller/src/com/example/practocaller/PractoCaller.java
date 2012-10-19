/* Sole purpose of this class is to get the DB populated in order to test the app
 * Three fields are populated name, contact number and address
 */
package com.example.practocaller;

import com.practo.dbhelper.SqlDBHelper;
import com.practo.phonestatereceiver.PhoneStateReceiver;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PractoCaller extends Activity {
	private SqlDBHelper sqlHelper;
	private EditText txtName;
	private EditText txtNumber;
	private EditText txtAddress;
	private String strSex;
	private static final int BUTTON_FINISH_TAG = 100;
	private static final int BUTTON_CREATE_TAG = 101;

	public static final String MALE = "M";
	public static final String FEMALE = "F";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enter_contact_details);
        initializeViewsAndListeners();
    }
    private void initializeViewsAndListeners() {
		// TODO initializeViewsAndListeners method stub
    	strSex = MALE;
		Button button = (Button)findViewById(R.id.button1);
		button.setTag(BUTTON_FINISH_TAG);
		button.setOnClickListener(lButtonListener);
		button = (Button)findViewById(R.id.button2);
		button.setTag(BUTTON_CREATE_TAG);
		button.setOnClickListener(lButtonListener);

		txtName = (EditText)findViewById(R.id.editText1);
		txtNumber = (EditText)findViewById(R.id.editText2);
		txtAddress = (EditText)findViewById(R.id.editText3);
	}
    private OnClickListener lButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int buttonTag = (Integer)v.getTag();
			switch (buttonTag) {
			case BUTTON_CREATE_TAG:
				if(validateInput()==true){
					sqlHelper = new SqlDBHelper(PractoCaller.this);
			        sqlHelper.getWritableDatabase();
			        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup1);
			        int selectedId= radioGroup.getCheckedRadioButtonId();
			        RadioButton radioButton = (RadioButton)findViewById(selectedId);
			        if(radioButton.getText().toString().trim().equalsIgnoreCase("male")){
			        	strSex = MALE;
			        }else{
			        	strSex = FEMALE;
			        }
					ContentValues contactContentValues = new ContentValues();
					contactContentValues.put(SqlDBHelper.contactName, txtName.getText().toString().trim());
					contactContentValues.put(SqlDBHelper.contactNumber, txtNumber.getText().toString().trim());
					contactContentValues.put(SqlDBHelper.contactAddress, txtAddress.getText().toString().trim());
					contactContentValues.put(SqlDBHelper.contactSex, strSex);
					boolean isSuccess = sqlHelper.insertIntoTable(contactContentValues);
					if(isSuccess==true){
						txtName.setText("");
						txtNumber.setText("");
						txtAddress.setText("");
					}
					
				}
					break;
			case BUTTON_FINISH_TAG:
					finish();
				break;
			}
		}
	};

	private boolean validateInput(){
		String toastMessage = "";
		if((txtName.getText().toString().trim().length()==0)||(txtNumber.getText().toString().trim().length()==0)){
			toastMessage = getResources().getString(R.string.validate_empty_fields);
			Toast.makeText(PractoCaller.this, toastMessage, Toast.LENGTH_SHORT).show();
			return false;
//		}else if(txtNumber.getText().toString().trim().length()!=10){
//			toastMessage = getResources().getString(R.string.validate_phone_field);
//			Toast.makeText(EnterContactDetails.this, toastMessage, Toast.LENGTH_SHORT).show();
//			return false;
		}else{
			return true;
		}
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.enter_contact_details, menu);
        return true;
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
    
}
