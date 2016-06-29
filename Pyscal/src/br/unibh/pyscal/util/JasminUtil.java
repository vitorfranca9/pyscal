package br.unibh.pyscal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Collections;
import java.util.Scanner;

import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.ComandoVO;
import br.unibh.pyscal.vo.MetodoVO;
import jasmin.Main;

public class JasminUtil {
//	private String code = ".class public " + "lexema" + "\n" +
//  ".super java/lang/Object\n" +
//  "identa" + ".method public static main([Ljava/lang/String;)V\n" +
//  identa + ".limit stack 50\n" +
//  identa + ".limit locals 50\n" + 
//  noListaCmd.code + 
//  identa + "    return\n" +
//  identa + ".end method";
//"getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
//"    " + noExp.code +
//identa +"invokevirtual java/io/PrintStream/println(" + tipoAssembly + ")V\n";
//TIPO_INT, new String[]{"I","i"});
//TIPO_STRING, new String[]{"Ljava/lang/String;", "a"});
//No.TIPO_BOOLEAN, new String[]{"I","i"});
//Token.MAIOR, new String[]{"cmple",""});
//Token.MAIOR_IGUAL, new String[]{"cmplt",""});
//identa + "if_" + tipoAssembly + tipoOperador + " " + noExpLinhaPai.fail + "\n" +
//identa + "ldc 1\n" +
//identa + "goto " + noExpLinhaPai.next + "\n" +
//identa + noExpLinhaPai.fail + ": \n" + 
//identa + "ldc 0\n" +
//identa + noExpLinhaPai.next + ":\n" +
	
//	private StringBuilder
	
	public static String getJCode(ArquivoVO arquivo) {
		Collections.reverse(arquivo.getClasseVO().getMetodos());
		for (MetodoVO metodo : arquivo.getClasseVO().getMetodos()) {
			System.out.println();
			
			for (ComandoVO comando : metodo.getComandos()) {
				System.out.println();
			}
		}
		return null;
	}
	
	public static int func(int i, int j) {
		return (i + j * 5);
	}
	
	public static void runAssemble(String jCode) {
		Main main = new Main();
		main.assemble(jCode);
	}
	
	@SuppressWarnings("resource")
	public static String loadJ(String path) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path), "UTF-8");
		StringBuilder sb = new StringBuilder();
		while (sc.hasNext()) {
			String line = sc.nextLine();
			sb.append(normalyze(line));
//			System.out.println(line);
		}
		return sb.toString();
	}
	
	public static String normalyze(String str) {
//		Pattern p = Pattern.compile("{cntrl}");
//		Matcher m = p.matcher("");
//		m.reset(str);
//		String result = m.replaceAll("");
//		return result;
		String temp = Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]","");
	}
	
	private static final String DIR = "/home/vitor/Documents/ambienteJava/gitRepository/pyscal/Pyscal";
	
	
	public static void imprimeSaidaComando(InputStream tipoSaida) throws IOException {
        String linha;
        BufferedReader input = new BufferedReader(new InputStreamReader(tipoSaida));
        while ((linha = input.readLine()) != null) {
            System.out.println(linha);
        }
    }
	
	public static void compileJCodeToClass(String path) throws IOException {
		path = DIR + path;
		Process cmd = Runtime.getRuntime().exec("java -jar jasmin.jar " + path);
		imprimeSaidaComando(cmd.getInputStream());
		imprimeSaidaComando(cmd.getErrorStream());
	}
	
	public static void main(String[] args) throws IOException {
		int x;
		int y;
		x = 5;
		y = 2;
//		System.out.println(func(x, y));
//		String jCode = loadJ("C:\\Users\\11210971\\Desktop\\test\\src\\Codigo.j");
//		String jCode = loadJ("C:/Users/11210971/Desktop/test/src/Codigo.j");
		String path = DIR + PyscalConstantUtil.ArquivosTesteOutros.CODIGO;
		String jCode = loadJ(path);
		Process cmd = Runtime.getRuntime().exec("java -jar jasmin.jar " + path);
		imprimeSaidaComando(cmd.getInputStream());
		imprimeSaidaComando(cmd.getErrorStream());
//		runAssemble(jCode);
//		runAssemble(".class public Codigo 	.super java/lang/Object 		.method public static func(II)I .limit stack 10 .limit locals 10 	iload 1 	ldc 5 	imul 	iload 0 	iadd 	ireturn 		.end method 		.method public static main([Ljava/lang/String;)V .limit stack 10 .limit locals 10 ldc 5 istore 0 ldc 2 istore 1 ;println getstatic java/lang/System/out/println Ljava/io/PrintStream; iload 0 iload 1 invokestatic Codigo/func(II)I invokevirtual java/io/PrintStream/println(I)V  return 		.end method");
		
	}
//	id q tiver no class e passar pro j
//	ldc carrega na pilha

}
