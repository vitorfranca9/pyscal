package br.unibh.pyscal.analisador;

import static br.unibh.pyscal.enumerador.PalavraReservada.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.unibh.pyscal.enumerador.PalavraReservada;
import br.unibh.pyscal.exception.AnaliseLexicaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;

public class AnalisadorLexico {
	
	public void analisar(ArquivoVO arquivo) throws AnaliseLexicaException {
		for (LinhaVO linha : arquivo.getLinhas()) {
			for (String palavra : linha.getPalavras()) {
				List<TokenVO> tokensPalavra = analisarPalavra(linha, palavra);
				adicionarTokens(linha, tokensPalavra);
			}
		}
	}
	
	private void adicionarTokens(LinhaVO linha, List<TokenVO> tokensPalavra) {
		TokenVO ultimoTokenAtual = tokensPalavra.get(tokensPalavra.size()-1);
		
		if (STRING.equals(ultimoTokenAtual.getPalavraReservada())) {
			if (!linha.getTokens().isEmpty()) {
				TokenVO ultimoTokenLinha = getUltimoTokenString(linha.getTokens());
				if (ultimoTokenAtual != ultimoTokenLinha ) {
					linha.getTokens().addAll(tokensPalavra);
				}
			} else {
				linha.getTokens().addAll(tokensPalavra);
			}
			
		} else {
			for (TokenVO tokenVO : tokensPalavra) {
				if (STRING.equals(tokenVO.getPalavraReservada())) {
					TokenVO ultimoTokenLinha = getUltimoTokenString(linha.getTokens());
					if (tokenVO != ultimoTokenLinha) {
						linha.getTokens().add(tokenVO);
					}
				} else {
					linha.getTokens().add(tokenVO);
				}
			}
		}
	}
	
	private TokenVO getUltimoTokenString(List<TokenVO> tokens) {
		TokenVO tokenString = null;
		for(int i = tokens.size()-1; i >= 0; i--) {
			if (STRING.equals(tokens.get(i).getPalavraReservada())){
				tokenString = tokens.get(i);
				break;
			}
		}
		return tokenString;
	}
	
