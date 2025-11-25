package com.app.hasanah.UI.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hasanah.R;
import com.app.hasanah.UI.Outer.OuterMainActivity;
import com.app.hasanah.Utils.Common;
import com.app.hasanah.Utils.Dialoge.SweetDialog;
import com.app.hasanah.Utils.Validation.PasswordValidation;
import com.app.hasanah.databinding.FragmentUpdatePasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UpdatePasswordFragment extends Fragment {
    private FragmentUpdatePasswordBinding mBinding;
    private FirebaseUser user;
    private SweetAlertDialog loading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding=FragmentUpdatePasswordBinding.inflate(inflater,container,false);
        mBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OuterMainActivity.class));
            }
        });
        mBinding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_updatePassword_to_uploadPhoto);
            }
        });
        mBinding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_updatePassword_to_profile);
            }
        });
        mBinding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_updatePassword_to_home);
            }
        });
        updatePassword();
        return mBinding.getRoot();
    }
    private void updatePassword() {
        mBinding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
    }

    private void checkData() {
        String newPassword = mBinding.password.getText().toString();
        String confirm = mBinding.confirm.getText().toString();
       if (!PasswordValidation.validatePassword(newPassword)) {
           mBinding.password.setError("Please enter a strong password that contains uppercase letters, lowercase letters, numbers and symbols (at least 8 characters)");
        } else if (!confirm.equals(newPassword)) {
            mBinding.confirm.setError("Password does not match");
        } else {
            alertDialog(newPassword);
        }

    }

    private void alertDialog(String newPassword) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Modify password")
                .setMessage("Do you really want to change your password? ");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                funLoading();
                changePassword(newPassword);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void changePassword(String newPassword) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), Common.password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // User re-authenticated successfully, now update password
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            loading.dismiss();
                                            if (task.isSuccessful()) {
                                                // Password updated successfully
                                                funSuccessfully();
                                            } else {
                                                // Handle password update failure
                                                funField("Please enter the old password correctly.");
                                            }
                                        }
                                    });
                        } else {
                            // Handle re-authentication failure
                            loading.dismiss();
                            funField("Please enter the  password correctly.");
                        }
                    }
                });
    }

    private void funSuccessfully() {
        SweetAlertDialog dialog = SweetDialog.success(getActivity(), "Password changed successfully");
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

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
    }
    private void navigationTo(int id)
    {
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment_activity_auth).navigate(id);
    }
}