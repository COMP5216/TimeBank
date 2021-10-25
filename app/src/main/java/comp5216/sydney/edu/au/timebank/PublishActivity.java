package comp5216.sydney.edu.au.timebank;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import comp5216.sydney.edu.au.timebank.model.Task;
import comp5216.sydney.edu.au.timebank.viewmodel.MainActivityViewModel;

public class PublishActivity extends Activity {
    private Spinner taskTimeCoins;
    private ImageButton imageButton;
    private MyPermissions permissions=new MyPermissions(this);
    public String photoFileName = "photo.jpg";
    private File file;
    private Query mQuery;
    private static final int LIMIT = 50;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorageRef;
    private TextInputEditText taskTitle;
    private TextInputEditText taskDate;
    private TextInputEditText taskDescription;
    private String newImagePath=null;
    //OPEN_CAMERA PERMISSIONS CODE
    private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 101;

    private MainActivityViewModel mViewModel;
    private static final int RC_SIGN_IN = 9001;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Populate the screen using the layout
        setContentView(R.layout.activity_publish);
        taskTimeCoins=findViewById(R.id.spinner1);
        imageButton=findViewById(R.id.upload);
        taskTitle=findViewById(R.id.taskTitle);
        taskDate=findViewById(R.id.taskDate);
        taskDescription=findViewById(R.id.taskDescription);
        imageButton.setImageResource(R.drawable.ic_baseline_camera);
        ArrayAdapter<String> timeCoinsAdaptor=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.time_coins));
        timeCoinsAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskTimeCoins.setAdapter(timeCoinsAdaptor);
        initFirestore();
    }


    public void onSubmitClick(View view){
        CollectionReference tasks = mFirestore.collection("tasks");
        Task newTask=null;
        if(newImagePath!=null){
            String s[]=newImagePath.split("/");
            newTask = new Task(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    taskTitle.getText().toString(),taskDescription.getText().toString(),s[s.length-1],
                    Integer.valueOf(taskTimeCoins.getSelectedItem().toString()));
        }else{
            newTask = new Task(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    taskTitle.getText().toString(),taskDescription.getText().toString(),"44",
                    Integer.valueOf(taskTimeCoins.getSelectedItem().toString()));
        }

        tasks.add(newTask);
        System.out.println(newTask);
        uploadImage();
        System.out.println("hhhhhhhhhhhhhhhhhhhello");
        newImagePath=null;
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference();
        // Get the 50 highest rated restaurants
        mQuery = mFirestore.collection("tasks")
                .orderBy("title", Query.Direction.DESCENDING)
                .limit(LIMIT);
    }

    public void onTakePhotoClick(View view) {
        // Check permissions
        if (!permissions.checkPermissionForCamera()
                || !permissions.checkPermissionForExternalStorage()) {
            permissions.requestPermissionForCamera();
        }  else {
            // create Intent to take a picture and return control to the calling application
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // set file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            photoFileName = "IMG_" + timeStamp + ".jpg";

            // Create a photo file reference
            Uri file_uri = getFileUri(photoFileName,0);
            // Add extended data to the intent
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            startActivityForResult(intent, MY_PERMISSIONS_REQUEST_OPEN_CAMERA);
        }
    }

    public void uploadImage(){
        if(newImagePath!=null){
            Uri file = Uri.fromFile(new File(newImagePath));
            String s[]=newImagePath.split("/");
            String name=s[s.length-1];
            StorageReference riversRef = mStorageRef.child(name);
            riversRef.putFile(file);
        }

    }


    public Uri getFileUri(String fileName, int type) {
        Uri fileUri = null;
        try {
            String typestr = "images"; //default to images type
            if (type == 1) {
                typestr = "videos";
            } else if (type != 0) {
                typestr = "audios";
            }
            // Get safe media storage directory depending on type
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().toString()
                    + File.separator + typestr);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }

            // Create the file target for the media based on filename
            file = new File(mediaStorageDir, fileName);

            // Wrap File object into a content provider, required for API >= 24
            // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
            if (Build.VERSION.SDK_INT >= 24) {
                fileUri = FileProvider.getUriForFile(
                        this.getApplicationContext(),
                        "com.example.timebank.fileprovider", file);
            } else {
                fileUri = Uri.fromFile(mediaStorageDir);
            }
        } catch (Exception ex) {
            Log.e("getFileUri", ex.getStackTrace().toString());
        }
        //get the new image absolute path
        newImagePath=file.getAbsolutePath();
        return fileUri;
    }

}
