/* 
 *  This programme is develop by
 *  Jiacheng gu 顾嘉诚
 *  To drive the haptic wristband, which can be controled by the received ble(Bluetooth low energy)signal from portable devices (a smart phone)
 *  
 *  finish on: 2017-4-19
 *  for BEng project in 
 *  the University of Edinburgh 
 *  School of Engineering
 *  Electrical and Electronic Engineering
 *  
 *  used library:
 *  Adafruit_DRV2605.h (motor driver)
 *  Adafruit_SSD1331.h ,Adafruit_GFX.h(Adafruit ssd 1331 oled screen )
 *  
 *  
 *   * connections:
 * OLED  ssd1331:
 * (10 11 13 are spi ports  8 and 9 are common digital ports)
 * GND (G)   - Gnd 
 * VCC (+)   - Vcc (on arduino pro mini 3.3v)
 * SDCS (SC) - skip
 * OCS (OC)  - D10 
 * RST (R)   - D9 
 * D/C (DC)  - D8 
 * SCK (CK)  - D13 
 * MOSI (SI) - D11 
 * MISO (SO) - skip
 * CD (CD) - skip
 * 
 * DRV2605/ADXL345   (I2C):
 * SDA- A4
 * SCL- A5
 * GND (G)   - Gnd 
 * VCC (+)   - Vcc (on arduino pro mini 3.3v)
 * IN - skip
 * 
 * HC-05  (UART):
 * RX- D0
 * TX- D1
 * GND (G)   - Gnd 
 * VCC (+)   - Vcc (battery)
 * 
 * button: D6
 * LED: D5
**************************************/
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1331.h>
#include <SPI.h>
//colors definations
//#define BLACK            0x0000
//#define BLUE            0x001F
//#define RED             0xF800
//#define GREEN           0x07E0
//#define YELLOW          0xFFE0  
//#define WHITE           0xFFFF
//#define sclk 13
//#define mosi 11
//#define cs   10
//#define rst  9
//#define dc   8
//Adafruit_SSD1331 display = Adafruit_SSD1331(cs, dc, mosi, sclk, rst);  
Adafruit_SSD1331 display = Adafruit_SSD1331(10, 8, 11, 13, 9);  
//difference for 0xF800 0x07E0 0x001F  and total color value per step(switching step)
int diff_r[4];
int diff_g[4];
int diff_b[4];
int diff[4];
// switch times is the the times it needed to switch from one color to another
int switch_times[4];
// color store the 16bit (565) color codes to display (converts from 24bits (8 8 8 ) color code from phone)
int color[5];
int colornumb;
/***************************************/
#include <Wire.h>
#include "Adafruit_DRV2605.h"
Adafruit_DRV2605 drv;
 /***************************************/
#include <EEPROM.h>


int addr = 0;       // address to save on the arduino ,  EEPROM on arduino
char command;         // every bit receive from ble
String mstring;        //the whole data
boolean isrec = false;
boolean isuploaded = false;
int la,ra,ca;
int effect[50];         //vibration code to play
int RGBDS[5][5];        // RGB 0xF800 0x07E0 0x001F data ,D duration of the color, S switching time between colors
int lnumb,rnumb,cnumb,cbits;
int codetosave[18][26];     // code to save in the rom or code read from rom
int uploadnumb;
int i,j,k,m,n;

