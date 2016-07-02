package br.unibh.pyscal.analisador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.unibh.pyscal.enumerador.EscopoVariavelEnum;
import br.unibh.pyscal.enumerador.PalavraReservadaEnum;
import br.unibh.pyscal.enumerador.TipoComandoEnum;
import br.unibh.pyscal.enumerador.TipoRetornoMetodoEnum;
import br.unibh.pyscal.enumerador.TipoVariavelEnum;
import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.util.JasminUtil;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.ClasseVO;
import br.unibh.pyscal.vo.ComandoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.MetodoVO;
import br.unibh.pyscal.vo.TokenVO;
import br.unibh.pyscal.vo.VariavelVO;

public class AnalisadorSemantico extends AnalisadorAbstrato {
	//tudo é map? treemap? ;PP
//	private ClasseVO classe;
	private Map<String, Map<String, VariavelVO>> mapaVariaveis = new HashMap<>();
//	private TreeMap<String, VariavelVO> treeMapVariavel = new TreeMap<>();
	private Map<String, ComandoVO> mapaComandos = new HashMap<>();
	private AnalisadorSintaticoHelper sintaticoHelper;
	private SemanticoHelper semanticoHelper;
	private boolean isMetodo;
	private boolean isComando;
	
	@Override
	protected void init() {
		super.init();
		sintaticoHelper = AnalisadorSintaticoHelper.getInstancia();
		semanticoHelper = SemanticoHelper.getInstancia();
		mapaVariaveis = new HashMap<>();
	}
	
	@Override
	public void analisar(ArquivoVO arquivo) throws Exception {
		init();
		ArquivoVO arquivoExecutavel = sintaticoHelper.getArquivoExecutavel(arquivo);
		arquivo.setLinhas(arquivoExecutavel.getLinhas());
		this.arquivo = arquivo;
		isMetodo = false;
		isComando = false;
		compilar();
//		String jCode = JasminUtil.getJ(this.arquivo);
	}
	
//	@SuppressWarnings("unused")
	private void compilar() throws AnaliseSemanticaException {
		
		List<MetodoVO> metodos = new ArrayList<>();
		MetodoVO metodoVO = new MetodoVO();
		contarProximaLinha();
		while (numLinhaAtual < arquivo.getLinhas().size()) {
			TokenVO tokenAtual = getTokenAtual();
			if (isMetodo) {
//				tratarMetodo2(metodoVO);
//				metodos.add(metodoVO);
				if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(getTokenAtual().getPalavraReservada())) {
					// declaração de variáveis
					TokenVO token = getToken(3);
					VariavelVO variavelVO = new VariavelVO();
					if (sintaticoHelper.isPalavraReservadaPontoVirgulaSemErro(token.getPalavraReservada())) {
						variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(getLinhaAtual(),tokenAtual));
						
						TokenVO id = getToken(2);
						variavelVO.setNome(id.getValor());
						variavelVO.setLinha(getLinhaAtual());
						adicionarVariavelMapa(metodoVO, variavelVO);
						variavelVO = new VariavelVO();
						contarProximaLinha();
						
					} else if (sintaticoHelper.isPalavraReservadaIgualSemErro(token.getPalavraReservada()) || 
							sintaticoHelper.isPalavraReservadaAbreParentesesSemErro(token.getPalavraReservada())) {
						//EH COMANDO DE ATRIBUIÇÃO
						isComando = true;
						continue;
					} else {
						//ERRO?
					}
				} else if (sintaticoHelper.isPalavraReservadaCmdSemErro(getTokenAtual().getPalavraReservada()) ||
						sintaticoHelper.isPalavraReservadaIDSemErro(getTokenAtual().getPalavraReservada())) {
					// comandos
					System.out.println();
					tratarComando(metodoVO);
//					isMetodo = false;
					isComando = false;
				} else if (sintaticoHelper.isPalavraReservadaReturnSemErro(getTokenAtual().getPalavraReservada())) {
					// retorno do método
					tratarRetorno(metodoVO);
					contarProximaLinha();
					contarProximaLinha();
					metodos.add(metodoVO);
					metodoVO = new MetodoVO();
					isMetodo = false;
					isComando = false;
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
		ClasseVO classeVO = new ClasseVO();
		classeVO.setMetodos(metodos);
		arquivo.setClasseVO(classeVO);
		System.out.println();
	}
	
	private void tratarComando(MetodoVO metodoVO) throws AnaliseSemanticaException {
		//ó, comando tem n comandos dentro dele
		ComandoVO comandoVO = new ComandoVO(); 
		TokenVO tokenAtual = getTokenAtual();
		comandoVO.setTipoComando(semanticoHelper.getTipoComando(getLinhaAtual(), tokenAtual));
//		variável deve ser resultado da expressão
		tokenAtual = getToken(3);
		VariavelVO variavel = new VariavelVO();
		variavel.setLinha(getLinhaAtual());
		tokenAtual.getValor();
		variavel.setTokem(tokenAtual);
		variavel.setTipoVariavel(semanticoHelper.getTipoVariavel(getLinhaAtual(), tokenAtual));
//		variavel.setTipoVariavel();
		comandoVO.getLinhas().add(getLinhaAtual());
		comandoVO.setVariavelRetorno(variavel);
		metodoVO.getComandos().add(comandoVO);
		contarProximaLinha();
		System.out.println();
	}
	
	//TODO
	private void tratarMetodo2(MetodoVO metodoVO) throws AnaliseSemanticaException {
		TokenVO tokenAtual = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(getTokenAtual().getPalavraReservada())) {
			// declaração de variáveis
			TokenVO token = getToken(3);
			VariavelVO variavelVO = new VariavelVO();
			if (sintaticoHelper.isPalavraReservadaPontoVirgulaSemErro(token.getPalavraReservada())) {
				variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(getLinhaAtual(),tokenAtual));
				
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
//				continue;
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
			contarProximaLinha();
			contarProximaLinha();
			isMetodo = false;
			System.out.println();
		} else if (sintaticoHelper.isPalavraReservadaEndSemErro(getTokenAtual().getPalavraReservada())) {
			metodoVO.setVariaveis(new ArrayList<>(mapaVariaveis.get(metodoVO.getNome()).values()));
//			metodos.add(metodoVO);
			metodoVO = new MetodoVO();
			isMetodo = false;
			isComando = false;
			contarProximaLinha();
		}
	}
	
