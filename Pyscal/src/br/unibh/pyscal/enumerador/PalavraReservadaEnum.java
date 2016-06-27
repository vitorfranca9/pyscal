package br.unibh.pyscal.enumerador;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.unibh.pyscal.util.StringUtil;
import lombok.Getter;

public enum PalavraReservadaEnum {

	ABRE_PARENTESES("(", 3),
	FECHA_PARENTESES(")",4),
//	início de String
//	ASPAS("\""/*, isRegex("\"")*/),
//	final de declaração de função
	DOIS_PONTOS(":",4),
//	início da classe
	CLASS("class",2), //
	ID("/[^A-Za-z0-9]*/",1), //regex [a-zA-Z][a-zA-Z][a-zA-Z0-9] [^A-Za-z0-9]*
	DEF("def",1),
	DEFSTATIC("defstatic",1),
	END("end",1),
	PONTO(".",3),
	PONTO_VIRGULA(";",4),
	VIRGULA(",",4),
//	operadores
	AND("and",2),
	OR("or",2),
	MENOR("<",3),
	MAIOR(">",3),
	MENOR_IGUAL("<=",3),
	MAIOR_IGUAL(">=",3),
	IGUAL("=",1), //atribuições
	IGUAL_IGUAL("==",2), //fazer comparações
	DIFERENTE("!=",2),
	DIVIDIR("/",4),
	MULTIPLICAR("*",4),
	SOMAR("+",3),
	SUBTRAIR("-",3), //opUnario, tratar(n funfa como opunario)
	NOT("!",5), //opUnario
//	tipos primitivos, tipo retorno
	BOOL("bool",3),
	INTEGER("integer",3),
	STRING("String",3),
	DOUBLE("double",3),
	// indica array de tipos primitivos
	ABRE_COLCHETE("[",3),
	FECHA_COLCHETE("]",3),
//	tipo retorno
	VOID("void",3), 
//	comandos
	WRITE("write",3),
	WRITELN("writeln",3),
	IF("if",3),
	ELSE("else",3),
	WHILE("while",3),
//	função estática principal, vem por último
	MAIN("main",3), 
	VECTOR("vector",3), //?
//	operadores binarios
	TRUE("true",3), //será um "CONST_BOOL"
	FALSE("false",3), //será um "CONST_BOOL"
	CONSTINTEGER("ConstInteger",3),
	CONSTDOUBLE("ConstDouble",3),
	CONSTSTRING("ConstString",3),
	RETURN("return",2),
	COMENTARIO_LINHA("//",3),
	COMENTARIO_GERAL("/*",3);
	
	@Getter private final String regex;
	@Getter private final Integer ordem;
//	private TipoToken tipo;

	//TODO regex
	private PalavraReservadaEnum(String regex, Integer ordem) {
		this.regex = regex;
		this.ordem = ordem;
	}
	
	//HELPER
	public static class PalavraReservadaHelper {
		
		public static boolean isAbreParenteses(String valor) {
//			return isRegex(ABRE_PARENTESES.getRegex(), valor);
			return ABRE_PARENTESES.getRegex().equals(valor);
		}
		
		public static boolean isFechaParenteses(String valor) {
//			return isRegex(FECHA_PARENTESES.getRegex(), valor);
			return FECHA_PARENTESES.getRegex().equals(valor);
		}
		
		public static boolean isAspas(String valor) {
//			return isRegex("\\\"", valor);
			return "\"".equals(valor);
		}
		
		public static boolean isDoisPontos(String valor) {
//			return isRegex(DOIS_PONTOS.getRegex(), valor);
			return DOIS_PONTOS.getRegex().equals(valor);
		}
		
		public static boolean isClass(String valor) {
			return PalavraReservadaHelper.isRegex(CLASS.getRegex(), valor);
		}
		
		public static boolean isUnderline(char valor) {
			return '_' == valor;
		}
		
