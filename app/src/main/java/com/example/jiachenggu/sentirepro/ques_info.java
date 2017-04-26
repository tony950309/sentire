package com.example.jiachenggu.sentirepro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ques_info extends AppCompatActivity implements View.OnClickListener {

    Button btnsave,btnskip;
    EditText etnation,etage,etgender,etdisab;
    public static String quesNationality,quesAge,quesGender,quesDisability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques_info);
        setTitle("                           Personal Details(Optional)");

        btnsave = (Button) findViewById(R.id.btnquesinfosave);
        btnskip = (Button) findViewById(R.id.btnquesinfoskip);
        etnation= (EditText) findViewById(R.id.etquesinfoNation);
        etage   = (EditText) findViewById(R.id.etquesinfoage);
        etgender= (EditText) findViewById(R.id.etquesinfogender);
        etdisab = (EditText) findViewById(R.id.etquesinfodisab);


        btnsave.setOnClickListener(this);
        btnskip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnquesinfosave:
                quesNationality = etnation.getText().toString();
                quesAge         = etage.getText().toString();
                quesGender      = etgender.getText().toString();
                quesDisability  = etdisab.getText().toString();
                Intent i = new Intent(ques_info.this, ques_vib.class);
                startActivity(i);
                break;
            case R.id.btnquesinfoskip:
                quesNationality = "";
                quesAge         = "";
                quesGender      = "";
                quesDisability  = "";
                i = new Intent(ques_info.this, ques_vib.class);
                startActivity(i);
                break;

        }

    }
}
