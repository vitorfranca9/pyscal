package br.unibh.pyscal.analisador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.unibh.pyscal.enumerador.PalavraReservada;
import br.unibh.pyscal.enumerador.TipoExpressao;
import br.unibh.pyscal.exception.AnaliseSintaticaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.NoVO;
import br.unibh.pyscal.vo.TokenVO;
import sun.invoke.util.BytecodeName;

public class AnalisadorSintatico {
	private static final int MAX_ERROS = 5;
	
//	private Map<String, Token>
//	private NoVO noRoot;
	private List<TokenVO> tokens;
	private ArquivoVO arquivo;
	private int errosCount = 0;
	private String errors = "";
	private int numLinhaAtual;
	private int numTokenAtual = 1;
	
	public void analisar(ArquivoVO arquivo) throws AnaliseSintaticaException {
		ArquivoVO arquivoExecutavel = getArquivoExecutavel(arquivo);
		List<TokenVO> tokens = getTokensAExecutar(arquivo);
		this.tokens = tokens;
		this.arquivo = arquivoExecutavel;
		compilar(arquivoExecutavel);
	}
	
	private void compilar(ArquivoVO arquivo) throws AnaliseSintaticaException {
//		int numLinhaAtual = 1;
		numLinhaAtual = 1;
		
		NoVO noClasse = classe(arquivo, numLinhaAtual);
		System.out.println(noClasse);
		numLinhaAtual++;
		NoVO noFuncao = listaFuncao(arquivo);
		
		arquivo.getArvore().setNoRaiz(noClasse);
		arquivo.getArvore().getNoRaiz().getFilhos().add(noFuncao);
		
		System.out.println(noFuncao);
	}
	
	private NoVO classe(ArquivoVO arquivo, int numLinhaAtual) throws AnaliseSintaticaException {
		LinhaVO linha = getLinha(arquivo, numLinhaAtual);
		NoVO no = null;
//		no.setToken(token);
		if (isExpressaoClasseValida(linha)) {
			ordenar(linha);
			no = new NoVO();
			no.setTipoExpressao(TipoExpressao.CLASSE);
			no.setLinha(linha);
			arquivo.adicionarNoMapa(no);
		} else {
			erro("Erro ao declarar expressão classe");
		}
		return no;
	}
	
	public static Comparator<TokenVO> compararPorOrdem = (t1, t2) -> 
//		t1.getOrdem().compareTo(t2.getOrdem()
		t1.getPalavraReservada().getOrdem().compareTo(t2.getPalavraReservada().getOrdem()
	);

	private void ordenar(LinhaVO linha) {
		Collections.sort(linha.getTokens(),compararPorOrdem);
	}
	
	private NoVO listaFuncao(ArquivoVO arquivo) throws AnaliseSintaticaException {
//		LinhaVO linha = getLinha(arquivo, numLinhaAtual);
		NoVO noFilho = funcao(arquivo);
		NoVO noPai = null;
		noPai = listaFuncao(arquivo);
		if (noPai != null) {
			noPai.getFilhos().add(noFilho);
			return noPai;
		}
		return noFilho;
	}
	
