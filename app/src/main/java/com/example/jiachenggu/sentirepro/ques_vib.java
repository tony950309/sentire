package com.example.jiachenggu.sentirepro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ques_vib extends AppCompatActivity implements View.OnClickListener {

    TextView tvintro,tvptnnumb;
    Button btnhap,btnsad,btnang,btncon,btnsup,btndis,btnfea,btnnod,btnsha,btnsave,btnnext;
    ImageView ivplay;
    String intro1 = "In this section, you are supposed to choose the emotion that matches the " +
            "played vibration pattern best. Press the play button below to play the vibration pattern " +
            "on the wristband and choose the emotion, then press the pattern number to go to " +
            "the next pattern. You should go through at least 16 vibration " +
            "patterns, then you can keep testing vibration pattern or click HERE to switch to " +
            "color patterns." + "" +
            "\n" + "Click on the pattern number below to moving on to next pattern";
    String intro2 = "Now you are supposed to choose the emotion that matches the " +
            "played color pattern best. Press the play button below to play the color pattern " +
            "on the wristband and choose the emotion, then press the pattern number to go to " +
            "the next pattern. You should go through at least 10 color " +
            "patterns, then you can click SAVE QUESTIONNAIRE AND EXIT " +
            "button to finish this questionnaire, or click NEXT(OPTIONAL) button to start section two." + "" +
            "\n" + "Click on the pattern number below to moving on to next pattern";

    int curquesnumb = 0;   //which guys is taking this questionnaire
    int ttlvibnumb;    //total vibration pattern number
    int curvibnumb;
    int curLVnumb,curRTVnumb,curCnumb;
    int lastLVnumb,lastRTVnumb,lastCnumb;
    int playLV,playRTV,playC;
    boolean isLV,isRTV,isC;
    boolean issetemot,isplayed;
    boolean isVfinish,isCfinish;
//    String nationality,age,gender,disability;
    public String[] ques1emot = new String[999];
    public String[] ques1type = new String[999];
    public String[] ques1code = new String[999];
    String curemot;
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
    private int readQnumb(){
        int n = 0;
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"QuestionnaireNumber.xls");
            Workbook workbook = Workbook.getWorkbook(file);
            //
            Sheet sheet = workbook.getSheet(0);
            //  get total number of existing ptn numbs
            Cell cell = sheet.getCell(0, 0);
            n = Integer.valueOf(cell.getContents()).intValue();
            cell = sheet.getCell(1,0);
            lastLVnumb = Integer.valueOf(cell.getContents()).intValue();
            cell = sheet.getCell(2,0);
            lastRTVnumb = Integer.valueOf(cell.getContents()).intValue();
            cell = sheet.getCell(3,0);
            lastCnumb = Integer.valueOf(cell.getContents()).intValue();

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return n;
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
            mynumb = new jxl.write.Number(1, 0, lastLVnumb);
            sheet.addCell(mynumb);
            mynumb = new jxl.write.Number(2, 0, lastRTVnumb);
            sheet.addCell(mynumb);
            mynumb = new jxl.write.Number(3, 0, lastCnumb);
            sheet.addCell(mynumb);


            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeQdetail(String filename){
        try {
            //  open the file
            File file=new File(Environment.getExternalStorageDirectory(),filename);
            //  create the sheet ,name it
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("Questionnaire Details", 0);
            //
            Label mystring = new Label(0, 0, "Total Pattern numbers :");
            sheet.addCell(mystring);
            mystring = new Label(1, 0, "Nationality :");
            sheet.addCell(mystring);
            mystring = new Label(2, 0, "Age :");
            sheet.addCell(mystring);
            mystring = new Label(3, 0, "Gender :");
            sheet.addCell(mystring);
            mystring = new Label(4, 0, "Disabilities :");
            sheet.addCell(mystring);
            //total ptn number
            jxl.write.Number mynumb = new jxl.write.Number(0, 1, curvibnumb + curCnumb);
            sheet.addCell(mynumb);
            // personal details
            mystring = new Label(1, 1, ques_info.quesNationality);
            sheet.addCell(mystring);
            mystring = new Label(2, 1, ques_info.quesAge);
            sheet.addCell(mystring);
            mystring = new Label(3, 1, ques_info.quesGender);
            sheet.addCell(mystring);
            mystring = new Label(4, 1, ques_info.quesDisability);
            sheet.addCell(mystring);

            // data labels
            mystring = new Label(0, 2, "Pattern type:");
            sheet.addCell(mystring);
            mystring = new Label(1, 2, "Pattern emotion:");
            sheet.addCell(mystring);
            mystring = new Label(2, 2, "Pattern code:");
            sheet.addCell(mystring);

            for(int row = 3 ; row <= curvibnumb + curCnumb + 2 ;row++ ){
                mystring = new Label( 0,row, ques1type[ row - 3 ]);
                sheet.addCell(mystring);
                mystring = new Label( 1,row, ques1emot[ row - 3 ]);
                sheet.addCell(mystring);
                mystring = new Label( 2,row, ques1code[ row - 3 ]);
                sheet.addCell(mystring);
            }

            book.write();
            book.close();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques_vib);
        setTitle("                               Questionnaire Section one");

        tvintro     = (TextView) findViewById(R.id.tvqintro);
        tvptnnumb   = (TextView) findViewById(R.id.tvqptnnumb);
        ivplay      = (ImageView)findViewById(R.id.ivqplay);
        btnhap      = (Button)   findViewById(R.id.btnqhap);
        btnsad      = (Button)   findViewById(R.id.btnqsad);
        btnang      = (Button)   findViewById(R.id.btnqang);
        btncon      = (Button)   findViewById(R.id.btnqcon);
        btnsup      = (Button)   findViewById(R.id.btnqsup);
        btndis      = (Button)   findViewById(R.id.btnqdis);
        btnfea      = (Button)   findViewById(R.id.btnqfea);
        btnnod      = (Button)   findViewById(R.id.btnqnod);
        btnsha      = (Button)   findViewById(R.id.btnqsha);

        btnsave     = (Button)   findViewById(R.id.btnqsave);
        btnnext     = (Button)   findViewById(R.id.btnqnext);

        tvintro.setOnClickListener(this);
        tvptnnumb.setOnClickListener(this);
        ivplay.setOnClickListener(this);
        btnhap.setOnClickListener(this);
        btnsad.setOnClickListener(this);
        btnang.setOnClickListener(this);
        btncon.setOnClickListener(this);
        btnsup.setOnClickListener(this);
        btndis.setOnClickListener(this);
        btnfea.setOnClickListener(this);
        btnnod.setOnClickListener(this);
        btnsha.setOnClickListener(this);
        btnsave.setOnClickListener(this);
        btnnext.setOnClickListener(this);

        // initialize
        curquesnumb = readQnumb() + 1;
        isLV = true;
        isRTV= false;
        isC  = false;
        isplayed = false;
        issetemot = false;
        curLVnumb = 1;
        curRTVnumb= 0;
        curCnumb  = 0;
        curvibnumb  = curLVnumb + curRTVnumb;
        // read saved patterns
        readLVExcel();
        ptn_numbLV  = existingPtnNumbLV;
        readRTVExcel();
        ptn_numbRTV = existingPtnNumbRTV;
        readCExcel();
        ptn_numbC   = existingPtnNumbC;

        ttlvibnumb  = existingPtnNumbLV + existingPtnNumbRTV;
        tvintro.setText(intro1);
        tvptnnumb.setText("Vibration Pattern # " +Integer.toString(curvibnumb)
                + " / " + Integer.toString(ttlvibnumb));




    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tvqintro:
                if(curvibnumb <= 16){
                    msg("You should test at least 16 vibration patterns before going on!");
                } else if (curvibnumb > 16 && !isCfinish){
                    tvintro.setText(intro2);
                    isC   = true;
                    isLV  = false;
                    isRTV = false;
                    isplayed = false;
                    curLVnumb  = 0;
                    curRTVnumb = 0;
                    curCnumb   = 1;
                    tvptnnumb.setText("Color Pattern # " +Integer.toString(curCnumb)
                            + " / " + Integer.toString(existingPtnNumbC));
                }else{
                    msg("You have finished all the patterns. " +
                            "Now you can click SAVE QUESTIONNAIRE AND EXIT " +
                            "button to finish this questionnaire, " +
                            "or click NEXT(OPTIONAL) button to start section two.");
                    isplayed = false;
                    isCfinish= true;
                }

                break;

            case R.id.ivqplay:
                if(isLV){
                    BTsend(myptn_codeLV[curLVnumb - 1]);
                    isplayed = true;
                }
                if(isRTV){
                    BTsend(myptn_codeRTV[curRTVnumb - 1]);
                    isplayed = true;
                }
                if(isC){
                    BTsend(myptn_codeC[curCnumb - 1]);
                    isplayed = true;
                }
                break;

            case R.id.tvqptnnumb:
                if(!issetemot){
                    msg("Choose your ideal emotion for this pattern!");
                }else if(isLV || isRTV){
                    /***
                     *   curvibnumb : n
                     *
                     *   odd: play (n+1)/2 LV ptn
                     *   even: play n/2 RTV ptn
                     *
                     *   when LV or RTV ptn are all played for one time, play the
                     *   another ptn until finish them all
                     *
                     *   logic loop : goto daybook 12/4/2017
                     */
                    if(curvibnumb < ttlvibnumb) {
                        if (curvibnumb % 2 == 0 && curvibnumb / 2 < existingPtnNumbLV) {
                            record();
                            isC   = false;
                            isLV  = true;
                            isRTV = false;
                            isplayed = false;
                            curLVnumb++;

                        }else if (curvibnumb % 2 == 1 && curvibnumb / 2 < existingPtnNumbRTV) {
                            record();
                            isC   = false;
                            isLV  = false;
                            isRTV = true;
                            isplayed = false;
                            curRTVnumb++;
                        }else if (curvibnumb / 2 >= existingPtnNumbLV) {
                            record();
                            isC   = false;
                            isLV  = false;
                            isRTV = true;
                            isplayed = false;
                            curRTVnumb++;
                        }else if (curvibnumb / 2 >= existingPtnNumbRTV) {
                            record();
                            isC   = false;
                            isLV  = true;
                            isRTV = false;
                            isplayed = false;
                            curLVnumb++;
                        }

                        curvibnumb = curLVnumb + curRTVnumb;
                        tvptnnumb.setText("Vibration Pattern # " +Integer.toString(curvibnumb)
                                + " / " + Integer.toString(ttlvibnumb));
                    }else{
                        record();
                        msg("You have test all the existing vibration patterns, " +
                            "you can go back to homepage and design your own " +
                            "library / real-time vibration patterns. Lest's go for " +
                            "color pattern!");
                        isC   = true;
                        isLV  = false;
                        isRTV = false;
                        isplayed = false;
                        isVfinish= true;
                        curLVnumb  = 0;
                        curRTVnumb = 0;
                        curCnumb   = 1;
                        tvptnnumb.setText("Color Pattern # " +Integer.toString(curCnumb)
                                + " / " + Integer.toString(existingPtnNumbC));
                        tvintro.setText(intro2);
                    }

                }else if (isC){
                    record();
                    if(curCnumb < existingPtnNumbC){
                        curCnumb ++;
                        tvptnnumb.setText("Color Pattern # " +Integer.toString(curCnumb)
                                + " / " + Integer.toString(existingPtnNumbC));
                        isplayed = false;
                    }else{
                        msg("You have finished all the patterns. " +
                                "Now you can click SAVE QUESTIONNAIRE AND EXIT " +
                                "button to finish this questionnaire, " +
                                "or click NEXT(OPTIONAL) button to start section two.");
                        isplayed = false;
                        isCfinish= true;
                    }
                }

                issetemot = false;

                break;

            case R.id.btnqhap:
                if(!isplayed) {
                    msg("Play the pattern before make your choice!");
                }else{
                    curemot = "Happy";
                    smsg("Happy selected");
                    issetemot = true;
                }
                break;

            case R.id.btnqsad:
                if(!isplayed) {
                    msg("Play the pattern before make your choice!");
                }else{
                    curemot = "Sad";
                    smsg("Sad selected");
                    issetemot = true;
                }
                break;

            case R.id.btnqang:
                if(!isplayed) {
                    msg("Play the pattern before make your choice!");
                }else{
                    curemot = "Angry";
                    smsg("Angry selected");
                    issetemot = true;
                }
                break;

            case R.id.btnqcon:
                if(!isplayed) {
                    msg("Play the pattern before make your choice!");
                }else{
                    curemot = "Contempt";
                    smsg("Contempt selected");
                    issetemot = true;
                }
                break;

            case R.id.btnqsup:
                if(!isplayed) {
                    msg("Play the pattern before make your choice!");
                }else{
                    curemot = "Surprised";
                    smsg("Surprised selected");
                    issetemot = true;
                }
                break;

            case R.id.btnqdis:
                if(!isplayed) {
                    msg("Play the pattern before make your choice!");
                }else{
                    curemot = "Disgust";
                    smsg("Disgust selected");
                    issetemot = true;
                }
                break;

            case R.id.btnqfea:
                if(!isplayed) {
                    msg("Play the pattern before make your choice!");
                }else{
                    curemot = "Fear";
                    smsg("Fear selected");
                    issetemot = true;
                }
                break;

            case R.id.btnqnod:
                if(!isplayed) {
                    msg("Play the pattern before make your choice!");
                }else{
                    curemot = "Nod";
                    smsg("Nod selected");
                    issetemot = true;
                }
                break;

            case R.id.btnqsha:
                if(!isplayed) {
                    msg("Play the pattern before make your choice!");
                }else{
                    curemot = "Shake";
                    smsg("Shake selected");
                    issetemot = true;
                }
                break;

            case R.id.btnqsave:
                if(isLV || isRTV){
                    if(curvibnumb <= 16){
                        msg("You have to go through at least 16 vibration patterns!");
                    }else{
                        msg("You have to go through color patterns!");
                    }
                }else if(isC && curCnumb <= 10){
                    msg("You have to go through at least 10 color patterns before finish the questionnaire!");
                }else {
                    if(!isCfinish){
                        isCfinish = true;
                        curCnumb--;
                    }
                    writeLVExcel();
                    writeRTVExcel();
                    writeCExcel();
                    lastLVnumb = playLV + 1;
                    lastRTVnumb= playRTV+ 1;
                    lastCnumb  = playC  + 1;
                    writeQnumb(curquesnumb);
                    writeQdetail("Questionnaire" + Integer.toString(curquesnumb) + ".xls");
                    isLV = true;
                    isRTV = false;
                    isC = false;
                    isplayed = false;
                    issetemot = false;
                    curLVnumb = 1;
                    curRTVnumb = 0;
                    curCnumb = 0;
                    curvibnumb = curLVnumb + curRTVnumb;
                    Intent i = new Intent(ques_vib.this, Homepage.class);
                    startActivity(i);
                    finishAfterTransition();
                }

                break;

            case R.id.btnqnext:
                if(isLV || isRTV){
                    if(curvibnumb <= 16){
                        msg("You have to go through at least 16 vibration patterns!");
                    }else{
                        msg("You have to go through color patterns!");
                    }
                }else if(isC && curCnumb <= 10){
                    msg("You have to go through at least 10 color patterns before finish this section!");
                }else {
                    if(!isCfinish){
                        isCfinish = true;
                        curCnumb--;
                    }
                    writeLVExcel();
                    writeRTVExcel();
                    writeCExcel();
                    lastLVnumb = playLV + 1;
                    lastRTVnumb= playRTV+ 1;
                    lastCnumb  = playC  + 1;
                    writeQnumb(curquesnumb);
                    writeQdetail("Questionnaire" + Integer.toString(curquesnumb) + ".xls");
                    isLV = true;
                    isRTV = false;
                    isC = false;
                    isplayed = false;
                    issetemot = false;
                    curLVnumb = 1;
                    curRTVnumb = 0;
                    curCnumb = 0;
                    curvibnumb = curLVnumb + curRTVnumb;
                    Intent i = new Intent(ques_vib.this, ques_compare.class);
                    startActivity(i);
                    finishAfterTransition();
                }


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
    private void record(){
        if(isLV){
            if(lastLVnumb + curLVnumb - 1 < existingPtnNumbLV){
                playLV = lastLVnumb + curLVnumb - 1;
            }else{
                playLV = lastLVnumb + curLVnumb - 1 - existingPtnNumbLV;
            }
            ques1emot[curvibnumb - 1] = curemot;
            ques1type[curvibnumb - 1] = "LV";
            ques1code[curvibnumb - 1] = myptn_codeLV[playLV];

            if(curemot.equals("Happy")){
                myptn_timesLV[playLV]++;
                shappyLV[playLV]++;
            }else if(curemot.equals("Sad")){
                myptn_timesLV[playLV]++;
                ssadLV[playLV]++;
            }else if(curemot.equals("Angry")){
                myptn_timesLV[playLV]++;
                sangryLV[playLV]++;
            }else if(curemot.equals("Contempt")){
                myptn_timesLV[playLV]++;
                scontemptLV[playLV]++;
            }else if(curemot.equals("Surprised")){
                myptn_timesLV[playLV]++;
                ssupriseLV[playLV]++;
            }else if(curemot.equals("Disgust")){
                myptn_timesLV[playLV]++;
                sdisgustLV[playLV]++;
            }else if(curemot.equals("Fear")){
                myptn_timesLV[playLV]++;
                sfearLV[playLV]++;
            }else if(curemot.equals("Nod")){
                myptn_timesLV[playLV]++;
                snodLV[playLV]++;
            }else if(curemot.equals("Shake")){
                myptn_timesLV[playLV]++;
                sshakeLV[playLV]++;
            }
        }
        if(isRTV){
            if(lastRTVnumb + curRTVnumb - 1 < existingPtnNumbRTV){
                playRTV = lastRTVnumb + curRTVnumb - 1;
            }else{
                playRTV = lastRTVnumb + curRTVnumb - 1 - existingPtnNumbRTV;
            }
            ques1emot[curvibnumb - 1] = curemot;
            ques1type[curvibnumb - 1] = "RTV";
            ques1code[curvibnumb - 1] = myptn_codeRTV[playRTV];
            if(curemot.equals("Happy")){
                myptn_timesRTV[playRTV]++;
                shappyRTV[playRTV]++;
            }else if(curemot.equals("Sad")){
                myptn_timesRTV[playRTV]++;
                ssadRTV[playRTV]++;
            }else if(curemot.equals("Angry")){
                myptn_timesRTV[playRTV]++;
                sangryRTV[playRTV]++;
            }else if(curemot.equals("Contempt")){
                myptn_timesRTV[playRTV]++;
                scontemptRTV[playRTV]++;
            }else if(curemot.equals("Surprised")){
                myptn_timesRTV[playRTV]++;
                ssupriseRTV[playRTV]++;
            }else if(curemot.equals("Disgust")){
                myptn_timesRTV[playRTV]++;
                sdisgustRTV[playRTV]++;
            }else if(curemot.equals("Fear")){
                myptn_timesRTV[playRTV]++;
                sfearRTV[playRTV]++;
            }else if(curemot.equals("Nod")){
                myptn_timesRTV[playRTV]++;
                snodRTV[playRTV]++;
            }else if(curemot.equals("Shake")){
                myptn_timesRTV[playRTV]++;
                sshakeRTV[playRTV]++;
            }
        }
        if(isC){
            if(!isVfinish){
                curvibnumb--;
                isVfinish = true;
            }
            if(lastCnumb + curCnumb - 1 < existingPtnNumbC){
                playC = lastCnumb + curCnumb - 1;
            }else{
                playC = lastCnumb + curCnumb - 1 - existingPtnNumbC;
            }
            ques1emot[curvibnumb + curCnumb - 1] = curemot;
            ques1type[curvibnumb + curCnumb - 1] = "C";
            ques1code[curvibnumb + curCnumb - 1] = myptn_codeC[playC];
            if(curemot.equals("Happy")){
                myptn_timesC[playC]++;
                shappyC[playC]++;
            }else if(curemot.equals("Sad")){
                myptn_timesC[playC]++;
                ssadC[playC]++;
            }else if(curemot.equals("Angry")){
                myptn_timesC[playC]++;
                sangryC[playC]++;
            }else if(curemot.equals("Contempt")){
                myptn_timesC[playC]++;
                scontemptC[playC]++;
            }else if(curemot.equals("Surprised")){
                myptn_timesC[playC]++;
                ssupriseC[playC]++;
            }else if(curemot.equals("Disgust")){
                myptn_timesC[playC]++;
                sdisgustC[playC]++;
            }else if(curemot.equals("Fear")){
                myptn_timesC[playC]++;
                sfearC[playC]++;
            }else if(curemot.equals("Nod")){
                myptn_timesC[playC]++;
                snodC[playC]++;
            }else if(curemot.equals("Shake")){
                myptn_timesC[playC]++;
                sshakeC[playC]++;
            }
        }
    }
    public void onBackPressed(){
        AlertDialog.Builder builder3 = new AlertDialog.Builder(this);  //builder
        builder3.setMessage("If leave now, you will lose all the data!"); //message
        builder3.setPositiveButton("Leave", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //
                finishAfterTransition();

            }
        });
        builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder3.create().show();
    }
}
