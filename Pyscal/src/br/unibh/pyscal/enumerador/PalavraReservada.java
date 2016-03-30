package br.unibh.pyscal.enumerador;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum PalavraReservada {

	ABRE_PARENTESES("("),
	FECHA_PARENTESES(")"),
//	início de String
//	ASPAS("\""/*, isRegex("\"")*/),
//	final de declaração de função
	DOIS_PONTOS(":"),
//	início da classe
	CLASS("class"), //
	ID("/[^A-Za-z0-9]*/"), //regex [a-zA-Z][a-zA-Z][a-zA-Z0-9] [^A-Za-z0-9]*
	DEF("def"),
	DEFSTATIC("defstatic"),
	END("end"),
	PONTO("."),
	PONTO_VIRGULA(";"),
//	operadores
	AND("and"),
	OR("or"),
	MENOR("<"),
	MAIOR(">"),
	MENOR_IGUAL("<="),
	MAIOR_IGUAL(">="),
	IGUAL("="), //atribuições
	IGUAL_IGUAL("=="), //fazer comparações
	DIFERENTE("!="),
	DIVIDIR("/"),
	MULTIPLICAR("*"),
	SOMAR("+"),
	SUBTRAIR("-"), //opUnario, tratar
	NOT("!"), //opUnario
//	tipos primitivos, tipo retorno
	BOOL("bool"),
	INTEGER("integer"),
	STRING("String"),
	DOUBLE("double"),
	// indica array de tipos primitivos
	ABRE_COLCHETE("["),
	FECHA_COLCHETE("]"),
//	tipo retorno
	VOID("void"), 
//	comandos
	WRITE("write"),
	WRITELN("writeln"),
	IF("if"),
	ELSE("else"),
	WHILE("while"),
//	função estática principal, vem por último
	MAIN("main"), 
	VECTOR("vector"), //?
//	operadores binarios
	TRUE("true"), //será um "CONST_BOOL"
	FALSE("false"), //será um "CONST_BOOL"
	CONSTINTEGER("ConstInteger"),
	CONSTDOUBLE("ConstDouble"),
	CONST_STRING("ConstString"),
	;
	
	private final String regex;
//	private TipoToken tipo;

	//TODO regex
	private PalavraReservada(String regex) {
		this.regex = regex;
	}
	
	public static boolean isAbreParenteses(String valor) {
//		return isRegex(ABRE_PARENTESES.getRegex(), valor);
		return ABRE_PARENTESES.getRegex().equals(valor);
	}
	
	public static boolean isFechaParenteses(String valor) {
//		return isRegex(FECHA_PARENTESES.getRegex(), valor);
		return FECHA_PARENTESES.getRegex().equals(valor);
	}
	
	public static boolean isAspas(String valor) {
//		return isRegex("\\\"", valor);
		return "\"".equals(valor);
	}
	
	public static boolean isDoisPontos(String valor) {
//		return isRegex(DOIS_PONTOS.getRegex(), valor);
		return DOIS_PONTOS.getRegex().equals(valor);
	}
	
	public static boolean isClass(String valor) {
		return isRegex(CLASS.getRegex(), valor);
	}
	
	public static boolean isUnderline(char valor) {
		return '_' == valor;
	}
	
	public static boolean isID(String valor) {
		char[] valorArray = valor.toCharArray();
		boolean isID = true;
		for (int i = 0; i < valorArray.length; i++) {
			char valorNormalizado = normalizar(valorArray[i]);
			if (valorArray[i] == valorNormalizado) {
			
				if (i == 0) {
					if (!isLetra(valorArray[i])) {
						isID = false;
						break;
					}
				} else {
					if (!isLetraDigito(valorArray[i]) && !isUnderline(valorArray[i])) {
						isID = false;
						break;
					}
				}
			
			} else {
				isID = false;
				break;
			}
		}
		return isID;
//		return isRegex(ID.getRegex(), valor);
	}
	
	public static boolean isDef(String valor) {
		return isRegex(DEF.getRegex(), valor);
	}
	
	public static boolean isDefStatic(String valor) {
		return isRegex(DEFSTATIC.getRegex(), valor);
	}
	
	public static boolean isEnd(String valor) {
		return isRegex(END.getRegex(), valor);
	}
	
	public static boolean isPonto(String valor) {
//		return isRegex(PONTO.getRegex(), valor);
		return PONTO.getRegex().equals(valor);
	}
	
	public static boolean isPontoVirgula(String valor) {
//		return isRegex(PONTO_VIRGULA.getRegex(), valor);
		return PONTO_VIRGULA.getRegex().equals(valor);
	}
	
	public static boolean isAnd(String valor) {
		return isRegex(AND.getRegex(), valor);
	}
	
	public static boolean isOr(String valor) {
		return isRegex(OR.getRegex(), valor);
	}
	
	public static boolean isMenor(String valor) {
//		return isRegex(MENOR.getRegex(), valor);
		return MENOR.getRegex().equals(valor);
	}
	
	public static boolean isMaior(String valor) {
//		return isRegex(MAIOR.getRegex(), valor);
		return MAIOR.getRegex().equals(valor);
	}
	
	public static boolean isMenorIgual(String valor) {
//		return isRegex(MENOR_IGUAL.getRegex(), valor);
		return MENOR_IGUAL.getRegex().equals(valor);
	}
	
	public static boolean isMaiorIgual(String valor) {
//		return isRegex(MAIOR_IGUAL.getRegex(), valor);
		return MAIOR_IGUAL.getRegex().equals(valor);
	}
	
	public static boolean isIgual(String valor) {
//		return isRegex(IGUAL.getRegex(), valor);
		return IGUAL.getRegex().equals(valor);
	}
	
	public static boolean isIgualIgual(String valor) {
//		return isRegex(IGUAL_IGUAL.getRegex(), valor);
		return IGUAL_IGUAL.getRegex().equals(valor);
	}
	
	public static boolean isDiferente(String valor) {
//		return isRegex(DIFERENTE.getRegex(), valor);
		return DIFERENTE.getRegex().equals(valor);
	}
	
	public static boolean isDividir(String valor) {
//		return isRegex(DIVIDIR.getRegex(), valor);
		return DIVIDIR.getRegex().equals(valor);
	}
	
	public static boolean isMultiplicar(String valor) {
//		return isRegex(MULTIPLICAR.getRegex(), valor);
		return MULTIPLICAR.getRegex().equals(valor);
	}
	
	public static boolean isSomar(String valor) {
//		return isRegex(SOMAR.getRegex(), valor);
		return SOMAR.getRegex().equals(valor);
	}
	
	public static boolean isSubtrair(String valor) {
//		return isRegex(SUBTRAIR.getRegex(), valor);
		return SUBTRAIR.getRegex().equals(valor);
	}
	
	public static boolean isNot(String valor) {
//		return isRegex(NOT.getRegex(), valor);
		return NOT.getRegex().equals(valor);
	}
	
	public static boolean isBool(String valor) {
		return isRegex(BOOL.getRegex(), valor);
	}
	
	public static boolean isInteger(String valor) {
		return isRegex(INTEGER.getRegex(), valor);
	}
	
	public static boolean isString(String valor) {
		return isRegex(STRING.getRegex(), valor);
	}
	
	public static boolean isDouble(String valor) {
		return isRegex(DOUBLE.getRegex(), valor);
	}
	
	public static boolean isAbreColchete(String valor) {
//		return isRegex(ABRE_COLCHETE.getRegex(), valor);
		return ABRE_COLCHETE.getRegex().equals(valor);
	}
	
	public static boolean isFechaColchete(String valor) {
//		return isRegex(FECHA_COLCHETE.getRegex(), valor);
		return FECHA_COLCHETE.getRegex().equals(valor);
	}
	
	public static boolean isVoid(String valor) {
		return isRegex(VOID.getRegex(), valor);
	}
	
	public static boolean isWrite(String valor) {
		return isRegex(WRITE.getRegex(), valor);
	}
	
	public static boolean isWriteln(String valor) {
		return isRegex(WRITELN.getRegex(), valor);
	}
	
	public static boolean isIf(String valor) {
		return isRegex(IF.getRegex(), valor);
	}
	
	public static boolean isElse(String valor) {
		return isRegex(ELSE.getRegex(), valor);
	}
	
	public static boolean isWhile(String valor) {
		return isRegex(WHILE.getRegex(), valor);
	}
	
	public static boolean isMain(String valor) {
		return isRegex(MAIN.getRegex(), valor);
	}
	
	public static boolean isVector(String valor) {
		return isRegex(VECTOR.getRegex(), valor);
	}
	
	public static boolean isTrue(String valor) {
		return isRegex(TRUE.getRegex(), valor);
	}
	
	public static boolean isFalse(String valor) {
		return isRegex(FALSE.getRegex(), valor);
	}
	
	public static boolean isConstInteger(String valor) {
//		return isRegex(CONSTINTEGER.getRegex(), valor);
		boolean isConstInteger = true;
		boolean primeiro = true;
		for (char c : valor.toCharArray()) {
			if ((primeiro && c != SUBTRAIR.getRegex().charAt(0)) && !isDigito(c)) {
				isConstInteger = false;
				primeiro = false;
				break;
			}
		}
		return isConstInteger;
	}
	
	//TODO Atribuir responsabilidade a uma classe utilitária
	public static boolean isLetra(char valor) {
		return Character.isLetter(valor);
	}
	public static boolean isDigito(char valor) {
		return Character.isDigit(valor);
	}
	public static boolean isLetraDigito(char valor) {
//		return Character.isLetter(valor) || Character.isDigit(valor);
		return Character.isLetterOrDigit(valor);
	}
	
	public static boolean isStringValue(String valor) {
		if (valor.length() > 1) {
			return valor.startsWith("\"") && valor.endsWith("\"");
		}
		return false;
	}
	
	public static char normalizar(char valor) {
		return normalizar(valor+"").charAt(0);
	}
	
	public static String normalizar(String valor) {
		String temp = Normalizer.normalize(valor, java.text.Normalizer.Form.NFD);
		return temp.replaceAll("[^\\p{ASCII}]","");
	}
	
	private static boolean isRegex(String regex, String valor) {
		if (valor != null && !valor.isEmpty()) {
			try {
				Pattern p = Pattern.compile(valor);
				Matcher m = p.matcher(regex);
				return m.matches();
			} catch (Exception e) {
//				e.printStackTrace();
//				System.err.println("Erro de pattern token "+valor);
			}
		}
		return false;
	}
	
	public String getRegex() {
		return regex;
	}

}
