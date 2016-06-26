package br.unibh.pyscal.analisador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.unibh.pyscal.enumerador.TipoVariavel;
import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.MetodoVO;
import br.unibh.pyscal.vo.TokenVO;
import br.unibh.pyscal.vo.VariavelVO;

public class AnalisadorSemantico extends AnalisadorAbstrato {

	private Map<String, TokenVO> mapaVariaveis;
//	private List<String> funcoes;	
//	private ClasseVO classe;
	private SintaticoHelper sintaticoHelper;
	
	@Override
	protected void init() {
		super.init();
//		classe = new ClasseVO();
		sintaticoHelper = SintaticoHelper.getInstancia();
		mapaVariaveis = new HashMap<String, TokenVO>();
	}
	
	@Override
	public void analisar(ArquivoVO arquivo) throws Exception {
		init();
		this.arquivo = arquivo;
		compilar();
	}
	
//	@SuppressWarnings("unused")
	private void compilar() throws AnaliseSemanticaException {
		boolean isMetodo = false;
		boolean isVariavel = false;
		boolean isComando = false;
		List<MetodoVO> metodos = new ArrayList<>();
		MetodoVO metodoVO = new MetodoVO();
		Map<MetodoVO, Map<String, VariavelVO>> mapaVariaveis = new HashMap<>();
		
		while (numLinhaAtual < arquivo.getLinhas().size()) {
			TokenVO tokenAtual = getTokenAtual();
//		for (LinhaVO linha : arquivo.getLinhas()) {
			if (sintaticoHelper.isPalavraReservadaClassSemErro(getTokenAtual().getPalavraReservada())) { //erro é sintático
				contarProximaLinha();
			} else {
				
				if (isMetodo) {
					
					if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(getTokenAtual().getPalavraReservada())) {
						// declaração de variáveis
//						while (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemVoidSemErro(getTokenAtual().getPalavraReservada())) {
//						while (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(getTokenAtual().getPalavraReservada())) {
//							contarProximoToken();
//						}
						if (sintaticoHelper.isPalavraReservadaVoidSemErro(getTokenAtual().getPalavraReservada())) {
							throw new AnaliseSemanticaException("Não deve-se declarar variável do tipo void");
						} else {
//							List<VariavelVO> variaveis = new ArrayList<>();
							Map<String, VariavelVO> mapaVariaveisLocais = new HashMap<>();
							isVariavel = true;
							TokenVO token = getToken(3);
							VariavelVO variavelVO = new VariavelVO();
							if (sintaticoHelper.isPalavraReservadaPontoVirgulaSemErro(token.getPalavraReservada())) {
								switch (tokenAtual.getPalavraReservada()) {
									case BOOL:
										variavelVO.setTipoVariavel(TipoVariavel.BOOL);
										break;
									case INTEGER:
										variavelVO.setTipoVariavel(TipoVariavel.INTEGER);
										break;
									case DOUBLE:
										variavelVO.setTipoVariavel(TipoVariavel.DOUBLE);
										break;
									default: break;
								}
								
								variavelVO.setLinha(getLinhaAtual());
//								variaveis.add(variavelVO);
								TokenVO id = getToken(2);
//								if (mapaVariaveisLocais.containsKey(id.getValor())) {
//									mapaVariaveisLocais.put(id.getValor(), variavelVO);
//									contarProximaLinha();
//								} else {
//									throw new AnaliseSemanticaException("Variável "+id.getValor()+" já foi declarada");
//								}
//								if (mapaVariaveis.containsKey(metodoVO.get))
								
							} else if (sintaticoHelper.isPalavraReservadaIgualSemErro(token.getPalavraReservada())) {
								//EH COMANDO DE ATRIBUIÇÃO
								isComando = true;
								continue;
							} else {
								//ERRO?
							}
//							metodoVO.setVariaveis((List<VariavelVO>) mapaVariaveisLocais.values());
							
						}
						
					} else if (sintaticoHelper.isPalavraReservadaIDSemErro(getTokenAtual().getPalavraReservada())) {
						// pode ser atribuição ou função
						System.out.println();
						
					} else if (sintaticoHelper.isPalavraReservadaCmdSemErro(getTokenAtual().getPalavraReservada())) {
						// comandos
						System.out.println();
						
					} else if (sintaticoHelper.isPalavraReservadaReturnSemErro(getTokenAtual().getPalavraReservada())) {
						// retorno do método
						contarProximoToken();
						System.out.println();
						//verificar se o tipo do retorno do método é o mesmo q a expressão do return
//						if ()
						metodos.add(metodoVO);
						metodoVO = new MetodoVO();
					}
					
				} else {
					if (sintaticoHelper.isPalavraReservadaDefSemErro(getTokenAtual().getPalavraReservada())) {
						System.out.println();
						isMetodo = true;
						//TODO analisar parametros
						metodoVO.setParametros(new ArrayList<>());
						contarProximaLinha();
	//					MetodoVO metodoVO = new MetodoVO();
					} else if (sintaticoHelper.isPalavraReservadaDefStaticSemErro(getTokenAtual().getPalavraReservada())) {
						//TODO
					}
				}
			}
			System.out.println();
		}
	}
	
}
