package com.example.jiachenggu.sentirepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by Jiacheng Gu on 2017/4/13.
 */

public class lvseeq_adapter extends BaseAdapter {
    private List<lvseeq_class> objects;
    public lvseeq_adapter(List<lvseeq_class> objects)
    {
        this.objects=objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View converView, final ViewGroup parent){
        final lvseeq_class myclass = objects.get(position);
        ViewHolder holder=null;
        if(converView==null)
        {
            holder=new ViewHolder();
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lvseeq, null);
            holder.tvtype = (TextView) view.findViewById(R.id.tvseeqtype);
            holder.tvemot = (TextView) view.findViewById(R.id.tvseeqemot);
            holder.tvcode = (TextView) view.findViewById(R.id.tvseeqcode);
            holder.ivbtPlay = (ImageView) view.findViewById(R.id.imbtnseeqplay);


            converView=view;
            converView.setTag(holder);
        }else
        {
            holder= (ViewHolder) converView.getTag();
        }
        holder.tvtype.setText(myclass.getPtntype());
        holder.tvemot.setText(myclass.getPtnemot());
        holder.tvcode.setText(myclass.getPtncode());
        holder.ivbtPlay.setImageResource(myclass.getImgid());
        holder.ivbtPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BTsend(myclass.getPtncode());
                Toast.makeText(parent.getContext(),"code : "+myclass.getPtncode(),Toast.LENGTH_SHORT).show();
//                BTsend(ptnclass.getPtncode());
            }
        });
        return converView;
    }
    private class ViewHolder
    {
        TextView tvtype,tvemot,tvcode;
        ImageView ivbtPlay;
    }


    private void BTsend(String data_to_send)
    {
        if (BTdevicelist.btSocket!=null) //If the btSocket is busy
        {
            try
            {BTdevicelist.btSocket.getOutputStream().write(data_to_send.getBytes());}
            catch (IOException e) { }
        }
    }

}