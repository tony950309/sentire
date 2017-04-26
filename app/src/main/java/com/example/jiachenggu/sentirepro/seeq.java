package com.example.jiachenggu.sentirepro;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

public class seeq extends AppCompatActivity implements View.OnClickListener {
    private List<lvseeq_class> mylist=new ArrayList<lvseeq_class>();
    private ListView listView;
    private lvseeq_adapter myadapter;

    ImageButton imbtnpre,imbtnnext;
    TextView tvNAG,tvdisab,tvnumb;
    int curnumb,ttlnumb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeq);
        setTitle("                                    Questionnaire Details");

        imbtnpre = (ImageButton) findViewById(R.id.imbtnseeqpre);
        imbtnnext= (ImageButton) findViewById(R.id.imbtnseeqnext);
        tvNAG    = (TextView)    findViewById(R.id.tvseeqNAG);
        tvdisab  = (TextView)    findViewById(R.id.tvseeqdisab);
        tvnumb   = (TextView)    findViewById(R.id.tvseeqnumb);

        imbtnpre.setOnClickListener(this);
        imbtnnext.setOnClickListener(this);

        //initialize
        ttlnumb = readQnumb();
        curnumb = 1;
        readthis("Questionnaire" + Integer.toString(curnumb) + ".xls");
        initptn();
        tvNAG.setText("Nationality :  " + nationality + "\n" +
                "Age :              " + age + "\n" +
                "Gender :       " + gender );
        tvdisab.setText("Disabilities: " + "\n" + disability);
        tvnumb.setText("#" + Integer.toString(curnumb) + "/" + Integer.toString(ttlnumb));


    }
    public void initptn(){
        for(int i = 0; i<curttlitemnumb;i++){
            mylist.add(new lvseeq_class(
                    data[i][0],data[i][1],data[i][2],
                    R.drawable.ic_media_embed_play));
        }
        myadapter = new lvseeq_adapter(mylist);
        listView  = (ListView)findViewById(R.id.lvseeq);
        listView.setAdapter(myadapter);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imbtnseeqpre:
                if(curnumb <= 1){
                    msg("This is the first questionnaire!");
                }else{
                    curnumb--;
                    for(int i = 0; i < curttlitemnumb ; i++){
                        mylist.remove(0);
                    }
                    readthis("Questionnaire" + Integer.toString(curnumb) + ".xls");
                    initptn();
                    tvNAG.setText("Nationality :  " + nationality + "\n" +
                            "Age :              " + age + "\n" +
                            "Gender :       " + gender );
                    tvdisab.setText("Disabilities: " + "\n" + disability);
                    tvnumb.setText("#" + Integer.toString(curnumb) + "/" + Integer.toString(ttlnumb));
                }
                break;
            case R.id.imbtnseeqnext:
                if(curnumb >= ttlnumb){
                    msg("This is the last questionnaire!");
                }else{
                    curnumb++;
                    for(int i = 0; i < curttlitemnumb ; i++){
                        mylist.remove(0);
                    }
                    readthis("Questionnaire" + Integer.toString(curnumb) + ".xls");
                    initptn();
                    tvNAG.setText("Nationality :  " + nationality + "\n" +
                                  "Age :              " + age + "\n" +
                                  "Gender :       " + gender );
                    tvdisab.setText("Disabilities: " + "\n" + disability);
                    tvnumb.setText("#" + Integer.toString(curnumb) + "/" + Integer.toString(ttlnumb));
                }
                break;

        }
    }
    private int readQnumb(){
        int n = 0;
        try {
            File file=new File(Environment.getExternalStorageDirectory(),"QuestionnaireNumber.xls");
            Workbook workbook = Workbook.getWorkbook(file);
            Sheet sheet = workbook.getSheet(0);
            //  get total number of existing ptn numbs
            Cell cell = sheet.getCell(0, 0);
            n = Integer.valueOf(cell.getContents()).intValue();
//            cell = sheet.getCell(1,0);
//            lastLVnumb = Integer.valueOf(cell.getContents()).intValue();
//            cell = sheet.getCell(2,0);
//            lastRTVnumb = Integer.valueOf(cell.getContents()).intValue();
//            cell = sheet.getCell(3,0);
//            lastCnumb = Integer.valueOf(cell.getContents()).intValue();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return n;
    }
    // for read the single excel file
    String nationality,age,gender,disability;
    String[][] data = new String[999][3];
    int curttlitemnumb;
    private void readthis(String filename){
        try {
            File file=new File(Environment.getExternalStorageDirectory(),filename);
            Workbook workbook = Workbook.getWorkbook(file);
            //
            Sheet sheet = workbook.getSheet(0);
            //  get total number of existing ptn numbs
            Cell cell = sheet.getCell(0, 1);
            curttlitemnumb = Integer.valueOf(cell.getContents()).intValue();
            //personal data
            cell = sheet.getCell(1,1);
            nationality = cell.getContents();
            cell = sheet.getCell(2,1);
            age = cell.getContents();
            cell = sheet.getCell(3,1);
            gender = cell.getContents();
            cell = sheet.getCell(4,1);
            disability = cell.getContents();
            //pattern data
            for (int row = 3; row <= curttlitemnumb + 2; row++)
            {
                cell = sheet.getCell(0,row);
                data[row-3][0] = cell.getContents();
                cell = sheet.getCell(1,row);
                data[row-3][1] = cell.getContents();
                cell = sheet.getCell(2,row);
                data[row-3][2] = cell.getContents();
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
