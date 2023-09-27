package com.example.myapplication;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.notification.Token;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class chat extends Fragment {

   private FirebaseFirestore firebaseFirestore;
   LinearLayoutManager linearLayoutManager;
   private FirebaseAuth firebaseAuth;
   ImageView imageView;
   RecyclerView mrecyclerView;
    FirestoreRecyclerAdapter<Firebasemodel,NoteViewHolder> chatAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        // return inflater.inflate(R.layout.chat,null);
        View v = inflater.inflate(R.layout.chat, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mrecyclerView = v.findViewById(R.id.recycleview);

        // CollectionReference
        Query query = firebaseFirestore.collection("Users").whereNotEqualTo("uid", firebaseAuth.getUid());
        FirestoreRecyclerOptions<Firebasemodel> allname = new FirestoreRecyclerOptions.Builder<Firebasemodel>().setQuery(query, Firebasemodel.class).build();

        chatAdapter =new FirestoreRecyclerAdapter<Firebasemodel, NoteViewHolder>(allname ) {

            @Override
            public void onBindViewHolder(NoteViewHolder noteViewHolder, int position, Firebasemodel firebasemodel) {
                noteViewHolder.name.setText(firebasemodel.getName());
                String uri = firebasemodel.getimg();

                Picasso.get().load(uri).into(noteViewHolder.imageView);

                if (firebasemodel.getStatus().equals("Online")) {
                    noteViewHolder.status.setText(firebasemodel.getStatus());
                    noteViewHolder.status.setTextColor(Color.GREEN);
                } else {
                    noteViewHolder.status.setText(firebasemodel.getStatus());
                }

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), specfiqchat.class);
                        intent.putExtra("name", firebasemodel.getName());
                        intent.putExtra("receiveruid", firebasemodel.getid());
                        intent.putExtra("imageuri", firebasemodel.getimg());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @NonNull
            @Override
            public Firebasemodel getItem(int position) {
                return super.getItem(position);
            }

            @Override
            public NoteViewHolder onCreateViewHolder(ViewGroup group, int Viewtype) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item, group, false);

                return new NoteViewHolder(view);
            }

        };
        mrecyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerView.setLayoutManager(linearLayoutManager);
        mrecyclerView.setAdapter(chatAdapter);

       // updatetoken(FirebaseInstanceId.getInstance().getToken());
        return v;
    }

   /* private void updatetoken (String refreshtoken){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token =new Token(refreshtoken);
        reference.child(firebaseAuth.getCurrentUser().getUid()).setValue(token);

    }*/
    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView name, status;
        private ImageView imageView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.namect);
            status = itemView.findViewById(R.id.status);
            imageView = itemView.findViewById(R.id.imgv1);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter!=null)
        {
            chatAdapter.stopListening();
        }
    }
}