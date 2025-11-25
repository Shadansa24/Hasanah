package com.app.hasanah.UI.Outer;

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
import com.app.hasanah.Utils.Common;
import com.app.hasanah.databinding.FragmentOuterResultBinding;
import com.app.hasanah.databinding.FragmentResultBinding;

public class OuterResultFragment extends Fragment {
    private FragmentOuterResultBinding mBinding;
    private Bitmap bitmap;
    private int positionType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding= FragmentOuterResultBinding.inflate(inflater,container,false);

        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_outerResult_to_outerUpload);
            }
        });
        mBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_outerResult_to_outerAdvice);
            }
        });
        positionType=getArguments().getInt("type");
        viewData();
        checkTheStatus();
        return mBinding.getRoot();
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
    private void viewData()
    {
        byte[] decodedByte = Base64.decode(Common.respnseName.getImage_base64(), Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        mBinding.image.setImageBitmap(bitmap);
    }

    private void navigationTo(int id)
    {
        Bundle b=new Bundle();
        b.putInt("type",positionType);
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_auth).navigate(id,b);
    }
}