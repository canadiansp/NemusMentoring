package com.nemus.addressbook;

import android.app.ListFragment;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.nemustech.tiffany.R;

import java.io.InputStream;

public class ListAddressFragment extends ListFragment {
	
	ResourceCursorAdapter mAdapter;
	boolean mDualPane;
	private long mId = -1;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
 		
        View list = inflater.inflate(R.layout.list, null);
        
        Button addBtn = (Button)list.findViewById(R.id.btn_add_address);
        
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                
                DetailFragment detailFrag = null;
                if(MainActivity.IS_DUAL_PANE) {
                    detailFrag = (DetailFragment)getFragmentManager().findFragmentByTag(MainActivity.TAG_DETAIL_FRAG);
                    if(detailFrag != null)
                        detailFrag.clearEditTexts();
                } else {
                    detailFrag = (DetailFragment)((MainActivity)getActivity()).switchToFragment(MainActivity.TAG_DETAIL_FRAG, R.id.main_layout);
                }
            }
        });
        return list;
    }
    
    @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		mId = id;
		
		DetailFragment detailFrag = null;
		if(MainActivity.IS_DUAL_PANE) {
		    detailFrag = (DetailFragment)(getFragmentManager().findFragmentByTag(MainActivity.TAG_DETAIL_FRAG));
		    detailFrag.refreshDetailViewById(id);
		} else {
		    detailFrag = (DetailFragment)((MainActivity)getActivity()).switchToFragment(MainActivity.TAG_DETAIL_FRAG, R.id.main_layout);//.getFragmentManager().findFragment(MainActivity.TAG_DETAIL_FRAG);
		}
		
		if(detailFrag != null)
		    detailFrag.setDataId(id);
	}

	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Cursor c = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI ,null, null,
                null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        
        mAdapter = new ResourceCursorAdapter(getActivity(), R.layout.list_item, c, true) {
			
			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				TextView name = (TextView)view.findViewById(R.id.list_item_name);
				ImageView photo = (ImageView)view.findViewById(R.id.list_item_photo);
				final int idxLastName = cursor.getColumnIndexOrThrow(Contacts.DISPLAY_NAME);
				int contactId_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
				Long contactid = cursor.getLong(contactId_idx);
				
				Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);      
				InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(), uri);      
				if (input == null) {     
					photo.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
				} 
				else{
					photo.setImageBitmap(BitmapFactory.decodeStream(input)); 	
				}
				name.setText(cursor.getString(idxLastName));
			}
		};
        
        setListAdapter(mAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        
        if(!MainActivity.IS_DUAL_PANE) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
