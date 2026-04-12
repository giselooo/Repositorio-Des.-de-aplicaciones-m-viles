package com.example.clase7;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MiCliente {

    private String url = "https://function-bun-production-298f.up.railway.app/api/characters";
    OkHttpClient client = new OkHttpClient();
    // Definimos que vamos a mandar un JSON
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    // METODO PARA LEER (GET)
    public ArrayList<String> getElements() {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) return new ArrayList<>();

            String respuesta = response.body().string();
            ArrayList<String> elementos = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(respuesta);
            JSONArray array = jsonObject.getJSONArray("characters");

            // Corrección: usar < en lugar de <= para evitar el IndexOutOfBounds
            for (int i = 0; i < array.length(); i++) {
                String elemento = array.getString(i);
                elementos.add(elemento);
            }
            return elementos;

        } catch (IOException | JSONException e) {
            Log.e("MiCliente", "Error en GET: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // METODO PARA GUARDAR (POST)
    public void addCharacter(String name) {
        // Construimos el JSON manualmente: {"name":"Valor"}
        String json = "{\"name\":\"" + name + "\"}";

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Log.d("MiCliente", "Personaje guardado en Railway: " + name);
            } else {
                Log.e("MiCliente", "Error en servidor: " + response.code());
            }
        } catch (IOException e) {
            Log.e("MiCliente", "Error de red al hacer POST: " + e.getMessage());
        }
    }
}

