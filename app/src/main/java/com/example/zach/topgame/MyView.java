package com.example.zach.topgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Zach on 10/25/2016.
 */
public class MyView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {

    private final float INTENSITY = .6f;
    public final float MAXSPIN = 20;
    private final int DELAY = 5;

    private Sensor mAccelerometer;

    protected Context context;
    DrawingThread thread;
    Paint text;
    Paint background;
    Paint bars;
    int score, barSwap;
    private float sensorX,sensorY,sensorZ;
    private float xPos,yPos,accX,accY;
    private int timer;
    private boolean spinning,bar;

    private Bitmap bg;
    private Top top;
    private Wind wind;

    public void startSimulation() {
            /*
             * It is not necessary to get accelerometer events at a very high
             * rate, by using a slower rate (SENSOR_DELAY_UI), we get an
             * automatic low-pass filter, which "extracts" the gravity component
             * of the acceleration. As an added benefit, we use less power and
             * CPU resources.
             */
        MainActivity.mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopSimulation() {
        MainActivity.mSensorManager.unregisterListener(this);
    }

    public MyView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        context = ctx;

        mAccelerometer = MainActivity.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        background = new Paint();
        background.setColor(Color.BLACK);

        text = new Paint();
        text.setTextAlign(Paint.Align.LEFT);
        text.setColor(Color.WHITE);
        text.setTextSize(24);

        bars = new Paint();

        bg = BitmapFactory.decodeResource(context.getResources(),R.drawable.wood);


        sensorX = 0;
        sensorY = 0;
        sensorZ = 0;

        accX = 0;
        accY = 0;

        barSwap = 0;
        timer = 0;

        spinning = false;
        bar = false;

        top = new Top();
        wind = new Wind();

    }

    public void customDraw(Canvas canvas) {
        canvas.drawBitmap(bg,0,0,null);
        top.draw(canvas,xPos,yPos);
        updatePhysics(canvas);

        text.setColor(Color.WHITE);
        text.setTextSize(40);

        canvas.drawText("Sensor X = " + sensorX, 50,250,text);
        canvas.drawText("Sensor Y = " + sensorY, 50,400,text);
        canvas.drawText("Spin = " + top.getSpin(), 50,550,text);

        if(!spinning){
            drawSpinBar(canvas);
        }

        if(wind.done()){
            wind.draw(canvas);
        }

    }

    /**  ACCELEROMETER SENSOR STUFF
     *
     * @param event
     */

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
            /*
             * record the accelerometer data, the event's timestamp as well as
             * the current time. The latter is needed so we can calculate the
             * "present" time during rendering. In this application, we need to
             * take into account how the screen is rotated with respect to the
             * sensors (which always return data in a coordinate space aligned
             * to with the screen in its native orientation).
             */

        switch (MainActivity.mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                sensorX = event.values[0];
                sensorY = event.values[1];
                break;
            case Surface.ROTATION_90:
                sensorX = -event.values[1];
                sensorY = event.values[0];
                break;
            case Surface.ROTATION_180:
                sensorX = -event.values[0];
                sensorY = -event.values[1];
                break;
            case Surface.ROTATION_270:
                sensorX = event.values[1];
                sensorY = -event.values[0];
                break;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }



    /**   VALUE CHANGERS
     *
     */

