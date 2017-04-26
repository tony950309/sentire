package com.example.jiachenggu.sentirepro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
public class Settings extends AppCompatActivity implements View.OnClickListener {
    Button btnini,btnclear,btnupload,btndome;
    TextView tvcode,tvpw;
    public EditText e1 ;
    private List<savedptn_class> mylist=new ArrayList<savedptn_class>();
    private ListView listView;
    private savedptn_adapter myadapter;
    public String password = "0620";
    private Dialog mydialog;
    boolean isready,isini,isupload,isclear;
    private String[][] tdatas = new String[18][4];
    private int[][] tdatai = new int[18][10];
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
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        btndome  = (Button) findViewById(R.id.btndemo);
        btnini   = (Button) findViewById(R.id.btnsettingini);
        btnclear = (Button) findViewById(R.id.btnclearscore);
        btnupload= (Button) findViewById(R.id.btnsettingup);
        tvcode   = (TextView)findViewById(R.id.tvsettings);

        btndome.setOnClickListener(this);
        btnini.setOnClickListener(this);
        btnclear.setOnClickListener(this);
        btnupload.setOnClickListener(this);
        writeupdata();
        isready = true;
        for(int j = 0; j < 18;j ++){
            if(tdatas[j][1].equals("null"))
                isready = false;
        }

        initptn();
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        savedptn_class value = mylist.get(position);
                        tvcode.setText("Pattern Code : " + tdatas[position][3]);
                    }
                }
        );

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btndemo:
                Intent iii = new Intent(Settings.this, Demo.class);
                startActivity(iii);
