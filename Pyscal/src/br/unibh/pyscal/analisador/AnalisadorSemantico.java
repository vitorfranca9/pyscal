package br.unibh.pyscal.analisador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import br.unibh.pyscal.enumerador.EscopoVariavelEnum;
import br.unibh.pyscal.enumerador.PalavraReservadaEnum;
import br.unibh.pyscal.enumerador.TipoComandoEnum;
import br.unibh.pyscal.enumerador.TipoRetornoMetodoEnum;
import br.unibh.pyscal.enumerador.TipoVariavelEnum;
import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.ClasseVO;
import br.unibh.pyscal.vo.EnderecoStackVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.MetodoVO;
import br.unibh.pyscal.vo.TokenVO;
import br.unibh.pyscal.vo.VariavelVO;

@SuppressWarnings({"unused"})
public class AnalisadorSemantico extends AnalisadorAbstrato {
	//tudo é map? treemap? ;PP
//	private ClasseVO classe;
//	@Getter private static Map<String, Map<String, VariavelVO>> mapaVariaveis;
	@Getter @Setter private static Map<String, Map<EnderecoStackVO,VariavelVO>> mapaStack;
	@Getter @Setter private static int stackIndex = 0;
//	private TreeMap<String, VariavelVO> treeMapVariavel = new TreeMap<>();
//	private Map<String, MetodoVO> mapaMetodos = new HashMap<>();
	@Getter private static AnalisadorSintaticoHelper sintaticoHelper;
	@Getter private static SemanticoHelper semanticoHelper;
	private boolean isMetodo;
	private boolean isComando;
	private MetodoVO metodoVO;
	
	public static VariavelVO getVariavelStack(MetodoVO metodo, VariavelVO variavel) {
		if (mapaStack.containsKey(metodo.getNome())) {
			EnderecoStackVO enderecoStack = getEnderecoStack(metodo, variavel.getNome());
			if (enderecoStack != null) {
				if (mapaStack.get(metodo.getNome()).containsKey(enderecoStack)) {
					return mapaStack.get(metodo.getNome()).get(enderecoStack);
				}
			}
		}
		return null;
	}
	
	private void adicionarMetodoStack(MetodoVO metodoVO) throws AnaliseSemanticaException {
		if (!mapaStack.containsKey(metodoVO.getNome())) {
			mapaStack.put(metodoVO.getNome(), new HashMap<EnderecoStackVO, VariavelVO>());
		} else {
			throw new AnaliseSemanticaException("Método "+metodoVO.getNome()+" já foi declarado.", getLinhaAtual(),getTokenAtual());
//			validarMetodoJaExistente(metodoVO);
		}
	}
	
	public static void adicionarVariavelStack(MetodoVO metodoVO, VariavelVO variavelVO) {
		if (!mapaStack.get(metodoVO.getNome()).containsKey(getEnderecoStack(metodoVO, variavelVO.getNome()))) {
			Map<EnderecoStackVO, VariavelVO> map = mapaStack.get(metodoVO.getNome());
			if (!map.containsKey(getEnderecoStack(metodoVO, variavelVO.getNome()))) {
				map.put(getNovoEndereco(variavelVO.getNome()), variavelVO);
			} else {
				System.out.println();
			}
		} else {
			VariavelVO v = mapaStack.get(metodoVO.getNome()).get(getEnderecoStack(metodoVO, variavelVO.getNome()));
			if (v.getTokem() == null || !variavelVO.getTipoVariavel().equals(TipoVariavelEnum.ID)) {
				v.setTokem(variavelVO.getTokem());
			} else {
				System.out.println();
			}
		}
	}
	
	public static EnderecoStackVO getNovoEndereco(String key) {
		return new EnderecoStackVO(key,++stackIndex);
	}
	
	public static EnderecoStackVO getEnderecoStack(MetodoVO metodo, String key) {
		if (mapaStack.containsKey(metodo.getNome())) {
			for (EnderecoStackVO endereco : mapaStack.get(metodo.getNome()).keySet()) {
				if (endereco.getKey().equals(key)) {
					return endereco;
				}
			}
		}
		return null;
	}
	
