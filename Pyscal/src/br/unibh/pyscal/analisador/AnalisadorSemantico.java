package br.unibh.pyscal.analisador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.unibh.pyscal.enumerador.EscopoVariavel;
import br.unibh.pyscal.enumerador.PalavraReservada;
import br.unibh.pyscal.enumerador.TipoRetornoMetodo;
import br.unibh.pyscal.enumerador.TipoVariavel;
import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.MetodoVO;
import br.unibh.pyscal.vo.TokenVO;
import br.unibh.pyscal.vo.VariavelVO;

public class AnalisadorSemantico extends AnalisadorAbstrato {

//	private ClasseVO classe;
	private Map<String, Map<String, VariavelVO>> mapaVariaveis = new HashMap<>();
	private SintaticoHelper sintaticoHelper;
	private SemanticoHelper semanticoHelper;
	
	@Override
	protected void init() {
		super.init();
//		classe = new ClasseVO();
		sintaticoHelper = SintaticoHelper.getInstancia();
		semanticoHelper = SemanticoHelper.getInstancia();
		mapaVariaveis = new HashMap<>();
	}
	
	@Override
	public void analisar(ArquivoVO arquivo) throws Exception {
		init();
		ArquivoVO arquivoExecutavel = sintaticoHelper.getArquivoExecutavel(arquivo);
		this.arquivo = arquivoExecutavel;
		compilar();
	}
	
//	@SuppressWarnings("unused")
	private void compilar() throws AnaliseSemanticaException {
		boolean isMetodo = false;
		boolean isComando = false;
		List<MetodoVO> metodos = new ArrayList<>();
		MetodoVO metodoVO = new MetodoVO();
		
		while (numLinhaAtual < arquivo.getLinhas().size()) {
			TokenVO tokenAtual = getTokenAtual();

			if (sintaticoHelper.isPalavraReservadaClassSemErro(getTokenAtual().getPalavraReservada())) { //erro é sintático
				contarProximaLinha();
			} else {
				
				if (isMetodo) {
					
					if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(getTokenAtual().getPalavraReservada())) {
						// declaração de variáveis

						TokenVO token = getToken(3);
						VariavelVO variavelVO = new VariavelVO();
						if (sintaticoHelper.isPalavraReservadaPontoVirgulaSemErro(token.getPalavraReservada())) {
							variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(tokenAtual.getPalavraReservada(),getLinhaAtual(),tokenAtual));
							
							TokenVO id = getToken(2);
							variavelVO.setNome(id.getValor());
							variavelVO.setLinha(getLinhaAtual());
							adicionarVariavelMapa(metodoVO, variavelVO);
							variavelVO = new VariavelVO();
							contarProximaLinha();
							
						} else if (sintaticoHelper.isPalavraReservadaIgualSemErro(token.getPalavraReservada())) {
							//EH COMANDO DE ATRIBUIÇÃO
							//validar void
							isComando = true;
							continue;
						} else {
							//ERRO?
						}
						
					} else if (sintaticoHelper.isPalavraReservadaIDSemErro(getTokenAtual().getPalavraReservada())) {
						// pode ser atribuição ou função
						System.out.println();
					} else if (sintaticoHelper.isPalavraReservadaCmdSemErro(getTokenAtual().getPalavraReservada())) {
						// comandos
						System.out.println();
					} else if (sintaticoHelper.isPalavraReservadaReturnSemErro(getTokenAtual().getPalavraReservada())) {
						// retorno do método
						tratarRetorno(metodoVO);
						System.out.println();
					} else if (sintaticoHelper.isPalavraReservadaEndSemErro(getTokenAtual().getPalavraReservada())) {
						metodoVO.setVariaveis(new ArrayList<>(mapaVariaveis.get(metodoVO.getNome()).values()));
						metodos.add(metodoVO);
						metodoVO = new MetodoVO();
						isMetodo = false;
						isComando = false;
						contarProximaLinha();
					}
				} else if (isComando) { 
					System.out.println();
				} else {
					if (sintaticoHelper.isPalavraReservadaDefOuDefstaticSemErro(getTokenAtual().getPalavraReservada())) {
						isMetodo = true;
						tratarMetodo(metodoVO);
						tokenAtual = getTokenAtual();
						continue;
					}
				}
			}
			System.out.println();
		}
	}
	
	private void tratarRetorno(MetodoVO metodoVO) throws AnaliseSemanticaException {
		//verificar se o tipo do retorno do método é o mesmo q a expressão do return
		contarProximoToken();
		TokenVO tokenAtual = getTokenAtual();
		if (!TipoRetornoMetodo.VOID.equals(metodoVO.getTipoRetornoMetodo())) {
			//TODO pegar o retorno da expressão e validar
			if (sintaticoHelper.isPalavraReservadaIDSemErro(tokenAtual.getPalavraReservada())) {
				System.out.println();
			} else if (sintaticoHelper.isPalavraReservadaConstanteSemErro(tokenAtual.getPalavraReservada())) {
				System.out.println();
//				boolean isValid = true;
				TipoRetornoMetodo retornoInesperado = null;
				if (sintaticoHelper.isPalavraReservadaConstStringSemErro(tokenAtual.getPalavraReservada())
						&& !TipoRetornoMetodo.STRING.equals(metodoVO.getTipoRetornoMetodo())) {
//					isValid = false;
					retornoInesperado = TipoRetornoMetodo.STRING; 
				} else if (sintaticoHelper.isPalavraReservadaConstDoubleSemErro(tokenAtual.getPalavraReservada())
						&& !TipoRetornoMetodo.DOUBLE.equals(metodoVO.getTipoRetornoMetodo())) {
//					isValid = false;
					retornoInesperado = TipoRetornoMetodo.DOUBLE;
				} else if (sintaticoHelper.isPalavraReservadaConstIntegerSemErro(tokenAtual.getPalavraReservada())
						&& !TipoRetornoMetodo.INTEGER.equals(metodoVO.getTipoRetornoMetodo())) {
//					isValid = false;
					retornoInesperado = TipoRetornoMetodo.INTEGER;
				} else if (sintaticoHelper.isPalavraReservadaConstBoolSemErro(tokenAtual.getPalavraReservada())
						&& !TipoRetornoMetodo.BOOL.equals(metodoVO.getTipoRetornoMetodo())) {
//					isValid = false;
					retornoInesperado = TipoRetornoMetodo.BOOL;
				}
				
				if (retornoInesperado != null) {
					throw new AnaliseSemanticaException(
						"Tipo do retorno esperado do método "+metodoVO.getNome()+" é "+metodoVO.getTipoRetornoMetodo()+
						", recebeu "+tokenAtual.getPalavraReservada(), getLinhaAtual(), getTokenAtual()); 
				}
			} else {
				System.out.println("tratarRetorno");
			}
		} else {
			throw new AnaliseSemanticaException(
				"Tipo do retorno esperado do método "+metodoVO.getNome()+" é void, não deveria ter retorno", getLinhaAtual(), getTokenAtual());
		}
	}
	
