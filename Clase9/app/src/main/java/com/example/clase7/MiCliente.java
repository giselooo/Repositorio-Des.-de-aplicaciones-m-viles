package com.example.clase7;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class MiCliente {

    private String url = "https://function-bun-production-298f.up.railway.app/api/characters";
    OkHttpClient client = new OkHttpClient();

    public ArrayList<Personaje> getElements() {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            ArrayList<Personaje> elementos = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray array = jsonObject.getJSONArray("characters");
            for (int i = 0; i <= array.length() - 1; i++){
                JSONObject elemento = array.getJSONObject(i);
                String name = elemento.getString("name");
                String desc = elemento.getString("desc");
                String photo = elemento.getString("photo");
                int atack = elemento.getInt("atack");
                int def = elemento.getInt("def");
                Personaje personaje = new Personaje(
                  name,
                  desc,
                  photo,
                  atack,
                  def
                );
                elementos.add(personaje);
            }
            return elementos;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
