package br.unibh.pyscal.vo;

import java.io.FileNotFoundException;
import java.util.HashMap;

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
	private String jCode = "";
	private String resultadoJ = "";
	private String resultadoJErro = "";
	private String resultadoClass = "";
	private String resultadoClassErro = "";

	public CompiladorVO(String fullPath) throws FileNotFoundException {
		analisadorLexico = new AnalisadorLexico();
		analisadorSintatico = new AnalisadorSintatico();
		analisadorSemantico = new AnalisadorSemantico();
		this.arquivo = FileUtil.montarArquivo(fullPath);
		this.fullPath = fullPath;
	}

	public void analisar() throws Exception {
		AnalisadorSemantico.setMapaStack(new HashMap<>());
		AnalisadorSemantico.setStackIndex(0);
		analisadorLexico.analisar(arquivo);
		analisadorSintatico.analisar(arquivo);
		analisadorSemantico.analisar(arquivo);
	}
	
	private void init() {
		jCode = "";
		resultadoJ = "";
		resultadoJErro = "";
		resultadoClass = "";
		resultadoClassErro = "";
	}
	
	public void compilar() throws Exception {
//		JasminUtil.jToClass(path);
		init();
		String jCode = JasminUtil.getJ(analisadorSemantico.getArquivo());
		System.out.println(jCode);
		this.jCode = jCode;
		FileUtil.writeJFile(fullPath,jCode);
		JasminUtil.jToClass(fullPath+".j");
	}
	
	public void rodar() throws Exception {
		JasminUtil.runClass(fullPath);
		resultadoJ = FileUtil.resultadoJ;
		resultadoJErro = FileUtil.resultadoJErro;
		resultadoClass = FileUtil.resultadoClass;
		resultadoClassErro = FileUtil.resultadoClassErro;
		/*FileUtil.imprimirTokens(arquivo);*/
	}

	public AnalisadorLexico getAnalisadorLexico() {
		return analisadorLexico;
	}

	public void setAnalisadorLexico(AnalisadorLexico analisadorLexico) {
		this.analisadorLexico = analisadorLexico;
	}

	public AnalisadorSintatico getAnalisadorSintatico() {
		return analisadorSintatico;
	}

	public void setAnalisadorSintatico(AnalisadorSintatico analisadorSintatico) {
		this.analisadorSintatico = analisadorSintatico;
	}

	public AnalisadorSemantico getAnalisadorSemantico() {
		return analisadorSemantico;
	}

	public void setAnalisadorSemantico(AnalisadorSemantico analisadorSemantico) {
		this.analisadorSemantico = analisadorSemantico;
	}

	public ArquivoVO getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoVO arquivo) {
		this.arquivo = arquivo;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getJCode() {
		return jCode;
	}

	public void setJCode(String jCode) {
		this.jCode = jCode;
	}

	public String getResultadoJ() {
		return resultadoJ;
	}

	public void setResultadoJ(String resultadoJ) {
		this.resultadoJ = resultadoJ;
	}

	public String getResultadoJErro() {
		return resultadoJErro;
	}

	public void setResultadoJErro(String resultadoJErro) {
		this.resultadoJErro = resultadoJErro;
	}

	public String getResultadoClass() {
		return resultadoClass;
	}

	public void setResultadoClass(String resultadoClass) {
		this.resultadoClass = resultadoClass;
	}

	public String getResultadoClassErro() {
		return resultadoClassErro;
	}

	public void setResultadoClassErro(String resultadoClassErro) {
		this.resultadoClassErro = resultadoClassErro;
	}
	
}
