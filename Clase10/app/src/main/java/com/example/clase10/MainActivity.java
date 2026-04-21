package com.example.clase10;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ListView listaClima;
    private ArrayList<String> datosParaLaVista = new ArrayList<>();
    private ArrayAdapter<String> adaptador;


    private final String API_URL = "https://api.weatherapi.com/v1/current.json?key=856754ed5ee2439a8cd05823261804&q=Mexico%20City&lang=es";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listaClima = findViewById(R.id.listaClima);
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosParaLaVista);
        listaClima.setAdapter(adaptador);


        ejecutarFlujoDeDatos();
    }

    private void ejecutarFlujoDeDatos() {
        AsyncTask.execute(() -> {
            WeatherDao dao = AppDatabase.getInstance(this).weatherDao();


            if (dao.getAll().isEmpty()) {
                dao.insert(new Weather("Sábado 11", 24.5, "Soleado"));
                dao.insert(new Weather("Domingo 12", 22.0, "Nublado"));
            }


            obtenerClimaDesdeInternet();


            actualizarPantalla();
        });
    }

    private void obtenerClimaDesdeInternet() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(API_URL).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JSONObject json = new JSONObject(jsonData);
                JSONObject current = json.getJSONObject("current");

                double temp = current.getDouble("temp_c");
                String condicion = current.getJSONObject("condition").getString("text");


                SimpleDateFormat sdf = new SimpleDateFormat("EEEE d 'de' MMMM", new Locale("es", "MX"));
                String fechaHoy = sdf.format(Calendar.getInstance().getTime());


                WeatherDao dao = AppDatabase.getInstance(this).weatherDao();
                dao.insert(new Weather(fechaHoy, temp, condicion));

                actualizarPantalla();
                Log.i("Gis", "API Exitosa: Dato de hoy guardado.");
            }
        } catch (Exception e) {
            Log.e("Gis", "Error al conectar con WeatherAPI: " + e.getMessage());
        }
    }

    private void actualizarPantalla() {
        AsyncTask.execute(() -> {
            WeatherDao dao = AppDatabase.getInstance(this).weatherDao();
            List<Weather> registros = dao.getAll();

            List<String> listaSimple = new ArrayList<>();
            for (Weather w : registros) {

                String renglon = w.fecha.toUpperCase() + "\n" + w.grados + "°C - " + w.condicion;
                listaSimple.add(renglon);
            }

            runOnUiThread(() -> {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, listaSimple) {

                    @Override
                    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                        android.view.View view = super.getView(position, convertView, parent);
                        android.widget.TextView text = (android.widget.TextView) view.findViewById(android.R.id.text1);


                        text.setTextColor(android.graphics.Color.BLACK);
                        text.setTypeface(null, android.graphics.Typeface.BOLD); // Negritas para que se vean más
                        text.setAlpha(1.0f);

                        return view;
                    }
                };
                listaClima.setAdapter(adapter);
            });
        });
    }
    }


    /*@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), ( v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
        });

        AsyncTask.execute(() -> {
        UserDao userDao = AppDatabase.getInstance(this).userDao();
        userDao.insertAll(new User(1, "Maria", "Félix"));
        List<User> usuarios = userDao.getAll();

        runOnUiThread(() -> {

        for (User user : usuarios){
            Log.i("Gis", "Usuario firstName: " + user.firstName );
            Log.i("Gis", "Usuario lastName: " + user.lastName );
            Log.i("Gis", "Usuario uuid: " + user.uid );
        }

    });
});}}*/