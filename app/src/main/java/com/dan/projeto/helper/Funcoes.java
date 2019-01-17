package com.dan.projeto.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.Toast;

import com.dan.projeto.view.PrincipalActivity;

import static android.support.v4.app.ActivityCompat.finishAffinity;


public class Funcoes {

    static Activity _aplicacaoContexto  = null;
    private static ProgressDialog _dialogLoad;

    public static final String ENVIA_CADASTRO_CLIENTE    = "cadastroCliente";
    public static final String AUTENTICA_LOGIN           = "autenticaLogin";
    public static final String CADASTRA_ENDERECO         = "cadastraEndereco";
    public static final String ENVIA_PEDIDO              = "enviaPedido";
    public static final String RETORNO_LOGIN_SUCESSO     = "loginSucesso";
    public static final String RETORNO_LOGIN_FALHA       = "loginFalhou";
    public static final String RETORNO_CADASTRO_SUCESSO  = "cadastroSucesso";
    public static final String RETORNO_CADASTRO_FALHA    = "cadastroFalhou";
    public static final String RETORNO_CADASTRO_USUARO_EXISTENTE    = "usuarioJaExiste";
    public static final String RETORNO_PEDIDO_FALHA      = "pedidoFalhou";
    public static final String RETORNO_PEDIDO_SUCESSO    = "recebido";
    public static final String RETORNO_ENDERECO_SUCESSO  = "enderecorecebido";
    public static final String URL_SERVIDOR              = "http://192.168.0.12:21015/";

    public static void showToastLongo(Context ctx, String msg){
        Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
    }
    public static void showToastCurto(Context ctx,String msg){
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }
    public static boolean stringVazia(String texto){
        return texto == null || texto.trim().length() <= 0;
    }
    public static boolean stringVazia(String... elementos){
        if(!(elementos.length > 0))
            return false;

            for(String elemento : elementos){
                if(stringVazia(elemento)){
                    return false;
                }
            }


        return true;
    }

    public static boolean estaoVazios(String... textos){
        for(String elemento : textos){
            if(stringVazia(elemento)){
                return false;
            }
        }
        return true;
    }

    public static void salvaContexto(Activity ctx){
        _aplicacaoContexto = ctx;
        _dialogLoad  = new ProgressDialog(ctx);
    }

    public static Context getContextoAplicaco(){
        return _aplicacaoContexto;
    }

    public static boolean camposVazios(EditText... editTexts) {
        for(EditText elemento : editTexts){
            if(elemento != null && elemento.getText().toString().equals("")){
                return true;
            }
        }
        return false;
    }

    public static boolean estaConectado(){
        ConnectivityManager cm = (ConnectivityManager)_aplicacaoContexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean semConexao() {
        ConnectivityManager cm = (ConnectivityManager)_aplicacaoContexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return !(activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    public static void iniciaLoad() {
        _dialogLoad.setMessage("Loading...");
        _dialogLoad.show();
    }

    public static void encerraLoad() {

        if (_dialogLoad.isShowing()) {
            _dialogLoad.dismiss();
        }
    }

    public static void sairDoSistema() {
        if(_aplicacaoContexto != null){
            finishAffinity(_aplicacaoContexto);
        }
    }
}
