package com.app.hasanah.UI.User;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.app.hasanah.DataClass.UserDataClass;
import com.app.hasanah.R;
import com.app.hasanah.UI.Outer.OuterMainActivity;
import com.app.hasanah.Utils.Common;
import com.app.hasanah.Utils.DateAndTime.CustomDatePickerDialog;
import com.app.hasanah.Utils.Dialoge.SweetDialog;
import com.app.hasanah.Utils.SaveImageRepo;
import com.app.hasanah.databinding.FragmentUserProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserProfileFragment extends Fragment {
    private FragmentUserProfileBinding mBinding;
    private SweetAlertDialog loading;
    private UserDataClass user;
    private String image;
    private String userId;
    private DatabaseReference database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        database = FirebaseDatabase.getInstance().getReference(Common.userTable);
        mBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OuterMainActivity.class));
            }
        });
        mBinding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_profile_to_uploadPhoto);
            }
        });

        mBinding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_profile_to_home);
            }
        });
        mBinding.password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_profile_to_updatePassword);
            }
        });
        mBinding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkThePermission();
            }
        });
        mBinding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        funLoading();
        getUserData();
        clickUpdate();
        return mBinding.getRoot();
    }

    private void getUserData() {
        database.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loading.dismiss();
                if (snapshot.exists()) {
                    String userName = snapshot.child("userName").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String dob = snapshot.child("dob").getValue().toString();
                    String user_id = snapshot.child("user_id").getValue().toString();
                    image = snapshot.child("photo").getValue().toString();
                    mBinding.userName.setText(userName);
                    mBinding.date.setText(dob);
                    mBinding.email.setText(email);
                    if (!image.isEmpty()) {
                        Glide.with(getActivity()).load(image).into(mBinding.imageProfile);
                    }
                    user = new UserDataClass(userName, email, dob, image, user_id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clickUpdate() {
        mBinding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mBinding.userName.getText().toString();
                String dob = mBinding.date.getText().toString();

                if (userName.isEmpty()) {
                    mBinding.userName.setError("Please enter your name");
                } else if (dob.isEmpty()) {
                    mBinding.date.setError("Please enter your DOB");
                } else {
                    alertDialog();
                }
            }
        });
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change user data")
                .setMessage("Are you really sure about changing user data? ");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                funLoading();
                saveData();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveData() {
        user.setUserName(mBinding.userName.getText().toString());
        user.setDob(mBinding.date.getText().toString());
        user.setPhoto(image);
        database.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                funSuccessfully();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                funField("Failed to change data, please try again ");
            }
        });
    }

    private void checkThePermission() {
        Log.e("test", "tttt");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
                Log.e("TAG1", "onViewCreated: openGallery ");
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 2);

            }
        } else {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
                Log.e("TAG", "onViewCreated: openGallery ");
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Glide.with(getActivity()).load(data.getData().toString()).into(mBinding.imageProfile);
                funLoading();
                saveImage(data.getData());

            }
        }
    }

    private void saveImage(Uri uri) {
        SaveImageRepo repo = new SaveImageRepo();
        repo.saveImage(uri, "profileImage").observe(getViewLifecycleOwner(), path -> {
            loading.dismiss();
            if (path.equals("failed")) {
                funField("There was an error adding the image. Please try again.");
            } else {
                this.image = path;
            }
        });
    }

    private void showDatePickerDialog() {
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(
                getActivity(),
                (view, year, month, dayOfMonth) -> {
                    // Handle date selection
                    String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                    mBinding.date.setText(date);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }

    private void funSuccessfully() {
        SweetAlertDialog dialog = SweetDialog.success(getActivity(), "User data has been changed successfully.");
        dialog.show();
    }

    private void funField(String title) {
        SweetAlertDialog dialog = SweetDialog.failed(getActivity(), title);
        dialog.show();
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        });
    }

    private void navigationTo(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_auth).navigate(id);
    }
}