package com.example.chart_0405;

import static android.media.CamcorderProfile.get;
import static com.example.chart_0405.DetailFragment.dDB;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context mContext;
    List<Detail> mData;
    Dialog myDailog;
    String out;

    public MyAdapter(Context mcontext,List<Detail> mData){
        this.mContext = mContext;
        this.mData = mData;
        notifyDataSetChanged();
    }



    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        //Dialog初始化
        myDailog = new Dialog(parent.getContext());
        myDailog.setContentView(R.layout.dialog_detail);
        myDailog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        vHolder.item_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out = mData.get(vHolder.getAdapterPosition()).getStatus();
                Switch swBelow = (Switch) myDailog.findViewById(R.id.switch_Below);
                EditText edName = (EditText) myDailog.findViewById(R.id.di_edName);
                EditText edType = (EditText) myDailog.findViewById(R.id.di_edType);
                EditText edFee = (EditText) myDailog.findViewById(R.id.di_edFee);
                Button modify = (Button) myDailog.findViewById(R.id.di_mod);
                edName.setText(mData.get(vHolder.getAdapterPosition()).getName());
                edType.setText(mData.get(vHolder.getAdapterPosition()).getType());
                edFee.setText(mData.get(vHolder.getAdapterPosition()).getFee());
                if(out.equals("in")){
                    swBelow.setChecked(true);
                }else {
                    swBelow.setChecked(false);
                }
                //Toast.makeText(parent.getContext(), "Test Click" + String.valueOf(vHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                swBelow.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked){
                        out = "in";
                    }else {
                        out = "out";
                    }
                });
                myDailog.show();
                modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Id = mData.get(vHolder.getAdapterPosition()).getId();
                        String Date = mData.get(vHolder.getAdapterPosition()).getDate();
                        dDB.modify(Id,Date,edName.getText().toString(),edType.getText().toString(),edFee.getText().toString(),out);
                        Toast.makeText(parent.getContext(),"修改成功!", Toast.LENGTH_SHORT).show();
                        Log.i("adapter","dDB : " + dDB.searchByDate(Date));
                        mData.set(vHolder.getAdapterPosition(),new Detail(edName.getText().toString(),edType.getText().toString(),edFee.getText().toString(),out));
                        notifyDataSetChanged();
                        dDB.close();
                        myDailog.dismiss();//修改完關閉dialog
                    }
                });
            }
        });

        vHolder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDB.deleteByIdEZ(mData.get(vHolder.getAdapterPosition()).getId());
                mData.remove(vHolder.getAdapterPosition());
                notifyItemRemoved(vHolder.getAdapterPosition());
                dDB.close();
                Log.i("adapter","dDB : " + dDB.showAll());
            }
        });

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_fee.setText(mData.get(position).getFee());
    }

    @Override
    public int getItemCount() {
        return  mData == null ? 0 : mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView tv_fee;
        private Button btn_del;
        private LinearLayout item_detail;

        public MyViewHolder(View itemView){
            super(itemView);
            item_detail = (LinearLayout) itemView.findViewById(R.id.item_detail);
            tv_name = (TextView) itemView.findViewById(R.id.name);
            tv_fee = (TextView) itemView.findViewById(R.id.fee);
            btn_del = (Button) itemView.findViewById(R.id.del);
        }
    }
}
