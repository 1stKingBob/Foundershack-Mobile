package com.scouthub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scouthub.R;
import com.scouthub.models.CalendarEvent;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private List<CalendarEvent> eventsList;
    private Context context;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(CalendarEvent event);
    }

    public CalendarAdapter(List<CalendarEvent> eventsList) {
        this.eventsList = eventsList;
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_calendar_event, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        CalendarEvent event = eventsList.get(position);
        
        holder.titleTextView.setText(event.getTitle());
        holder.dateTextView.setText(event.getDate());
        holder.timeTextView.setText(event.getTime());
        holder.locationTextView.setText(event.getLocation());
        
        // Set type indicator color
        switch (event.getType()) {
            case "trial":
                holder.typeIndicator.setBackgroundColor(context.getColor(R.color.scouthub_green));
                break;
            case "training":
                holder.typeIndicator.setBackgroundColor(context.getColor(R.color.scouthub_orange));
                break;
            case "match":
                holder.typeIndicator.setBackgroundColor(context.getColor(R.color.scouthub_dark_grey));
                break;
            case "meeting":
                holder.typeIndicator.setBackgroundColor(context.getColor(R.color.scouthub_medium_grey));
                break;
        }
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEventClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        View typeIndicator;
        TextView titleTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView locationTextView;

        CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            typeIndicator = itemView.findViewById(R.id.type_indicator);
            titleTextView = itemView.findViewById(R.id.title);
            dateTextView = itemView.findViewById(R.id.date);
            timeTextView = itemView.findViewById(R.id.time);
            locationTextView = itemView.findViewById(R.id.location);
        }
    }
}
