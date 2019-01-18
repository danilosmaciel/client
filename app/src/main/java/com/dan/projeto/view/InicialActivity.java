package com.dan.projeto.view;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dan.projeto.PrincipalActivity;
import com.dan.projeto.R;
import com.dan.projeto.controller.MainController;
import com.dan.projeto.helper.Funcoes;
import com.dan.projeto.model.UsuarioResponse;

public class InicialActivity extends AppCompatActivity implements View.OnClickListener{
    MainController _controller;
    AlertDialog    _dlgCadLogin;
    private Button _btnCadastro;
    private Button _btnEntrar;
    private Button _btnSair;
    private Button _btnBackupBanco;
    private EditText _edtLogin;
    private EditText _edtSenha;
    private Button _btnGravarCadastro;
    private Button _btnCancelarCadastro;
    private EditText _edtNomeCadastro;
    private EditText _edtSenhaCadastro;
    private EditText _edtEnderecoCadastro;
    private EditText _edtNroEnderecoCadastro;
    private EditText _edtTelefoneFixoCadastro;
    private EditText _edtTelefoneMovelCadastro;
    private EditText _edtEmailCadastro;
    private EditText _edtBairroCadastro;
    private EditText _edtCidadeCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicial_activity);
        _controller = new MainController(this);
        Funcoes.salvaContexto(this);
        setTitle("Identifique-se");
    }



    @Override
    protected void onStart() {
        super.onStart();
        initComponentes();

        /*
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            String status = bundle.getString("status");
            if(status.equals(Funcoes.RETORNO_LOGIN_FALHA)){
                Handler handle = new Handler();
                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mostraMsgLoginIncorreto();
                    }
                }, 2000);
            }else if(status.equals(Funcoes.RETORNO_CADASTRO_FALHA)){
                Handler handle = new Handler();
                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mostraMsgErroNoCadastro();
                    }
                }, 2000);
            }
        }
        */

    }

    private void mostraMsgErroNoCadastro() {
        Funcoes.showToastLongo(this,"Erro no cadastro de usuário!");
    }

    private void mostraMsgLoginIncorreto() {
        Funcoes.showToastLongo(this,"Usuário não reconhecido!");
    }


    private void initComponentes() {

        _btnCadastro         = findViewById(R.id.main_btnCadastrar);
        _btnEntrar           = findViewById(R.id.main_btnEntrar);
        _btnSair             = findViewById(R.id.main_btnSair);
        _edtLogin            = findViewById(R.id.main_edtLogin);
        _edtSenha            = findViewById(R.id.main_edtSenha);
        _btnBackupBanco      = findViewById(R.id.main_btnBackupBanco);

        _btnCadastro.setOnClickListener(this);
        _btnEntrar.setOnClickListener(this);
        _btnSair.setOnClickListener(this);
        _btnBackupBanco.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_btnCadastrar:
                _dlgCadLogin = exibeTelaCadastroLogin();
                _dlgCadLogin.show();
                break;
            case R.id.main_btnEntrar:
                processaLogin();
                break;
            case R.id.main_btnSair:
                this.onDestroy();
                System.exit(0);
                break;
            case R.id.main_dlg_cadatro_btnGravar:
                processaClienteNovo();
                break;
            case R.id.main_dlg_cadatro_btnCancelar:
               // _controller.getItensHttp();
                _dlgCadLogin.dismiss();
                break;
            case R.id.main_btnBackupBanco:

                break;
        }
    }

    private void processaLogin() {
        if(Funcoes.semConexao()){
            Funcoes.showToastLongo(this,"É necessário estar conectado em uma rede para prosseguir!");
            return;
        }
        if (_edtLogin == null || _edtSenha == null) {
            return;
        }
        String login = _edtLogin.getText().toString();
        String senha = _edtSenha.getText().toString();

        if (Funcoes.stringVazia(login) || Funcoes.stringVazia(senha)) {
            Funcoes.showToastLongo(this,"Necessário preencher todos os campos!");
            return;
        }
        _controller.validarLogin(login, senha);

    }

    private boolean processaClienteNovo() {
        if(Funcoes.semConexao()){
            Funcoes.showToastLongo(this,"É necessário estar conectado em uma rede para prosseguir!");
            return false;
        }
        //if(_controller.usuarioExiste(_edtEmailCadastro.getText().toString())){
        //    Funcoes.showToastLongo(this,"Usuário já cadastrado!");
        //    return false;
       // }
        if(Funcoes.camposVazios(_edtNomeCadastro, _edtSenhaCadastro,_edtEmailCadastro)){
            Funcoes.showToastLongo(this,"Os campos marcados(*) não podem ficar vazios!");
            return false;
        }
        _controller.gravarUsuario(_edtNomeCadastro,_edtSenhaCadastro,_edtTelefoneFixoCadastro,_edtTelefoneMovelCadastro,_edtEmailCadastro);

        return false;
    }

    public AlertDialog exibeTelaCadastroLogin() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        alert.setTitle("Cadastro de usuário");
        View layoutLogin = inflater.inflate(R.layout.inicial_dlg_cadusuario, null);
        alert.setView(layoutLogin);
        configuraComponentesCadastro(layoutLogin);
        return alert.create();
    }

    private void configuraComponentesCadastro(View view) {
        _btnGravarCadastro        = view.findViewById(R.id.main_dlg_cadatro_btnGravar);
        _btnCancelarCadastro      = view.findViewById(R.id.main_dlg_cadatro_btnCancelar);
        _edtNomeCadastro          = view.findViewById(R.id.main_dlg_cadatro_edtNome);
        _edtSenhaCadastro         = view.findViewById(R.id.main_dlg_cadatro_edtSenha);
        _edtEnderecoCadastro      = view.findViewById(R.id.main_dlg_cadatro_edtRua);
        _edtNroEnderecoCadastro   = view.findViewById(R.id.main_dlg_cadatro_edtNro);
        _edtTelefoneFixoCadastro  = view.findViewById(R.id.main_dlg_cadatro_edtTelefoneFixo);
        _edtTelefoneMovelCadastro = view.findViewById(R.id.main_dlg_cadatro_edtTelefoneCelular);
        _edtEmailCadastro         = view.findViewById(R.id.main_dlg_cadatro_edtEmail);
        _edtBairroCadastro        = view.findViewById(R.id.main_dlg_cadatro_edtBairro);
        _edtCidadeCadastro        = view.findViewById(R.id.main_dlg_cadatro_edtCidade);


        _btnGravarCadastro.setOnClickListener(this);
        _btnCancelarCadastro.setOnClickListener(this);
    }

    private BroadcastReceiver broadcastReceiverLogin= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    UsuarioResponse usuarioResponse =(UsuarioResponse) bundle.getSerializable("usuario");
                   // String status = bundle.getString("status");
                    if(usuarioResponse != null){
                        if(usuarioResponse.getStatus().equals(Funcoes.RETORNO_LOGIN_SUCESSO)) {
                            //chama a tela princiapl
                            Intent novaTelaIntent = new Intent(InicialActivity.this, PrincipalActivity.class);
                            novaTelaIntent.putExtra("usuario",usuarioResponse);
                            startActivity(novaTelaIntent);
                        }else {
                            mostraMsgLoginIncorreto();
                        }
                    }

                }
            }

        }
    };
    private BroadcastReceiver broadcastReceiverCadastroCliente = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    String usuario = bundle.getString("usuario");
                    String status = bundle.getString("status");
                    if(status.equals(Funcoes.RETORNO_CADASTRO_SUCESSO)){
                        Funcoes.showToastLongo(InicialActivity.this,"Cadastro realizado com sucesso!");
                        _dlgCadLogin.dismiss();
                    }else{
                        mostraMsgErroNoCadastro();
                    }
                }
            }

        }
    };


    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverCadastroCliente, new IntentFilter(Funcoes.ENVIA_CADASTRO_CLIENTE));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverLogin, new IntentFilter(Funcoes.AUTENTICA_LOGIN));
    }

    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(InicialActivity.this).unregisterReceiver(broadcastReceiverCadastroCliente);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverLogin);
    }
}
