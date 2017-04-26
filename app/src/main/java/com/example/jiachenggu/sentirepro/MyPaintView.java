package com.example.jiachenggu.sentirepro;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Created by Jiacheng Gu on 2017/4/10.
 */



/**
 * Created by songer1993 on 03/04/2016.
 */
public class MyPaintView extends View {
    public int viewHeight = 0;
    public int viewWidth = 0;

    private List<Point> allPoints=new ArrayList<Point>();

    public MyPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnTouchListener(new OnTouchListenerImp());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint p=new Paint();
        p.setColor(getResources().getColor(R.color.colorPrimaryDark));
        if(MyPaintView.this.allPoints.size()>1){
            Iterator<Point> iter=MyPaintView.this.allPoints.iterator();
            Point first=null;
            Point last=null;
            while(iter.hasNext()){
                if(first==null){
                    first= iter.next();
                }else{
                    if(last!=null){
                        first=last;
                    }
                    last= iter.next();//end
                    canvas.drawLine(first.x, first.y, last.x, last.y, p);
                }
            }
        }
    }



    private class OnTouchListenerImp implements OnTouchListener{

        public boolean onTouch(View v, MotionEvent event) {
            Point p=new Point((int)event.getX(),(int)event.getY());
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                // start to touch the screen, so restore the point array
                MyPaintView.this.allPoints=new ArrayList<Point>();
                MyPaintView.this.allPoints.add(p);

            }
            else if(event.getAction()==MotionEvent.ACTION_UP){
                // release the finger
                MyPaintView.this.allPoints.add(p);
                MyPaintView.this.postInvalidate(); // redraw
            }
            else if(event.getAction()==MotionEvent.ACTION_MOVE){
                MyPaintView.this.allPoints.add(p);
                MyPaintView.this.postInvalidate();// redraw
            }
            return true;
        }
    }


    public List<Point> getAllPoints(){
        return allPoints;
    }

    // return the path
    public Path getPath(){
        Path path = new Path();
        boolean first = true;
        for(int i = 0; i < allPoints.size(); i += 2){
            Point point = allPoints.get(i);
            if(first){
                first = false;
                path.moveTo(point.x, point.y);
            }

            else if(i < allPoints.size() - 1){
                Point next = allPoints.get(i + 1);
                path.quadTo(point.x, point.y, next.x, next.y);
            }
            else{
                path.lineTo(point.x, point.y);
            }
        }

        return path;
    }


    // return all amplitudes
    public List<Integer> getAmplitudes(int numSamples){
        Path path = new Path();
        List<Integer> amplitudes = new ArrayList<Integer>();

        path = getPath();
        PathMeasure pm = new PathMeasure(path, false);
        float distance = 0f;
        float samplingPeriod = (float)viewWidth / numSamples;
        float[] aCoordinates = new float[2];

        while ((distance < pm.getLength())) {
            // get point from the path
            pm.getPosTan(distance, aCoordinates, null);
            float temp = viewHeight-(int) aCoordinates[1]-1;
            Integer amplitude = new Integer((int)(temp*256f/viewHeight));
            if(amplitude<0) amplitude = 0;
            else if(amplitude>255) amplitude = 255;
            amplitudes.add(amplitude);
            distance = distance + samplingPeriod;
        }

        return amplitudes;
    }

    public void clearPoints(){
        MyPaintView.this.postInvalidate();// redraw
        MyPaintView.this.allPoints.clear();
        MyPaintView.this.allPoints=new ArrayList<Point>();
    }
}
