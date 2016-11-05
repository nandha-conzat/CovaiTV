package com.rukina.dialogfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.rukina.R;
import com.rukina.interfaces.DialogClickListener;
import com.rukina.utils.CovaiTVConstants;


public class CompoundAlertDialogFragment extends DialogFragment {
    private int tag;
    DialogClickListener dialogActions;

    public static CompoundAlertDialogFragment newInstance(String title, String message, String posButton, String negButton, int tag) {
        CompoundAlertDialogFragment frag = new CompoundAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString(CovaiTVConstants.ALERT_DIALOG_TITLE, title);
        args.putString(CovaiTVConstants.ALERT_DIALOG_MESSAGE, message);
        args.putString(CovaiTVConstants.ALERT_DIALOG_POS_BUTTON, posButton);
        args.putString(CovaiTVConstants.ALERT_DIALOG_NEG_BUTTON, negButton);
        args.putInt(CovaiTVConstants.ALERT_DIALOG_TAG, tag);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogActions = (DialogClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling activity must implement DialogClickListener interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String message = args.getString(CovaiTVConstants.ALERT_DIALOG_MESSAGE, "");
        String title = args.getString(CovaiTVConstants.ALERT_DIALOG_TITLE);
        tag = args.getInt(CovaiTVConstants.ALERT_DIALOG_TAG, 0);
        String posButton = args.getString(CovaiTVConstants.ALERT_DIALOG_POS_BUTTON, getActivity().getString(R.string.alert_button_ok));
        String negButton = args.getString(CovaiTVConstants.ALERT_DIALOG_NEG_BUTTON, getActivity().getString(R.string.alert_button_cancel));
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(posButton, mListener)
                .setNegativeButton(negButton, mListener)
                .create();

    }

    DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (which == -1) {
                dialog.cancel();
                if (CompoundAlertDialogFragment.this.dialogActions != null)
                    CompoundAlertDialogFragment.this.dialogActions
                            .onAlertPositiveClicked(tag);

            } else {
                dialog.cancel();
                if (CompoundAlertDialogFragment.this.dialogActions != null)
                    CompoundAlertDialogFragment.this.dialogActions
                            .onAlertNegativeClicked(tag);
            }
        }

    };
}
