package it.guaraldi.to_dotaskmanager.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.utils.ActivityUtils;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private Context mContext;
    private List<String> mRepeaterTextArray;
    private int mPrecSelection;
    private OnItemClickListener mClickListener;
    private OnItemLongCLickListener mLongClickListener;
    public interface OnItemClickListener{
        void onItemClick(int position, String itemText);
    }
    public interface OnItemLongCLickListener{
        void onItemLongClick(int position, String itemText);
    }

    public RecyclerAdapter(Context context, int precSelection, List<String> repeaterTextArray, OnItemClickListener clickListener,OnItemLongCLickListener longCLickListener){
        mContext = context;
        mRepeaterTextArray = repeaterTextArray;
        mClickListener = clickListener;
        mLongClickListener = longCLickListener;
        mPrecSelection = precSelection;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_item,parent,false);
        return new RecyclerHolder(v);
    }

    @Override
    public int getItemCount() {
        return mRepeaterTextArray==null || mRepeaterTextArray.size() == 0 ? 0 : mRepeaterTextArray.size();
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        holder.bind(mRepeaterTextArray.get(position),position,mClickListener);
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder{
        private TextView mPeriodText;
        private RadioButton mRadio;

        public RecyclerHolder (View itemView){
            super(itemView);
            mPeriodText = itemView.findViewById(R.id.text_item);
            mRadio = (RadioButton) itemView.findViewById(R.id.radio_btn_item);
        }

        public void bind(String periodText, final int position, final OnItemClickListener listener){
            mPeriodText.setText(periodText);
            if(mPrecSelection == position || (position==0 && mPrecSelection==ActivityUtils.PERSONALIZED_POS))
                mRadio.setChecked(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRadio.setChecked(true);
                    listener.onItemClick(position,mPeriodText.getText().toString());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(position,mPeriodText.getText().toString());
                    return true;
                }
            });
        }


    }
}
