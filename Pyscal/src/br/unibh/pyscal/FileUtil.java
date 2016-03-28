package br.unibh.pyscal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileUtil {
	
	@SuppressWarnings("resource")
	public static Arquivo montarArquivo(String path) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path));
		Arquivo arquivo = new Arquivo();
		int linhaCount = 1;
		
		while (sc.hasNextLine()) {
			String linha = sc.nextLine();
			String linhaFormatada = StringUtil.tratarCaracteres(linha); 
			arquivo.adicionarLinha(linhaCount, linhaFormatada);
			arquivo.setConteudoOriginal(arquivo.getConteudoOriginal()+"\n"+linha);
			linhaCount++;
		}
		
		return arquivo;
	}
	
	public static void imprimirLinhas(Arquivo arquivo) {
		
		for (Linha linha : arquivo.getLinhas()) {
			if (!linha.getConteudo().isEmpty()) {
				System.out.println(linha.getConteudo());
			}
		}
		
	}
	
	public static void imprimirPalavras(Arquivo arquivo) {
		for (Linha linha : arquivo.getLinhas()) {
			for (String palavra : linha.getPalavras()) {
				System.out.println(palavra);
			}
		}
	}
	
	public static void imprimirTokens(Arquivo arquivo) {
		for (Linha linha : arquivo.getLinhas()) {
			if (!linha.getConteudo().isEmpty()) {
				System.out.println("Linha: "+linha.getNumero()+"; Conteudo: "+linha.getConteudo());
				System.out.println("Tokens: ");
				for (Token token : linha.getTokens()) {
					System.out.println(token.getValor()+","+token.getPalavraReservada());
				}
				System.out.println();
			}
		}
	}

}
