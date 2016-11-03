package com.rukina.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.rukina.R;

/**
 * Created by Nandha on 30-10-2016.
 */

public class RegisterEventFragment extends Fragment {

    View rootView;
    EditText etxName, etxMobileNo, etxEmailId, extAddress;
    Spinner spnEvents;
    Button btnSubmit;

    public RegisterEventFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);

        etxName = (EditText) rootView.findViewById(R.id.editText_name);
        etxMobileNo = (EditText) rootView.findViewById(R.id.editText_mobile);
        etxEmailId = (EditText) rootView.findViewById(R.id.editText_email);
        extAddress = (EditText) rootView.findViewById(R.id.editText_address);
        spnEvents = (Spinner) rootView.findViewById(R.id.eventselectspinner);
        btnSubmit = (Button) rootView.findViewById(R.id.btn_submit);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.events_type));
        spnEvents.setAdapter(dataAdapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootView;
    }
}
