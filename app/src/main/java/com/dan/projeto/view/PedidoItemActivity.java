package com.dan.projeto.view;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dan.projeto.Cache.CacheItem;
import com.dan.projeto.R;
import com.dan.projeto.componentes.AdapterItem;
import com.dan.projeto.controller.PedidoItemContoller;
import com.dan.projeto.dao.EnderecoDAO;
import com.dan.projeto.database.CarregaBanco;
import com.dan.projeto.helper.Funcoes;
import com.dan.projeto.http.HttpRequestItem;
import com.dan.projeto.model.Endereco;
import com.dan.projeto.model.Item;

import com.dan.projeto.model.UsuarioResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class PedidoItemActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView            _listaItens;
    private AdapterItem         _adapterItens;
    private ArrayAdapter<Endereco> _adapterEnderecos;
    private AlertDialog            _dialogVenda;
    private AlertDialog            _dialogEnderecos;
    private PedidoItemContoller    _controller;
    private Button                 _btnFazerPedido;
    public final static  int       MENU_ENDERECO = 999;
    private Button                 _btnEnderecoContinuar;
    private Button                 _btnEndercoCancelar;
    private ListView               _lstEnderecos;
    //
    private Button   _btnAumentaQtde;
    private Button   _btnDiminuiQtde;
    private Button   _btnAceitar;
    private EditText _edtItemQtde;
    private Item itemPosicionado;
    private TextView tvTotal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedido_item_activity);
        this.setTitle("Pedido");
        Bundle extras = getIntent().getExtras();
        UsuarioResponse usuarioResponse;
        if(extras != null){
            usuarioResponse = (UsuarioResponse)extras.getSerializable("usuario");
            _controller = new PedidoItemContoller(this, usuarioResponse);
        }

        initComponentes();
        _dialogVenda = criaDialogVenda();
    }

    private void initComponentes() {
        _listaItens    = findViewById(R.id.pedido_item_lista);
        _btnFazerPedido  = findViewById(R.id.pedido_item_btnFazerPedido);


        _listaItens.setOnItemClickListener(this);
        _btnFazerPedido .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_controller.enderecoEstaVazio()){
                    Funcoes.showToastLongo(PedidoItemActivity.this,"Selecione um endereço para entrega!");
                    return;
                }
                if(_controller.getQtdeItensVendidos() > 0){

                        _controller.getMsgItensVedidos();
                        AlertDialog dlg = criaDialogFechamento().create();
                        tvTotal.setText(_controller.getMsgItensVedidos());
                        dlg.show();

                }else{
                    Funcoes.showToastLongo(PedidoItemActivity.this,"Não há itens vendidos!");
                }
            }
        });
        _dialogEnderecos = carregDialogEndereco();
    }

    private void carregaListas() {
        CacheItem cacheItem = CacheItem.getINSTANCE();

        _adapterItens = new AdapterItem( this, cacheItem.getItens());
        _listaItens.setAdapter(_adapterItens);
    }

    private void carregaEnderecos(){
        EnderecoDAO enderecoDAO = new CarregaBanco().getDb(this).getEndereooDao();
        _adapterEnderecos = new ArrayAdapter<Endereco>(this,android.R.layout.simple_list_item_1, enderecoDAO.getEnderecos(_controller.getClienteId()));
        if(_adapterEnderecos != null){
            _lstEnderecos.setAdapter( _adapterEnderecos);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        preencheItens();
        _controller.gerarNovoNumeroPedido();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_ENDERECO, Menu.NONE, "Endereço");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case MENU_ENDERECO:

                carregaEnderecos();
                _dialogEnderecos.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AlertDialog carregDialogEndereco() {
        AlertDialog.Builder dlgEndereco = new AlertDialog.Builder(this);
        dlgEndereco.setTitle("Selecione o endereço para entrega");
        LayoutInflater inflater = LayoutInflater.from(this);
        View layoutEndereco = inflater.inflate(R.layout.pedido_item_endereco, null);
        dlgEndereco.setView(layoutEndereco);
        configuraComponentesCadastro(layoutEndereco);
        return dlgEndereco.create();
    }

    private void configuraComponentesCadastro(View layoutEndereco) {
        _btnEnderecoContinuar    = layoutEndereco.findViewById(R.id.item_pedido_dlg_btnContinuar);
        _lstEnderecos            = layoutEndereco.findViewById(R.id.item_pedido_lstendereco);

        _btnEnderecoContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogEnderecos.dismiss();
            }
        });

        _lstEnderecos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Endereco endereco;
                endereco = _adapterEnderecos.getItem(position);
                Funcoes.showToastLongo(PedidoItemActivity.this,"Endereço "+endereco.getId()+" selecioando!");
                _controller.setEnderecoEntrega(endereco.getId());
            }
        });
    }

    private void preencheItens(){
        CacheItem cacheItem = CacheItem.getINSTANCE();
        cacheItem.insereTodos(getItensHttp());
        carregaListas();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        itemPosicionado = _adapterItens.getItem(position);
        _edtItemQtde.setText(String.valueOf(_controller.getQtdeItemVendida(itemPosicionado.getId())));
        _dialogVenda.show();
    }

    public AlertDialog.Builder criaDialogFechamento() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        alert.setTitle("Realizar o pedido?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _controller.enviaPedido();
            }
        });
        alert.setNegativeButton("Não", null);
        View layoutLogin = inflater.inflate(R.layout.pedido_item_dlg_fechamento, null);
        alert.setView(layoutLogin);
        configuraComponentesDlgFechamento(layoutLogin);
        return alert;
    }

    private void configuraComponentesDlgFechamento(View layoutLogin) {
        tvTotal = layoutLogin.findViewById(R.id.dlg_fechamento_itens);
    }

    public List<Item> getItensHttp(){

        try {
            List<Item> retorno = new HttpRequestItem(this).execute().get();
            return retorno;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BroadcastReceiver broadcastReceiverPedido = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    String numeroPedido = bundle.getString("numeroPedido");
                    String status = bundle.getString("status");
                    if(status.equals(Funcoes.RETORNO_PEDIDO_SUCESSO)){
                        executarEncerramentoPedido();
                    }else{
                        mostraMsgErroAoEnviar();
                    }
                }
            }

        }
    };

    private void executarEncerramentoPedido() {

        _controller.gravarPedidoNoBanco();
        Funcoes.showToastLongo(_controller.getContext(),"Pedido realizado com sucesso!");
        PedidoItemActivity.this.finish();
    }

    private void mostraMsgErroAoEnviar() {
        Funcoes.showToastLongo(this, "Erro ao tentar enviar o pedido!\nTente novamente!");
    }


    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverPedido, new IntentFilter(Funcoes.ENVIA_PEDIDO));
    }

    @Override
    protected void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverPedido);
    }

    public AlertDialog criaDialogVenda() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.pedito_item_dlg_venda, null);
        builder.setTitle("Qual a quantidade!");
        builder.setView(layout);
        initComponentes(layout);
        return builder.create();
    }

    private void initComponentes(View view) {
        _btnAumentaQtde = view.findViewById(R.id.btMais);
        _btnDiminuiQtde = view.findViewById(R.id.btMenos);
        _edtItemQtde    = view.findViewById(R.id.edtQtde);
        _btnAceitar     = view.findViewById(R.id.btnAceitar);

        _btnDiminuiQtde.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int valor = Integer.valueOf(_edtItemQtde.getText().toString());
                if(valor > 0 ) {
                    _edtItemQtde.setText(String.valueOf(valor - 1));
                }
            }

        });
        _btnAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _controller.insereItemVendido(itemPosicionado,Integer.parseInt(_edtItemQtde.getText().toString()));
                _dialogVenda.dismiss();
            }
        });

        _btnAumentaQtde.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int valor = Integer.valueOf(_edtItemQtde.getText().toString());

                _edtItemQtde.setText(String.valueOf(valor + 1));

            }

        });
    }
}
