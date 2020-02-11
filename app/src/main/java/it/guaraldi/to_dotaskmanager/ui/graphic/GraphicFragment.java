package it.guaraldi.to_dotaskmanager.ui.graphic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import it.guaraldi.to_dotaskmanager.NewsApp;
import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.adapter.GrapSpinAdapter;
import it.guaraldi.to_dotaskmanager.adapter.GraphicAdapter;
import it.guaraldi.to_dotaskmanager.ui.base.BaseFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GraphicFragment extends BaseFragment implements GraphicContract.View, OnChartValueSelectedListener, AdapterView.OnItemSelectedListener {

    @Inject
    public GraphicPresenter mPresenter;
    private BarChart chart;
    private Toolbar mToolbar;
    private GridView mGridView ;
    private GraphicAdapter mGAdapter;
    private Spinner mSpinner;
    private GrapSpinAdapter mSAdapter;
    private String mCurrCategor=null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NewsApp.getNewsComponent().inject(this);
        if(mPresenter != null)
            mPresenter.attachView(this);
        View v = inflater.inflate(R.layout.graphic_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        setUp(getArguments());
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if(mPresenter != null) {
            mPresenter.detachView(this);
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    protected void setUp(Bundle data) {
        mPresenter.getAllCategories();
    }

    @Override
    protected void initViews() {
        mToolbar = getActivity().findViewById(R.id.toolbar_graphic);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mGridView = (GridView)getActivity().findViewById(R.id.gridview);
        mSpinner = (Spinner) getActivity().findViewById(R.id.spinnerGraphic);
        mSpinner.setOnItemSelectedListener(this);

        //graphic
        chart = getActivity().findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(4);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.setHighlightFullBarEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        chart.getAxisRight().setEnabled(false);
        XAxis xLabels = chart.getXAxis();
        xLabels.setValueFormatter(new LargeValueFormatter(" Priority"));
        xLabels.setLabelCount(4);
        xLabels.setPosition(XAxis.XAxisPosition.TOP);
        xLabels.setAxisMinimum(0.5f);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
    }



    @Override
    public void getTasksForGraphic(String category, int []totalTasks, int []pendingTasks , int [] completeTasks) {

        ArrayList<BarEntry> values = getValues(completeTasks,pendingTasks);
        chart.setData(getDataSet(values,mCurrCategor));
        if(category == null){
            chart.getAxisLeft().setAxisMaximum(1f);
            chart.getAxisLeft().setAxisMinimum(0f);
        }
        chart.setFitBars(true);
        chart.invalidate();
        chart.animateXY(2000, 2000);
        mGAdapter = new GraphicAdapter(getActivity(),tasksValues(pendingTasks,completeTasks,totalTasks));
        mGridView.setAdapter(mGAdapter);
    }

    @Override
    public void updateSpinner(List<String> categories) {
        List<String> allCategories = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.category)));
        allCategories.remove(getString(R.string.add_other));
        if(categories!= null && categories.size()>0)
            allCategories.addAll(categories);
        mSAdapter = new GrapSpinAdapter(getActivity(),allCategories);
        mSpinner.setAdapter(mSAdapter);
        mSpinner.setSelection(0);
    }

    private List<Integer> tasksValues(int [] pending, int [] complete,int total[]){
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i<12; i++){
            if(i>=4 && i<8)
                result.add(complete[i-4]);
            if(i>=8)
                result.add(total[i-8]);
            if(i<4)
                result.add(pending[i]);
        }
        return result;
    }

    private ArrayList<BarEntry> getValues (int completeTasks[],int pendingTasks[]){
        ArrayList<BarEntry> result = new ArrayList<>();
        for (int i = 0; i<5;i++) {
            if (i == 0)
                result.add(new BarEntry(i, new float[]{(float) 0, (float) 0}, getResources().getDrawable(R.drawable.star)));
            else
                result.add(new BarEntry(i, new float[]{(float)completeTasks[i-1] , (float)pendingTasks[i-1] }, getResources().getDrawable(R.drawable.star)));
        }
            return result;
    }

    private BarData getDataSet(ArrayList<BarEntry> values,String category){
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet taskSet;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            taskSet= (BarDataSet) chart.getData().getDataSetByIndex(0);
            taskSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            taskSet = new BarDataSet(values, "Statistics "+category+"");
            taskSet.setDrawIcons(false);
            taskSet.setColors(getColors());
            taskSet.setStackLabels(new String[]{"Complete", "Pending"});
            taskSet.setLabel(category);
            dataSets.add(taskSet);
        }
        BarData result = new BarData(dataSets);
        return result;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {

    }

    private int[] getColors() {
        int[] colors = new int[2];
        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 2);
        return colors;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tv;
        if((tv = getActivity().findViewById(R.id.item_spinner_graphic))!=null) {
            mCurrCategor = tv.getText().toString();
            mPresenter.getAllTasksByCategory(mCurrCategor);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