    public void drawSpinBar(Canvas c){
        if(!bar){
            timer++;
            if(timer == DELAY){
                timer = 0;
                barSwap++;
            }
        }else{
            timer++;
            if(timer == DELAY){
                timer = 0;
                barSwap--;
            }
        }
        switch (barSwap){
            case 0:
                bar = false;
                break;
            case 1:
                bars.setColor(Color.rgb(50,0,0));
                c.drawRect(50,c.getHeight()-150,100,c.getHeight()-100,bars);
                break;
            case 2:
                bars.setColor(Color.rgb(50,0,0));
                c.drawRect(50,c.getHeight()-150,100,c.getHeight()-100,bars);
                bars.setColor(Color.rgb(100,0,0));
                c.drawRect(150,c.getHeight()-200,200,c.getHeight()-100,bars);
                break;
            case 3:
                bars.setColor(Color.rgb(50,0,0));
                c.drawRect(50,c.getHeight()-150,100,c.getHeight()-100,bars);
                bars.setColor(Color.rgb(100,0,0));
                c.drawRect(150,c.getHeight()-200,200,c.getHeight()-100,bars);
                bars.setColor(Color.rgb(150,0,0));
                c.drawRect(250,c.getHeight()-250,300,c.getHeight()-100,bars);
                break;
            case 4:
                bars.setColor(Color.rgb(50,0,0));
                c.drawRect(50,c.getHeight()-150,100,c.getHeight()-100,bars);
                bars.setColor(Color.rgb(100,0,0));
                c.drawRect(150,c.getHeight()-200,200,c.getHeight()-100,bars);
                bars.setColor(Color.rgb(150,0,0));
                c.drawRect(250,c.getHeight()-250,300,c.getHeight()-100,bars);
                bars.setColor(Color.rgb(200,0,0));
                c.drawRect(350,c.getHeight()-300,400,c.getHeight()-100,bars);
                break;
            case 5:
                bars.setColor(Color.rgb(50,0,0));
                c.drawRect(50,c.getHeight()-150,100,c.getHeight()-100,bars);
                bars.setColor(Color.rgb(100,0,0));
                c.drawRect(150,c.getHeight()-200,200,c.getHeight()-100,bars);
                bars.setColor(Color.rgb(150,0,0));
                c.drawRect(250,c.getHeight()-250,300,c.getHeight()-100,bars);
                bars.setColor(Color.rgb(200,0,0));
                c.drawRect(350,c.getHeight()-300,400,c.getHeight()-100,bars);
                bars.setColor(Color.rgb(255,0,0));
                c.drawRect(450,c.getHeight()-350,500,c.getHeight()-100,bars);
                bar = true;
                break;
        }

        //I could do this with loops...

    }

    public void updatePhysics(Canvas c){
        float nsx = sensorX/INTENSITY;
        float nsy = sensorY/INTENSITY;
        float maxSpeed = 4;

        if(spinning){
            if(xPos > 0 && xPos < c.getWidth()){
                xPos+=-nsx;
            }else if(xPos <= 0){
                xPos = 1;
            }else if(xPos >= c.getWidth()){
                xPos = c.getWidth()-1;
            }

            if(yPos > 0 && yPos < c.getHeight()){
                yPos+=nsy;
            }else if(yPos <= 0){
                yPos = 1;
            }else if(yPos >= c.getHeight()){
                yPos = c.getHeight()-1;
            }

        }
    }


