package br.unibh.pyscal.test;

import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteLexico.*;

import org.junit.Test;

import br.unibh.pyscal.analisador.AnalisadorLexico;
import br.unibh.pyscal.exception.AnaliseLexicaException;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.vo.ArquivoVO;

public class AnaliseLexicaTest {
	
	@Test
	public void testeArquivo1() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA1_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo2() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA2_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo3() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA3_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo4() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA4_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test(expected=AnaliseLexicaException.class)
	public void testeArquivo5() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA5_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test(expected=AnaliseLexicaException.class)
	public void testeArquivo6() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA6_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo7() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA7_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo8() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA8_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo9() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA9_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo10() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA10_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo11() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(LEXICO_ENTRADA11_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}

}
