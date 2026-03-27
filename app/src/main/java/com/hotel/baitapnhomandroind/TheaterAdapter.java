package com.hotel.baitapnhomandroind;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hotel.baitapnhomandroind.activities.ShowtimeActivity;
import com.hotel.baitapnhomandroind.entities.Theater;

import java.util.List;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder> {

    private List<Theater> theaterList;

    public TheaterAdapter(List<Theater> theaterList) {
        this.theaterList = theaterList;
    }

    @NonNull
    @Override
    public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theater, parent, false);
        return new TheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {
        Theater theater = theaterList.get(position);
        holder.tvName.setText(theater.name);
        holder.tvLocation.setText(theater.location);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ShowtimeActivity.class);
            intent.putExtra("THEATER_ID", theater.id);
            intent.putExtra("THEATER_NAME", theater.name);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return theaterList != null ? theaterList.size() : 0;
    }

    public static class TheaterViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation;

        public TheaterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTheaterName);
            tvLocation = itemView.findViewById(R.id.tvTheaterLocation);
        }
    }
}
