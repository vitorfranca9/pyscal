package br.unibh.pyscal.analisador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.unibh.pyscal.enumerador.EscopoVariavelEnum;
import br.unibh.pyscal.enumerador.PalavraReservadaEnum;
import br.unibh.pyscal.enumerador.TipoComandoEnum;
import br.unibh.pyscal.enumerador.TipoRetornoMetodoEnum;
import br.unibh.pyscal.enumerador.TipoVariavelEnum;
import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.ClasseVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.MetodoVO;
import br.unibh.pyscal.vo.TokenVO;
import br.unibh.pyscal.vo.VariavelVO;
import lombok.Getter;

@SuppressWarnings({"unused"})
public class AnalisadorSemantico extends AnalisadorAbstrato {
	//tudo é map? treemap? ;PP
//	private ClasseVO classe;
	@Getter private static Map<String, Map<String, VariavelVO>> mapaVariaveis;
//	private TreeMap<String, VariavelVO> treeMapVariavel = new TreeMap<>();
//	private Map<String, MetodoVO> mapaMetodos = new HashMap<>();
	@Getter private static AnalisadorSintaticoHelper sintaticoHelper;
	@Getter private static SemanticoHelper semanticoHelper;
	private boolean isMetodo;
	private boolean isComando;
	private MetodoVO metodoVO;
	
