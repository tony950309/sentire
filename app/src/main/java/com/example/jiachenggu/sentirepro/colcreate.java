package com.example.jiachenggu.sentirepro;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.WriteException;
import java.io.IOException;

public class colcreate extends AppCompatActivity implements View.OnClickListener {
    Button btnadd,btnsave,btnclear;
    ImageView imbtnplay;
    ImageButton imbtnhelp;
    EditText etcode;
    TextView tvemot;
    private SeekBar sbDuration,sbswitching;
    private TextView tvDuration,tvswitching;

    String current_ptncode = "PC";
    boolean issetemot = false;
    boolean isvalid = true;
    String[] dialogemot = new String[]{"Happy","Sad","Angry","Contempt","Surprised",
            "Disgust","Fear","Nod","Shake"};
    Dialog dialog;

    private PieChart mPieChart;
    private String mValue = "";
    private int mColor = 0xffffffff;
    private int mNumber = 0;
    private int mDuration = 0;
    private int mSwitchingtime = 0;

    /******************* Read /write excel ************************/
    private String Cfilename = "Cptn.xls";    //C : color pattern file name
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
    /*******************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colcreate);

        btnadd   = (Button) findViewById(R.id.btnCCadd);
        btnsave  = (Button) findViewById(R.id.btnCCsave);
        btnclear = (Button) findViewById(R.id.btnCCclear);
        imbtnplay= (ImageView) findViewById(R.id.imbtnCCplay);
        imbtnhelp= (ImageButton) findViewById(R.id.imbtnCChelp);
        etcode   = (EditText) findViewById(R.id.etCCptncode);
        tvemot   = (TextView) findViewById(R.id.tvCCemot);
        mPieChart = (PieChart) findViewById(R.id.piechartCC);
        etcode.setKeyListener(null);

        btnadd.setOnClickListener(this);
        btnsave.setOnClickListener(this);
        btnclear.setOnClickListener(this);
        imbtnplay.setOnClickListener(this);
        imbtnhelp.setOnClickListener(this);
        tvemot.setOnClickListener(this);

        //initialize
        etcode.setText(current_ptncode);
        tvemot.setText("Click to select the emotion for this pattern");
        ptn_type = "C";
        mPieChart.setInnerValueUnit("ms");

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imbtnCChelp:  //help dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(colcreate.this);
                builder1.setTitle("Help"); //title
                builder1.setMessage( "Click 'add colors' button to add colors, you can choose" +
                        "the duration (how long this color last for), and switching time(time for " +
                        "this color to transit to next color. " + "\n" + "\n" + "For the last color in a pattern," +
                        "switching time will not work."+ "\n" + "\n" +" We would advice you to add less than 3 color for" +
                        " each pattern and each pattern last for less than 4.5 seconds.");
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.create().show();

                break;

            case R.id.btnCCadd:     //add colours
                ColorPickerDialogBuilder
                    .with(colcreate.this)
                    .setTitle("Choose color")
                    .initialColor(mColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setPositiveButton("ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int selectedColor, Integer[] allColors) {
                            mColor = selectedColor;

                            AlertDialog.Builder builder = new AlertDialog.Builder(colcreate.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View v = inflater.inflate(R.layout.mduration, null);
                            builder.setView(v)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int r = (mColor >> 16) & 0xFF;
                                            int g = (mColor >> 8) & 0xFF;
                                            int b = (mColor >> 0) & 0xFF;
                                            mValue += (new Integer(r)).toString() + ",";
                                            mValue += (new Integer(g)).toString() + ",";
                                            mValue += (new Integer(b)).toString() + ",";
                                            mValue += (new Integer(mDuration).toString()) + ",";
                                            mValue += (new Integer(mSwitchingtime).toString()) +",#";
                                            mNumber += 1;
                                            mPieChart.addPieSlice(new PieModel((new Integer(mNumber)).toString(), mDuration, mColor));
                                            current_ptncode += mValue;
                                            mValue = "";
                                            if(current_ptncode.length() > 90){
                                                isvalid = false;
                                            }else{
                                                isvalid = true;
                                            }
                                            if(isvalid){
                                                etcode.setText(current_ptncode);
                                            }else{
                                                msg("Invalid drawing! The curve might be too complex or too long!" + "\n"+
                                                        "Current code length is " + Integer.toString(current_ptncode.length()) +
                                                        ". Try to make it shorter than 90.");
                                            }
                                            mDuration = 0;
                                            mSwitchingtime = 0;
                                        }
                                    });
//                                    .setTitle("Duration");
                            /*************** duration*****/
                            tvDuration = (TextView)v.findViewById(R.id.tvDuration);
                            sbDuration = (SeekBar)v.findViewById(R.id.sbDuration);
                            sbDuration.setMax(2000);
                            sbDuration.setProgress(0);
                            sbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress,
                                                              boolean fromUser) {
                                    // TODO Auto-generated method stub
                                    mDuration = progress;
                                    tvDuration.setText((new Integer(mDuration)).toString()+"ms");
                                }
                            });
                            /*************** switching time*****/

                            tvswitching= (TextView)v.findViewById(R.id.tvswitching);
                            sbswitching = (SeekBar)v.findViewById(R.id.sbswitching);
                            sbswitching.setMax(1600);
                            sbswitching.setProgress(0);
                            sbswitching.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress,
                                                              boolean fromUser) {
                                    // TODO Auto-generated method stub
                                    mSwitchingtime = progress;
                                    tvswitching.setText((new Integer(mSwitchingtime)).toString()+"ms");
                                }
                            });
                            builder.create();
                            builder.show();

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .showColorEdit(false)
                    .build()
                    .show();

                break;

            case R.id.imbtnCCplay:  //ptn the ptn
                if(isvalid) {
                    BTsend(current_ptncode);
                }else{
                    msg("Invalid pattern!");
                }
            break;

            case R.id.tvCCemot:     //set the emotion for color ptn
                AlertDialog.Builder builder = new AlertDialog.Builder(colcreate.this);
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

            case R.id.btnCCsave:    //save this ptn
                if(current_ptncode.equals("PC")){
                    msg("Add colors first!");
                }else if(!issetemot){
                    msg("Choose the emotion for this pattern first!");
                }else if(isvalid){
                    dialog = new Dialog(colcreate.this);
                    final View view = LayoutInflater.from(colcreate.this).inflate(R.layout.dialog_ptnname,null);
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
                            readExcel(Cfilename);
                            ptn_code = current_ptncode;
                            ptn_numb = existingPtnNumb + 1;
                            myptn_numb[ptn_numb - 1] = ptn_numb;
                            myptn_name[ptn_numb - 1] = ptn_name;
                            myptn_type[ptn_numb - 1] = ptn_type;
                            myptn_emot[ptn_numb - 1] = ptn_emot;
                            myptn_code[ptn_numb - 1] = ptn_code;
                            writeExcel(Cfilename);
                            msg("Pattern Saved! Total patterns ：" + ptn_numb);
                            // initialize pattern code
                            issetemot = false;
                            colptn.isback = true;
                            current_ptncode = "PC";
                            ptn_emot = "";
                            etcode.setText(current_ptncode);
                            tvemot.setText("Click to select the emotion for this pattern");
                            mPieChart.setInnerValueUnit("ms");
                            ptn_name = "";
                            ptn_code = "";
                            mNumber = 0;
                            mValue = "";
                            mPieChart.clearChart();
                            mPieChart.startAnimation();

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

            case R.id.btnCCclear:   //clear designed ptn
                // initialize pattern code
                issetemot = false;
                current_ptncode = "PC";
                ptn_emot = "";
                etcode.setText(current_ptncode);
                tvemot.setText("Click to select the emotion for this pattern");
                mPieChart.setInnerValueUnit("ms");
                ptn_name = "";
                ptn_code = "";
                mNumber = 0;
                mValue = "";
                mPieChart.clearChart();
                mPieChart.startAnimation();

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
