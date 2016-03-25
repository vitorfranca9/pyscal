package br.unibh.pyscal;

import java.io.FileNotFoundException;
import java.util.List;

//
/**/
public class Main {
	
	private static final String DIR = "/home/vitor/Documents/ambienteJava/gitRepository/Pyscal/";
	
	
	public static void main(String[] args) throws FileNotFoundException {
		Arquivo arquivo = FileUtil.montarArquivo(DIR + "arquivo_entrada3.pys");
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		List<Token> todosTokens = analisadorLexico.analisar(arquivo);
		
//		FileUtil.imprimirLinhas(arquivo);
//		FileUtil.imprimirPalavras(arquivo);
		FileUtil.imprimirTokens(arquivo);
		System.out.println(arquivo.getConteudoOriginal());
	}

}
