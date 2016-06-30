package br.unibh.pyscal.util;

import jasmin.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Collections;
import java.util.Scanner;

import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.ComandoVO;
import br.unibh.pyscal.vo.MetodoVO;

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
	
	private long startTime;
	private long endTime;
	
//	private static StringBuilder code = new StringBuilder()
//		.append(".class public Codigo \n")
//		.append(".super java/lang/Object\n")
//		.append(".method public static main([Ljava/lang/String;)V\n")
//		.append(".limit stack 50\n")
//		.append(".limit locals 50\n")
//		.append("ldc 2\n")
//		.append("istore 0\n")
//		.append("getstatic java/lang/System/out Ljava/io/PrintStream;\n")
//		.append("iload 0\n")
//		.append(".invokevirtual java/io/PrintStream/println(\"Hello World\")V\n");
//		.append("invokevirtual java/io/PrintStream/print(I)V\n")
		;
	
	public static String getJ(ArquivoVO arquivo) {
		StringBuilder code = new StringBuilder(getMain(arquivo));
		if (arquivo != null) {
//			Collections.reverse(arquivo.getClasseVO().getMetodos());
			for (MetodoVO metodo : arquivo.getClasseVO().getMetodos()) {
				System.out.println();
				for (ComandoVO comando : metodo.getComandos()) {
					code.append(comando.getTipoComando().getJCode());
				}
			}
		}
		code.append("    return\n");
		code.append(".end method");
		return code.toString();
	}
	
	
	
	public static String getMain(ArquivoVO arquivo) {
		arquivo.setNomeArquivo("/arquivos_fonte/semantico/Comandos.pys");
		return new StringBuilder()
			.append(".class public ")
			.append(arquivo.getNomeArquivo().substring(
				arquivo.getNomeArquivo().lastIndexOf("/")+1, arquivo.getNomeArquivo().lastIndexOf(".")))
			.append("\n")
			.append(".super java/lang/Object\n")
			.append(".method public static main([Ljava/lang/String;)V\n")
			.append(".limit stack 50\n")
			.append(".limit locals 50\n")
			.append("ldc 9\n")
			.append("istore 0\n")
			.append("")
			.toString();
	}
	
	public static int func(int i, int j) {
		return (i + j * 5);
	}
	
	public synchronized static void runAssemble(String jCode) {
		Main main = new Main();
		main.assemble(jCode);
	}
	
	@SuppressWarnings("resource")
	public synchronized static String loadJFile(String path) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path), "UTF-8");
		StringBuilder sb = new StringBuilder();
		while (sc.hasNext()) {
			String line = sc.nextLine();
			sb.append(normalyze(line));
//			System.out.println(line);
		}
		return sb.toString();
	}
	
	public synchronized static void writeJFile(String fullPath, String jCode) throws IOException {
		FileWriter fileWriter = new FileWriter(new File(DIR+getPath(fullPath)+getName(jCode)+".j"));
		fileWriter.write(jCode);
		fileWriter.flush();
		fileWriter.close();
	}
	
	private static String getFileName(String fullPath) {
		String fileName = fullPath.substring(fullPath.lastIndexOf("/")+1, fullPath.lastIndexOf("."));
		return fileName;
	}
	
	private static String getPath(String fullPath) {
		String path = fullPath.substring(0,fullPath.lastIndexOf("/")+1);
		return path;
	}
	
	private static String getName(String jCode) {
		return jCode.substring(jCode.indexOf(".class public ")+14, jCode.indexOf("\n.super"));
	}
	
	public static String normalyze(String str) {
		String temp = Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]","");
	}
	
	private static final String DIR = "/home/vitor/Documents/ambienteJava/gitRepository/pyscal/Pyscal";
//	private static final String DIR = "D:/Users/p065815/git/pyscal/Pyscal/";
	
	public static void imprimeSaidaComando(InputStream tipoSaida) throws IOException {
        String linha;
        BufferedReader input = new BufferedReader(new InputStreamReader(tipoSaida));
        while ((linha = input.readLine()) != null) {
            System.out.println(linha);
        }
    }
	
	public synchronized static void jToClass(String fullPath) throws IOException {
		fullPath = fullPath.replace(".pys", "");
		fullPath = DIR+"/" + getFileName(fullPath);
		Process cmd = Runtime.getRuntime().exec("java -jar jasmin.jar " + fullPath);
		imprimeSaidaComando(cmd.getInputStream());
		imprimeSaidaComando(cmd.getErrorStream());
	}
	
	public synchronized static void runClass(String path) throws IOException {
//		path = DIR + path;
		path = "Comandos";
		Process cmd = Runtime.getRuntime().exec("java "+path);
        imprimeSaidaComando(cmd.getInputStream());
        imprimeSaidaComando(cmd.getErrorStream());
	}
	
//	public static void main(String[] args) throws IOException {
//		String path = "Codigo";
//		System.out.println("Running "+path+"...");
//		String jCode = getJ(null);
//		writeJFile(jCode);
//		jToClass(path+".j");
//		runClass(path);
//	}
	
//	public static void main(String[] args) throws IOException {
//		int x;
//		int y;
//		x = 5;
//		y = 2;
//		System.out.println(func(x, y));
//		String jCode = loadJ("C:\\Users\\11210971\\Desktop\\test\\src\\Codigo.j");
//		String jCode = loadJ("C:/Users/11210971/Desktop/test/src/Codigo.j");
//		String path = DIR + PyscalConstantUtil.ArquivosTesteOutros.CODIGO;
//		String jCode = loadJ(path);
//		Process cmd = Runtime.getRuntime().exec("java -jar jasmin.jar " + path);
//		imprimeSaidaComando(cmd.getInputStream());
//		imprimeSaidaComando(cmd.getErrorStream());
//		runAssemble(jCode);
//		runAssemble(".class public Codigo 	.super java/lang/Object 		.method public static func(II)I .limit stack 10 .limit locals 10 	iload 1 	ldc 5 	imul 	iload 0 	iadd 	ireturn 		.end method 		.method public static main([Ljava/lang/String;)V .limit stack 10 .limit locals 10 ldc 5 istore 0 ldc 2 istore 1 ;println getstatic java/lang/System/out/println Ljava/io/PrintStream; iload 0 iload 1 invokestatic Codigo/func(II)I invokevirtual java/io/PrintStream/println(I)V  return 		.end method");
//	}
//	id q tiver no class e passar pro j
//	ldc carrega na pilha

}
