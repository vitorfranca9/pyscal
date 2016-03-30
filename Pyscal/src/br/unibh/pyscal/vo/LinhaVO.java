package br.unibh.pyscal.vo;

import java.util.ArrayList;
import java.util.List;

public class LinhaVO {
	
	private int numero;
	private String conteudo;
	private List<String> palavras = new ArrayList<>();
	private List<TokenVO> tokens = new ArrayList<>();
	
	public LinhaVO() { }
	
	public LinhaVO(int numero, String linha) {
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
	public List<TokenVO> getTokens() {
		return tokens;
	}
	public void setTokens(List<TokenVO> tokens) {
		this.tokens = tokens;
	}
	
}
