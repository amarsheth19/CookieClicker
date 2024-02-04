package com.example.cookieclickerredo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements SharedPreferences {

    ConstraintLayout myLayout;
    Button button;
    ImageView cookieImage;
    static TextView scoreTextView;
    public static AtomicInteger amount;
    Animation movingAnimation;
    public static int grannyAmount = 0;
    ImageView grannyImage;
    Boolean grannyGone = true;
    Boolean spawningGranny = false;
    public static int farmAmount = 0;
    Boolean spawningFarm = false;
    Boolean farmGone = true;
    ImageView farmImage;
    TextView grannyAmountTextView;
    TextView farmAmountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = findViewById(R.id.id_scoreTextView);
        myLayout = findViewById(R.id.id_layout);
        grannyAmountTextView = findViewById(R.id.id_grannyAmountTextView);
        farmAmountTextView = findViewById(R.id.id_farmAmountTextView);

        cookieImage = findViewById(R.id.id_cookieImage);
        cookieImage.setImageResource(R.drawable.cookiepic);


        amount = new AtomicInteger(0);


        SharedPreferences sharedPref = getSharedPreferences("cookieclickerredo", MODE_PRIVATE);
        Log.d("SAVING5", ""+amount.get());
        if(sharedPref.contains("amount"))
            amount.set(sharedPref.getInt("amount", 0));
        if(sharedPref.contains("g"))
            grannyAmount = (sharedPref.getInt("g", 0));
        if(sharedPref.contains("f"))
            farmAmount = (sharedPref.getInt("f", 0));

/*
        amount.set(0);
        grannyAmount = 0;
        farmAmount=0;

 */











        for(int i = 0; i<grannyAmount; i++)
            addToBottom(grannyImage, R.drawable.cookieclickergrandma);
        for(int i = 0; i<farmAmount; i++)
            addToBottom(farmImage, R.drawable.farmerimage);

        farmAmountTextView.setText("Farms: " + farmAmount);
        grannyAmountTextView.setText("Grannies: " + grannyAmount);


        scoreTextView.setText("Score: "+ amount.get());


        new MyThread().start();


        AsyncThread task = new AsyncThread();
        task.execute();


        final ScaleAnimation scaleAnimation = new ScaleAnimation(1f,1.5f,1f,1.5f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(200);


        cookieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scaleAnimation);
                amount.addAndGet(1);
                scoreTextView.setText("Score: " + amount.get());

                createPlusOnes();

            }
        });


    }


    public void createPlusOnes(){

        TextView plusOnesTextView2 = new TextView(MainActivity.this);
        plusOnesTextView2.setId(View.generateViewId());
        plusOnesTextView2.setText("+1");

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        plusOnesTextView2.setLayoutParams(lp);

        myLayout.addView(plusOnesTextView2);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(myLayout);

        cs.connect(plusOnesTextView2.getId(),ConstraintSet.TOP,cookieImage.getId(),ConstraintSet.TOP);
        cs.connect(plusOnesTextView2.getId(),ConstraintSet.BOTTOM,cookieImage.getId(),ConstraintSet.BOTTOM);
        cs.connect(plusOnesTextView2.getId(),ConstraintSet.LEFT,cookieImage.getId(),ConstraintSet.LEFT);
        cs.connect(plusOnesTextView2.getId(),ConstraintSet.RIGHT,cookieImage.getId(),ConstraintSet.RIGHT);


        double random = (Math.random() * 1);

        cs.setHorizontalBias(plusOnesTextView2.getId(),(float)random);
        cs.setVerticalBias(plusOnesTextView2.getId(),0f);

        cs.applyTo(myLayout);


        movingAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, plusOnesTextView2.getX(), Animation.RELATIVE_TO_SELF, plusOnesTextView2.getX(), Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, -20f);
        movingAnimation.setDuration(2000);

        plusOnesTextView2.startAnimation(movingAnimation);

        new CountDownTimer(2000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                   myLayout.removeView(plusOnesTextView2);
               }
        }.start();

    }


    public void spawnGrandma(){

        grannyImage = new ImageView(MainActivity.this);
        grannyImage.setId(View.generateViewId());
        grannyImage.setImageResource(R.drawable.cookieclickergrandma);



        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(150, 150);
        grannyImage.setLayoutParams(lp);

        myLayout.addView(grannyImage);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(myLayout);

        cs.connect(grannyImage.getId(),ConstraintSet.TOP,myLayout.getId(),ConstraintSet.TOP);
        cs.connect(grannyImage.getId(),ConstraintSet.BOTTOM,myLayout.getId(),ConstraintSet.BOTTOM);
        cs.connect(grannyImage.getId(),ConstraintSet.LEFT,myLayout.getId(),ConstraintSet.LEFT);
        cs.connect(grannyImage.getId(),ConstraintSet.RIGHT,myLayout.getId(),ConstraintSet.RIGHT);


        cs.setHorizontalBias(grannyImage.getId(),0.17f);
        cs.setVerticalBias(grannyImage.getId(),0.23f);


        cs.applyTo(myLayout);


        final Animation spawnAnimation = new ScaleAnimation(0f,1f,0f,1f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        spawnAnimation.setDuration(1000);

        grannyImage.startAnimation(spawnAnimation);


        final Animation exitAnimation = new ScaleAnimation(1f,0f,1f,0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        spawnAnimation.setDuration(1000);



        grannyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amount.get()>=5) {
                    amount.getAndAdd(-5);
                    grannyAmount++;
                    grannyAmountTextView.setText("Grannies: " + grannyAmount);
                    addToBottom(grannyImage, R.drawable.cookieclickergrandma);
                    scoreTextView.setText("Score: " + amount.get());
                }

                if(amount.get()<10 && farmImage!=null){
                    final Animation exitAnimation = new ScaleAnimation(1f,0f,1f,0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    exitAnimation.setDuration(2000);
                    farmImage.startAnimation(exitAnimation);
                    farmImage.setClickable(false);
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            farmGone=true;
                            myLayout.removeView(farmImage);
                        }
                    }.start();
                }



                if (amount.get() < 5) {
                    final Animation exitAnimation = new ScaleAnimation(1f,0f,1f,0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    exitAnimation.setDuration(2000);
                    grannyImage.setClickable(false);
                    grannyImage.startAnimation(exitAnimation);
                    if(spawningGranny) {
                        grannyGone=true;
                        grannyImage.clearAnimation();
                        myLayout.removeView(grannyImage);
                    }
                    else {
                        new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                grannyGone=true;
                                myLayout.removeView(grannyImage);
                            }
                        }.start();
                    }



                }
            }
        });



    }



    public void spawnFarm(){

        farmImage = new ImageView(MainActivity.this);
        farmImage.setId(View.generateViewId());
        farmImage.setImageResource(R.drawable.farmerimage);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(150, 150);
        farmImage.setLayoutParams(lp);

        myLayout.addView(farmImage);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(myLayout);

        cs.connect(farmImage.getId(),ConstraintSet.TOP,myLayout.getId(),ConstraintSet.TOP);
        cs.connect(farmImage.getId(),ConstraintSet.BOTTOM,myLayout.getId(),ConstraintSet.BOTTOM);
        cs.connect(farmImage.getId(),ConstraintSet.LEFT,myLayout.getId(),ConstraintSet.LEFT);
        cs.connect(farmImage.getId(),ConstraintSet.RIGHT,myLayout.getId(),ConstraintSet.RIGHT);


        cs.setHorizontalBias(farmImage.getId(),0.83f);
        cs.setVerticalBias(farmImage.getId(),0.23f);


        cs.applyTo(myLayout);


        final Animation spawnAnimation = new ScaleAnimation(0f,1f,0f,1f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        spawnAnimation.setDuration(1000);

        farmImage.startAnimation(spawnAnimation);


        final Animation exitAnimation = new ScaleAnimation(1f,0f,1f,0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        spawnAnimation.setDuration(1000);


        farmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(amount.get()>=10) {
                    amount.getAndAdd(-10);
                    farmAmount++;
                    farmAmountTextView.setText("Farms: " + farmAmount);
                    addToBottom(farmImage, R.drawable.farmerimage);
                    scoreTextView.setText("Score: " + amount.get());
                }
                if(amount.get()<5 && grannyImage!=null){
                    final Animation exitAnimation = new ScaleAnimation(1f,0f,1f,0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    exitAnimation.setDuration(2000);
                    grannyImage.startAnimation(exitAnimation);
                    grannyImage.setClickable(false);
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            grannyGone=true;
                            myLayout.removeView(grannyImage);
                        }
                    }.start();
                }
                if (amount.get() < 10) {
                    final Animation exitAnimation = new ScaleAnimation(1f,0f,1f,0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    exitAnimation.setDuration(2000);
                    farmImage.setClickable(false);
                    farmImage.startAnimation(exitAnimation);
                    if(spawningFarm) {
                        //farmGone=true;
                        farmImage.clearAnimation();
                        myLayout.removeView(farmImage);
                    }
                    else {
                        new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {}
                            @Override
                            public void onFinish() {
                                farmGone=true;
                                myLayout.removeView(farmImage);
                            }
                        }.start();
                    }


                }
            }
        });



    }




    public void addToBottom(ImageView imageView, int imageResource){

        imageView = new ImageView(MainActivity.this);
        imageView.setId(View.generateViewId());
        imageView.setImageResource(imageResource);


        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(150, 150);
        imageView.setLayoutParams(lp);

        myLayout.addView(imageView);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(myLayout);

        cs.connect(imageView.getId(),ConstraintSet.TOP,myLayout.getId(),ConstraintSet.TOP);
        cs.connect(imageView.getId(),ConstraintSet.BOTTOM,myLayout.getId(),ConstraintSet.BOTTOM);
        cs.connect(imageView.getId(),ConstraintSet.LEFT,myLayout.getId(),ConstraintSet.LEFT);
        cs.connect(imageView.getId(),ConstraintSet.RIGHT,myLayout.getId(),ConstraintSet.RIGHT);

        double randomX = (Math.random() * 1);
        double randomY = (Math.random() * 0.4)+0.6;

        cs.setHorizontalBias(imageView.getId(),(float)randomX);
        cs.setVerticalBias(imageView.getId(),(float)randomY);


        cs.applyTo(myLayout);

        final Animation spawnAnimation = new ScaleAnimation(0f,1f,0f,1f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        spawnAnimation.setDuration(2000);

        imageView.startAnimation(spawnAnimation);

    }


    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        return null;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return null;
    }

    @Override
    public int getInt(String key, int defValue) {
        return 0;
    }

    @Override
    public long getLong(String key, long defValue) {
        return 0;
    }

    @Override
    public float getFloat(String key, float defValue) {
        return 0;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return false;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public Editor edit() {
        return null;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    }


    public static class MyThread extends Thread implements Runnable{

        public Handler handler = new Handler();

        @Override
        public void run() {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    amount.addAndGet(grannyAmount + (2*farmAmount));
                    //Log.d("myThread", "Atomic int:" + amount.get());
                    handler.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    scoreTextView.setText("Score: " + amount.get());
                                }
                            }
                    );
                }
            },2000,2000);

        }

    }


    public class AsyncThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    SharedPreferences sharedPref = getSharedPreferences("cookieclickerredo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("amount", amount.get());
                    editor.apply();

                    editor.putInt("g", grannyAmount);
                    editor.apply();


                    editor.putInt("f", farmAmount);
                    editor.apply();


                    //Log.d("TIMerTHread", "check");
                    if(amount.get()>=5 && grannyGone) {
                        grannyGone = false;
                        spawningGranny = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spawnGrandma();
                            }
                        });
                    }
                    else
                        spawningGranny = false;

                    if(amount.get()>=10 && farmGone) {
                        farmGone = false;
                        spawningFarm = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                spawnFarm();

                            }
                        });
                    }
                    else
                        spawningFarm = false;


                }
            },501,5);


            return null;
        }

    }



}




