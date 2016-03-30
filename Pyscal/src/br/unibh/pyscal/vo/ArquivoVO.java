package br.unibh.pyscal.vo;

import java.util.ArrayList;
import java.util.List;

public class ArquivoVO {

	private String nomeArquivo = "";
	private String conteudoOriginal = "";
	private List<LinhaVO> linhas = new ArrayList<>();
	
	public void adicionarLinha(int numeroLinha, String conteudo) {
		linhas.add(new LinhaVO(numeroLinha, conteudo));
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public String getConteudoOriginal() {
		return conteudoOriginal;
	}
	public void setConteudoOriginal(String conteudoOriginal) {
		this.conteudoOriginal = conteudoOriginal;
	}
	public List<LinhaVO> getLinhas() {
		return linhas;
	}
	public void setLinhas(List<LinhaVO> linhas) {
		this.linhas = linhas;
	}
	
}
