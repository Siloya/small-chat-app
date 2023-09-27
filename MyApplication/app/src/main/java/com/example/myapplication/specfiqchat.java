package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.notification.Client;
import com.example.myapplication.notification.Data;
import com.example.myapplication.notification.Myresponse;
import com.example.myapplication.notification.Sender;
import com.example.myapplication.notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.Query;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.toolbox.JsonObjectRequest;


public class specfiqchat extends AppCompatActivity {
//tht
    EditText mgetmsg;
    ImageButton msendmsgbtn;
    //CardView msendmsgcardv;
   // androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    //fo2
    ImageView imguser;
    TextView nameuser;
    ImageButton backbtn;
    RelativeLayout bar;

    private String enteredmsg;
    String recievername,sendername,recieveruid,senderuid;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderroom,recieverroom;
    String uri;
    RecyclerView msgerecyclerv;
    String curentime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
   Intent intent;
   // MessagesAdapter messagesAdapter;
    //ArrayList<Messages> messagesArrayList;
   messageAdapter messagesAdapter;
    ArrayList<message> messagesArrayList;
    Apiservice apiservice;
    FirebaseUser fuser ;
   // boolean notify = false;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specfiqchat);
       // fuser = firebaseAuth.getCurrentUser();

        mgetmsg= findViewById(R.id.getmessage);
        msendmsgbtn=findViewById(R.id.sendicon);
       imguser= findViewById(R.id.imageview);
        nameuser= findViewById(R.id.nameuser);
        backbtn= findViewById(R.id.backbtn);
        msgerecyclerv= findViewById(R.id.recyclev);
        bar = findViewById(R.id.mytool);
   intent= getIntent();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        calendar= Calendar.getInstance();
        simpleDateFormat= new SimpleDateFormat("hh:mm a");

        if (getIntent().hasExtra("title")) {
            recieveruid = getIntent().getStringExtra("hisID");
            uri = intent.getStringExtra("hisImage");
        }
        else {

            recieveruid = getIntent().getStringExtra("receiveruid");
            Log.d("receiveruid", recieveruid);
            recievername = getIntent().getStringExtra("name");
            Log.d("recievername", recievername);
             uri = intent.getStringExtra("imageuri");

        }
        nameuser.setText(recievername);
        if (uri == null){
            Toast.makeText(getApplicationContext(), "null image", Toast.LENGTH_SHORT).show();
        }//1
        else{
            Picasso.get().load(uri).into(imguser);
        }

        senderuid=  firebaseAuth.getUid();
         senderroom=senderuid+recieveruid;
         recieverroom=recieveruid+senderuid;
        Log.d("senderroom", senderroom);
        Log.d("receiverroom", recieverroom);


        messagesArrayList=new ArrayList<>();
     /*   for (int i = 0; i < messagesArrayList.size(); i++) {
            message msg = messagesArrayList.get(i);
            Log.d("Message " + i, "Message: " + msg.getMsg() + ", Sender ID: " + msg.getSenderid() + ", Time: " + msg.getCurenttime());
        }*/

        msgerecyclerv=findViewById(R.id.recyclev);
  //      apiservice = Client.getClient("https://form.googleapis.com/").create(Apiservice.class);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        msgerecyclerv.setLayoutManager(linearLayoutManager);

