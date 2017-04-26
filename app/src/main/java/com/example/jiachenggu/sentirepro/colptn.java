package com.example.jiachenggu.sentirepro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class colptn extends AppCompatActivity implements View.OnClickListener {

    Button btndel,btnset,btnsave;
    TextView tvbtnemot,tvcode,tvbtnclear,tvbtnnew;
    EditText etname;
    ImageButton imbtnhelp;
    ImageView imbtnrefresh;



    /*******************************************************************/
    //   filter
    String[] dialogemot = new String[]{"Happy","Sad","Angry","Contempt","Surprised",
            "Disgust","Fear","Nod","Shake"};
    boolean[] fltselected = new boolean[]{true, true,   true,   true,   true,
            true,   true,  true, true};

    boolean[] ptnnumb_flag = new boolean[999];

    static public int editptnnumb;
    static public boolean isedit = false;
    static public boolean isback = false;
    boolean isselected = false;


    private List<savedptn_class> mylist=new ArrayList<savedptn_class>();
    private ListView listView;
    private savedptn_adapter myadapter;
    /******************* Read /write excel ************************/
    private String Cfilename = "Cptn.xls";    //C : color pattern file name
    public int sltbtn = 0;              // for listview on click listener
    int ptn_numb = 0;
    int buf_ptn_numb = 0;
    String[] myptn_name = new String[999],myptn_type = new String[999],
            myptn_emot = new String[999],myptn_code = new String[999];
    int[] myptn_numb = new int[999],myptn_times = new int[999],
            shappy = new int[999],ssad = new int[999],sangry = new int[999],
            scontempt = new int[999],ssuprise = new int[999],sdisgust = new int[999],
            sfear = new int[999],snod = new int[999],sshake = new int[999];

    int existingPtnNumb = 0;

    private void writeExcel(String Filename) {
        try {
            //  open the file
            File file=new File(Environment.getExternalStorageDirectory(),Filename);
            //  create the sheet ,name it
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("My color patterns", 0);

            sheet.mergeCells(0,0,2,0);//  merge the first 3 cell on row 1 and cell 4,5
            sheet.mergeCells(3,0,4,0);//    first 3:column  first 0: row
            sheet.mergeCells(6,0,14,0);
            //  row 1: total number of current ptn
            Label mystring = new Label(0, 0, "Total pattern number :");
            sheet.addCell(mystring);
            jxl.write.Number mynumb = new jxl.write.Number(3, 0, ptn_numb);
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

            for (int row = 2; row <= ptn_numb + 1; row++)
            {
                mynumb = new jxl.write.Number(0, row, myptn_numb[ row - 2 ]);
                sheet.addCell(mynumb);
                mystring = new Label( 1,row, myptn_name[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 2,row, myptn_type[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 3,row, myptn_emot[ row - 2 ]);
                sheet.addCell(mystring);
                mystring = new Label( 4,row, myptn_code[ row - 2 ]);
                sheet.addCell(mystring);
                mynumb = new jxl.write.Number(5, row, myptn_times[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(6, row, shappy[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(7, row, ssad[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(8, row, sangry[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(9, row, scontempt[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(10, row, ssuprise[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(11, row, sdisgust[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(12, row, sfear[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(13, row, snod[ row - 2 ]);
                sheet.addCell(mynumb);
                mynumb = new jxl.write.Number(14, row, sshake[ row - 2 ]);
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
    private void readExcel(String Filename){
        try {
            File file=new File(Environment.getExternalStorageDirectory(),Filename);
            Workbook workbook = Workbook.getWorkbook(file);
            //
            Sheet sheet = workbook.getSheet(0);
            //  get total number of existing ptn numbs
            Cell cell = sheet.getCell(3, 0);
            existingPtnNumb = Integer.valueOf(cell.getContents()).intValue();

            for (int row = 2; row <= existingPtnNumb + 1; row++)
            {
                cell = sheet.getCell(0,row);
                myptn_numb[row - 2] = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(1,row);
                myptn_name[row - 2] = cell.getContents();
                cell = sheet.getCell(2,row);
                myptn_type[row - 2] = cell.getContents();
                cell = sheet.getCell(3,row);
                myptn_emot[row - 2] = cell.getContents();
                cell = sheet.getCell(4,row);
                myptn_code[row - 2] = cell.getContents();
                /*****      Scores ***************/
                cell = sheet.getCell(5,row);
                myptn_times[row - 2] = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(6,row);
                shappy[row - 2]      = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(7,row);
                ssad[row - 2]        = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(8,row);
                sangry[row - 2]      = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(9,row);
                scontempt[row - 2]   = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(10,row);
                ssuprise[row - 2]    = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(11,row);
                sdisgust[row - 2]    = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(12,row);
                sfear[row - 2]       = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(13,row);
                snod[row - 2]        = Integer.valueOf(cell.getContents()).intValue();
                cell = sheet.getCell(14,row);
                sshake[row - 2]      = Integer.valueOf(cell.getContents()).intValue();

            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }
    private void writeupdata( String[] data_s,int[] data_i) {
        String[][] tdatas = new String[18][4];
        int[][] tdatai = new int[18][10];
//        try {
//            //  open the file
//            File file=new File(Environment.getExternalStorageDirectory(),"datatoupload.xls");
//            //  create the sheet ,name it
//            WritableWorkbook book = Workbook.createWorkbook(file);
//            WritableSheet sheet = book.createSheet("data to upload", 0);
//            for (int row = 0; row < 18; row++) {
//                for (int column = 0; column < 4; column++) {        //details
//                    Label mystring = new Label(column, row, "null");
//                    sheet.addCell(mystring);
//                }
//                for (int column = 4; column < 14; column++){        //scores
//                    jxl.write.Number mynumb = new jxl.write.Number(column, row, 0);
//                    sheet.addCell(mynumb);
//                }
//            }
//            book.write();
//            book.close();
//        } catch (WriteException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
        String[] database = new String[]{"Happy","Sad","Angry","Contempt","Surprised",
                "Disgust","Fear","Nod","Shake"};

        if(!data_s[1].equals("C")) {        //for vibration patterns
            for (int i = 0; i < 9; i++) {
                if (database[i].equals(data_s[2])) {
                    for(int j = 0;j < 4;j++){
                        tdatas[i][j] = data_s[j];
                    }
                    for(int j = 0;j < 10;j ++){
                        tdatai[i][j] = data_i[j];
                    }
                }
            }
        }else {
            for (int i = 0; i < 9; i++) {
                if (database[i].equals(data_s[2])) {
                    for(int j = 0;j < 4;j++){
                        tdatas[i + 9][j] = data_s[j];
                    }
                    for(int j = 0;j < 10;j ++){
                        tdatai[i + 9][j] = data_i[j];
                    }
                }
            }
        }

        try {
            //  open the file
            File file=new File(Environment.getExternalStorageDirectory(),"datatoupload.xls");
            //  create the sheet ,name it
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("data to upload", 0);
            for (int row = 0; row < 18; row++) {
                for (int column = 0; column < 4; column++) {        //details
                    Label mystring = new Label(column, row, tdatas[row][column]);
                    sheet.addCell(mystring);
                }
                for (int column = 4; column < 14; column++){        //scores
                    jxl.write.Number mynumb = new jxl.write.Number(column, row, tdatai[row][column - 4]);
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
    /*******************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colptn);
        setTitle("                              Color Pattern List");



        // read excel file
        readExcel(Cfilename);
        ptn_numb = existingPtnNumb;

        //declear the widgets

//        btnflt     = (Button) findViewById(R.id.btnLVfilter);
        btndel     = (Button) findViewById(R.id.btnCdel);
        btnset     = (Button) findViewById(R.id.btnCsetas);
        btnsave    = (Button) findViewById(R.id.btnCsave);
        etname     = (EditText) findViewById(R.id.etCptnname);
        tvbtnemot  = (TextView) findViewById(R.id.tvbtnCptnemot);
        tvcode     = (TextView) findViewById(R.id.tvbtnCptncode);
        tvbtnclear = (TextView) findViewById(R.id.tvbtnCclear);
        tvbtnnew   = (TextView) findViewById(R.id.tvbtnCnew);
        imbtnhelp  = (ImageButton) findViewById(R.id.imbtnChelp);
        imbtnrefresh = (ImageView) findViewById(R.id.imbtnCrefresh);
        // set onclick listener
//        btnflt.setOnClickListener(this);
        btndel.setOnClickListener(this);
        btnset.setOnClickListener(this);
        btnsave.setOnClickListener(this);

        tvbtnemot.setOnClickListener(this);
        tvbtnclear.setOnClickListener(this);
        tvbtnnew.setOnClickListener(this);
        imbtnhelp.setOnClickListener(this);
        imbtnrefresh.setOnClickListener(this);
        initptn();

        for(int i = 0; i <999;i++)
            ptnnumb_flag[i] = false;

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        savedptn_class value = mylist.get(position);
                        sltbtn = position;
                        editptnnumb = myptn_numb[position];
                        etname.setText(myptn_name[position]);
                        tvbtnemot.setText(myptn_emot[position]);
                        tvcode.setText(myptn_code[position]);
                        isselected = true;
                    }
                }

        );
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
//            case R.id.btnLVfilter:
//                AlertDialog.Builder builder0 = new AlertDialog.Builder(libvibptn.this);
//                builder0.setTitle("Select the emotion"); //title
////                int dialogemot_numb = 0;
////                builder.setIcon(R.mipmap.ic_launcher);//picture
////                for(int i = 0; i<=8;i++){
////                    if (dialogemot[i] == myptn_emot[sltbtn]){
////                        dialogemot_numb = i;
////                    }
////                }
//                builder0.setMultiChoiceItems(dialogemot,fltselected,
//                        new DialogInterface.OnMultiChoiceClickListener()  {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which, boolean isChecked) {
//                                fltselected[which] = isChecked;
//
//                            }
//                        });
//                builder0.setPositiveButton("YES",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
////                        for(int i = 0; i < existingPtnNumb; i++){
////                            for(int j = 0; j < 9; j ++){
////                                if(myptn_emot[i].equals(dialogemot[j]) && fltselected[j])
////                                    ptnnumb_flag[i] = true;
////                                else
////                                    ptnnumb_flag[i] = false;
////                            }
////                        }
//                        dialog.dismiss();
//
//                    }
//                });
//                builder0.create().show();
//                break;
            case R.id.imbtnChelp:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(colptn.this);
                builder1.setTitle("Help"); //title
                builder1.setMessage(
                        "To look at the saved pattern details, simply click on the pattern in the list." + "\n" + "\n" +
                        "You can edit its name, emotion (click on its emotion), you can delete the selected" +
                        "pattern as well." + "\n" + "\n" +"'Set as' is to add this pattern to the pattern list, which " +
                        "can be upload to the wristband in  <Wristband Settings>." + "\n" + "\n" +
                        "After edition, click save button to save the changed data." + "\n" + "\n" +
                        "'Clear' button will clear all the existing pattern data. You can create new pattern if click 'Create new'.");
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.create().show();
                break;
            case R.id.tvbtnCptnemot:
                if(isselected) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(colptn.this);
                    builder2.setTitle("Select the emotion"); //title

                    builder2.setSingleChoiceItems(dialogemot, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tvbtnemot.setText(dialogemot[which]);
                                }
                            });
                    builder2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();


                        }
                    });
                    builder2.create().show();
                }else
                    msg("Select one pattern before editing!");
                break;
            case R.id.btnCdel:
                if(isselected) {
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(this);  //builder
                    builder3.setTitle("Delete confirm"); //title
                    builder3.setMessage("Delete is IRREVERSIBLE! Are you still want to delete this pattern?"); //message
                    builder3.setPositiveButton("Delete", new DialogInterface.OnClickListener() { //设置确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //
                            for (int i = sltbtn; i < existingPtnNumb; i++) {
                                //myptn_numb[i] = myptn_numb[i + 1];     // delete : move the next ptn data
                                myptn_name[i] = myptn_name[i + 1];       // to current ptn
                                myptn_type[i] = myptn_type[i + 1];
                                myptn_emot[i] = myptn_emot[i + 1];
                                myptn_code[i] = myptn_code[i + 1];
                            }                                           //the ptn number would use the previous
                            myptn_numb[existingPtnNumb - 1] = 0;        //ptn's number
                            existingPtnNumb--;                          //delete: existing ptn number -1
                            ptn_numb = existingPtnNumb;                 // writeExcel would the the ptn_numb
                            writeExcel(Cfilename);
                            msg("Delete succeed");
                            for (int i = 0; i <= existingPtnNumb; i++) {
                                mylist.remove(0);
                            }
                            initptn();
                            etname.setText("");
                            tvcode.setText("");
                            tvbtnemot.setText("Click to set emotion");
                            isselected = false;
                        }
                    });
                    builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //设置取消按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder3.create().show();
                }else
                    msg("Select one pattern before editing!");
                break;
            case R.id.btnCsetas:
                if(isselected) {
                    String[] data_s = new String[4];
                    data_s[0] = myptn_name[sltbtn];
                    data_s[1] = myptn_type[sltbtn];
                    data_s[2] = myptn_emot[sltbtn];
                    data_s[3] = myptn_code[sltbtn];

                    int[] data_i = new int[10];
                    data_i[0] = myptn_times[sltbtn];
                    data_i[1] = shappy[sltbtn];
                    data_i[2] = ssad[sltbtn];
                    data_i[3] = sangry[sltbtn];
                    data_i[4] = scontempt[sltbtn];
                    data_i[5] = ssuprise[sltbtn];
                    data_i[6] = sdisgust[sltbtn];
                    data_i[7] = sfear[sltbtn];
                    data_i[8] = snod[sltbtn];
                    data_i[9] = sshake[sltbtn];
                    writeupdata(data_s,data_i);
                    msg("Set as " + data_s[2] + " color pattern!");
                }else
                    msg("Select one pattern before editing!");
                break;
            case R.id.btnCsave:
                if(isselected) {
                    myptn_name[sltbtn] = etname.getText().toString();
                    myptn_emot[sltbtn] = tvbtnemot.getText().toString();
                    writeExcel(Cfilename);
                    for (int i = 0; i < existingPtnNumb; i++) {
                        mylist.remove(0);
                    }
                    initptn();
                    etname.setText("");
                    tvcode.setText("");
                    tvbtnemot.setText("Click to set emotion");
                    isselected = false;
                }else
                    msg("Select one pattern before editing!");
                break;

            case R.id.tvbtnCclear:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                builder4.setTitle("Clear Confirm"); //title
                builder4.setMessage("Clear is IRREVERSIBLE! Are you still want to delete all the patterns?"); //message
                builder4.setPositiveButton("Delete", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //
                        for(int i=0; i<existingPtnNumb;i++){
                            //myptn_numb[i] = myptn_numb[i + 1];     // delete : move the next ptn data
                            myptn_name[i] = null;       // to current ptn
                            myptn_type[i] = null;
                            myptn_emot[i] = null;
                            myptn_code[i] = null;
                            //the ptn number would use the previous
                            myptn_numb[existingPtnNumb - 1] = 0;        //ptn's number
                            mylist.remove(0);
                        }

                        existingPtnNumb = 0;                          //clear: existing ptn number = 0
                        ptn_numb = existingPtnNumb;                 // writeExcel would the the ptn_numb
                        writeExcel(Cfilename);
                        etname.setText("");
                        tvcode.setText("");
                        tvbtnemot.setText("Click to set emotion");
                        isselected = false;
                        msg("Clear succeed");
                        initptn();
                    }
                });
                builder4.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder4.create().show();
                break;
            case R.id.tvbtnCnew:
                isedit = false;
                readExcel(Cfilename);
                if(isback){
                    for (int j = 0; j < buf_ptn_numb; j++) {
                        mylist.remove(0);
                    }
                    isback = false;
                }else{
                    for (int j = 0; j < existingPtnNumb; j++) {
                        mylist.remove(0);
                    }
                }
                initptn();
                etname.setText("");
                tvcode.setText("");
                tvbtnemot.setText("Click to set emotion");
                isselected = false;
                buf_ptn_numb = existingPtnNumb;
                Intent i = new Intent(colptn.this, colcreate.class);
                startActivity(i);

                break;
            case R.id.imbtnCrefresh:
                readExcel(Cfilename);
                msg("Total pattern number is: " + Integer.toString(existingPtnNumb));
                if(isback){
                    for (int j = 0; j < buf_ptn_numb; j++) {
                        mylist.remove(0);
                    }
                    isback = false;
                }else{
                    for (int j = 0; j < existingPtnNumb; j++) {
                        mylist.remove(0);
                    }
                }
                etname.setText("");
                tvcode.setText("");
                tvbtnemot.setText("Click to set emotion");
                isselected = false;
                initptn();
                break;

        }

    }

    public void initptn(){
//        msg(Integer.toString(existingPtnNumb));
        for(int i = 0; i<existingPtnNumb;i++){
            mylist.add(new savedptn_class(
                    myptn_name[i],myptn_type[i],myptn_emot[i],myptn_code[i],
                    myptn_times[i],
                    shappy[i],ssad[i],sangry[i],scontempt[i],
                    ssuprise[i],sdisgust[i],sfear[i],snod[i],sshake[i],
//                    "1","2","3","4",5,6,7,8,9,10,11,12,13,14,
                    R.drawable.ic_media_embed_play));
        }
        myadapter = new savedptn_adapter(mylist);
        listView  = (ListView)findViewById(R.id.lvC);
        listView.setAdapter(myadapter);
    }

    public void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
