package com.rukina.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rukina.R;

/**
 * Created by Nandha on 03-11-2016.
 */

public class ComingSoonFragment extends Fragment {

    View rootView;

    public ComingSoonFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_coming_soon, container, false);

        return rootView;
    }
}
