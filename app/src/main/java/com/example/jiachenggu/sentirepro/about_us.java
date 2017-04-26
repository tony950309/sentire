package com.example.jiachenggu.sentirepro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class about_us extends AppCompatActivity {
    TextView tvus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setTitle("                                 About Us");
        tvus = (TextView) findViewById(R.id.tvus);
        tvus.setText("Developed by :" + "\n" +
                    "Jiacheng Gu" + "\n" + "\n" +
                    "Supervised by:" + "\n" +
                    "Dr. Philip Hands" + "\n" + "\n" +
                    "Acknowledgement: " + "\n" +
                    "Qisong Wang" + "\n" +
                    "Yang Guo" + "\n" +
                    "Qin Wang" + "\n" + "\n" +
                    "If you have any question or advice, please send me an e-mail:" +"\n" +"\n" +
                    "<s1573210@sms.ed.ac.uk>, or <1850612322@qq.com>");

    }
}
