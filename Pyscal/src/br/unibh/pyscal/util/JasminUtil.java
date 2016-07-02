package br.unibh.pyscal.util;

import java.io.IOException;

import br.unibh.pyscal.enumerador.TipoRetornoMetodoEnum;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.ComandoVO;
import br.unibh.pyscal.vo.MetodoVO;
import br.unibh.pyscal.vo.VariavelVO;

public class JasminUtil {
//	private static final String DIR = "D:/Users/p065815/git/pyscal/Pyscal/";
	private static final String DIR = "/home/vitor/Documents/ambienteJava/gitRepository/pyscal/Pyscal";
	private static final int METHOD_LIMIT_STACK = 30;
	private static final int METHOD_LIMIT_LOCALS = 30;
//	private long startTime;
//	private long endTime;
	
	public static String getJ(ArquivoVO arquivo) {
		MetodoVO main = null;
		StringBuilder code = new StringBuilder();
		code.append(getClasss(arquivo));
		
		if (arquivo != null) {
//			Collections.reverse(arquivo.getClasseVO().getMetodos());
			for (MetodoVO metodo : arquivo.getClasseVO().getMetodos()) {
				if (!metodo.isMain()) {
					String method = getMethod(metodo);
					code.append(method).append("\n");
				} else {
					main = metodo;
				}
			}
		}
		code.append(getMain(arquivo,main));
		return code.toString();
//		code.append("invokestatic ");
//		code.append(getFileName(arquivo));
//		code.append("/");
//		code.append("imprimir");
//		code.append("()V\n");
	}
	
	public static String getClasss(ArquivoVO arquivo) {
		return new StringBuilder()
			.append(getLine(".class public "+arquivo.getNomeArquivo().substring(
				arquivo.getNomeArquivo().lastIndexOf("/")+1, arquivo.getNomeArquivo().lastIndexOf(".")), null))
			.append(getLine(".super java/lang/Object", null))
			.append("\n")
			.append(getLine(".method public <init>()V", true))
			.append(getLine("aload_0", true))
			.append(getLine("invokenonvirtual java/lang/Object/<init>()V", null))
			.append(getLine("return", null))
			.append(getLine(".end method", false))
			.append("\n")
			.toString();
	}
	
	private static String getMethod(MetodoVO metodo) {
		StringBuilder method = new StringBuilder()
			.append(getLine(".method public "+metodo.getNome()+"()"+metodo.getTipoRetornoMetodo().getAssembleInvokeType(), null))
			.append(getLine(".limit stack "+METHOD_LIMIT_STACK, null)).append(getLine(".limit locals "+METHOD_LIMIT_LOCALS, null));
		for (ComandoVO comando : metodo.getComandos()) {
			method.append(getCmd(metodo, comando));
		}
		method.append(getLine(metodo.getTipoRetornoMetodo().getAssembleReturnType()+"return", null))
		.append(getLine(".end method",false));
		return method.toString();
	}
	
	public static String getCmd(MetodoVO metodo, ComandoVO comando) {
		switch (comando.getTipoComando()) {
			case WRITE:
				return getWrite(comando.getVariavelRetorno(), metodo.getTipoRetornoMetodo());
			default: return null; 
		}
	}
//	createWrite(TokenVO,LinhaVO,methodName)
	public static String getWrite(VariavelVO variavel, TipoRetornoMetodoEnum tipoRetornoMetodoEnum) {
		return new StringBuilder()
			.append(getLine("ldc "+getValue(variavel), null))
			.append(getLine(variavel.getTipoVariavel().getAssembleType() + "store 0", null))
			.append(getLine("getstatic java/lang/System/out Ljava/io/PrintStream;", null))
			.append(getLine(variavel.getTipoVariavel().getAssembleType()+"load 0", null))
			.append(getLine("invokevirtual java/io/PrintStream/print("+variavel.getTipoVariavel().getAssembleInvokeType()+")"+
				tipoRetornoMetodoEnum.getAssembleInvokeType(), null))
			.toString();
	}
	
