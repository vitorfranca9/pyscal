package br.unibh.vo;

import java.util.ArrayList;
import java.util.List;

public class ArquivoVO {

	private String conteudoOriginal = "";
	private List<LinhaVO> linhas = new ArrayList<>();
	
	public void adicionarLinha(int numeroLinha, String conteudo) {
		linhas.add(new LinhaVO(numeroLinha, conteudo));
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
