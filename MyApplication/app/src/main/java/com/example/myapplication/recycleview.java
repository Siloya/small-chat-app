package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class recycleview extends AppCompatActivity {
    TabLayout tabLayout;
    TabItem chat,call,status;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        tabLayout=findViewById(R.id.chatbar);
        chat=findViewById(R.id.chat);
        call=findViewById(R.id.call);
        status=findViewById(R.id.status);
        viewPager=findViewById(R.id.fragmentcontainer);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        pagerAdapter= new pageradapteur(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              //HY L MUHME
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition()==1|| tab.getPosition()==2)
                {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.profile:
                Intent intent=new Intent(recycleview.this,seeprofile.class);
                startActivity(intent);
                break;

            case R.id.settings:
                Toast.makeText(getApplicationContext(),"Settign is clicked",Toast.LENGTH_SHORT).show();
                break;
        }



        return  true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menus) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menun,menus);


        return true;
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