	@Override
	protected void init() {
		super.init();
		sintaticoHelper = AnalisadorSintaticoHelper.getInstancia();
		semanticoHelper = new SemanticoHelper();
//		mapaVariaveis = new HashMap<>();
		mapaStack = new HashMap<>();
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
//						adicionarVariavelMapa(metodoVO, variavelVO);
						adicionarVariavelStack(metodoVO, variavelVO);
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
					tratarComando(metodoVO, false, true, false);
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
//					metodoVO.setVariaveis(new ArrayList<>(mapaVariaveis.get(metodoVO.getNome()).values()));
					metodoVO.setVariaveis(new ArrayList<>(mapaStack.get(metodoVO.getNome()).values()));
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
		if (!metodos.contains(metodoVO) && metodoVO.isMain()) {
			metodos.add(metodoVO);
		}
		classeVO.setMetodos(metodos);
		arquivo.setClasseVO(classeVO);
		System.out.println();
	}
	
	private void tratarComando(MetodoVO metodo, boolean isIf, boolean ifTrue, boolean isWhile) throws AnaliseSemanticaException {
		//ó, comando(ou metodo) tem n comandos dentro dele
		MetodoVO novoMetodo = new MetodoVO();
		TokenVO tokenAtual = getTokenAtual();
		LinhaVO linhaAtual = getLinhaAtual();
		
		if (metodo.getTipoComando() != null && metodo.getTipoComando().equals(TipoComandoEnum.IF)) {
			if (sintaticoHelper.isPalavraReservadaEndSemErro(tokenAtual.getPalavraReservada())) {
				if (isIf) {
					contarProximaLinha();
					return;
				}
			} else if (sintaticoHelper.isPalavraReservadaElseSemErro(tokenAtual.getPalavraReservada())) {
				if (isIf) {
					tratarComando(metodo, false, false, false);
					contarProximaLinha();
					return;
				}
			} else {
				if (isIf) {
					tratarComando(metodo, false, ifTrue, false);
					LinhaVO l = getLinhaAtual();
					TokenVO tk = getTokenAtual();
					if (!sintaticoHelper.isPalavraReservadaElseSemErro(tk.getPalavraReservada()) &&
							!sintaticoHelper.isPalavraReservadaEndSemErro(tk.getPalavraReservada())) {
						tratarComando(metodo, false, ifTrue, false);
					} else {
						contarProximaLinha();
						if (sintaticoHelper.isPalavraReservadaElseSemErro(tk.getPalavraReservada())) {
							tratarComando(metodo, isIf, false, false);
						} else {
							tratarComando(metodo, isIf, ifTrue, false);
						}
					}
					return;
//					if (sintaticoHelper.isPalavraReservadaElseSemErro(tk.getPalavraReservada())) {
//						tratarComando(metodo, true, false);
//					} else if (sintaticoHelper.isPalavraReservadaEndSemErro(tk.getPalavraReservada())) {
//						contarProximaLinha();
//						return;
//					} else {
//						tratarComando(metodo, true, true);
//					}
				}
			}
		} else if (metodo.getTipoComando() != null && metodo.getTipoComando().equals(TipoComandoEnum.WHILE)) {
			if (!isWhile) {
				System.out.println();
			} else { 
				do {
					tratarComando(metodo, isIf, ifTrue, false);
					LinhaVO linhaAtual2 = getLinhaAtual();
					TokenVO tokenAtual2 = getTokenAtual();
					System.out.println();
				} while (!sintaticoHelper.isPalavraReservadaEndSemErro(getTokenAtual().getPalavraReservada()));
				System.out.println();
				return;
			}
		}
		
		novoMetodo.setTipoComando(semanticoHelper.getTipoComando(getLinhaAtual(), tokenAtual));
		novoMetodo.setComando(semanticoHelper.isComando(getLinhaAtual(), tokenAtual));
		novoMetodo.setNome(novoMetodo.getTipoComando().toString());
		novoMetodo.setTipoRetornoMetodo(semanticoHelper.getTipoRetornoMetodo(getLinhaAtual(),tokenAtual));
		if (metodo.isMain()) {
//			List<VariavelVO> parametros = getParametros(metodo);
			novoMetodo.setNome(getTokenAtual().getValor());
			novoMetodo.setMetodoPai(metodo);
			List<VariavelVO> parametros = getParametros(novoMetodo);
			novoMetodo.setParametros(parametros);
		} else {
			List<VariavelVO> parametros = getParametrosComando();
			novoMetodo.setParametros(parametros);
			novoMetodo.setMetodoPai(metodo);
		}
		
		switch (novoMetodo.getTipoComando()) {
			case FUNCAO:
				//verificar se método já existe
				novoMetodo.setNome(getTokenAtual().getValor());
				List<VariavelVO> novosParametros = new ArrayList<>();
				for (int i=0; i < novoMetodo.getParametros().size(); i++) {
					VariavelVO v = novoMetodo.getParametros().get(i);
					if (mapaStack.containsKey(getTokenAtual().getValor())) {
						if (mapaStack.get(getTokenAtual().getValor()).size() >= i) {
							List<VariavelVO> values = new ArrayList<>(mapaStack.get
								(getTokenAtual().getValor()).values());
							VariavelVO variavelAux = values.get(i);
//							mapaStack.get(getTokenAtual().getValor()).put(getNovoEndereco(variavelAux.getNome()), v);
//							mapaStack.get(getTokenAtual().getValor()).put(getNovoEndereco(v.getNome()), v);
							variavelAux.setTipoVariavel(v.getTipoVariavel());
							variavelAux.setTokem(v.getTokem());
//							novosParametros.add(variavelAux); //eh aq
//							adicionarVariavelStack(novoMetodo, variavelVO);
						}
					}
					/*if (mapaVariaveis.containsKey(getTokenAtual().getValor())) {
						if (mapaVariaveis.get(getTokenAtual().getValor()).size() >= i) {
							List<VariavelVO> values = new ArrayList<>(mapaVariaveis.get
								(getTokenAtual().getValor()).values());
							VariavelVO variavelAux = values.get(i);
							mapaVariaveis.get(getTokenAtual().getValor())
								.put(variavelAux.getNome(), v);
							novosParametros.add(variavelAux);
						}
					}*/
					
				}
				System.out.println();
				break;
			case ATRIBUI: //tratar expressão
				TokenVO token = getToken(numTokenAtual+2);
				VariavelVO variavel = new VariavelVO();
				variavel.setEscopoVariavel(EscopoVariavelEnum.PARAMETRO);
				variavel.setArray(false);
				variavel.setLinha(getLinhaAtual());
				variavel.setNome(tokenAtual.getValor());
				variavel.setTipoVariavel(TipoVariavelEnum.getTipoVariavel(token));
				variavel.setTokem(token);
//				adicionarVariavelMapa(metodo, variavel);
				validarParametrosAtribui(novoMetodo, novoMetodo.getParametros());
				adicionarVariavelStack(metodo, variavel);
				novoMetodo.getParametros().add(variavel);
				System.out.println(); 
				break;
			case IF:
				System.out.println();
//				TokenVO flag = getToken(numTokenAtual+1);
				contarProximaLinha();
				tratarComando(novoMetodo, true, true, false);
				break;
			case WHILE:
				contarProximaLinha();
				tratarComando(novoMetodo, true, true, true);
				System.out.println();
				break;
			case WRITE:
			case WRITELN:
			default: break;
		}
		
		if (ifTrue) {
			metodo.getSubMetodos().add(novoMetodo);
		} else {
			metodo.getSubMetodosFalse().add(novoMetodo);
		}
		contarProximaLinha();
		System.out.println();
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

	private void tratarMetodo(MetodoVO metodo) throws AnaliseSemanticaException {
		String nomeMetodo = getToken(3).getValor();
		if (sintaticoHelper.isPalavraReservadaDefStaticSemErro(getTokenAtual().getPalavraReservada())) {
			metodo.setEstatico(true);
			metodo.setMain(PalavraReservadaEnum.MAIN.getRegex().equals(nomeMetodo));
		}
		metodo.setNome(nomeMetodo);
		metodo.setTipoRetornoMetodo(semanticoHelper.getTipoRetornoMetodo(getLinhaAtual(),getToken(2)));
		metodo.setTipoComando(semanticoHelper.getTipoComando());
		
//		adicionarMetodoMapa(metodo);
		adicionarMetodoStack(metodo);
		List<VariavelVO> parametros = getParametros(metodo);
		metodo.setParametros(parametros);
//		tratarParametros(metodoVO);
		contarProximaLinha();
	}
	
	private List<VariavelVO> getParametrosComando() throws AnaliseSemanticaException {
		List<VariavelVO> parametros = new ArrayList<>();
		LinhaVO linhaAtual = getLinhaAtual();
		TokenVO tokenAtual = getTokenAtual();
		TipoComandoEnum tipoComando = semanticoHelper.getTipoComando();
		switch (tipoComando) {
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
			case ATRIBUI:
				parametros = getParametrosAtribui();
				validarParametrosAtribui(metodo.getMetodoPai(), parametros);
				break;
			case IF:
				parametros = getParametrosIf();
				validarParametrosIf(metodo, parametros);
				break;
			case WHILE:
				parametros = getParametrosWhile();
				validarParametrosWhile(metodo, parametros);
				break;
			default: break;
		}
		return parametros;
	}
	
	private void validarParametrosIf(MetodoVO metodo, List<VariavelVO> parametros) throws AnaliseSemanticaException {
		if (parametros != null && !parametros.isEmpty()) {
			if (parametros.size() > 1) {
				throw new AnaliseSemanticaException("IF deve receber apenas um parametro", getLinhaAtual(), getTokenAtual());
			}
			VariavelVO variavelVO = parametros.get(0);
			if (!sintaticoHelper.isPalavraReservadaConstBoolSemErro(variavelVO.getTokem().getPalavraReservada())) {
				VariavelVO variavelStack = getVariavelStack(metodo.getMetodoPai(), variavelVO);
				if (variavelStack == null || !sintaticoHelper.isPalavraReservadaConstBoolSemErro(variavelStack.getTokem().getPalavraReservada())) {
					throw new AnaliseSemanticaException("IF deve receber apenas um parametro do tipo bool", 
						getLinhaAtual(), getTokenAtual());
				}
			}
		}
	}
	
	private void validarParametrosWhile(MetodoVO metodo, List<VariavelVO> parametros) throws AnaliseSemanticaException {
		if (parametros != null && !parametros.isEmpty()) {
			if (parametros.size() > 1) {
				throw new AnaliseSemanticaException("WHILE deve receber apenas um parametro", getLinhaAtual(), getTokenAtual());
			}
			VariavelVO variavelVO = parametros.get(0);
			if (!sintaticoHelper.isPalavraReservadaConstBoolSemErro(variavelVO.getTokem().getPalavraReservada())) {
				throw new AnaliseSemanticaException("WHILE deve receber apenas um parametro do tipo bool", 
					getLinhaAtual(), getTokenAtual());
			}
		}
	}

	private void validarParametrosAtribui(MetodoVO metodo, List<VariavelVO> parametros) throws AnaliseSemanticaException {
		TokenVO tokenAtual = getTokenAtual();
		LinhaVO linhaAtual = getLinhaAtual();
		for (VariavelVO variavelVO : parametros) {
			if (mapaStack.containsKey(metodo.getNome())) {
				if (mapaStack.get(metodo.getNome()).containsKey(getEnderecoStack(metodo, tokenAtual.getValor()))) {
					VariavelVO v2 = mapaStack.get(metodo.getNome()).get(getEnderecoStack(metodo, tokenAtual.getValor()));
					if (!v2.getTipoVariavel().equals(variavelVO.getTipoVariavel())) {
						throw new AnaliseSemanticaException("Atribuicao de variaveis de tipos incompativeis", 
							linhaAtual, tokenAtual);
					}
				} else {
					throw new AnaliseSemanticaException("Variavel "+variavelVO.getNome() +" nao foi declarada", 
							linhaAtual, tokenAtual);
				}
			} else {
				System.out.println();
			}
//			if (!mapaStack.get(metodo.getNome()).containsKey(variavelVO.getNome())) {
//				throw new AnaliseSemanticaException("Variavel "+variavelVO.getNome() +" nao foi declarada", 
//					linhaAtual, tokenAtual);
//			}
		}
		System.out.println();
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
		return getParametros(2);
	}
	
	private List<VariavelVO> getParametrosAtribui() throws AnaliseSemanticaException {
		return getParametros(2);
	}
	
	private List<VariavelVO> getParametrosWhile() throws AnaliseSemanticaException {
		return getParametros(2);
	}

	private List<VariavelVO> getParametrosIf() throws AnaliseSemanticaException {
		return getParametros(2);
	}
	
	private List<VariavelVO> getParametros(int numToken) throws AnaliseSemanticaException{
		List<VariavelVO> parametros = new ArrayList<>();
		LinhaVO linhaAtual = getLinhaAtual();
		TokenVO tokenAtual = getTokenAtual();
		VariavelVO variavelVO = new VariavelVO();
		variavelVO.setArray(false);
		
		while (!sintaticoHelper.isPalavraReservadaFechaParentesesSemErro(getToken(numToken).getPalavraReservada())) {
			numToken++;
			TokenVO token = getToken(numToken);
			variavelVO.setLinha(getLinhaAtual());
			variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(metodoVO, getLinhaAtual(),getToken(numToken)));
			variavelVO.setEscopoVariavel(EscopoVariavelEnum.PARAMETRO);
			variavelVO.setNome(token.getValor());
			TokenVO proxToken = getToken(numToken+1);
			
			if (sintaticoHelper.isPalavraReservadaPontoVirgulaSemErro(proxToken.getPalavraReservada())) {
				if (variavelVO.getTokem() == null || 
						sintaticoHelper.isPalavraReservadaIDSemErro(variavelVO.getTokem().getPalavraReservada())) {
					if (sintaticoHelper.isPalavraReservadaIDSemErro(token.getPalavraReservada())
							|| sintaticoHelper.isPalavraReservadaConstanteSemErro(token.getPalavraReservada())) {
						if (variavelVO.getNome().equals(token.getValor())) {
							variavelVO.setNome(tokenAtual.getValor());
						}
						variavelVO.setTokem(token);
					} else {
						System.out.println();
					}
				} else {
					variavelVO.setNome(tokenAtual.getValor());
					variavelVO.getTokem().setValor(token.getValor());
					System.out.println();
				}
				if (!parametros.contains(variavelVO)) {
					parametros.add(variavelVO);
				}
				break;
			}
			
			if (sintaticoHelper.isPalavraReservadaAbreColcheteSemErro(token.getPalavraReservada())) {
				numToken++;
				variavelVO.setArray(true);
			}
			if (sintaticoHelper.isPalavraReservadaIDSemErro(token.getPalavraReservada())) {
				variavelVO.setNome(token.getValor());
				variavelVO.setTokem(token);
				if (!parametros.contains(variavelVO)) {
					parametros.add(variavelVO);
				}
//				adicionarVariavelMapa(metodoVO, variavelVO);
				adicionarVariavelStack(metodoVO, variavelVO);
				variavelVO = new VariavelVO();
//				parametros.add(variavelVO);
				numToken++;
			} 
			if (sintaticoHelper.isPalavraReservadaConstanteSemErro(token.getPalavraReservada())) {
				variavelVO.setNome(token.getValor());
				variavelVO.setTipoVariavel(TipoVariavelEnum.getTipoVariavel(token));
				variavelVO.setTokem(token);
				if (!parametros.contains(variavelVO)) {
					parametros.add(variavelVO);
				}
//				adicionarVariavelMapa(metodoVO, variavelVO);
				adicionarVariavelStack(metodoVO, variavelVO);
				variavelVO = new VariavelVO();
//				parametros.add(variavelVO);
				numToken++;
			}
			if (sintaticoHelper.isPalavraReservadaVirgulaSemErro(token.getPalavraReservada())) {
				TokenVO tokenAnterior = getToken(numToken-1);
				if (sintaticoHelper.isPalavraReservadaConstanteSemErro(tokenAnterior.getPalavraReservada())) {
					variavelVO.setNome(tokenAnterior.getValor());
					variavelVO.setTokem(tokenAnterior);
					variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(metodoVO, getLinhaAtual(),tokenAnterior));
					if (!parametros.contains(variavelVO)) {
						parametros.add(variavelVO);
					}
//					adicionarVariavelMapa(metodoVO, variavelVO); //buscar o cara no mapa e já trocar e pa
					adicionarVariavelStack(metodoVO, variavelVO);
					variavelVO = new VariavelVO();
				}
				numToken++;
			}
		} //PEGAR CONSTANTES TAMBEM
		if (PalavraReservadaEnum.FECHA_PARENTESES.getRegex().equals(variavelVO.getNome())) {
			TokenVO tokenAnterior = getToken(numToken-1);
			if (sintaticoHelper.isPalavraReservadaConstanteSemErro(tokenAnterior.getPalavraReservada())) {
				variavelVO.setNome(tokenAnterior.getValor());
				variavelVO.setTokem(tokenAnterior);
				variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(metodoVO, getLinhaAtual(),tokenAnterior));
				if (!parametros.contains(variavelVO)) {
					parametros.add(variavelVO);
				}
				//				adicionarVariavelMapa(metodoVO, variavelVO); //buscar o cara no mapa e já trocar e pa
				adicionarVariavelStack(metodoVO, variavelVO);
				variavelVO = new VariavelVO();
			} else if (sintaticoHelper.isPalavraReservadaIDSemErro(tokenAnterior.getPalavraReservada())) {
				variavelVO.setNome(tokenAnterior.getValor());
				variavelVO.setTokem(tokenAnterior);
				variavelVO.setTipoVariavel(semanticoHelper.getTipoVariavel(metodoVO, getLinhaAtual(),tokenAnterior));
				if (!parametros.contains(variavelVO)) {
					parametros.add(variavelVO);
				}
//				adicionarVariavelMapa(metodoVO, variavelVO);
				adicionarVariavelStack(metodoVO, variavelVO);
				variavelVO = new VariavelVO();
			}
		}
		if (variavelVO.getTipoVariavel() != null) { //trocar os parametros do mapa nesse momento?
			if (!parametros.contains(variavelVO)) {
				if (!TipoVariavelEnum.ID.equals(variavelVO.getTipoVariavel())) {
					variavelVO.setTokem(getToken(numToken-1));
				}
				if (!parametros.contains(variavelVO)) {
					parametros.add(variavelVO);
				}
			}
		}
		return parametros;
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
	
	/*private void adicionarVariavelMapa(MetodoVO metodoVO, VariavelVO variavelVO) {
		if (!mapaVariaveis.get(metodoVO.getNome()).containsKey(variavelVO.getNome())) {
			mapaVariaveis.get(metodoVO.getNome()).put(variavelVO.getNome(), variavelVO);
		} else {
			System.out.println();
//			throw new AnaliseSemanticaException(
//				"Variável "+variavelVO.getNome()+" já foi declarada no método "+metodoVO.getNome()+".",getLinhaAtual(),getTokenAtual());
		}
	}*/
	
	/*private void adicionarMetodoMapa(MetodoVO metodoVO) throws AnaliseSemanticaException {
		if (!mapaVariaveis.containsKey(metodoVO.getNome())) {
			mapaVariaveis.put(metodoVO.getNome(), new HashMap<String, VariavelVO>());
		} else {
			throw new AnaliseSemanticaException("Método "+metodoVO.getNome()+" já foi declarado.", getLinhaAtual(),getTokenAtual());
//			validarMetodoJaExistente(metodoVO);
		}
	}*/
	
	/*public static VariavelVO getVariavelMapa(MetodoVO metodo, VariavelVO variavel) {
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
	}*/
	
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
	
	/*for (VariavelVO variavel : metodoVO.getParametros()) {
		System.out.println();
		if (EscopoVariavelEnum.PARAMETRO.equals(variavel.getEscopoVariavel())) { 
			VariavelVO vm = getVariavelMapa(metodoVO, variavel);
			if (vm != null) {
				comandoVO.getVariaveisRetorno().add(vm);
				novoMetodo.getVariaveisRetorno().add(vm);
			} else {
				//erro?
			}
		}
	}
	metodo.getSubMetodos().add(novoMetodo);*/
	
