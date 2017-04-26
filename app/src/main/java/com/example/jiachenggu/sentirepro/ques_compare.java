package com.example.jiachenggu.sentirepro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ques_compare extends AppCompatActivity implements View.OnClickListener {
    TextView tvmessage,tvnumb;
    Button btnfinish,btnswitch;
    ImageButton imbtnnext,imbtnptn1,imbtnptn2;
    CheckBox cbptn1,cbptn2;
    int emotnumb,ptnnumb1,ptnnumb2,flagvib;
    boolean is1LV,is2LV,iscolor;
    String curcode;
    String[] dialogemot = new String[]{"Happy","Sad","Angry","Contempt","Surprised",
            "Disgust","Fear","Nod","Shake"};
    /*************************Library vibration pattern************/
    int ptn_numbLV = 0;
    String[] myptn_nameLV = new String[999],myptn_typeLV = new String[999],
            myptn_emotLV = new String[999],myptn_codeLV = new String[999];
    int[] myptn_numbLV = new int[999],myptn_timesLV = new int[999],
            shappyLV = new int[999],ssadLV = new int[999],sangryLV = new int[999],
            scontemptLV = new int[999],ssupriseLV = new int[999],sdisgustLV = new int[999],
            sfearLV = new int[999],snodLV = new int[999],sshakeLV = new int[999];

    int existingPtnNumbLV = 0;

    private void writeLVExcel() {
        try {
            //  open the file
            File file=new File(Environment.getExternalStorageDirectory(),"LVptn.xls");
            //  create the sheet ,name it
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("My library vibration patterns", 0);

            sheet.mergeCells(0,0,2,0);//  merge the first 3 cell on row 1 and cell 4,5
            sheet.mergeCells(3,0,4,0);//    first 3:column  first 0: row
            sheet.mergeCells(6,0,14,0);
            //  row 1: total number of current ptn
            Label mystring = new Label(0, 0, "Total pattern number :");
            sheet.addCell(mystring);
            jxl.write.Number mynumb = new jxl.write.Number(3, 0, ptn_numbLV);
            sheet.addCell(mynumb);
            mystring = new Label(6, 0, "Scores for different emotions(scores comes from questionnaire)");
            sheet.addCell(mystring);

            //  labels for different data
            mystring = new Label(0, 1, "Pattern Number");
            sheet.addCell(mystring);
            mystring = new Label(1, 1, "Pattern Name");
            sheet.addCell(mystring);
            mystring = new Label(2, 1, "Pattern Type");
            sheet.addCell(mystring);
            mystring = new Label(3, 1, "Pattern Emotion");
            sheet.addCell(mystring);
            mystring = new Label(4, 1, "Pattern Code");
            sheet.addCell(mystring);
            mystring = new Label(5, 1, "Testing Times");
            sheet.addCell(mystring);


            //  write data

            for (int row = 2; row <= ptn_numbLV + 1; row++)
            {
                mynumb = new jxl.write.Number(0, row, myptn_numbLV[ row - 2 ]);
                sheet.addCell(mynumb);
                mystring = new Label( 1,row, myptn_nameLV[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 2,row, myptn_typeLV[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 3,row, myptn_emotLV[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 4,row, myptn_codeLV[ row - 2 ]);
                sheet.addCell(mystring);
                mynumb = new jxl.write.Number(5, row, myptn_timesLV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(6, row, shappyLV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(7, row, ssadLV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(8, row, sangryLV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(9, row, scontemptLV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(10, row, ssupriseLV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(11, row, sdisgustLV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(12, row, sfearLV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(13, row, snodLV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(14, row, sshakeLV[ row - 2 ]);
                sheet.addCell(mynumb);

            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readLVExcel(){
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"LVptn.xls");
            Workbook workbook = Workbook.getWorkbook(file);
            //
            Sheet sheet = workbook.getSheet(0);
            //  get total number of existing ptn numbs
            Cell cell = sheet.getCell(3, 0);
            existingPtnNumbLV = Integer.valueOf(cell.getContents()).intValue();

            for (int row = 2; row <= existingPtnNumbLV + 1; row++)
            {
                cell = sheet.getCell(0,row);
                myptn_numbLV[row - 2] = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(1,row);
                myptn_nameLV[row - 2] = cell.getContents();
                cell = sheet.getCell(2,row);
                myptn_typeLV[row - 2] = cell.getContents();
                cell = sheet.getCell(3,row);
                myptn_emotLV[row - 2] = cell.getContents();
                cell = sheet.getCell(4,row);
                myptn_codeLV[row - 2] = cell.getContents();
                /*****      Scores ***************/
                cell = sheet.getCell(5,row);
                myptn_timesLV[row - 2] = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(6,row);
                shappyLV[row - 2]      = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(7,row);
                ssadLV[row - 2]        = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(8,row);
                sangryLV[row - 2]      = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(9,row);
                scontemptLV[row - 2]   = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(10,row);
                ssupriseLV[row - 2]    = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(11,row);
                sdisgustLV[row - 2]    = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(12,row);
                sfearLV[row - 2]       = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(13,row);
                snodLV[row - 2]        = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(14,row);
                sshakeLV[row - 2]      = Integer.valueOf(cell.getContents()).intValue();

            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }
    /**************************************************************/

    /*************************Real-time vibration pattern************/
    int ptn_numbRTV = 0;
    String[] myptn_nameRTV = new String[999],myptn_typeRTV = new String[999],
            myptn_emotRTV = new String[999],myptn_codeRTV = new String[999];
    int[] myptn_numbRTV = new int[999],myptn_timesRTV = new int[999],
            shappyRTV = new int[999],ssadRTV = new int[999],sangryRTV = new int[999],
            scontemptRTV = new int[999],ssupriseRTV = new int[999],sdisgustRTV = new int[999],
            sfearRTV = new int[999],snodRTV = new int[999],sshakeRTV = new int[999];

    int existingPtnNumbRTV = 0;

    private void writeRTVExcel() {
        try {
            //  open the file
            File file=new File(Environment.getExternalStorageDirectory(),"RTVptn.xls");
            //  create the sheet ,name it
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("My library vibration patterns", 0);

            sheet.mergeCells(0,0,2,0);//  merge the first 3 cell on row 1 and cell 4,5
            sheet.mergeCells(3,0,4,0);//    first 3:column  first 0: row
            sheet.mergeCells(6,0,14,0);
            //  row 1: total number of current ptn
            Label mystring = new Label(0, 0, "Total pattern number :");
            sheet.addCell(mystring);
            jxl.write.Number mynumb = new jxl.write.Number(3, 0, ptn_numbRTV);
            sheet.addCell(mynumb);
            mystring = new Label(6, 0, "Scores for different emotions(scores comes from questionnaire)");
            sheet.addCell(mystring);

            //  labels for different data
            mystring = new Label(0, 1, "Pattern Number");
            sheet.addCell(mystring);
            mystring = new Label(1, 1, "Pattern Name");
            sheet.addCell(mystring);
            mystring = new Label(2, 1, "Pattern Type");
            sheet.addCell(mystring);
            mystring = new Label(3, 1, "Pattern Emotion");
            sheet.addCell(mystring);
            mystring = new Label(4, 1, "Pattern Code");
            sheet.addCell(mystring);
            mystring = new Label(5, 1, "Testing Times");
            sheet.addCell(mystring);


            //  write data

            for (int row = 2; row <= ptn_numbRTV + 1; row++)
            {
                mynumb = new jxl.write.Number(0, row, myptn_numbRTV[ row - 2 ]);
                sheet.addCell(mynumb);
                mystring = new Label( 1,row, myptn_nameRTV[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 2,row, myptn_typeRTV[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 3,row, myptn_emotRTV[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 4,row, myptn_codeRTV[ row - 2 ]);
                sheet.addCell(mystring);
                mynumb = new jxl.write.Number(5, row, myptn_timesRTV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(6, row, shappyRTV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(7, row, ssadRTV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(8, row, sangryRTV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(9, row, scontemptRTV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(10, row, ssupriseRTV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(11, row, sdisgustRTV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(12, row, sfearRTV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(13, row, snodRTV[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(14, row, sshakeRTV[ row - 2 ]);
                sheet.addCell(mynumb);

            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readRTVExcel(){
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"RTVptn.xls");
            Workbook workbook = Workbook.getWorkbook(file);
            //
            Sheet sheet = workbook.getSheet(0);
            //  get total number of existing ptn numbs
            Cell cell = sheet.getCell(3, 0);
            existingPtnNumbRTV = Integer.valueOf(cell.getContents()).intValue();

            for (int row = 2; row <= existingPtnNumbRTV + 1; row++)
            {
                cell = sheet.getCell(0,row);
                myptn_numbRTV[row - 2] = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(1,row);
                myptn_nameRTV[row - 2] = cell.getContents();
                cell = sheet.getCell(2,row);
                myptn_typeRTV[row - 2] = cell.getContents();
                cell = sheet.getCell(3,row);
                myptn_emotRTV[row - 2] = cell.getContents();
                cell = sheet.getCell(4,row);
                myptn_codeRTV[row - 2] = cell.getContents();
                /*****      Scores ***************/
                cell = sheet.getCell(5,row);
                myptn_timesRTV[row - 2] = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(6,row);
                shappyRTV[row - 2]      = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(7,row);
                ssadRTV[row - 2]        = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(8,row);
                sangryRTV[row - 2]      = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(9,row);
                scontemptRTV[row - 2]   = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(10,row);
                ssupriseRTV[row - 2]    = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(11,row);
                sdisgustRTV[row - 2]    = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(12,row);
                sfearRTV[row - 2]       = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(13,row);
                snodRTV[row - 2]        = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(14,row);
                sshakeRTV[row - 2]      = Integer.valueOf(cell.getContents()).intValue();

            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }
    /**************************************************************/

    /*************************     Color  pattern************/
    int ptn_numbC = 0;
    String[] myptn_nameC = new String[999],myptn_typeC = new String[999],
            myptn_emotC = new String[999],myptn_codeC = new String[999];
    int[] myptn_numbC = new int[999],myptn_timesC = new int[999],
            shappyC = new int[999],ssadC = new int[999],sangryC = new int[999],
            scontemptC = new int[999],ssupriseC = new int[999],sdisgustC = new int[999],
            sfearC = new int[999],snodC = new int[999],sshakeC = new int[999];

    int existingPtnNumbC = 0;

    private void writeCExcel() {
        try {
            //  open the file
            File file=new File(Environment.getExternalStorageDirectory(),"Cptn.xls");
            //  create the sheet ,name it
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("My library vibration patterns", 0);

            sheet.mergeCells(0,0,2,0);//  merge the first 3 cell on row 1 and cell 4,5
            sheet.mergeCells(3,0,4,0);//    first 3:column  first 0: row
            sheet.mergeCells(6,0,14,0);
            //  row 1: total number of current ptn
            Label mystring = new Label(0, 0, "Total pattern number :");
            sheet.addCell(mystring);
            jxl.write.Number mynumb = new jxl.write.Number(3, 0, ptn_numbC);
            sheet.addCell(mynumb);
            mystring = new Label(6, 0, "Scores for different emotions(scores comes from questionnaire)");
            sheet.addCell(mystring);

            //  labels for different data
            mystring = new Label(0, 1, "Pattern Number");
            sheet.addCell(mystring);
            mystring = new Label(1, 1, "Pattern Name");
            sheet.addCell(mystring);
            mystring = new Label(2, 1, "Pattern Type");
            sheet.addCell(mystring);
            mystring = new Label(3, 1, "Pattern Emotion");
            sheet.addCell(mystring);
            mystring = new Label(4, 1, "Pattern Code");
            sheet.addCell(mystring);
            mystring = new Label(5, 1, "Testing Times");
            sheet.addCell(mystring);


            //  write data

            for (int row = 2; row <= ptn_numbC + 1; row++)
            {
                mynumb = new jxl.write.Number(0, row, myptn_numbC[ row - 2 ]);
                sheet.addCell(mynumb);
                mystring = new Label( 1,row, myptn_nameC[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 2,row, myptn_typeC[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 3,row, myptn_emotC[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 4,row, myptn_codeC[ row - 2 ]);
                sheet.addCell(mystring);
                mynumb = new jxl.write.Number(5, row, myptn_timesC[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(6, row, shappyC[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(7, row, ssadC[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(8, row, sangryC[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(9, row, scontemptC[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(10, row, ssupriseC[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(11, row, sdisgustC[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(12, row, sfearC[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(13, row, snodC[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(14, row, sshakeC[ row - 2 ]);
                sheet.addCell(mynumb);

            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readCExcel(){
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"Cptn.xls");
            Workbook workbook = Workbook.getWorkbook(file);
            //
            Sheet sheet = workbook.getSheet(0);
            //  get total number of existing ptn numbs
            Cell cell = sheet.getCell(3, 0);
            existingPtnNumbC = Integer.valueOf(cell.getContents()).intValue();

            for (int row = 2; row <= existingPtnNumbC + 1; row++)
            {
                cell = sheet.getCell(0,row);
                myptn_numbC[row - 2] = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(1,row);
                myptn_nameC[row - 2] = cell.getContents();
                cell = sheet.getCell(2,row);
                myptn_typeC[row - 2] = cell.getContents();
                cell = sheet.getCell(3,row);
                myptn_emotC[row - 2] = cell.getContents();
                cell = sheet.getCell(4,row);
                myptn_codeC[row - 2] = cell.getContents();
                /*****      Scores ***************/
                cell = sheet.getCell(5,row);
                myptn_timesC[row - 2] = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(6,row);
                shappyC[row - 2]      = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(7,row);
                ssadC[row - 2]        = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(8,row);
                sangryC[row - 2]      = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(9,row);
                scontemptC[row - 2]   = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(10,row);
                ssupriseC[row - 2]    = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(11,row);
                sdisgustC[row - 2]    = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(12,row);
                sfearC[row - 2]       = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(13,row);
                snodC[row - 2]        = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(14,row);
                sshakeC[row - 2]      = Integer.valueOf(cell.getContents()).intValue();

            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }
    /**************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques_compare);
        setTitle("                               Questionnaire Section two");
        tvnumb    = (TextView) findViewById(R.id.tvques_compemot);
        tvmessage = (TextView) findViewById(R.id.tvques_compmessage);
        btnfinish = (Button) findViewById(R.id.btnques_compfinish);
        btnswitch = (Button) findViewById(R.id.btnques_compswitch);
        imbtnptn1 = (ImageButton) findViewById(R.id.imbtnques_comp1);
        imbtnptn2 = (ImageButton) findViewById(R.id.imbtnques_comp2);
        imbtnnext = (ImageButton) findViewById(R.id.imbtnques_compnext);
        cbptn1    = (CheckBox) findViewById(R.id.cbques_comp1);
        cbptn2    = (CheckBox) findViewById(R.id.cbques_comp2);

        imbtnnext.setOnClickListener(this);
        cbptn1.setOnClickListener(this);
        cbptn2.setOnClickListener(this);
        imbtnptn1.setOnClickListener(this);
        imbtnptn2.setOnClickListener(this);
        btnfinish.setOnClickListener(this);
        btnswitch.setOnClickListener(this);
        //initialize
        readLVExcel();
        ptn_numbLV  = existingPtnNumbLV;
        readRTVExcel();
        ptn_numbRTV = existingPtnNumbRTV;
        readCExcel();
        ptn_numbC   = existingPtnNumbC;

        /**************************/
        iscolor  = false;
        emotnumb = myrand(9);
        flagvib  = myrand(2);
        cbptn1.setChecked(false);
        cbptn2.setChecked(false);
        if(flagvib == 1){
            ptnnumb1 = myrand(existingPtnNumbLV);
            is1LV = true;
        }else{
            ptnnumb1 = myrand(existingPtnNumbRTV);
            is1LV = false;
        }
        flagvib  = myrand(2);
        if(flagvib == 1){
            ptnnumb2 = myrand(existingPtnNumbLV);
            is2LV = true;
        }else{
            ptnnumb2 = myrand(existingPtnNumbRTV);
            is2LV = false;
        }
        /**************************/

        tvmessage.setText("In this section, you should choose the pattern that match the " +
                "required emotion better. Data will be save right after you move to next pattern," +
                "you can finish this questionnaire at any time.");
        tvnumb.setText("Which pattern is more " + dialogemot[emotnumb] + "?");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imbtnques_comp1:
                if(is1LV &&!iscolor){
                    curcode = myptn_codeLV[ptnnumb1];
                    BTsend(curcode);
                    msg(curcode);
                }else if(!is1LV && !iscolor){
                    curcode = myptn_codeRTV[ptnnumb1];
                    BTsend(curcode);
                    msg(curcode);
                }else{
                    curcode = myptn_codeC[ptnnumb1];
                    BTsend(curcode);
                    msg(curcode);
                }

                break;
            case R.id.imbtnques_comp2:
                if(is2LV &&!iscolor){
                    curcode = myptn_codeLV[ptnnumb2];
                    BTsend(curcode);
                    msg(curcode);
                }else if(!is2LV && !iscolor){
                    curcode = myptn_codeRTV[ptnnumb2];
                    BTsend(curcode);
                    msg(curcode);
                }else{
                    curcode = myptn_codeC[ptnnumb2];
                    BTsend(curcode);
                    msg(curcode);
                }
                break;
            case R.id.imbtnques_compnext:
                if(iscolor){
                    if(cbptn1.isChecked()) {
                        switch (emotnumb) {
                            case 0:
                                myptn_timesC[ptnnumb1] ++;
                                shappyC[ptnnumb1] ++;
                                break;
                            case 1:
                                myptn_timesC[ptnnumb1] ++;
                                ssadC[ptnnumb1] ++;
                                break;

                            case 2:
                                myptn_timesC[ptnnumb1] ++;
                                sangryC[ptnnumb1] ++;
                                break;

                            case 3:
                                myptn_timesC[ptnnumb1] ++;
                                scontemptC[ptnnumb1] ++;
                                break;

                            case 4:
                                myptn_timesC[ptnnumb1] ++;
                                ssupriseC[ptnnumb1] ++;
                                break;

                            case 5:
                                myptn_timesC[ptnnumb1] ++;
                                sdisgustC[ptnnumb1] ++;
                                break;

                            case 6:
                                myptn_timesC[ptnnumb1] ++;
                                sfearC[ptnnumb1] ++;
                                break;

                            case 7:
                                myptn_timesC[ptnnumb1] ++;
                                snodC[ptnnumb1] ++;
                                break;

                            case 8:
                                myptn_timesC[ptnnumb1] ++;
                                sshakeC[ptnnumb1] ++;
                                break;
                        }
                        emotnumb = myrand(9);
                        flagvib  = myrand(2);
                        cbptn1.setChecked(false);
                        cbptn2.setChecked(false);
                        if(flagvib == 1){
                            ptnnumb1 = myrand(existingPtnNumbLV);
                            is1LV = true;
                        }else{
                            ptnnumb1 = myrand(existingPtnNumbRTV);
                            is1LV = false;
                        }
                        flagvib  = myrand(2);
                        if(flagvib == 1){
                            ptnnumb2 = myrand(existingPtnNumbLV);
                            is2LV = true;
                        }else{
                            ptnnumb2 = myrand(existingPtnNumbRTV);
                            is2LV = false;
                        }
                        tvnumb.setText("Which pattern is more " + dialogemot[emotnumb] + "?");
                        writeLVExcel();
                        writeRTVExcel();
                        writeCExcel();
                    }else if(cbptn2.isChecked()){
                        switch (emotnumb) {
                            case 0:
                                myptn_timesC[ptnnumb2] ++;
                                shappyC[ptnnumb2] ++;
                                break;
                            case 1:
                                myptn_timesC[ptnnumb2] ++;
                                ssadC[ptnnumb2] ++;
                                break;

                            case 2:
                                myptn_timesC[ptnnumb2] ++;
                                sangryC[ptnnumb2] ++;
                                break;

                            case 3:
                                myptn_timesC[ptnnumb2] ++;
                                scontemptC[ptnnumb2] ++;
                                break;

                            case 4:
                                myptn_timesC[ptnnumb2] ++;
                                ssupriseC[ptnnumb2] ++;
                                break;

                            case 5:
                                myptn_timesC[ptnnumb2] ++;
                                sdisgustC[ptnnumb2] ++;
                                break;

                            case 6:
                                myptn_timesC[ptnnumb2] ++;
                                sfearC[ptnnumb2] ++;
                                break;

                            case 7:
                                myptn_timesC[ptnnumb2] ++;
                                snodC[ptnnumb2] ++;
                                break;

                            case 8:
                                myptn_timesC[ptnnumb2] ++;
                                sshakeC[ptnnumb2] ++;
                                break;
                        }
                        emotnumb = myrand(9);
                        flagvib  = myrand(2);
                        cbptn1.setChecked(false);
                        cbptn2.setChecked(false);
                        if(flagvib == 1){
                            ptnnumb1 = myrand(existingPtnNumbLV);
                            is1LV = true;
                        }else{
                            ptnnumb1 = myrand(existingPtnNumbRTV);
                            is1LV = false;
                        }
                        flagvib  = myrand(2);
                        if(flagvib == 1){
                            ptnnumb2 = myrand(existingPtnNumbLV);
                            is2LV = true;
                        }else{
                            ptnnumb2 = myrand(existingPtnNumbRTV);
                            is2LV = false;
                        }
                        tvnumb.setText("Which pattern is more " + dialogemot[emotnumb] + "?");
                        writeLVExcel();
                        writeRTVExcel();
                        writeCExcel();
                    }else{
                        msg("Make your choice first!");
                    }

                }else if(cbptn1.isChecked()){
                    if(is1LV){
                        switch (emotnumb) {
                            case 0:
                                myptn_timesLV[ptnnumb1] ++;
                                shappyLV[ptnnumb1] ++;
                                break;
                            case 1:
                                myptn_timesLV[ptnnumb1] ++;
                                ssadLV[ptnnumb1] ++;
                                break;

                            case 2:
                                myptn_timesLV[ptnnumb1] ++;
                                sangryLV[ptnnumb1] ++;
                                break;

                            case 3:
                                myptn_timesLV[ptnnumb1] ++;
                                scontemptLV[ptnnumb1] ++;
                                break;

                            case 4:
                                myptn_timesLV[ptnnumb1] ++;
                                ssupriseLV[ptnnumb1] ++;
                                break;

                            case 5:
                                myptn_timesLV[ptnnumb1] ++;
                                sdisgustLV[ptnnumb1] ++;
                                break;

                            case 6:
                                myptn_timesLV[ptnnumb1] ++;
                                sfearLV[ptnnumb1] ++;
                                break;

                            case 7:
                                myptn_timesLV[ptnnumb1] ++;
                                snodLV[ptnnumb1] ++;
                                break;

                            case 8:
                                myptn_timesLV[ptnnumb1] ++;
                                sshakeLV[ptnnumb1] ++;
                                break;
                        }
                        emotnumb = myrand(9);
                        flagvib  = myrand(2);
                        cbptn1.setChecked(false);
                        cbptn2.setChecked(false);
                        if(flagvib == 1){
                            ptnnumb1 = myrand(existingPtnNumbLV);
                            is1LV = true;
                        }else{
                            ptnnumb1 = myrand(existingPtnNumbRTV);
                            is1LV = false;
                        }
                        flagvib  = myrand(2);
                        if(flagvib == 1){
                            ptnnumb2 = myrand(existingPtnNumbLV);
                            is2LV = true;
                        }else{
                            ptnnumb2 = myrand(existingPtnNumbRTV);
                            is2LV = false;
                        }
                        tvnumb.setText("Which pattern is more " + dialogemot[emotnumb] + "?");
                        writeLVExcel();
                        writeRTVExcel();
                        writeCExcel();
                    }else{
                        switch (emotnumb) {
                            case 0:
                                myptn_timesRTV[ptnnumb1] ++;
                                shappyRTV[ptnnumb1] ++;
                                break;
                            case 1:
                                myptn_timesRTV[ptnnumb1] ++;
                                ssadRTV[ptnnumb1] ++;
                                break;

                            case 2:
                                myptn_timesRTV[ptnnumb1] ++;
                                sangryRTV[ptnnumb1] ++;
                                break;

                            case 3:
                                myptn_timesRTV[ptnnumb1] ++;
                                scontemptRTV[ptnnumb1] ++;
                                break;

                            case 4:
                                myptn_timesRTV[ptnnumb1] ++;
                                ssupriseRTV[ptnnumb1] ++;
                                break;

                            case 5:
                                myptn_timesRTV[ptnnumb1] ++;
                                sdisgustRTV[ptnnumb1] ++;
                                break;

                            case 6:
                                myptn_timesRTV[ptnnumb1] ++;
                                sfearRTV[ptnnumb1] ++;
                                break;

                            case 7:
                                myptn_timesRTV[ptnnumb1] ++;
                                snodRTV[ptnnumb1] ++;
                                break;

                            case 8:
                                myptn_timesRTV[ptnnumb1] ++;
                                sshakeRTV[ptnnumb1] ++;
                                break;
                        }
                        emotnumb = myrand(9);
                        flagvib  = myrand(2);
                        cbptn1.setChecked(false);
                        cbptn2.setChecked(false);
                        if(flagvib == 1){
                            ptnnumb1 = myrand(existingPtnNumbLV);
                            is1LV = true;
                        }else{
                            ptnnumb1 = myrand(existingPtnNumbRTV);
                            is1LV = false;
                        }
                        flagvib  = myrand(2);
                        if(flagvib == 1){
                            ptnnumb2 = myrand(existingPtnNumbLV);
                            is2LV = true;
                        }else{
                            ptnnumb2 = myrand(existingPtnNumbRTV);
                            is2LV = false;
                        }
                        tvnumb.setText("Which pattern is more " + dialogemot[emotnumb] + "?");
                        writeLVExcel();
                        writeRTVExcel();
                        writeCExcel();
                    }
                }else if(cbptn2.isChecked()){
                    if(is2LV){
                        switch (emotnumb) {
                            case 0:
                                myptn_timesLV[ptnnumb2] ++;
                                shappyLV[ptnnumb2] ++;
                                break;
                            case 1:
                                myptn_timesLV[ptnnumb2] ++;
                                ssadLV[ptnnumb2] ++;
                                break;

                            case 2:
                                myptn_timesLV[ptnnumb2] ++;
                                sangryLV[ptnnumb2] ++;
                                break;

                            case 3:
                                myptn_timesLV[ptnnumb2] ++;
                                scontemptLV[ptnnumb2] ++;
                                break;

                            case 4:
                                myptn_timesLV[ptnnumb2] ++;
                                ssupriseLV[ptnnumb2] ++;
                                break;

                            case 5:
                                myptn_timesLV[ptnnumb2] ++;
                                sdisgustLV[ptnnumb2] ++;
                                break;

                            case 6:
                                myptn_timesLV[ptnnumb2] ++;
                                sfearLV[ptnnumb2] ++;
                                break;

                            case 7:
                                myptn_timesLV[ptnnumb2] ++;
                                snodLV[ptnnumb2] ++;
                                break;

                            case 8:
                                myptn_timesLV[ptnnumb2] ++;
                                sshakeLV[ptnnumb2] ++;
                                break;
                        }
                        emotnumb = myrand(9);
                        flagvib  = myrand(2);
                        cbptn1.setChecked(false);
                        cbptn2.setChecked(false);
                        if(flagvib == 1){
                            ptnnumb1 = myrand(existingPtnNumbLV);
                            is1LV = true;
                        }else{
                            ptnnumb1 = myrand(existingPtnNumbRTV);
                            is1LV = false;
                        }
                        flagvib  = myrand(2);
                        if(flagvib == 1){
                            ptnnumb2 = myrand(existingPtnNumbLV);
                            is2LV = true;
                        }else{
                            ptnnumb2 = myrand(existingPtnNumbRTV);
                            is2LV = false;
                        }
                        tvnumb.setText("Which pattern is more " + dialogemot[emotnumb] + "?");
                        writeLVExcel();
                        writeRTVExcel();
                        writeCExcel();
                    }else{
                        switch (emotnumb) {
                            case 0:
                                myptn_timesRTV[ptnnumb2] ++;
                                shappyRTV[ptnnumb2] ++;
                                break;
                            case 1:
                                myptn_timesRTV[ptnnumb2] ++;
                                ssadRTV[ptnnumb2] ++;
                                break;

                            case 2:
                                myptn_timesRTV[ptnnumb2] ++;
                                sangryRTV[ptnnumb2] ++;
                                break;

                            case 3:
                                myptn_timesRTV[ptnnumb2] ++;
                                scontemptRTV[ptnnumb2] ++;
                                break;

                            case 4:
                                myptn_timesRTV[ptnnumb2] ++;
                                ssupriseRTV[ptnnumb2] ++;
                                break;

                            case 5:
                                myptn_timesRTV[ptnnumb2] ++;
                                sdisgustRTV[ptnnumb2] ++;
                                break;

                            case 6:
                                myptn_timesRTV[ptnnumb2] ++;
                                sfearRTV[ptnnumb2] ++;
                                break;

                            case 7:
                                myptn_timesRTV[ptnnumb2] ++;
                                snodRTV[ptnnumb2] ++;
                                break;

                            case 8:
                                myptn_timesRTV[ptnnumb2] ++;
                                sshakeRTV[ptnnumb2] ++;
                                break;
                        }
                        emotnumb = myrand(9);
                        flagvib  = myrand(2);
                        cbptn1.setChecked(false);
                        cbptn2.setChecked(false);
                        if(flagvib == 1){
                            ptnnumb1 = myrand(existingPtnNumbLV);
                            is1LV = true;
                        }else{
                            ptnnumb1 = myrand(existingPtnNumbRTV);
                            is1LV = false;
                        }
                        flagvib  = myrand(2);
                        if(flagvib == 1){
                            ptnnumb2 = myrand(existingPtnNumbLV);
                            is2LV = true;
                        }else{
                            ptnnumb2 = myrand(existingPtnNumbRTV);
                            is2LV = false;
                        }
                        tvnumb.setText("Which pattern is more " + dialogemot[emotnumb] + "?");
                        writeLVExcel();
                        writeRTVExcel();
                        writeCExcel();
                    }

                }else{
                    msg("Make your choice first!");
                }

                break;
            case R.id.btnques_compfinish:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ques_compare.this);
                builder1.setTitle("Help"); //title
                builder1.setMessage(
                        "Thank you so much for your effort!");
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(ques_compare.this, Homepage.class);
                        startActivity(i);
                        finishAfterTransition();
                    }
                });
                builder1.create().show();

                break;
            case R.id.btnques_compswitch:
                if(!iscolor) {
                    iscolor = true;
                    btnswitch.setText("SWITCH TO VIBRATION PATTERNS");
                }else{
                    iscolor = false;
                    btnswitch.setText("SWITCH TO COLOR PATTERNS");
                }
                break;
            case R.id.cbques_comp1:
                if(cbptn1.isChecked()){
                    cbptn2.setChecked(false);
                }
                break;
            case R.id.cbques_comp2:
                if(cbptn2.isChecked()){
                    cbptn1.setChecked(false);
                }
                break;
        }
    }
    private int myrand(int max){   // random number: 0 to max -1
        Random random=new Random();
        return random.nextInt(max);
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
    public void smsg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
