package com.app.hasanah.UI.Outer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.app.hasanah.DataClass.UserDataClass;
import com.app.hasanah.R;
import com.app.hasanah.UI.User.UserMainActivity;
import com.app.hasanah.Utils.Common;
import com.app.hasanah.Utils.DateAndTime.CustomDatePickerDialog;
import com.app.hasanah.Utils.Dialoge.SweetDialog;
import com.app.hasanah.Utils.Validation.EmailValidator;
import com.app.hasanah.Utils.Validation.PasswordValidation;
import com.app.hasanah.databinding.FragmentRegistrationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrationFragment extends Fragment {
    private FragmentRegistrationBinding mBinding;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private SweetAlertDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRegistrationBinding.inflate(inflater, container, false);

        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_register_to_start);
            }
        });
        mBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_register_to_login);
            }
        });
        mBinding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        mBinding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEnteredData();
            }
        });
        return mBinding.getRoot();
    }

    public void checkEnteredData() {
        String name = mBinding.name.getText().toString();
        String email = mBinding.email.getText().toString();
        String password = mBinding.password.getText().toString();
        String date = mBinding.date.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mBinding.name.setError("Please enter your name");
        } else if (email.isEmpty()) {
            mBinding.email.setError("Please enter your password");
        } else if (!EmailValidator.isValidEmail(email)) {
            mBinding.email.setError("Enter the password correctly 'user@gmail.com'");
        } else if (password.isEmpty()) {
            mBinding.password.setError("Please enter your password");
        } else if (!PasswordValidation.validatePassword(password)) {
            mBinding.password.setError("Please enter a strong password that contains uppercase letters, lowercase letters, numbers and symbols (at least 8 characters)");
        } else if (date.isEmpty()) {
            mBinding.date.setError("Please enter your DOB");
        } else {
            funLoading();
            UserDataClass user = new UserDataClass(name, email, date,"", "");
            signUp(email, password, user);
        }
    }

    public void signUp(String email, String password, UserDataClass user) {
        auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Common.password =password;
                    user.setUser_id(authResult.getUser().getUid().toString());
                    saveUserData(user);
                })
                .addOnFailureListener(e -> {
                    loading.dismiss();
                    funField("Failed to create account Please change your password");

                });

    }
    public void saveUserData(UserDataClass user)
    {
        database= FirebaseDatabase.getInstance().getReference(Common.userTable);
        database.child(user.getUser_id()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                funSuccessfully();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
    private void funLoading() {
        loading = SweetDialog.loading(getActivity());
        loading.show();
    }

    private void funSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "New account created successfully");
        success.show();
        startActivity(new Intent(getActivity(), UserMainActivity.class));
        getActivity().finish();
    }

    private void funField(String message) {
        SweetAlertDialog field = SweetDialog.failed(getContext(), message);
        field.show();
        field.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                field.dismiss();
            }
        });
    }

    private void navigationTo(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_auth).navigate(id);
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
}