//	private void validarTipoRetorno() {
//	}
	//TODO
//	private void tratarExpressao() {
//	}
	
	private void tratarMetodo(MetodoVO metodoVO) throws AnaliseSemanticaException {
		String nomeMetodo = getToken(3).getValor();
		if (sintaticoHelper.isPalavraReservadaDefStaticSemErro(getTokenAtual().getPalavraReservada())) {
			metodoVO.setEstatico(true);
			metodoVO.setMain(PalavraReservada.MAIN.getRegex().equals(nomeMetodo));
		}
		metodoVO.setNome(nomeMetodo);
		metodoVO.setTipoRetornoMetodo(semanticoHelper.getTipoRetornoMetodo(getToken(2).getPalavraReservada(),getLinhaAtual(),getToken(2)));
		adicionarMetodoMapa(metodoVO);
		tratarParametros(metodoVO);
		contarProximaLinha();
	}
	
	private void tratarParametros(MetodoVO metodoVO) throws AnaliseSemanticaException {
		int numToken = 5;
		List<VariavelVO> parametros = new ArrayList<>();
		while (!sintaticoHelper.isPalavraReservadaFechaParentesesSemErro(getToken(numToken).getPalavraReservada())) {
			TokenVO token = getToken(numToken);
			VariavelVO variavelVO = new VariavelVO();
			variavelVO.setLinha(getLinhaAtual());
			variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(getToken(numToken).getPalavraReservada(),getLinhaAtual(),getToken(numToken)));
			variavelVO.setEscopoVariavel(EscopoVariavel.PARAMETRO);
			variavelVO.setArray(false);
			numToken++;
			token = getToken(numToken);
			
			if (sintaticoHelper.isPalavraReservadaAbreColcheteSemErro(token.getPalavraReservada())) {
				numToken = numToken+2;
				token = getToken(numToken);
				variavelVO.setArray(true);
			}
			if (sintaticoHelper.isPalavraReservadaIDSemErro(token.getPalavraReservada())) {
				variavelVO.setNome(getToken(numToken).getValor());
				adicionarVariavelMapa(metodoVO, variavelVO);
				parametros.add(variavelVO);
				numToken++;
			}
			if (sintaticoHelper.isPalavraReservadaVirgulaSemErro(getToken(numToken).getPalavraReservada())) {
				numToken++;
			}
		}
		metodoVO.setParametros(parametros);
	}
	
	private void adicionarVariavelMapa(MetodoVO metodoVO, VariavelVO variavelVO) throws AnaliseSemanticaException {
		if (!mapaVariaveis.get(metodoVO.getNome()).containsKey(variavelVO.getNome())) {
			mapaVariaveis.get(metodoVO.getNome()).put(variavelVO.getNome(), variavelVO);
		} else {
			throw new AnaliseSemanticaException(
				"Variável "+variavelVO.getNome()+" já foi declarada no método "+metodoVO.getNome()+".",getLinhaAtual(),getTokenAtual());
		}
	}
	
	private void adicionarMetodoMapa(MetodoVO metodoVO) throws AnaliseSemanticaException {
		if (!mapaVariaveis.containsKey(metodoVO.getNome())) {
			mapaVariaveis.put(metodoVO.getNome(), new HashMap<>());
		} else {
			throw new AnaliseSemanticaException("Método "+metodoVO.getNome()+" já foi declarado.", getLinhaAtual(),getTokenAtual());
//			validarMetodoJaExistente(metodoVO);
		}
	}
