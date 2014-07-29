package br.com.brasinha.ConnectServer;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class ConnectServer {
	
	private String ip;
	private int port;
	private Socket sockServer;
	
	public ByteArrayOutputStream makeData(List<String> data, int tipo, int acao) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			bos.write(tipo);
			bos.write(acao);
			
			for(int i = 0; i < data.size(); i ++) {
				bos.write(data.get(i).length());
				bos.write(data.get(i).getBytes());
			}
			
			Log.i("ConnecServer-makedata", "data.size -> "+data.size());
			
			return bos;
			
		} catch (IOException e) {
			// TODO Bloco catch gerado automaticamente
			e.printStackTrace();
			return null;
		}
	}
	
	public ByteArrayOutputStream makeData(byte tipo, byte acao) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		bos.write(tipo);
		bos.write(acao);
					
		return bos;
	}
	
	public void sendMessage(ByteArrayOutputStream data) {

		try {
			DataOutputStream out = new DataOutputStream (sockServer.getOutputStream());
			out.writeInt(data.toByteArray().length);
			out.write(data.toByteArray());
			out.flush();
			
		} catch (IOException e) {
			// TODO Bloco catch gerado automaticamente
			e.printStackTrace();
		}
	}
	
	/** recebe os dados do servidor e armazena no buffer */
	public DatagramServer getMessage() {
		try {
			DataInputStream in = new DataInputStream(sockServer.getInputStream());
			
			int lenghtString, k = 0;
			DatagramServer datagram = new DatagramServer();
		
			/* recebe o número de bytes que serão lidos, desconsiderando o byte de tipo e acao */
			int lenghtData = in.readInt();
			Log.d("DatagramServer - getMessage","lenght data -> "+lenghtData);
			
			datagram.setTipo(in.readByte());				//lê o tipo do dado recebedido
			Log.d("DatagramServer - getMessage","tipo -> "+datagram.getTipo());
			lenghtData--;
			
			datagram.setAcao(in.readByte());				//lê a ação a ser tomada com o dado recebido
			Log.d("DatagramServer - getMessage", "acao -> "+datagram.getAcao());
			lenghtData--;
			
			while(lenghtData > 0) {
			
				lenghtString = in.readByte();
				lenghtData--;
				Log.d("DatagramServer - getMessage", "tam string -> "+lenghtString);
				
				byte[] buf = new byte[lenghtString];		//lê o número de caracteres que a string irá conter
			
				in.read(buf);
				lenghtData = lenghtData - lenghtString;			
				
				datagram.addData(new String(buf));			//adiciona a string lida na lista de dados
				
				Log.d("DatagramServer - getMessage", "String convertida -> "+datagram.getData(k));
				k++;
			}
			return datagram;	
			
		} catch (IOException e) {
			// TODO Bloco catch gerado automaticamente
			e.printStackTrace();
			return null;
		}
	}
	
	/* envia os dados ao servidor central */
	/*public void sendMessage(String message) throws IOException {
		putData = new DataOutputStream(sockServer.getOutputStream());
		putData.writeUTF(message);
		putData.flush();
		//putData.close();
	}
	
	public String getMessage() throws IOException {
		getData = new DataInputStream(sockServer.getInputStream());
		message = getData.readUTF();
		//getData.close();
		
		return message;
	}*/
	
	public void connect(String ip, int port) throws UnknownHostException, IOException {
		sockServer = new Socket(ip, port);
	}
	
	public Socket getSocket() {
		return sockServer;
	}
	
	public void close() throws UnknownHostException, IOException {
		sockServer.close();
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public int getPort() {
		return this.port;
	}
}
