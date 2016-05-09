package br.unibh.pyscal.test;

import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_ATRIBUI;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_COMANDOS;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_ENTRADA1;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_ENTRADA2;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_ENTRADA3;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_EXPRESSAO;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_FUNCAO;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_FUNCAO2;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_IF;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_MAIN;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_RETURN;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_WHILE;
import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSintatico.SINTATICO_WRITE;

import java.io.FileNotFoundException;

import org.junit.Test;

import br.unibh.pyscal.analisador.AnalisadorLexico;
import br.unibh.pyscal.analisador.AnalisadorSintatico;
import br.unibh.pyscal.exception.AnaliseLexicaException;
import br.unibh.pyscal.exception.AnaliseSintaticaException;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.vo.ArquivoVO;

public class AnaliseSintaticaTest {
	
	@Test
	public void testeAtribui() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_ATRIBUI;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeComandos() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_COMANDOS;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeEntrada1() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_ENTRADA1;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeEntrada2() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_ENTRADA2;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeEntrada3() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_ENTRADA3;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeExpressao() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_EXPRESSAO;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeFuncao() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_FUNCAO;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeFuncao2() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_FUNCAO2;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeIf() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_IF;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeMain() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_MAIN;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeReturn() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_RETURN;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeWhile() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_WHILE;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}
	
	@Test
	public void testeWrite() throws AnaliseSintaticaException, AnaliseLexicaException, FileNotFoundException {
		String sintaticoEntrada1Pys = SINTATICO_WRITE;
		ArquivoVO arquivo = FileUtil.montarArquivo(sintaticoEntrada1Pys);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
	}

}
