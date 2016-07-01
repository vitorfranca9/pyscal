package br.unibh.pyscal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Scanner;

import br.unibh.pyscal.enumerador.TipoRetornoMetodoEnum;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.ComandoVO;
import br.unibh.pyscal.vo.MetodoVO;
import br.unibh.pyscal.vo.VariavelVO;
import jasmin.Main;

public class JasminUtil {
	//TODO validar funções q não são desta responsa(FileUtil...)
//	private static final String DIR = "D:/Users/p065815/git/pyscal/Pyscal/";
	private static final String DIR = "/home/vitor/Documents/ambienteJava/gitRepository/pyscal/Pyscal";
	
//		private long startTime;
//		private long endTime;
	
	public static String getJ(ArquivoVO arquivo) {
		StringBuilder code = new StringBuilder(getMain(arquivo));
		if (arquivo != null) {
//			Collections.reverse(arquivo.getClasseVO().getMetodos());
			for (MetodoVO metodo : arquivo.getClasseVO().getMetodos()) {
				System.out.println();
				for (ComandoVO comando : metodo.getComandos()) {
					code.append(getCmd(metodo, comando));
				}
			}
		}
		code.append("    return\n");
		code.append(".end method\n");
		return code.toString();
	}
	
	public static String getCmd(MetodoVO metodo, ComandoVO comando) {
		switch (comando.getTipoComando()) {
			case WRITE:
				return getWrite(comando.getVariavelRetorno(), metodo.getTipoRetornoMetodo());
			default: return null; 
		}
	}
	
	public static String getWrite(VariavelVO variavel, TipoRetornoMetodoEnum tipoRetornoMetodoEnum) {
		return new StringBuilder()
			.append("ldc ")
			.append(getValue(variavel))
			.append("\n")
			.append(variavel.getTipoVariavel().getAssembleType())
			.append("store 0\n")
			.append("getstatic java/lang/System/out Ljava/io/PrintStream;\n")
			.append(variavel.getTipoVariavel().getAssembleType())
			.append("load 0\n")
			.append("invokevirtual java/io/PrintStream/print(")
			.append(variavel.getTipoVariavel().getAssembleInvokeType())
			.append(")")
			.append(tipoRetornoMetodoEnum.getAssembleReturnType())
			.append("\n")
			.toString();
	}
	
	private static String getValue(VariavelVO variavel) {
		switch (variavel.getTipoVariavel()) {
			case BOOL:
				return variavel.getTokem().getValor();
			case DOUBLE:
				return variavel.getTokem().getValor();
			case INTEGER:
				return variavel.getTokem().getValor();
			case STRING:
				return variavel.getTokem().getValor();
			default: return null;
		}
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
	public static String loadJFile(String path) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path), "UTF-8");
		StringBuilder sb = new StringBuilder();
		while (sc.hasNext()) {
			String line = sc.nextLine();
			sb.append(normalyze(line));
//			System.out.println(line);
		}
		return sb.toString();
	}
	
	public static void writeJFile(String fullPath, String jCode) throws IOException {
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
		String name = jCode.substring(jCode.indexOf(".class public ")+14, jCode.indexOf("\n.super"));
		return name;
	}
	
	public static String normalyze(String str) {
		String temp = Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]","");
	}
	
	
	public static void imprimeSaidaComando(InputStream tipoSaida, boolean isError, String message) throws IOException {
        String linha;
        BufferedReader input = new BufferedReader(new InputStreamReader(tipoSaida));
        while ((linha = input.readLine()) != null) {
            System.out.println(linha);
        }
    }
	
	public static void jToClass(String fullPath) throws IOException {
		String jName = DIR + getJName(fullPath);
		Process cmd = Runtime.getRuntime().exec("java -jar jasmin.jar " + jName);
		imprimeSaidaComando(cmd.getInputStream(),false,"");
		imprimeSaidaComando(cmd.getErrorStream(),false,"");	//TODO validar caso tenha erros
		System.out.println();
	}
	
	private static String getJName(String fullPath) {
		String jName = fullPath.substring(0,fullPath.length()-5)+"j";
		return jName;
	}
	
	public static void runClass(String fullPath) throws IOException {
		String classFileName = getFileName(fullPath);
		Process cmd = Runtime.getRuntime().exec("java "+classFileName);
        imprimeSaidaComando(cmd.getInputStream(),false,"");
        imprimeSaidaComando(cmd.getErrorStream(),false,"");
	}
	
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
