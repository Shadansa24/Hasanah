package com.app.hasanah.UI.Outer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hasanah.R;
import com.app.hasanah.Utils.Dialoge.SweetDialog;
import com.app.hasanah.databinding.FragmentForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding mBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentForgotPasswordBinding.inflate(inflater,container,false);

        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_forgotPassword_to_login);
            }
        });

        mBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mBinding.email.getText().toString();
                if (email.isEmpty())
                {
                    mBinding.email.setError("Enter your password");
                }
                else{
                    sendEmail(email);
                }
            }
        });
        return mBinding.getRoot();
    }
    private void sendEmail(String email)
    {
        FirebaseAuth auth=FirebaseAuth.getInstance();
       auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            funSuccessfully();
                        } else {
                            funField("Failed to send password. Please try again.");
                        }
                    }
                });
    }
    private void funSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "Email sent successfully");
        success.show();
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
    private void navigationTo(int id)
    {
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_auth).navigate(id);
    }
}