package com.example.jiachenggu.sentirepro;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jiacheng Gu on 2017/4/8.
 */

public class LVCgvadapter extends ArrayAdapter {
    Context context;

    public LVCgvadapter(Context context) {
        super(context, 0);
        this.context = context;

    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;

        //if (row == null)
        //{
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(R.layout.lvc_grid_view, parent, false);


        TextView textViewTitle = (TextView) row.findViewById(R.id.textViewlv);
        ImageView imageViewIte = (ImageView) row.findViewById(R.id.imageView);


        textViewTitle.setText(mEffectDescriptions[position]);
        imageViewIte.setImageResource(mThumbIds[position]);

        //}

        return row;

    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.click0, R.drawable.click0, R.drawable.click0,
            R.drawable.click0, R.drawable.click0, R.drawable.click0,
            R.drawable.bump, R.drawable.bump, R.drawable.bump,
            R.drawable.click1, R.drawable.click1, R.drawable.click2,
            R.drawable.fuzz, R.drawable.buzz0,
            R.drawable.alert, R.drawable.alert,
            R.drawable.click0, R.drawable.click0, R.drawable.click0, R.drawable.click0,
            R.drawable.click0, R.drawable.click0, R.drawable.click0,
            R.drawable.tick, R.drawable.tick, R.drawable.tick,
            R.drawable.click1, R.drawable.click1, R.drawable.click1, R.drawable.click1,
            R.drawable.click1, R.drawable.click1, R.drawable.click1,
            R.drawable.tick, R.drawable.tick, R.drawable.tick,
            R.drawable.click1, R.drawable.click1, R.drawable.click1, R.drawable.click1,
            R.drawable.click1, R.drawable.click1, R.drawable.click1,
            R.drawable.tick, R.drawable.tick, R.drawable.tick,
            R.drawable.buzz0, R.drawable.buzz0, R.drawable.buzz0, R.drawable.buzz0, R.drawable.buzz0,
            R.drawable.pulse, R.drawable.pulse, R.drawable.pulse, R.drawable.pulse, R.drawable.pulse, R.drawable.pulse,
            R.drawable.click3, R.drawable.click3, R.drawable.click3,
            R.drawable.click3, R.drawable.click3, R.drawable.click3,
            R.drawable.hum, R.drawable.hum, R.drawable.hum,
            R.drawable.hum, R.drawable.hum, R.drawable.hum,
            R.drawable.ramp_down_full, R.drawable.ramp_down_full,
            R.drawable.ramp_down_full, R.drawable.ramp_down_full,
            R.drawable.ramp_down_full, R.drawable.ramp_down_full,
            R.drawable.ramp_down_full, R.drawable.ramp_down_full,
            R.drawable.ramp_down_full, R.drawable.ramp_down_full,
            R.drawable.ramp_down_full, R.drawable.ramp_down_full,
            R.drawable.ramp_up_full, R.drawable.ramp_up_full,
            R.drawable.ramp_up_full, R.drawable.ramp_up_full,
            R.drawable.ramp_up_full, R.drawable.ramp_up_full,
            R.drawable.ramp_up_full, R.drawable.ramp_up_full,
            R.drawable.ramp_up_full, R.drawable.ramp_up_full,
            R.drawable.ramp_up_full, R.drawable.ramp_up_full,
            R.drawable.ramp_down_half, R.drawable.ramp_down_half,
            R.drawable.ramp_down_half, R.drawable.ramp_down_half,
            R.drawable.ramp_down_half, R.drawable.ramp_down_half,
            R.drawable.ramp_down_half, R.drawable.ramp_down_half,
            R.drawable.ramp_down_half, R.drawable.ramp_down_half,
            R.drawable.ramp_down_half, R.drawable.ramp_down_half,
            R.drawable.ramp_up_half, R.drawable.ramp_up_half,
            R.drawable.ramp_up_half, R.drawable.ramp_up_half,
            R.drawable.ramp_up_half, R.drawable.ramp_up_half,
            R.drawable.ramp_up_half, R.drawable.ramp_up_half,
            R.drawable.ramp_up_half, R.drawable.ramp_up_half,
            R.drawable.ramp_up_half, R.drawable.ramp_up_half,
            R.drawable.buzz0,
            R.drawable.hum,
            R.drawable.hum,
            R.drawable.hum,
            R.drawable.hum,
            R.drawable.hum
    };

