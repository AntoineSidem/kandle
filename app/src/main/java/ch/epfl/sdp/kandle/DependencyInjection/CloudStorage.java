package ch.epfl.sdp.kandle.DependencyInjection;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CloudStorage extends Storage {

    private static final CloudStorage instance = new CloudStorage();

    private static final StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://kandle-1b646.appspot.com");

    public static CloudStorage getInstance() {
        return instance;
    }

    @Override
    public Task<Uri> storeAndGetDownloadUrl(String fileExtension, Uri fileUri) {
        String path = System.currentTimeMillis() + "." + fileExtension;
        final StorageReference fileReference = storageReference.child(path);
        UploadTask uploadTask = fileReference.putFile(fileUri);
        return uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException() != null? task.getException() : new Exception("Unknown error");
            }
            return fileReference.getDownloadUrl();
        });
    }
}