package com.dan.projeto.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.dan.projeto.dao.EnderecoDAO;
import com.dan.projeto.database.CarregaBanco;
import com.dan.projeto.helper.Funcoes;
import com.dan.projeto.model.Endereco;
import com.dan.projeto.model.EnderecoResponse;
import com.dan.projeto.model.Pedido;
import com.dan.projeto.model.PedidoResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpSendEndereco extends AsyncTask<String, Void, EnderecoResponse> {

    private final Context _ctx;
    private final String URLQAS = Funcoes.URL_SERVIDOR+"endereco/cadastro";
    private ProgressDialog _dialog;
    private String         _broadcastIntent;
    private Endereco       _endereco;


    public HttpSendEndereco(Context ctx, Endereco endereco, String cadastraEndereco) {
        _ctx = ctx;
        _endereco = endereco;
        _dialog = new ProgressDialog(ctx);
        _broadcastIntent = cadastraEndereco;
    }

    @Override
    protected void onPreExecute() {
        _dialog.setMessage("Loading...");
        _dialog.show();
    }

    @Override
    protected EnderecoResponse doInBackground(String... strings) {
        try {
            URL url = new URL(String.format(URLQAS));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.connect();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            String jsonEndereco = new Gson().toJson(_endereco, Endereco.class);

            writer.write(jsonEndereco);
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
                EnderecoResponse enderecoResponse = new Gson().fromJson(resposta.toString(), EnderecoResponse.class);
                return enderecoResponse;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(EnderecoResponse enderecoResponse){
        if (_dialog.isShowing()) {
            _dialog.dismiss();
        }
        Intent intent = new Intent(_broadcastIntent);
        intent.putExtra("id", enderecoResponse != null ? enderecoResponse.getId() : "");
        intent.putExtra("status", enderecoResponse != null ?  enderecoResponse.getStatus() : "falhaCadastroGeral");
        LocalBroadcastManager.getInstance(_ctx).sendBroadcast(intent);

    }

    private Gson convertePedidoJson(Pedido pedido) {
        Gson pedidoJson = new Gson();
        pedidoJson.toJson(pedido.toJson());
        return pedidoJson;
    }
}


