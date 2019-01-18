package com.dan.projeto;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dan.projeto.controller.PrincipalController;
import com.dan.projeto.dao.EnderecoDAO;
import com.dan.projeto.database.CarregaBanco;
import com.dan.projeto.helper.Funcoes;
import com.dan.projeto.http.HttpSendEndereco;
import com.dan.projeto.model.Endereco;
import com.dan.projeto.model.Pedido;
import com.dan.projeto.model.UsuarioResponse;
import com.dan.projeto.view.PedidoItemActivity;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements View.OnClickListener {

    public  PrincipalController _controller;
    private TextView _tvBoasVindas;
    private Button _btnIniciarPedido;
    private Button _btnHistoricoPedidos;
    private ListView _lstHistPedidos;
    private Button _btnGravarCadastro;
    private Button _btnCancelarCadastro;
    private Button _btnCadastroEndereco;
    private EditText _edtNomeRua;
    private EditText _edtNumero;
    private EditText _edtBairro;
    private EditText _edtCidade;
    private AlertDialog _dlgCadastroEndereco;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        setTitle("Tela Principal");
        Bundle extras = getIntent().getExtras();
        UsuarioResponse usuarioResponse;
        if(extras != null) {
            usuarioResponse = (UsuarioResponse)extras.getSerializable("usuario");
            if(usuarioResponse  != null){
                _controller = new PrincipalController(this, usuarioResponse);
            }else {
                Funcoes.showToastLongo(this, "Erro na aplicação");
                Handler handle = new Handler();
                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Funcoes.sairDoSistema();
                    }
                }, 5000);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initComponentes();

    }

    private void initComponentes() {
        _tvBoasVindas        = findViewById(R.id.pricipal_activity_tvBoasVindas);
        _btnIniciarPedido    = findViewById(R.id.principal_activity_btnIniciarPedido);
        _btnHistoricoPedidos = findViewById(R.id.pricipal_activity_btnHistoricoPedidos);
        _lstHistPedidos      = findViewById(R.id.principal_activity_lstHistoricPedidos);
        _btnCadastroEndereco = findViewById(R.id.principal_activity_btnCadatrarEndereco);

        _btnIniciarPedido.setOnClickListener(this);
        _btnHistoricoPedidos.setOnClickListener(this);
        _btnCadastroEndereco.setOnClickListener(this);
        _tvBoasVindas.setText(_tvBoasVindas.getText().toString() + " " + _controller.getUsuarioNome());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.principal_activity_btnIniciarPedido:
                Intent it = new Intent(PrincipalActivity.this, PedidoItemActivity.class);
                it.putExtra("usuario", _controller.getUsuario());
                startActivity(it);
                break;
            case R.id.pricipal_activity_btnHistoricoPedidos:
                carregaListaPedido();
                break;
            case R.id.principal_dlg_cadendereco_btnGravar:
                enviaEndereco(getEnderecoEntrega());
                break;
            case R.id.principal_dlg_cadendereco_btnCancelar:
                _dlgCadastroEndereco.dismiss();
                break;
            case R.id.principal_activity_btnCadatrarEndereco:
                _dlgCadastroEndereco = exibeTelaCadastroEndereco();
                _dlgCadastroEndereco.show();
                break;
        }

    }

    private Endereco getEnderecoEntrega() {
        Endereco endereco = new Endereco();
        endereco.setCliente(_controller.getUsuarioId());
        endereco.setRua(_edtNomeRua.getText().toString());
        endereco.setNumero(_edtNumero.getText().toString());
        endereco.setBairro(_edtBairro.getText().toString());
        endereco.setCidade(_edtCidade.getText().toString());
        return endereco;
    }

    private void gravarEnderecoNoBd(){
        Endereco endereco = getEnderecoEntrega();
        EnderecoDAO enderecoDAO = new CarregaBanco().getDb(this).getEndereooDao();
        enderecoDAO.gravarEndereco(endereco);
    }

    private void carregaListaPedido() {
        List<Pedido> listaPedidos = _controller.carregaPedidos(_controller.getUsuarioId());
        if(listaPedidos != null){
            ArrayAdapter<Pedido> adpPedidos = new ArrayAdapter<Pedido>(this,android.R.layout.simple_list_item_1, listaPedidos);
            _lstHistPedidos.setAdapter(adpPedidos);
        }else{
            Funcoes.showToastLongo(this,"Não existem pedidos para exibir!");
        }

    }

    public AlertDialog exibeTelaCadastroEndereco() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        alert.setTitle("Cadastro de endereços");
        View layoutLogin = inflater.inflate(R.layout.principal_dlg_cadendereco, null);
        alert.setView(layoutLogin);
        configuraComponentesCadastro(layoutLogin);
        return alert.create();
    }

    private void configuraComponentesCadastro(View view) {
        _btnGravarCadastro        = view.findViewById(R.id.principal_dlg_cadendereco_btnGravar);
        _btnCancelarCadastro      = view.findViewById(R.id.principal_dlg_cadendereco_btnCancelar);
        _edtNomeRua               = view.findViewById(R.id.principal_dlg_cadendereco_edtLogradouro);
        _edtNumero                = view.findViewById(R.id.principal_dlg_cadendereco_edtNumero);
        _edtBairro                = view.findViewById(R.id.principal_dlg_cadendereco_edtBairro);
        _edtCidade                = view.findViewById(R.id.principal_dlg_cadendereco_edtCidade);

        _btnGravarCadastro.setOnClickListener(this);
        _btnCancelarCadastro.setOnClickListener(this);
    }

    public void enviaEndereco(Endereco endereco) {
        new HttpSendEndereco(this,endereco, Funcoes.CADASTRA_ENDERECO).execute();
    }

    private BroadcastReceiver broadcastReceiverEndereco= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    String numeroPedido = bundle.getString("numeroPedido");
                    String status = bundle.getString("status");
                    if(status.equals(Funcoes.RETORNO_ENDERECO_SUCESSO)){
                        exibirMsgEnderecoSucesso();
                    }else{
                        exibirMsgEnderecoErro();
                    }
                }
            }
        }
    };


    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverEndereco, new IntentFilter(Funcoes.CADASTRA_ENDERECO));
    }

    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverEndereco);
    }
    private void exibirMsgEnderecoSucesso(){
        Funcoes.showToastLongo(this,"Endereço gravado com sucesso!");
        gravarEnderecoNoBd();
        _dlgCadastroEndereco.dismiss();
    }
    private void exibirMsgEnderecoErro(){
        Funcoes.showToastLongo(this,"Não foi possível gravar o endereço!\n Ele já existe?");
    }
}