	private List<TokenVO> analisarPalavra(LinhaVO linha, String palavra) throws AnaliseLexicaException {
		List<TokenVO> tokens = new ArrayList<>();
		char[] charArray = palavra.toCharArray();
		String tokenAtual = "";
		TokenVO token = new TokenVO();
		boolean abriuAspas = false;
		
		for (int i = 0; i < charArray.length; i++) {
			tokenAtual += charArray[i];
			
			if (linha.getTokens().size() > 0 && !abriuAspas) {
				TokenVO ultimoToken = linha.getTokens().get(linha.getTokens().size()-1);
				if (STRING.equals(ultimoToken.getPalavraReservada()) 
						&& !isAspas(ultimoToken.getValor().substring(
								ultimoToken.getValor().length()-1, ultimoToken.getValor().length()))) {
					token = ultimoToken;
					token.setValor(token.getValor()+" ");
					abriuAspas = true;
				}
			}
			
			if (token.getPalavraReservada() != null && 
					STRING.equals(token.getPalavraReservada())) {
				String ultimaLetra = tokenAtual.substring(tokenAtual.length()-1,tokenAtual.length());
				if (isAspas(ultimaLetra)) { //fecha ConstString
					token.setValor(token.getValor()+tokenAtual);
					tokens.add(token);
					token = new TokenVO();
					tokenAtual = "";
					abriuAspas = false;
				} 
			//TODO tratar comentário	
			} else {
				
				//primeiro trata com 1 caractere
				if (i == 0 || tokenAtual.length() == 1) {
					
					if (isAspas(tokenAtual)) {
						token.setPalavraReservada(STRING);
						if (!abriuAspas) {
							abriuAspas = true;
						} else {
							tokenAtual = "";
							tokens.add(token);
							token = new TokenVO();
							abriuAspas = false;
						}
					} else if (isAbreParenteses(tokenAtual)) {
						token.setPalavraReservada(ABRE_PARENTESES);
						token.setValor(tokenAtual);
						tokens.add(token);
						tokenAtual = "";
						token = new TokenVO();
					} else if (isFechaParenteses(tokenAtual)) {
						token.setPalavraReservada(FECHA_PARENTESES);
						token.setValor(tokenAtual);
						tokens.add(token);
						tokenAtual = "";
						token = new TokenVO();
					} else if (isDoisPontos(tokenAtual)) {
						token.setPalavraReservada(DOIS_PONTOS);
						token.setValor(tokenAtual);
						tokens.add(token);
						tokenAtual = "";
						token = new TokenVO();
					} else if (isPonto(tokenAtual)) {
						token.setPalavraReservada(PONTO);
						token.setValor(tokenAtual);
						tokens.add(token);
						tokenAtual = "";
						token = new TokenVO();
					} else if (isPontoVirgula(tokenAtual)) {
						token.setPalavraReservada(PONTO_VIRGULA);
						token.setValor(tokenAtual);
						tokens.add(token);
						tokenAtual = "";
						token = new TokenVO();
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
						token = new TokenVO();
					} else if (isMultiplicar(tokenAtual)) {
						token.setPalavraReservada(MULTIPLICAR);
						tokens.add(token);
						tokenAtual = "";
						token = new TokenVO();
					} else if (isSomar(tokenAtual)) {
						token.setPalavraReservada(SOMAR);
						token.setValor(tokenAtual);
						tokens.add(token);
						tokenAtual = "";
						token = new TokenVO();
					} else if (isSubtrair(tokenAtual)) {
						//pode vir a ser número negativo
						token.setPalavraReservada(SUBTRAIR);
					} else if (isNot(tokenAtual)) {
						token.setPalavraReservada(NOT);
						//pode vir a ser diferente
					} else if (isAbreColchete(tokenAtual)) {
						token.setPalavraReservada(ABRE_COLCHETE);
						token.setValor(tokenAtual);
						tokens.add(token);
						tokenAtual = "";
						token = new TokenVO();
					} else if (isFechaColchete(tokenAtual)) {
						token.setPalavraReservada(FECHA_COLCHETE);
						token.setValor(tokenAtual);
						tokens.add(token);
						tokenAtual = "";
						token = new TokenVO();
					} else if (isConstInteger(tokenAtual)) { 
						token.setPalavraReservada(CONSTINTEGER);
						//pode vir a ser constdouble
					} else {
						
						if (!isLetra(charArray[i])) {
							throw new AnaliseLexicaException("Uma palavra reservada ou ID deve começar com letra!",linha,palavra);
						}
						
					}
				} else {
					
					if (token.getPalavraReservada() == null) {
						//começa a tratar com mais de 1 caractere e "não-ambíguas"
						//pode vir a ser ID 
						if (isClass(tokenAtual)) {
							token.setPalavraReservada(CLASS);
						} else if (isDef(tokenAtual)) {
							token.setPalavraReservada(DEF);
							//pode vir a ser defstatic
						} else if (isEnd(tokenAtual)) {
							token.setPalavraReservada(END);
						} else if (isAnd(tokenAtual)) {
							token.setPalavraReservada(AND);
						} else if (isOr(tokenAtual)) {
							token.setPalavraReservada(OR);
						} else if (isBool(tokenAtual)) {
							token.setPalavraReservada(PalavraReservada.BOOL);
						} else if (isInteger(tokenAtual)) {
							token.setPalavraReservada(PalavraReservada.INTEGER);
						} else if (isString(tokenAtual)) {
							token.setPalavraReservada(STRING);
							tokenAtual = "";
							tokens.add(token);
							token = new TokenVO();
						} else if (isDouble(tokenAtual)) {
							token.setPalavraReservada(DOUBLE);
						} else if (isVoid(tokenAtual)) {
							token.setPalavraReservada(VOID);
						} else if (isWrite(tokenAtual)) {
							token.setPalavraReservada(WRITE);
							//pode ser writeln tb
						} else if (isIf(tokenAtual)) {
							token.setPalavraReservada(IF);
						} else if (isElse(tokenAtual)) {
							token.setPalavraReservada(ELSE);
						} else if (isWhile(tokenAtual)) {
							token.setPalavraReservada(WHILE);
						} else if (isMain(tokenAtual)) {
							token.setPalavraReservada(MAIN);
						} else if (isVector(tokenAtual)) {
							token.setPalavraReservada(VECTOR);
						} else if (isTrue(tokenAtual)) {
							token.setPalavraReservada(TRUE);
						} else if (isFalse(tokenAtual)) {
							token.setPalavraReservada(FALSE);
						} else {
							
							if (!podeSerID(tokenAtual)) {
								
								String ultimaLetra = tokenAtual.substring(tokenAtual.length()-1,tokenAtual.length());
								tokenAtual = tokenAtual.substring(0,tokenAtual.length()-1);
								
								if (podeSerID(tokenAtual) && podeSerProximaExpressao(ultimaLetra)) {
									token.setValor(tokenAtual);
									token.setPalavraReservada(ID);
									tokens.add(token);
									token = new TokenVO();
									tokenAtual = "";
									i--;
								} else {
									throw new AnaliseLexicaException(linha,palavra);
								}
								
							}
							
						}
					
					} else {
						//começa a tratar com mais de 1 caractere e "ambíguas"
						//provaveis: defstatic, menorigual, maiorigual, igualigual, diferente, writeln
						//tokens que podem vir a ser ID
						
						if (MENOR.equals(token.getPalavraReservada())) {
							if (isMenorIgual(tokenAtual)) {
								token.setPalavraReservada(MENOR_IGUAL);
								token.setValor(tokenAtual);
								tokens.add(token);
								tokenAtual = "";
								token = new TokenVO();
							} else {
								//processar primeira letra de novo, pode vir a ser já outro comando
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
								token = new TokenVO();
							} else {
								//processar primeira letra de novo, pode vir a ser já outro comando
								token.setValor(tokenAtual.substring(0, tokenAtual.length()-1));
								tokens.add(token);
								token = new TokenVO();
								tokenAtual = "";
								i--;
							}
						} else if (IGUAL.equals(token.getPalavraReservada())) {
							if (isIgualIgual(tokenAtual)) {
								token.setPalavraReservada(IGUAL_IGUAL);
								token.setValor(tokenAtual);
								tokens.add(token);
								tokenAtual = "";
								token = new TokenVO();
							} else {
								//processar primeira letra de novo, pode vir a ser já outro comando
								token.setValor(tokenAtual.substring(0, tokenAtual.length()-1));
								tokens.add(token);
								token = new TokenVO();
								tokenAtual = "";
								i--;
							}
						} else if (NOT.equals(token.getPalavraReservada())) {
							if (isDiferente(tokenAtual)) {
								token.setPalavraReservada(DIFERENTE);
								token.setValor(tokenAtual);
								tokens.add(token);
								tokenAtual = "";
								token = new TokenVO();
							} else {
								//processar primeira letra de novo, pode vir a ser já outro comando
								token.setValor(tokenAtual.substring(0, tokenAtual.length()-1));
								tokens.add(token);
								token = new TokenVO();
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
									token = new TokenVO();
									tokenAtual = "";
									i--;								
								} else if (isID(tokenAtual)) {
									token.setPalavraReservada(ID);
								} else { // se não é ID vai toma no cú(hue)
									throw new AnaliseLexicaException(
										"Um ID deve começar com letra e a partir daí deve possuir apenas letras, dígitos ou underline!",linha,palavra);
								}
							}
						} else if (DEF.equals(token.getPalavraReservada())) {
							if (isDefStatic(tokenAtual)) {
								token.setPalavraReservada(DEFSTATIC);
							} else if (!(DEFSTATIC.getRegex().startsWith(tokenAtual))) {
								if (isID(tokenAtual)) {
									token.setPalavraReservada(ID);
								} else {
									throw new AnaliseLexicaException(
										"Um ID deve começar com letra e a partir daí deve possuir apenas letras, dígitos ou underline!",linha,palavra);
								}
							}
						} else if (CONSTINTEGER.equals(token.getPalavraReservada())) {  
							if (!isConstInteger(tokenAtual)) {
								if (isPonto(tokenAtual.substring(tokenAtual.length()-1,tokenAtual.length()))) {
									token.setPalavraReservada(CONSTDOUBLE);
								} else {
									//ver se rola mais comando no proximo
									token.setValor(tokenAtual.substring(0, tokenAtual.length()-1));
									tokens.add(token);
									token = new TokenVO();
									tokenAtual = "";
									i--;
								}
							}
						} else if (CONSTDOUBLE.equals(token.getPalavraReservada())) { 
							if (!isDigito(tokenAtual.charAt(tokenAtual.length()-1))) {
								token.setValor(tokenAtual.substring(0, tokenAtual.length()-1)+"0");
								tokens.add(token);
								token = new TokenVO();
								tokenAtual = "";
								i--;
							}
						} else if (SUBTRAIR.equals(token.getPalavraReservada())) { 
							if (isDigito(tokenAtual.charAt(tokenAtual.length()-1))) {
								token.setPalavraReservada(PalavraReservada.CONSTINTEGER);
							}
						} else {
							// só pode ser ID
							if (!abriuAspas && !podeSerID(tokenAtual)) {
								//ver se pode o ultimo pode vir a ser um novo comando
								if (podeSerProximaExpressao(tokenAtual.substring(tokenAtual.length()-1,tokenAtual.length()))) {
									token.setValor(tokenAtual.substring(0,tokenAtual.length()-1));
									tokens.add(token);
									token = new TokenVO();
									tokenAtual = "";
									i--;
								} else {
									throw new AnaliseLexicaException(
										"Um ID deve começar com letra e a partir daí deve possuir apenas letras, dígitos ou underline!",linha,palavra);
								}
								
							} 
							
						}
						
					}
				
				}
			
			}
			
		}
		
