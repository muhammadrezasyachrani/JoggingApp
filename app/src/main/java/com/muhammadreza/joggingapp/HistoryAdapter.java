package com.muhammadreza.joggingapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    SessionHistory myContext;
    ArrayList<sessionClass> alSessionHistoty;

    public HistoryAdapter(SessionHistory myContext, ArrayList<sessionClass> alSessionHistoty)
    {
        this.myContext=myContext;
        this.alSessionHistoty=alSessionHistoty;
    }

    // Define ur own View Holder (Refers to Single Row)
    class MyViewHolder extends RecyclerView.ViewHolder {
        View customview;

        public MyViewHolder(View itemView) {
            super(itemView);
            customview = itemView;
        }
    }
    // Inflate ur Single Row / CardView from XML here

    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View singleview = inflater.inflate(R.layout.layoutforhistory, parent, false);


        return new MyViewHolder(singleview);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, final int position) {

        View localcardview = holder.itemView;
        TextView tv1, sessionnametv,totaltimetv,caloriestv;
        ImageButton deletesessionimgbtn;
        sessionnametv = (TextView) (localcardview.findViewById(R.id.sessionnametv));
        totaltimetv = (TextView) (localcardview.findViewById(R.id.totaltimetv));
        caloriestv = (TextView) (localcardview.findViewById(R.id.caloriestv));
        deletesessionimgbtn = (ImageButton) (localcardview.findViewById(R.id.deletesessionimgbtn));


        final sessionClass obj = alSessionHistoty.get(position);
        sessionnametv.setText(""+obj.getSessionname());
        totaltimetv.setText(obj.getTotaltime()+"");


        localcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(myContext,SessionHistoryDetail.class);
                in.putExtra("sessionid",obj.getSessionid()+"");
                in.putExtra("sessionname",obj.getSessionname()+"");
                in.putExtra("totaltime",obj.getTotaltime()+"");
                in.putExtra("date",obj.getDate()+"");
                in.putExtra("distance",obj.getKilometers()+"");

                myContext.startActivity(in);

            }
        });

        deletesessionimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myContext.deleteSession(obj.getSessionid()+"");

            }
        });

    }

    @Override
    public int getItemCount() {

        return alSessionHistoty.size();
    }
}