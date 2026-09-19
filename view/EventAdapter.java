package com.example.cs360_projectthree.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs360_projectthree.R;
import com.example.cs360_projectthree.model.Event;

import java.util.ArrayList;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final ArrayList<Event> eventList;
    private final OnDeleteClickListener deleteClickListener;
    private final OnUpdateClickListener updateClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Event event);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(int id, String date, String title, String description);
    }

    public EventAdapter(ArrayList<Event> eventList,
                        OnDeleteClickListener deleteClickListener,
                        OnUpdateClickListener updateClickListener) {
        this.eventList = eventList;
        this.deleteClickListener = deleteClickListener;
        this.updateClickListener = updateClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.textDate.setText("Date: " + event.getDate());
        holder.textTitle.setText(event.getTitle().toUpperCase(Locale.US));
        holder.textDescription.setText(event.getDescription());

        holder.editDate.setText(event.getDate());
        holder.editTitle.setText(event.getTitle());
        holder.editDescription.setText(event.getDescription());

        if (holder.isEditing) {
            holder.textDate.setVisibility(View.GONE);
            holder.textTitle.setVisibility(View.GONE);
            holder.textDescription.setVisibility(View.GONE);

            holder.editDate.setVisibility(View.VISIBLE);
            holder.editTitle.setVisibility(View.VISIBLE);
            holder.editDescription.setVisibility(View.VISIBLE);

            holder.buttonUpdate.setText("Save");
        } else {
            holder.textDate.setVisibility(View.VISIBLE);
            holder.textTitle.setVisibility(View.VISIBLE);
            holder.textDescription.setVisibility(View.VISIBLE);

            holder.editDate.setVisibility(View.GONE);
            holder.editTitle.setVisibility(View.GONE);
            holder.editDescription.setVisibility(View.GONE);

            holder.buttonUpdate.setText("Update");
        }

        holder.buttonDelete.setOnClickListener(v -> deleteClickListener.onDeleteClick(event));

        holder.buttonUpdate.setOnClickListener(v -> {
            if (!holder.isEditing) {
                holder.isEditing = true;

                holder.textDate.setVisibility(View.GONE);
                holder.textTitle.setVisibility(View.GONE);
                holder.textDescription.setVisibility(View.GONE);

                holder.editDate.setVisibility(View.VISIBLE);
                holder.editTitle.setVisibility(View.VISIBLE);
                holder.editDescription.setVisibility(View.VISIBLE);

                holder.buttonUpdate.setText("Save");
            } else {
                String newDate = holder.editDate.getText().toString().trim();
                String newTitle = holder.editTitle.getText().toString().trim();
                String newDescription = holder.editDescription.getText().toString().trim();

                updateClickListener.onUpdateClick(event.getId(), newDate, newTitle, newDescription);
                holder.isEditing = false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView textDate, textTitle, textDescription;
        EditText editDate, editTitle, editDescription;
        Button buttonDelete, buttonUpdate;
        boolean isEditing = false;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            textDate = itemView.findViewById(R.id.textDate);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);

            editDate = itemView.findViewById(R.id.editDate);
            editTitle = itemView.findViewById(R.id.editTitle);
            editDescription = itemView.findViewById(R.id.editDescription);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.updateDelete);
        }
    }
}