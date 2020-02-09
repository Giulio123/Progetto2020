package it.guaraldi.to_dotaskmanager.ui.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
        // each data item is just a string in this case
        public TextView textView;

        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.itemFlightDateText);
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
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(tasks.get(position).getDescription());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tasks.size();
    }
}


//class Example5FlightsAdapter : RecyclerView.Adapter<Example5FlightsAdapter.Example5FlightsViewHolder>() {
//
//        val flights = mutableListOf<Flight>()
//
//private val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Example5FlightsViewHolder {
//        return Example5FlightsViewHolder(parent.inflate(R.layout.example_5_event_item_view))
//        }
//
//        override fun onBindViewHolder(viewHolder: Example5FlightsViewHolder, position: Int) {
//        viewHolder.bind(flights[position])
//        }
//
//        override fun getItemCount(): Int = flights.size
//
//        inner class Example5FlightsViewHolder(override val containerView: View) :
//        RecyclerView.ViewHolder(containerView), LayoutContainer {
//
//        fun bind(flight: Flight) {
//        itemFlightDateText.text = formatter.format(flight.time)
//        itemFlightDateText.setBackgroundColor(itemView.context.getColorCompat(flight.color))
//
//        itemDepartureAirportCodeText.text = flight.departure.code
//        itemDepartureAirportCityText.text = flight.departure.city
//
//        itemDestinationAirportCodeText.text = flight.destination.code
//        itemDestinationAirportCityText.text = flight.destination.city
//        }
//        }
//        }
