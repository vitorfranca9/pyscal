package br.unibh.pyscal.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.NoVO;
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

	public static void imprimirAST(ArquivoVO arquivo) {
		NoVO noAux = arquivo.getNoRaiz();
		imprimir(noAux);
		System.out.println();
		imprimirFilhos(noAux);
	}
	
	private static void imprimirFilhos(NoVO noPai) {
//		System.out.println("	Filhos de "+noPai.getTokens()+":");
		if (!noPai.getFilhos().isEmpty()) {
			System.out.println("	Filhos de '"+noPai.getTokens().get(0).getValor()+"':");
			for (NoVO filho : noPai.getFilhos()) {
				imprimir(filho);
				System.out.print(" Filho de: '"+noPai.getTokens().get(0).getValor()+"'");
				System.out.println();
				if (!filho.getFilhos().isEmpty()) {
					System.out.println();
					imprimirFilhos(filho);
				}
			}
		}
	}
	
	private static void imprimir(NoVO noAux) {
//		System.out.print("Nivel: "+noAux.getNivel());
		System.out.print(" Nó: '"+noAux.getTokens().get(0).getValor()+"'");
//		System.out.print( noAux.getLinha() != null ? " Linha: "+noAux.getLinha().getConteudo() : "");
	}

}
