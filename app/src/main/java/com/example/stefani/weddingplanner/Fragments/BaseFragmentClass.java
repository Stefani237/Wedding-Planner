package com.example.stefani.weddingplanner.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.stefani.weddingplanner.InteractFragments;


public class BaseFragmentClass extends Fragment {

    // allows to open another fragment out of it
    private InteractFragments mInteractFragments;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // communicating with other fragments in order to reuse the Fragment UI components
            mInteractFragments = (InteractFragments) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public InteractFragments getmInteractFragments() {
        // enable to call the switch fragment interface by returning an instance of it
        return mInteractFragments;
    }
}
