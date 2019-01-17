package com.dan.projeto.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.dan.projeto.helper.Funcoes;
import com.dan.projeto.model.Usuario;
import com.dan.projeto.model.UsuarioResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class HttpSendCadastroLogin extends AsyncTask<Void, Void, UsuarioResponse> {

    private final Usuario _usuario;
    private ProgressDialog _dialog;
    private final Context _ctx;
    private String _broadcastIntent;
    private final String URLQAS = Funcoes.URL_SERVIDOR+"cliente/cadastro";

    public HttpSendCadastroLogin(Context ctx, Usuario usuario, String enviaCadastroCliente) {
        _usuario = usuario;
        _dialog = new ProgressDialog(ctx);
        _ctx = ctx;
        _broadcastIntent = enviaCadastroCliente;
    }

    @Override
    protected void onPreExecute() {
       _dialog.setMessage("Loading...");
        _dialog.show();
    }

    @Override
    protected  UsuarioResponse doInBackground(Void... voids) {
        UsuarioResponse usuarioResponse = null;

        if ((_usuario != null)) {
            try {
                URL url = new URL(String.format(URLQAS));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                connection.connect();

                //JSONObject jsonUsuario = new JSONObject(_usuario);
                //Usuario jsonUsuario = new Gson().fromJson(_usuario.toString(), Usuario.class);
                String jsonUsuario = new Gson().toJson(_usuario, Usuario.class);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(jsonUsuario);
                writer.flush();

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder resposta = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        resposta.append(line);
                    }

                    //JSONObject jsonObj = new JSONObject(resposta.toString());
                    usuarioResponse = new Gson().fromJson(resposta.toString(), UsuarioResponse.class);
                    return usuarioResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
    @Override
    protected void onPostExecute(UsuarioResponse result){
        if (_dialog.isShowing()) {
            _dialog.dismiss();
        }
        Intent intent = new Intent(_broadcastIntent);
        intent.putExtra("usuario", result != null ? result.getEmail() : "");
        intent.putExtra("status", result != null ?  result.getStatus() : "falhaCadastroGeral");
        LocalBroadcastManager.getInstance(_ctx).sendBroadcast(intent);
    }
}