	private NoVO funcao(ArquivoVO arquivo) throws AnaliseSintaticaException {
//		int numTokenAtual = 1;
		numTokenAtual = 1;
		LinhaVO linha = getLinha(arquivo, numLinhaAtual);
		NoVO noFuncao = new NoVO();
		TokenVO tokenDEF = linha.getTokens().get(numTokenAtual-1);
		if (isPalavraReservadaDEF(tokenDEF.getPalavraReservada())) {
			NoVO noDEF = new NoVO();
			noDEF.setLinha(linha);
			noDEF.getTokens().add(tokenDEF);
			noFuncao.getFilhos().add(noDEF);
//			numLinhaAtual++;
			numTokenAtual++;
			
			NoVO noTipoMacro = tipoMacro(arquivo, numLinhaAtual, numTokenAtual);
			noDEF.getFilhos().add(noTipoMacro);
			TokenVO tokenTipoMacro = linha.getTokens().get(numTokenAtual);
			
			if (isPalavraReservadaID(tokenTipoMacro.getPalavraReservada())) {
				NoVO noID = new NoVO();
				noID.setLinha(linha);
				noID.getTokens().add(tokenTipoMacro);
				noTipoMacro.getFilhos().add(noID);
				numTokenAtual++;
				
				TokenVO tokenAbreParenteses = linha.getTokens().get(numTokenAtual);
				if (isPalavraReservadaAbreParenteses(tokenAbreParenteses.getPalavraReservada())) {
					NoVO noAbreParenteses = new NoVO();
					noAbreParenteses.setLinha(linha);
					noAbreParenteses.getTokens().add(tokenAbreParenteses);
					noID.getFilhos().add(noAbreParenteses);
					numTokenAtual++;

					//TODO
					NoVO noListaArg = listaArg(arquivo, numLinhaAtual, numTokenAtual);
					numTokenAtual = numTokenAtual+5; //pular listaArg
					TokenVO tokenFechaParenteses = linha.getTokens().get(numTokenAtual);
					if (isPalavraReservadaFechaParenteses(tokenFechaParenteses.getPalavraReservada())) {
						NoVO noFechaParenteses = new NoVO();
						noFechaParenteses.setLinha(linha);
						noFechaParenteses.getTokens().add(tokenFechaParenteses);
						noListaArg.getFilhos().add(noFechaParenteses);
						numTokenAtual++;
						
						TokenVO tokenDoisPontos = linha.getTokens().get(numTokenAtual);
						if (isPalavraReservadaDoisPontos(tokenDoisPontos.getPalavraReservada())) {
							NoVO noDoisPontos = new NoVO();
							noDoisPontos.setLinha(linha);
							noDoisPontos.getTokens().add(tokenDoisPontos);
							noFechaParenteses.getFilhos().add(noDoisPontos);
//							numTokenAtual++;
							
							//TODO
							NoVO noDeclaraIDLinha = declaraIDLinha(arquivo);
							noDoisPontos.getFilhos().add(noDeclaraIDLinha);
							NoVO noListaCmd = listaCmd(arquivo);
							noDeclaraIDLinha.getFilhos().add(noListaCmd);
							NoVO noRetorno = retorno(arquivo);
							noListaCmd.getFilhos().add(noRetorno);
							//pular 
							LinhaVO proxLinha = arquivo.getLinhas().get(numLinhaAtual);
							linha = proxLinha;
							contarProximaLinhaToken(arquivo, linha);
							LinhaVO proxProxLinha = arquivo.getLinhas().get(numLinhaAtual);
							linha = proxProxLinha;
							
							TokenVO tokenEnd = linha.getTokens().get(numTokenAtual-1);
							if (isPalavraReservadaEnd(tokenEnd.getPalavraReservada())) {
								NoVO noEnd = new NoVO();
								noEnd.setLinha(linha);
								noEnd.getTokens().add(tokenEnd);
								TokenVO tokenPontoVirgula = linha.getTokens().get(numTokenAtual);
								if (isPalavraReservadaPontoVirgula(tokenPontoVirgula.getPalavraReservada())) {
									NoVO noPontoVirgula = new NoVO();
									noPontoVirgula.setLinha(linha);
									noPontoVirgula.getTokens().add(tokenEnd);
									noEnd.getFilhos().add(noPontoVirgula);
									numLinhaAtual = numLinhaAtual+2;
									//fim funcao
									
								}
							}
						}
					}
					
				}
			}
		}
		return noFuncao;
	}
	
	private void contarProximaLinhaToken(ArquivoVO arquivo, LinhaVO linhaAtual) {
		if (linhaAtual.getTokens().size() > numTokenAtual) {
			numTokenAtual++;
		} else {
			numLinhaAtual++;
			numTokenAtual = 1;
		}
	}
	
	private NoVO retorno(ArquivoVO arquivo) {
//		NoVO noRetorno = new NoVO();
//		return noRetorno;
		return null;
	}

	private NoVO listaCmd(ArquivoVO arquivo) {
		NoVO noListaCmd = new NoVO();
		return noListaCmd;
	}

	private NoVO declaraID(ArquivoVO arquivo) {
		NoVO noDeclaraID = new NoVO();
		return noDeclaraID;
	}
	
