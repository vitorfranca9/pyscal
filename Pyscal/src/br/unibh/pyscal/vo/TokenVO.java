package br.unibh.pyscal.vo;

import br.unibh.pyscal.enumerador.PalavraReservada;
import lombok.Getter;
import lombok.Setter;

public class TokenVO {
	@Getter @Setter private String valor = "";
	@Getter @Setter private PalavraReservada palavraReservada;
	@Getter @Setter private Integer ordem;
	
	@Override
	public String toString() {
		return "TokenVO [valor='" + valor + "', palavraReservada=" + palavraReservada + "]";
	}
}
