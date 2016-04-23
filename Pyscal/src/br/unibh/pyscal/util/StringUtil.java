package br.unibh.pyscal.util;

import java.text.Normalizer;

public class StringUtil {

	public static String tratarCaracteres(String valor) {
		valor = valor
			.replaceAll("\r", "")
			.replaceAll("\t", "")
			.replaceAll("\n", "");
		return valor;
	}

	public static char normalizar(char valor) {
		return StringUtil.normalizar(valor+"").charAt(0);
	}

	public static String normalizar(String valor) {
		String temp = Normalizer.normalize(valor, java.text.Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]","");
	}

	//TODO Atribuir responsabilidade a uma classe utilitária
	public static boolean isLetra(char valor) {
		return Character.isLetter(valor);
	}

	public static boolean isDigito(char valor) {
		return Character.isDigit(valor);
	}

	public static boolean isLetraDigito(char valor) {
		return Character.isLetterOrDigit(valor);
	}
	
}