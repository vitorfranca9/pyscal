package br.unibh.pyscal;

public enum TipoToken {
	CLASS("class"), 
	ID("id"), //regex
//	FUNCAO("função"), //definir função([]),etc dps
//	FUNCAO_MAIN("função main:def"),
	ARG("arg"),
	RETORNO("return"),
	
	
	;
	
	private TipoToken(String valor) {
		this.valor = valor;
	}
	
	private final String valor;

	public String getValor() {
		return valor;
	}
	
}
