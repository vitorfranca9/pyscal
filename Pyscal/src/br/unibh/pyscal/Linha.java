package br.unibh.pyscal;

import java.util.ArrayList;
import java.util.List;

public class Linha {
	
	private int numero;
	private String conteudo;
	private List<String> palavras = new ArrayList<>();
	private List<Token> tokens = new ArrayList<>();
	
	public Linha() { }
	
	public Linha(int numero, String linha) {
		this.numero = numero;
		this.conteudo = linha;
		for (String palavra : linha.split(" ")) {
			if (!palavra.trim().isEmpty()) {
				this.palavras.add(palavra);
			}
		}
	}
	
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public List<String> getPalavras() {
		return palavras;
	}
	public void setPalavras(List<String> palavras) {
		this.palavras = palavras;
	}
	public List<Token> getTokens() {
		return tokens;
	}
	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}
	
}
