package br.unibh.pyscal;

import java.io.FileNotFoundException;

public class Main {
	
	private static final String DIR = "/home/vitor/Documents/ambienteJava/workspaces/workspace/Pyscal/";
	
	
	public static void main(String[] args) throws FileNotFoundException {
		Arquivo arquivo = FileUtil.montarArquivo(DIR + "arquivo_entrada1.pys");
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		
//		FileUtil.imprimirLinhas(arquivo);
		FileUtil.imprimirPalavras(arquivo);
		System.out.println(arquivo.getConteudoOriginal());
	}

}
