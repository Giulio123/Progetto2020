package it.guaraldi.to_dotaskmanager.ui.edittask.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.adapter.RecyclerAdapter;

import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.CATEGORY_PARAMS;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.LIST_CATEGORIES;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.LONG_CLICK;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.PARAMS;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.POS;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.SELECTED_CATEGORY;

public class DialogShowCategoryFragment extends DialogFragment {

    private Intent intent;
    private static final String TAG = "DialogShowCategoryFragment";
    private List<String> data;
    private int lastSelectedPosition;
    private ActionMode mActionMode;

    public DialogShowCategoryFragment(){

    }

    public static DialogShowCategoryFragment getInstance(Bundle params){
        DialogShowCategoryFragment fragment = new DialogShowCategoryFragment();
        Bundle args = new Bundle();
        if (params.containsKey(CATEGORY_PARAMS)) {
            args.putBundle(PARAMS, params.getBundle(CATEGORY_PARAMS));
        } else {
            args.putBundle(PARAMS, params);
        }
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
        return inflater.inflate(R.layout.show_category_dialog,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null && getArguments().containsKey(PARAMS)) {
            Bundle content = getArguments().getBundle(PARAMS);
            Log.d(TAG, "onViewCreated: content:"+content);
            data = content.getStringArrayList(LIST_CATEGORIES);
            lastSelectedPosition = content.containsKey(POS)? content.getInt(POS):0;
        }
        RecyclerView recyclerView = view.findViewById(R.id.categories_list_dialog);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerAdapter adapter  = new RecyclerAdapter(getContext(), lastSelectedPosition, data, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String category) {
                intent.putExtra(SELECTED_CATEGORY, category);
                intent.putExtra(POS, position);
                dismiss();
            }
        }, new RecyclerAdapter.OnItemLongCLickListener() {
            @Override
            public void onItemLongClick(final int position,final String itemText) {
                Snackbar mySnackbar = Snackbar.make(getView(),itemText, Snackbar.LENGTH_INDEFINITE);
                mySnackbar.setAction(R.string.remove_category, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra(LONG_CLICK,true);
                        intent.putExtra(SELECTED_CATEGORY, itemText);
                        intent.putExtra(POS, position);
                        dismiss();
                    }
                });
                mySnackbar.show();

            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        intent = new Intent();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
    }

}
