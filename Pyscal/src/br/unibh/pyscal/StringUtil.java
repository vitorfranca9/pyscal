package br.unibh.pyscal;

public class StringUtil {

	public static String tratarCaracteres(String valor) {
		valor = valor
			.replaceAll("\r", "")
			.replaceAll("\t", "")
			.replaceAll("\n", "");
		
		return valor;
	}
	
}
