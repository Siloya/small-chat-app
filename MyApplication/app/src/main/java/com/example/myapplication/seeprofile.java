package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class seeprofile extends AppCompatActivity {


    EditText vname;
    TextView updtp;
    ImageView vimage;

    private String ImageURIacessToken;

    ImageButton backbtn;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeprofile);
        vimage= findViewById(R.id.imgv1);
        vname = findViewById(R.id.name);
        updtp = findViewById(R.id.updatp);
        firebaseFirestore = FirebaseFirestore.getInstance();

        backbtn = findViewById(R.id.rju3);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent it = new Intent(seeprofile.this,recycleview.class);
                startActivity(it);

            }
        });
        //HY LL SURA
        storageReference =   firebaseStorage.getReference();
        storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIacessToken = uri.toString();
              //  CookieHandler Picasso = null;

                Picasso.get().load(uri).into(vimage);
            }
        });
      DatabaseReference databaseReference= firebaseDatabase.getReference(firebaseAuth.getUid());
        ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userprofile musprofile = snapshot.getValue(userprofile.class);
                vname.setText(musprofile.getuname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(),"failed to fetch",Toast.LENGTH_SHORT).show();

            }
        });
        updtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(seeprofile.this,UpdateProfile.class);
                intent.putExtra("nameofuser",vname.getText().toString());
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Now User is Offline",Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Now User is Online",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