        messagesAdapter=new messageAdapter(getApplicationContext(),messagesArrayList);
        msgerecyclerv.setAdapter(messagesAdapter);
        DatabaseReference databaseReference=firebaseDatabase.getReference().child("chats").child(senderroom).child("messages");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                int i = 1;
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    message msg=snapshot1.getValue(message.class);
                    if (msg == null) {
                        Log.d("TAG", "onDataChange: null message");
                        continue;
                    }
                    //assert msg != null;
                    messagesArrayList.add(msg);
                    Log.d("Message " + i++, "Message: " + msg.getMsg() + ", Sender ID: " + msg.getSenderid() + ", Time: " + msg.getCurenttime());
                }
                messagesAdapter.notifyDataSetChanged();
              //  messageAdapter.notifyItemInserted();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),recycleview.class);
                startActivity(intent);
            }
        }  );


        msendmsgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredmsg=mgetmsg.getText().toString();
                if (enteredmsg.isEmpty()){
                    Toast.makeText(specfiqchat.this, "enter a message first j", Toast.LENGTH_SHORT).show();
                }
                else{
                  //  notify=true;

                 Date date = new Date();
                 curentime=simpleDateFormat.format(calendar.getTime());
                 message msg = new message(enteredmsg, firebaseAuth.getUid(),curentime,date.getTime());
                 firebaseDatabase= FirebaseDatabase.getInstance();
                 firebaseDatabase.getReference().child("chats")
                         .child(senderroom)
                         .child("messages")
                         .push()
                         .setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 firebaseDatabase.getReference().child("chats")
                                         .child(recieverroom)
                                         .child("messages")
                                          .push()
                                         .setValue(msg)
                                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {}});
                             }
                         });
                    getToken(enteredmsg,recieveruid,uri,recieverroom,recievername);
                    mgetmsg.setText(null);
                }
            }});

       /* final String msg message;
        reference "Users") .child(fuser.getUid());
        reference.addValueEventListener (new ValueEventListenez() I
        @Override
        public void onDataChange (@NonNull DataSnapshot dataSnapshot) I
        User user= dataSnapshot. getValue (User.class);
        sendNotifiaction (recieveruid, user. getUsername(), msg);
        1
        @Override
        public void onCancelled (@NonNull DatabaseError databaseError)*/

       /* final String msg=  enteredmsg;


      /* // DocumentReference documentReference= FirebaseFirestore.getInstance().collection("Users").document(firebaseAuth.getUid());
        com.google.firebase.firestore.Query query = FirebaseFirestore.getInstance().collection("Users").whereEqualTo("uid", firebaseAuth.getUid());
        OnCompleteListener<QuerySnapshot> a = new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        };

        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        DocumentReference reference = fireStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent( DocumentSnapshot snapshot,  FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Error getting data.", error);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                        Firebasemodel user = snapshot.toObject( Firebasemodel .class);
                    if (notify) {
                        SendNotification(recieveruid, user.getName(), msg);
                        notify = false;
                    }
                }
            }
        });


        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("uid", firebaseAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                  //  DocumentSnapshot a = task.getResult().getDocuments().get(0);
                    //a.toObject();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Firebasemodel firebasemodel= (Firebasemodel) document.toObject(Firebasemodel.class);
                        if (notify) {
                            SendNotification(recieveruid, firebasemodel.getName(), msg);
                            notify = false;
                        }

                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }

                }

            }
        );

       DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (notify) {
SendNotification (recieveruid,user.getUsername(),msg);
notify = false;
                }

            }       @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/
        /*
private void SendNotification(String receiver,String username,String message){
    DatabaseReference  tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=  tokens.orderByKey().equalTo(receiver);
    query.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot datasnapshot) {
            for(DataSnapshot snapshot : datasnapshot.getChildren()){

                Token token =  snapshot.getValue(Token.class);
                Data data = new Data(fuser.getUid(),R.mipmap.ic_launcher,username + ": "+message,"New Message",recieveruid);

            Sender sender = new Sender(data,token.getToken());

            apiservice.sendNotification(sender)
                    .enqueue(new Callback<Myresponse>() {
                        @Override
                        public void onResponse(Call<Myresponse> call, Response<Myresponse> response) {
                            if(response.code()==200){
                                if(response.body().succes != 1){
                                    Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Myresponse> call, Throwable t) {

                        }
                    });
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });*/

}
    private void getToken(String message, String hisID, String myImage, String chatID,String hisName) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token = snapshot.child("token").getValue().toString();
                userprofile user = snapshot.getValue(userprofile.class);



              JSONObject to = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    //user huwe ana
                    data.put("title", user.uname);
                    data.put("message", message);
                    data.put("hisID", user.usid);
                    data.put("myImage", myImage);
                    data.put("chatID", chatID);
                    data.put("hisName",hisName);


                    to.put("to", token);
                    to.put("data", data);

                    sendNotification(to);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendNotification(JSONObject to) {

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, "https://fcm.googleapis.com/fcm/send", to, response -> {
            Log.d("notification", "sendNotification: " + response);
        }, error -> {
            Log.d("notification", "sendNotification: " + error);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "key=" + "AAAA1p7h8yc:APA91bHXsdKqY_96jgcLjIMor5LvY9FKXXCMXWe6M9wBBPYum-yuK207E4mFTyWLRe8v9ELj6m9oTAaXKdsTGUyrpSJYYO0sugYQ_nzEeCI-CVuBzY96g_2szWqK9RNJt8T6D84suRs1");
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy
                (new DefaultRetryPolicy(30000,

                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }



    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }
}