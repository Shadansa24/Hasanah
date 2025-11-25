package com.app.hasanah.UI.Outer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.app.hasanah.R;
import com.app.hasanah.Utils.Common;
import com.app.hasanah.Utils.Dialoge.SweetDialog;
import com.app.hasanah.Utils.RealPathUtil;
import com.app.hasanah.WebApi.APIInterface;
import com.app.hasanah.WebApi.ApiRetrofit;
import com.app.hasanah.WebApi.RespnseName;
import com.app.hasanah.databinding.FragmentOuterUploadFileBinding;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OuterUploadFileFragment extends Fragment {
    private FragmentOuterUploadFileBinding mBinding;
    private SweetAlertDialog loading;
    private static final int pic_id = 123;
    private APIInterface apiInterface;
    private Uri filePath;
    private String path;
    private File file;
    private int positionType = 0, status = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentOuterUploadFileBinding.inflate(inflater, container, false);


        mBinding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_outerUpload_to_start);
            }
        });
        mBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationTo(R.id.action_outerUpload_to_outerResult);
            }
        });
        apiInterface = ApiRetrofit.getClient().create(APIInterface.class);

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
            filePath = data.getData();
            path = RealPathUtil.getRealPath(getActivity(), filePath);
            funLoading();
            Log.e("path", path);
            if (positionType == 0) {
                getAiResponseTakbeer();
            } else if (positionType == 1) {
                getAiResponseRuku();
            } else if (positionType == 2) {
                getAiResponseRaising();
            } else if (positionType == 3) {
                getAiResponseSujud();
            }

        }
    }

    public void getAiResponseRuku() {

        if (path != null) {
            file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imagefile", file.getName(), requestBody);
            apiInterface.UploadFileRuku(body).enqueue(new Callback<RespnseName>() {
                @Override
                public void onResponse(Call<RespnseName> call, Response<RespnseName> response) {

                    loading.dismiss();
                    if (response.isSuccessful()) {
                        Log.e("succ", response.body().toString());
                        Log.e("error2", response.body().getImage_base64());
                        Common.respnseName = response.body();
                        funLoginSuccessfully();


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
        } else {

            Toast.makeText(getActivity(), "path null", Toast.LENGTH_SHORT).show();
        }


    }

    public void getAiResponseTakbeer() {

        if (path != null) {
            file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imagefile", file.getName(), requestBody);
            apiInterface.UploadFileTakbeer(body).enqueue(new Callback<RespnseName>() {
                @Override
                public void onResponse(Call<RespnseName> call, Response<RespnseName> response) {

                    loading.dismiss();
                    if (response.isSuccessful()) {
                        Log.e("succ", response.body().toString());
                        Log.e("error2", response.body().getImage_base64());
                        Common.respnseName = response.body();
                        funLoginSuccessfully();
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
        } else {

            Toast.makeText(getActivity(), "path null", Toast.LENGTH_SHORT).show();
        }


    }

    public void getAiResponseRaising() {

        if (path != null) {
            file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imagefile", file.getName(), requestBody);
            apiInterface.UploadFileRaising(body).enqueue(new Callback<RespnseName>() {
                @Override
                public void onResponse(Call<RespnseName> call, Response<RespnseName> response) {

                    loading.dismiss();
                    if (response.isSuccessful()) {
                        Log.e("succ", response.body().toString());
                        Log.e("error2", response.body().getImage_base64());
                        Common.respnseName = response.body();
                        funLoginSuccessfully();
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
        } else {

            Toast.makeText(getActivity(), "path null", Toast.LENGTH_SHORT).show();
        }
    }
    public void getAiResponseSujud() {

        if (path != null) {
            file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("imagefile", file.getName(), requestBody);
            apiInterface.UploadFileASujud(body).enqueue(new Callback<RespnseName>() {
                @Override
                public void onResponse(Call<RespnseName> call, Response<RespnseName> response) {

                    loading.dismiss();
                    if (response.isSuccessful()) {
                        Log.e("succ", response.body().toString());
                        Log.e("error2", response.body().getImage_base64());
                        Common.respnseName = response.body();
                        funLoginSuccessfully();
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
        } else {

            Toast.makeText(getActivity(), "path null", Toast.LENGTH_SHORT).show();
        }
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