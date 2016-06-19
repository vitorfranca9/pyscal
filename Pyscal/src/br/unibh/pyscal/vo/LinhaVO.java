package br.unibh.pyscal.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class LinhaVO {
	
	@Getter @Setter private int numero;
	@Getter @Setter private int numeroReal;
	@Getter @Setter private String conteudo;
	@Getter @Setter private List<String> palavras = new ArrayList<>();
	@Getter @Setter private List<TokenVO> tokens = new ArrayList<>();
	
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
	
	@Override
	public String toString() {
		return "LinhaVO [numero=" + numero + ", conteudo=" + conteudo + ", palavras=" + palavras + ", tokens=" + tokens
				+ "]";
	}
}
