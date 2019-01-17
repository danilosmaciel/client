package com.dan.projeto.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dan.projeto.helper.Funcoes;
import com.dan.projeto.model.Usuario;
import com.dan.projeto.model.UsuarioResponse;
import com.dan.projeto.view.InicialActivity;
import com.dan.projeto.view.PrincipalActivity;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;



    public class HttpRequestLogin extends AsyncTask<Void, Void, UsuarioResponse> {

        private final String _login;
        private final String _senha;
        private ProgressDialog _dialog;
        private Context _ctx;
        private String _broadcastIntent;

        private final String URLQAS = Funcoes.URL_SERVIDOR+"cliente/login";

        public HttpRequestLogin(String login, String senha, Context ctx, String autenticaLogin){
            _login = login;
            _senha = senha;
            _dialog = new ProgressDialog(ctx);
            _ctx = ctx;
            _broadcastIntent = autenticaLogin;
        }


        @Override
        protected void onPreExecute() {
            _dialog.setMessage("Loading...");
            _dialog.show();
        }

        @Override
        protected UsuarioResponse doInBackground(Void... voids) {
            UsuarioResponse usuarioResponse = null;
            StringBuilder resposta = new StringBuilder();

            if(!(Funcoes.stringVazia(_login) && Funcoes.stringVazia(_senha))){
                try {
                    //URL url = new URL(String.format("http://www.projetox.kinghost.net:21015/cliente/login/{1}/{2}",_login,_senha));
                    //URL url = new URL(URLQAS + "/"+_login+"/"+_senha);
                    URL url = new URL(URLQAS);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.connect();

                    Usuario user = new Usuario();
                    user.setEmail(_login);
                    user.setSenha(_senha);
                    String jsonUsuario = new Gson().toJson(user, Usuario.class);

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    writer.write(jsonUsuario);
                    writer.flush();

                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;

                        while ((line = reader.readLine()) != null) {
                            resposta.append(line);
                        }

                        //JSONObject jsonObj = new JSONObject(resposta.toString());
                        usuarioResponse = new Gson().fromJson(resposta.toString(), UsuarioResponse.class);
                        return usuarioResponse;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return usuarioResponse;
        }

        @Override
        protected void onPostExecute(UsuarioResponse usuario) {
            if (_dialog.isShowing()) {
                _dialog.dismiss();
            }


            Intent intent = new Intent(_broadcastIntent);
            intent.putExtra("usuario",usuario);
            // Intent intent = new Intent(_ctx, InicialActivity.class);
            /*
            intent.putExtra("id", usuario != null ?  usuario.getId() : "");
            intent.putExtra("usuario", usuario != null ?  usuario.getNome() : "");
            intent.putExtra("email", usuario != null ?  usuario.getEmail() : "");
            intent.putExtra("status", usuario != null ? usuario.getStatus() : "DeuPau");
            intent.putExtra("tolkienAcesso", usuario != null ? usuario.getTolkienAcesso() : "DeuPau");
            */
            LocalBroadcastManager.getInstance(_ctx).sendBroadcast(intent);
        }
    }


