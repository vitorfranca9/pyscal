package br.unibh.pyscal.vo;

import java.io.FileNotFoundException;
import java.util.HashMap;

import br.unibh.pyscal.analisador.AnalisadorLexico;
import br.unibh.pyscal.analisador.AnalisadorSemantico;
import br.unibh.pyscal.analisador.AnalisadorSintatico;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.util.JasminUtil;
import lombok.Getter;
import lombok.Setter;

//as ctrl,command

public class CompiladorVO {

	@Getter @Setter private AnalisadorLexico analisadorLexico;
	@Getter @Setter private AnalisadorSintatico analisadorSintatico;
	@Getter @Setter private AnalisadorSemantico analisadorSemantico;
	@Getter @Setter private ArquivoVO arquivo;
	@Getter @Setter private String fullPath;
	@Getter @Setter private String jCode = "";
	@Getter @Setter private String resultadoJ = "";
	@Getter @Setter private String resultadoJErro = "";
	@Getter @Setter private String resultadoClass = "";
	@Getter @Setter private String resultadoClassErro = "";

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
	
}
