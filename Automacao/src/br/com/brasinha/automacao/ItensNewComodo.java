package br.com.brasinha.automacao;

public class ItensNewComodo {

	private String nomeComodo;
	private String nomeItem;
	private String tipoItem;
	private String numItem;
	private int pos;
	
	/*public ItensNewComodo () {

	}*/
	
	public int getPosItem() {
		return pos;
	}
	
	public void setPosItem(int pos) {
		this.pos = pos;
		
		if(this.pos == 0)
			this.setTipoItem("Lampada");
		
		else if(this.pos == 1)
			this.setTipoItem("Ventilador");
	}
	
	public String getNomeComodo() {
		return this.nomeComodo;
	}
	
	public void setNomeComodo(String comodo) {
		this.nomeComodo = comodo;
	}
	
	public String getNomeItem() {
		return this.nomeItem;
	}
	
	public void setNomeItem(String nomeItem) {
		this.nomeItem = nomeItem;
	}
	
	public String getTipoItem() {
		return this.tipoItem;
	}
	
	public void setTipoItem(String tipoItem) {
		this.tipoItem = tipoItem;
	}
	
	public String getNumItem() {
		return this.numItem;
	}
	
	public void setNumItem(String numItem) {
		this.numItem = numItem;
	}
}
