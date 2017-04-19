package com.example.mateo.sensores;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
TextView salida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        salida = (TextView) findViewById(R.id.textView);
        SensorManager sensorManager = (SensorManager)
                getSystemService(SENSOR_SERVICE);
        List<Sensor> listaSensores = sensorManager.
                getSensorList(Sensor.TYPE_ALL);
        log("SENSORES DEL DISPOSITIVO:\n\n");
        for(Sensor sensor: listaSensores) {
            log("- "+sensor.getName());
        }
    }
    private void log(String string) {
        salida.append(string + "\n");
    }

}
