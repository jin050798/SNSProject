package com.test.sns_project.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.test.sns_project.Memberinfo;
import com.test.sns_project.R;
import com.test.sns_project.activity.CameraActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MemberinitActivity extends BasicActivity {

    private static final  String TAG = "MemberInitActivity";
    private ImageView profileImageView;
    private String profilePath;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);

        profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(onClickListener);
        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
        findViewById(R.id.gallery).setOnClickListener(onClickListener);
        findViewById(R.id.picture).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 0:{
                if(resultCode == Activity.RESULT_OK){
                    profilePath = data.getStringExtra("profilePath");
                    Glide.with(this).load(profilePath).centerCrop().override(300).into(profileImageView);

                }
                break;
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.checkButton:

                    storageUploader();
                    break;
                case R.id.profileImageView:
                    CardView cardView = findViewById(R.id.buttonsCardView);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.picture:
                    myStartActivity(CameraActivity.class);
                    break;
                case R.id.gallery:
                    myStartActivity(GalleryActivity.class);
                    break;

            }
        }
    };

    private void storageUploader(){

        final String name = ((EditText)findViewById(R.id.nameEditText)).getText().toString();
        final String phoneNumber = ((EditText)findViewById(R.id.phoneNumberEditText)).getText().toString();
        final String birthDay = ((EditText)findViewById(R.id.birthDayEditText)).getText().toString();
        final String address = ((EditText)findViewById(R.id.addressEditText)).getText().toString();


        if(name.length()>0 && phoneNumber.length()>9 && birthDay.length()>5 && address.length()>0){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainsRef = storageRef.child("mountains.jpg");
            final StorageReference mountainImagesRef = storageRef.child("users/"+user.getUid()+"/profileImage.jpg");

            if(profilePath == null){
                Memberinfo memberinfo = new Memberinfo(name, phoneNumber, birthDay, address);
                storeUploader(memberinfo);

            }else{
                try{

                    InputStream stream = new FileInputStream(new File(profilePath));

                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Memberinfo memberinfo = new Memberinfo(name, phoneNumber, birthDay, address,downloadUri.toString());
                                storeUploader(memberinfo);
                            } else {
                                startToast("??????????????? ???????????? ?????????????????????.");
                            }
                        }
                    });
                }catch (FileNotFoundException e){
                    Log.e("??????","??????: "+e.toString());
                }

            }
        }else{
            startToast("?????? ????????? ????????? ?????????.");
        }
    }
    private void storeUploader(Memberinfo memberinfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(memberinfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("???????????? ????????? ?????????????????????.");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("???????????? ????????? ?????????????????????.");
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }
    private void startToast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        startActivityForResult(intent,0);
    }
}


