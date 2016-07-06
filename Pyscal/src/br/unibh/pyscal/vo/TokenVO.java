package br.unibh.pyscal.vo;

import br.unibh.pyscal.enumerador.PalavraReservadaEnum;

public class TokenVO {
	private String valor = "";
	private PalavraReservadaEnum palavraReservada;
	private Integer ordem;
	
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public PalavraReservadaEnum getPalavraReservada() {
		return palavraReservada;
	}

	public void setPalavraReservada(PalavraReservadaEnum palavraReservada) {
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
		return "TokenVO [valor='" + valor + "', palavraReservada=" + palavraReservada + "]";
	}
}
