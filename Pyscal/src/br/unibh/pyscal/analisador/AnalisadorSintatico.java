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

public class AnalisadorSintatico {
//	private NoVO noRoot;
//	private List<TokenVO> tokens;
	private ArquivoVO arquivo;
	private int numLinhaAtual;
	private int numTokenAtual;
	
	public AnalisadorSintatico() {
		init();
	}
	
	public static Comparator<TokenVO> compararPorOrdem = (t1, t2) -> 
	//	t1.getOrdem().compareTo(t2.getOrdem()
		t1.getPalavraReservada().getOrdem().compareTo(t2.getPalavraReservada().getOrdem()
	);
		
	private void ordenar(LinhaVO linha) {
		Collections.sort(linha.getTokens(),compararPorOrdem);
	}
	
	private void init() {
//		this.tokens = null;
		this.arquivo = null;
		this.numLinhaAtual = 1;
		this.numTokenAtual = 1;
		SintaticoHelper.errors = "";
		SintaticoHelper.errosCount = 0;
	}

	public void analisar(ArquivoVO arquivo) throws AnaliseSintaticaException {
		init();
		ArquivoVO arquivoExecutavel = SintaticoHelper.getArquivoExecutavel(arquivo);
		/*List<TokenVO> tokens = */SintaticoHelper.getTokensAExecutar(arquivo);
//		this.tokens = tokens;
		this.arquivo = arquivoExecutavel;
		compilar();
	}
	
	private void compilar() throws AnaliseSintaticaException {
		numLinhaAtual = 1;
		
		NoVO noClasse = classe();
//		System.out.println(noClasse);
//		numLinhaAtual++;
		NoVO noFuncao = listaFuncao();
		noClasse.getUltimoFilho().getFilhos().add(noFuncao);
		numLinhaAtual = 22;
		numTokenAtual = 1;
		NoVO noMain = main();
		noFuncao.getUltimoFilho().getFilhos().add(noMain);
		NoVO noEndPonto = endPonto();
		noMain.getUltimoFilho().getFilhos().add(noEndPonto);
//		arquivo.getArvore().setNoRaiz(noClasse);
//		arquivo.getArvore().getNoRaiz().getFilhos().add(noFuncao);
//		System.out.println(noFuncao);
	}
	
	private NoVO classe() throws AnaliseSintaticaException {
		NoVO noClasse = noClass();
		NoVO noID = id();
		noClasse.getFilhos().add(noID);
		NoVO noDoisPontos = doisPontos();
		noID.getFilhos().add(noDoisPontos);
		return noClasse;
	}
	
	private NoVO main() throws AnaliseSintaticaException {
		NoVO noMain = defstatic();
		NoVO noVoid = noVoid();
		noMain.getFilhos().add(noVoid);
		NoVO main = noMain();
		noVoid.getFilhos().add(main);
		NoVO noAbreParenteses = abreParenteses();
		noMain.getFilhos().add(noAbreParenteses);
		NoVO noString = string();
		noAbreParenteses.getFilhos().add(noString);
		NoVO noAbreColchete = abreColchete();
		noString.getFilhos().add(noAbreColchete);
		NoVO noFechaColchete = fechaColchete();
		noAbreColchete.getFilhos().add(noFechaColchete);
		NoVO noID = id();
		noFechaColchete.getFilhos().add(noID);
		NoVO noFechaParenteses = fechaParenteses();
		noID.getFilhos().add(noFechaParenteses);
		NoVO noDoisPontos = doisPontos();
		noFechaParenteses.getFilhos().add(noDoisPontos);
		
		NoVO declaraIDS = declaraIDS();
		noDoisPontos.getFilhos().add(declaraIDS);
		
		NoVO noListaCmd = listaCmd();
		noDoisPontos.getUltimoFilho().getFilhos().add(noListaCmd);
		numLinhaAtual = 29;
		NoVO noEndPontoVirgula = endPontoVirgula();
		noListaCmd.getUltimoFilho().getFilhos().add(noEndPontoVirgula);
		
		return noMain;
	}
	
