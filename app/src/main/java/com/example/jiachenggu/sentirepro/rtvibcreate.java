package com.example.jiachenggu.sentirepro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.WriteException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class rtvibcreate extends AppCompatActivity implements View.OnClickListener {

    Button btnsave,btnclear;
    ImageView imbtnplay;
    ImageButton imbtnhelp;
    TextView tvemot,tvcode;

    String current_ptncode = "PR";
    boolean issetemot = false;
    String[] dialogemot = new String[]{"Happy","Sad","Angry","Contempt","Surprised",
            "Disgust","Fear","Nod","Shake"};
    Dialog dialog;
    private String mValue = "";
    private ActionBar mActionBar;
    private boolean isfinish = true;
    private boolean isvalid = true;
    private Context mContext;


    private List<Integer> amplitudes = new ArrayList<Integer>();
    private MyPaintView myPaintView;
    private ValueLineChart mCubicValueLineChart;

    /******************* Read /write excel ************************/
    private String RTVfilename = "RTVptn.xls";
    int ptn_numb = 0;
    String ptn_name,ptn_type,ptn_emot,ptn_code;
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
            WritableSheet sheet = book.createSheet("My Real-time vibration patterns", 0);

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
    /*******************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtvibcreate);

        btnsave  = (Button) findViewById(R.id.btnRVCsave);
        btnclear = (Button) findViewById(R.id.btnRVCclear);
        imbtnplay= (ImageView) findViewById(R.id.imbtnRVCplay);
        imbtnhelp= (ImageButton) findViewById(R.id.imbtnRVChelp);
        tvcode   = (TextView) findViewById(R.id.tvRVCptncode);
        tvemot   = (TextView) findViewById(R.id.tvRVCemot);

        btnsave.setOnClickListener(this);
        btnclear.setOnClickListener(this);
        imbtnplay.setOnClickListener(this);
        imbtnhelp.setOnClickListener(this);
        tvemot.setOnClickListener(this);

        mContext = this;
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        // draw board   paint view
        myPaintView = (MyPaintView) findViewById(R.id.paintView);
        mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);
        mCubicValueLineChart.setUseDynamicScaling(false);
        mCubicValueLineChart.setMaxZoomY(255);
        mCubicValueLineChart.setIndicatorTextColor(getResources().getColor(R.color.colorPrimaryDark));

        //initialize
        mCubicValueLineChart.clearChart();
        issetemot = false;
        isfinish = true;
        myPaintView.clearPoints();
        myPaintView.invalidate();
        current_ptncode = "PR";
        ptn_emot = "";
        ptn_name = "";
        ptn_code = "";
        mValue   = "";
        tvcode.setText(current_ptncode);
        tvemot.setText("Click to select the emotion for this pattern");
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imbtnRVChelp:  //help dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(rtvibcreate.this);
                builder1.setTitle("Help"); //titley
                builder1.setMessage(
                        "Sketch your own curve on the white drawboard." + "\n" + "\n" +
                        "Click play button to see how the pattern looks like , and of course," +
                        "play it on the wristband" + "\n" + "\n" +"After selecting the emotion for the " +
                        "new pattern, you can save it.");
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.create().show();

                break;
            case R.id.imbtnRVCplay :
                if(isfinish) {
                    mValue = "";
                    mCubicValueLineChart.clearChart();
                    amplitudes = myPaintView.getAmplitudes(50);
                    amplitudes.add(0, 0);
                    amplitudes.add(0);

                    ValueLineSeries series = new ValueLineSeries();
                    series.setColor(getResources().getColor(R.color.colorAccent));

                    for(int i=0; i < amplitudes.size(); i = i + 3) {
                        mValue += amplitudes.get(i).toString()+",";
                        series.addPoint(new ValueLinePoint(((new Integer(i*40)).toString()+"ms"), amplitudes.get(i)));
                    }
                    mCubicValueLineChart.addSeries(series);
                    mCubicValueLineChart.startAnimation();
                    current_ptncode += mValue;
                    isfinish = false;
                    if(current_ptncode.length() > 130){
                        isvalid = false;
                    }else{
                        isvalid = true;
                    }
                }
                if(isvalid){
                    tvcode.setText(current_ptncode);
                    BTsend(current_ptncode);
                }else{
                    msg("Invalid drawing! The curve might be too complex or too long!" + "\n"+
                        "Current code length is " + Integer.toString(current_ptncode.length()) +
                        ". Try to make it shorter than 130.");
                    tvcode.setText(current_ptncode);
                }

                break;
            case R.id.tvRVCemot :     //set the emotion for realtime vib ptn
                AlertDialog.Builder builder = new AlertDialog.Builder(rtvibcreate.this);
                builder.setTitle("Select the emotion"); //title
                builder.setSingleChoiceItems(dialogemot,1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvemot.setText(dialogemot[which]);
                                ptn_emot = dialogemot[which];
                                issetemot = true;
                            }
                        });
                builder.setPositiveButton("YES",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();


                break;
            case R.id.btnRVCsave :
                if(current_ptncode.equals("PR")){
                    msg("Sketch your curve first!");
                }else if(!issetemot){
                    msg("Choose the emotion for this pattern first!");
                }else if(isvalid){
                    dialog = new Dialog(rtvibcreate.this);
                    final View view = LayoutInflater.from(rtvibcreate.this).inflate(R.layout.dialog_ptnname,null);
                    //set onclick listener for btn in dialog
                    view.findViewById(R.id.btndialogcancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    view.findViewById(R.id.btndialogconf).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText editText = (EditText) view.findViewById(R.id.etdialogname);
                            ptn_name = editText.getText().toString();
                            readExcel(RTVfilename);
                            ptn_code = current_ptncode;
                            ptn_numb = existingPtnNumb + 1;
                            myptn_numb[ptn_numb - 1] = ptn_numb;
                            myptn_name[ptn_numb - 1] = ptn_name;
                            myptn_type[ptn_numb - 1] = ptn_type;
                            myptn_emot[ptn_numb - 1] = ptn_emot;
                            myptn_code[ptn_numb - 1] = ptn_code;
                            writeExcel(RTVfilename);
                            msg("Pattern Saved! Total patterns ：" + ptn_numb);
                            // initialize pattern code
                            rtvibptn.isback = true;
                            issetemot = false;
                            isfinish  = true;
                            myPaintView.clearPoints();
                            myPaintView.invalidate();
                            mCubicValueLineChart.clearChart();
                            current_ptncode = "PR";
                            ptn_emot = "";
                            ptn_name = "";
                            ptn_code = "";
                            mValue   = "";
                            tvcode.setText(current_ptncode);
                            tvemot.setText("Click to select the emotion for this pattern");

                            dialog.dismiss();
                        }
                    }); /********view. is important otherwise it would collapse***********//****************************//****************************//****************************/
                    dialog.setTitle("Pattern Name");
                    dialog.setContentView(view);
                    dialog.show();
                }else{
                    msg("Invalid pattern!");
                }

                break;
            case R.id.btnRVCclear :   //clear designed ptn
                // initialize pattern code
                mCubicValueLineChart.clearChart();
                issetemot = false;
                isfinish  = true;
                myPaintView.clearPoints();
                myPaintView.invalidate();
                current_ptncode = "PR";
                ptn_emot = "";
                ptn_name = "";
                ptn_code = "";
                mValue   = "";
                tvcode.setText(current_ptncode);
                tvemot.setText("Click to select the emotion for this pattern");
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
