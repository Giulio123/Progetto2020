package it.guaraldi.to_dotaskmanager.ui.calendar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import it.guaraldi.to_dotaskmanager.R;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PARAMS;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.USER_EMAIL;

public class DialogEmailVerification extends DialogFragment {

    private static final String TAG = "DialogEmailVerification";
    private Intent intent;
    private TextView msg;
    private TextView validation;

    public DialogEmailVerification(){

    }

    public static DialogEmailVerification getInstance(Bundle params){
        DialogEmailVerification fragment = new DialogEmailVerification();
        Bundle args = new Bundle();
        args.putBundle(PARAMS,params.getBundle(PARAMS));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_email_verification,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null && getArguments().containsKey(PARAMS))
            setUp(getArguments().getBundle(PARAMS).getString(USER_EMAIL));

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setUp(String textMsg){
        msg = (TextView)getView().findViewById(R.id.dialog_email_verification_msg);
        msg.setText( getString(R.string.dialog_email_verification_msg_1)+" "+ textMsg + getString(R.string.dialog_email_verification_msg_2));
        validation = (TextView) getView().findViewById(R.id.dialog_email_verification_click_email);
        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent();
                dismiss();
            }
        });
        this.getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        choiceEmailApp();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
    }

    private void choiceEmailApp(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        Intent chooser = Intent.createChooser(intent,getString(R.string.email_chooser));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivity(chooser);
    }

}
