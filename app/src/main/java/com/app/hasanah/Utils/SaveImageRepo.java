package com.app.hasanah.Utils;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class SaveImageRepo {
    private StorageReference storageReference;
    private UploadTask uploadTask;

    public LiveData<String> saveImage(Uri data, String category) {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        storageReference = FirebaseStorage.getInstance().getReference(category);
        StorageReference reference = storageReference.child(UUID.randomUUID().toString());
        uploadTask = reference.putFile(data);

        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    //Uri downloadUri=task.getResult();
                    String image = task.getResult().toString();
                    liveData.setValue(image);
                }
            }
        });
        return liveData;
    }
}