    /** IMAGE CHANGERS
     *
     * @param b
     * @param newWidth
     * @param newHeight
     * @return
     */
    public Bitmap resizeBitmap(Bitmap b, int newWidth, int newHeight) {
        int w = b.getWidth();
        int h = b.getHeight();
        float scaleWidth = ((float) newWidth) / w;
        float scaleHeight = ((float) newHeight) / h;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                b, 0, 0, w, h, matrix, false);
        b.recycle();
        return resizedBitmap;
    }


    /** THREAD STUFF
     *
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.setRunning(false);
        boolean waitingForDeath = true;
        while (waitingForDeath) {
            try {
                thread.join();
                waitingForDeath = false;
            } catch (Exception e) {
                Log.v("Thread Exception", "Waiting on drawing thread to die: " + e.getMessage());
            }
        }
        stopSimulation();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new DrawingThread(holder, context, this);
        thread.setRunning(true);
        thread.start();
        startSimulation();
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!spinning){
            if(barSwap != 0){
                float spinAmount = (MAXSPIN/5) * barSwap;
                top.toss(spinAmount);
                if(spinAmount >= MAXSPIN-10){
                    wind.setX((int)xPos);
                    wind.setY((int)yPos);
                    wind.setBlow(true);
                }
            }else{
                top.toss(1);
            }
        }
        return true;
    }


    class DrawingThread extends Thread {
        private boolean running;
        private Canvas canvas;
        private SurfaceHolder holder;
        private Context context;
        private MyView view;

        private int FRAME_RATE = 30;
        private double delay = 1.0 / FRAME_RATE * 1000;
        private long time;

        public DrawingThread(SurfaceHolder holder, Context c, MyView v) {
            this.holder = holder;
            context = c;
            view = v;
            time = System.currentTimeMillis();
            xPos = v.getWidth()/2;
            yPos = v.getHeight()/2;
        }

        void setRunning(boolean r) {
            running = r;
        }

        @Override
        public void run() {
            super.run();
            while (running) {
                if (System.currentTimeMillis() - time > delay) {
                    time = System.currentTimeMillis();
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        view.customDraw(canvas);
                        holder.unlockCanvasAndPost(canvas);
                    }

                }
            }
        }


    }

    class Top{

        private ArrayList<Bitmap> imgs;
        private float spin,timer,spinDecrease;
        private int swap,delay;

        public Top(){
            imgs = new ArrayList<Bitmap>();

            Bitmap skim;
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.square);
            skim = resizeBitmap(skim,70,70);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.diamond);
            skim = resizeBitmap(skim,80,80);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.fall);
            skim = resizeBitmap(skim,60,70);
            imgs.add(skim);

            spin = 0;
            timer = 0;
            swap = 0;
            spinDecrease = 0;

        }

        public void draw(Canvas c, float x, float y){

            if(spin != 0){
                spinDraw(c,x,y);
            }else{
                c.drawBitmap(imgs.get(2),x,y,null);
                spinning = false;
            }
        }

        public void toss(float value){
            spin = value;
            spinning = true;
            timer = MAXSPIN - spin;
            spinDecrease = 0;
        }


        public void spinDraw(Canvas c,float x, float y){
            c.drawBitmap(imgs.get((int)swap),x - (imgs.get(swap).getWidth()/2),y - (imgs.get(swap).getHeight()/2),null);

            if(swap == 0){
                timer--;
                if(timer <= 0){
                    swap = 1;
                    timer = MAXSPIN - spin;
                }
            }else{
                timer--;
                if(timer <= 0){
                    swap = 0;
                    timer = MAXSPIN - spin;
                }
            }


            if(spin <= 0){
                spin = 0;
                spinning = false;
            }else{
                if(delay == 5){
                    spin-=spinDecrease;
                    spinDecrease+=.005;
                }else{
                    delay++;
                }
            }
        }

        public float getSpin(){return spin;}
    }

    class Wind{

        private int xW,yW,itter;
        private ArrayList<Bitmap> imgs;
        private Bitmap skim;
        private boolean blow;

        public Wind(){
            xW = 0;
            yW = 0;
            itter = 0;
            blow = false;
            imgs = new ArrayList<>();
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind1);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind2);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind3);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind4);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind5);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind6);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind7);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind8);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind9);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind10);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind11);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind12);
            imgs.add(skim);
            skim = BitmapFactory.decodeResource(context.getResources(),R.drawable.wind13);
            imgs.add(skim);

        }

        public void draw(Canvas c){
           if(blow){
               c.drawBitmap(imgs.get(itter),xW - (imgs.get(itter).getWidth()/2),yW - (imgs.get(itter).getHeight()/2),null);
               if(itter < imgs.size()-1){
                   itter++;
               }else{
                   itter = 0;
                   blow = false;
               }
           }
        }

        public void setX(int nx){xW = nx;}
        public void setY(int ny){yW = ny;}
        public void setBlow(boolean b){blow = b;}
        public boolean done(){return blow;}
    }


}