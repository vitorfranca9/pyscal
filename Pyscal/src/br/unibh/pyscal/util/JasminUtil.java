package br.unibh.pyscal.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.sun.xml.internal.fastinfoset.algorithm.IEEE754FloatingPointEncodingAlgorithm;

import br.unibh.pyscal.analisador.AnalisadorSemantico;
import br.unibh.pyscal.enumerador.PalavraReservadaEnum;
import br.unibh.pyscal.enumerador.TipoVariavelEnum;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.EnderecoStackVO;
import br.unibh.pyscal.vo.MetodoVO;
import br.unibh.pyscal.vo.TokenVO;
import br.unibh.pyscal.vo.VariavelVO;

public class JasminUtil {
	private static final String DIR = "D:/Users/p065815/git/pyscal/Pyscal";
//	private static final String DIR = "/home/vitor/Documents/ambienteJava/gitRepository/pyscal/Pyscal";
	private static final int METHOD_LIMIT_STACK = 50;
	private static final int METHOD_LIMIT_LOCALS = 50;
//	private long startTime;
//	private long endTime;
	private static String className = "";
	private static ArquivoVO arquivo;
	
	private static void putOnStackMap(MetodoVO metodo, VariavelVO v) {
		AnalisadorSemantico.adicionarVariavelStack(metodo, v);
		/*if (getMapaStack().containsKey(metodo.getNome())) {
			if (!getMapaStack().containsKey(metodo.getNome())) {
				getMapaStack().get(metodo.getNome()).put(AnalisadorSemantico.getNovoEndereco(v.getNome()),v);
			} else {
				System.out.println();
			}
		} else { //colocar na stack?
			System.out.println();
		}*/
//		Map<String, Map<String, VariavelVO>> mapVariaveis = AnalisadorSemantico.getMapaVariaveis();
//		mapVariaveis.get(metodoPai.getNome()).put(v.getNome(), v);
	}
	
	private static VariavelVO getOnStack(MetodoVO metodo, VariavelVO variavel) {
//		Map<EnderecoStackVO, VariavelVO> map = getMapaStack().get(metodo.getNome());
		VariavelVO variavelStack = AnalisadorSemantico.getVariavelStack(metodo, variavel);
		return variavelStack;
	}
	
	public static String getJ(ArquivoVO arquivo) {
		MetodoVO main = null;
		JasminUtil.arquivo = arquivo;
		className = getClassName(arquivo);
		StringBuilder code = new StringBuilder();
		code.append(getClasss(arquivo));
		String mainCode = "";
		String code2 = "";
		
		//while
		/*code2 = "ldc 5" + "\n" + 
		"istore 0" + "\n" +
		"ldc 2" + "\n" +
		"istore 1" + "\n" +
		"while:" + "\n" +
		
		"iload 0" + "\n" +
		"ifle done" + "\n" +
		
	//		"ldc 1" + "\n" +
	//		"istore_0" + "\n" +
			"getstatic java/lang/System/out Ljava/io/PrintStream;" + "\n" +
			" iload_0" + "\n" +
			" invokevirtual java/io/PrintStream/println(Z)V" + "\n" +
			//"return" + "\n" + 
	//		"iload 0" + "\n" +
	//		"iload 1" + "\n" +
	//		"imul" + "\n" +
	//		"istore 1" + "\n" +
			"iload 0" + "\n" +
			"ldc 1" + "\n" +
			"isub" + "\n" +
			"istore 0" + "\n" +
		
		"goto while" + "\n" +
		
		"done:" + "\n"
//		+ "iload 1" + "\n" 
		;*/
		//endwhile
				
//		code2 = "ldc 0" + "\n" +
//			"ifeq else" + "\n" +
//				"ldc 1" + "\n" +
//				"istore_0" + "\n" +
//				"getstatic java/lang/System/out Ljava/io/PrintStream;" + "\n" +
//				" iload_0" + "\n" +
//				" invokevirtual java/io/PrintStream/print(Z)V" + "\n" +
//				"return" + "\n" + 
//			
//			"else:"+ "\n" +
//				"ldc 0" + "\n" +
//				"istore_0" + "\n" +
//				"getstatic java/lang/System/out Ljava/io/PrintStream;" + "\n" +
//				" iload_0" + "\n" +
//				" invokevirtual java/io/PrintStream/print(Z)V" + "\n"; 
		
//		mainCode = ".method public static main([Ljava/lang/String;)V" + "\n" +
//			".limit stack 50" + "\n" +
//			".limit locals 50" + "\n" +
//			"new Comandos9" + "\n" +
//			"dup" + "\n" +
//			"invokespecial Comandos9/<init>()V" + "\n" +
//			"aload_0" + "\n" +
//			code2 +
//			"return" + "\n" +
//			".end method";

		if (arquivo != null) {
			Collections.reverse(arquivo.getClasseVO().getMetodos());
			for (MetodoVO metodo : arquivo.getClasseVO().getMetodos()) {
				if (!metodo.isMain()) {
					String method = getMethod(metodo);
					code.append(method).append("\n");
				} else {
					main = metodo;
					mainCode = getMain(arquivo,main);
				}
			}
		}
		code.append(mainCode);
		return code.toString();
//		code.append("invokestatic ");
//		code.append(getFileName(arquivo));
//		code.append("/");
//		code.append("imprimir");
//		code.append("()V\n");
	}
	
