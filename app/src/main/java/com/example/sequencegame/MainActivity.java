package com.example.sequencegame;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.widget.Toast;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.stream.Collectors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;


public class MainActivity extends AppCompatActivity implements SensorEventListener  {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Handler handler = new Handler();
    private String tiltDirection = "flat";
    private String lastTiltDirection = "flat";
    private int sequenceCount = 4;
    private List<String> sequence = new ArrayList<>();
    private List<String> playerSequence = new ArrayList<>();
    private int playerScore = 0;

    //buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // we are going to use the sensor service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Button btn = findViewById(R.id.button5);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetRandomButtonColour();
            }
        });
    }

    public void SetRandomButtonColour(){
        //variables
        int buttonCount = 0;
        //colours list
        List<Integer> colors = Arrays.asList(
                R.color.blue, R.color.red, R.color.yellow, R.color.orange,
                R.color.black, R.color.white, R.color.cream, R.color.darkGreen, R.color.armyGreen,
                R.color.pink, R.color.purple, R.color.teal, R.color.brown, R.color.indigo,
                R.color.cyan, R.color.amber, R.color.lime, R.color.deepOrange, R.color.lightGreen,
                R.color.deepPurple, R.color.lightRed, R.color.green, R.color.lightYellow, R.color.lightCyan,
                R.color.lightPink
        );

        //buttons
        Button btn1 = findViewById(R.id.button);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button3);
        Button btn4 = findViewById(R.id.button4);
        //shuffling
        Collections.shuffle(colors);
        List<Button> buttons = new ArrayList();
        buttons.add(btn1);
        buttons.add(btn2);
        buttons.add(btn3);
        buttons.add(btn4);
        Collections.shuffle(buttons);
        // Showing colour sequence
        for (int i = 0; i < sequenceCount; i++) {
            //resting button count
            if(buttonCount == 3){
                buttonCount = 0;
            }
            Button button = buttons.get(buttonCount);
            if(button.getId() == btn1.getId()){
                sequence.add("left");
            }
            else if(button.getId() == btn2.getId()){
                sequence.add("right");
            }
            else if(button.getId() == btn3.getId()){
                sequence.add("up");
            }
            else if(button.getId() == btn4.getId()){
                sequence.add("down");
            }
            else {
                sequence.add("flat");
            }
            buttonCount++;

            int color = colors.get(i);

            // After 2 seconds, change the color
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Set the color
                    button.setBackgroundColor(getColor(color));
                }
            }, (i + 1) * 2000);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Set the color
                    button.setBackgroundColor(getColor(R.color.gray));
                }
            }, (i + 1) * 2500);

        }
    }
    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * App running but not on screen - in the background
     */
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        // Calculate the tilt angles
        float tiltX = (float) Math.atan2(y, Math.sqrt(x * x + z * z));
        float tiltY = (float) Math.atan2(-x, Math.sqrt(y * y + z * z));
        // Convert the angles to degrees
        tiltX = (float) Math.toDegrees(tiltX);
        tiltY = (float) Math.toDegrees(tiltY);
        lastTiltDirection = tiltDirection;

        if(tiltY > 10){
            //up
            tiltDirection = "up";

        } else if (tiltY < -10) {
            //down
            tiltDirection = "down";

        } else if(tiltX < -10){
            //left
            tiltDirection = "left";
        }
        else if(tiltX > 10){
            //right
            tiltDirection = "right";
        }
        else {
            //flat
            tiltDirection = "flat";
        }
        // Getting rid of repeating values
        if (!tiltDirection.equals(lastTiltDirection)) {
            // Omitting "flat" option as it is default
            if (!tiltDirection.equals("flat")) {
                playerSequence.add(tiltDirection);
                Toast.makeText(this.getApplicationContext(), tiltDirection, Toast.LENGTH_SHORT).show();

                if (playerSequence.size() == sequence.size()) {
                    boolean isCorrect = false;
                    for(int i = 0; i < sequence.size(); i++){
                        if(playerSequence.get(i) == sequence.get(i)){
                            isCorrect = true;
                        }
                        else {
                            isCorrect = false;
                            break;
                        }
                    }

                    if (isCorrect) {
                        Toast.makeText(this.getApplicationContext(), "correct!", Toast.LENGTH_SHORT).show();
                        playerScore += sequenceCount;
                        sequenceCount += 2;
                        sequence.clear();
                        playerSequence.clear();
                        SetRandomButtonColour();
                    } else {
                        Toast.makeText(this.getApplicationContext(), "wrong!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, GameOver.class);
                        intent.putExtra("playerScore", playerScore);
                        startActivity(intent);
                    }
                }
            }

            // Update lastTiltDirection after processing
            lastTiltDirection = tiltDirection;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }

}