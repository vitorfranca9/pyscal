package br.unibh.pyscal;

import java.util.ArrayList;
import java.util.List;
import static br.unibh.pyscal.PalavraReservada.*;

public class AnalisadorLexico {

	public List<Token> analisar(Arquivo arquivo) {
		List<Token> tokens = new ArrayList<>();
		
		for (Linha linha : arquivo.getLinhas()) {
			for (String palavra : linha.getPalavras()) {
				List<Token> tokensPalavra = analisarPalavra(palavra);
				linha.getTokens().addAll(tokensPalavra);
				tokens.addAll(tokensPalavra);
			}
		}
		
		return tokens;
	}
	
	private List<Token> analisarPalavra(String palavra) {
		List<Token> tokens = new ArrayList<>();
		char[] charArray = palavra.toCharArray();
		String tokenAtual = "";
		Token token = new Token();
		boolean abriuAspas = false;
		
		for (int i = 0; i < charArray.length; i++) {
			tokenAtual += charArray[i];
			
			if (i == 0 || tokenAtual.length() == 1) {
				//primeiro trata com 1 caractere
				if (isAspas(tokenAtual)) {
					token.setPalavraReservada(STRING);
					if (!abriuAspas) {
						abriuAspas = true;
					} else {
						tokenAtual = "";
						tokens.add(token);
						token = new Token();
						abriuAspas = false;
					}
				} else if (isAbreParenteses(tokenAtual)) {
					token.setPalavraReservada(ABRE_PARENTESES);
					token.setValor(tokenAtual);
					//melhorar saporra q todo mundo faz
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else if (isFechaParenteses(tokenAtual)) {
					token.setPalavraReservada(FECHA_PARENTESES);
					token.setValor(tokenAtual);
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else if (isDoisPontos(tokenAtual)) {
					token.setPalavraReservada(DOIS_PONTOS);
					token.setValor(tokenAtual);
					//melhorar saporra q todo mundo faz
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else if (isPonto(tokenAtual)) {
					token.setPalavraReservada(PONTO);
					token.setValor(tokenAtual);
					//melhorar saporra q todo mundo faz
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else if (isPontoVirgula(tokenAtual)) {
					token.setPalavraReservada(PONTO_VIRGULA);
					token.setValor(tokenAtual);
					//melhorar saporra q todo mundo faz
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else if (isMenor(tokenAtual)) {
					token.setPalavraReservada(MENOR);
					//pode vir a ser menorigual
				} else if (isMaior(tokenAtual)) {
					token.setPalavraReservada(MAIOR);
					//pode vir a ser menorigual
				} else if (isIgual(tokenAtual)) {
					token.setPalavraReservada(IGUAL);
					//pode vir a ser igualigual
				} else if (isDividir(tokenAtual)) {
					token.setPalavraReservada(DIVIDIR);
					token.setValor(tokenAtual);
					//TODO tratar, pode vir a ser comentário 
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else if (isMultiplicar(tokenAtual)) {
					token.setPalavraReservada(MULTIPLICAR);
					tokenAtual = "";
					tokens.add(token);
					token = new Token();
				} else if (isSomar(tokenAtual)) {
					token.setPalavraReservada(SOMAR);
					token.setValor(tokenAtual);
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else if (isSubtrair(tokenAtual)) {
					token.setPalavraReservada(SUBTRAIR);
					token.setValor(tokenAtual);
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else if (isNot(tokenAtual)) {
					token.setPalavraReservada(NOT);
					//pode vir a ser diferente
				} else if (isAbreColchete(tokenAtual)) {
					token.setPalavraReservada(ABRE_COLCHETE);
					token.setValor(tokenAtual);
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else if (isFechaColchete(tokenAtual)) {
					token.setPalavraReservada(FECHA_COLCHETE);
					token.setValor(tokenAtual);
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else if (isConstInteger(tokenAtual)) { 
					token.setPalavraReservada(CONSTINTEGER);
					//pode vir a ser constdouble
				} else {
					
					if (!isLetra(charArray[i])) {
						throw new AnaliseLexicaException("Uma palavra reservada ou ID deve começar com letra!");
					}
					
				}
			} else {
				
				if (token.getPalavraReservada() == null) {
					//começa a tratar com mais de 1 caractere e "não-ambíguas"
					//maioria pode vir a ser ID 
					if (isClass(tokenAtual)) {
						token.setPalavraReservada(CLASS);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isDef(tokenAtual)) {
						token.setPalavraReservada(DEF);
						//pode vir a ser defstatic
					} else if (isEnd(tokenAtual)) {
						token.setPalavraReservada(END);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isAnd(tokenAtual)) {
						token.setPalavraReservada(AND);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isOr(tokenAtual)) {
						token.setPalavraReservada(OR);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isBool(tokenAtual)) {
						token.setPalavraReservada(PalavraReservada.BOOL);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isInteger(tokenAtual)) {
						token.setPalavraReservada(PalavraReservada.INTEGER);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isString(tokenAtual)) {
						token.setPalavraReservada(STRING);
						tokenAtual = "";
						tokens.add(token);
						token = new Token();
					} else if (isDouble(tokenAtual)) {
						token.setPalavraReservada(DOUBLE);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isVoid(tokenAtual)) {
						token.setPalavraReservada(VOID);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isWrite(tokenAtual)) {
						token.setPalavraReservada(WRITE);
						//pode ser writeln tb
	//					tokenAtual = ""; ou dígito
	//					tokens.add(token);
	//					token = new Token();
					} else if (isIf(tokenAtual)) {
						token.setPalavraReservada(IF);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isWhile(tokenAtual)) {
						token.setPalavraReservada(WHILE);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isMain(tokenAtual)) {
						token.setPalavraReservada(MAIN);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isVector(tokenAtual)) {
						token.setPalavraReservada(VECTOR);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isTrue(tokenAtual)) {
						token.setPalavraReservada(TRUE);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else if (isFalse(tokenAtual)) {
						token.setPalavraReservada(FALSE);
//						tokenAtual = "";
//						tokens.add(token);
//						token = new Token();
					} else {
						
						if (!podeSerID(tokenAtual)) {
							
							tokenAtual = tokenAtual.substring(0,tokenAtual.length()-1);
							
							if (podeSerID(tokenAtual)) {
								token.setValor(tokenAtual);
								token.setPalavraReservada(ID);
								tokens.add(token);
								token = new Token();
								tokenAtual = "";
								i--;
							} else {
								throw new AnaliseLexicaException("Um ID deve ter apenas letras e dígitos!");
							}
							
//							throw new AnaliseLexicaException("Um ID deve começar com 2 letras!");
						} else {
//							token.setPalavraReservada(ID);
						}
						
					}
				
				} else {
					//começa a tratar com mais de 1 caractere e "ambíguas"
					//tokens que podem vir a ser ID
					//provaveis: defstatic, menorigual, maiorigual, igualigual, diferente, writeln
					
					if (MENOR.equals(token.getPalavraReservada())) {
						if (isMenorIgual(tokenAtual)) {
							token.setPalavraReservada(MENOR_IGUAL);
							token.setValor(tokenAtual);
							tokens.add(token);
							tokenAtual = "";
							token = new Token();
						} else {
							//processar primeira letra de novo, pode vir a ser já outro comando(eita)
							token.setValor(tokenAtual.substring(0, tokenAtual.length()-1));
							tokens.add(token);
							tokenAtual = "";
							i--;
						}
					} else if (MAIOR.equals(token.getPalavraReservada())) {
						if (isMaiorIgual(tokenAtual)) {
							token.setPalavraReservada(MAIOR_IGUAL);
							token.setValor(tokenAtual);
							tokens.add(token);
							tokenAtual = "";
							token = new Token();
						} else {
							//processar primeira letra de novo, pode vir a ser já outro comando(eita)
							token.setValor(tokenAtual.substring(0, tokenAtual.length()-1));
							tokens.add(token);
							token = new Token();
							tokenAtual = "";
							i--;
						}
					} else if (IGUAL.equals(token.getPalavraReservada())) {
						if (isIgualIgual(tokenAtual)) {
							token.setPalavraReservada(IGUAL_IGUAL);
							token.setValor(tokenAtual);
							tokens.add(token);
							tokenAtual = "";
							token = new Token();
						} else {
							//processar primeira letra de novo, pode vir a ser já outro comando(eita)
							token.setValor(tokenAtual.substring(0, tokenAtual.length()-1));
							tokens.add(token);
							token = new Token();
							tokenAtual = "";
							i--;
						}
					} else if (NOT.equals(token.getPalavraReservada())) {
						if (isDiferente(tokenAtual)) {
							token.setPalavraReservada(DIFERENTE);
							token.setValor(tokenAtual);
							tokens.add(token);
							tokenAtual = "";
							token = new Token();
						} else {
							//processar primeira letra de novo, pode vir a ser já outro comando(eita)
							token.setValor(tokenAtual.substring(0, tokenAtual.length()-1));
							tokens.add(token);
							token = new Token();
							tokenAtual = "";							
							i--;
						}
					} else if (WRITE.equals(token.getPalavraReservada())) {
						if (isWriteln(tokenAtual)) {
							token.setPalavraReservada(WRITELN);
						} else if (!(WRITELN.getRegex().startsWith(tokenAtual))) {
							String ultimaLetra = tokenAtual.substring(tokenAtual.length()-1, tokenAtual.length());
							if (isAbreParenteses(ultimaLetra)) { 
								token.setPalavraReservada(WRITE);
								token.setValor(tokenAtual.substring(0, tokenAtual.length()-1));
								tokens.add(token);
								token = new Token();
								tokenAtual = "";
								i--;								
							} else if (isID(tokenAtual)) {
								token.setPalavraReservada(ID);
							} else { // se não é ID vai toma no cú(hue)
								throw new AnaliseLexicaException("Um ID deve começar com 2 letras a partir daí deve possuir apenas letras ou dígitos!");
							}
						}
					} else if (DEF.equals(token.getPalavraReservada())) {
						if (isDefStatic(tokenAtual)) {
							token.setPalavraReservada(DEFSTATIC);
						} else if (!(DEFSTATIC.getRegex().startsWith(tokenAtual))) {
							if (isID(tokenAtual)) {
								token.setPalavraReservada(ID);
							} else {
								throw new AnaliseLexicaException("Um ID deve começar com 2 letras a partir daí deve possuir apenas letras ou dígitos!");
							}
						}
					} else if (CONSTINTEGER.equals(token.getPalavraReservada())) {  
						if (!isConstInteger(tokenAtual)) {
							if (isPonto(tokenAtual.substring(0,tokenAtual.length()-1))) {
								token.setPalavraReservada(CONSTDOUBLE);
							} else {
								//ver se rola mais comando no proximo
								token.setValor(tokenAtual.substring(0, tokenAtual.length()-1));
								tokens.add(token);
								token = new Token();
								tokenAtual = "";
								i--;
//								throw new AnaliseLexicaException("Um constante inteiro deve conter apenas dígitos!");
							}
						}
					} else {
						// só pode ser ID nessaporra
						if (!podeSerID(tokenAtual)) {
//							throw new AnaliseLexicaException("Um ID deve começar com 2 letras a partir daí deve possuir apenas letras ou dígitos!");
							//ver se pode o ultimo pode vir a ser um novo comando
							token.setValor(tokenAtual.substring(0,tokenAtual.length()-1));
							tokens.add(token);
							token = new Token();
							tokenAtual = "";
							i--;
						} 
					}
					
				}
			
//				if (/*(i == 0 && isAspas(tokenAtual)) || */!abriuAspas) {
//					throw new AnaliseLexicaException("Caractere não reconhecido");
//				}
//				tokenAtual += charArray[i];
				
			}
			
		}
		
		if (token.getPalavraReservada() != null && !tokenAtual.isEmpty()) {
			token.setValor(tokenAtual);
			tokens.add(token);
			tokenAtual = "";
			token = new Token();
		} else {
			if (token.getPalavraReservada() == null && !tokenAtual.isEmpty()){
				if (isID(tokenAtual)) {
					token.setPalavraReservada(ID);
					token.setValor(tokenAtual);
					tokens.add(token);
					tokenAtual = "";
					token = new Token();
				} else {
					//perai q deu merda
					System.out.println("VAI DAR(DEU) MERDA NO TOKEN "+tokenAtual);
				}
			}
		}
		
		/*if (token.getPalavraReservada() == null) {
			throw new AnaliseLexicaException("Nenhuma palavra reservada encontrada");
		}else */
		if (abriuAspas) {
			throw new AnaliseLexicaException("Abriu aspas, esperava fechar ao final da palavra");
		}
		
		return tokens;
	}
	
	//fazer esse trem funfar
	private void reiniciarValores(Token token, String tokenAtual) {
//		token = new Token();
//		tokenAtual = "";
		token.setPalavraReservada(null);
		token.setTipoToken(null);
		tokenAtual.replaceAll(".","");
		System.out.println();
	}
	
	private List<PalavraReservada> retornaPalavrasPossiveis(String tokenAtual, List<PalavraReservada> palavrasDisponiveis) {
		List<PalavraReservada> palavrasPossiveis = new ArrayList<>();
		
		for (PalavraReservada palavraReservada : palavrasDisponiveis) {
			if (palavraReservada.getRegex().startsWith(tokenAtual)) {
				palavrasPossiveis.add(palavraReservada);
			}
		}
		
		if (podeSerID(tokenAtual)) {
			palavrasPossiveis.add(ID);
		}
		return palavrasPossiveis;
	}
	
	public boolean podeSerID(String tokenAtual) {
		if (tokenAtual.length() == 1) {
			return isLetra(tokenAtual.charAt(0));
		} else if (tokenAtual.length() == 2) {
			return isLetra(tokenAtual.charAt(0)) &&
				isLetra(tokenAtual.charAt(1));
		} else {
			return isID(tokenAtual);
		}
		
	}

}
