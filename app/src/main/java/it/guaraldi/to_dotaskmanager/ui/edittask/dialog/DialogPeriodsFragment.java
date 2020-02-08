package it.guaraldi.to_dotaskmanager.ui.edittask.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.adapter.RecyclerAdapter;
import it.guaraldi.to_dotaskmanager.util.ActivityUtils;

import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PARAMS;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PERIOD;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PERIOD_PARAMS;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PERSONALIZED_PERIOD;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.PERSONALIZED_POS;
import static it.guaraldi.to_dotaskmanager.util.ActivityUtils.POS;

public class DialogPeriodsFragment extends DialogFragment{
    private String mPersonalizedInstance;
    private int lastSelectedPosition;
    private Intent intent;
    private static final String TAG = "DialogPeriodsFragment";

    public DialogPeriodsFragment(){

    }

    public static DialogPeriodsFragment getInstance(Bundle params){
        DialogPeriodsFragment fragment = new DialogPeriodsFragment();
        Bundle args = new Bundle();
        Log.d(TAG, "getInstance: "+params);
        args.putBundle(PARAMS,params.getBundle(PERIOD_PARAMS));
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
        return inflater.inflate(R.layout.periods_dialog,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        Bundle content;
        if(args!=null && (content=args.getBundle(PARAMS))!=null) {
            lastSelectedPosition = content.getInt(POS);
            mPersonalizedInstance = lastSelectedPosition == 0 ?
                    content.getString(PERSONALIZED_PERIOD) : null;
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.repeater_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(
                getContext(),
                lastSelectedPosition,
                repeaterData(mPersonalizedInstance),
                new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String period) {
                intent.putExtra(POS, position);
                intent.putExtra(PERIOD, period);
                dismiss();
            }
        },null);
        recyclerView.setAdapter(adapter);
        intent = new Intent();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
    }

    private List<String> repeaterData(String personalized){
        int j = personalized!=null ? 1 : 0;
        TypedArray array = getResources().obtainTypedArray(R.array.repeater);
        List<String> res = new ArrayList<>();
        if(j==1)
            res.add(personalized);
        for(int i = j; i < array.length()+j; i++)
            res.add(getResources().getString(array.getResourceId(i-j,-1)));
        return res;
    }
}
