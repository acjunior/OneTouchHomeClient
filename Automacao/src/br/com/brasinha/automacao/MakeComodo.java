package br.com.brasinha.automacao;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import defines.Message;
import br.com.brasinha.ConnectServer.ConnectServer;
import br.com.brasinha.ConnectServer.DatagramServer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MakeComodo extends Activity implements OnClickListener {

	private EditText nomeEditText;
	private Intent resultComodo, loginData;
	private String result;
	private ImageView addImage, delImage;
	private Button salvarComodo;
	private Button cancelarComodo;
	private List<ItensNewComodo> itens;
	private ListView listItem;
	private List<String> listData;
	private AdapterNewComodoListView adapter;
	private ConnectServer server;
	private DatagramServer datagram;
	private String ipServer;
	private int portServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.make_comodo);
		
		this.initialize();
		listItem.setAdapter(adapter);
	}
	
	private void initialize() {
		server = new ConnectServer();
		
		nomeEditText = (EditText)findViewById(R.id.nomeEditText);
		addImage = (ImageView)findViewById(R.id.addImageView);
		delImage = (ImageView)findViewById(R.id.delImageView);
		listItem = (ListView)findViewById(R.id.comodoListView);
		salvarComodo = (Button)findViewById(R.id.salvarMakeComodoButton);
		cancelarComodo = (Button)findViewById(R.id.cancelMakeComodoButton);
		
		salvarComodo.setOnClickListener(this);
		cancelarComodo.setOnClickListener(this);
		addImage.setOnClickListener(this);
		delImage.setOnClickListener(this);
		
		loginData = getIntent();
		ipServer = loginData.getExtras().getString("ipServer");
		portServer = loginData.getExtras().getInt("portServer");
		
		listData = new ArrayList<String>();
		itens = new ArrayList<ItensNewComodo>();
		adapter = new AdapterNewComodoListView(this, itens);
		
		resultComodo = new Intent(MakeComodo.this, Comodo.class);
	}

	@Override
	public void onClick(View bt) {
		// TODO Stub de método gerado automaticamente
		
		switch(bt.getId()) {
		
		case R.id.addImageView:
			ItensNewComodo item = new ItensNewComodo();
			
			itens.add(item);
			adapter.notifyDataSetChanged();
			break;
			
		case R.id.delImageView:
			itens.remove(itens.size()-1);
			adapter.notifyDataSetChanged();
			break;
			
		case R.id.salvarMakeComodoButton:
			
			try {
				/** tem que puxar os dados do SQLite */
				server.connect(ipServer, portServer);
				
				/* pega a lista de itens inseridos pelo usuário para armazenar no DB */
				itens = adapter.getList();
				
				/* adiciona a lista de dados o nome do cômodo a ser cadastrado */
				listData.add(nomeEditText.getText().toString());
				
				//server.sendMessage(server.makeData(listData, Message.SQL, Message.NEW_COMODO));
				//listData.clear();
				
				/* adiciona a lista de dados todos os itens que serão cadastrados */
				for(int i = 0; i < itens.size(); i++) {
					listData.add(itens.get(i).getNomeItem());
					listData.add(itens.get(i).getNumItem());
					listData.add(itens.get(i).getTipoItem());
				}
				
				/* envia para o servidor os itens do comodo que serão cadastrados */
				server.sendMessage(server.makeData(listData, Message.SQL, Message.NEW_COMODO));
				
				datagram = server.getMessage();
				
				if(datagram.getAcao() == Message.OK) {
					Toast.makeText(this, "Cômodo cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
					server.close();
					result = "OK";
					resultComodo.putExtra("RESULT", result);
					setResult(1, resultComodo);
					this.finish();
				}
				
			} catch (UnknownHostException e) {
				// TODO Bloco catch gerado automaticamente
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Bloco catch gerado automaticamente
				e.printStackTrace();
			}			
			break;
			
		case R.id.cancelMakeComodoButton:
			this.finish();
		}		
	}
}