		if (token.getPalavraReservada() != null && !tokenAtual.isEmpty()) {
			token.setValor(token.getValor()+tokenAtual);
			tokenAtual = "";
			tokens.add(token);
			token = new TokenVO();
		} else {
			if (token.getPalavraReservada() == null && !tokenAtual.isEmpty()){
				if (isID(tokenAtual)) {
					token.setPalavraReservada(ID);
					token.setValor(tokenAtual);
					tokens.add(token);
					tokenAtual = "";
					token = new TokenVO();
				} else {
					//perai q deu merda
					System.out.println("VAI DAR(DEU) MERDA NO TOKEN "+tokenAtual);
				}
			}
		}
		
		if (abriuAspas && linha.getPalavras().get(linha.getPalavras().size()-1).equals(palavra)) {
			throw new AnaliseLexicaException("Abriu aspas, esperava fechar ao final da expressão",linha,palavra);
		}
		return tokens;
	}
	
	@SuppressWarnings("unused")
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
		} else {
			return isID(tokenAtual);
		}
	}
	
	public boolean podeSerProximaExpressao(String tokenAtual) {
		return tokensDeUmCharactere.contains(tokenAtual);
	}
	
	private static final List<String> tokensDeUmCharactere = Arrays.asList(new String[]{
		ABRE_PARENTESES.getRegex(),
		FECHA_PARENTESES.getRegex(),
		DOIS_PONTOS.getRegex(),
		PONTO.getRegex(),
		PONTO_VIRGULA.getRegex(),
		MENOR.getRegex(),
		MAIOR.getRegex(),
		IGUAL.getRegex(),
		NOT.getRegex(),
		DIVIDIR.getRegex(),
		MULTIPLICAR.getRegex(),
		SOMAR.getRegex(),
		SUBTRAIR.getRegex(),
		ABRE_COLCHETE.getRegex(),
		FECHA_COLCHETE.getRegex()
	});

}
