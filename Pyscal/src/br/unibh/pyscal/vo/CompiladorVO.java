package br.unibh.pyscal.vo;

import java.io.FileNotFoundException;

import br.unibh.pyscal.analisador.AnalisadorLexico;
import br.unibh.pyscal.analisador.AnalisadorSemantico;
import br.unibh.pyscal.analisador.AnalisadorSintatico;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.util.JasminUtil;

//as ctrl,command

public class CompiladorVO {

	private AnalisadorLexico analisadorLexico;
	private AnalisadorSintatico analisadorSintatico;
	private AnalisadorSemantico analisadorSemantico;
	private ArquivoVO arquivo;
	private String fullPath;

	public CompiladorVO(String fullPath) throws FileNotFoundException {
		analisadorLexico = new AnalisadorLexico();
		analisadorSintatico = new AnalisadorSintatico();
		analisadorSemantico = new AnalisadorSemantico();
		this.arquivo = FileUtil.montarArquivo(fullPath);
		this.fullPath = fullPath;
	}

	public void analisar() throws Exception {
		analisadorLexico.analisar(arquivo);
		analisadorSintatico.analisar(arquivo);
		analisadorSemantico.analisar(arquivo);
	}
	
	public void compilar(String fullPath) throws Exception {
//		JasminUtil.jToClass(path);
		String jCode = JasminUtil.getJ(analisadorSemantico.getArquivo());
		System.out.println(jCode);
		JasminUtil.writeJFile(fullPath,jCode);
		JasminUtil.jToClass(fullPath+".j");
	}
	
	public void rodar() throws Exception {
		JasminUtil.runClass(fullPath);
		/*FileUtil.imprimirTokens(arquivo);*/
	}
	
}