	public static String getClasss(ArquivoVO arquivo) {
		return new StringBuilder()
			.append(getLine(".class public "+getClassName(arquivo), null))
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
	
	private static String getClassName(ArquivoVO arquivo) {
		return arquivo.getNomeArquivo().substring(
				arquivo.getNomeArquivo().lastIndexOf("/")+1, arquivo.getNomeArquivo().lastIndexOf("."));
	}
	
	private static String getMethod(MetodoVO metodo) {
		String parameters = getParameters(metodo);
		StringBuilder method = new StringBuilder()
			.append(getLine(".method public "+metodo.getNome()+"("+parameters+")"
				+metodo.getTipoRetornoMetodo().getAssembleInvokeType(), null))
			.append(getLine(".limit stack "+METHOD_LIMIT_STACK, null)).append(getLine(".limit locals "+METHOD_LIMIT_LOCALS, null));
		boolean first = true;
		for (MetodoVO subMetodo : metodo.getSubMetodos()) {
			if (first) {
				move(true);
				first = false;
			}
			method.append(getCmd(metodo, subMetodo));
		}
		method.append(getLine(metodo.getTipoRetornoMetodo().getAssembleReturnType()+"return", null))
		.append(getLine(".end method",false));
		return method.toString();
	}
	
	private static String getParameters(MetodoVO metodo) {
		StringBuilder parameters = new StringBuilder("");
		if (metodo.getParametros() != null && !metodo.getParametros().isEmpty()) { 
//			Collections.reverse(metodo.getParametros());
			for (VariavelVO v : metodo.getParametros()) {
				TokenVO tokenValue = getTokenValue(metodo, v);
				if (tokenValue != null) {
					TipoVariavelEnum tipoVariavel = TipoVariavelEnum.getTipoVariavel(tokenValue);
					parameters.append(tipoVariavel.getAssembleInvokeType());
				} else {
					System.out.println();
				}
			}
		}
		return parameters.toString();
	}
	
	public static String getCmd(MetodoVO metodoPai, MetodoVO metodo) {
		switch (metodo.getTipoComando()) {
			case WRITE:
				return getWrite(metodoPai, metodo, true);
			case WRITELN:
				return getWrite(metodoPai, metodo, false);
			case FUNCAO:
				return getFuncao(metodoPai, metodo);
			case ATRIBUI:
				return getAtribui(metodoPai, metodo);
			case IF:
				return getIf(metodoPai, metodo);
			case WHILE:
				return getWhile(metodoPai, metodo);
			default: return null; 
		}
	}
	
	private static String getWhile(MetodoVO metodoPai, MetodoVO metodo) {
		VariavelVO variavelVO = metodo.getParametros().get(0);
		System.out.println();
		return null;
	}

	private static String getIf(MetodoVO metodoPai, MetodoVO metodo) {
		StringBuilder ifCmd = new StringBuilder();
		VariavelVO variavelVO = metodo.getParametros().get(0);
//		ifCmd.append(loadFromStack(variavelVO, 0, true));
		ifCmd.append(putOnStack(metodo, variavelVO, null));
		boolean isElse = metodo.getSubMetodosFalse() != null && !metodo.getSubMetodosFalse().isEmpty();
//		ifCmd.append(getLine("ifeq "+(isElse ? "else" : ""), null));
		ifCmd.append(getLine("ifeq else", null));
		boolean first = true;
		for (MetodoVO subMetodo : metodo.getSubMetodos()) {
			if (first) {
				move(true);
				first = false;
			}
			String subCmd = getCmd(metodoPai, subMetodo);
			ifCmd.append(subCmd);
		}
//		ifCmd.append(getLine("goto endif", null));
		ifCmd.append(getLine("else:", false));
		if (isElse) {
			first = true;
			for (MetodoVO subMetodo : metodo.getSubMetodosFalse()) {
				if (first) {
					move(true);
					first = false;
				}
				String subCmd = getCmd(metodoPai, subMetodo);
				ifCmd.append(subCmd);
			}
		} else {
//			ifCmd.append(getLine("return", false));
		}
//		ifCmd.append(getLine("endif", false));
		return ifCmd.toString();
	}
//	String mainCode = ".method public static main([Ljava/lang/String;)V" + "\n" +
//	".limit stack 50" + "\n" +
//	".limit locals 50" + "\n" +
//	"new Comandos9" + "\n" +
//	"dup" + "\n" +
//	"invokespecial Comandos9/<init>()V" + "\n" +
//	
//	"ldc 0" + "\n" +
//	"ifeq else" + "\n" +
//	
//	"ldc 1" + "\n" +
//	"istore_0" + "\n" +
//	"getstatic java/lang/System/out Ljava/io/PrintStream;" + "\n" +
//	" iload_0" + "\n" +
//	" invokevirtual java/io/PrintStream/print(Z)V" + "\n" +
//	"return" + "\n" +
//	
//	"else:"+ "\n" +
//	
//	"ldc 0" + "\n" +
//	"istore_0" + "\n" +
//	"getstatic java/lang/System/out Ljava/io/PrintStream;" + "\n" +
//	" iload_0" + "\n" +
//	" invokevirtual java/io/PrintStream/print(Z)V" + "\n" +
//	"return" + "\n" +
//	
//	".end method";

	public static String getWrite(MetodoVO metodoPai, MetodoVO metodo, boolean isPrint) {
		if (metodo.getParametros() != null && !metodo.getParametros().isEmpty()) {
			if (metodo.getParametros().size() > 1) {
				//erro
			}
//			int index = 1;
			StringBuilder write = new StringBuilder();
//			Collections.reverse(metodo.getParametros());
			for (VariavelVO variavel : metodo.getParametros()) {
				if (variavel.getTipoVariavel().equals(TipoVariavelEnum.ID)) {
//					VariavelVO variavelMapa = AnalisadorSemantico.getVariavelMapa(metodo, variavel)(metodoPai, variavel);
//					variavel = variavelMapa;
					VariavelVO variavelStack = getOnStack(metodoPai, variavel);
					variavel = variavelStack;
					System.out.println();
				} //vindo 5 em vez de i
				int index = AnalisadorSemantico.getEnderecoStack(metodoPai, variavel.getNome()).getIndex();
//				VariavelVO onStack = getOnStack(metodoPai, variavel);
//				if (onStack == null) {
//					write.append(putOnStack(metodo, variavel, null)).append(storeFromStack(variavel, index, null));
//				} else {
//					write.append("");
//				}
				write
					.append(putOnStack(metodo, variavel, null))
					.append(storeFromStack(variavel, index, null))
					.append(getLine("getstatic java/lang/System/out Ljava/io/PrintStream;", null))
					.append(loadFromStack(variavel, index, null))
					.append(getLine("invokevirtual java/io/PrintStream/print"+(isPrint ? "" : "ln")+"("
						+variavel.getTipoVariavel().getAssembleInvokeType()+")"+
						metodo.getTipoRetornoMetodo().getAssembleInvokeType(), null))
//					.append(getLine("pop2",null))
					;
//				index++;
			}
			
			
			VariavelVO variavel = metodo.getParametros().get(0);
			
//			if (variavel.getTipoVariavel().equals(TipoVariavelEnum.ID)) {
//				VariavelVO variavelStack = AnalisadorSemantico.getVariavelStack(metodoPai, variavel);
//				variavel = variavelStack;
//			}
//			if (variavel.getTipoVariavel().equals(TipoVariavelEnum.ID)) {
//				VariavelVO variavelMapa = AnalisadorSemantico.getVariavelMapa(metodoPai, variavel);
//				variavel = variavelMapa;
//				System.out.println();
//			}
			return write.toString();
		}
		return ""; //erro?
	}
	
	private static String getFuncao(MetodoVO metodoPai, MetodoVO metodo) {
		StringBuilder funcao = new StringBuilder();
		if (!metodo.isMain()) {
			funcao.append(getClassInstance(arquivo));
		}
		String assemble = "";
		if (metodo.getParametros() != null && !metodo.getParametros().isEmpty()) {
//			Collections.reverse(metodo.getParametros());
//			int index = 3;
			for (VariavelVO parametro : metodo.getParametros()) {
				int index = AnalisadorSemantico.getEnderecoStack(metodoPai, parametro.getNome()).getIndex();
				assemble += parametro.getTipoVariavel().getAssembleInvokeType();
				funcao.append(putOnStack(metodo, parametro, null));
				funcao.append(storeFromStack(parametro, index, null));
				funcao.append(loadFromStack(parametro, index, null));
//				index++;
			}
		}
		StringBuilder line = new StringBuilder("invokevirtual ")
			.append(className).append(".").append(metodo.getNome()).append("(")
			.append(assemble).append(")").append(metodo.getTipoRetornoMetodo().getAssembleInvokeType());
		funcao.append(getLine(line.toString(), null));
		return funcao.toString();
	}
	
	private static String getAtribui(MetodoVO metodoPai, MetodoVO metodo) {
		VariavelVO v = metodo.getParametros().get(0); //tratar expressão
		int index = AnalisadorSemantico.getEnderecoStack(metodoPai, v.getNome()).getIndex();
		StringBuilder atribui = new StringBuilder()
//			.append(putOnStack(metodo, v, null))
			.append(assignToStack(metodo, v, index, null))
//			.append(storeFromStack(v, pos, null))
		;
//		Map<String, Map<String, VariavelVO>> mapVariaveis = AnalisadorSemantico.getMapaVariaveis();
//		mapVariaveis.get(metodoPai.getNome()).put(v.getNome(), v);
		putOnStackMap(metodoPai, v);

		return atribui.toString();
	}
	
	private static String getParametrosFuncao() {
		StringBuilder parametros = new StringBuilder();
		return parametros.toString();
	}
	
	private static String assignToStack(MetodoVO metodo, VariavelVO variavel, int pos, Boolean move) {
		StringBuilder cnst = new StringBuilder()
			.append(putOnStack(metodo, variavel, move)).append(storeFromStack(variavel, pos, null));
		return cnst.toString();
	}
	
//	private static String assignToStack2() {
//		StringBuilder stack = new StringBuilder()
//			.append("")
//			.append("");
//		cnst.append(variavel.getTipoVariavel().getAssembleType()).append("const_").append(pos);
//		.append(" ").append(variavel.getTokem().getValor())
//		return stack.toString();
//	}
	
	private static String putOnStack(MetodoVO metodo, VariavelVO variavel, Boolean move) {
		StringBuilder ldc = new StringBuilder();
		switch (variavel.getTipoVariavel()) {
			case DOUBLE:
				ldc.append("ldc2_w ");
				break;
			default:
				ldc.append("ldc ");
				break;
		}
		ldc.append(getValue(metodo, variavel));
		return getLine(ldc.toString(), move);
	}
	
	private static String storeFromStack(VariavelVO variavel, int pos, Boolean move) {
		return getLine(variavel.getTipoVariavel().getAssembleType() + "store_"+pos, move);
	}
	
	private static String loadFromStack(VariavelVO variavel, int pos, Boolean move) {
		return getLine(variavel.getTipoVariavel().getAssembleType()+"load_"+pos, null);
	}
	
	public static String getMain(ArquivoVO arquivo, MetodoVO metodoMain) {
		StringBuilder main = new StringBuilder();
		main.append(getLine(".method public static main([Ljava/lang/String;)V", null))
			.append(getLine(".limit stack "+METHOD_LIMIT_STACK, null))
			.append(getLine(".limit locals "+METHOD_LIMIT_LOCALS, null))
			.append(getClassInstance(arquivo))
			.append(getLine("aload_0", null))
			;
//			.append(getLine("bipush 100", null))
		for (MetodoVO subMetodo : metodoMain.getSubMetodos()) {
//			if (subMetodo.getTipoComando().equals(TipoComandoEnum.FUNCAO)) {
				move(true);
				main.append(getCmd(metodoMain,subMetodo));
//			}
		}
		main.append(getLine(metodoMain.getTipoRetornoMetodo().getAssembleReturnType()+"return", null))
			.append(getLine(".end method",false));
//		code.append("%object Comandos\n");
//		code.append("invokespecial ");
//		code.append(getLine("aload_0",true));
		return main.toString(); 
	}
	
	private static String getClassInstance(ArquivoVO arquivo) {
		return new StringBuilder(getLine("new "+getFileName(arquivo), null))
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
	
	private static String getValue(MetodoVO metodo, VariavelVO variavel) {
		String value = "";
		if (variavel != null && variavel.getTipoVariavel() != null) {
			switch (variavel.getTipoVariavel()) {
				case BOOL:
					if (variavel.getTokem().getValor().equals("true")) {
//						return "1";
						value = "1";
					} else {
//						return "0";
						value = "0";
					}
					break;
//					return variavel.getTokem().getValor().toLowerCase();
				case DOUBLE:
//					return variavel.getTokem().getValor();
					value = variavel.getTokem().getValor();
					break;
				case INTEGER:
//					return variavel.getTokem().getValor();
					value = variavel.getTokem().getValor();
					break;
				case STRING:
//					return variavel.getTokem().getValor();
					value = variavel.getTokem().getValor();
					break;
				case ID:
//					return getValue(metodo, getValorVariavelMapa(metodo, variavel));
					VariavelVO valorVariavelMapa = getValorVariavelMapa(metodo.getMetodoPai(), variavel);
					if (valorVariavelMapa.equals(variavel)) {
						return valorVariavelMapa.getTokem().getValor();
//						return null;
					} else {
						return getValue(metodo, valorVariavelMapa);
					}
//					VariavelVO valorVariavelMapa = getValorVariavelMapa(metodo, variavel);
//					return valorVariavelMapa.getTokem().getValor();
				default:
					break;
//					return null;
			}
		}
		if (variavel != null && variavel.getTokem() != null) {
			if (PalavraReservadaEnum.ID.equals(variavel.getTokem().getPalavraReservada())) {
				return getValue(metodo, getValorVariavelMapa(metodo, variavel));
			}
		}
		return value;
	}
	
	private static TokenVO getTokenValue(MetodoVO metodo, VariavelVO variavel) {
		TokenVO value = null;
		if (variavel != null && variavel.getTipoVariavel() != null) {
			switch (variavel.getTipoVariavel()) {
				case BOOL:
					value = new TokenVO();
					value.setPalavraReservada(PalavraReservadaEnum.BOOL);
					if (variavel.getTokem().getValor().equals("true")) {
						value.setValor("1");
					} else {
						value.setValor("0");
					}
					break;
				case DOUBLE:
					value = new TokenVO();
					value.setPalavraReservada(PalavraReservadaEnum.DOUBLE);
					value.setValor(variavel.getTokem().getValor());
					break;
				case INTEGER:
					value = new TokenVO();
					value.setPalavraReservada(PalavraReservadaEnum.INTEGER);
					value.setValor(variavel.getTokem().getValor());
					break;
				case STRING:
					value = new TokenVO();
					value.setPalavraReservada(PalavraReservadaEnum.STRING);
					value.setValor(variavel.getTokem().getValor());
					break;
				case ID:
					return getValorVariavelMapa(metodo, variavel).getTokem();
				default: break;
			}
		}
		if (variavel != null && variavel.getTokem() != null) {
			if (PalavraReservadaEnum.ID.equals(variavel.getTokem().getPalavraReservada())) {
				return getValorVariavelMapa(metodo, variavel).getTokem();
			}
		}
		return value;
	}
	
//	private static String getValue3(MetodoVO metodo, VariavelVO variavel) {
//		switch (variavel.getTokem().getPalavraReservada()) {
//			case BOOL:
//				if (variavel.getTokem().getValor().equals("true")) {
//					return "1";
//				} else {
//					return "0";
//				}
//			case DOUBLE:
//				return variavel.getTokem().getValor();
//			case INTEGER:
//				return variavel.getTokem().getValor();
//			case STRING:
//				return variavel.getTokem().getValor();
//			case ID:
//				return getValue3(metodo, getValorVariavelMapa(metodo, variavel));
//		default: return null;
//		}
//	}
	
	private static VariavelVO getValorVariavelMapa(MetodoVO metodo, VariavelVO variavel) {
//		VariavelVO variavelMapa = AnalisadorSemantico.getVariavelMapa(metodo, variavel);
		VariavelVO variavelStack = AnalisadorSemantico.getVariavelStack(metodo, variavel);
		return variavelStack;
	}
	
	private static String getFileName(ArquivoVO arquivo) {
		String fileName = arquivo.getLinhas().get(0).getTokens().get(1).getValor();
		return fileName;
	}
	
	private static String getFileName(String fullPath) {
		String fileName = fullPath.substring(fullPath.lastIndexOf("/")+1, fullPath.lastIndexOf("."));
		return fileName;
	}
	
//	String invokeMock = getInvokeMock(arquivo);
//	main.append(invokeMock);
	
//	private static String getInvokeMock(ArquivoVO arquivo) {
//		StringBuilder mock = new StringBuilder();
//		MetodoVO m = new MetodoVO();
//		m.setNome("imprimir");
//		TokenVO t = new TokenVO();
//		t.setPalavraReservada(PalavraReservadaEnum.INTEGER);
//		t.setValor("12");
//		t.setPalavraReservada(PalavraReservadaEnum.STRING);
//		t.setValor("\"Kakaka\"");
//		t.setPalavraReservada(PalavraReservadaEnum.DOUBLE);
//		t.setValor("654.92");
//		t.setPalavraReservada(PalavraReservadaEnum.BOOL);
//		t.setValor("false");
//		VariavelVO v = new VariavelVO();
//		v.setTipoVariavel(TipoVariavelEnum.getTipoVariavel(m, t));
//		v.setTokem(t);
//		v.setNome("i");
//		VariavelVO variavelMapa = AnalisadorSemantico.getVariavelMapa(m, v);
//		variavelMapa.setTokem(t);
//		m.setTipoRetornoMetodo(TipoRetornoMetodoEnum.getTipoRetornoMetodo(t));
//		mock.append(putOnStack(m, v, null));
//		mock.append(storeFromStack(v, 0, null));
//		mock.append(loadFromStack(v, 0, null));
//		mock.append(getLine("invokevirtual "+getFileName(arquivo)+"."+"imprimir("+
//		TipoVariavelEnum.getTipoVariavel(m, t).getAssembleInvokeType()+")V", null));
//		mock.append("ldc 1\n").append("istore_0\n").append("iload_0\n").append("ldc j\n").append("istore_1\n")
//			.append("iload_1\n");
//		return mock.toString();
//		ldc i
//	    istore_0
//	    iload_0
//	    ldc j
//	    istore_1
//	    iload_1
//	}
	
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
//		runAssemble(
	/*".class public Codigo 	.super java/lang/Object 		
	.method public static func(II)I 
	.limit stack 10 .limit locals 10 	
	  iload 1 
	  ldc 5
	  imul 
	  iload 0 
	  iadd 	
	  ireturn 		
	.end method 	
	.method public static main([Ljava/lang/String;)V 
	.limit stack 10 .limit locals 10 
	  ldc 5 
	  istore 0 
	  ldc 2 
	  istore 1 
	  ;println getstatic java/lang/System/out/
	  println Ljava/io/PrintStream; 
	  iload 0 
	  iload 1 
	  invokestatic Codigo/func(II)I 
	  invokevirtual java/io/PrintStream/println(I)V  
	return 		
	.end method");*/
	
	
//	}
//	id q tiver no class e passar pro j
//	ldc carrega na pilha

}
