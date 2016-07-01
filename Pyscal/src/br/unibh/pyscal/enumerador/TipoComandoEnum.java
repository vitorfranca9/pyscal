package br.unibh.pyscal.enumerador;

import lombok.Getter;

public enum TipoComandoEnum {
	IF(""),
	WHILE(""),
	WRITE(""),
	WRITELN(""),
	ATRIBUI(""),
	FUNCAO("");
	
	private TipoComandoEnum(String jCode) {
		this.jCode = jCode;
	}

	@Getter private final String jCode;
	
}
