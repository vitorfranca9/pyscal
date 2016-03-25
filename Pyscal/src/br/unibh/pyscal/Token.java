package br.unibh.pyscal;

public class Token /*implements Comportamento*/ {
	private String valor;
	private TipoToken tipoToken;
	private PalavraReservada palavraReservada;
	
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public TipoToken getTipoToken() {
		return tipoToken;
	}
	public void setTipoToken(TipoToken tipoToken) {
		this.tipoToken = tipoToken;
	}
	public PalavraReservada getPalavraReservada() {
		return palavraReservada;
	}
	public void setPalavraReservada(PalavraReservada palavraReservada) {
		this.palavraReservada = palavraReservada;
	}
}
