package br.com.brasinha.automacao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends Activity implements OnClickListener {
	
	EditText ipServerEditText;
	EditText portServerEditText;
	Button saveButton;
	Button cancelButton;
	String ipServer;
	String portServer;
	Intent serverData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.settings);
		
		this.initialize();
	}
	
	private void setIpServer() {
		ipServer = ipServerEditText.getText().toString();
	}
	
	private void setPortServer() {
		portServer = portServerEditText.getText().toString();
	}
	
	private void initialize() {
		ipServerEditText = (EditText)findViewById(R.id.ipServer);
		portServerEditText = (EditText)findViewById(R.id.serverPort);
		saveButton = (Button)findViewById(R.id.saveButton);
		saveButton.setOnClickListener(this);
		cancelButton = (Button)findViewById(R.id.cancelLoginButton);
		cancelButton.setOnClickListener(this);
	}
	
	public boolean validaCampo() {
		
		if(ipServerEditText.getText().toString().equals("") && portServerEditText.getText().toString().equals("")) {
			ipServerEditText.setError("Campo obrigatório");
			portServerEditText.setError("Campo obrigatório");
			return false;
		}
		
		else if(ipServerEditText.getText().toString().equals("")) {
			ipServerEditText.setError("Campo obrigatório");
			return false;
		}
		
		else if(portServerEditText.getText().toString().equals("")) {
			portServerEditText.setError("Campo obrigatório");
			return false;
		}
		else
			return true;
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
			
		case R.id.saveButton:
			
			if(this.validaCampo()) {
			
				this.setIpServer();
				this.setPortServer();
			
				serverData = new Intent(Settings.this, ActivityLogin.class);
				serverData.putExtra("ipServer", ipServer);
				serverData.putExtra("portServer", portServer);
				setResult(1, serverData);
			
				this.finish();
			}
			break;
		
		case R.id.cancelLoginButton:
			this.finish();
			break;
			
		}
	}
}
