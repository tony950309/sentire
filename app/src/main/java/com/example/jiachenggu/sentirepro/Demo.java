package com.example.jiachenggu.sentirepro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class Demo extends AppCompatActivity implements View.OnClickListener {
    Button btnhap,btnsad,btnang,btncon,btnsup,btndis,btnfea,btnnod,btnsha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        setTitle("                                  Demo");

        btnhap      = (Button)   findViewById(R.id.btndemohap);
        btnsad      = (Button)   findViewById(R.id.btndemosad);
        btnang      = (Button)   findViewById(R.id.btndemoang);
        btncon      = (Button)   findViewById(R.id.btndemocon);
        btnsup      = (Button)   findViewById(R.id.btndemosup);
        btndis      = (Button)   findViewById(R.id.btndemodis);
        btnfea      = (Button)   findViewById(R.id.btndemofea);
        btnnod      = (Button)   findViewById(R.id.btndemonod);
        btnsha      = (Button)   findViewById(R.id.btndemosha);

        btnhap.setOnClickListener(this);
        btnsad.setOnClickListener(this);
        btnang.setOnClickListener(this);
        btncon.setOnClickListener(this);
        btnsup.setOnClickListener(this);
        btndis.setOnClickListener(this);
        btnfea.setOnClickListener(this);
        btnnod.setOnClickListener(this);
        btnsha.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btndemohap:
                msg("Happy");
                BTsend("DHA");
                break;

            case R.id.btndemosad:
                msg("Sad");
                BTsend("DSA");
                break;

            case R.id.btndemoang:
                msg("Angry");
                BTsend("DAN");
                break;

            case R.id.btndemocon:
                msg("Contempt");
                BTsend("DCO");
                break;

            case R.id.btndemosup:
                msg("Surprised");
                BTsend("DSU");
                break;

            case R.id.btndemodis:
                msg("Disgust");
                BTsend("DDI");
                break;

            case R.id.btndemofea:
                msg("Fear");
                BTsend("DFE");
                break;

            case R.id.btndemonod:
                msg("Nod");
                BTsend("DNO");
                break;

            case R.id.btndemosha:
                msg("Shake");
                BTsend("DSH");
                break;
        }
    }
    private void BTsend(String data){

        if (BTdevicelist.btSocket!=null) //If the btSocket is busy
        {
            try
            {
                BTdevicelist.btSocket.getOutputStream().write(data.toString().getBytes());
            }
            catch (IOException e)
            { msg("Error");}
        }
    }

    public void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
