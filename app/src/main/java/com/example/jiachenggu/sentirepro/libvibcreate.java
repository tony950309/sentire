package com.example.jiachenggu.sentirepro;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.WriteException;

public class libvibcreate extends AppCompatActivity {

    EditText etLVCptncode;
    Button btnsaveas,btnsave,btnclear;
    ImageView imbtnplay;
    ImageButton imbtnLVChelp;
    GridView gridView;
    TextView tveffect,tvemot;
    LVCgvadapter GVAdapter;
    private Context mContext;
    String effectSequence = "";
    String current_ptncode;
    boolean issetemot = false;
    boolean isvalid = true;
    String[] dialogemot = new String[]{"Happy","Sad","Angry","Contempt","Surprised",
            "Disgust","Fear","Nod","Shake"};
    Dialog dialog;


    private String LVfilename = "LVptn.xls";    //LV : library vibration file name

    /******************* Read /write excel ************************/
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
            WritableSheet sheet = book.createSheet("My library vibration patterns", 0);

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
        setContentView(R.layout.activity_libvibcreate);
        setTitle("                              Create Vibration Patterns");


        btnsaveas  = (Button) findViewById(R.id.btnLVCsaveas);
        btnsave    = (Button) findViewById(R.id.btnLVCsave);
        btnclear   = (Button) findViewById(R.id.btnLVCclear);
        imbtnplay  = (ImageView)findViewById(R.id.imbtnLVCplay);
        imbtnLVChelp=(ImageButton)findViewById(R.id.imbtnLVChelp);
        tveffect   = (TextView)findViewById(R.id.tvLVCeffect) ;
        tvemot     = (TextView)findViewById(R.id.tvLVCemot);
        etLVCptncode = (EditText) findViewById(R.id.etCCptncode);
        etLVCptncode.setKeyListener(null);


        // read excel file
        readExcel(LVfilename);
        ptn_numb = existingPtnNumb;
        ptn_type = "LV";
        // initialize pattern code
        if(libvibptn.isedit){
            current_ptncode = myptn_code[libvibptn.editptnnumb - 1 ];
            ptn_emot = myptn_emot[libvibptn.editptnnumb - 1 ];
        }else
        {
            current_ptncode = "PL";  // P : play    LV: library vibration pattern
            ptn_emot = "Click to select the emotion for this pattern";
        }
        etLVCptncode.setText(current_ptncode);
        tvemot.setText(ptn_emot);

        // set gridView
        gridView=(GridView)findViewById(R.id.gvLVC);
        mContext = this;
        // Create the Custom Adapter Object
        GVAdapter = new LVCgvadapter(mContext);
        // Set the Adapter to GridView
        gridView.setAdapter(GVAdapter);
        // Handling touch/click Event on GridView Item

