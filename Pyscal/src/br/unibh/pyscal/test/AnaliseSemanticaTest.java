package br.unibh.pyscal.test;

import static br.unibh.pyscal.util.PyscalConstantUtil.ArquivosTesteSemantico.*;


import org.junit.Test;

import br.unibh.pyscal.analisador.AnalisadorLexico;
import br.unibh.pyscal.analisador.AnalisadorSemantico;
import br.unibh.pyscal.analisador.AnalisadorSintatico;
import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.vo.ArquivoVO;

public class AnaliseSemanticaTest {
	
	@Test(expected=AnaliseSemanticaException.class)
	public void ex1() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(EX1_DECLARACAO_DUPLICADA);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
		AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico();
		analisadorSemantico.analisar(arquivo);
	}
	
	@Test(expected=AnaliseSemanticaException.class)
	public void ex2() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(EX2_VARIAVEL_VOID);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
		AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico();
		analisadorSemantico.analisar(arquivo);
	}
	
	@Test(expected=AnaliseSemanticaException.class)
	public void metodoDuplicado() throws Exception {
		ArquivoVO arquivo = FileUtil.montarArquivo(METODO_DUPLICADO);
		AnalisadorLexico analisadorLexico = new AnalisadorLexico();
		analisadorLexico.analisar(arquivo);
		AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico();
		analisadorSintatico.analisar(arquivo);
		AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico();
		analisadorSemantico.analisar(arquivo);
	}

}
