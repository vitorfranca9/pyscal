package br.unibh.pyscal.enumerador;

//TODO
public enum TipoExpressaoEnum {
	CLASSE,
//	LISTA_FUNCAO,
	FUNCAO,
//	LISTA_ARG,
	ARG,
	RETORNO,
	MAIN,
	TIPO_MACRO,
	TIPO_PRIMITIVO,
//	LISTA_CMD,
	CMD,
	CMD_IF,
	CMD_WHILE,
	CMD_WRITE,
	CMD_WRITELN,
	CMD_ATRIBUI,
	CMD_FUNCAO, 
	EXPRESSAO, //expr Op expr
	OP, //or,and
	OP_UNARIO, //!,-
	DECLARA_ID	
	;
}

