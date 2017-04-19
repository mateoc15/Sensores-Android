package co.edu.udea.compumovil.gr0620171.sensoresandroid;

import java.text.DecimalFormat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private TextView acelerometro;
    private TextView magnetic;
    private TextView proximity;
    private TextView luminosidad;
    private TextView temperatura;
    private TextView gravedad;
    private TextView giro;
    private TextView presion;
    private TextView txtAngle;
    private ImageView imgCompass;

    float max_x = 0;
    float max_y = 0;
    float max_z = 0;

    private float currentDegree = 0f;
    float degree;
    float azimut;
    float[] mGravity;
    float[] mGeomagnetic;
    Bundle bundle;

    DecimalFormat dosdecimales = new DecimalFormat("###.###");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Defino los botones
        Button sensores = (Button) findViewById(R.id.listado);

        // Defino los TXT para representar los datos de los sensores
        acelerometro = (TextView) findViewById(R.id.acelerometro);
        magnetic = (TextView) findViewById(R.id.magnetic);
        proximity = (TextView) findViewById(R.id.proximity);
        luminosidad = (TextView) findViewById(R.id.luminosidad);
        temperatura = (TextView) findViewById(R.id.temperatura);
        gravedad = (TextView) findViewById(R.id.gravedad);
        giro = (TextView) findViewById(R.id.giro);
        presion = (TextView) findViewById(R.id.presion);
        imgCompass = (ImageView) findViewById(R.id.imgViewCompass);
        txtAngle = (TextView) findViewById(R.id.txtAngle);

        mGravity = null;
        mGeomagnetic = null;

        // Accedemos al servicio de sensores
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Boton que muestra el listado de los sensores disponibles
        sensores.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, ListaActivity.class);
                startActivity(i);
            }
        });

    }

    // Metodo para iniciar el acceso a los sensores
    //SENSOR DELAY es la frecuencia en microsegundos con la que se realiza la escucha
    protected void iniciarSensores() {

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Metodo para parar la escucha de los sensores
    private void cerrarSensores() {

        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));

        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));

        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));

        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE));

        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));

        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));

        mSensorManager.unregisterListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE));
    }

    // Metodo que escucha el cambio de sensibilidad de los sensores
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // Metodo que escucha el cambio de los sensores
    @Override
    public void onSensorChanged(SensorEvent event) {
        String txt = "\n\nSensor: ";

        // Cada sensor puede lanzar un thread que pase por aqui
        // Para asegurarnos ante los accesos simultaneos sincronizamos esto

        synchronized (this) {
            Log.d("sensor", event.sensor.getName());
            switch (event.sensor.getType()) {

                case Sensor.TYPE_ACCELEROMETER:
                    //Usamos el acelerometro para la brujula
                    mGravity = event.values.clone();

                    if (event.values[0] > max_x) {
                        max_x = event.values[0];
                    }
                    if (event.values[1] > max_y) {
                        max_y = event.values[1];
                    }
                    if (event.values[2] > max_z) {
                        max_z = event.values[2];
                    }

                    txt += "Acelerometro\n";
                    txt += "\n x: " + dosdecimales.format(event.values[0])
                            + " m/s - Max: " + dosdecimales.format(max_x);
                    txt += "\n y: " + dosdecimales.format(event.values[1])
                            + " m/s - Max: " + dosdecimales.format(max_y);
                    txt += "\n z: " + dosdecimales.format(event.values[2])
                            + " m/s - Max: " + dosdecimales.format(max_z);
                    txt += "\n\n Rango maximo: " + event.sensor.getMaximumRange() +
                            "\n Rango minimo: " + event.sensor.getMaximumRange()*-1 +
                            "\n Power: (Gasto de energia)" + event.sensor.getPower() + " mA";
                    acelerometro.setText(txt);

                    if ((event.values[0] > 15) || (event.values[1] > 15)
                            || (event.values[2] > 15)) {
                        Toast.makeText(getApplicationContext(),"Vibracion Detectada", Toast.LENGTH_LONG).show();
                    }

                    break;

                case Sensor.TYPE_ROTATION_VECTOR:
                    txt += " Rotation vector\n";
                    txt += "\n x: " + event.values[0];
                    txt += "\n y: " + event.values[1];
                    txt += "\n z: " + event.values[2];
                    txt += "\n\n Rango maximo: " + event.sensor.getMaximumRange() +
                            "\n Rango minimo: " + event.sensor.getMaximumRange()*-1 +
                            "\n Power: (Gasto de energia)" + event.sensor.getPower() + " mA";

                    // Creo objeto para saber como esta la pantalla
                    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                            .getDefaultDisplay();
                    int rotation = display.getRotation();

                    // El objeto devuelve 3 estados 0, 1 o 3
                    if (rotation == 0) {
                        txt += "\n\n Posicion: Vertical";

                    } else if (rotation == 1) {
                        txt += "\n\n Posicion: Horizontal Izquierda";

                    } else if (rotation == 3) {
                        txt += "\n\n Posicion: Horizontal Derecha";
                    }

                    txt += "\n\n display: " + rotation;

                    giro.setText(txt);
                    break;

                case Sensor.TYPE_GRAVITY:
                    txt += "Gravedad\n";
                    txt += "\n x: " + event.values[0];
                    txt += "\n y: " + event.values[1];
                    txt += "\n z: " + event.values[2];
                    txt += "\n\n Rango maximo: " + event.sensor.getMaximumRange() +
                            "\n Rango minimo: " + event.sensor.getMaximumRange()*-1 +
                            "\n Power: (Gasto de energia)" + event.sensor.getPower() + " mA";

                    gravedad.setText(txt);

                    break;

                case Sensor.TYPE_MAGNETIC_FIELD:
                    txt += "Magnetometro\n";
                    txt += "\n" + event.values[0] + " uT";
                    txt += "\n\n Rango maximo: " + event.sensor.getMaximumRange() +
                            "\n Rango minimo: " + event.sensor.getMaximumRange()*-1 +
                            "\n Power: (Gasto de energia)" + event.sensor.getPower() + " mA";

                    magnetic.setText(txt);
                    //Usamos el Magnetometro para la brujula
                    mGeomagnetic = event.values.clone();
                    break;

                case Sensor.TYPE_PROXIMITY:
                    txt += "Proximidad\n";
                    txt += "\n" + event.values[0] + " cm";
                    txt += "\n\n Rango maximo: " + event.sensor.getMaximumRange() +
                            "\n Rango minimo: " + event.sensor.getMaximumRange()*-1 +
                            "\n Power: (Gasto de energia)" + event.sensor.getPower() + " mA";

                    proximity.setText(txt);

                    // Si detecta 0 lo represento
                    if (event.values[0] == 0) {
                        Toast.makeText(getApplicationContext(),"Proximidad Detectada", Toast.LENGTH_LONG).show();
                    }

                    break;

                case Sensor.TYPE_LIGHT:
                    txt += "Luminosidad\n";
                    txt += "\n" + event.values[0] + " Lux";
                    txt += "\n\n Rango maximo: " + event.sensor.getMaximumRange() +
                            "\n Rango minimo: " + event.sensor.getMaximumRange()*-1 +
                            "\n Power: (Gasto de energia)" + event.sensor.getPower() + " mA";

                    luminosidad.setText(txt);

                    break;

                case Sensor.TYPE_PRESSURE:
                    txt += "Presion\n";
                    txt += "\n" + event.values[0] + " mBar \n\n";
                    txt += "\n\n Rango maximo: " + event.sensor.getMaximumRange() +
                            "\n Rango minimo: " + event.sensor.getMaximumRange()*-1 +
                            "\n Power: (Gasto de energia)" + event.sensor.getPower() + " mA";

                    presion.setText(txt);

                    break;

                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    txt += "Temperatura ambiente\n";
                    txt += "\n" + event.values[0] + "C";
                    txt += "\n\n Rango maximo: " + event.sensor.getMaximumRange() +
                            "\n Rango minimo: " + event.sensor.getMaximumRange()*-1 +
                            "\n Power: (Gasto de energia)" + event.sensor.getPower() + " mA";

                    temperatura.setText(txt);

                    break;

            }
            hacerBrujula();

        }

    }

    private void hacerBrujula() {
        if ((mGravity != null) && (mGeomagnetic != null)) {
            float RotationMatrix[] = new float[16];
            // Se crea una matriz de rotacion que sirve como instrumento para el getOrientation (necesario)
            boolean success = SensorManager.getRotationMatrix(RotationMatrix, null, mGravity, mGeomagnetic);
            String direccion = "";
            if (success) {
                float orientation[] = new float[3];
                //metodo que reemplaza al sensor obsoleto Orientation
                SensorManager.getOrientation(RotationMatrix, orientation);
                azimut = orientation[0] * (180 / (float) Math.PI);
                direccion = getDireccion(azimut);
            }
            degree = azimut;

            txtAngle.setText("Angulo: " + Float.toString(degree) + " grados ("+ direccion+")");
            // se crea la animacion de la rottacion (se revierte el giro en grados, negativo)
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);
            // el tiempo durante el cual la animación se llevará a cabo
            ra.setDuration(1000);
            // establecer la animación después del final de la estado de reserva
            ra.setFillAfter(true);
            // Inicio de la animacion
            imgCompass.startAnimation(ra);
            currentDegree = -degree;
        }
    }

    private String getDireccion(float values) {
        String txtDirection = "";
        if (values < 22)
            txtDirection = "Norte";
        else if (values >= 22 && values < 67)
            txtDirection = "Noreste";
        else if (values >= 67 && values < 112)
            txtDirection = "Este";
        else if (values >= 112 && values < 157)
            txtDirection = "Sureste";
        else if (values >= 157 && values < 202)
            txtDirection = "Sur";
        else if (values >= 202 && values < 247)
            txtDirection = "Suroeste";
        else if (values >= 247 && values < 292)
            txtDirection = "Oeste";
        else if (values >= 292 && values < 337)
            txtDirection = "Noroeste";
        else if (values >= 337)
            txtDirection = "Norte";

        return txtDirection;
    }

    @Override
    protected void onStop() {
        cerrarSensores();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        cerrarSensores();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        cerrarSensores();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        iniciarSensores();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciarSensores();
    }

    public void infoAcelerometro(View view) {
        bundle = new Bundle();
        bundle.putString("descripcion", "Brinda información de la aceleración que se le ejerce al dispositivo. (Medellín gravitacional = 9.78)");
        bundle.putString("uso", "Detección de movimiento.");
        bundle.putString("datos", "'SENSOR_TYPE_ACCELEROMETER' \n Todos los valores están en unidades del SI (m / s ^ 2), al restar la aceleración del dispositivo menos la aceleracion de la gravedad a lo largo de los ejes del sensor. se consigue la aceleracion neta del dispositivo");
        bundle.putString("sensor", "Acelerometro");
        mostrarFragment(bundle);
    }

    public void infoGravedad(View view) {
        bundle = new Bundle();
        bundle.putString("descripcion", "Sensores físicos subyacentes: acelerómetro y (si está presente) del giroscopio (o magnetómetro si giroscopio no está presente)");
        bundle.putString("uso", "informa de la dirección y la magnitud de la gravedad en las coordenadas del dispositivo.");
        bundle.putString("datos", "'SENSOR_TYPE_GRAVITY' \n Los componentes vector de gravedad se reportan en m / s ^ 2 en los campos X, Y y Z del vector Value[3]");
        bundle.putString("sensor", "Gravedad");
        mostrarFragment(bundle);
    }

    public void infoGiro(View view) {
        bundle = new Bundle();
        bundle.putString("descripcion", "Un sensor de rotación vectorial informa la orientación del dispositivo con respecto al Este-Norte-Up coordina marco. Por lo general se obtiene por integración de las lecturas del acelerómetro, giroscopio, y magnetómetro. ");
        bundle.putString("uso", "Posicionamiento");
        bundle.putString("datos", "'SENSOR_TYPE_ROTATION_VECTOR' \n Devuelve datos en tres coordenadas (X,Y,Z), - X puntos este y es tangencial a la tierra.\n" +
                "- Y apunta hacia el norte y es tangencial al suelo.\n" +
                "- Z puntos hacia el cielo y es perpendicular al suelo.");
        bundle.putString("sensor", "Vector de giro");
        mostrarFragment(bundle);
    }

    public void infoMagnetic(View view) {
        bundle = new Bundle();
        bundle.putString("descripcion", "Mide la intensidad de los campos geomagnéticos de la tierra en µT (densidad de flujo magnético)");
        bundle.putString("uso", "Brújula (polo norte magnético)\n" +
                "Smart cover (Estuches que bloquean la pantalla)\n");
        bundle.putString("datos", "'TYPE_MAGNETIC_FIELD' \n ");
        bundle.putString("sensor", "Magnetometro");
        mostrarFragment(bundle);
    }

    public void infoProximity(View view) {
        bundle = new Bundle();
        bundle.putString("descripcion", "Mide la proximidad de un objeto en cm.");
        bundle.putString("uso", "detección de objeto a corta distancia.");
        bundle.putString("datos", "'TYPE_PROXIMITY' \n ");
        bundle.putString("sensor", "Proximidad");
        mostrarFragment(bundle);
    }

    public void infoIluminosidad(View view) {
        bundle = new Bundle();
        bundle.putString("descripcion", "Mide el nivel de luz ambiental en lx (Unidad de densidad de luz)");
        bundle.putString("uso", "Control automático de brillo en pantalla.");
        bundle.putString("datos", "'TYPE_LIGHT' \n ");
        bundle.putString("sensor", "Sensor de luz");
        mostrarFragment(bundle);
    }

    public void infoTemperatura(View view) {
        bundle = new Bundle();
        bundle.putString("descripcion", "Mide el nivel de luz ambiental en lx (Unidad de densidad de luz)");
        bundle.putString("uso", "Control automático de brillo en pantalla.");
        bundle.putString("datos", "'TYPE_LIGHT' \n ");
        bundle.putString("sensor", "Sensor de luz");
        mostrarFragment(bundle);
    }

    public void infoPresion(View view) {
        bundle = new Bundle();
        bundle.putString("descripcion", "Mide la presión del aire ambiental en mbar, Medellin = 853.26");
        bundle.putString("uso", "Detección de altitud, ayudar al GPS.");
        bundle.putString("datos", "'TYPE_PRESSURE' \n ");
        bundle.putString("sensor", "Barómetro");
        mostrarFragment(bundle);
    }

    private void mostrarFragment(Bundle bundle) {
        DialogFragment dialogFragment =  new InfoFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(), "Dialog");
    }
}