//	private void validarTipoRetorno() {
//	}
	//TODO
//	private void tratarExpressao() {
//	}
	
	/*if (!novosParametros.isEmpty()) {
		novoMetodo.setParametros(novosParametros);
	}
	for (VariavelVO variavel : parametros) {
		if (mapaVariaveis.containsKey(getTokenAtual().getValor())) {
			if (mapaVariaveis.get(getTokenAtual().getValor()).containsKey(variavel)){
				VariavelVO variavelVO = mapaVariaveis.get(getTokenAtual().getValor()).get(variavel);
				System.out.println();
			}
		}
	}
	novoMetodo.setParametros(new ArrayList<>(mapaVariaveis.get(getTokenAtual().getValor()).values()));*/
	
//	private void validarMetodoJaExistente(MetodoVO metodoVO) throws AnaliseSemanticaException {
//	boolean metodoJaExiste = false;
//	for (String nomeMetodo : mapaVariaveis.keySet()) {
//		if (nomeMetodo.equals(metodoVO.getNome())) {
//			metodoJaExiste = true;
//			break;
//		}
//	}
//	if (metodoJaExiste) { 
//		throw new AnaliseSemanticaException("Método "+metodoVO.getNome()+" já foi declarado.");
//	}
//}
	
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
//				default: throw new AnaliseSemanticaException("Erro TipoVariavel.",linhaAtual, tokenAtual);
					default: return null;
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
