package br.unibh.pyscal.test;

import java.io.FileNotFoundException;

import br.unibh.pyscal.analisador.AnalisadorLexico;
import br.unibh.pyscal.analisador.AnalisadorSemantico;
import br.unibh.pyscal.analisador.AnalisadorSintatico;
import br.unibh.pyscal.exception.AnaliseLexicaException;
import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.exception.AnaliseSintaticaException;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.util.JasminUtil;
import br.unibh.pyscal.util.PyscalConstantUtil;
import br.unibh.pyscal.vo.ArquivoVO;

public class MainTest {
	
	public static void main(String[] args) throws FileNotFoundException {
		String path = PyscalConstantUtil.ArquivosTesteSemantico.COMANDOS;
		ArquivoVO arquivo = FileUtil.montarArquivo(path);
		try {
			AnalisadorLexico analisadorLexico = new AnalisadorLexico();
			analisadorLexico.analisar(arquivo);
			AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
			analisadorSintatico.analisar(arquivo);
			AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico();
			analisadorSemantico.analisar(arquivo);
			
			JasminUtil.compileJCodeToClass(path);
			/*FileUtil.imprimirTokens(arquivo);*/
		} catch (Exception e) {
			if (e instanceof AnaliseLexicaException) {
				System.out.println("Erro léxico: "+e.getMessage());
			} else if (e instanceof AnaliseSintaticaException) {
				System.out.println("Erro sintático: "+e.getMessage());
			} else if (e instanceof AnaliseSemanticaException){
				System.out.println("Erro semântico: "+e.getMessage());
			} else {
				e.printStackTrace();
				System.out.println("Erro: "+e.getMessage());
			}
		}
	}
	
//	public static void main(String[] args) throws FileNotFoundException {
//		String path = PyscalConstantUtil.ArquivosTesteSemantico.COMANDOS;
//		ArquivoVO arquivo = FileUtil.montarArquivo(path);
//		try {
//			AnalisadorLexico analisadorLexico = new AnalisadorLexico();
//			analisadorLexico.analisar(arquivo);
//			AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
//			analisadorSintatico.analisar(arquivo);
//			AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico();
//			analisadorSemantico.analisar(arquivo);
//			
//			JasminUtil.compileJCodeToClass(path);
//			/*FileUtil.imprimirTokens(arquivo);*/
//		} catch (Exception e) {
//			if (e instanceof AnaliseLexicaException) {
//				System.out.println("Erro lexico: "+e.getMessage());
//			} else if (e instanceof AnaliseSintaticaException) {
//				System.out.println("Erro sintatico: "+e.getMessage());
//			} else if (e instanceof AnaliseSemanticaException){
//				System.out.println("Erro semantico: "+e.getMessage());
//			} else {
//				e.printStackTrace();
//				System.out.println("Erro: "+e.getMessage());
//			}
//		}
//	}
	
//	System.out.println("Total"+2*(2+2));
}
