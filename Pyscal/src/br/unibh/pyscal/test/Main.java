package br.unibh.pyscal.test;

import java.io.FileNotFoundException;

import br.unibh.exception.AnaliseLexicaException;
import br.unibh.pyscal.analisador.AnalisadorLexico;
import br.unibh.util.FileUtil;
import br.unibh.vo.ArquivoVO;

//
/**/
public class Main {
	private static final String DIR = "/home/vitor/Documents/ambienteJava/gitRepository/Pyscal/";
	private static final String ARQUIVO_ENTRADA_PYS = "arquivo_entrada5.pys";
	
	public static void main(String[] args) throws FileNotFoundException {
		ArquivoVO arquivo = FileUtil.montarArquivo(DIR + ARQUIVO_ENTRADA_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		try {
			analisadorLexico.analisar(arquivo);
		} catch (AnaliseLexicaException e) {
			System.out.println(e.getMessage());
		}
		
//		FileUtil.imprimirLinhas(arquivo);
//		FileUtil.imprimirPalavras(arquivo);
		FileUtil.imprimirTokens(arquivo);
//		System.out.println(arquivo.getConteudoOriginal());
	}

}
