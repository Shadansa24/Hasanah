package com.app.hasanah.UI.User.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hasanah.DataClass.ActivityDataClass;
import com.app.hasanah.R;
import com.bumptech.glide.Glide;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.holder>{
    ArrayList<ActivityDataClass> activities;
    Context context;
    public ActivitiesAdapter(ArrayList<ActivityDataClass> activities, Context context) {
        this.activities=activities;
        this.context=context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_activities,parent,false);

        return new holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        ActivityDataClass activity=activities.get(position);
        if (activity.getStatus()==0) {
            holder.status.setImageResource(R.drawable.correct);
        }else{
            holder.status.setImageResource(R.drawable.baseline_cancel_24);
        }
        byte[] decodedByte = Base64.decode(activity.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        holder.image.setImageBitmap(bitmap);
        holder.period.setText(delayTime(activity.getDate()));
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class holder extends RecyclerView.ViewHolder
    {
        ImageView status,image;
        TextView period;
        public holder(@NonNull View itemView) {
            super(itemView);
            status=itemView.findViewById(R.id.status);
            image=itemView.findViewById(R.id.image);
            period=itemView.findViewById(R.id.period);

        }
    }
    private String delayTime(String date) {

        String result="";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            // Specify another date (in the future)
            String [] arr=date.split("/");
            LocalDateTime targetDateTime = LocalDateTime.of(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]),Integer.parseInt( arr[2]), Integer.parseInt(arr[3]),Integer.parseInt(arr[4]));  // Example: 7th Oct 2024, 15:00

            // Calculate the difference using Duration
            Duration duration = null;

            duration = Duration.between(targetDateTime,currentDateTime);


            // Check the difference and print the most appropriate time unit
            if (duration.toHours() < 24) {
                // If less than 24 hours, show hours and minutes
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;
                result= minutes + "m " +hours + "h " ;
            } else {
                // If more than 24 hours, show days
                long days = duration.toDays();
              result=days + "days";
            }
        }
        return result;
    }
}
