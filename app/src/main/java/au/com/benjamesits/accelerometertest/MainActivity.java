package au.com.benjamesits.accelerometertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor sensor;

    Float xAccel, yAccel, zAccel;

    TextView xAccelTV, yAccelTV, zAccelTV;

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
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
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

        xAccelTV.setText(String.format("X-Accel: %s", xAccel.toString()));
        yAccelTV.setText(String.format("Y-Accel: %s", yAccel.toString()));
        zAccelTV.setText(String.format("Z-Accel: %s", zAccel.toString()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
