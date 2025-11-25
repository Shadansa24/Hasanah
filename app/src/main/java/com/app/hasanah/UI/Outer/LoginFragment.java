package com.app.hasanah.UI.Outer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.app.hasanah.R;
import com.app.hasanah.UI.User.UserMainActivity;
import com.app.hasanah.Utils.Common;
import com.app.hasanah.Utils.Dialoge.SweetDialog;
import com.app.hasanah.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding mBinding;
    private FirebaseAuth auth;
    private SweetAlertDialog loading;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);

        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_login_to_start);
            }
        });
        mBinding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_login_to_register);
            }
        });
        mBinding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_login_to_forgotPassword);
            }
        });
        login();

        return mBinding.getRoot();
    }

    private void navigationTo(int id) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_auth).navigate(id);
    }

    private void login() {
        mBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mBinding.email.getText().toString();
                String password = mBinding.password.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mBinding.email.setError("Please enter your password");
                }
                if (TextUtils.isEmpty(password)) {
                    mBinding.password.setError("Please enter your password");
                } else {
                    funLoading();
                    signIn(email, password);
                }
            }
        });
    }

    public void signIn(String email, String password) {
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Common.password =password;
                    loading.dismiss();
                    funLoginSuccessfully();

                })
                .addOnFailureListener(e -> {
                    loading.dismiss();
                    funLoginField("Sorry, we couldn't find your account. Please check your password and password.");
                });

    }

    private void funLoginSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "You have successfully logged in.");
        success.show();
        startActivity(new Intent(getActivity(), UserMainActivity.class));
        getActivity().finish();
    }
    private void funLoginField(String message) {
        SweetAlertDialog field = SweetDialog.failed(getContext(), message);
        field.show();
        field.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                field.dismiss();
            }
        });
    }


    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }
}