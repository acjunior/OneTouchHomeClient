package br.com.brasinha.automacao;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import defines.Message;
import br.com.brasinha.ConnectServer.ConnectServer;
import br.com.brasinha.ConnectServer.DatagramServer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ActivityLogin extends Activity implements OnClickListener {

	private Button newUserButton;
	private Button loginButton;
	private Button configButton;
	private Button exitButton;
	private Intent newUser;
	private Intent settings;
	private Intent comodo;
	private ConnectServer server;
	private String ipServer = new String();
	private int portServer = 0;
	private ProgressDialog dialog;
	private TelephonyManager telephony;
	private AlertDialog alerta;
	private DatagramServer datagram;
	private List<String> listData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login_activity);
		
		/*erro*/
		initialize();
	}
	
	private void initialize() {
		newUserButton = (Button)findViewById(R.id.newUserButton);		
		loginButton = (Button)findViewById(R.id.loginButton);
		configButton = (Button)findViewById(R.id.login_configButton);
		exitButton = (Button)findViewById(R.id.login_exitButton);
		
		newUserButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		configButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
		
		dialog = new ProgressDialog(this);
		dialog.setMessage("Conectando ao servidor....");
		
		telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		server = new ConnectServer();
		listData = new ArrayList<String>();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == 1 && data != null) {
			Toast.makeText(this, "conexao salva", Toast.LENGTH_LONG).show();
			
			ipServer = data.getExtras().getString("ipServer");
			portServer = Integer.parseInt(data.getExtras().getString("portServer"));
		}
	}
	
	/* constrói a janela de alerta */
	private void buildAlert(String titulo, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(titulo);
		builder.setMessage(msg);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Stub de método gerado automaticamente
				alerta.dismiss();
				settings = new Intent(ActivityLogin.this, Settings.class);
				startActivityForResult(settings, 1);
			}
		});
		
		alerta = builder.create();
		alerta.show();
	}

	@Override
	public void onClick(View bt) {
		// TODO Stub de método gerado automaticamente
		
		if(bt.getId() == R.id.login_configButton) {
			settings = new Intent(ActivityLogin.this, Settings.class);
			startActivityForResult(settings, 1);
		}
		
		/* se o ip e porta não estão configurados */
		else if(ipServer.isEmpty() || portServer == 0) 
			this.buildAlert("ERRO", "Conexão com servidor não configurada!");
		
		else {
		
			switch(bt.getId()) {
		
			case R.id.loginButton:
				try {
					
					/* pega o endereço IMEI do celular */
					listData.add(telephony.getDeviceId());				
				
					dialog.show();
				
					/* conexão com o servidor */
					server.connect(ipServer, portServer);
					
					/* monta os dados e envia o pacote para o servidor */
					server.sendMessage(server.makeData(listData, Message.SQL, Message.LOGIN));
					
					/* recebe confirmação se usuário está cadastrado ou não */
					datagram = server.getMessage();
					
					if(datagram.getAcao() == Message.OK) {
						dialog.dismiss();
						
						comodo = new Intent(ActivityLogin.this, Comodo.class);
						comodo.putExtra("ipServer", ipServer);
						comodo.putExtra("portServer", portServer);
						
						startActivity(comodo);
						server.close();
						this.finish();
					}

					else {
						this.buildAlert("ERRO", "Usuário ainda não cadastrado no sistema!");
						//Toast.makeText(this, "Usuário não cadastrado", Toast.LENGTH_LONG).show();
						dialog.dismiss();
						server.close();
						listData.clear();
					}
				
				} catch (UnknownHostException e) {
					//TODO Bloco catch gerado automaticamente
					dialog.dismiss();
					listData.clear();
					this.buildAlert("ERRO", "Conexão com o servidor falhou!");
				
					e.printStackTrace();
				} catch (IOException e) {
					//TODO Bloco catch gerado automaticamente
					dialog.dismiss();
					listData.clear();
					this.buildAlert("ERRO", "Conexão com o servidor falhou!");
				
					e.printStackTrace();
				}
				
			break;	
		
			case R.id.newUserButton:
				newUser = new Intent(ActivityLogin.this, CadastroUserActivity.class);
				newUser.putExtra("ipServer", ipServer);
				newUser.putExtra("portServer", portServer);
				startActivity(newUser);
				//this.finish();
				break;
			
			case R.id.login_exitButton:
				this.finish();
			}
		}
	}
}