	@Override
	protected void init() {
		super.init();
		sintaticoHelper = AnalisadorSintaticoHelper.getInstancia();
		semanticoHelper = new SemanticoHelper();
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
		metodoVO = new MetodoVO();
		contarProximaLinha();
		while (numLinhaAtual < arquivo.getLinhas().size()) {
			TokenVO tokenAtual = getTokenAtual();
			LinhaVO linhaAtual = getLinhaAtual();
			if (isMetodo) {
//				tratarMetodo2(metodoVO);
//				metodos.add(metodoVO);
				if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(getTokenAtual().getPalavraReservada())) {
					// declaração de variáveis
					TokenVO token = getToken(3);
					VariavelVO variavelVO = new VariavelVO();
					if (sintaticoHelper.isPalavraReservadaPontoVirgulaSemErro(token.getPalavraReservada())) {
						variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(metodoVO, getLinhaAtual(),tokenAtual));
						
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
	
	private void tratarComando(MetodoVO metodo) throws AnaliseSemanticaException {
		//ó, comando(ou metodo) tem n comandos dentro dele
//		ComandoVO comandoVO = new ComandoVO(); 
		MetodoVO novoMetodo = new MetodoVO();
		TokenVO tokenAtual = getTokenAtual();
		LinhaVO linhaAtual = getLinhaAtual();
		
//		if (semanticoHelper.isComando(getLinhaAtual(), tokenAtual)) {
//			comandoVO.setTipoComando(semanticoHelper.getTipoComando(getLinhaAtual(), tokenAtual));
			novoMetodo.setTipoComando(semanticoHelper.getTipoComando(getLinhaAtual(), tokenAtual));
//			metodo.getComandos().add(comandoVO);
			novoMetodo.setComando(semanticoHelper.isComando(getLinhaAtual(), tokenAtual));
			novoMetodo.setNome(novoMetodo.getTipoComando().toString());
			novoMetodo.setTipoRetornoMetodo(semanticoHelper.getTipoRetornoMetodo(getLinhaAtual(),tokenAtual));
			List<VariavelVO> parametros = getParametros(novoMetodo);
			novoMetodo.setParametros(parametros);
			novoMetodo.setMetodoPai(metodo);
			
			switch (novoMetodo.getTipoComando()) {
				case FUNCAO:
					//verificar se método já existe
					novoMetodo.setNome(getTokenAtual().getValor());
					List<VariavelVO> novosParametros = new ArrayList<>();
					for (int i=0; i < novoMetodo.getParametros().size(); i++) {
						VariavelVO v = novoMetodo.getParametros().get(i);
						if (mapaVariaveis.containsKey(getTokenAtual().getValor())) {
							if (mapaVariaveis.get(getTokenAtual().getValor()).size() >= i) {
								List<VariavelVO> values = new ArrayList<>(mapaVariaveis.get
									(getTokenAtual().getValor()).values());
								VariavelVO variavelAux = values.get(i);
								mapaVariaveis.get(getTokenAtual().getValor())
									.put(variavelAux.getNome(), v);
								novosParametros.add(variavelAux);
							}
						}
						
					}
					novoMetodo.setParametros(novosParametros);
					/*for (VariavelVO variavel : parametros) {
						if (mapaVariaveis.containsKey(getTokenAtual().getValor())) {
							if (mapaVariaveis.get(getTokenAtual().getValor()).containsKey(variavel)){
								VariavelVO variavelVO = mapaVariaveis.get(getTokenAtual().getValor()).get(variavel);
								System.out.println();
							}
						}
					}*/
//					novoMetodo.setParametros(new ArrayList<>(mapaVariaveis.get(getTokenAtual().getValor()).values()));
					System.out.println();
				break;
	
				default: break;
			}
			
//		} else {
//			novoMetodo.setTipoComando(semanticoHelper.getTipoComando(getLinhaAtual(), tokenAtual));
//			novoMetodo.setComando(semanticoHelper.isComando(getLinhaAtual(), tokenAtual));
//			novoMetodo.setNome(tokenAtual.getValor());
//			novoMetodo.setTipoRetornoMetodo(semanticoHelper.getTipoRetornoMetodo(getLinhaAtual(),tokenAtual));
//			List<VariavelVO> parametros = getParametros(novoMetodo);
//			novoMetodo.setParametros(parametros);
//			novoMetodo.setMetodoPai(metodo);
			
//			for (VariavelVO variavel : metodoVO.getParametros()) {
//				System.out.println();
//				if (EscopoVariavelEnum.PARAMETRO.equals(variavel.getEscopoVariavel())) { 
//					VariavelVO vm = getVariavelMapa(metodoVO, variavel);
//					if (vm != null) {
//						comandoVO.getVariaveisRetorno().add(vm);
//						novoMetodo.getVariaveisRetorno().add(vm);
//					} else {
//						//erro?
//					}
//				}
//			}
//			metodo.getSubMetodos().add(novoMetodo);
//		}
		
//		variável deve ser resultado da expressão
//		tokenAtual = getToken(3);
//		VariavelVO variavel = new VariavelVO();
//		variavel.setLinha(getLinhaAtual());
//		tokenAtual.getValor();
//		variavel.setTokem(tokenAtual);
//		variavel.setTipoVariavel(semanticoHelper.getTipoVariavel(metodoVO, getLinhaAtual(), tokenAtual));
//		variavel.setNome(tokenAtual.getValor());
//		variavel.setTipoVariavel();
//		comandoVO.getLinhas().add(getLinhaAtual());
//		comandoVO.setVariavelRetorno(variavel);
//		comandoVO.setID(isID);
		metodo.getSubMetodos().add(novoMetodo);
		contarProximaLinha();
		System.out.println();
	}
	
	//TODO
	/*private void tratarMetodo2(MetodoVO metodoVO) throws AnaliseSemanticaException {
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
			metodoVO = new MetodoVO();
			isMetodo = false;
			isComando = false;
			contarProximaLinha();
		}
	}*/
	
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
	
	private void tratarMetodo(MetodoVO metodo) throws AnaliseSemanticaException {
		String nomeMetodo = getToken(3).getValor();
		if (sintaticoHelper.isPalavraReservadaDefStaticSemErro(getTokenAtual().getPalavraReservada())) {
			metodo.setEstatico(true);
			metodo.setMain(PalavraReservadaEnum.MAIN.getRegex().equals(nomeMetodo));
		}
		metodo.setNome(nomeMetodo);
		metodo.setTipoRetornoMetodo(semanticoHelper.getTipoRetornoMetodo(getLinhaAtual(),getToken(2)));
		metodo.setTipoComando(semanticoHelper.getTipoComando());
		
		adicionarMetodoMapa(metodo);
		List<VariavelVO> parametros = getParametros(metodo);
		metodo.setParametros(parametros);
//		tratarParametros(metodoVO);
		contarProximaLinha();
	}
	
	private List<VariavelVO> getParametros(MetodoVO metodo) throws AnaliseSemanticaException {
		List<VariavelVO> parametros = new ArrayList<>();
		LinhaVO linhaAtual = getLinhaAtual();
		TokenVO tokenAtual = getTokenAtual();
		switch (metodo.getTipoComando()) {
			case FUNCAO:
				parametros = getParametrosDef();
				break;
			case WRITE:
				parametros = getParametrosWrite();
//				contarProximaLinha();
				break;
			case WRITELN:
				parametros = getParametrosWrite();
//				contarProximaLinha();
				break;

			default: break;
		}
		
		return parametros;
	}
	
	private List<VariavelVO> getParametrosDef() throws AnaliseSemanticaException{
		int numToken = 5;
		if (sintaticoHelper.isPalavraReservadaDefOuDefstaticSemErro(getTokenAtual().getPalavraReservada())) {
			numToken = 5;
		} else if (sintaticoHelper.isPalavraReservadaIDSemErro(getTokenAtual().getPalavraReservada())) {
			if (sintaticoHelper.isPalavraReservadaAbreParentesesSemErro(getToken(2).getPalavraReservada())) {
				numToken = 3;
			} else if (sintaticoHelper.isPalavraReservadaIgualSemErro(getToken(2).getPalavraReservada())) {
				//eh atribui
				System.out.println();
			}
		}
		return getParametros(numToken);
	}
	
	private List<VariavelVO> getParametrosWrite() throws AnaliseSemanticaException{
		LinhaVO linhaAtual = getLinhaAtual();
		TokenVO tokenAtual = getTokenAtual();
		return getParametros(3);
	}
	
	private List<VariavelVO> getParametros(int numToken) throws AnaliseSemanticaException{
		List<VariavelVO> parametros = new ArrayList<>();
		LinhaVO linhaAtual = getLinhaAtual();
		TokenVO tokenAtual = getTokenAtual();
		VariavelVO variavelVO = new VariavelVO();
		
		while (!sintaticoHelper.isPalavraReservadaFechaParentesesSemErro(getToken(numToken).getPalavraReservada())) {
			TokenVO token = getToken(numToken);
			variavelVO.setLinha(getLinhaAtual());
			variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(metodoVO, getLinhaAtual(),getToken(numToken)));
			variavelVO.setEscopoVariavel(EscopoVariavelEnum.PARAMETRO);
			variavelVO.setArray(false);
			variavelVO.setNome(token.getValor());
			numToken++;
			token = getToken(numToken);
			
			if (sintaticoHelper.isPalavraReservadaAbreColcheteSemErro(token.getPalavraReservada())) {
				numToken = numToken+2;
				token = getToken(numToken);
				variavelVO.setArray(true);
			}
			if (sintaticoHelper.isPalavraReservadaIDSemErro(token.getPalavraReservada())) {
				variavelVO.setNome(getToken(numToken).getValor());
				variavelVO.setTokem(token);
				parametros.add(variavelVO);
				adicionarVariavelMapa(metodoVO, variavelVO);
				variavelVO = new VariavelVO();
//				parametros.add(variavelVO);
				numToken++;
			}
			if (sintaticoHelper.isPalavraReservadaVirgulaSemErro(getToken(numToken).getPalavraReservada())) {
				numToken++;
			}
		}
		if (variavelVO.getTipoVariavel() != null) {
			if (!parametros.contains(variavelVO)) {
				if (!TipoVariavelEnum.ID.equals(variavelVO.getTipoVariavel())) {
					variavelVO.setTokem(getToken(numToken-1));
				}
				parametros.add(variavelVO);
			}
		}
		return parametros;
	}
	
	
	
	
//	private void tratarParametros(MetodoVO metodoVO) throws AnaliseSemanticaException {
//		int numToken = 5;
//		List<VariavelVO> parametros = new ArrayList<>();
//		while (!sintaticoHelper.isPalavraReservadaFechaParentesesSemErro(getToken(numToken).getPalavraReservada())) {
//			TokenVO token = getToken(numToken);
//			VariavelVO variavelVO = new VariavelVO();
//			variavelVO.setLinha(getLinhaAtual());
//			variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(metodoVO, getLinhaAtual(),getToken(numToken)));
//			variavelVO.setEscopoVariavel(EscopoVariavelEnum.PARAMETRO);
//			variavelVO.setArray(false);
//			numToken++;
//			token = getToken(numToken);
//			
//			if (sintaticoHelper.isPalavraReservadaAbreColcheteSemErro(token.getPalavraReservada())) {
//				numToken = numToken+2;
//				token = getToken(numToken);
//				variavelVO.setArray(true);
//			}
//			if (sintaticoHelper.isPalavraReservadaIDSemErro(token.getPalavraReservada())) {
//				variavelVO.setNome(getToken(numToken).getValor());
//				adicionarVariavelMapa(metodoVO, variavelVO);
//				parametros.add(variavelVO);
//				numToken++;
//			}
//			if (sintaticoHelper.isPalavraReservadaVirgulaSemErro(getToken(numToken).getPalavraReservada())) {
//				numToken++;
//			}
//		}
//		metodoVO.setParametros(parametros);
//	}
	
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
	
	public static VariavelVO getVariavelMapa(MetodoVO metodo, VariavelVO variavel) {
		for (String nomeMetodo : mapaVariaveis.keySet()) {
			if (metodo.getNome().equals(nomeMetodo)) {
				for (String nomeVariavel : mapaVariaveis.get(nomeMetodo).keySet()) {
					if (nomeVariavel.equals(variavel.getNome())) {
						VariavelVO variavelVO = mapaVariaveis.get(nomeMetodo).get(nomeVariavel);
//						if (variavelVO.getTokem() != null) {
							return variavelVO;
//						} else {
							//lançar exceção?
//						}
					}
				}
			}
		}
		return null;
	}
	
	//----------------------------------// SEMANTICO HELPER //----------------------------------//
	public class SemanticoHelper {
//		private final SemanticoHelper instancia = getInstancia();
//		private SemanticoHelper() { }
//		public SemanticoHelper getInstancia() {
//			SemanticoHelper instancia = null;
//			if (instancia == null) {
//				instancia = new SemanticoHelper();
//			}
//			return instancia; 
//		}
		
		public TipoComandoEnum getTipoComando() throws AnaliseSemanticaException {
			
			if (sintaticoHelper.isPalavraReservadaDefOuDefstaticSemErro(getTokenAtual().getPalavraReservada())) {
				return TipoComandoEnum.FUNCAO;
			} else {
				switch (getTokenAtual().getPalavraReservada()) {
					case WRITE:
						return TipoComandoEnum.WRITE;
					case WRITELN:
						return TipoComandoEnum.WRITELN;
					case IF:
						return TipoComandoEnum.IF;
					case WHILE:
						return TipoComandoEnum.WHILE;
					case ID: //pode ser atribui ou funcao
						TokenVO token = getLinhaAtual().getTokens().get(1);
						if (sintaticoHelper.isPalavraReservadaAbreParentesesSemErro(token.getPalavraReservada())) {
							return TipoComandoEnum.FUNCAO;
						} else if (sintaticoHelper.isPalavraReservadaIgualSemErro(token.getPalavraReservada())) {
							return TipoComandoEnum.ATRIBUI;
						}
					default: break;
				}
			}
			//Arnaldo?
			throw new AnaliseSemanticaException("Erro TipoComando.",getLinhaAtual(), getTokenAtual());
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
				case ID: //pode ser atribui ou funcao
					TokenVO token = linhaAtual.getTokens().get(1);
					if (sintaticoHelper.isPalavraReservadaAbreParentesesSemErro(token.getPalavraReservada())) {
						return TipoComandoEnum.FUNCAO;
					} else if (sintaticoHelper.isPalavraReservadaIgualSemErro(token.getPalavraReservada())) {
						return TipoComandoEnum.ATRIBUI;
					}
				default: break;
			}
			//Arnaldo?
			throw new AnaliseSemanticaException("Erro TipoComando.",linhaAtual, tokenAtual);
		}
		
		public boolean isComando(LinhaVO linhaAtual, TokenVO tokenAtual) throws AnaliseSemanticaException {
			TipoComandoEnum tipoComando = getTipoComando(linhaAtual, tokenAtual);
			return !TipoComandoEnum.ATRIBUI.equals(tipoComando) &&
				!TipoComandoEnum.FUNCAO.equals(tipoComando);
		}
		
		public TipoRetornoMetodoEnum getTipoRetornoMetodo(LinhaVO linhaAtual, TokenVO tokenAtual) 
				throws AnaliseSemanticaException {
			if (sintaticoHelper.isPalavraReservadaCmdSemErro(tokenAtual.getPalavraReservada())) {
				return TipoRetornoMetodoEnum.VOID;
			} else {
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
		}
		
		public TipoVariavelEnum getTipoVariavel(MetodoVO metodo, LinhaVO linhaAtual, TokenVO tokenAtual) 
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
				case ID:
//					return getVariavelMapa(metodo, tokenAtual);
					return TipoVariavelEnum.ID;
				case VOID: 
					throw new AnaliseSemanticaException("Não deve-se declarar variável do tipo void.",linhaAtual, tokenAtual);
				default: throw new AnaliseSemanticaException("Erro TipoVariavel.",linhaAtual, tokenAtual);
			}
		}
		
//		public TipoVariavelEnum getTipoVariavel(MetodoVO metodo, TokenVO tokenAtual) {
//			switch (tokenAtual.getPalavraReservada()) {
//				case TRUE:
//					return TipoVariavelEnum.BOOL;
//				case FALSE:
//					return TipoVariavelEnum.BOOL;
//				case BOOL:
//					return TipoVariavelEnum.BOOL;
//				case INTEGER:
//					return TipoVariavelEnum.INTEGER;
//				case CONSTINTEGER:
//					return TipoVariavelEnum.INTEGER;
//				case DOUBLE:
//					return TipoVariavelEnum.DOUBLE;
//				case CONSTDOUBLE:
//					return TipoVariavelEnum.DOUBLE;
//				case STRING:
//					return TipoVariavelEnum.STRING;
//				case CONSTSTRING:
//					return TipoVariavelEnum.STRING;
//				case ID:
//					return TipoVariavelEnum.ID;
//				default: return null;
//			}
//		}
		
	}
	
}
