package com.example.myapplication;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity {
   private CardView grtuserimage;
    private ImageView userimg;
    private static int PICK_IMAGE= 123;
    private Uri imgpath;
    private EditText tusername;
    private MaterialButton savepbtn;
    private FirebaseAuth firebaseAuth;
    private String name;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private String imgurltoken;
    private FirebaseFirestore firebaseFirestore;
    ProgressBar mprogress;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore =FirebaseFirestore.getInstance();
     firebaseStorage = FirebaseStorage.getInstance();
     storageReference = firebaseStorage.getReference();

        userimg = (ImageView) findViewById(R.id.imgv1);
        tusername = (EditText) findViewById(R.id.name);
        savepbtn= (MaterialButton) findViewById(R.id.profbtn);
        mprogress = (ProgressBar) findViewById(R.id.progress);
        grtuserimage = (CardView)findViewById(R.id.getuserimage);
        grtuserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(intent,PICK_IMAGE);
            }
        });

        savepbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=tusername.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText (getApplicationContext(),  "Name is Empty", Toast.LENGTH_LONG).show();
                }
                else if (imgpath==null)
                {
                    Toast.makeText(getApplicationContext(),  "Image is Empty", Toast.LENGTH_LONG).show();
                }
                else{
                    mprogress.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    mprogress.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(profile.this,recycleview.class);
                    startActivity(intent);
                    finish();

                }
            }
        });


    }
    private void  sendDataForNewUser(){
        sendDataToRealTimeDatabse();
    }
    private void  sendDataToRealTimeDatabse(){
        name =tusername.getText().toString().trim();
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
       DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        //userprofile muserprofile=new userprofile(name,firebaseAuth.getUid());
        userprofile muserprofile=new userprofile(name,firebaseAuth.getUid());
        databaseReference.setValue(muserprofile);
        Toast.makeText(getApplicationContext(),"User Profile Added Sucessfully",Toast.LENGTH_LONG).show();
        sendImagetoStorage();

    }

    private void sendImagetoStorage()
    {

        StorageReference imageref=storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic");
        

        //Image compresesion

        Bitmap bitmap=null;
        try {
            bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imgpath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data=byteArrayOutputStream.toByteArray();

        ///putting image to storage

        UploadTask uploadTask=imageref.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imgurltoken=uri.toString();
                        Toast.makeText(getApplicationContext(),"URI get sucess",Toast.LENGTH_LONG).show();
                        sendDataTocloudFirestore();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"URI get Failed",Toast.LENGTH_LONG).show();
                    }


                });
                Toast.makeText(getApplicationContext(),"Image is uploaded",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Image Not UPdloaded",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void sendDataTocloudFirestore() {


        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String , Object> userdata=new HashMap<>();
        userdata.put("name",name);
        userdata.put("image",imgurltoken);
        userdata.put("uid",firebaseAuth.getUid());
        userdata.put("status","Online");

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Data on Cloud Firestore send success",Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imgpath=data.getData();
            userimg.setImageURI(imgpath);
        }
        super.onActivityResult (requestCode, resultCode, data);
    }


}