package com.app.hasanah.UI.User;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.app.hasanah.DataClass.AdviceModel;
import com.app.hasanah.R;
import com.app.hasanah.UI.Outer.Adapters.ViewVideoAdapter;
import com.app.hasanah.UI.Outer.OuterMainActivity;
import com.app.hasanah.Utils.Dialoge.SweetDialog;
import com.app.hasanah.databinding.FragmentAdviceBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdviceFragment extends Fragment {
    private FragmentAdviceBinding mBinding;
    private int positionType;
    private SweetAlertDialog loading;
    private ViewVideoAdapter adapter;
    private ArrayList<AdviceModel> arrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentAdviceBinding.inflate(inflater,container,false);
        positionType=getArguments().getInt("type");

        mBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OuterMainActivity.class));
            }
        });
        mBinding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_advice_to_uploadPhoto);
            }
        });
        mBinding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_advice_to_profile);
            }
        });
        mBinding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_advice_to_home);
            }
        });

        funLoading();
        getVideo();
        arrayList=new ArrayList<>();
        adapter=new ViewVideoAdapter(arrayList,getActivity());
        mBinding.videos.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.videos.setAdapter(adapter);
        return mBinding.getRoot();
    }


    private void getVideo() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("video");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.dismiss();
                String video;
                if (positionType==1) {
                    video = snapshot.child("1").getValue().toString();
                }
                else if (positionType==0){
                    video = snapshot.child("0").getValue().toString();
                }
                else if (positionType==2){
                    video = snapshot.child("3").getValue().toString();
                }
                else {
                    video = snapshot.child("2").getValue().toString();
                }
                AdviceModel model=new AdviceModel("",video);
                arrayList.add(model);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*private String getAdvice() {
        if (positionType == 0) {
            return getString(R.string.Takbirat);
        } else if (positionType == 1) {
            return getString(R.string.Ruku);
        } else if (positionType == 2) {
            return getString(R.string.Afterruku);
        } else if (positionType == 3) {
            return getString(R.string.Sujud);
        }

        return  "";
    }*/
    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }
    private void navigationTo(int id)
    {
        Bundle b=new Bundle();
        b.putInt("type",positionType);
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_auth).navigate(id,b);
    }
}