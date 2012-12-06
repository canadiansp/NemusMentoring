package com.nemus.addressbook;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nemustech.tiffany.R;

public class DetailFragment extends Fragment implements TextWatcher {

    private View mContentView;
    private long mId = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.detail, null);
		
		Button btn = (Button)mContentView.findViewById(R.id.done);
		btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			    insertOrUpdateRow(mContentView);
				clearEditTexts();
				
				if(!MainActivity.IS_DUAL_PANE) {
				    FragmentTransaction ft = getFragmentManager().beginTransaction();
				    
				    Fragment frag = getFragmentManager().findFragmentByTag(MainActivity.TAG_LIST_FRAG);
				    if(frag != null) {
				        ft.replace(R.id.main_layout, frag);
				    } else {
				        ft.replace(R.id.main_layout, new ListAddressFragment());
				    }
				    ft.commit();
				}
			}
		});
		
		return mContentView;
	}
	
	public void setDataId(long id) {
	    mId = id;
	}
    
    @Override
    public void onResume() {
        super.onResume();
        hideDoneButton();
        
        if(mId != -1) {
            refreshDetailViewById(mId);
        } else {
            clearEditTexts();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mId = -1;
    }
    
    public void showDoneButton() {
        mContentView.findViewById(R.id.done).setVisibility(View.VISIBLE);
    }
    
    public void hideDoneButton() {
        mContentView.findViewById(R.id.done).setVisibility(View.INVISIBLE);
    }
    
    public void clearEditTexts() {
        final EditText lastName = (EditText)mContentView.findViewById(R.id.last_name);
        final EditText phoneNumber = (EditText)mContentView.findViewById(R.id.phone_number);
        final EditText address = (EditText)mContentView.findViewById(R.id.address);
        final EditText birthday = (EditText)mContentView.findViewById(R.id.birthday);
        
        //TODO removeTextWatcher
        lastName.removeTextChangedListener(this);
        phoneNumber.removeTextChangedListener(this);
        address.removeTextChangedListener(this);
        birthday.removeTextChangedListener(this);
        
        lastName.setText(null);
        phoneNumber.setText(null);
        address.setText(null);
        birthday.setText(null);
        
        //TODO addTextWatcher
        lastName.addTextChangedListener(this);
        phoneNumber.addTextChangedListener(this);
        address.addTextChangedListener(this);
        birthday.addTextChangedListener(this);
        
        hideDoneButton();
        
        mId = -1;
    }

	public void refreshDetailViewById(long id) {

		Cursor c = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI , 
				null, Contacts._ID + "=?",new String[]{String.valueOf(id)}, 
				Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		Log.d("pch2","id = " + id);

		c.moveToFirst();		

		final int idxLastName = c.getColumnIndexOrThrow(Contacts.DISPLAY_NAME);
		final int idxPhoneNumber = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
		EditText lastName = (EditText)mContentView.findViewById(R.id.last_name);
		EditText phoneNumber = (EditText)mContentView.findViewById(R.id.phone_number);
		
        lastName.removeTextChangedListener(this);
        phoneNumber.removeTextChangedListener(this);
	
		lastName.setText(c.getString(idxLastName));

		phoneNumber.setText(c.getString(idxPhoneNumber));
        lastName.addTextChangedListener(this);
        phoneNumber.addTextChangedListener(this);
	}
	
	public void insertOrUpdateRow(View view) {
		EditText lastName = (EditText)view.findViewById(R.id.last_name);
		EditText phoneNumber = (EditText)view.findViewById(R.id.phone_number);
		EditText address = (EditText)view.findViewById(R.id.address);
		EditText birthday = (EditText)view.findViewById(R.id.birthday);

		ContentResolver cr = getActivity().getContentResolver();

		ContentValues values = new ContentValues();
		values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,  phoneNumber.getText().toString());
		cr.update(RawContacts.CONTENT_URI, values, RawContacts.CONTACT_ID+"="+mId, null);
		Log.d("pch3", "mId= " + mId);
		Log.d("pch3", "phoneNumber.getText().toString()= " + phoneNumber.getText().toString());

		
		getActivity().getContentResolver().notifyChange(ContactsContract.Data.CONTENT_URI, null);
	}

    @Override
    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2,
            int paramInt3) {
        
    }

    @Override
    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2,
            int paramInt3) {
        
    }

    @Override
    public void afterTextChanged(Editable paramEditable) {
        showDoneButton();
    }
}