	public static String getMain(ArquivoVO arquivo, MetodoVO metodoMain) {
		StringBuilder main = new StringBuilder();
		main.append(getLine(".method public static main([Ljava/lang/String;)V", null))
			.append(getLine(".limit stack "+METHOD_LIMIT_STACK, null))
			.append(getLine(".limit locals "+METHOD_LIMIT_LOCALS, null))
			.append(getClassInstance(arquivo));
		
		for (ComandoVO comando : metodoMain.getComandos()) {
			main.append(getCmd(metodoMain, comando));
		}
//		main.append(getLine("invokevirtual "+getFileName(arquivo)+"."+"imprimir()V", null));
		
		main.append(getLine(metodoMain.getTipoRetornoMetodo().getAssembleReturnType()+"return", null))
			.append(getLine(".end method",false));
//		code.append("%object Comandos\n");
//		code.append("invokespecial ");
//		code.append(getLine("aload_0",true));
		return main.toString(); 
	}
	
	private static String getClassInstance(ArquivoVO arquivo) {
		return new StringBuilder(getLine("new "+getFileName(arquivo), true))
			.append(getLine("dup", null))
			.append(getLine("invokespecial "+getFileName(arquivo)+"/<init>()V", null)).toString();
	}
	
	public static void jToClass(String fullPath) throws IOException {
		String jName = DIR + getJName(fullPath);
		Process cmd = Runtime.getRuntime().exec("java -jar ./lib/jasmin.jar " + jName);
		FileUtil.imprimeSaidaComando(cmd.getInputStream(),false,"");
		FileUtil.imprimeSaidaComando(cmd.getErrorStream(),false,"");	//TODO validar caso tenha erros
		System.out.println();
	}
	
	public static void runClass(String fullPath) throws IOException {
		String classFileName = getFileName(fullPath);
		Process cmd = Runtime.getRuntime().exec("java "+classFileName);
		FileUtil.imprimeSaidaComando(cmd.getInputStream(),false,"");
		FileUtil.imprimeSaidaComando(cmd.getErrorStream(),false,"");
	}
	
	private static String getJName(String fullPath) {
		String jName = fullPath.substring(0,fullPath.length()-5)+"j";
		return jName;
	}
	
	private static int identacao = 0;
	
	private static String getLine(String valor, Boolean move) {
		return new StringBuilder(getEspaco(move)).append(valor).append("\n").toString();
	}
	
	private static void move(Boolean move) {
		if (move != null) {
			if (move) {
				identacao++;
			} else {
				identacao--;
			}
		}
	}
	
	private static String getEspaco(Boolean move) {
		move(move);
		StringBuilder barraT = new StringBuilder("");
		for(int i = 0; i < identacao; i++)
//			barraT.append("\t");
			barraT.append("   ");
			
		return barraT.toString();
	}
	
	private static String getValue(VariavelVO variavel) {
		switch (variavel.getTipoVariavel()) {
		case BOOL:
			return variavel.getTokem().getValor().toLowerCase();
		case DOUBLE:
			return variavel.getTokem().getValor();
		case INTEGER:
			return variavel.getTokem().getValor();
		case STRING:
			return variavel.getTokem().getValor();
		default: return null;
		}
	}
	
	private static String getFileName(ArquivoVO arquivo) {
		String fileName = arquivo.getLinhas().get(0).getTokens().get(1).getValor();
		return fileName;
	}
	
	
	private static String getFileName(String fullPath) {
		String fileName = fullPath.substring(fullPath.lastIndexOf("/")+1, fullPath.lastIndexOf("."));
		return fileName;
	}
	
//	public static int func(int i, int j) {
//		return (i + j * 5);
//	}
//	
//	public synchronized static void runAssemble(String jCode) {
//		Main main = new Main();
//		main.assemble(jCode);
//	}
//	
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
