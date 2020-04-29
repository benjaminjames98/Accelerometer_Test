package au.com.benjamesits.accelerometertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor sensor;

    Float xAccel, yAccel, zAccel;

    TextView xAccelTV, yAccelTV, zAccelTV;

    boolean calibrationInProgress = false;
    float xOffset = 0.0f;
    float yOffset = 0.0f;
    List<Float> xOffsets = new ArrayList<Float>();
    List<Float> yOffsets = new ArrayList<Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xAccelTV = findViewById(R.id.xAccelTV);
        yAccelTV = findViewById(R.id.yAccelTV);
        zAccelTV = findViewById(R.id.zAccelTV);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) {
            String message = "Could not access sensor service";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            finish();
        }


        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null) {
            String message = "Could not access accelerometer";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xAccel = event.values[0];
        yAccel = event.values[1];
        zAccel = event.values[2];

        if (calibrationInProgress) {
            xOffsets.add(xAccel);
            yOffsets.add(yAccel);

            if (xOffsets.size() == 10) {
                float total = 0.0f;
                for (float f : xOffsets)
                    total += f;
                xOffset = total / 10.0f;

                total = 0.0f;
                for (float f : yOffsets)
                    total += f;
                yOffset = total / 10.0f;

                calibrationInProgress = false;
            }
        }

        float correctedXAccel = xAccel - xOffset;
        float correctedYAccel = yAccel - yOffset;

        xAccelTV.setText(String.format("X-Accel: %s", Float.toString(correctedXAccel)));
        yAccelTV.setText(String.format("Y-Accel: %s", Float.toString(correctedYAccel)));
        zAccelTV.setText(String.format("Z-Accel: %s", zAccel.toString()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void startCalibration(View view) {
        calibrationInProgress = true;
    }
}