	private void tratarRetorno(MetodoVO metodoVO) throws AnaliseSemanticaException {
		//verificar se o tipo do retorno do método é o mesmo q a expressão do return
		contarProximoToken();
		TokenVO tokenAtual = getTokenAtual();
		if (!TipoRetornoMetodoEnum.VOID.equals(metodoVO.getTipoRetornoMetodo())) {
			//TODO pegar o retorno da expressão e validar
			if (sintaticoHelper.isPalavraReservadaIDSemErro(tokenAtual.getPalavraReservada())) {
				System.out.println();
			} else if (sintaticoHelper.isPalavraReservadaConstanteSemErro(tokenAtual.getPalavraReservada())) {
				System.out.println();
				//String aceita todos tipos e converte, Double double e integer, etc.
				TipoRetornoMetodoEnum retornoInesperado = null;
				if (sintaticoHelper.isPalavraReservadaConstStringSemErro(tokenAtual.getPalavraReservada())
						&& !TipoRetornoMetodoEnum.STRING.equals(metodoVO.getTipoRetornoMetodo())) {
					retornoInesperado = TipoRetornoMetodoEnum.STRING; 
				} else if (sintaticoHelper.isPalavraReservadaConstDoubleSemErro(tokenAtual.getPalavraReservada())
						&& !TipoRetornoMetodoEnum.DOUBLE.equals(metodoVO.getTipoRetornoMetodo())) {
					retornoInesperado = TipoRetornoMetodoEnum.DOUBLE;
				} else if (sintaticoHelper.isPalavraReservadaConstIntegerSemErro(tokenAtual.getPalavraReservada())
						&& !TipoRetornoMetodoEnum.INTEGER.equals(metodoVO.getTipoRetornoMetodo())) {
					retornoInesperado = TipoRetornoMetodoEnum.INTEGER;
				} else if (sintaticoHelper.isPalavraReservadaConstBoolSemErro(tokenAtual.getPalavraReservada())
						&& !TipoRetornoMetodoEnum.BOOL.equals(metodoVO.getTipoRetornoMetodo())) {
					retornoInesperado = TipoRetornoMetodoEnum.BOOL;
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
			metodoVO.setMain(PalavraReservadaEnum.MAIN.getRegex().equals(nomeMetodo));
		}
		metodoVO.setNome(nomeMetodo);
		metodoVO.setTipoRetornoMetodo(semanticoHelper.getTipoRetornoMetodo(getLinhaAtual(),getToken(2)));
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
			variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(getLinhaAtual(),getToken(numToken)));
			variavelVO.setEscopoVariavel(EscopoVariavelEnum.PARAMETRO);
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
			mapaVariaveis.put(metodoVO.getNome(), new HashMap<String, VariavelVO>());
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
	private TipoVariavelEnum tipoRetornoEsperado(Object objeto1, Object objeto2) throws AnaliseSemanticaException {
		TipoVariavelEnum v1 = retornarTipoVariavel(objeto1);
		TipoVariavelEnum v2 = retornarTipoVariavel(objeto2);
		
		switch (v1) {
			case STRING:
				return TipoVariavelEnum.STRING;
			case DOUBLE:
				switch (v2) {
					case STRING:
						return TipoVariavelEnum.STRING;
					case DOUBLE:
						return TipoVariavelEnum.DOUBLE;
					case INTEGER:
						return TipoVariavelEnum.DOUBLE;
					default: break;
				}
			case INTEGER:
				switch (v2) {
					case STRING:
						return TipoVariavelEnum.STRING;
					case DOUBLE:
						return TipoVariavelEnum.DOUBLE;
					case INTEGER:
						return TipoVariavelEnum.INTEGER;
					default: break;
				}
			case BOOL:
				switch (v2) {
					case STRING:
						return TipoVariavelEnum.STRING;
					case BOOL:
						return TipoVariavelEnum.BOOL;
					default: break;
				}
		}
		//Pode isso Arnaldo?
		throw new AnaliseSemanticaException(
			"Não é possível somar uma variável do tipo "+v1+" com uma do tipo "+v2+".",getLinhaAtual(),getTokenAtual());
	}
	
	private TipoVariavelEnum retornarTipoVariavel(Object objeto) throws AnaliseSemanticaException {
		if (objeto instanceof Boolean) {
			return TipoVariavelEnum.BOOL;
		} else if (objeto instanceof Integer) {
			return TipoVariavelEnum.INTEGER;
		} else if (objeto instanceof Double) {
			return TipoVariavelEnum.DOUBLE;
		} else if (objeto instanceof String) {
			return TipoVariavelEnum.STRING;
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
		
		public TipoComandoEnum getTipoComando(LinhaVO linhaAtual, TokenVO tokenAtual) throws AnaliseSemanticaException {
			switch (tokenAtual.getPalavraReservada()) {
				case WRITE:
					return TipoComandoEnum.WRITE;
				case WRITELN:
					return TipoComandoEnum.WRITELN;
				case IF:
					return TipoComandoEnum.IF;
				case WHILE:
					return TipoComandoEnum.WHILE;
				case ID:
					//pode ser atribui ou funcao
					return TipoComandoEnum.ATRIBUI;
//					return TipoComandoEnum.FUNCAO;
				default: throw new AnaliseSemanticaException("Erro TipoComando.",linhaAtual, tokenAtual);
			}
			
			
		}
		
		public TipoRetornoMetodoEnum getTipoRetornoMetodo(LinhaVO linhaAtual, TokenVO tokenAtual) 
				throws AnaliseSemanticaException {
			switch (tokenAtual.getPalavraReservada()) {
				case VOID:
					return TipoRetornoMetodoEnum.VOID;
				case BOOL:
					return TipoRetornoMetodoEnum.BOOL;
				case INTEGER:
					return TipoRetornoMetodoEnum.INTEGER;
				case DOUBLE:
					return TipoRetornoMetodoEnum.DOUBLE;
				case STRING:
					return TipoRetornoMetodoEnum.STRING;
				default: throw new AnaliseSemanticaException("Erro TipoRetornoMetodo.",linhaAtual, tokenAtual);
			}
		}
		
		public TipoVariavelEnum getTipoVariavel(LinhaVO linhaAtual, TokenVO tokenAtual) 
				throws AnaliseSemanticaException {
			switch (tokenAtual.getPalavraReservada()) {
				case TRUE:
					return TipoVariavelEnum.BOOL;
				case FALSE:
					return TipoVariavelEnum.BOOL;
				case BOOL:
					return TipoVariavelEnum.BOOL;
				case INTEGER:
					return TipoVariavelEnum.INTEGER;
				case CONSTINTEGER:
					return TipoVariavelEnum.INTEGER;
				case DOUBLE:
					return TipoVariavelEnum.DOUBLE;
				case CONSTDOUBLE:
					return TipoVariavelEnum.DOUBLE;
				case STRING:
					return TipoVariavelEnum.STRING;
				case CONSTSTRING:
					return TipoVariavelEnum.STRING;
				case VOID: 
					throw new AnaliseSemanticaException("Não deve-se declarar variável do tipo void.",linhaAtual, tokenAtual);
				default: throw new AnaliseSemanticaException("Erro TipoVariavel.",linhaAtual, tokenAtual);
			}
		}
		
	}
	
}