	private NoVO declaraIDLinha(ArquivoVO arquivo) throws AnaliseSintaticaException {
		NoVO noDeclaraIDLinha = new NoVO();
		LinhaVO linha = getLinha(arquivo, numLinhaAtual);
		TokenVO tokenTipoPrimitivo = linha.getTokens().get(numTokenAtual);
		
		if (isPalavraReservadaTipoPrimitivo(tokenTipoPrimitivo.getPalavraReservada())) {
			
		}
		
		NoVO noDeclaraID = declaraID(arquivo);
		return noDeclaraIDLinha;
	}
	
	private boolean isPalavraReservadaPontoVirgula(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.PONTO_VIRGULA, palavra, "funcao espera ponto virgula dps de end");
	}
	
	private boolean isPalavraReservadaEnd(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.END, palavra, "funcao espera end dps de retorno");
	}
	
	private boolean isPalavraReservadaDoisPontos(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.DOIS_PONTOS, palavra, "funcao espera dois pontos dps de fecha parenteses");
	}
	
	private boolean isPalavraReservadaFechaParenteses(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.FECHA_PARENTESES, palavra, "funcao espera fecha parenteses dps de lista arg");
	}
	
	private boolean isPalavraReservadaAbreParenteses(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.ABRE_PARENTESES, palavra, "funcao espera abre parenteses dps do id");
	}
	
	private boolean isPalavraReservadaFechaColchete(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.FECHA_COLCHETE, palavra, "Esperava fecha colchete");
	}
	
	private boolean isPalavraReservadaDEF(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.DEF, palavra, "funcao deve iniciar com def");
	}
	
	private boolean isPalavraReservadaID(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.ID, palavra, "funcao espera id dps de tipo macro");
	}
	
//	private boolean isPalavraReservada(PalavraReservada palavraReservada, PalavraReservada palavraAtual, String mensagemValidacao) 
//			throws AnaliseSintaticaException {
//		if (palavraReservada.equals(palavraAtual)) {
//			return true;
//		} 
//		erro(mensagemValidacao);
//		return false;
//	}
	
	private boolean isPalavraReservada(PalavraReservada palavraReservada, PalavraReservada palavraAtual, String mensagemValidacao) 
			throws AnaliseSintaticaException {
		if (palavraReservada.equals(palavraAtual)) {
			return true;
		} 
		erro(mensagemValidacao);
		return false;
	}
	
	private boolean isPalavraReservadaTipoPrimitivo(PalavraReservada palavra, String mensagemValidacao) 
			throws AnaliseSintaticaException {
		if (tiposPrimitivos.contains(palavra)) {
			return true;
		}
		erro(mensagemValidacao);
		return false;
	}
	
	private boolean isPalavraReservadaTipoPrimitivo(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservadaTipoPrimitivo(palavra, "Não é um tipo primitivo");
	}
	
	private NoVO listaArg(ArquivoVO arquivo, int numLinhaAtual, int numTokenAtual) {
		NoVO nolistaArg = new NoVO();
		return nolistaArg;
	}
	
	private NoVO arg(ArquivoVO arquivo, int numLinhaAtual, int numTokenAtual) {
		NoVO noArg = new NoVO();
		return noArg;
	}
	
	private NoVO tipoMacro(ArquivoVO arquivo, int numLinhaAtual, int numTokenAtual) throws AnaliseSintaticaException {
		LinhaVO linha = getLinha(arquivo, numLinhaAtual);
		NoVO noTipoPrimitivo = tipoPrimitivo(arquivo, numLinhaAtual, numTokenAtual);
		numTokenAtual++;
		TokenVO proxToken = linha.getTokens().get(numTokenAtual-1);
		if (PalavraReservada.ABRE_COLCHETE.equals(proxToken.getPalavraReservada())) {
			numTokenAtual++;
			TokenVO proxProxToken = linha.getTokens().get(numTokenAtual-1);
			if (isPalavraReservadaFechaColchete(proxProxToken.getPalavraReservada())) {
				
				
			}
		}
		return noTipoPrimitivo;
	}
	
	private NoVO tipoPrimitivo(ArquivoVO arquivo, int numLinhaAtual, int numTokenAtual) throws AnaliseSintaticaException {
		LinhaVO linha = getLinha(arquivo, numLinhaAtual);
		TokenVO token = linha.getTokens().get(numTokenAtual-1);
		NoVO noTipoPrimitivo = null;
		if (isPalavraReservadaTipoPrimitivo(token.getPalavraReservada())) {
			noTipoPrimitivo = new NoVO();
			noTipoPrimitivo.setTipoExpressao(TipoExpressao.TIPO_PRIMITIVO);
			noTipoPrimitivo.setLinha(linha);
			noTipoPrimitivo.getTokens().add(token);
		}
		return noTipoPrimitivo;
	}
	
	private static List<PalavraReservada> tiposPrimitivos = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.BOOL,
		PalavraReservada.INTEGER,
		PalavraReservada.STRING,
		PalavraReservada.DOUBLE,
		PalavraReservada.VOID
	});
	
	private static PalavraReservada[] sequenciaClasse = new PalavraReservada[]{
		PalavraReservada.CLASS,
		PalavraReservada.ID,
		PalavraReservada.DOIS_PONTOS
	};
		
	private boolean isExpressaoClasseValida(LinhaVO linha) {
		boolean valido = true;
		if (sequenciaClasse.length != linha.getTokens().size()) {
			valido = false;
		} else { 
			for(int i = 1; i < sequenciaClasse.length; i++) {
				PalavraReservada palavraLinha = getTokenPorLinha(linha, i).getPalavraReservada();
				PalavraReservada palavraEsperada = sequenciaClasse[i];
				if (!palavraEsperada.equals(palavraLinha)) {
					valido = false;
					break;
				}
			}
		}
		return valido;
	}
	
	private TokenVO getTokenPorLinha(LinhaVO linha, int numeroLinha) {
		if (!linha.getTokens().isEmpty() &&
				linha.getTokens().size() >= numeroLinha) {
			return linha.getTokens().get(numeroLinha);
		}
		return null;
	}
	
