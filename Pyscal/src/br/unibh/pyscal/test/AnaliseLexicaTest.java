package br.unibh.pyscal.test;

import org.junit.Test;

import br.unibh.exception.AnaliseLexicaException;
import br.unibh.pyscal.analisador.AnalisadorLexico;
import br.unibh.util.FileUtil;
import br.unibh.vo.ArquivoVO;

//TODO
public class AnaliseLexicaTest {
	public static final String ARQUIVO_ENTRADA1_PYS = "arquivo_entrada1.pys";
	public static final String ARQUIVO_ENTRADA2_PYS = "arquivo_entrada2.pys";
	public static final String ARQUIVO_ENTRADA3_PYS = "arquivo_entrada3.pys";
	public static final String ARQUIVO_ENTRADA4_PYS = "arquivo_entrada4.pys";
	public static final String ARQUIVO_ENTRADA5_PYS = "arquivo_entrada5.pys";
	public static final String ARQUIVO_ENTRADA6_PYS = "arquivo_entrada6.pys";
	public static final String ARQUIVO_ENTRADA7_PYS = "arquivo_entrada7.pys";
	public static final String ARQUIVO_ENTRADA8_PYS = "arquivo_entrada8.pys";
	public static final String ARQUIVO_ENTRADA9_PYS = "arquivo_entrada9.pys";
	
	@Test
	public void testeArquivo1() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(ARQUIVO_ENTRADA1_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo2() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(ARQUIVO_ENTRADA2_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo3() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(ARQUIVO_ENTRADA3_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo4() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(ARQUIVO_ENTRADA4_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test(expected=AnaliseLexicaException.class)
	public void testeArquivo5() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(ARQUIVO_ENTRADA5_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test(expected=AnaliseLexicaException.class)
	public void testeArquivo6() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(ARQUIVO_ENTRADA6_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo7() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(ARQUIVO_ENTRADA7_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test(expected=AnaliseLexicaException.class)
	public void testeArquivo8() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(ARQUIVO_ENTRADA8_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}
	
	@Test
	public void testeArquivo9() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(ARQUIVO_ENTRADA9_PYS);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
//		FileUtil.imprimirTokens(arquivo);
	}

}
