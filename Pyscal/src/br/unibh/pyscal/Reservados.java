package br.unibh.pyscal;

public enum Reservados {
	
	ABRE_PARENTESES("("),
	FECHA_PARENTESES(")"),
	ASPAS("\""),
	DOIS_PONTOS(":"),
	PONTO_VIRTULA(";"),
	CLASS("class"),
	DEF("def"),
	DEF_STATIC("defstatic"),
	END_PONTO_VIRGULA("end;"),
	END_PONTO("end."),
	AND("and"),
	OR("or"),
	MENOR("<"),
	MAIOR(">"),
	MENOR_IGUAL("<="),
	MAIOR_IGUAL(">="),
	IGUAL_IGUAL("=="),
	IGUAL("="),
	DIFERENTE("!="),
	DIVIDIR("/"),
	MULTIPLICAR("*"),
	SOMAR("+"),
	SUBTRAIR("-"),
	NOT("!"),
	BOOLEAN("bool"),
	INTEGER("integer"),
	STRING("String"),
	DOUBLE("double"),
	ABRE_COLCHETE("["),
	FECHA_COLCHETE("]"),
	VOID("void"),
	WRITE("write"),
	WRITELN("writeln"),
	MAIN("main"),
	IF("if"),
	WHILE("while"),
	VECTOR("vector"), //?
	TRUE("true"),
	FALSE("false"),
	CONST_INTEGER("ConstInteger"),
	CONST_DOUBLE("ConstDouble"),
	CONST_STRING("ConstString"),
	;
	
	private final String palavra;
	//TODO
	private TipoOque tipo;

	private Reservados(String palavra) {
		this.palavra = palavra;
	}

	public String getPalavra() {
		return palavra;
	}

}
