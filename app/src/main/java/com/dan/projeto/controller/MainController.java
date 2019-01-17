package com.dan.projeto.controller;

import android.content.Context;
import android.widget.EditText;

import com.dan.projeto.dao.UsuarioDAO;
import com.dan.projeto.database.CarregaBanco;
import com.dan.projeto.helper.Funcoes;
import com.dan.projeto.http.HttpRequestLogin;
import com.dan.projeto.http.HttpSendCadastroLogin;
import com.dan.projeto.model.Usuario;

public class MainController {
    private Context _contexto;
    private Usuario _usuario;

    public MainController(Context _contexto) {
        this._contexto = _contexto;
        _usuario = null;
    }

    public boolean gravarUsuario(EditText edtNomeCadastro, EditText edtSenhaCadastro, EditText edtTelefoneFixoCadastro, EditText edtTelefoneMovelCadastro, EditText edtEmailCadastro) {
        boolean resultado   = false;
        String nome         = edtNomeCadastro.getText().toString();
        String senha        = edtSenhaCadastro.getText().toString();
        String telefoneFixo = edtTelefoneFixoCadastro.getText().toString();
        String celular      = edtTelefoneMovelCadastro.getText().toString();
        String email        = edtEmailCadastro.getText().toString();
        _usuario = new Usuario("", email, nome, celular, telefoneFixo, senha);
        //String id, String email, String nome, String telefoneCelular, String telefoneFixo, String senha, String tolkienAcesso

        /*
       if(transmitirClienteNovo(_usuario)){
           UsuarioDAO usuarioDAO = new CarregaBanco().getDb(_contexto).getUsuarioDao();
           usuarioDAO.gravarUsuario(_usuario);
           resultado = true;
       }

        */
        transmitirClienteNovo(_usuario);

        return resultado;
    }

    private void transmitirClienteNovo(Usuario _usuario) {
        new HttpSendCadastroLogin( _contexto, _usuario, Funcoes.ENVIA_CADASTRO_CLIENTE).execute();
    }

    public void validarLogin(String login, String senha) {
        new HttpRequestLogin(login, senha, _contexto,Funcoes.AUTENTICA_LOGIN).execute();
    }

    public boolean usuarioExiste(String edtEmailCadastro) {
        UsuarioDAO usuarioDAO = new CarregaBanco().getDb(_contexto).getUsuarioDao();
        return usuarioDAO.usuarioExiste(edtEmailCadastro) != null;
    }

   public boolean autenticaLoginHttp(String login, String senha){
       if(login.equals("a") && senha.equals("a")){
           _usuario = new Usuario("10", "a","Dan","","","a");
           return true;
       }
       return false;
   }
}