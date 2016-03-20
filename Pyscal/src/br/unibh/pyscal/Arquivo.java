package br.unibh.pyscal;

import java.util.ArrayList;
import java.util.List;

public class Arquivo {

	private String conteudoOriginal = "";
	private List<Linha> linhas = new ArrayList<>();
	
	public void adicionarLinha(int numeroLinha, String conteudo) {
		linhas.add(new Linha(numeroLinha, conteudo));
	}
	public String getConteudoOriginal() {
		return conteudoOriginal;
	}
	public void setConteudoOriginal(String conteudoOriginal) {
		this.conteudoOriginal = conteudoOriginal;
	}
	public List<Linha> getLinhas() {
		return linhas;
	}
	public void setLinhas(List<Linha> linhas) {
		this.linhas = linhas;
	}
	
}
