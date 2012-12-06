package com.nemus.addressbook;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

import com.nemustech.tiffany.R;
import com.nemustech.tiffany.world.TFContentProvider;
import com.nemustech.tiffany.world.TFModel;
import com.nemustech.tiffany.world.TFObject;
import com.nemustech.tiffany.world.TFTextureInfo;

import java.io.InputStream;

public class ResourceProvider extends TFContentProvider<Long> {
	private String TAG = "ResourceProvider";
	private Resources mResources;
	private int mTextureAlignMode = TFTextureInfo.TEXTURE_ALIGN_DEFAULT;
	
	Context mContext;
	Cursor mCursor;
	
	public ResourceProvider(Context context) {
		super();
		
		mContext = context;
		mResources = context.getResources();
		
		mCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI ,null, null,
                null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		
		final int colIdIdx = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
		
		while(mCursor.moveToNext()) {
		    long id = mCursor.getLong(colIdIdx);
		    addItem(Long.valueOf(id));
		}
	}
	
	public ResourceProvider(Resources resources, int textureAlignMode) {
		super();
		mResources = resources;
		mTextureAlignMode = textureAlignMode;
	}
	
	@Override
	protected void applyContent(TFObject object, int itemIndex) {
	    final long contactid = getItem(itemIndex); 
	    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);      
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(mContext.getContentResolver(), uri); 
        
        if (input == null) {     
            ((TFModel)object).setImageResource(0, mResources, R.drawable.ic_launcher, mTextureAlignMode);
        } 
        else{
            ((TFModel)object).setImageResource(0, BitmapFactory.decodeStream(input));
        }
        
//		((TFModel)object).setImageResource(itemIndex, BitmapFactory.decodeStream(input)); //0, mResources, getItem(itemIndex), mTextureAlignMode);		
	}
}