        // create pattern
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (effectSequence.isEmpty()) {
                    effectSequence  += String.valueOf(position);
                } else {
                    effectSequence  += ("," + String.valueOf(position));
                }
                current_ptncode += (String.valueOf(position) + ",");
                if(current_ptncode.length() > 30){
                    isvalid = false;
                }else{
                    isvalid = true;
                }
                if(isvalid){
                    tveffect.setText(effectSequence);
                    etLVCptncode.setText(current_ptncode);
                }else{
                    msg("Invalid drawing! The sequence might be too complex or too long to play!" + "\n"+
                            "Current code length is " + Integer.toString(current_ptncode.length()) +
                            ". Try to make it shorter than 30." + "\n" +
                            "Otherwise the sequence might not be played perfectly.");
                }
            }
        });
        // set emotion
        tvemot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(libvibcreate.this);
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
            }
        });

        //save
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(libvibptn.isedit && isvalid){
                    AlertDialog.Builder builder4 = new AlertDialog.Builder(libvibcreate.this);
                    builder4.setTitle("Overwrite Confirm"); //title
                    builder4.setMessage("Save will overwrite the previous pattern! Are you still want to overwrite this pattern?"); //message
                    builder4.setPositiveButton("Save", new DialogInterface.OnClickListener() { //设置确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myptn_code[libvibptn.editptnnumb - 1 ] = current_ptncode;
                            myptn_emot[libvibptn.editptnnumb - 1 ] = tvemot.getText().toString();
                            dialog.dismiss();
                            writeExcel(LVfilename);
                            issetemot = false;
                            libvibptn.isedit = false;
                            current_ptncode = "PL";  // P : play    L: library vibration pattern
                            ptn_emot = "";
                            effectSequence = "";
                            tveffect.setText(effectSequence);
                            etLVCptncode.setText(current_ptncode);
                            tvemot.setText("Click to select the emotion for this pattern");
                            ptn_name = "";
                            ptn_code = "";
                            
                            msg("Pattern Saved! ");
                        }
                    });
                    builder4.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //设置取消按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder4.create().show();
                }else if(effectSequence.isEmpty()){
                    msg("Create your Library Vibration pattern sequence first!");
                }else if(!issetemot){
                    msg("Set your pattern emotion first!");
                }else if (isvalid){
                    dialog = new Dialog(libvibcreate.this);
                    final View view = LayoutInflater.from(libvibcreate.this).inflate(R.layout.dialog_ptnname,null);
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
                            readExcel(LVfilename);
                            ptn_code = current_ptncode;
                            ptn_numb = existingPtnNumb + 1;
                            myptn_numb[ptn_numb - 1] = ptn_numb;
                            myptn_name[ptn_numb - 1] = ptn_name;
                            myptn_type[ptn_numb - 1] = ptn_type;
                            myptn_emot[ptn_numb - 1] = ptn_emot;
                            myptn_code[ptn_numb - 1] = ptn_code;
                            writeExcel(LVfilename);
                            msg("Pattern Saved! Total patterns ：" + ptn_numb);
                            // initialize pattern code
                            issetemot = false;
                            libvibptn.isedit = false;
                            libvibptn.isback = true;
                            current_ptncode = "PL";  // P : play    LV: library vibration pattern
                            ptn_emot = "";
                            effectSequence = "";
                            tveffect.setText(effectSequence);
                            etLVCptncode.setText(current_ptncode);
                            tvemot.setText("Click to select the emotion for this pattern");
                            ptn_name = "";
                            ptn_code = "";
                            dialog.dismiss();
                        }
                    }); /********view. is important otherwise it would collapse***********//****************************//****************************//****************************/
                    dialog.setTitle("Pattern Name");
                    dialog.setContentView(view);
//                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                params.width = 900;
//                dialog.getWindow().setAttributes(params);
//                dialog.setCancelable(false);
                    dialog.show();

                }else{
                    msg("Invalid pattern!");
                }

                libvibptn.isedit = false;
            }
        });

        //save as
        btnsaveas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(libvibptn.isedit && isvalid){
                    issetemot = true;
                    ptn_emot = tvemot.getText().toString();
                }
                if(effectSequence.isEmpty()){
                    msg("Create your Library Vibration pattern sequence first!");
                }else if(!issetemot){
                    msg("Set your pattern emotion first!");
                }else if(isvalid){
                    dialog = new Dialog(libvibcreate.this);
                    final View view = LayoutInflater.from(libvibcreate.this).inflate(R.layout.dialog_ptnname,null);
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
                            readExcel(LVfilename);
                            ptn_code = current_ptncode;
                            ptn_numb = existingPtnNumb + 1;
                            myptn_numb[ptn_numb - 1] = ptn_numb;
                            myptn_name[ptn_numb - 1] = ptn_name;
                            myptn_type[ptn_numb - 1] = ptn_type;
                            myptn_emot[ptn_numb - 1] = ptn_emot;
                            myptn_code[ptn_numb - 1] = ptn_code;
                            writeExcel(LVfilename);
                            msg("Pattern Saved! Total patterns ：" + ptn_numb);
                            // initialize pattern code
                            issetemot = false;
                            libvibptn.isedit = false;
                            libvibptn.isback = true;
                            current_ptncode = "PL";  // P : play    LV: library vibration pattern
                            ptn_emot = "";
                            effectSequence = "";
                            tveffect.setText(effectSequence);
                            etLVCptncode.setText(current_ptncode);
                            tvemot.setText("Click to select the emotion for this pattern");
                            ptn_name = "";
                            ptn_code = "";
                            dialog.dismiss();
                        }
                    }); /********view. is important otherwise it would collapse***********//****************************//****************************//****************************/
                    dialog.setTitle("Pattern Name");
                    dialog.setContentView(view);
