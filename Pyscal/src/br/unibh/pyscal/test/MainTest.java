package br.unibh.pyscal.test;

import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteLexico.LEXICO_ENTRADA3;

import java.io.FileNotFoundException;

import br.unibh.pyscal.analisador.AnalisadorLexico;
import br.unibh.pyscal.analisador.AnalisadorSintatico;
import br.unibh.pyscal.exception.AnaliseLexicaException;
import br.unibh.pyscal.exception.AnaliseSintaticaException;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.util.PyscalConstantUtil;
import br.unibh.pyscal.vo.ArquivoVO;

public class MainTest {
	
	public static void main(String[] args) throws FileNotFoundException {
		@SuppressWarnings("unused") String lexicoEntrada3Pys = LEXICO_ENTRADA3;
		String sintaticoEntrada1Pys = PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_APRESENTACAOTP2;
//		String sintaticoEntrada1Pys = SINTATICO_EXPRESSAO;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		try {
			analisadorLexico.analisar(arquivo);
			AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
			analisadorSintatico.analisar(arquivo);
//			FileUtil.imprimirTokens(arquivo);
		} catch (Exception e) {
			if (e instanceof AnaliseLexicaException) {
				System.out.println("Erro léxico: "+e.getMessage());
			} else if (e instanceof AnaliseSintaticaException) {
				System.out.println("Erro sintático: "+e.getMessage());
			} else {
				e.printStackTrace();
				System.out.println("Erro: "+e.getMessage());
			}
		}
	}
//	System.out.println("Total"+2*(2+2));
}
