package it.guaraldi.to_dotaskmanager.ui.calendar;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.ArrayList;
import java.util.List;

import it.guaraldi.to_dotaskmanager.R;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {
    private List<Task> tasks;
    public void setDataset(List<Task> tasks) {
        this.tasks = tasks;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView textDate;
        private final View textColorBox;
        private final TextView textCategory;
        private final View view;
        // each data item is just a string in this case
        public TextView textView;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            textView = v.findViewById(R.id.taskDescr);
            textColorBox = v.findViewById(R.id.taskColorBox);
            textCategory = v.findViewById(R.id.taskCategory);
            textDate = v.findViewById(R.id.taskDate);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TasksAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TasksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.calendar_item_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task task = tasks.get(position);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDa4teTime(FormatStyle.SHORT);
        LocalDateTime start = LocalDateTime.ofEpochSecond(Long.parseLong(task.getStart()) / 1000,0, ZoneOffset.UTC);
        LocalDateTime end = LocalDateTime.ofEpochSecond(Long.parseLong(task.getEnd()) / 1000, 0, ZoneOffset.UTC);
        holder.textView.setText(task.getTitle());
        holder.textDate.setText(start.format(dateTimeFormatter) + " - " + end.format(dateTimeFormatter));
        holder.textCategory.setText(task.getCategory());
        holder.textColorBox.setBackgroundColor(holder.view.getContext().getColor(Integer.parseInt(task.getColor())));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tasks.size();
    }
}