//                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                params.width = 900;
//                dialog.getWindow().setAttributes(params);
//                dialog.setCancelable(false);
                    dialog.show();

                }else{
                    msg("Invalid pattern!");
                }
                libvibptn.isedit = false;
            }
        });

        //clear
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialize pattern code
                if(libvibptn.isedit){
                    current_ptncode = myptn_code[libvibptn.editptnnumb - 1 ];
                    ptn_emot = myptn_emot[libvibptn.editptnnumb - 1 ];
                }else
                {
                    current_ptncode = "PL";  // P : play    LV: library vibration pattern
                    ptn_emot = "Click to select the emotion for this pattern";
                }
                effectSequence = "";
                tveffect.setText(effectSequence);
                etLVCptncode.setText(current_ptncode);
                tvemot.setText(ptn_emot);
                ptn_name = "";
                ptn_code = "";
                issetemot = false;
            }
        });
        imbtnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isvalid) {
                    BTsend(current_ptncode);
                }else{
                    msg("Invalid pattern!");
                }
            }
        });
        imbtnLVChelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(libvibcreate.this);
                builder1.setTitle("Help"); //title
                builder1.setMessage(
                    "Choose the showed vibration type, click on it to add to the sequence, you can " +
                    "play the current sequence by clicking the blue play button." + "\n" + "\n" +
                    "To set the emotion, just click the button on the right." + "\n" + "\n" +
                    " 'Save as' and 'save' button make no difference when create new pattern," +
                    "but when editing existing pattern, 'save' button will overwrite the original" +
                    "pattern, 'save as' button will save as a new pattern."
                );
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.create().show();
            }
        });
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
//    private String[] mEffectNames = {
//            "Strong Click - 100%", "Strong Click - 60%", "Strong Click - 30%",
//            "Sharp Click -100%", "Sharp Click - 60%", "Sharp Click - 30%",
//            "Soft Bump - 100%", "Soft Bump - 60%", "Soft Bump - 30%",
//            "Double Click - 100%", "Double Click - 60%", "Triple Click - 100%",
//            "Soft Fuzz - 60%", "Strong Buzz - 100%",
//            "750ms Alert - 100%", "1000ms Alert - 100%",
//            "Strong Click 1 - 100%", "Strong Click 2 - 80%", "Strong Click 3 - 60%", "Strong Click 4 - 30%",
//            "Medium Click 1 - 100%", "Medium Click 2 - 80%", "Medium Click 3 - 60%",
//            "Sharp Tick 1 - 100%", "Sharp Tick 2 - 80%", "Sharp Tick 3 - 60%",
//            "Short Double Click Strong 1 - 100%", "Short Double Click Strong 2 - 80%",
//            "Short Double Click Strong 3 - 60%", "Short Double Click Strong 4 - 30%",
//            "Short Double Click Medium 1 - 100%", "Short Double Click Medium 2 - 80%", "Short Double Click Medium3 60%",
//            "Short Double Sharp Tick 1 - 100%", "Short Double Sharp Tick2 80%", "Short Double Sharp Tick3 60%",
//            "Long Double Click Strong 1 - 100%", "Long Double Click Strong 2 - 80%",
//            "Long Double Click Strong 3 - 60%", "Long Double Click Strong 4 - 30%",
//            "Long Double Click Medium 1 - 100%", "Long Double Click Medium 2 - 80%", "Long Double Click Medium 3 - 60%",
//            "Long Double Sharp Tick 1 - 100%", "Long Double Sharp Tick 2 - 80%", "Long Double Sharp Tick 3 - 60%",
//            "Buzz 1 100%", "Buzz 2 - 80%", "Buzz 3 - 60%", "Buzz 4 - 40%", "Buzz 5 - 20%",
//            "Pulsing Strong 1 - 100%", "Pulsing Strong 2 - 60%",
//            "Pulsing Medium 1 - 100%", "Pulsing Medium 2 - 60%",
//            "Pulsing Sharp 1 - 100%", "Pulsing Sharp 2 - 60%",
//            "Transition Click 1 - 100%", "Transition Click2 80%", "Transition Click 3 -60%",
//            "Transition Click 4 - 40%", "Transition Click5 20%", "Transition Click 6 - 10%",
//            "Transition Hum 1 - 100%", "Transition Hum2 80%", "Transition Hum 3 - 60%",
//            "Transition Hum 4 - 40%", "Transition Hum5 20%", "Transition Hum 1 - 10%",
//            "Transition Ramp Down Long Smooth 1 - 100 to 0%", "Transition Ramp Down Long Smooth 2 - 100 to 0%",
//            "Transition Ramp Down Medium Smooth 1 - 100 to 0%", "Transition Ramp Down Medium Smooth 2 -100 to 0%",
//            "Transition Ramp Down Short Smooth 1 - 100 to 0%", "Transition Ramp Down Short Smooth 2 - 100 to 0%",
//            "Transition Ramp Down Long Sharp 1 - 100 to 0%", "Transition Ramp Down Long Sharp 2 - 100 to 0%",
//            "Transition Ramp Down Medium Sharp 1 - 100 to 0%", "Transition Ramp Down Medium Sharp 2 - 100 to 0%",
//            "Transition Ramp Down Short Sharp 1 - 100 to 0%", "Transition Ramp Down Short Sharp 2 - 100 to 0%",
//            "Transition Ramp Up Long Smooth 1 - 0 to 100%", "Transition Ramp Up Long Smooth 2 - 0 to 100%",
//            "Transition Ramp Up Medium Smooth 1 - 0 to 100%", "Transition Ramp Up Medium Smooth 2 - 0 to 100%",
//            "Transition Ramp Up Short Smooth 1 - 0 to 100%", "Transition Ramp Up Short Smooth 2 - 0 to 100%",
//            "Transition Ramp Up Long Sharp 1 - 0 to 100%", "Transition Ramp Up Long Sharp 2 - 0 to 100%",
//            "Transition Ramp Up Medium Sharp 1 - 0 to 100%", "Transition Ramp Up Medium Sharp 2 - 0 to 100%",
//            "Transition Ramp Up Short Sharp 1 - 0 to 100%", "Transition Ramp Up Short Sharp 2 - 0 to 100%",
//            "Transition Ramp Down Long Smooth 1 - 50 to 0%", "Transition Ramp Down Long Smooth 2 - 100 to 0%",
//            "Transition Ramp Down Medium Smooth 1 - 50 to 0%", "Transition Ramp Down Medium Smooth 2 - 100 to 0%",
//            "Transition Ramp Down Short Smooth 1 - 50 to 0%", "Transition Ramp Down Short Smooth 2 - 100 to 0%",
//            "Transition Ramp Down Long Sharp 1 - 50 to 0%", "Transition Ramp Down Long Sharp 2 - 100 to 0%",
//            "Transition Ramp Down Medium Sharp 1 - 50 to 0%", "Transition Ramp Down Medium Sharp 2 - 100 to 0%",
//            "Transition Ramp Down Short Sharp 1 - 50 to 0%", "Transition Ramp Down Short Sharp 2 - 100 to 0%",
//            "Transition Ramp Up Long Smooth 1 - 0 to 50%", "Transition Ramp Up Long Smooth 2 - 0 to 50%",
//            "Transition Ramp Up Medium Smooth 1 - 0 to 50%", "Transition Ramp Up Medium Smooth 2 - 0 to 50%",
//            "Transition Ramp Up Short Smooth 1 - 0 to 50%", "Transition Ramp Up Short Smooth 2 - 0 to 50%",
//            "Transition Ramp Up Long Sharp 1 - 0 to 50%", "Transition Ramp Up Long Sharp 2 - 0 to 50%",
//            "Transition Ramp Up Medium Sharp 1 - 0 to 50%", "Transition Ramp Up Medium Sharp 2 - 0 to 50%",
//            "Transition Ramp Up Short Sharp 1 - 0 to 50%", "Transition Ramp Up Short Sharp 2 - 0 to 50%",
//            "Long Buzz for Programmatic Stopping - 100%",
//            "Smooth Hum 1 (No kick or brake pulse) - 50%",
//            "Smooth Hum 2 (No kick or brake pulse) - 40%",
//            "Smooth Hum 3 (No kick or brake pulse) - 30%",
//            "Smooth Hum 4 (No kick or brake pulse) - 20%",
//            "Smooth Hum 5 (No kick or brake pulse) - 10%",
//    };
}