	private NoVO listaFuncao() throws AnaliseSintaticaException {
		NoVO noFuncao = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDef = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaDefSemErro(tokenDef.getPalavraReservada())) {
			noFuncao = listaFuncaoL(noFuncao);
		}
		return noFuncao;
	}
	
	private NoVO listaFuncaoL(NoVO noPai) throws AnaliseSintaticaException {
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDef = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaDefSemErro(tokenDef.getPalavraReservada())) {
			NoVO noFuncao = funcao();
//			System.out.println(noFuncao);
			linha = getLinhaAtual();
			TokenVO proxToken = linha.getTokens().get(numTokenAtual-1);
			if (SintaticoHelper.isPalavraReservadaDefSemErro(proxToken.getPalavraReservada())) {
//				NoVO noFuncao2 = funcao();
//				System.out.println(noFuncao2);
//				if (noPai.getFilhos().isEmpty()) {
//					return noFuncao;
//				} else {
					noPai.getUltimoFilho().getFilhos().add(noFuncao);
//					return noPai;
//				}
			}
		}
		return noPai;
	}
	
//	if (SintaticoHelper.isPalavraReservadaTipoPrimitivo(proxToken.getPalavraReservada())) {
//		NoVO declaraIDL = declaraIDL(noArg);
//		if (noPai.getFilhos().isEmpty()) {
//			return declaraIDL;
//		} else {
//			noPai.getUltimoFilho().getFilhos().add(declaraIDL);
//			return noPai;
//		}
//	} else {
//		noPai.getUltimoFilho().getFilhos().add(noArg);
//	}
	
	private NoVO funcao() throws AnaliseSintaticaException {
		NoVO noFuncao = def();
			
		NoVO noTipoMacro = tipoMacro();
		noFuncao.getFilhos().add(noTipoMacro);
	
		NoVO noID = id();
		noTipoMacro.getFilhos().add(noID);
		NoVO noAbreParenteses = abreParenteses();
		noID.getFilhos().add(noAbreParenteses);

		NoVO noListaArg = listaArg();
		noAbreParenteses.getFilhos().add(noListaArg);
		
		NoVO noFechaParenteses = fechaParenteses();
		noListaArg.getUltimoFilho().getFilhos().add(noFechaParenteses);
		NoVO noDoisPontos = doisPontos();
		noFechaParenteses.getFilhos().add(noDoisPontos);
		
		NoVO noDeclaraID = declaraIDS();
		noDoisPontos.getFilhos().add(noDeclaraID);
		NoVO noListaCmd = listaCmd();
		noDeclaraID.getUltimoFilho().getFilhos().add(noListaCmd);
		
		numLinhaAtual = 9;
		numTokenAtual = 1; 
		NoVO noRetorno = retorno();
		noListaCmd.getFilhos().add(noRetorno);

		NoVO noEndPontoVirgula = endPontoVirgula();
		noRetorno.getUltimoFilho().getFilhos().add(noEndPontoVirgula);
		return noFuncao;
	}
	
	private NoVO criarNo(LinhaVO linha, TokenVO token) {
		NoVO no = new NoVO();
		no.setLinha(linha);
		no.getTokens().add(token);
//		numTokenAtual++;
		contarProximaLinhaToken(linha);
		return no;
	}
	
	private NoVO criarNo(LinhaVO linha, NoVO noPai, TokenVO tokenDEF) {
		NoVO noFilho = criarNo(linha, tokenDEF);
		noPai.getFilhos().add(noFilho);
		return noFilho;
	}
	
	private void contarProximaLinhaToken(LinhaVO linhaAtual) {
		if (linhaAtual.getTokens().size() > numTokenAtual) {
			numTokenAtual++;
		} else {
			numLinhaAtual++;
			numTokenAtual = 1;
		}
	}
	
	private NoVO defstatic() throws AnaliseSintaticaException {
		NoVO noDefstatic = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDefstatic = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaDefStatic(tokenDefstatic.getPalavraReservada())) {
			noDefstatic = criarNo(linha, tokenDefstatic);
		}
		return noDefstatic;
	}
	
	private NoVO noVoid() throws AnaliseSintaticaException {
		NoVO noVoid = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenVoid = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaVoid(tokenVoid.getPalavraReservada())) {
			noVoid = criarNo(linha, tokenVoid);
		}
		return noVoid;
	}
	
	private NoVO noMain() throws AnaliseSintaticaException {
		NoVO noMain = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenMain = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaMain(tokenMain.getPalavraReservada())) {
			noMain = criarNo(linha, tokenMain);
		}
		return noMain;
	}
	
	private NoVO noClass() throws AnaliseSintaticaException {
		NoVO noClass = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenClass = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaClass(tokenClass.getPalavraReservada())) {
			noClass = criarNo(linha, tokenClass);
		}
		return noClass;
	}
	
	private NoVO string() throws AnaliseSintaticaException {
		NoVO noString = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenString = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaString(tokenString.getPalavraReservada())) {
			noString = criarNo(linha, tokenString);
		}
		return noString;
	}
	
	private NoVO id() throws AnaliseSintaticaException {
		NoVO noID = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenID = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaID(tokenID.getPalavraReservada())) {
			noID = criarNo(linha, tokenID);
		}
		return noID;
	}
	
	private NoVO abreParenteses() throws AnaliseSintaticaException {
		NoVO noAbreParenteses = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenAbreParenteses = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaAbreParenteses(tokenAbreParenteses.getPalavraReservada())) {
			noAbreParenteses = criarNo(linha, tokenAbreParenteses);
		}
		return noAbreParenteses;
	}
	
	private NoVO fechaParenteses() throws AnaliseSintaticaException {
		NoVO noFechaParenteses = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenFechaParenteses = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaFechaParenteses(tokenFechaParenteses.getPalavraReservada())) {
			noFechaParenteses = criarNo(linha, tokenFechaParenteses);
		}
		return noFechaParenteses;
	}
	
	private NoVO abreColchete() throws AnaliseSintaticaException {
		NoVO noAbreColchete = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenAbreColchete = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaAbreColchete(tokenAbreColchete.getPalavraReservada())) {
			noAbreColchete = criarNo(linha, tokenAbreColchete);
		}
		return noAbreColchete;
	}
	
	private NoVO fechaColchete() throws AnaliseSintaticaException {
		NoVO noFechaParenteses = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenFechaParenteses = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaFechaColchete(tokenFechaParenteses.getPalavraReservada())) {
			noFechaParenteses = criarNo(linha, tokenFechaParenteses);
		}
		return noFechaParenteses;
	}
	
	private NoVO doisPontos() throws AnaliseSintaticaException {
		NoVO noDoisPontos = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDoisPontos = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaDoisPontos(tokenDoisPontos.getPalavraReservada())) {
			noDoisPontos = criarNo(linha, tokenDoisPontos);
		}
		return noDoisPontos;
	}
	
	private NoVO def() throws AnaliseSintaticaException {
		NoVO noDEF = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDEF = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaDef(tokenDEF.getPalavraReservada())) {
			noDEF = criarNo(linha, tokenDEF);
		}
		return noDEF;
	}
	
	private NoVO pontoVirgula() throws AnaliseSintaticaException {
		NoVO noPontoVirgula = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenPontoVirgula = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaPontoVirgula(tokenPontoVirgula.getPalavraReservada())) {
			noPontoVirgula = criarNo(linha, tokenPontoVirgula);
		}
		return noPontoVirgula;
	}
	
	private NoVO endPontoVirgula() throws AnaliseSintaticaException {
		NoVO noEnd = end();
		NoVO noPontoVirgula = pontoVirgula();
		noEnd.getFilhos().add(noPontoVirgula);
		return noEnd;
	}
	
	private NoVO endPonto() throws AnaliseSintaticaException {
		NoVO noEnd = end();
		NoVO noPonto = ponto();
		noEnd.getFilhos().add(noPonto);
		return noEnd;
	}
	
	private NoVO end() throws AnaliseSintaticaException {
		NoVO noEnd = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenEnd = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaEnd(tokenEnd.getPalavraReservada())) {
			noEnd = criarNo(linha, tokenEnd);
		}
		return noEnd;
	}
	
	private NoVO virgula() throws AnaliseSintaticaException {
		NoVO noVirgula = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenVirgula = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaVirgula(tokenVirgula.getPalavraReservada())) {
			noVirgula = criarNo(linha, tokenVirgula);
		}
		return noVirgula;
	}
	
	private NoVO ponto() throws AnaliseSintaticaException {
		NoVO noPonto = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenPonto = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaPonto(tokenPonto.getPalavraReservada())) {
			noPonto = criarNo(linha, tokenPonto);
		}
		return noPonto;
	}
	
	private NoVO noReturn() throws AnaliseSintaticaException {
		NoVO noReturn = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenReturn = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaReturn(tokenReturn.getPalavraReservada())) {
			noReturn = criarNo(linha, tokenReturn);
		}
		return noReturn;
	}
	
	private NoVO retorno() throws AnaliseSintaticaException {
		NoVO noRetorno = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenReturn = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaReturnSemErro(tokenReturn.getPalavraReservada())) {
			noRetorno = noReturn();
			
			NoVO noExpressao = expressao();
			noRetorno.getFilhos().add(noExpressao);
			
			linha = getLinhaAtual();
			TokenVO tokenPontoVirgula = linha.getTokens().get(numTokenAtual-1);
			if (SintaticoHelper.isPalavraReservadaPontoVirgula(tokenPontoVirgula.getPalavraReservada())) {
				NoVO noPontoVirgula = pontoVirgula();
				noExpressao.getFilhos().add(noPontoVirgula);
			}
		}
		return noRetorno;
	}
	
	private NoVO expressao() {
		NoVO noExpressao = new NoVO();
		
		contarProximaLinhaToken(getLinhaAtual());
		
		return noExpressao;
	}
	
	private NoVO expressaoL() {
		return null;
	}

	private NoVO listaCmd() {
		NoVO noListaCmd = new NoVO();
		return noListaCmd;
	}
	
	private NoVO declaraIDS() throws AnaliseSintaticaException {
		NoVO noDeclaraIDS = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(tokenTipoPrimitivo.getPalavraReservada())) {
			noDeclaraIDS = declaraIDL(noDeclaraIDS);
		}
		return noDeclaraIDS;
	}
	
	private NoVO declaraIDL(NoVO noPai) throws AnaliseSintaticaException {
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaTipoPrimitivo(tokenTipoPrimitivo.getPalavraReservada())) {
			NoVO noArg = arg();
			linha = getLinhaAtual();
			TokenVO tokenPontoVirgula = linha.getTokens().get(numTokenAtual-1);
			if (SintaticoHelper.isPalavraReservadaPontoVirgula(tokenPontoVirgula.getPalavraReservada())) {
				NoVO noPontoVirgula = pontoVirgula();
				noArg.getUltimoFilho().getFilhos().add(noPontoVirgula);
				linha = getLinhaAtual();
				TokenVO proxToken = linha.getTokens().get(numTokenAtual-1);
				if (SintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(proxToken.getPalavraReservada())) {
					NoVO declaraIDL = declaraIDL(noArg);
					if (noPai.getFilhos().isEmpty()) {
						return declaraIDL;
					} else {
						noPai.getUltimoFilho().getFilhos().add(declaraIDL);
						return noPai;
					}
				} else {
					noPai.getUltimoFilho().getFilhos().add(noArg);
				}
			}
		}
		return noPai;
	}

	private NoVO listaArg() throws AnaliseSintaticaException {
		NoVO noListaArg = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(tokenTipoPrimitivo.getPalavraReservada())) {
			noListaArg = arg();
			linha = getLinhaAtual();
			TokenVO tokenVirgula = linha.getTokens().get(numTokenAtual-1);
			if (SintaticoHelper.isPalavraReservadaVirgulaSemErro(tokenVirgula.getPalavraReservada())) {
				noListaArg = listaArgL(noListaArg);
			}
		}
		return noListaArg;
	}
	
	private NoVO listaArgL(NoVO noPai) throws AnaliseSintaticaException {
		NoVO noVirgula = virgula();
		NoVO noArg = arg();
		noVirgula.getFilhos().add(noArg);
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenVirgula = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaVirgulaSemErro(tokenVirgula.getPalavraReservada())) {
			NoVO noListaArgL = listaArgL(noVirgula);
			noPai.getUltimoFilho().getFilhos().add(noListaArgL);
			return noPai;
		} else {
			noPai.getUltimoFilho().getFilhos().add(noVirgula);
			return noPai;
		}
	}
	
	private NoVO arg() throws AnaliseSintaticaException {
		NoVO noArg = tipoMacro();
		NoVO noID = id();
		noArg.getFilhos().add(noID);
		return noArg;
	}
	
	private NoVO tipoMacro() throws AnaliseSintaticaException {
		LinhaVO linha = getLinhaAtual();
		NoVO noTipoMacro = tipoPrimitivo();
		TokenVO tokenAbreColchete = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaAbreColcheteSemErro(tokenAbreColchete.getPalavraReservada())) {
//			noTipoMacro.getTokens().add(tokenAbreColchete);
//			numTokenAtual++;
			NoVO noAbreColchete = criarNo(linha, noTipoMacro, tokenAbreColchete);
			linha = getLinhaAtual();
			TokenVO tokenFechaColchete = linha.getTokens().get(numTokenAtual-1);
			if (SintaticoHelper.isPalavraReservadaFechaColchete(tokenFechaColchete.getPalavraReservada())) {
//				noTipoMacro.getTokens().add(tokenFechaColchete);
//				numTokenAtual++;
				/*NoVO noFechaColchete = */criarNo(linha, noAbreColchete, tokenFechaColchete);
				
			}
		}
		return noTipoMacro;
	}
	
	private NoVO tipoPrimitivo() throws AnaliseSintaticaException {
		NoVO noTipoPrimitivo = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaTipoPrimitivo(tokenTipoPrimitivo.getPalavraReservada())) {
			noTipoPrimitivo = criarNo(linha, tokenTipoPrimitivo);
			noTipoPrimitivo.setTipoExpressao(TipoExpressao.TIPO_PRIMITIVO);
		}
		return noTipoPrimitivo;
	}
	
	private LinhaVO getLinhaAtual() {
		return getLinha(numLinhaAtual);
	}
	
	private LinhaVO getLinha(int numLinhaAtual) {
		LinhaVO linha = null;
		for (LinhaVO l : arquivo.getLinhas()) {
			if (l.getNumero() == numLinhaAtual) {
				linha = l;
				break;
			}
		}
		return linha;
	}
	
	//HELPER
	public static class SintaticoHelper {
		
		private static final int MAX_ERROS = 5;
		private static int errosCount = 0;
		private static String errors = "";
		
		private static List<PalavraReservada> tiposPrimitivos = Arrays.asList(new PalavraReservada[]{
			PalavraReservada.BOOL,
			PalavraReservada.INTEGER,
			PalavraReservada.STRING,
			PalavraReservada.DOUBLE,
			PalavraReservada.VOID
		});
		
//		private static PalavraReservada[] sequenciaClasse = new PalavraReservada[]{
//			PalavraReservada.CLASS,
//			PalavraReservada.ID,
//			PalavraReservada.DOIS_PONTOS
//		};
		
		private static ArquivoVO getArquivoExecutavel(ArquivoVO arquivo) {
			ArquivoVO arquivoExecutavel = new ArquivoVO();
			int numLinha = 1;
			for (LinhaVO linhaVO : arquivo.getLinhas()) {
				List<TokenVO> tokensLinha = new ArrayList<>();
				String conteudo = "";
				for (TokenVO tokenVO : linhaVO.getTokens()) {
					if (!SintaticoHelper.isComentario(tokenVO)){
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
		
		private static List<TokenVO> getTokensAExecutar(ArquivoVO arquivo) {
			List<TokenVO> tokensAExecutar = new ArrayList<>();
			for (LinhaVO linha : arquivo.getLinhas()) {
				for (TokenVO tokenVO : linha.getTokens()) {
					if (!SintaticoHelper.isComentario(tokenVO)) {
						tokensAExecutar.add(tokenVO);
					}
				}
			}
			return tokensAExecutar;
		}
		
//		private static TokenVO getTokenPorLinha(LinhaVO linha, int numeroLinha) {
//			if (!linha.getTokens().isEmpty() &&
//					linha.getTokens().size() >= numeroLinha) {
//				return linha.getTokens().get(numeroLinha);
//			}
//			return null;
//		}
		
//		private static boolean isExpressaoClasseValida(LinhaVO linha, String mensagemValidacao) throws AnaliseSintaticaException {
//			if (isExpressaoClasseValida(linha)) {
//				return true;
//			}
//			erro(mensagemValidacao);
//			return false;
//		}
		
//		private static boolean isExpressaoClasseValida(LinhaVO linha) {
//			boolean valido = true;
//			if (sequenciaClasse.length != linha.getTokens().size()) {
//				valido = false;
//			} else { 
//				for(int i = 1; i < sequenciaClasse.length; i++) {
//					PalavraReservada palavraLinha = getTokenPorLinha(linha, i).getPalavraReservada();
//					PalavraReservada palavraEsperada = sequenciaClasse[i];
//					if (!palavraEsperada.equals(palavraLinha)) {
//						valido = false;
//						break;
//					}
//				}
//			}
//			return valido;
//		}
		
		private static boolean isPalavraReservadaPonto(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.PONTO, palavra, "esperava ponto final");
		}
		
		private static boolean isPalavraReservadaClass(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.CLASS, palavra, "esperava class");
		}
		
		private static boolean isPalavraReservadaString(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.STRING, palavra, "esperava string");
		}
		
		private static boolean isPalavraReservadaReturn(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.RETURN, palavra, "funcao esperava return");
		}
		
		private static boolean isPalavraReservadaPontoVirgula(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.PONTO_VIRGULA, palavra, "funcao espera ponto virgula dps de end");
		}
		
		private static boolean isPalavraReservadaVirgula(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.VIRGULA, palavra, "funcao esperava token virgula");
		}
		
		private static boolean isPalavraReservadaEnd(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.END, palavra, "funcao espera end dps de retorno");
		}
		
		private static boolean isPalavraReservadaDoisPontos(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.DOIS_PONTOS, palavra, "funcao espera dois pontos dps de fecha parenteses");
		}
		
		private static boolean isPalavraReservadaAbreParenteses(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.ABRE_PARENTESES, palavra, "funcao espera abre parenteses dps do id");
		}
		
		private static boolean isPalavraReservadaFechaParenteses(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.FECHA_PARENTESES, palavra, "funcao espera fecha parenteses dps de lista arg");
		}
		
		private static boolean isPalavraReservadaAbreColchete(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.ABRE_COLCHETE, palavra, "Esperava abre colchete");
		}
		
		private static boolean isPalavraReservadaFechaColchete(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.FECHA_COLCHETE, palavra, "Esperava fecha colchete");
		}
		
		private static boolean isPalavraReservadaDef(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.DEF, palavra, "funcao deve iniciar com def");
		}
		
		private static boolean isPalavraReservadaDefStatic(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.DEFSTATIC, palavra, "funcao main deve iniciar com defstatic");
		}
		
		private static boolean isPalavraReservadaVoid(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.VOID, palavra, "esperava void");
		}
		
		private static boolean isPalavraReservadaMain(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.MAIN, palavra, "esperava main");
		}
		
