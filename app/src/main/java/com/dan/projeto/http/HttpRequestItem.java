package com.dan.projeto.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.dan.projeto.helper.Funcoes;
import com.dan.projeto.model.Item;
import com.dan.projeto.model.UsuarioResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HttpRequestItem extends AsyncTask<Void, Void, List<Item>> {

    private ProgressDialog dialog;
    public HttpRequestItem(Activity act){
        dialog = new ProgressDialog(act);
    }

    private final String URLQAS = Funcoes.URL_SERVIDOR+"produtos/json";

    protected void onPreExecute() {
        dialog.setMessage("Carregando aguarde...");
        dialog.show();
    }

    @Override
    protected  List<Item> doInBackground(Void... voids) {
        StringBuilder resposta = new StringBuilder();
        List<Item> listaItens = new ArrayList<>();

            try {
                URL url = new URL(URLQAS);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setConnectTimeout(10000);

                connection.connect();

                int responseCode = connection.getResponseCode();
                Log.w("RetonoGetProdutos",connection.getResponseMessage());
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        resposta.append(line);
                    }
                    try {
                        //JSONObject jsonObj = new JSONObject(resposta.toString());
                        //JSONArray jsonArray = jsonObj.getJSONArray("itens");
                        // JSONObject jsonItens = jsonObj.getJSONObject("itens");
                        JSONArray jsonArray =  new JSONArray(resposta.toString());
                        for(int i = 0 ; i < jsonArray.length() ; i++){
                            listaItens.add(new Gson().fromJson(jsonArray.getJSONObject(i).toString(), Item.class));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                /*
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    resposta.append(scanner.next());
                }
                */
            } catch (IOException e) {
                e.printStackTrace();
            }
        /*
        try {
            //JSONObject jsonObj = new JSONObject(resposta.toString());
            //JSONArray jsonArray = jsonObj.getJSONArray("itens");
           // JSONObject jsonItens = jsonObj.getJSONObject("itens");
            JSONArray jsonArray =  new JSONArray(resposta.toString());
            for(int i = 0 ; i < jsonArray.length() ; i++){
                listaItens.add(new Gson().fromJson(jsonArray.getJSONObject(i).toString(), Item.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
        //return resposta.toString();
        //new Gson().fromJson(resposta.toString(), Item.class);
        return listaItens;
    }

    @Override
    protected void onPostExecute(List<Item> items) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
