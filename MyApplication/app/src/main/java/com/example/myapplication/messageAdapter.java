package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<message> msgArrayList;

    public messageAdapter(Context context ,ArrayList<message> msgArrayList) {
        this.msgArrayList = msgArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      if (viewType==1){
          View view = LayoutInflater.from(context).inflate(R.layout.sendmesg,parent,false);
          return new senderviewholder(view);
      }
      else{ View view = LayoutInflater.from(context).inflate(R.layout.receivemsg,parent,false);
          return new receiviewholder(view);
    }}

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      message msg = msgArrayList.get(position);
      if(holder.getClass()==senderviewholder.class){
          senderviewholder viewholder = (senderviewholder) holder;
          viewholder.txtmsg.setText(msg.getMsg());
          viewholder.timemsg.setText(msg.getCurenttime());
      }
      else{
          receiviewholder viewholder = (receiviewholder) holder;
          viewholder.txtmsg.setText(msg.getMsg());
          viewholder.timemsg.setText(msg.getCurenttime());

      }


    }
    @Override
    public int getItemViewType(int position){
        message msg =msgArrayList.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(msg.getSenderid())){
           //sender
            return 1;
        }
        else {//receiver
            return 2;}
    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "size " + msgArrayList.size());
        return msgArrayList.size();
    }
    class senderviewholder extends RecyclerView.ViewHolder{
         TextView txtmsg,timemsg;
        public senderviewholder(@NonNull View itemView) {
            super(itemView);
            txtmsg= itemView.findViewById(R.id.sendermessage);
            timemsg= itemView.findViewById(R.id.timeofmessage);
        }}
        class receiviewholder extends RecyclerView.ViewHolder{
            TextView txtmsg,timemsg;
            public receiviewholder(@NonNull View itemView) {
                super(itemView);
                txtmsg= itemView.findViewById(R.id.sendermessage);
                timemsg= itemView.findViewById(R.id.timeofmessage);
            }}

}
