package br.com.brasinha.automacao;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import defines.Message;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import br.com.brasinha.ConnectServer.*;

public class Comodo extends Activity implements OnClickListener {

	private Intent makeComodo, loginData;
	private Button novoComodoButton;
	private ExpandableListView lista;
	private AdapterComodo adapter;
	private ConnectServer server;
	private List<String> listaComodo;
	private List<String> itens, listData;
	private Map<String, List<String>> listaItem;
	private DatagramServer datagram;
	private String ipServer;
	private int portServer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_comodo);
		
		initialize();
		this.carregaComodos();
	}
	
	private void initialize() {
		listaComodo = new ArrayList<String>();
		listData = new ArrayList<String>();
		listaItem = new HashMap<String, List<String>>();
		server = new ConnectServer();
		
		loginData = getIntent();		
		ipServer = loginData.getExtras().getString("ipServer");
		portServer = loginData.getExtras().getInt("portServer");
		
		novoComodoButton = (Button)findViewById(R.id.novoComodoButton);
		lista = (ExpandableListView)findViewById(R.id.expandableListView);

		novoComodoButton.setOnClickListener(this);
		
		adapter = new AdapterComodo(this, listaComodo, listaItem);
		lista.setAdapter(adapter);
	}
	
	public void carregaComodos() {
		
		String comodoItem;
		
		if(!listaComodo.isEmpty())
			listaComodo.clear();
		
		try {
			server.connect(ipServer, portServer);
			
			server.sendMessage(server.makeData(Message.SQL, Message.GET_COMODO));

			datagram = server.getMessage();					//recebe os comodos e itens cadastrados
			
			while(datagram.getAcao() != Message.FIM) {
			
				itens = new ArrayList<String>();
				itens = datagram.getLista();
				
				comodoItem = itens.get(0);					//atribui o nome do comodo
				listaComodo.add(comodoItem);
				itens.remove(0);
				
				Log.d("Comodo", "comodo -> "+comodoItem);
				Log.d("Comodo", "item -> "+itens.get(0));
				
				listaItem.put(comodoItem, itens);
				
				datagram = server.getMessage();				//recebe os comodos e itens cadastrados
			}
			
			/*for (int i = 0; i < datagram.getData().size(); i++) {
				comodoItemList = datagram.getData().get(i).split("-");
				
				itens = new ArrayList<String>();
				
				for(int j = 1; j < comodoItemList.length; j++) {
					itens.add(comodoItemList[j]);
				}				
			}*/
			
			adapter.notifyDataSetChanged();
			
			server.close();
			
		} catch (UnknownHostException e) {
			// TODO Bloco catch gerado automaticamente
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Bloco catch gerado automaticamente
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View bt) {
		// TODO Stub de método gerado automaticamente
		if(bt.getId() == R.id.novoComodoButton) {
			makeComodo = new Intent(this, MakeComodo.class);
			
			makeComodo.putExtra("ipServer", ipServer);
			makeComodo.putExtra("portServer", portServer);
			
			startActivityForResult(makeComodo, 1);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == 1 && data != null) {
			
			if(data.getExtras().getString("RESULT").equals("OK")){
				//Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
				this.carregaComodos();
			}
		}
	}
}