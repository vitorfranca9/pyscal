package br.unibh.pyscal.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;

public class FileUtil {
	private static final String DIR = "/home/vitor/Documents/ambienteJava/gitRepository/pyscal/Pyscal/";
	
	@SuppressWarnings("resource")
	public static ArquivoVO montarArquivo(String path) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(DIR+path));
		ArquivoVO arquivo = new ArquivoVO();
		arquivo.setNomeArquivo(path);
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
	
	public static void imprimirLinhas(ArquivoVO arquivo) {
		for (LinhaVO linha : arquivo.getLinhas()) {
			if (!linha.getConteudo().isEmpty()) {
				System.out.println(linha.getConteudo());
			}
		}
	}
	
	public static void imprimirPalavras(ArquivoVO arquivo) {
		for (LinhaVO linha : arquivo.getLinhas()) {
			for (String palavra : linha.getPalavras()) {
				System.out.println(palavra);
			}
		}
	}
	
	public static void imprimirTokens(ArquivoVO arquivo) {
		for (LinhaVO linha : arquivo.getLinhas()) {
			if (!linha.getConteudo().isEmpty()) {
				System.out.println("Linha: "+linha.getNumero()+"; Conteudo: "+linha.getConteudo());
				System.out.println("Tokens: ");
				imprimirTokens(linha.getTokens());
				System.out.println();
			}
		}
	}
	
	public static void imprimirTokens(List<TokenVO> tokens) {
		for (TokenVO token : tokens) {
			System.out.println(token.getValor()+","+token.getPalavraReservada());
		}
	}

}
