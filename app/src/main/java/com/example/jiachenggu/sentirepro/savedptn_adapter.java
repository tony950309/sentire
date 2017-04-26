package com.example.jiachenggu.sentirepro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

/**
 * Created by Jiacheng Gu on 2017/4/7.
 */

public class savedptn_adapter extends BaseAdapter {
    private List<savedptn_class> objects;
    public savedptn_adapter(List<savedptn_class> objects)
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
        final savedptn_class ptnclass = objects.get(position);
        ViewHolder holder=null;
        if(converView==null)
        {
            holder=new ViewHolder();
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.savedptnlist, null);
            holder.tvname = (TextView) view.findViewById(R.id.tvptnname);
            holder.tvemot = (TextView) view.findViewById(R.id.tvptnemot);
//            holder.tvcode = (TextView) view.findViewById(R.id.tvptncode);
            holder.ivbtPlay = (ImageView) view.findViewById(R.id.ivplay);
            holder.tvtimes = (TextView) view.findViewById(R.id.tvptntimes);
            holder.tvhap = (TextView) view.findViewById(R.id.tvscorehap);
            holder.tvsad = (TextView) view.findViewById(R.id.tvscoresad);
            holder.tvang = (TextView) view.findViewById(R.id.tvscoreang);
            holder.tvcon = (TextView) view.findViewById(R.id.tvscorecon);
            holder.tvsup = (TextView) view.findViewById(R.id.tvscoresup);
            holder.tvdis = (TextView) view.findViewById(R.id.tvscoredis);
            holder.tvfea = (TextView) view.findViewById(R.id.tvscorefea);
            holder.tvnod = (TextView) view.findViewById(R.id.tvscorenod);
            holder.tvsha = (TextView) view.findViewById(R.id.tvscoresha);

            converView=view;
            converView.setTag(holder);
        }else
        {
            holder= (ViewHolder) converView.getTag();
        }
        holder.tvname.setText(ptnclass.getPtnname());
        holder.tvemot.setText(ptnclass.getPtnemot());
        holder.tvtimes.setText(""+ptnclass.getPtntimes());
        holder.tvhap.setText(""+ptnclass.getScorehap());
        holder.tvsad.setText(""+ptnclass.getScoresad());
        holder.tvang.setText(""+ptnclass.getScoreang());
        holder.tvcon.setText(""+ptnclass.getScorecon());
        holder.tvsup.setText(""+ptnclass.getScoresup());
        holder.tvdis.setText(""+ptnclass.getScoredis());
        holder.tvfea.setText(""+ptnclass.getScorefea());
        holder.tvnod.setText(""+ptnclass.getScorenod());
        holder.tvsha.setText(""+ptnclass.getScoresha());


        holder.ivbtPlay.setImageResource(ptnclass.getImgid());
        holder.ivbtPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BTsend(ptnclass.getPtncode());
                Toast.makeText(parent.getContext(),"code : "+ptnclass.getPtncode(),Toast.LENGTH_SHORT).show();
//                BTsend(ptnclass.getPtncode());
            }
        });
        return converView;
    }
    private class ViewHolder
    {
        TextView tvname,tvtype,tvemot,tvcode,tvtimes,tvhap,tvsad,
                tvang,tvcon,tvsup,tvdis,tvfea,tvnod,tvsha;
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