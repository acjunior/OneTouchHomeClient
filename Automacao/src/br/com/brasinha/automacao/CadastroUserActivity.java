package br.com.brasinha.automacao;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import defines.Message;
import br.com.brasinha.ConnectServer.ConnectServer;
import br.com.brasinha.ConnectServer.DatagramServer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroUserActivity extends Activity implements OnClickListener {

	private EditText nameEditText;
	private EditText emailEditText;
	private EditText passEditText;
	private EditText confirmPassEditText;
	private Button cadastroButton;
	private String pass;
	private String confirmPass;
	private TelephonyManager telephony;
	private ConnectServer server;
	private String message;
	private AlertDialog alerta;
	private DatagramServer datagram;
	private List<String> listData;
	private Intent loginData;
	private String ipServer;
	private int portServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cadastrouser_activity);
		
		initialize();
	}
	
	private void initialize() {
		nameEditText = (EditText)findViewById(R.id.nameEditText);
		emailEditText = (EditText)findViewById(R.id.emailEditText);
		passEditText = (EditText)findViewById(R.id.passEditText);
		confirmPassEditText = (EditText)findViewById(R.id.confirmPassEditText);
		
		cadastroButton = (Button)findViewById(R.id.newUserButton);
		cadastroButton.setOnClickListener(this);

		listData = new ArrayList<String>();
		telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		loginData = getIntent();
		ipServer = loginData.getExtras().getString("ipServer");
		portServer = loginData.getExtras().getInt("portServer");
		
		server = new ConnectServer();
	}
	
	/** constrói janela de alerta com erro */
	private void buildAlert(String titulo, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(titulo);
		builder.setMessage(msg);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Stub de método gerado automaticamente
				alerta.dismiss();
				CadastroUserActivity.this.finish();
			}
		});
		
		alerta = builder.create();
		alerta.show();
	}

	private boolean validaCampos() {
		boolean flag = true;
		
		if(nameEditText.getText().toString().equals("")) {
			nameEditText.setError("Campo obrigatório");
			flag = false;
		}
		
		if(emailEditText.getText().toString().equals("")) {
			emailEditText.setError("Campo obrigatório");
			flag = false;
		}
		
		if(passEditText.getText().toString().equals("")) {
			passEditText.setError("Campo obrigatório");
			flag = false;
		}
		//if(datagram.getData().get(0).equals(Message.OK)) {
		//	this.buildAlert("Sucesso!", "Usuário cadastrado!");
			//Toast.makeText(this, "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();
		//}
	
		//else if (datagram.getData().get(0).equals(Message.NOK)) {
		//	this.buildAlert("ERRO!", "Usuário ja cadastrado!");
		//	listData.clear();
			//Toast.makeText(this, "Usuário já cadastrado", Toast.LENGTH_LONG).show();
		//}
		if(confirmPassEditText.getText().toString().equals("")) {
			confirmPassEditText.setError("Campo obrigatório");
			flag = false;
		}
		
		return flag;
	}
	
	@Override
	public void onClick(View button) {
		// TODO Stub de método gerado automaticamente
		if(button.getId() == R.id.newUserButton) {
			
			if(this.validaCampos()) {
			
				pass = passEditText.getText().toString();
				confirmPass = confirmPassEditText.getText().toString();
			
				if(pass.equals(confirmPass)) {
					listData.add(nameEditText.getText().toString());
					listData.add(passEditText.getText().toString());
					listData.add(telephony.getDeviceId());
					listData.add(emailEditText.getText().toString());

					try {
						server.connect(ipServer, portServer);
						server.sendMessage(server.makeData(listData, Message.SQL, Message.NEW_USER));
						
						datagram = server.getMessage();
					
						if(datagram.getAcao() == Message.OK) {
							//this.buildAlert("Sucesso!", "Usuário cadastrado!");
							Toast.makeText(this, "Sucesso! Usuário cadastrado!", Toast.LENGTH_SHORT).show();
							server.close();
							this.finish();
						}
						
						else {
							this.buildAlert("ATENÇÃO", "Usuário já cadastrado!");
							listData.clear();
						}
					
					} catch (UnknownHostException e) {
						// TODO Bloco catch gerado automaticamente
						this.buildAlert("ERRO", "Conexão com o servidor falhou!");
						listData.clear();
						e.printStackTrace();
					
					} catch (IOException e) {
						// TODO Bloco catch gerado automaticamente
						this.buildAlert("ERRO", "Conexão com o servidor falhou!");
						listData.clear();
						e.printStackTrace();
					}
				}
			
				else {
					this.buildAlert("ERRO", "Senhas incorretas! Tente novamente.");
					listData.clear();
					//Toast.makeText(CadastroUserActivity.this, "senhas erradas", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
}
