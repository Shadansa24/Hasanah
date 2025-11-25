package com.app.hasanah.UI.User;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.hasanah.DataClass.ActivityDataClass;
import com.app.hasanah.DataClass.Status;
import com.app.hasanah.DataClass.UserDataClass;
import com.app.hasanah.R;
import com.app.hasanah.UI.Outer.OuterMainActivity;
import com.app.hasanah.UI.User.Adapters.ActivitiesAdapter;
import com.app.hasanah.Utils.Common;
import com.app.hasanah.Utils.Dialoge.SweetDialog;
import com.app.hasanah.databinding.FragmentUserHomeBinding;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserHomeFragment extends Fragment {
    private FragmentUserHomeBinding mBinding;
    private String userId;
    private DatabaseReference database;
    private ArrayList<ActivityDataClass> activities;
    private ArrayList<Status> statuses;
    private ActivitiesAdapter adapter;
    private SweetAlertDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentUserHomeBinding.inflate(inflater, container, false);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        database = FirebaseDatabase.getInstance().getReference(Common.activityTable);


        mBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OuterMainActivity.class));
            }
        });
        mBinding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_home_to_uploadPhoto);
            }
        });
        mBinding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_home_to_profile);
            }
        });

        mBinding.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_home_to_profile);
            }
        });
        mBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    viewBarCharStatics(0);
                } else if (position == 1) {
                    viewBarCharStatics(1);
                } else if (position == 2) {
                    viewBarCharStatics(2);
                } else if (position == 3) {
                    viewBarCharStatics(3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        funLoading();
        //uploadActivities();
        defineRecyclerview();
        getUserActivities();
        setupPieChart();
        getUserData();
        return mBinding.getRoot();
    }

    private void defineRecyclerview() {
        statuses = new ArrayList<>();
        statuses.add(new Status(0, 0));
        statuses.add(new Status(0, 0));
        statuses.add(new Status(0, 0));
        statuses.add(new Status(0, 0));

        activities = new ArrayList<>();
        adapter = new ActivitiesAdapter(activities, getActivity());
        mBinding.activities.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mBinding.activities.setAdapter(adapter);
    }

    private void getUserActivities() {
        database.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        ActivityDataClass activity = data.getValue(ActivityDataClass.class);
                        if (activity.getStatus() == 0) {
                            int i = statuses.get(activity.getType()).getCorrect() + 1;
                            statuses.get(activity.getType()).setCorrect(i);
                        }
                        if (activity.getStatus() == 1) {
                            int i = statuses.get(activity.getType()).getMistake() + 1;
                            statuses.get(activity.getType()).setMistake(i);
                        }
                        activities.add(activity);
                    }
                } else {
                    mBinding.nodata.setVisibility(View.VISIBLE);
                }
                loading.dismiss();
                adapter.notifyDataSetChanged();
                viewBarCharStatics(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupPieChart() {
        mBinding.barChart.setDrawHoleEnabled(false);  // Disable the hole
        mBinding.barChart.setUsePercentValues(true);
        mBinding.barChart.setEntryLabelTextSize(12);
        mBinding.barChart.setEntryLabelColor(Color.BLACK);
        mBinding.barChart.getDescription().setEnabled(false);

        Legend legend = mBinding.barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }

    private void viewBarCharStatics(int i) {
        int m = statuses.get(i).getMistake();
        int c = statuses.get(i).getCorrect();
        if (m == 0 && c == 0) {
            mBinding.noStatistics.setVisibility(View.VISIBLE);
            mBinding.barChart.setVisibility(View.INVISIBLE);
        } else {
            mBinding.noStatistics.setVisibility(View.GONE);
            mBinding.barChart.setVisibility(View.VISIBLE);
            loadPieChartData(m, c);
        }
    }

    private void loadPieChartData(float mist, float correct) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(mist, "mistake"));
        entries.add(new PieEntry(correct, "Correct"));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#B30A0A"));
        colors.add(Color.parseColor("#0E9D14"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        mBinding.barChart.setData(data);
        mBinding.barChart.invalidate(); // Refresh the chart
    }
    private void getUserData() {
        DatabaseReference database=FirebaseDatabase.getInstance().getReference(Common.userTable);
        database.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userName = snapshot.child("userName").getValue().toString();
                    String image = snapshot.child("photo").getValue().toString();
                    mBinding.userName.setText(userName);
                    if (!image.isEmpty()) {
                        Glide.with(getActivity()).load(image).into(mBinding.profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void navigationTo(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_auth).navigate(id);
    }

}