//                finish();
                break;
            case R.id.btnsettingini:
                isini    = true;
                isupload = false;
                isclear  = false;
                mydialog = new Dialog(this);
                View view = LayoutInflater.from(this).inflate(R.layout.hp_seeq_pw,null);
                //set onclick listener for btn in dialog
                view.findViewById(R.id.btndialogcanc).setOnClickListener(this);
                view.findViewById(R.id.btndialogconf).setOnClickListener(this);
                tvpw = (TextView)view.findViewById(R.id.tvdialogmessage);
                e1 = (EditText)view.findViewById(R.id.etdialogpw); /********view. is important otherwise it would collapse***********//****************************//****************************//****************************/
                mydialog.setTitle("Initialize Confirm");
                mydialog.setContentView(view);
                tvpw.setText("Initialize is only for the first time run this app." + "\n" +
                        "Or when you want to delete the existing questionnaire detail" +
                        " data files. (After initialization, you would have to delete " +
                        "these files by hand!) " +
                        "\n" + "Scores of patterns would not be delete!" + "\n" +
                        "Type in the password to initialize.");
                mydialog.show();
                break;


            case R.id.btnclearscore:
                isini    = false;
                isupload = false;
                isclear  = true;
                mydialog = new Dialog(this);
                view = LayoutInflater.from(this).inflate(R.layout.hp_seeq_pw,null);
                //set onclick listener for btn in dialog
                view.findViewById(R.id.btndialogcanc).setOnClickListener(this);
                view.findViewById(R.id.btndialogconf).setOnClickListener(this);
                tvpw = (TextView)view.findViewById(R.id.tvdialogmessage);
                e1 = (EditText)view.findViewById(R.id.etdialogpw); /********view. is important otherwise it would collapse***********//****************************//****************************//****************************/
                mydialog.setTitle("Clear Score?");
                mydialog.setContentView(view);
                tvpw.setText("Clear is IRREVERSIBLE! Are you still want to clear all the scores?" + "\n" +
                        "Type in the password to clear the scores.");
                mydialog.show();

                break;

            case R.id.btnsettingup:
                isini    = false;
                isupload = true;
                isclear  = false;
                mydialog = new Dialog(this);
                view = LayoutInflater.from(this).inflate(R.layout.hp_seeq_pw,null);
                //set onclick listener for btn in dialog
                view.findViewById(R.id.btndialogcanc).setOnClickListener(this);
                view.findViewById(R.id.btndialogconf).setOnClickListener(this);
                tvpw = (TextView)view.findViewById(R.id.tvdialogmessage);
                e1 = (EditText)view.findViewById(R.id.etdialogpw); /********view. is important otherwise it would collapse***********//****************************//****************************//****************************/
                mydialog.setTitle("Upload?");
                mydialog.setContentView(view);
                tvpw.setText(//"Arduino's RAM is " + "\n" +
                        "Type in the password to upload.");
                mydialog.show();

                break;
            case R.id.btndialogcanc:
                mydialog.dismiss();
                break;
            case R.id.btndialogconf:
                if( password.equals( e1.getText().toString() ) ){     //use equals to find whether two string value are the same
                    if(isini){
                        isini    = false;
                        isupload = false;
                        isclear  = false;
                        writeQnumb(0);
                        createupdata();
                    }
                    if(isclear){
                        isini    = false;
                        isupload = false;
                        isclear  = false;
                        readLVExcel();
                        ptn_numbLV = existingPtnNumbLV;
                        readRTVExcel();
                        ptn_numbRTV = existingPtnNumbRTV;
                        readCExcel();
                        ptn_numbC = existingPtnNumbC;
                        for(int i = 0; i < 999; i++){
                            myptn_timesLV[i] = 0 ;
                            shappyLV[i]      = 0 ;
                            ssadLV[i]        = 0 ;
                            sangryLV[i]      = 0 ;
                            scontemptLV[i]   = 0 ;
                            ssupriseLV[i]    = 0 ;
                            sdisgustLV[i]    = 0 ;
                            sfearLV[i]       = 0 ;
                            snodLV[i]        = 0 ;
                            sshakeLV[i]      = 0 ;

                            myptn_timesRTV[i] = 0 ;
                            shappyRTV[i]      = 0 ;
                            ssadRTV[i]        = 0 ;
                            sangryRTV[i]      = 0 ;
                            scontemptRTV[i]   = 0 ;
                            ssupriseRTV[i]    = 0 ;
                            sdisgustRTV[i]    = 0 ;
                            sfearRTV[i]       = 0 ;
                            snodRTV[i]        = 0 ;
                            sshakeRTV[i]      = 0 ;

                            myptn_timesC[i] = 0 ;
                            shappyC[i]      = 0 ;
                            ssadC[i]        = 0 ;
                            sangryC[i]      = 0 ;
                            scontemptC[i]   = 0 ;
                            ssupriseC[i]    = 0 ;
                            sdisgustC[i]    = 0 ;
                            sfearC[i]       = 0 ;
                            snodC[i]        = 0 ;
                            sshakeC[i]      = 0 ;
                        }
                        writeLVExcel();
                        writeRTVExcel();
                        writeCExcel();
                    }
                    if(isupload){
                        isini    = false;
                        isupload = false;
                        isclear  = false;
                        if(!isready){
                            msg("Some emotion is not ready to upload!");
                        }else if(isready && BTdevicelist.isBtConnected){

                            for(int i = 0; i < 18;i++){

                                BTsend("U" + tdatas[i][3]);
                                for(int j = 0;j<= 99999;j++){
                                    for(int k = 0; k<= 50;k++){
                                        int b = 1;
                                    }
                                }
                            }
                            msg("Upload succeed!!!");
                        }else
                            msg("Connect the Wristband before uploading!");
                    }
                }
                else {msg("The password is wrong!");}
                mydialog.dismiss();
                break;

        }

    }
    private void initptn(){
        for(int i = 0; i<18;i++){
            mylist.add(new savedptn_class(
                    tdatas[i][0],tdatas[i][1],tdatas[i][2],tdatas[i][3],
                    tdatai[i][0],tdatai[i][1],tdatai[i][2],tdatai[i][3],tdatai[i][4],
                    tdatai[i][5],tdatai[i][6],tdatai[i][7],tdatai[i][8],tdatai[i][9],
                    R.drawable.ic_media_embed_play));
        }
        myadapter = new savedptn_adapter(mylist);
        listView  = (ListView)findViewById(R.id.lvsettings);
        listView.setAdapter(myadapter);
    }
    private void writeQnumb(int n){
        try {
            //  open the file
            File file=new File(Environment.getExternalStorageDirectory(),"QuestionnaireNumber.xls");
            //  create the sheet ,name it
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("Current numbers", 0);
            jxl.write.Number mynumb = new jxl.write.Number(0, 0, n);
            sheet.addCell(mynumb);
            mynumb = new jxl.write.Number(1, 0, 0);
            sheet.addCell(mynumb);
            mynumb = new jxl.write.Number(2, 0, 0);
            sheet.addCell(mynumb);
            mynumb = new jxl.write.Number(3, 0, 0);
            sheet.addCell(mynumb);


            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createupdata(){
        try {
            //  open the file
            File file=new File(Environment.getExternalStorageDirectory(),"datatoupload.xls");
            //  create the sheet ,name it
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("data to upload", 0);
            for (int row = 0; row < 18; row++) {
                for (int column = 0; column < 4; column++) {        //details
                    Label mystring = new Label(column, row, "null");
                    sheet.addCell(mystring);
                }
                for (int column = 4; column < 14; column++){        //scores
                    jxl.write.Number mynumb = new jxl.write.Number(column, row, 0);
                    sheet.addCell(mynumb);
                }
            }
            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeupdata() {
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"datatoupload.xls");
            Workbook workbook = Workbook.getWorkbook(file);
            //
            Sheet sheet = workbook.getSheet(0);

            for (int row = 0; row < 18; row++) {
                for (int column = 0; column < 4; column++) {        //details

                    Cell cell = sheet.getCell(column, row);
                    tdatas[row][column] = cell.getContents();
                }
                for (int column = 4; column < 14; column++){        //scores
                    Cell cell = sheet.getCell(column, row);
                    tdatai[row][column - 4] = Integer.valueOf(cell.getContents()).intValue();
                }
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
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
    public void smsg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
