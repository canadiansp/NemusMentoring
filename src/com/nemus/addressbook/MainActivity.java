package com.nemus.addressbook;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.nemustech.tiffany.R;

public class MainActivity extends Activity {
    
    static final String TAG_LIST_FRAG = "list_fragment";
    static final String TAG_TIFFANY_FRAG = "list_fragment";
    static final String TAG_DETAIL_FRAG = "detail_fragment";
    
    public static boolean IS_DUAL_PANE = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        View listView = findViewById(R.id.list_fragment);
        View tfView = findViewById(R.id.tiffany_fragment);
//        View detailView = findViewById(R.id.detail_fragment); 
        
        if(listView != null || tfView != null) {
            // Dual fragment
            IS_DUAL_PANE = true;
        }
        
        
        DetailFragment detailFragment = new DetailFragment();
        
        if(IS_DUAL_PANE) {
            if(listView != null) {
                ListAddressFragment listFragment = new ListAddressFragment();
                ft.replace(R.id.list_fragment, listFragment, TAG_LIST_FRAG);
            }
            
            if(tfView != null) {
                TiffantFragment tiffanyFragment = new TiffantFragment();
                ft.replace(R.id.tiffany_fragment, tiffanyFragment, TAG_TIFFANY_FRAG);
            }
            
            ft.replace(R.id.detail_fragment, detailFragment, TAG_DETAIL_FRAG);
        } else {
            ListAddressFragment listFragment = new ListAddressFragment();
            ft.replace(R.id.main_layout, listFragment, TAG_LIST_FRAG);
        }
        ft.commit(); 
    }
    
    public Fragment switchToFragment(String fragmentTag, int resourceId) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment frag = fm.findFragmentByTag(fragmentTag);
        if(frag == null) {
            if(TAG_LIST_FRAG.equals(fragmentTag)) {
                frag = new ListAddressFragment();
            } else {
                frag = new DetailFragment();
            }
        }
        ft.replace(resourceId, frag, fragmentTag);
        ft.commit();

        return frag;
    }
    
    @Override
    public void onBackPressed() {
        if(!IS_DUAL_PANE) {
            Fragment frag = getFragmentManager().findFragmentById(R.id.main_layout);
            if(frag == null || frag instanceof DetailFragment) {
                switchToFragment(TAG_LIST_FRAG, R.id.main_layout);
            } else
                super.onBackPressed();
        } else 
            super.onBackPressed();
    }
}