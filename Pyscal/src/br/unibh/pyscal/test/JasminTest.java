package br.unibh.pyscal.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.Normalizer;
import java.util.Scanner;

import jasmin.Main;

public class JasminTest {
	
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
	
	public static void main(String[] args) throws FileNotFoundException {
		int x;
		int y;
		x = 5;
		y = 2;
//		System.out.println(func(x, y));
//		String jCode = loadJ("C:\\Users\\11210971\\Desktop\\test\\src\\Codigo.j");
		String jCode = loadJ("C:/Users/11210971/Desktop/test/src/Codigo.j");
		runAssemble(jCode);
//		runAssemble(".class public Codigo 	.super java/lang/Object 		.method public static func(II)I .limit stack 10 .limit locals 10 	iload 1 	ldc 5 	imul 	iload 0 	iadd 	ireturn 		.end method 		.method public static main([Ljava/lang/String;)V .limit stack 10 .limit locals 10 ldc 5 istore 0 ldc 2 istore 1 ;println getstatic java/lang/System/out/println Ljava/io/PrintStream; iload 0 iload 1 invokestatic Codigo/func(II)I invokevirtual java/io/PrintStream/println(I)V  return 		.end method");
		
	}
//	id q tiver no class e passar pro j
//	ldc carrega na pilha

}
