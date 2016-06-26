package br.unibh.pyscal.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Scanner;

import jasmin.Main;

public class JasminTest {
	
	public static int func(int i, int j) {
		return (i + j * 5);
	}
	
	public static void runAssemble(String jPath, String jCode) throws IOException {
		Main main = new Main();
//		main.assemble(jCode);
		
		String comando = "java -jar jasmin.jar -d ./arquivos_fonte/semantico/ ./arquivos_fonte/semantico/CodigoTeste.j";
//		String comando = "java -jar jasmin.jar " + path + ".j";
		Process cmd = Runtime.getRuntime().exec(comando);
		printInput(cmd.getInputStream());
        printInput(cmd.getErrorStream());
        String classPath = jPath.substring(0,jPath.length()-2) + ".class";
        classPath = "CodigoTeste";
        
        cmd = Runtime.getRuntime().exec("java " + classPath);
        printInput(cmd.getInputStream());
        printInput(cmd.getErrorStream());
		
		
	}
	
	public static void printInput(InputStream is) throws IOException {
		String linha;
        BufferedReader input = new BufferedReader(new InputStreamReader(is));
        while ((linha = input.readLine()) != null) {
            System.out.println(linha);
        }
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
		String temp = Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]","");
	}
	
	public static void main(String[] args) throws IOException {
		int x;
		int y;
		x = 5;
		y = 2;
		String path = "./arquivos_fonte/semantico/CodigoTeste.j";
		String jCode = loadJ(path);
		jCode = normalyze(jCode);
//		String jCode = loadJ("C:/Users/11210971/Desktop/test/src/Codigo.j");
		runAssemble(path, jCode);
//		runAssemble(".class public Codigo 	.super java/lang/Object 		.method public static func(II)I .limit stack 10 .limit locals 10 	iload 1 	ldc 5 	imul 	iload 0 	iadd 	ireturn 		.end method 		.method public static main([Ljava/lang/String;)V .limit stack 10 .limit locals 10 ldc 5 istore 0 ldc 2 istore 1 ;println getstatic java/lang/System/out/println Ljava/io/PrintStream; iload 0 iload 1 invokestatic Codigo/func(II)I invokevirtual java/io/PrintStream/println(I)V  return 		.end method");
		
	}
//	id q tiver no class e passar pro j
//	ldc carrega na pilha
//	java -jar jasmin.jar -d ./ ./arquivos_fonte/semantico/Codigo.j

}
