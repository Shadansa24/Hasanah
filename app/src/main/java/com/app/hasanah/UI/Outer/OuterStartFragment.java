package com.app.hasanah.UI.Outer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.hasanah.R;
import com.app.hasanah.databinding.FragmentOuterStartBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class OuterStartFragment extends Fragment {
    private FragmentOuterStartBinding mBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentOuterStartBinding.inflate(inflater,container,false);
        mBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_start_to_login);
            }
        });
        mBinding.registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_start_to_register);
            }
        });
        mBinding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_start_to_outerUpload);
            }
        });
       // selectVideo();

        return mBinding.getRoot();
    }
    private void navigationTo(int id)
    {
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_auth).navigate(id);
    }
    private void selectVideo()
    {
        mBinding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri videoUri = data.getData();
            uploadVideo(videoUri,getActivity());
        }
    }
    public void uploadVideo(Uri videoUri, Context context)
    {
        MutableLiveData<String> liveData=new MutableLiveData<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference videoRef = storageRef.child("videos/" + UUID.randomUUID().toString() + ".mp4");

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("تحميل فديو...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();


        UploadTask uploadTask = videoRef.putFile(videoUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                progressDialog.dismiss();
                String downloadUrl = uri.toString();
                FirebaseDatabase.getInstance().getReference("video").child("v1").setValue(downloadUrl);
                liveData.setValue(downloadUrl);

            });
        }).addOnFailureListener(exception -> {
            progressDialog.dismiss();
            liveData.setValue("failed");
        }).addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            progressDialog.setProgress((int) progress);

        });
    }
}