void setup()
{
Serial.begin(9600);
  /***************************************/
  display.begin();
  for(j = 0; j < 18;j++){
    for(i = 0; i < 26; i++){
      codetosave[j][i] = EEPROM.read(j * 26 + i);
    }
  }
 
  display.fillScreen(0x0000);         //full screen black
  display.setCursor(5, 5);
  display.setTextColor(0xF800);       // text color is red
  display.setTextSize(1);
  display.println("Sensory Assistive Device");  //display text "Sensory Assistive Device"
  delay(500);       
  display.setCursor(5, 30);
  display.setTextColor(0xFFE0);         //text color is yellow
  display.setTextSize(1);
  display.println("with haptic feedback");     //display text "with haptic feedback"
  delay(2000);
  display.fillScreen(0x0000);
//  display.setCursor(0, 5);
//  display.setTextColor(0x001F);
//  display.setTextSize(1);
//  display.print("By J.GU(s1573210)");
//  display.setCursor(0, 20);
//  display.print("SV:Dr.Philip Hands" );
//  delay(2000);
//  display.setCursor(30, 25);
//  display.fillScreen(0x0000);
//  display.setTextColor(0xFFFF);
//  display.setTextSize(2);
//  display.print("DEMO");
//  delay(2000);
//  display.fillScreen(0x0000);
  drv.begin();
  drv.selectLibrary(1);
}
void loop()
{
  /*************receive data from phone*************/
if (Serial.available() > 0)
{mstring = "";}
  while(Serial.available() > 0)
  {
    isrec = true;
    command = ((byte)Serial.read());
    if(command == ':')
    {break; }
    else
    {
      mstring += command;
      }

    delay(2);
  }
//      delay(99);


  /***************************************/
  if(isrec){
    play();
                if(isuploaded){       // if user upload the new pattern file, read it 
//                  for(j = 0; j < 18;j++){
//                    for(i = 0; i < 26; i++){
//                      codetosave[j][i] = EEPROM.read(j * 26 + i);
//                       }
//                  }
      uploadnumb = 0;
      isuploaded = false;
//    display.setCursor(15, 25);
//    display.fillScreen(0x0000);
//    display.setTextColor(0x07E0);      
//    display.setTextSize(1);
//    display.print("Downloading succeed");
//    delay(2000);
//    display.fillScreen(0x0000);  
    }
    isrec = false;
    mstring ="";
    }
}
/**********  play pattern function ************/
void play(){
  if(mstring[0] == 'P'){        //for play patterns
    switch(mstring[1]){
      case 'L':         // receive the Library vibration pattern
      /*library vibration pattern                        
        *e.g    code : PL12,1,12,
        *   i=2:  l_effect[0] = 0,la = 1    lnumb=0
        *   i=3:  l_effect[0] = 1,la = 2    lnumb=0
        * , i=4:  l_effect[0] = 1*10+2=12   lnumb=1
        *   i=5:  l_effect[1] = 0,la = 1    lnumb=1
        * , i=6:  l_effect[1] = 1           lnumb=2
        *   i=7:  l_effect[2] = 0,la = 1    lnumb=2
        *   i=8:  l_effect[2] = 1,la = 2    lnumb=2
        * , i=9:  l_effect[2] = 12,         lnumb=3
        *
         */
        for(i = 0; i<=lnumb;i++){
          effect[i] = 0;
        }
        lnumb = 0;
        for(i = 2;i<mstring.length() ;i++){
          if(mstring[i] != ','){                //different effects in a pattern is divided by ','
            effect[lnumb] = effect[lnumb] * 10 +la;
            la = mstring[i] - '0';
          }else{
            effect[lnumb] = effect[lnumb] * 10 +la;
            la = 0;
            lnumb++;                          // number of the effect in library pattern
          }
        }
        la = 0;
        /********here we got the effect and it number = lnumb  ****************/
        drv.setMode(DRV2605_MODE_INTTRIG); 
        playLV(effect,lnumb);
      break;
      
      case 'R':    
      /*real-time vibration pattern   
         */
        for(i = 0; i<=rnumb;i++){
          effect[i] = 0;
        }
        rnumb = 0;
        for(i = 2;i<mstring.length() ;i++){
          if(mstring[i] != ','){
            effect[rnumb] = effect[rnumb] * 10 +ra;
            ra = mstring[i] - '0';
          }else{
            effect[rnumb] = effect[rnumb] * 10 +ra;
            ra = 0;
            rnumb++;                          // number of the effect in realtime pattern
          }
        }
        /********here we got the effect and it number = lnumb  ****************/
        drv.setMode(DRV2605_MODE_REALTIME);
        playRV(effect,rnumb,80);
      break;
      
      case 'C':     
    /*    color vibration pattern                       
        *
         */
        for(i = 0; i<5;i++){
          for(j =0; j<= cnumb;j++){
            RGBDS[j][i] = 0;
          }
        }
        cnumb = 0;
        cbits = 0;
        for(i = 2;i<mstring.length() ;i++){
            if(mstring[i] !='#'){                   // different colors divided by #
              if(cbits ==5){
                cbits = 0;
              }
                if(mstring[i] != ','){              // R   B  G  D  S  data for one color is divided by ,
                  RGBDS[cnumb][cbits] = RGBDS[cnumb][cbits] * 10 +ca;   
                  ca = mstring[i] - '0';
                }else{
                  RGBDS[cnumb][cbits] = RGBDS[cnumb][cbits] * 10 +ca;
                  ca = 0;
                  cbits++;
                }
            }else{
                RGBDS[cnumb][cbits] = RGBDS[cnumb][cbits] * 10 +ca;
                ca = 0;
                cnumb++;
            }
        }
        /********here we got the effect and it number = lnumb  ****************/
        oledplay(RGBDS,cnumb);
      break;
      
    }
  }
  /************ Uploading pattern ***********/
  /***************************************/
  /***************************************/
  if(mstring[0] == 'U'){        // for upload patterns
    uploadnumb++;
        switch(mstring[2]){
            case 'L':     
            /*library vibration pattern                      
              *
               */
              for(i = 0; i<=lnumb;i++){
                effect[i] = 0;
              }
              lnumb = 0;
              for(i = 3;i<mstring.length() ;i++){
                if(mstring[i] != ','){
                  effect[lnumb] = effect[lnumb] * 10 +la;
                  la = mstring[i] - '0';
                }else{
                  effect[lnumb] = effect[lnumb] * 10 +la;
                  la = 0;
                  lnumb++;
                }
              }
              /********here we got the effect and it number = lnumb  ****************/
              codetosave[uploadnumb - 1][0] = 100 + lnumb;       //   if the first bit of the code is between 101-199  then it is a library pattern
              for(k=1; k<=lnumb;k++){
                codetosave[uploadnumb - 1][k] = effect[k - 1];
              }
            break;
            
            case 'R':    
            /*real-time vibration pattern      
             * 
               */
              for(i = 0; i<=rnumb;i++){
                effect[i] = 0;
              }
              rnumb = 0;
              for(i = 3;i<mstring.length() ;i++){
                if(mstring[i] != ','){
                  effect[rnumb] = effect[rnumb] * 10 +ra;
                  ra = mstring[i] - '0';
                }else{
                  effect[rnumb] = effect[rnumb] * 10 +ra;
                  ra = 0;
                  rnumb++;
                }
              }
              /********here we got the effect and it number = lnumb  ****************/
              codetosave[uploadnumb - 1][0] = 200 + rnumb;       //   if the first bit of the code is between 201-255  then it is a realtime pattern
              for(k=1; k<=rnumb;k++){
                codetosave[uploadnumb - 1][k] = effect[ 2 * (k - 1) ];
              }
            break;
            
            case 'C':     
          /*    color vibration pattern                       
              *
               */
              for(i = 0; i<5;i++){
                for(j =0; j<= cnumb;j++){
                  RGBDS[j][i] = 0;
                }
              }
              cnumb = 0;
              cbits = 0;
              for(i = 3;i<mstring.length() ;i++){
                  if(mstring[i] !='#'){
                    if(cbits ==5){
                      cbits = 0;
                    }
                      if(mstring[i] != ','){
                        RGBDS[cnumb][cbits] = RGBDS[cnumb][cbits] * 10 +ca;
                        ca = mstring[i] - '0';
                      }else{
                        RGBDS[cnumb][cbits] = RGBDS[cnumb][cbits] * 10 +ca;
                        ca = 0;
                        cbits++;
                      }
                  }else{
                      RGBDS[cnumb][cbits] = RGBDS[cnumb][cbits] * 10 +ca;
                      ca = 0;
                      cnumb++;
                  }
              }
              /********here we got the effect and it number = lnumb  ****************/
              for(j = 0; j < 5; j++){   //eeprom is 8bytes per bit  max 255 duration and switching time (ms) usually larger than 255
                RGBDS[j][3] /= 10;
                RGBDS[j][4] /= 10;
                
              }
              codetosave[uploadnumb - 1][0] = cnumb;       //   if the first bit of the code is between 1-99  then it is a color pattern
              for(k=1; k<=cnumb;k++){
                for(i = 0; i<5;i++){
                  codetosave[uploadnumb - 1][5 * (k - 1) + i+1] = RGBDS[k - 1][i];
                }
              }
            break;
    }
    if(uploadnumb == 18){
        uploadnumb = 0;
        isuploaded = true;
    /***
      Write the value to the appropriate byte of the EEPROM.
      these values will remain there when the board is
      turned off.
    ***/
        for(j = 0; j < 18;j++){
          for(i = 0; i < 26; i++){
            EEPROM.write(j * 26 + i, codetosave[j][i]);
            }
          }
    }
  }
  if(mstring[0] == 'D'){                //for testing and demo   received data are emotions
    if(mstring == "DHA"){                 // when asked to display happy emotion 
      if(codetosave[0][0] >= 100 && codetosave[0][0] < 200){     //library vibration pattern
          lnumb = codetosave[0][0] - 100;
          for(i = 0; i < lnumb;i++){
              effect[i] = codetosave[0][i + 1];
          }
          drv.setMode(DRV2605_MODE_INTTRIG);
          playLV(effect,lnumb);
      }else if(codetosave[0][0] >= 200){                 //realtime vibration pattern
          rnumb = codetosave[0][0] - 200; 
          for(i = 0; i < rnumb; i++){
            if(i == ( i / 2 ) * 2)
              effect[i] = codetosave[0][i / 2 + 1];
            else
              effect[i] = (codetosave[0][i / 2 + 1] + codetosave[0][i / 2 + 2]) / 2;
          }
          drv.setMode(DRV2605_MODE_REALTIME); 
          playRV(effect,rnumb,80);
      }

        cnumb = codetosave[9][0];
        for(j =0; j < cnumb;j++){
          for(i = 0; i<5;i++){
            RGBDS[j][i] = codetosave[9][5 * j + i + 1];
          }
        }
        for(j = 0; j < cnumb; j++){   //the time duration was divided by 20 when saving , now times 20 to recover
          RGBDS[j][3] *= 10;
          RGBDS[j][4] *= 10;
          
        }
        oledplay(RGBDS,cnumb);
      
      
      }else if(mstring=="DSA"){      //sad
    if(codetosave[1][0] >= 100 && codetosave[1][0] < 200){     //library vibration pattern
          lnumb = codetosave[1][0] - 100;
          for(i = 0; i < lnumb;i++){
              effect[i] = codetosave[1][i + 1];
          }
          drv.setMode(DRV2605_MODE_INTTRIG);
          playLV(effect,lnumb);
      }else if(codetosave[1][0] >= 200){
          rnumb = codetosave[1][0] - 200; 
          for(i = 0; i < rnumb; i++){
            if(i == ( i / 2 ) * 2)
              effect[i] = codetosave[1][i / 2 + 1];
            else
              effect[i] = (codetosave[1][i / 2 + 1] + codetosave[1][i / 2 + 2]) / 2;
          }
          drv.setMode(DRV2605_MODE_REALTIME); 
          playRV(effect,rnumb,80);
      }

        cnumb = codetosave[10][0];
        for(j =0; j < cnumb;j++){
          for(i = 0; i<5;i++){
            RGBDS[j][i] = codetosave[10][5 * j + i + 1];
          }
        }
        for(j = 0; j < cnumb; j++){   //the time duration was divided by 20 when saving , now times 20 to recover
          RGBDS[j][3] *= 10;
          RGBDS[j][4] *= 10;
          
        }
        oledplay(RGBDS,cnumb);
      
      }else if(mstring=="DAN"){    //angry
    if(codetosave[2][0] >= 100 && codetosave[2][0] < 200){     //library vibration pattern
          lnumb = codetosave[2][0] - 100;
          for(i = 0; i < lnumb;i++){
              effect[i] = codetosave[2][i + 1];
          }
          drv.setMode(DRV2605_MODE_INTTRIG);
          playLV(effect,lnumb);
      }else if(codetosave[2][0] >= 200){
          rnumb = codetosave[2][0] - 200; 
          for(i = 0; i < rnumb; i++){
            if(i == ( i / 2 ) * 2)
              effect[i] = codetosave[2][i / 2 + 1];
            else
              effect[i] = (codetosave[2][i / 2 + 1] + codetosave[2][i / 2 + 2]) / 2;
          }
          drv.setMode(DRV2605_MODE_REALTIME); 
          playRV(effect,rnumb,80);
      }

        cnumb = codetosave[11][0];
        for(j =0; j < cnumb;j++){
          for(i = 0; i<5;i++){
            RGBDS[j][i] = codetosave[11][5 * j + i + 1];
          }
        }
        for(j = 0; j < cnumb; j++){   //the time duration was divided by 20 when saving , now times 20 to recover
          RGBDS[j][3] *= 10;
          RGBDS[j][4] *= 10;
          
        }
        oledplay(RGBDS,cnumb);
      
      }else if(mstring=="DCO"){      //contempt
    if(codetosave[3][0] >= 100 && codetosave[3][0] < 200){     //library vibration pattern
          lnumb = codetosave[3][0] - 100;
          for(i = 0; i < lnumb;i++){
              effect[i] = codetosave[3][i + 1];
          }
          drv.setMode(DRV2605_MODE_INTTRIG);
          playLV(effect,lnumb);
      }else if(codetosave[3][0] >= 200){
          rnumb = codetosave[3][0] - 200; 
          for(i = 0; i < rnumb; i++){
            if(i == ( i / 2 ) * 2)
              effect[i] = codetosave[3][i / 2 + 1];
            else
              effect[i] = (codetosave[3][i / 2 + 1] + codetosave[3][i / 2 + 2]) / 2;
          }
          drv.setMode(DRV2605_MODE_REALTIME); 
          playRV(effect,rnumb,80);
      }

        cnumb = codetosave[12][0];
        for(j =0; j < cnumb;j++){
          for(i = 0; i<5;i++){
            RGBDS[j][i] = codetosave[12][5 * j + i + 1];
          }
        }
        for(j = 0; j < cnumb; j++){   //the time duration was divided by 20 when saving , now times 20 to recover
          RGBDS[j][3] *= 10;
          RGBDS[j][4] *= 10;
          
        }
        oledplay(RGBDS,cnumb);
      
      }else if(mstring=="DSU"){     //surprised
    if(codetosave[4][0] >= 100 && codetosave[4][0] < 200){     //library vibration pattern
          lnumb = codetosave[4][0] - 100;
          for(i = 0; i < lnumb;i++){
              effect[i] = codetosave[4][i + 1];
          }
          drv.setMode(DRV2605_MODE_INTTRIG);
          playLV(effect,lnumb);
      }else if(codetosave[4][0] >= 200){
          rnumb = codetosave[4][0] - 200; 
          for(i = 0; i < rnumb; i++){
            if(i == ( i / 2 ) * 2)
              effect[i] = codetosave[4][i / 2 + 1];
            else
              effect[i] = (codetosave[4][i / 2 + 1] + codetosave[4][i / 2 + 2]) / 2;
          }
          drv.setMode(DRV2605_MODE_REALTIME); 
          playRV(effect,rnumb,80);
      }

        cnumb = codetosave[13][0];
        for(j =0; j < cnumb;j++){
          for(i = 0; i<5;i++){
            RGBDS[j][i] = codetosave[13][5 * j + i + 1];
          }
        }
        for(j = 0; j < cnumb; j++){   //the time duration was divided by 20 when saving , now times 20 to recover
          RGBDS[j][3] *= 10;
          RGBDS[j][4] *= 10;
          
        }
        oledplay(RGBDS,cnumb);
      
      }else if(mstring=="DDI"){    //disgust
    if(codetosave[5][0] >= 100 && codetosave[5][0] < 200){     //library vibration pattern
          lnumb = codetosave[5][0] - 100;
          for(i = 0; i < lnumb;i++){
              effect[i] = codetosave[5][i + 1];
          }
          drv.setMode(DRV2605_MODE_INTTRIG);
          playLV(effect,lnumb);
      }else if(codetosave[5][0] >= 200){
          rnumb = codetosave[5][0] - 200; 
          for(i = 0; i < rnumb; i++){
            if(i == ( i / 2 ) * 2)
              effect[i] = codetosave[5][i / 2 + 1];
            else
              effect[i] = (codetosave[5][i / 2 + 1] + codetosave[5][i / 2 + 2]) / 2;
          }
          drv.setMode(DRV2605_MODE_REALTIME); 
          playRV(effect,rnumb,80);
      }

        cnumb = codetosave[14][0];
        for(j =0; j < cnumb;j++){
          for(i = 0; i<5;i++){
            RGBDS[j][i] = codetosave[14][5 * j + i + 1];
          }
        }
        for(j = 0; j < cnumb; j++){   //the time duration was divided by 20 when saving , now times 20 to recover
          RGBDS[j][3] *= 10;
          RGBDS[j][4] *= 10;
          
        }
        oledplay(RGBDS,cnumb);
      
      }else if(mstring=="DFE"){    //fear
    if(codetosave[6][0] >= 100 && codetosave[6][0] < 200){     //library vibration pattern
          lnumb = codetosave[6][0] - 100;
          for(i = 0; i < lnumb;i++){
              effect[i] = codetosave[6][i + 1];
          }
          drv.setMode(DRV2605_MODE_INTTRIG);
          playLV(effect,lnumb);
      }else if(codetosave[6][0] >= 200){
          rnumb = codetosave[6][0] - 200; 
          for(i = 0; i < rnumb; i++){
            if(i == ( i / 2 ) * 2)
              effect[i] = codetosave[6][i / 2 + 1];
            else
              effect[i] = (codetosave[6][i / 2 + 1] + codetosave[6][i / 2 + 2]) / 2;
          }
          drv.setMode(DRV2605_MODE_REALTIME); 
          playRV(effect,rnumb,80);
      }

        cnumb = codetosave[15][0];
        for(j =0; j < cnumb;j++){
          for(i = 0; i<5;i++){
            RGBDS[j][i] = codetosave[15][5 * j + i + 1];
          }
        }
        for(j = 0; j < cnumb; j++){   //the time duration was divided by 20 when saving , now times 20 to recover
          RGBDS[j][3] *= 10;
          RGBDS[j][4] *= 10;
          
        }
        oledplay(RGBDS,cnumb);
      
      }else if(mstring=="DNO"){      //nod
    if(codetosave[7][0] >= 100 && codetosave[7][0] < 200){     //library vibration pattern
          lnumb = codetosave[7][0] - 100;
          for(i = 0; i < lnumb;i++){
              effect[i] = codetosave[7][i + 1];
          }
          drv.setMode(DRV2605_MODE_INTTRIG);
          playLV(effect,lnumb);
      }else if(codetosave[7][0] >= 200){
          rnumb = codetosave[7][0] - 200; 
          for(i = 0; i < rnumb; i++){
            if(i == ( i / 2 ) * 2)
              effect[i] = codetosave[7][i / 2 + 1];
            else
              effect[i] = (codetosave[7][i / 2 + 1] + codetosave[7][i / 2 + 2]) / 2;
          }
          drv.setMode(DRV2605_MODE_REALTIME); 
          playRV(effect,rnumb,80);
      }

        cnumb = codetosave[16][0];
        for(j =0; j < cnumb;j++){
          for(i = 0; i<5;i++){
            RGBDS[j][i] = codetosave[16][5 * j + i + 1];
          }
        }
        for(j = 0; j < cnumb; j++){   //the time duration was divided by 20 when saving , now times 20 to recover
          RGBDS[j][3] *= 10;
          RGBDS[j][4] *= 10;
        }
        oledplay(RGBDS,cnumb);
      
      }else if(mstring=="DSH"){      //shake
    if(codetosave[8][0] >= 100 && codetosave[8][0] < 200){     //library vibration pattern
          lnumb = codetosave[8][0] - 100;
          for(i = 0; i < lnumb;i++){
              effect[i] = codetosave[8][i + 1];
          }
          drv.setMode(DRV2605_MODE_INTTRIG);
          playLV(effect,lnumb);
      }else if(codetosave[8][0] >= 200){
          rnumb = codetosave[8][0] - 200; 
          for(i = 0; i < rnumb; i++){
            if(i == ( i / 2 ) * 2)
              effect[i] = codetosave[8][i / 2 + 1];
            else
              effect[i] = (codetosave[8][i / 2 + 1] + codetosave[0][i / 2 + 2]) / 2;
          }
          drv.setMode(DRV2605_MODE_REALTIME); 
          playRV(effect,rnumb,80);
      }

        cnumb = codetosave[17][0];
        for(j =0; j < cnumb;j++){
          for(i = 0; i<5;i++){
            RGBDS[j][i] = codetosave[17][5 * j + i + 1];
          }
        }
        for(j = 0; j < cnumb; j++){   //the time duration was divided by 20 when saving , now times 20 to recover
          RGBDS[j][3] *= 10;
          RGBDS[j][4] *= 10;
          
        }
        oledplay(RGBDS,cnumb);
      
      }
  }
}
/************ oled display function *******************/
void oledplay(int code[5][5],int ptnnumb){
    for(i = 0; i < ptnnumb ; i++){                          // transfer 24-bits color(R,G,B all 8 bits) to 16 bits color (R 5 bits, G 6 bits, B 5 bits)
      color[i] = ((code[i][0]/8)<<11) + ((code[i][1]/8)<<5) + (code[i][2]/8);
    }
    for(j = 0 ; j < ptnnumb - 1 ;j ++){
      if(code[j][4] <= 200){                               // if not gradually change to next color
        switch_times[j] = 0;
      }else{                                               // gradually change: calculate the switching steps  and color difference for each step
        switch_times[j] = code[j][4] / 200 ;
        diff_r[j] = ( color[j+1] & 0xF800  - color[j] & 0xF800   ) >>11;
        diff_g[j] = ( color[j+1] & 0x07E0  - color[j] & 0x07E0 ) >>5;
        diff_b[j] = ( color[j+1] & 0x001F  - color[j] & 0x001F  );
        diff[j]   = ((diff_r[j] / switch_times[j]) << 11) + ((diff_g[j] / switch_times[j] ) << 5) + (diff_b[j] / switch_times[j]);
      }
    }
    for(i = 0; i<ptnnumb; i++){
        display.fillRect(5,5,40,50,color[i]);             //display the color according 
        delay(code[i][3]);                                //duration
        if(i == ptnnumb - 1 )                             // for the last color, no switching is needed
          break;
        for( j = 0; j <= switch_times[i] ;j++){           // switching time 
          display.fillRect(5,5,40,50,color[i] + diff[i] * j);
        }
    }
    display.fillRect(5,5,40,50,0x0000);         //to minimize the refresh time , only display in a small rectangle
}
void playLV(int effects[],int ptnnumb) {            //library vibration pattern play function
  for (unsigned int i = 0; i < ptnnumb; i++) {
    drv.setWaveform(i, effects[i] + 1);           // set the whole sequence 
    if (i >= 10) break;
  }
  drv.setWaveform(ptnnumb, 0);                   //set effect numbers
  drv.go();                                      // play
}
void playRV(int effects[],int ptnnumb, int time) {      //real time vibration play function 
   for (unsigned int i = 0; i < ptnnumb; i++) {
      drv.setRealtimeValue(effects[i]/2);               //play 
      delay(time);
    }
    drv.setRealtimeValue(0x00);                         //stop
}



