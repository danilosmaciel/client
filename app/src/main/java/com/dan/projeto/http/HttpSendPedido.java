package com.dan.projeto.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.dan.projeto.dao.EnderecoDAO;
import com.dan.projeto.dao.UsuarioDAO;
import com.dan.projeto.database.CarregaBanco;
import com.dan.projeto.helper.Funcoes;
import com.dan.projeto.model.Endereco;
import com.dan.projeto.model.Pedido;
import com.dan.projeto.model.PedidoResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpSendPedido extends AsyncTask<String, Void, PedidoResponse> {

    private final Context _ctx;
    private final Pedido _pedido;
    private final String URLQAS = Funcoes.URL_SERVIDOR+"pedidos/novo";
    private ProgressDialog      _dialog;
    private String              _broadcastIntent;

    public HttpSendPedido(Context ctx, Pedido pedido, String enviaPedido) {
        _ctx = ctx;
        _pedido = pedido;
        _dialog = new ProgressDialog(ctx);
        _broadcastIntent = enviaPedido;
    }

    @Override
    protected void onPreExecute() {
        _dialog.setMessage("Loading...");
        _dialog.show();
    }

    @Override
    protected PedidoResponse doInBackground(String... strings) {
        try {
            URL url = new URL(String.format(URLQAS));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.connect();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            String jsonPedido   = new Gson().toJson(_pedido, Pedido.class);
            writer.write(jsonPedido);
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
                PedidoResponse pedidoResponse = new Gson().fromJson(resposta.toString(), PedidoResponse.class);
                return pedidoResponse;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(PedidoResponse pedido){
        if (_dialog.isShowing()) {
            _dialog.dismiss();
        }
        Intent intent = new Intent(_broadcastIntent);
        intent.putExtra("numeroPedido", pedido != null ? pedido.getNumeroPedido() : "");
        intent.putExtra("status", pedido != null ?  pedido.getStatus() : "falhaCadastroGeral");
        LocalBroadcastManager.getInstance(_ctx).sendBroadcast(intent);
    }

    private Gson convertePedidoJson(Pedido pedido) {
        Gson pedidoJson = new Gson();
        pedidoJson.toJson(pedido.toJson());
        return pedidoJson;
    }
}