//	private boolean isExpressaoValida(PalavraReservada[] sequenciaEsperada, 
//			PalavraReservada[] veio) {
//		return false;
//	}
	
	private LinhaVO getLinha(ArquivoVO arquivo, int numLinhaAtual) {
		LinhaVO linha = null;
		for (LinhaVO l : arquivo.getLinhas()) {
			if (l.getNumero() == numLinhaAtual) {
				linha = l;
				break;
			}
		}
		return linha;
	}
	
	private void erro(String mensagem) throws AnaliseSintaticaException {
		errosCount++;
		errors += mensagem+"\n";
		if (errosCount >= MAX_ERROS) {
			throw new AnaliseSintaticaException(errors);
		}
	}
	
	private ArquivoVO getArquivoExecutavel(ArquivoVO arquivo) {
		ArquivoVO arquivoExecutavel = new ArquivoVO();
		int numLinha = 1;
		for (LinhaVO linhaVO : arquivo.getLinhas()) {
			List<TokenVO> tokensLinha = new ArrayList<>();
			String conteudo = "";
			for (TokenVO tokenVO : linhaVO.getTokens()) {
				if (!isComentario(tokenVO)){
					tokensLinha.add(tokenVO);
					conteudo += tokenVO.getValor()+" ";
				}
			}
			if (!tokensLinha.isEmpty()) {
				LinhaVO linhaExecutavel = new LinhaVO();
				linhaExecutavel.getTokens().addAll(tokensLinha);
				linhaExecutavel.setNumero(numLinha);
				linhaExecutavel.setConteudo(conteudo);
				arquivoExecutavel.getLinhas().add(linhaExecutavel);
				numLinha++;
				conteudo = "";
			}
		}
		return arquivoExecutavel;
	}
	
	private List<TokenVO> getTokensAExecutar(ArquivoVO arquivo) {
		List<TokenVO> tokensAExecutar = new ArrayList<>();
		for (LinhaVO linha : arquivo.getLinhas()) {
			for (TokenVO tokenVO : linha.getTokens()) {
				if (!isComentario(tokenVO)) {
					tokensAExecutar.add(tokenVO);
				}
			}
		}
		return tokensAExecutar;
	}
	
	private boolean isComentario(TokenVO tokenVO) {
		return PalavraReservada.COMENTARIO_GERAL.equals(tokenVO.getPalavraReservada()) ||
				PalavraReservada.COMENTARIO_LINHA.equals(tokenVO.getPalavraReservada());
	}

}
