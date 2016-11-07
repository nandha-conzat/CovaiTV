package com.rukina.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.rukina.R;
import com.rukina.helper.AlertDialogHelper;
import com.rukina.helper.ProgressDialogHelper;
import com.rukina.interfaces.DialogClickListener;
import com.rukina.serviceinterfaces.IEventServiceListener;
import com.rukina.serviceinterfaces.IServiceListener;
import com.rukina.serviceshelper.EventServiceHelper;
import com.rukina.serviceshelper.SignUpServiceHelper;
import com.rukina.utils.CommonUtils;
import com.rukina.utils.CovaiTVConstants;
import com.rukina.utils.CovaiTVValidator;

import org.json.JSONObject;

/**
 * Created by Nandha on 30-10-2016.
 */

public class RegisterEventFragment extends Fragment implements IEventServiceListener {
    private static final String TAG = RegisterEventFragment.class.getName();
    View rootView;
    EditText etxName, etxMobileNo, etxEmailId, extAddress;
    Spinner spnEvents;
    Handler mHandler = new Handler();
    Button btnSubmit;
    private ProgressDialog mProgressDialog = null;
    protected EventServiceHelper eventServiceHelper;
    protected ProgressDialogHelper progressDialogHelper;
    String eventType;
    String regName;
    String regMobileNo;
    String regEmailId;
    String regAddress;

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

        initializeEventHelpers();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventType = spnEvents.getSelectedItem().toString();
                regName = etxName.getText().toString();
                regMobileNo = etxMobileNo.getText().toString();
                regEmailId = etxEmailId.getText().toString();
                regAddress = extAddress.getText().toString();

                if (validateFields()) {
                    if (CommonUtils.isNetworkAvailable(getActivity())) {
                        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                        //    eventServiceHelper.makeRawRequest(FindAFunConstants.GET_ADVANCE_SINGLE_SEARCH);
                        new HttpAsyncTask().execute("");
                    } else {
                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), getString(R.string.no_connectivity));
                    }
                }
            }
        });
        return rootView;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... urls) {
            eventServiceHelper.makeGetEventServiceCall(String.format(CovaiTVConstants.EVENT_REGISTRATION_URL, regName, regMobileNo,
                    regEmailId, eventType, regAddress));

            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Void result) {
            progressDialogHelper.cancelProgressDialog();
        }
    }

    private void clear() {

        etxName.setText("");
        etxMobileNo.setText("");
        etxEmailId.setText("");
        extAddress.setText("");
    }

    protected void initializeEventHelpers() {
        eventServiceHelper = new EventServiceHelper(getActivity());
        eventServiceHelper.setEventServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(getActivity());
    }

    @Override
    public void onEventResponse(JSONObject response) {
        Log.d("ajazFilterresponse : ", response.toString());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
                clear();
                AlertDialogHelper.showSimpleAlertDialog(getActivity(), "Thank you " + regName + ", your message has been submitted and someone will contact you shortly.");
            }
        });
    }

    @Override
    public void onEventError(final String error) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialogHelper.hideProgressDialog();
                AlertDialogHelper.showSimpleAlertDialog(getActivity(), error);
            }
        });
    }

    private boolean validateFields() {

        if (!CovaiTVValidator.checkNullString(this.etxName.getText().toString().trim())) {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), this.getResources().getString(R.string.enter_name));
            return false;
        } else if (!CovaiTVValidator.checkNullString(this.etxMobileNo.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), this.getResources().getString(R.string.enter_mobile));
            return false;
        } else if (!CovaiTVValidator.checkNullString(this.etxEmailId.getText().toString().trim())) {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), this.getResources().getString(R.string.email_empty_str));
            return false;
        } else if (!CovaiTVValidator.isEmailValid(this.etxEmailId.getText().toString().trim())) {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), this.getResources().getString(R.string.enter_valid_email));
            return false;
        } else if (!CovaiTVValidator.checkSpinnerString(this.spnEvents.getSelectedItem().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), this.getResources().getString(R.string.select_event));
            return false;
        } else if (!CovaiTVValidator.checkNullString(this.extAddress.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), this.getResources().getString(R.string.enter_address));
            return false;
        } /* else if (!CovaiTVValidator.withinPermittedLength(this.edtPassword.getText().toString())) {
            AlertDialogHelper.showSimpleAlertDialog(getActivity(), "Password length should be greater than 6 characters");
            return false;
        } */ else {
            return true;
        }
    }
}
