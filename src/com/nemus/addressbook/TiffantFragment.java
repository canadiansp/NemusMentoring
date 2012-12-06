package com.nemus.addressbook;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nemustech.tiffany.R;
import com.nemustech.tiffany.world.TFHolder;
import com.nemustech.tiffany.world.TFItemProvider;
import com.nemustech.tiffany.world.TFLinearHolder;
import com.nemustech.tiffany.world.TFPanel;
import com.nemustech.tiffany.world.TFWorld;

public class TiffantFragment extends Fragment{
    
    private static final int MODEL_COUNT = 6;
    private TFWorld mWorld;
    private TFHolder mHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        
        View list = inflater.inflate(R.layout.tfview, null);
        
        mWorld = new TFWorld(2, 2, 10);
        mWorld.setTouchedModelColorMasking(0, 1, 1);
//        mWorld.setSelectListener(mOnSelect);
//        mWorld.setEffectFinishListener(mOnEffectFinish);
        mWorld.setBlendingMode(true);

        mHolder = new TFLinearHolder(6f);
        mHolder.attachTo(mWorld);
        mHolder.setStepPerUnitForce(3.0f);
        mHolder.setDeceleration(0.00005f);

        ResourceProvider provider = new ResourceProvider(this.getActivity());
        mHolder.setItemProvider(provider); //getResources(), TiffanySampleGuiMain.IMAGE_RESOURCE));
        mHolder.locate(0.3f, 0.4f, -2f);
        mHolder.look(-15, 30);
        
        // Add reflecting floor with 50% opacity
        mWorld.addReflectingFloor( mHolder.getLocation( TFWorld.AXIS_Y ) - 0.5f, 0.5f );
        //mHolder.setEndlessMode(true);

        final int model_count = Math.min(provider.getItemCount(), MODEL_COUNT);

        for (int i = 0; i < model_count; i++) {
            TFPanel p = new TFPanel( mHolder, 1, 1);
            p.setBeautyReflection( true );
        }

        mWorld.show(list.findViewById(R.id.tfview));
        
        return list;
    }
    
}
