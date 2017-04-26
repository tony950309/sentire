package com.example.jiachenggu.sentirepro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Homepage extends AppCompatActivity implements View.OnClickListener {
    private Dialog dialog;
    public EditText e1 ;
    public String password = "0620";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        setTitle("                                       Home Page");
//        btn1 = (Button)findViewById(R.id.button);
//        btn2 = (Button)findViewById(R.id.button2);
//        btn3 = (Button)findViewById(R.id.button3);
//
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (BTdevicelist.btSocket!=null) //If the btSocket is busy
//                {
//                    try
//                    {BTdevicelist.btSocket.close();}
//                    catch (IOException e)
//                    { msg("Error");}
//                    BTdevicelist.isBtConnected = false;
//                }
//            }
//        });
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (BTdevicelist.btSocket!=null)
//                {
//                    try
//                    {
//                        BTdevicelist.btSocket.getOutputStream().write("TF".toString().getBytes());
//                    }
//                    catch (IOException e)
//                    {
//                        msg("Error");
//                    }
//                }
//            }
//        });
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (BTdevicelist.btSocket!=null)
//                {
//                    try
//                    {
//                        BTdevicelist.btSocket.getOutputStream().write("TO".toString().getBytes());
//                    }
//                    catch (IOException e)
//                    {
//                        msg("Error");
//                    }
//                }
//            }
//        });b
//        imbtnhelp  = (ImageButton)findViewById(R.id.imbtnHPhelp);
//        btnsetting = (Button)findViewById(R.id.btnHPwristband);
//        btnlivvib  = (Button)findViewById(R.id.btnHPlibvib);
//        btnrtvib   = (Button)findViewById(R.id.btnHPRTvib);
//        btncol     = (Button)findViewById(R.id.btnHPcol);
//        btnques    = (Button)findViewById(R.id.btnHPques);
//        btnseeq    = (Button)findViewById(R.id.btnHPseeq);
//        btnaboutus = (Button)findViewById(R.id.btnHPaboutus);
//        btndisc    = (Button)findViewById(R.id.btnHPdisc);

        findViewById(R.id.imbtnHPhelp).setOnClickListener(this);
        findViewById(R.id.btnHPwristband).setOnClickListener(this);
        findViewById(R.id.btnHPlibvib).setOnClickListener(this);
        findViewById(R.id.btnHPRTvib).setOnClickListener(this);
        findViewById(R.id.btnHPcol).setOnClickListener(this);
        findViewById(R.id.btnHPques).setOnClickListener(this);
        findViewById(R.id.btnHPseeq).setOnClickListener(this);
        findViewById(R.id.btnHPaboutus).setOnClickListener(this);
        findViewById(R.id.btnHPreco).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {       //this onclick method allows the app goes to different activities
        switch(v.getId()){
            case R.id.imbtnHPhelp:
                AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);
                builder.setTitle("Help"); //title
                builder.setMessage( "WRISTBAND SETTINGS" + "\n" +
                                    "Upload your designed pattern data to the wristband," +
                                    " and emulate the computer to send command to the wristband" + "\n" + "\n" +

                                    "LIBRARY VIBRATION PATTERNS" + "\n" +
                                    "Design your own vibration patterns based on DRV2605'library, " +
                                    "you can combine different library patterns in any order" + "\n" + "\n" +

                                    "REAl-TIME VIBRATION PATTERNS" + "\n" +
                                    "Design your own vibration patterns based on the curve you sketched" + "\n" + "\n" +

                                    "COLOUR PATTERNS" + "\n" +
                                    "Design your own colour patterns that display on the OLED screen on the wristband" + "\n" + "\n" +

                                    "QUESTIONNAIRE" + "\n" +
                                    "You should choose the emotion that matches the displayed pattern, " +
                                    "and compare the displayed patterns" + "\n" + "\n" +

                                    "SEE QUESTIONNAIRE DATA(DEVELOPER ONLY)" + "\n" +
                                    "Skim all the questionnaire data. (PS: developer only)" + "\n" + "\n" +

                                    "ABOUT US" + "\n" +
                                    "If you have any question or suggestions, please contact us. " +
                                    "Acknowledgement and copyright" + "\n" + "\n" +
                                    "If the bluetooth connection is broken when playing vibration pattern, " +
                                    "it is likely that the battery is running out, please charge the wristband!");
                builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.btnHPwristband:
                Intent i = new Intent(Homepage.this, Settings.class);
                startActivity(i);
                break;
            case R.id.btnHPlibvib:
                i = new Intent(Homepage.this, libvibptn.class);
                startActivity(i);
                break;
            case R.id.btnHPRTvib:
                i = new Intent(Homepage.this, rtvibptn.class);
                startActivity(i);
                break;
            case R.id.btnHPcol:
                i = new Intent(Homepage.this, colptn.class);
                startActivity(i);
                break;
            case R.id.btnHPques:
                i = new Intent(Homepage.this, ques_info.class);
                startActivity(i);
                break;
            /****************************/
            case R.id.btnHPseeq:

                dialog = new Dialog(this);
                View view = LayoutInflater.from(this).inflate(R.layout.hp_seeq_pw,null);
                //set onclick listener for btn in dialog
                view.findViewById(R.id.btndialogcanc).setOnClickListener(this);
                view.findViewById(R.id.btndialogconf).setOnClickListener(this);
                e1 = (EditText)view.findViewById(R.id.etdialogpw); /********view. is important otherwise it would collapse***********//****************************//****************************//****************************/
                dialog.setTitle("Developer Authentiation");
                dialog.setContentView(view);

                dialog.show();
                break;
            case R.id.btndialogcanc:
                dialog.dismiss();
                break;
            case R.id.btndialogconf:
                if( password.equals( e1.getText().toString() ) ){     //use equals to find whether two string value are the same
                    i = new Intent(Homepage.this, seeq.class);
                    startActivity(i);
                }
                else {msg("The password is wrong!");}
                dialog.dismiss();
                break;
            /****************************/

            case R.id.btnHPaboutus:
                i = new Intent(Homepage.this, about_us.class);
                startActivity(i);
                break;
            case R.id.btnHPreco:
                BTdevicelist.isBtConnected = false;
                i = new Intent(Homepage.this, BTdevicelist.class);
                startActivity(i);
                finish();

                break;
        }
    }
     public void msg(String s) {
         Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
     }
}
