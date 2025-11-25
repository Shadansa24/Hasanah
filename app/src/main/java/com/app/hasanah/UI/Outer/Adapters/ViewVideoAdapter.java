package com.app.hasanah.UI.Outer.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hasanah.DataClass.AdviceModel;
import com.app.hasanah.R;

import java.util.ArrayList;

public class ViewVideoAdapter extends RecyclerView.Adapter<ViewVideoAdapter.helper>{
    ArrayList<AdviceModel> videos;
    Context context;
    public ViewVideoAdapter(ArrayList<AdviceModel> videos, Context context) {
        this.videos = videos;
        this.context=context;
    }
    @NonNull
    @Override
    public helper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video_users,parent,false);

        return new helper(v);
    }

    @Override
    public void onBindViewHolder(@NonNull helper holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(videos.get(position).getAdvice());
        holder.setVideoPath(videos.get(position).getVideoUrl());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class helper extends RecyclerView.ViewHolder{
        TextView title;
        ImageView btnPlayPause;
        private VideoView videoView;
        private SeekBar seekBar;
        private ProgressBar progressBar;
        private TextView textViewTime;
        private Handler handler;
        private boolean isPlaying = false;
        public helper(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            videoView=itemView.findViewById(R.id.videoView);
            btnPlayPause=itemView.findViewById(R.id.manage);
            textViewTime=itemView.findViewById(R.id.textViewTime);
            seekBar=itemView.findViewById(R.id.seekBar);
            progressBar=itemView.findViewById(R.id.progressBar);
            handler = new Handler();



            btnPlayPause.setOnClickListener(view -> togglePlayPause());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        videoView.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Not needed for this example
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Not needed for this example
                }
            });
            progressBar.setVisibility(View.GONE);

            // Set up a listener to show/hide the progress bar based on video loading state
            videoView.setOnPreparedListener(mp -> {
                // Hide progress bar when video is prepared
                progressBar.setVisibility(View.VISIBLE);
            });

            videoView.setOnErrorListener((mp, what, extra) -> {
                // Hide progress bar if an error occurs
                progressBar.setVisibility(View.VISIBLE);
                return false;
            });

            videoView.setOnInfoListener((mp, what, extra) -> {
                // Show progress bar when buffering starts
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    // Hide progress bar when buffering ends
                    progressBar.setVisibility(View.GONE);
                }
                return false;
            });
        }
        public void setVideoPath(String path) {
            videoView.setVideoPath(path);
            updateSeekBar();
        }

        private void togglePlayPause() {
            if (isPlaying) {
                videoView.pause();
                btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_24);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                videoView.start();
                btnPlayPause.setImageResource(R.drawable.baseline_pause_24);
            }
            isPlaying = !isPlaying;
        }

        private void updateSeekBar() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (videoView.isPlaying()) {
                        progressBar.setVisibility(View.GONE);
                        int currentPosition = videoView.getCurrentPosition();
                        int totalDuration = videoView.getDuration();

                        seekBar.setMax(totalDuration);
                        seekBar.setProgress(currentPosition);

                        String timeText = formatTime(currentPosition) + " / " + formatTime(totalDuration);
                        textViewTime.setText(timeText);
                    }
                    handler.postDelayed(this, 1000); // Update every second
                }
            }, 0);
        }

        private String formatTime(int milliseconds) {
            int seconds = (milliseconds / 1000) % 60;
            int minutes = (milliseconds / (1000 * 60)) % 60;
            return String.format("%d:%02d", minutes, seconds);
        }

    }
}
