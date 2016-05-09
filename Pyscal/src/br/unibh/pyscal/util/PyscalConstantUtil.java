package br.unibh.pyscal.util;

public interface PyscalConstantUtil {
	String ARQUIVO_FONTE = "/arquivos_fonte/";

	interface ArquivosTesteLexico {
		String LEXICO = "lexico/";
		String LEXICO_ENTRADA1 = ARQUIVO_FONTE + LEXICO + "lexico_entrada1.pys";
		String LEXICO_ENTRADA2 = ARQUIVO_FONTE + LEXICO + "lexico_entrada2.pys";
		String LEXICO_ENTRADA3 = ARQUIVO_FONTE + LEXICO + "lexico_entrada3.pys";
		String LEXICO_ENTRADA4 = ARQUIVO_FONTE + LEXICO + "lexico_entrada4.pys";
		String LEXICO_ENTRADA5 = ARQUIVO_FONTE + LEXICO + "lexico_entrada5.pys";
		String LEXICO_ENTRADA6 = ARQUIVO_FONTE + LEXICO + "lexico_entrada6.pys";
		String LEXICO_ENTRADA7 = ARQUIVO_FONTE + LEXICO + "lexico_entrada7.pys";
		String LEXICO_ENTRADA8 = ARQUIVO_FONTE + LEXICO +" lexico_entrada8.pys";
		String LEXICO_ENTRADA9 = ARQUIVO_FONTE + LEXICO + "lexico_entrada9.pys";
		String LEXICO_ENTRADA10 = ARQUIVO_FONTE + LEXICO + "lexico_entrada10.pys";
		String LEXICO_ENTRADA11 = ARQUIVO_FONTE + LEXICO + "lexico_entrada11.pys";
	}
	
	interface ArquivosTesteSintatico {
		String SINTATICO = "sintatico/";
		String SINTATICO_ENTRADA1 = ARQUIVO_FONTE + SINTATICO + "sintatico_entrada1.pys";
		String SINTATICO_ENTRADA2 = ARQUIVO_FONTE + SINTATICO + "sintatico_entrada2.pys";
		String SINTATICO_ENTRADA3 = ARQUIVO_FONTE + SINTATICO + "sintatico_entrada3.pys";
		String SINTATICO_ATRIBUI = ARQUIVO_FONTE + SINTATICO + "sintatico_atribui.pys";
		String SINTATICO_FUNCAO = ARQUIVO_FONTE + SINTATICO + "sintatico_funcao.pys";
		String SINTATICO_FUNCAO2 = ARQUIVO_FONTE + SINTATICO + "sintatico_funcao2.pys";
		String SINTATICO_EXPRESSAO = ARQUIVO_FONTE + SINTATICO + "sintatico_expressao.pys";
		String SINTATICO_COMANDOS = ARQUIVO_FONTE + SINTATICO + "sintatico_comandos.pys";
		String SINTATICO_IF = ARQUIVO_FONTE + SINTATICO + "sintatico_if.pys";
		String SINTATICO_RETURN = ARQUIVO_FONTE + SINTATICO + "sintatico_return.pys";
		String SINTATICO_WHILE = ARQUIVO_FONTE + SINTATICO + "sintatico_while.pys";
		String SINTATICO_WRITE = ARQUIVO_FONTE + SINTATICO + "sintatico_write.pys";
		String SINTATICO_MAIN = ARQUIVO_FONTE + SINTATICO + "sintatico_main.pys";
	}
	
}