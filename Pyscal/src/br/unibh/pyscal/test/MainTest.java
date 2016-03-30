package br.unibh.pyscal.test;

import java.io.FileNotFoundException;

import br.unibh.pyscal.analisador.AnalisadorLexico;
import br.unibh.pyscal.exception.AnaliseLexicaException;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.vo.ArquivoVO;

//
/**/
public class MainTest {
	
	public static void main(String[] args) throws FileNotFoundException {
		ArquivoVO arquivo = FileUtil.montarArquivo(AnaliseLexicaTest.ARQUIVO_ENTRADA9_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		try {
			analisadorLexico.analisar(arquivo);
			FileUtil.imprimirTokens(arquivo);
		} catch (AnaliseLexicaException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
