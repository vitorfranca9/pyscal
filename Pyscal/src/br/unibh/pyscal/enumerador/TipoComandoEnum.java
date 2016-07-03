package br.unibh.pyscal.enumerador;

public enum TipoComandoEnum {
	IF,
	WHILE,
	WRITE,
	WRITELN,
	ATRIBUI,
	FUNCAO;
	
	public static TipoComandoEnum getTipoComando(PalavraReservadaEnum palavraReservada) {
		switch (palavraReservada) {
			case IF:
				return TipoComandoEnum.IF;

			default: return null;
		}
	}
	
}