    private String[] mEffectDescriptions = {
            "Strong, 100%", "Strong, 60%", "Strong, 30%",
            "Sharp, 100%", "Sharp, 60%", "Sharp, 30%",
            "Soft, 100%", "Soft, 60%", "Soft, 30%",
            "Double, 100%", "Double, 60%", "Triple, 100%",
            "Soft, 60%", "Strong, 100%",
            "750ms, 100%", "1000ms, 100%",
            "Strong, 100%", "Strong, 80%", "Strong, 60%", "Strong, 30%",
            "Medium, 100%", "Medium, 80%", "Medium, 60%",
            "Sharp, 100%", "Sharp, 80%", "Sharp, 60%",
            "Short Double Strong, 100%", "Short Double Strong, 80%",
            "Short Double Strong, 60%", "Short Double Strong, 30%",
            "Short Double Medium, 100%", "Short Double Medium, 80%", "Short Double Medium, 60%",
            "Short Double Sharp, 100%", "Short Double Sharp, 80%", "Short Double Sharp, 60%",
            "Long Double Strong1 100%", "Long Double Strong, 80%",
            "Long Double Strong3 60%", "Long Double Strong, 30%",
            "Long Double Medium, 100%", "Long Double Medium, 80%", "Long Double Medium, 60%",
            "Long Double Sharp, 100%", "Long Double Sharp, 80%", "Long Double Sharp, 60%",
            "100%", "80%", "60%", "40%", "20%",
            "Strong 1, 100%", "Strong 2, 60%",
            "Medium 1, 100%", "Medium 2, 60%",
            "Sharp 1, 100%", "Sharp 2, 60%",
            "Transition 1, 100%", "Transition 2, 80%", "Transition 3, 60%",
            "Transition 4, 40%", "Transition 5, 20%", "Transition 1, 10%",
            "100%", "80%", "60%",
            "40%", "20%", "10%",
            "Long & Smooth 1", "Long & Smooth 2",
            "Medium & Smooth 1", "Medium & Smooth 2",
            "Short & Smooth 1", "Short & Smooth 2",
            "Long & Sharp 1", "Long & Sharp 2",
            "Sharp 1", "Medium & Sharp 2",
            "Short & Sharp 1", "Short & Sharp 2",
            "Long & Smooth 1", "Long & Smooth 2",
            "Medium & Smooth 1", "Medium & Smooth 2",
            "Short & Smooth 1", "Short & Smooth 2",
            "Long & Sharp 1", "Long & Sharp 2",
            "Medium & Sharp1", "Medium & Sharp 2",
            "Short & Sharp 1", "Short & Sharp 2",
            "Long & Smooth 1", "Long & Smooth 2",
            "Medium & Smooth 1", "Medium & Smooth 2",
            "Short & Smooth 1%", "Short & Smooth 2",
            "Long & Sharp 1", "Long & Sharp 2",
            "Sharp 1", "Medium & Sharp 2",
            "Short & Sharp 1", "Short & Sharp 2",
            "Long & Smooth 1", "Long & Smooth 2",
            "Medium & Smooth 1", "Medium & Smooth 2",
            "Short & Smooth 1", "Short & Smooth 2",
            "Long & Sharp 1", "Long & Sharp 2",
            "Medium & Sharp1", "Medium & Sharp 2",
            "Short & Sharp 1", "Short & Sharp 2",
            "Long, 100%",
            "Smooth, 50%",
            "Smooth, 40%",
            "Smooth, 30%",
            "Smooth, 20%",
            "Smooth, 10%",
    };
}