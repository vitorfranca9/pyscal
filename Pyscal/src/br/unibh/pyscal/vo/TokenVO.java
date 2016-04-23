package br.unibh.pyscal.vo;

import br.unibh.pyscal.enumerador.PalavraReservada;

public class TokenVO {
	private String valor = "";
	private PalavraReservada palavraReservada;
	private Integer ordem;
	
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public PalavraReservada getPalavraReservada() {
		return palavraReservada;
	}
	public void setPalavraReservada(PalavraReservada palavraReservada) {
		this.palavraReservada = palavraReservada;
	}
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	@Override
	public String toString() {
		return "TokenVO [valor=" + valor + ", palavraReservada=" + palavraReservada + "]";
	}
}
