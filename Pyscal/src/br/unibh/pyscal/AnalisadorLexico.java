package br.unibh.pyscal;

import java.util.List;

public class AnalisadorLexico {

	public List<Token> analisar(Arquivo arquivo) {
		
		for (Linha linha : arquivo.getLinhas()) {
			
			for (String palavra : linha.getPalavras()) {
				
				for (char letra : palavra.toCharArray()) {
					
				}
				
			}
			
		}
		
		return null;
	}

}