//		private boolean isPalavraReservadaID(PalavraReservada palavra) throws AnaliseSintaticaException {
//			return isPalavraReservada(PalavraReservada.ID, palavra, "funcao espera id dps de tipo macro");
//		}
		
		private static boolean isPalavraReservadaAbreColcheteSemErro(PalavraReservada palavra) {
			return isPalavraReservadaSemErro(PalavraReservada.ABRE_COLCHETE, palavra);
		}
		
		private static boolean isPalavraReservadaDefSemErro(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservadaSemErro(PalavraReservada.DEF, palavra);
		}
		
		private static boolean isPalavraReservadaReturnSemErro(PalavraReservada palavra) {
			return isPalavraReservadaSemErro(PalavraReservada.RETURN, palavra);
		}
		
		private static boolean isPalavraReservadaVirgulaSemErro(PalavraReservada palavra) {
			return isPalavraReservadaSemErro(PalavraReservada.VIRGULA, palavra);
		}
		
		private static boolean isPalavraReservadaSemErro(PalavraReservada palavraReservada, PalavraReservada palavraAtual) {
			return palavraReservada.equals(palavraAtual);
		}
		
		private static boolean isPalavraReservadaTipoPrimitivoSemErro(PalavraReservada palavra) {
			return tiposPrimitivos.contains(palavra);
		}
		
		private static boolean isPalavraReservada(PalavraReservada palavraReservada, PalavraReservada palavraAtual, String mensagemValidacao) 
				throws AnaliseSintaticaException {
			if (palavraReservada.equals(palavraAtual)) {
				return true;
			} 
			erro(mensagemValidacao);
			return false;
		}
		
		private static boolean isPalavraReservadaTipoPrimitivo(PalavraReservada palavra, String mensagemValidacao) 
				throws AnaliseSintaticaException {
			if (tiposPrimitivos.contains(palavra)) {
				return true;
			}
			erro(mensagemValidacao);
			return false;
		}
		
		private static boolean isPalavraReservadaTipoPrimitivo(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservadaTipoPrimitivo(palavra, "Não é um tipo primitivo");
		}
		
		private static boolean isComentario(TokenVO tokenVO) {
			return PalavraReservada.COMENTARIO_GERAL.equals(tokenVO.getPalavraReservada()) ||
					PalavraReservada.COMENTARIO_LINHA.equals(tokenVO.getPalavraReservada());
		}
		
		private static boolean isPalavraReservadaID(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.ID, palavra, "funcao espera id dps de tipo macro");
		}
		
		private static void erro(String mensagem) throws AnaliseSintaticaException {
			errosCount++;
			errors += mensagem+"\n";
			System.out.println(String.format("Erro %s: %s",errosCount,mensagem));
			if (errosCount >= MAX_ERROS) {
				throw new AnaliseSintaticaException(errors);
			}
		}
		
	}

}