//	private void validarMetodoJaExistente(MetodoVO metodoVO) throws AnaliseSemanticaException {
//		boolean metodoJaExiste = false;
//		for (String nomeMetodo : mapaVariaveis.keySet()) {
//			if (nomeMetodo.equals(metodoVO.getNome())) {
//				metodoJaExiste = true;
//				break;
//			}
//		}
//		if (metodoJaExiste) { 
//			throw new AnaliseSemanticaException("Método "+metodoVO.getNome()+" já foi declarado.");
//		}
//	}
	
	@SuppressWarnings("unused")
	//TODO
	private TipoVariavel tipoRetornoEsperado(Object objeto1, Object objeto2) throws AnaliseSemanticaException {
		TipoVariavel v1 = retornarTipoVariavel(objeto1);
		TipoVariavel v2 = retornarTipoVariavel(objeto2);
		
		switch (v1) {
			case STRING:
				return TipoVariavel.STRING;
			case DOUBLE:
				switch (v2) {
					case STRING:
						return TipoVariavel.STRING;
					case DOUBLE:
						return TipoVariavel.DOUBLE;
					case INTEGER:
						return TipoVariavel.DOUBLE;
					default: break;
				}
			case INTEGER:
				switch (v2) {
					case STRING:
						return TipoVariavel.STRING;
					case DOUBLE:
						return TipoVariavel.DOUBLE;
					case INTEGER:
						return TipoVariavel.INTEGER;
					default: break;
				}
			case BOOL:
				switch (v2) {
					case STRING:
						return TipoVariavel.STRING;
					case BOOL:
						return TipoVariavel.BOOL;
					default: break;
				}
		}
		//Pode isso Arnaldo?
		throw new AnaliseSemanticaException(
			"Não é possível somar uma variável do tipo "+v1+" com uma do tipo "+v2+".",getLinhaAtual(),getTokenAtual());
	}
	
	private TipoVariavel retornarTipoVariavel(Object objeto) throws AnaliseSemanticaException {
		if (objeto instanceof Boolean) {
			return TipoVariavel.BOOL;
		} else if (objeto instanceof Integer) {
			return TipoVariavel.INTEGER;
		} else if (objeto instanceof Double) {
			return TipoVariavel.DOUBLE;
		} else if (objeto instanceof String) {
			return TipoVariavel.STRING;
		} else {
			throw new AnaliseSemanticaException("Erro ao retornarVariavel",getLinhaAtual(),getTokenAtual());
		}
	}
	
	//----------------------------------// SEMANTICO HELPER //----------------------------------//
	public static class SemanticoHelper {
		
		private static SemanticoHelper instancia;
		
		private SemanticoHelper() { }
		
		public static SemanticoHelper getInstancia() {
			if (instancia == null) {
				instancia = new SemanticoHelper();
			}
			return instancia; 
		}
		
		public TipoRetornoMetodo getTipoRetornoMetodo(PalavraReservada palavraReservada, LinhaVO linhaAtual, TokenVO tokenAtual) 
				throws AnaliseSemanticaException {
			switch (palavraReservada) {
				case VOID:
					return TipoRetornoMetodo.VOID;
				case BOOL:
					return TipoRetornoMetodo.BOOL;
				case INTEGER:
					return TipoRetornoMetodo.INTEGER;
				case DOUBLE:
					return TipoRetornoMetodo.DOUBLE;
				case STRING:
					return TipoRetornoMetodo.STRING;
				default: throw new AnaliseSemanticaException("Erro TipoRetornoMetodo.",linhaAtual, tokenAtual);
			}
		}
		
		public TipoVariavel getTipoVariavel(PalavraReservada palavraReservada, LinhaVO linhaAtual, TokenVO tokenAtual) 
				throws AnaliseSemanticaException {
			switch (palavraReservada) {
				case BOOL:
					return TipoVariavel.BOOL;
				case INTEGER:
					return TipoVariavel.INTEGER;
				case DOUBLE:
					return TipoVariavel.DOUBLE;
				case STRING:
					return TipoVariavel.STRING;
				case VOID: 
					throw new AnaliseSemanticaException("Não deve-se declarar variável do tipo void.",linhaAtual, tokenAtual);
				default: throw new AnaliseSemanticaException("Erro TipoVariavel.",linhaAtual, tokenAtual);
			}
		}
		
	}
	
}
