package com.example.travelpet.domain;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonRequest {
    public static String request(String uri ) throws Exception {

        //fazer aqui a conexão com a uri, que agente vai formar , la no parâmetro da “String uri”
        URL url = new URL( uri );
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader r = new BufferedReader(new InputStreamReader(in));

        // E vamos obter cada um dos registros dos dados enviados nos pacotes
        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            jsonString.append(line);
        }

        urlConnection.disconnect();

        //E ae vamos retornar essa String json “jsonString”, e depois vamos ter
        // que criar as entidades que vão trabalhar com esse retorno aqui:
        return jsonString.toString();
    }
}
