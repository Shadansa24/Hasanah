package com.app.hasanah.UI.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hasanah.R;
import com.app.hasanah.UI.Outer.OuterMainActivity;
import com.app.hasanah.Utils.Common;
import com.app.hasanah.databinding.FragmentResultBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResultFragment extends Fragment {
    private FragmentResultBinding mBinding;
    private Bitmap bitmap;
    private int positionType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentResultBinding.inflate(inflater,container,false);
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
                navigationTo(R.id.action_result_to_uploadPhoto);
            }
        });
        mBinding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_result_to_profile);
            }
        });
        mBinding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_result_to_home);
            }
        });
        mBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_result_to_advice);
            }
        });
        viewData();
        checkTheStatus();
        return mBinding.getRoot();
    }
    private void viewData()
    {
        byte[] decodedByte = Base64.decode(Common.respnseName.getImage_base64(), Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        mBinding.image.setImageBitmap(bitmap);
    }
    private void checkTheStatus()
    {
        if (Common.respnseName.isIs_pose())
        {
            mBinding.text.setText("No mistakes found");
            mBinding.correction.setVisibility(View.GONE);
            mBinding.next.setVisibility(View.GONE);

        }
        else{
            mBinding.text.setText("mistakes found");
            mBinding.correction.setVisibility(View.VISIBLE);
            mBinding.next.setVisibility(View.VISIBLE);

        }
        if (positionType==0)
        {
            mBinding.correctionImage.setImageResource(R.drawable.tackbeer);
        }
        else if (positionType==1){
            mBinding.correctionImage.setImageResource(R.drawable.raku);
        }
        else if (positionType==2){
            mBinding.correctionImage.setImageResource(R.drawable.after_raku);
        }
        else if (positionType==3){
            mBinding.correctionImage.setImageResource(R.drawable.sajud);
        }
    }
    private void navigationTo(int id)
    {
        Bundle b=new Bundle();
        b.putInt("type",positionType);
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_auth).navigate(id,b);
    }
}