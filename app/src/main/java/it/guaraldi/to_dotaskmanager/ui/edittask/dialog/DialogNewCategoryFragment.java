package it.guaraldi.to_dotaskmanager.ui.edittask.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.util.ActivityUtils;

public class DialogNewCategoryFragment extends DialogFragment{

    EditText editCategory;
    private Intent intent;
    private static final String TAG = "DialogNewCategoryFragment";

    public DialogNewCategoryFragment(){

    }

    public static DialogNewCategoryFragment getInstance(Bundle params){
        DialogNewCategoryFragment fragment = new DialogNewCategoryFragment();
        Bundle args = new Bundle();
        args.putBundle(ActivityUtils.PARAMS,params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_category_dialog,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null && getArguments().containsKey(ActivityUtils.PARAMS)) {

        }
        intent = new Intent();

    }

    @Override
    public void onResume() {
        init();
        super.onResume();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
    }

    private void init(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.dialog_category_confirm:
                        intent.putExtra(ActivityUtils.CATEGORY,editCategory.getText().toString());
                        dismiss();
                        break;
                    case R.id.dialog_category_cancel:
                        dismiss();
                        break;
                }
            }
        };
        editCategory =(EditText) getView().findViewById(R.id.dialog_category_edit);
        Button cancel =(Button) getView().findViewById(R.id.dialog_category_cancel);
        Button confirm =(Button) getView().findViewById(R.id.dialog_category_confirm);
        confirm.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
    }


}