		public static boolean isID(String valor) {
			char[] valorArray = valor.toCharArray();
			boolean isID = true;
			for (int i = 0; i < valorArray.length; i++) {
				char valorNormalizado = StringUtil.normalizar(valorArray[i]);
				if (valorArray[i] == valorNormalizado) {
				
					if (i == 0) {
						if (!StringUtil.isLetra(valorArray[i])) {
							isID = false;
							break;
						}
					} else {
						if (!StringUtil.isLetraDigito(valorArray[i]) && !isUnderline(valorArray[i])) {
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
//			return isRegex(ID.getRegex(), valor);
		}
		
		public static boolean isDef(String valor) {
			return PalavraReservadaHelper.isRegex(DEF.getRegex(), valor);
		}
		
		public static boolean isDefStatic(String valor) {
			return PalavraReservadaHelper.isRegex(DEFSTATIC.getRegex(), valor);
		}
		
		public static boolean isEnd(String valor) {
			return PalavraReservadaHelper.isRegex(END.getRegex(), valor);
		}
		
		public static boolean isPonto(String valor) {
//			return isRegex(PONTO.getRegex(), valor);
			return PONTO.getRegex().equals(valor);
		}
		
		public static boolean isPontoVirgula(String valor) {
//			return isRegex(PONTO_VIRGULA.getRegex(), valor);
			return PONTO_VIRGULA.getRegex().equals(valor);
		}
		
		public static boolean isVirgula(String valor) {
//			return isRegex(PONTO_VIRGULA.getRegex(), valor);
			return VIRGULA.getRegex().equals(valor);
		}
		
		public static boolean isAnd(String valor) {
			return PalavraReservadaHelper.isRegex(AND.getRegex(), valor);
		}
		
		public static boolean isOr(String valor) {
			return PalavraReservadaHelper.isRegex(OR.getRegex(), valor);
		}
		
		public static boolean isMenor(String valor) {
//			return isRegex(MENOR.getRegex(), valor);
			return MENOR.getRegex().equals(valor);
		}
		
		public static boolean isMaior(String valor) {
//			return isRegex(MAIOR.getRegex(), valor);
			return MAIOR.getRegex().equals(valor);
		}
		
		public static boolean isMenorIgual(String valor) {
//			return isRegex(MENOR_IGUAL.getRegex(), valor);
			return MENOR_IGUAL.getRegex().equals(valor);
		}
		
		public static boolean isMaiorIgual(String valor) {
//			return isRegex(MAIOR_IGUAL.getRegex(), valor);
			return MAIOR_IGUAL.getRegex().equals(valor);
		}
		
		public static boolean isIgual(String valor) {
//			return isRegex(IGUAL.getRegex(), valor);
			return IGUAL.getRegex().equals(valor);
		}
		
		public static boolean isIgualIgual(String valor) {
//			return isRegex(IGUAL_IGUAL.getRegex(), valor);
			return IGUAL_IGUAL.getRegex().equals(valor);
		}
		
		public static boolean isDiferente(String valor) {
//			return isRegex(DIFERENTE.getRegex(), valor);
			return DIFERENTE.getRegex().equals(valor);
		}
		
		public static boolean isDividir(String valor) {
//			return isRegex(DIVIDIR.getRegex(), valor);
			return DIVIDIR.getRegex().equals(valor);
		}
		
		public static boolean isMultiplicar(String valor) {
//			return isRegex(MULTIPLICAR.getRegex(), valor);
			return MULTIPLICAR.getRegex().equals(valor);
		}
		
		public static boolean isSomar(String valor) {
//			return isRegex(SOMAR.getRegex(), valor);
			return SOMAR.getRegex().equals(valor);
		}
		
		public static boolean isSubtrair(String valor) {
//			return isRegex(SUBTRAIR.getRegex(), valor);
			return SUBTRAIR.getRegex().equals(valor);
		}
		
		public static boolean isNot(String valor) {
//			return isRegex(NOT.getRegex(), valor);
			return NOT.getRegex().equals(valor);
		}
		
		public static boolean isBool(String valor) {
			return PalavraReservadaHelper.isRegex(BOOL.getRegex(), valor);
		}
		
		public static boolean isInteger(String valor) {
			return PalavraReservadaHelper.isRegex(INTEGER.getRegex(), valor);
		}
		
		public static boolean isString(String valor) {
			return PalavraReservadaHelper.isRegex(STRING.getRegex(), valor);
		}
		
		public static boolean isDouble(String valor) {
			return PalavraReservadaHelper.isRegex(DOUBLE.getRegex(), valor);
		}
		
		public static boolean isAbreColchete(String valor) {
//			return isRegex(ABRE_COLCHETE.getRegex(), valor);
			return ABRE_COLCHETE.getRegex().equals(valor);
		}
		
		public static boolean isFechaColchete(String valor) {
//			return isRegex(FECHA_COLCHETE.getRegex(), valor);
			return FECHA_COLCHETE.getRegex().equals(valor);
		}
		
		public static boolean isVoid(String valor) {
			return PalavraReservadaHelper.isRegex(VOID.getRegex(), valor);
		}
		
		public static boolean isWrite(String valor) {
			return PalavraReservadaHelper.isRegex(WRITE.getRegex(), valor);
		}
		
		public static boolean isWriteln(String valor) {
			return PalavraReservadaHelper.isRegex(WRITELN.getRegex(), valor);
		}
		
		public static boolean isIf(String valor) {
			return PalavraReservadaHelper.isRegex(IF.getRegex(), valor);
		}
		
		public static boolean isElse(String valor) {
			return PalavraReservadaHelper.isRegex(ELSE.getRegex(), valor);
		}
		
		public static boolean isWhile(String valor) {
			return PalavraReservadaHelper.isRegex(WHILE.getRegex(), valor);
		}
		
		public static boolean isMain(String valor) {
			return PalavraReservadaHelper.isRegex(MAIN.getRegex(), valor);
		}
		
		public static boolean isVector(String valor) {
			return PalavraReservadaHelper.isRegex(VECTOR.getRegex(), valor);
		}
		
		public static boolean isTrue(String valor) {
			return PalavraReservadaHelper.isRegex(TRUE.getRegex(), valor);
		}
		
		public static boolean isFalse(String valor) {
			return PalavraReservadaHelper.isRegex(FALSE.getRegex(), valor);
		}
		
		public static boolean isConstInteger(String valor) {
//			return isRegex(CONSTINTEGER.getRegex(), valor);
			boolean isConstInteger = true;
			boolean primeiro = true;
			for (char c : valor.toCharArray()) {
				if (((primeiro && c != SUBTRAIR.getRegex().charAt(0)) && !StringUtil.isDigito(c)) 
						||
						(!primeiro && !StringUtil.isDigito(c))) {
					isConstInteger = false;
					break;
				}
				primeiro = false;
			}
			return isConstInteger;
		}
		
		public static boolean isConstString(String valor) {
			if (valor.length() > 1) {
				return valor.startsWith("\"") && valor.endsWith("\"");
			}
			return false;
		}

		public static boolean isConstDouble(String valor) {
			boolean valid = true;
			if (valor.contains(".")) {
				valid = valor.contains(".");
				String[] strs = valor.split("\\.");
				if (strs.length > 1) {
					for (String string : strs) {
						for (char c : string.toCharArray()) {
							if (!StringUtil.isDigito(c)) {
								valid = false;
								break;
							}
						}
					}
				} else {
					valid = false;
				}
			} else {
				valid = false;
			}
			return valid;
		}
		
		public static boolean isReturn(String valor) {
			return RETURN.getRegex().equals(valor);
		}

		public static boolean isComentarioGeral(String valor) {
			boolean valid = valor.startsWith("/*");
			return valid;
		}

		public static boolean isComentarioLinha(String valor) {
			boolean valid = valor.startsWith("//");
			return valid;
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
		
	}
	
}
