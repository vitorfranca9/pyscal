package br.unibh.pyscal.util;

public interface PyscalConstantUtil {
	String ARQUIVO_FONTE = "/arquivos_fonte/";

	interface ArquivosTesteLexico {
		String LEXICO = "lexico/";
		String LEXICO_ENTRADA1_PYS = ARQUIVO_FONTE + LEXICO + "lexico_entrada1.pys";
		String LEXICO_ENTRADA2_PYS = ARQUIVO_FONTE + LEXICO + "lexico_entrada2.pys";
		String LEXICO_ENTRADA3_PYS = ARQUIVO_FONTE + LEXICO + "lexico_entrada3.pys";
		String LEXICO_ENTRADA4_PYS = ARQUIVO_FONTE + LEXICO + "lexico_entrada4.pys";
		String LEXICO_ENTRADA5_PYS = ARQUIVO_FONTE + LEXICO + "lexico_entrada5.pys";
		String LEXICO_ENTRADA6_PYS = ARQUIVO_FONTE + LEXICO + "lexico_entrada6.pys";
		String LEXICO_ENTRADA7_PYS = ARQUIVO_FONTE + LEXICO + "lexico_entrada7.pys";
		String LEXICO_ENTRADA8_PYS = ARQUIVO_FONTE + LEXICO +" lexico_entrada8.pys";
		String LEXICO_ENTRADA9_PYS = ARQUIVO_FONTE + LEXICO + "lexico_entrada9.pys";
		String LEXICO_ENTRADA10_PYS = ARQUIVO_FONTE + LEXICO + "lexico_entrada10.pys";
		String LEXICO_ENTRADA11_PYS = ARQUIVO_FONTE + LEXICO + "lexico_entrada11.pys";
	}
	
	interface ArquivosTesteSintatico {
		String SINTATICO = "sintatico/";
		String SINTATICO_ENTRADA1_PYS = ARQUIVO_FONTE + SINTATICO + "sintatico_entrada1.pys";
		String SINTATICO_ENTRADA2_PYS = ARQUIVO_FONTE + SINTATICO + "sintatico_entrada2.pys";
		String SINTATICO_ENTRADA3_PYS = ARQUIVO_FONTE + SINTATICO + "sintatico_entrada3.pys";
	}
	
}
