package com.app.hasanah.UI.User;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.app.hasanah.DataClass.ActivityDataClass;
import com.app.hasanah.R;
import com.app.hasanah.UI.Outer.OuterMainActivity;
import com.app.hasanah.Utils.Common;
import com.app.hasanah.Utils.Dialoge.SweetDialog;
import com.app.hasanah.Utils.RealPathUtil;
import com.app.hasanah.WebApi.APIInterface;
import com.app.hasanah.WebApi.ApiRetrofit;
import com.app.hasanah.WebApi.RespnseName;
import com.app.hasanah.databinding.FragmentUploadPhotoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadPhotoFragment extends Fragment {
    private FragmentUploadPhotoBinding mBinding;
    private SweetAlertDialog loading;
    private static final int pic_id = 123;
    private APIInterface apiInterface;
    private Uri filePath;
    private String path, userId;
    private File file;
    private int positionType = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentUploadPhotoBinding.inflate(inflater, container, false);


        mBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_uploadPhoto_to_result);
            }
        });
        mBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OuterMainActivity.class));
            }
        });
        mBinding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_uploadPhoto_to_profile);
            }
        });
        mBinding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_uploadPhoto_to_home);
            }
        });
        apiInterface = ApiRetrofit.getClient().create(APIInterface.class);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        getPositionType();
        pickImage();
        return mBinding.getRoot();
    }

    private void getPositionType() {
        mBinding.takbirat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    positionType = 0;
            }
        });
        mBinding.Ruku.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    positionType = 1;
            }
        });
        mBinding.afterRuku.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    positionType = 2;
            }
        });
        mBinding.sujud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    positionType = 3;
            }
        });
    }

    private void pickImage() {
        mBinding.uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("test", "tttt");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                        Log.e("TAG", "onViewCreated: openGallery ");
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 2);

                    }
                } else {
                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                        Log.e("TAG", "onViewCreated: openGallery ");
                    } else {
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), pic_id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                filePath = data.getData();
                prepareImage();
            }
        }
    }

    private void prepareImage() {
        path = RealPathUtil.getRealPath(getActivity(), filePath);
        funLoading();
        Log.e("path", path);
        if (path != null) {
            file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imagefile", file.getName(), requestBody);
            if (positionType == 0) {
                getAiResponseTakbeer(body);
            } else if (positionType == 1) {
                getAiResponseRuku(body);
            } else if (positionType == 2) {
                getAiResponseRaising(body);
            } else if (positionType == 3) {
                getAiResponseSujud(body);
            }
        } else {
            Toast.makeText(getActivity(), "path null", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAiResponseTakbeer(MultipartBody.Part body) {
        apiInterface.UploadFileTakbeer(body).enqueue(new Callback<RespnseName>() {
            @Override
            public void onResponse(Call<RespnseName> call, Response<RespnseName> response) {

                loading.dismiss();
                if (response.isSuccessful()) {
                    Log.e("succ", response.body().toString());
                    Common.respnseName = response.body();
                    funLoginSuccessfully();
                    saveToActivities(response.body());
                } else {
                    Log.e("failed", "failed " + response.message() + " " + response.code());
                    funField("There was an error connecting to the server.");

                }
            }

            @Override
            public void onFailure(Call<RespnseName> call, Throwable t) {
                loading.dismiss();
                Log.e("error", t.getMessage());

                Toast.makeText(getContext(), "error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAiResponseRuku(MultipartBody.Part body) {
        apiInterface.UploadFileRuku(body).enqueue(new Callback<RespnseName>() {
            @Override
            public void onResponse(Call<RespnseName> call, Response<RespnseName> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    Common.respnseName = response.body();
                    funLoginSuccessfully();
                    saveToActivities(response.body());
                } else {
                    Log.e("failed", "failed " + response.message() + " " + response.code());
                    funField("There was an error connecting to the server.");

                }
            }

            @Override
            public void onFailure(Call<RespnseName> call, Throwable t) {
                loading.dismiss();
                Log.e("error", t.getMessage());

                Toast.makeText(getContext(), "error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAiResponseSujud(MultipartBody.Part body) {
        apiInterface.UploadFileASujud(body).enqueue(new Callback<RespnseName>() {
            @Override
            public void onResponse(Call<RespnseName> call, Response<RespnseName> response) {
                loading.dismiss();
                if (response.isSuccessful()) {
                    Log.e("succ", response.body().toString());
                    Common.respnseName = response.body();
                    funLoginSuccessfully();
                    saveToActivities(response.body());
                } else {
                    Log.e("failed", "failed " + response.message() + " " + response.code());
                    funField("There was an error connecting to the server.");

                }
            }

            @Override
            public void onFailure(Call<RespnseName> call, Throwable t) {
                loading.dismiss();
                Log.e("error", t.getMessage());

                Toast.makeText(getContext(), "error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAiResponseRaising(MultipartBody.Part body) {
        apiInterface.UploadFileRaising(body).enqueue(new Callback<RespnseName>() {
            @Override
            public void onResponse(Call<RespnseName> call, Response<RespnseName> response) {

                loading.dismiss();
                if (response.isSuccessful()) {
                    Log.e("succ", response.body().toString());
                    Common.respnseName = response.body();
                    funLoginSuccessfully();
                    saveToActivities(response.body());
                } else {
                    Log.e("failed", "failed " + response.message() + " " + response.code());
                    funField("There was an error connecting to the server.");

                }
            }

            @Override
            public void onFailure(Call<RespnseName> call, Throwable t) {
                loading.dismiss();
                Log.e("error", t.getMessage());
                Toast.makeText(getContext(), "error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToActivities(RespnseName response) {
        byte[] decodedByte = Base64.decode(response.getImage_base64(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        ActivityDataClass activity = new ActivityDataClass(getTime(), bitmapToString(bitmap), positionType, (response.isIs_pose()) ? 0 : 1);
        FirebaseDatabase.getInstance().getReference(Common.activityTable).child(userId).push().setValue(activity);

    }

    private String getTime() {
        LocalDateTime currentDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm");
            String formattedDateTime = currentDateTime.format(formatter);
            return formattedDateTime;
        }

        return "";
    }

    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void funLoginSuccessfully() {
        SweetAlertDialog success = SweetDialog.success(getContext(), "Image uploaded successfully");
        success.show();
        mBinding.submit.setVisibility(View.VISIBLE);
    }

    private void funLoading() {
        loading = SweetDialog.loading(getContext());
        loading.show();
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
        Bundle b = new Bundle();
        b.putInt("type", positionType);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_auth).navigate(id, b);
    }
}