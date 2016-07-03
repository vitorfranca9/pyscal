package br.unibh.pyscal.analisador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.unibh.pyscal.enumerador.PalavraReservadaEnum;
import br.unibh.pyscal.enumerador.PalavraReservadaEnum.PalavraReservadaHelper;
import br.unibh.pyscal.enumerador.TipoExpressaoEnum;
import br.unibh.pyscal.exception.AnaliseSintaticaException;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.NoVO;
import br.unibh.pyscal.vo.TokenVO;
import java_cup.runtime.token;

@SuppressWarnings("unused")
public class AnalisadorSintatico extends AnalisadorAbstrato {

	private AnalisadorSintaticoHelper sintaticoHelper;
	
//	public static Comparator<TokenVO> compararPorOrdem = (t1, t2) -> 
//		t1.getPalavraReservada().getOrdem().compareTo(t2.getPalavraReservada().getOrdem()
//	);
	
	@Override
	protected void init() {
		super.init();
		sintaticoHelper = AnalisadorSintaticoHelper.getInstancia();
		sintaticoHelper.errors = "";
		sintaticoHelper.errosCount = 0;
	}

	public void analisar(ArquivoVO arquivo) throws AnaliseSintaticaException {
		init();
		ArquivoVO arquivoExecutavel = sintaticoHelper.getArquivoExecutavel(arquivo);
		this.arquivo = arquivoExecutavel;
		compilar();
	}
	
	private void compilar() throws AnaliseSintaticaException {
		NoVO noClasse = classe();
		NoVO noFuncao = listaFuncao();
		NoVO noMain = main();
		NoVO noEndPonto = endPonto();
		if (!noFuncao.getFilhos().isEmpty()) {
			noClasse.getFilhos().add(noFuncao);
		}
		noClasse.getFilhos().add(noMain);
		arquivo.setNoRaiz(noClasse);
		System.out.println();
//		FileUtil.imprimirAST(arquivo);
	}
	
	private NoVO classe() throws AnaliseSintaticaException {
		NoVO noClasse = null;
		NoVO noClass = noClass();
		NoVO noID = id();
		NoVO noDoisPontos = doisPontos();
		noClasse = ordenarClasse(noClass,noID);
		return noClasse;
	}
	
	private NoVO ordenarClasse(NoVO noClass, NoVO noID) {
		NoVO noClasse = noID;
		noClasse.getFilhos().add(noClass);
		return noClasse;
	}
	
	/*
	 * if pode ter 2 ou 3 filhos;
	 * if,then else
	 * atribuicao(+) - pai
	 */
	private NoVO listaFuncao() throws AnaliseSintaticaException {
		NoVO noFuncao = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDef = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaDefSemErro(tokenDef.getPalavraReservada())) {
			noFuncao = listaFuncaoL(noFuncao);
		}
		return noFuncao;
	}
	
	private NoVO listaFuncaoL(NoVO noPai) throws AnaliseSintaticaException {
		NoVO noFuncao = funcao();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDef = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaDefSemErro(tokenDef.getPalavraReservada())) {
			NoVO noListaFuncao = listaFuncaoL(noFuncao);
			if (noListaFuncao.getFilhos().isEmpty()) {
				noPai.getUltimoFilho().getFilhos().add(noFuncao);
				return noPai;
			} else {
				if (noFuncao.equals(noListaFuncao)) {
					return noFuncao;
				} else {
					noFuncao.getUltimoFilho().getFilhos().add(noListaFuncao);
					return noFuncao;
				}
			}
		}
		return noFuncao;
	}//
	
	private NoVO funcao() throws AnaliseSintaticaException {
		NoVO noFuncao = new NoVO(); 
		NoVO def = def();
		NoVO noTipoMacro = tipoMacro();
		NoVO noID = id();
		NoVO noAbreParenteses = abreParenteses();
		NoVO noListaArg = listaArg();
		NoVO noFechaParenteses = fechaParenteses();
		NoVO noDoisPontos = doisPontos();
		NoVO noDeclaraID = declaraIDS();
		NoVO noListaCmd = listaCmd();
		NoVO noRetorno = retorno();
		NoVO noEndPontoVirgula = endPontoVirgula();
		noFuncao = noID;
		noFuncao.getFilhos().add(def);
		if (!noListaArg.getFilhos().isEmpty()) {
			noFuncao.getFilhos().addAll(noListaArg.getFilhos());
		}
		if (!noListaCmd.getFilhos().isEmpty()) {
			noFuncao.getFilhos().addAll(noListaCmd.getFilhos());
		}
		if (!noRetorno.getFilhos().isEmpty()) {
			noFuncao.getFilhos().add(noRetorno);
		}
		return noFuncao;
	}
	
	private NoVO main() throws AnaliseSintaticaException {
		NoVO noMain = null; 
		NoVO defstatic = defstatic();
		NoVO noVoid = noVoid();
		NoVO main = noMain();
		NoVO noAbreParenteses = abreParenteses();
		NoVO noString = string();
		NoVO noAbreColchete = abreColchete();
		NoVO noFechaColchete = fechaColchete();
		NoVO noID = id();
		NoVO noFechaParenteses = fechaParenteses();
		NoVO noDoisPontos = doisPontos();
		NoVO declaraIDS = declaraIDS();
		NoVO noListaCmd = listaCmd();
		NoVO noEndPontoVirgula = endPontoVirgula();
		noMain = ordenarMain(defstatic, main, noID, declaraIDS, noListaCmd);
		return noMain;
	}
	
	private NoVO ordenarMain(NoVO defstatic, NoVO main, NoVO args, NoVO declaraIDS, NoVO noListaCmd) {
		NoVO noMain = main;
		noMain.getFilhos().add(defstatic);
		noMain.getFilhos().add(args);
		if (!noListaCmd.getFilhos().isEmpty()) {
			noMain.getFilhos().addAll(noListaCmd.getFilhos());
		}
		return noMain;
	}
	
	private NoVO declaraIDS() throws AnaliseSintaticaException {
		NoVO noDeclaraIDS = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(tokenTipoPrimitivo.getPalavraReservada())) {
			noDeclaraIDS = declaraIDL(noDeclaraIDS);
		}
//		noDeclaraIDS = ordenaDeclaraIDS(noDeclaraIDS);
		return noDeclaraIDS;
	}
//	private NoVO ordenaDeclaraIDS(NoVO declaraIDS) {
//		NoVO declaraIDSOrdenado = new NoVO();
//		while (!declaraIDS.getFilhos().isEmpty()) {
//			NoVO filho = declaraIDS.getFilhos().get(0);
//			NoVO neto = filho.getFilhos().get(0);
//		}
//		return null;
//	}

	private NoVO declaraIDL(NoVO noPai) throws AnaliseSintaticaException {
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaTipoPrimitivo(linha, tokenTipoPrimitivo)) {
			NoVO noArg = arg();
			linha = getLinhaAtual();
			TokenVO tokenPontoVirgula = getTokenAtual();
			if (sintaticoHelper.isPalavraReservadaPontoVirgula(linha, tokenPontoVirgula)) {
				NoVO noPontoVirgula = pontoVirgula();
				noArg.getUltimoFilho().getFilhos().add(noPontoVirgula);
				linha = getLinhaAtual();
				TokenVO proxToken = getTokenAtual();
				if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(proxToken.getPalavraReservada())) {
					NoVO declaraIDL = declaraIDL(noArg);
					if (noPai.getFilhos().isEmpty()) {
						return declaraIDL;
					} else {
						noPai.getUltimoFilho().getFilhos().add(declaraIDL);
						return noPai;
					}
				} else {
					noPai = noArg;
				}
			}
		}
		return noPai;
	}
	
	private NoVO arg() throws AnaliseSintaticaException {
		NoVO noArg = tipoMacro();
		NoVO noID = id();
		noArg = noID;
		return noArg;
	}
	
	private NoVO retorno() throws AnaliseSintaticaException {
		NoVO noRetorno = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenReturn = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaReturnSemErro(tokenReturn.getPalavraReservada())) {
			noRetorno = noReturn();
			NoVO noExpressao = expressao();
			linha = getLinhaAtual();
			TokenVO tokenPontoVirgula = getTokenAtual();
			NoVO noPontoVirgula = pontoVirgula();
			noRetorno = noExpressao;
		}
		return noRetorno;
	}
	
	private NoVO tipoMacro() throws AnaliseSintaticaException {
		LinhaVO linha = getLinhaAtual();
		NoVO noTipoMacro = tipoPrimitivo();
		TokenVO tokenAbreColchete = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaAbreColcheteSemErro(tokenAbreColchete.getPalavraReservada())) {
			NoVO noAbreColchete = criarNo(linha, noTipoMacro, tokenAbreColchete);
			linha = getLinhaAtual();
			TokenVO tokenFechaColchete = getTokenAtual();
			if (sintaticoHelper.isPalavraReservadaFechaColchete(linha, tokenFechaColchete)) {
				criarNo(linha, noAbreColchete, tokenFechaColchete);
			}
		}
		return noTipoMacro;
	}
	
	private NoVO tipoPrimitivo() throws AnaliseSintaticaException {
		NoVO noTipoPrimitivo = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaTipoPrimitivo(linha, tokenTipoPrimitivo)) {
			noTipoPrimitivo = criarNo(linha, tokenTipoPrimitivo);
			noTipoPrimitivo.setTipoExpressao(TipoExpressaoEnum.TIPO_PRIMITIVO);
		}
		return noTipoPrimitivo;
	}
	
	private NoVO criarNo(LinhaVO linha, TokenVO token) {
		NoVO no = new NoVO();
		no.setLinha(linha);
		no.getTokens().add(token);
//		imprimir(no);
		contarProximoToken(linha);
		return no;
	}
	
	private void imprimir(NoVO no) {
		System.out.println(//"Nivel:"+no.getNivel()+
			" Linha:"+no.getLinha().getNumero()+" No:'"+no.getTokens().get(0)+"'");
	}
	
	private NoVO criarNo(LinhaVO linha, NoVO noPai, TokenVO tokenDEF) {
		NoVO noFilho = criarNo(linha, tokenDEF);
		noPai.getFilhos().add(noFilho);
		return noFilho;
	}
	
	private NoVO defstatic() throws AnaliseSintaticaException {
		NoVO noDefstatic = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDefstatic = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaDefStatic(linha, tokenDefstatic)) {
			noDefstatic = criarNo(linha, tokenDefstatic);
		}
		return noDefstatic;
	}
	
	private NoVO noVoid() throws AnaliseSintaticaException {
		NoVO noVoid = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenVoid = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaVoid(linha, tokenVoid)) {
			noVoid = criarNo(linha, tokenVoid);
		}
		return noVoid;
	}
	
	private NoVO noMain() throws AnaliseSintaticaException {
		NoVO noMain = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenMain = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaMain(linha, tokenMain)) {
			noMain = criarNo(linha, tokenMain);
		}
		return noMain;
	}
	
	private NoVO noClass() throws AnaliseSintaticaException {
		NoVO noClass = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenClass = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaClass(linha, tokenClass)) {
			noClass = criarNo(linha, tokenClass);
		}
		return noClass;
	}
	
	private NoVO string() throws AnaliseSintaticaException {
		NoVO noString = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenString = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaString(linha, tokenString)) {
			noString = criarNo(linha, tokenString);
		}
		return noString;
	}
	
	private NoVO id() throws AnaliseSintaticaException {
		NoVO noID = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenID = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaID(linha, tokenID)) {
			noID = criarNo(linha, tokenID);
		}
		return noID;
	}
	
	private NoVO abreParenteses() throws AnaliseSintaticaException {
		NoVO noAbreParenteses = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenAbreParenteses = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaAbreParenteses(linha, tokenAbreParenteses)) {
			noAbreParenteses = criarNo(linha, tokenAbreParenteses);
		}
		return noAbreParenteses;
	}
	
	private NoVO fechaParenteses() throws AnaliseSintaticaException {
		NoVO noFechaParenteses = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenFechaParenteses = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaFechaParenteses(linha, tokenFechaParenteses)) {
			noFechaParenteses = criarNo(linha, tokenFechaParenteses);
		}
		return noFechaParenteses;
	}
	
	private NoVO abreColchete() throws AnaliseSintaticaException {
		NoVO noAbreColchete = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenAbreColchete = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaAbreColchete(linha, tokenAbreColchete)) {
			noAbreColchete = criarNo(linha, tokenAbreColchete);
		}
		return noAbreColchete;
	}
	
	private NoVO fechaColchete() throws AnaliseSintaticaException {
		NoVO noFechaParenteses = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenFechaParenteses = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaFechaColchete(linha, tokenFechaParenteses)) {
			noFechaParenteses = criarNo(linha, tokenFechaParenteses);
		}
		return noFechaParenteses;
	}
	
	private NoVO doisPontos() throws AnaliseSintaticaException {
		NoVO noDoisPontos = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDoisPontos = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaDoisPontos(linha, tokenDoisPontos)) {
			noDoisPontos = criarNo(linha, tokenDoisPontos);
		}
		return noDoisPontos;
	}
	
	private NoVO def() throws AnaliseSintaticaException {
		NoVO noDEF = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDEF = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaDef(linha, tokenDEF)) {
			noDEF = criarNo(linha, tokenDEF);
		}
		return noDEF;
	}
	
	private NoVO pontoVirgula() throws AnaliseSintaticaException {
		NoVO noPontoVirgula = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenPontoVirgula = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaPontoVirgula(linha, tokenPontoVirgula)) {
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
		TokenVO tokenEnd = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaEnd(linha, tokenEnd)) {
			noEnd = criarNo(linha, tokenEnd);
		}
		return noEnd;
	}
	
	private NoVO virgula() throws AnaliseSintaticaException {
		NoVO noVirgula = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenVirgula = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaVirgula(linha, tokenVirgula)) {
			noVirgula = criarNo(linha, tokenVirgula);
		}
		return noVirgula;
	}
	
	private NoVO ponto() throws AnaliseSintaticaException {
		NoVO noPonto = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenPonto = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaPonto(linha, tokenPonto)) {
			noPonto = criarNo(linha, tokenPonto);
		}
		return noPonto;
	}
	
	private NoVO igual() throws AnaliseSintaticaException {
		NoVO igual = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenIgual = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaIgual(linha, tokenIgual)) {
			igual = criarNo(linha, tokenIgual);
		}
		return igual;
	}
	
	private NoVO noReturn() throws AnaliseSintaticaException {
		NoVO noReturn = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenReturn = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaReturn(linha, tokenReturn)) {
			noReturn = criarNo(linha, tokenReturn);
		}
		return noReturn;
	}
	
	private NoVO expressao() throws AnaliseSintaticaException {
		NoVO noExpressao = expressaoL();
		return noExpressao;
	}
	
	private NoVO expressaoL() throws AnaliseSintaticaException {
		NoVO noExpressao = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenExpressao = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaConstanteSemErro(tokenExpressao.getPalavraReservada())) {
			noExpressao = expressaoConst();
			linha = getLinhaAtual();
			TokenVO tokenOp = getTokenAtual();
			if (sintaticoHelper.isPalavraReservadaOpSemErro(tokenOp.getPalavraReservada())) {
				NoVO op = op();
				noExpressao.getFilhos().add(op);
				NoVO expressaoL = expressaoL();
				op.getFilhos().add(expressaoL);
			} else if (isTokenValorNegativo(tokenOp)){
				String valorTokenValor = tokenOp.getValor().substring(1, tokenOp.getValor().length());
				tokenOp.setValor(tokenOp.getValor().substring(0, 1));
				TokenVO tokenValor = new TokenVO();
				tokenValor.setValor(valorTokenValor);
				tokenValor.setPalavraReservada(tokenOp.getPalavraReservada());
				tokenOp.setPalavraReservada(PalavraReservadaEnum.SUBTRAIR);
				linha.getTokens().add(numLinhaAtual, tokenValor);
				NoVO op = op();
				noExpressao.getFilhos().add(op);
				NoVO expressaoL = expressaoL();
				op.getFilhos().add(expressaoL);
			}
		} else if (sintaticoHelper.isPalavraReservadaOpUnarioSemErro(tokenExpressao.getPalavraReservada())) {
			noExpressao = expressaoOpUnario();
			linha = getLinhaAtual();
			TokenVO tokenOp = getTokenAtual();
			if (sintaticoHelper.isPalavraReservadaOpSemErro(tokenOp.getPalavraReservada())) {
				NoVO op = op();
				noExpressao.getUltimoFilho().getFilhos().add(op);
				NoVO expressaoL = expressaoL();
				op.getFilhos().add(expressaoL);
			} 
		} else if (sintaticoHelper.isPalavraReservadaIDSemErro(tokenExpressao.getPalavraReservada())) {
			noExpressao = expressaoID();
			linha = getLinhaAtual();
			TokenVO tokenOp = getTokenAtual();
			if (sintaticoHelper.isPalavraReservadaOpSemErro(tokenOp.getPalavraReservada())) {
				NoVO op = op();
				noExpressao.getUltimoFilho().getFilhos().add(op);
				NoVO expressaoL = expressaoL();
				op.getFilhos().add(expressaoL);
			} 
		} else if (sintaticoHelper.isPalavraReservadaVectorSemErro(tokenExpressao.getPalavraReservada())) {
			noExpressao = expressaoVector();
			linha = getLinhaAtual();
			TokenVO tokenOp = getTokenAtual();
			if (sintaticoHelper.isPalavraReservadaOpSemErro(tokenOp.getPalavraReservada())) {
				NoVO op = op();
				noExpressao.getUltimoFilho().getFilhos().add(op);
				NoVO expressaoL = expressaoL();
				op.getFilhos().add(expressaoL);
			} 
		} else if (sintaticoHelper.isPalavraReservadaAbreParentesesSemErro(tokenExpressao.getPalavraReservada())) {
			noExpressao = abreParenteses();
			NoVO expressao = expressaoL();
			noExpressao.getFilhos().add(expressao);
			NoVO fechaParenteses = fechaParenteses();
			noExpressao.getUltimoFilho().getFilhos().add(fechaParenteses);
			linha = getLinhaAtual();
			TokenVO tokenOp = getTokenAtual();
			if (sintaticoHelper.isPalavraReservadaOpSemErro(tokenOp.getPalavraReservada())) {
				NoVO op = op();
				noExpressao.getUltimoFilho().getFilhos().add(op);
				NoVO expressao2 = expressaoL();
				op.getFilhos().add(expressao2);
			} 
		} /*else if (sintaticoHelper.isPalavraReservadaPontoVirgulaSemErro(tokenExpressao.getPalavraReservada())) {
			
		}*/
		return noExpressao;
	}
	
	//TODO
	private NoVO ordenarExpressao(NoVO noExpressao) {
		NoVO noOrdenado = new NoVO();
		if (!noExpressao.getFilhos().isEmpty()) {
			List<NoVO> nos = new ArrayList<>();
			try {
				NoVO noExpressaoAux = (NoVO) noExpressao.clone();
				noExpressaoAux.setFilhos(new ArrayList<NoVO>());
				nos.add(noExpressaoAux);
				while (!noExpressao.getFilhos().isEmpty()) {
					NoVO noExpressaoAux2 = (NoVO)noExpressao.getFilhos().get(0).clone();
					noExpressaoAux2.setFilhos(new ArrayList<NoVO>());
					nos.add(noExpressaoAux2);
					noExpressao = noExpressao.getFilhos().get(0);
				}
				noOrdenado = ordenarExpressao(nos);
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			noOrdenado = noExpressao;
		}
		
		return noOrdenado;
	}
	
	private List<NoVO> getNos(NoVO no) {
		List<NoVO> nos = new ArrayList<>();
		try {
			NoVO noAux = (NoVO) no.clone();
			noAux.setFilhos(new ArrayList<NoVO>());
			nos.add(noAux);
			while (!no.getFilhos().isEmpty()) {
				NoVO noExpressaoAux2 = (NoVO)no.getFilhos().get(0).clone();
				noExpressaoAux2.setFilhos(new ArrayList<NoVO>());
				nos.add(noExpressaoAux2);
				no = no.getFilhos().get(0);
			}
			return nos;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private int nivel = 0;
	private NoVO ordenarExpressao(List<NoVO> nos) {
		NoVO noOrdenado = new NoVO();
		NoVO noAux = null;
		List<NoVO> nosAux = new ArrayList<>();
		List<TokenVO> tokens = new ArrayList<>();
			
		for (NoVO no : nos) {
			if (!isOperador(no.getTokens().get(0))) {
				noAux = no;
			} else {
				no.getFilhos().add(noAux);
				nosAux.add(no);
				noAux = null;
			}
			tokens.add(no.getTokens().get(0));
		}
		if (noAux != null) {
			nosAux.add(noAux);
		}
		
		noOrdenado = ordenarTokens(tokens);
		noOrdenado = ordenarNos(nosAux);
		return noOrdenado;
	}
	
	private NoVO ordenarTokens(List<TokenVO> tokens) {
		NoVO no = new NoVO();
		TokenVO token = new TokenVO();
		for (int i = 0; i < tokens.size(); i++) {
			TokenVO tokenVO = tokens.get(i);
			System.out.println(tokenVO);
		}
		return no;
	}
	
	private NoVO ordenarNos(List<NoVO> nos) {
		NoVO no = new NoVO();
		
		for (int i = 0; i < nos.size(); i++) {
			NoVO noVO = nos.get(i);
			System.out.println(noVO);
		}
		
//		Collections.sort(nos, (o1, o2) -> 
//			o1.getTokens().get(0).getPalavraReservada()
//				.compareTo(o2.getTokens().get(0).getPalavraReservada()));
//		no.getFilhos().addAll(nos);
		return no;
	}
	
	private boolean isOperador(TokenVO tokenVO) {
		return sintaticoHelper.op.contains(tokenVO.getPalavraReservada())
			|| sintaticoHelper.opUnario.contains(tokenVO.getPalavraReservada());
	}
	
	//TODO CONST_DOUBLE ou CONST_INTEGER com valor negativo - tratar?
	private boolean isTokenValorNegativo(TokenVO token) {
		if (sintaticoHelper.isPalavraReservadaConstIntegerSemErro(token.getPalavraReservada()) ||
				sintaticoHelper.isPalavraReservadaConstDoubleSemErro(token.getPalavraReservada())) {
			return PalavraReservadaHelper.isSubtrair(token.getValor().substring(0,1));
		}
		return false;
	}
//	private NoVO expressaoL() throws AnaliseSintaticaException {
//		NoVO noExpressao = new NoVO();
//		LinhaVO linha = getLinhaAtual();
//		TokenVO tokenExpressao = getTokenAtual();
//		if (sintaticoHelper.isPalavraReservadaConstanteSemErro(tokenExpressao.getPalavraReservada())) {
//			noExpressao = expressaoConst();
//			linha = getLinhaAtual();
//			TokenVO tokenOp = getTokenAtual();
//			if (sintaticoHelper.isPalavraReservadaOpSemErro(tokenOp.getPalavraReservada())) {
//				NoVO op = op();
//				noExpressao.getFilhos().add(op);
//				NoVO expressao2 = expressaoL();
//				op.getFilhos().add(expressao2);
//			}
//		} else if (sintaticoHelper.isPalavraReservadaOpUnarioSemErro(tokenExpressao.getPalavraReservada())) {
//			NoVO opUnario = opUnario();
//			noExpressao.getFilhos().add(opUnario);
//			
//			
//		} else if (sintaticoHelper.isPalavraReservadaIDSemErro(tokenExpressao.getPalavraReservada())) {
//			noExpressao = expressaoID();
//		}
//		
//		return noExpressao;
//	}
	
	private NoVO expressaoID() throws AnaliseSintaticaException {
		NoVO expressaoID = id();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenAbreColchete = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaAbreColcheteSemErro(tokenAbreColchete.getPalavraReservada())) {
			NoVO expressaoIDAbreColchete = expressaoIDAbreColchete();
		} else if (sintaticoHelper.isPalavraReservadaAbreParentesesSemErro(tokenAbreColchete.getPalavraReservada())) {
			NoVO expressaoIDAbreParenteses = expressaoIDAbreParenteses();
			expressaoID.getFilhos().addAll(expressaoIDAbreParenteses.getFilhos());
		}
		return expressaoID;
	}

	private NoVO expressaoIDAbreColchete() throws AnaliseSintaticaException {
		NoVO expressaoIDAbreColchete = abreColchete();
		NoVO expressao = expressao();
		expressaoIDAbreColchete.getFilhos().add(expressao);
		NoVO noFechaColchete = fechaColchete();
		expressao.getUltimoFilho().getFilhos().add(noFechaColchete);
		return expressaoIDAbreColchete;
	}
	
	private NoVO declaraExpressao(NoVO noPai) throws AnaliseSintaticaException {
		NoVO expressao = expressao();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenVirgulaFechaParenteses = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaVirgulaSemErro(tokenVirgulaFechaParenteses.getPalavraReservada())) {
			NoVO virgula = virgula();
			NoVO expressao2 = declaraExpressao(expressao);
//			linha = getLinhaAtual();
//			TokenVO tokenExpressaoFechaParenteses = getTokenAtual();
//			if (expressao2.getFilhos().isEmpty()) {
//				expressao.getUltimoFilho().getFilhos().add(expressao2);
//				noPai.getUltimoFilho().getFilhos().add(expressao);
//			} else {
				if (!expressao.equals(expressao2)) {
					expressao.getUltimoFilho().getFilhos().add(expressao2);
				}
//			}
		} /*else if (sintaticoHelper.isPalavraReservadaFechaParentesesSemErro(tokenVirgulaFechaParenteses.getPalavraReservada())) {
			System.out.println();
		}*/

		return expressao;
	}
//	if (sintaticoHelper.isPalavraReservadaFechaParentesesSemErro(tokenExpressaoFechaParenteses.getPalavraReservada())) {
//		NoVO fechaParenteses = fechaParenteses();
//		expressao.getUltimoFilho().getFilhos().add(fechaParenteses);
//		noPai.getUltimoFilho().getFilhos().add(expressao);
//		return noPai;
//	} else {
//	}

	
	//TODO não trata soma(soma(2));
	private NoVO expressaoIDAbreParenteses() throws AnaliseSintaticaException {
		abreParenteses();
		NoVO expressaoIDAbreParenteses = new NoVO();
		NoVO expressao = new NoVO(); 
		expressao = declaraExpressao(expressao);
		if (expressao.getFilhos().isEmpty()) {
			if (!expressao.getTokens().isEmpty()) { //gambit
//				expressaoIDAbreParenteses.getFilhos().add(expressao);
			} else {
				expressao = expressaoIDAbreParenteses;
			}
		} else {
//			expressaoIDAbreParenteses.getFilhos().add(expressao);
			expressaoIDAbreParenteses.getFilhos().addAll(getNos(expressao));
		}
		NoVO fechaParenteses = fechaParenteses();
//		expressao.getUltimoFilho().getFilhos().add(fechaParenteses);
		return expressaoIDAbreParenteses;
	}
	
	private NoVO expressaoConst() {
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenConst = getTokenAtual();
		NoVO expressaoConst = criarNo(linha, tokenConst);
		return expressaoConst;
	}
	
	private NoVO expressaoVector() throws AnaliseSintaticaException {
		NoVO expressaoVector = tipoPrimitivo();
		NoVO abreColchete = abreColchete();
		expressaoVector.getFilhos().add(abreColchete);
		NoVO expressao = expressao();
		abreColchete.getFilhos().add(expressao);
		NoVO fechaColchete = fechaColchete();
		expressao.getUltimoFilho().getFilhos().add(fechaColchete);
		return expressaoVector;
	}
	
	private NoVO expressaoOpUnario() throws AnaliseSintaticaException {
		NoVO expressaoOpUnario = opUnario();
		NoVO expressao = expressao();
		expressaoOpUnario.getFilhos().add(expressao);
		return expressaoOpUnario;
	}
	
//	private NoVO expressaoAbreParenteses() throws AnaliseSintaticaException {
//		NoVO expressaoAbreParenteses = abreParenteses();
//		NoVO expressao = expressao();
//		expressaoAbreParenteses.getFilhos().add(expressao);
//		return expressaoAbreParenteses;
//	}

	private NoVO listaCmd() throws AnaliseSintaticaException {
		NoVO noListaCmd = new NoVO();
		noListaCmd = listaCmdL(noListaCmd);
		return noListaCmd;
	}
	
	private NoVO listaCmdL(NoVO noPai) throws AnaliseSintaticaException {
		NoVO noCmd = cmd();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenCmd = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaCmdSemErro(tokenCmd.getPalavraReservada())) {
			NoVO noListaCmd = listaCmdL(noPai);
			if (noListaCmd.getFilhos().isEmpty()) {
				return noPai; //NAO PAROU AQUI
			} else {
				if (!noCmd.equals(noListaCmd)) {
					if (noPai.getTokens().isEmpty()){
//						return noCmd;
						noListaCmd.getFilhos().add(0,noCmd);
						return noListaCmd;
					} else {
						noPai.getFilhos().add(0,noCmd);
						return noPai;
					}
				}
			}
		}
		noPai.getFilhos().add(0,noCmd);
		return noCmd;
	}
	
	private NoVO cmd() throws AnaliseSintaticaException {
		NoVO noCmd = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenCmd = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaIfSemErro(tokenCmd.getPalavraReservada())) {
			noCmd = cmdIf();
		} else if (sintaticoHelper.isPalavraReservadaWhileSemErro(tokenCmd.getPalavraReservada())) {
			noCmd = cmdWhile();
		} else if (sintaticoHelper.isPalavraReservadaWriteSemErro(tokenCmd.getPalavraReservada())) {
			noCmd = cmdWrite();
		} else if (sintaticoHelper.isPalavraReservadaWriteLnSemErro(tokenCmd.getPalavraReservada())) {
			noCmd = cmdWriteLn();
		} else if (sintaticoHelper.isPalavraReservadaIDSemErro(tokenCmd.getPalavraReservada())) {
			noCmd = cmdID();
		} 
		return noCmd;
	}
	
	private NoVO cmdIf() throws AnaliseSintaticaException {
		NoVO noIf = noIf();
		NoVO abreParenteses = abreParenteses();
		NoVO expressao = expressao();
		NoVO fechaParenteses = fechaParenteses();
		NoVO doisPontos = doisPontos();
		NoVO listaCmd = listaCmd();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenEndElse = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaElseSemErro(tokenEndElse.getPalavraReservada())) {
			NoVO noElse = noElse();
//			if (noElse.getFilhos().isEmpty()) {
//				if (noElse.getTokens().isEmpty()) {
//					noElse = listaCmd;
//				} else {
//					listaCmd.getUltimoFilho().getFilhos().add(noElse);
//				}
//			} else {
//				listaCmd.getUltimoFilho().getFilhos().add(noElse);
//			}
			NoVO endPontoVirgula = endPontoVirgula();
		} else if (sintaticoHelper.isPalavraReservadaEndSemErro(tokenEndElse.getPalavraReservada())) {
			NoVO endPontoVirgula = endPontoVirgula();
		} else {
			sintaticoHelper.erro("esperava else ou end na expressao if", linha, tokenEndElse);
		}
		noIf.getFilhos().add(expressao);
		if (!listaCmd.getFilhos().isEmpty()){ 
			noIf.getFilhos().add(listaCmd);
		}
		return noIf;
	}
	
	private NoVO noElse() throws AnaliseSintaticaException {
		NoVO noElse = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenElse = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaElse(linha, tokenElse)) {
			noElse = criarNo(linha, tokenElse);
			NoVO listaCmd = listaCmd();
			if (listaCmd.getFilhos().isEmpty()) {
				listaCmd = noElse;
			} else {
				noElse.getFilhos().add(listaCmd);
			}
		}
		return noElse;
	}
	
	private NoVO noIf() throws AnaliseSintaticaException {
		NoVO noIf = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenIf = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaIf(linha, tokenIf)) {
			noIf = criarNo(linha, tokenIf);
			noIf.setTipoExpressao(TipoExpressaoEnum.CMD_IF);
		}
		return noIf;
	}
	
	private NoVO op() {
		NoVO op = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenOp = getTokenAtual();
		op = criarNo(linha, tokenOp);
		op.setTipoExpressao(TipoExpressaoEnum.OP);
		return op;
	}
	
	private NoVO opUnario() {
		NoVO opUnario = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenOp = getTokenAtual();
		opUnario = criarNo(linha, tokenOp);
		opUnario.setTipoExpressao(TipoExpressaoEnum.OP_UNARIO);
		return opUnario;
	}

	private NoVO cmdWhile() throws AnaliseSintaticaException {
		NoVO cmdWhile = noWhile();
		NoVO abreParenteses = abreParenteses();
		NoVO expressao = expressao();
		NoVO fechaParenteses = fechaParenteses();
		NoVO doisPontos = doisPontos();
		NoVO listaCmd = listaCmd();
		NoVO endPontoVirgula = endPontoVirgula();
		cmdWhile.getFilhos().add(expressao); //expressao errada
		cmdWhile.getFilhos().add(listaCmd);
		return cmdWhile;
	}
	
	private NoVO noWhile() throws AnaliseSintaticaException {
		NoVO noIf = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenWrite = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaWhile(linha, tokenWrite)) {
			noIf = criarNo(linha, tokenWrite);
			noIf.setTipoExpressao(TipoExpressaoEnum.CMD_WHILE);
		}
		return noIf;
	}
	
	private NoVO cmdWrite() throws AnaliseSintaticaException {
		NoVO noWrite = noWrite();
		NoVO abreParenteses = abreParenteses();
		NoVO expressao = expressao();
		NoVO fechaParenteses = fechaParenteses();
		NoVO pontoVirgula = pontoVirgula();
		noWrite.getFilhos().add(expressao);
		return noWrite;
	}
	
	private NoVO noWrite() throws AnaliseSintaticaException {
		NoVO noIf = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenWrite = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaWrite(linha, tokenWrite)) {
			noIf = criarNo(linha, tokenWrite);
			noIf.setTipoExpressao(TipoExpressaoEnum.CMD_WRITE);
		}
		return noIf;
	}
	
	private NoVO cmdWriteLn() throws AnaliseSintaticaException {
		NoVO noWriteLn = noWriteLn();
		NoVO abreParenteses = abreParenteses();
		NoVO expressao = expressao();
		NoVO fechaParenteses = fechaParenteses();
		NoVO pontoVirgula = pontoVirgula();
		noWriteLn.getFilhos().add(expressao);
		return noWriteLn;
	}
	
	private NoVO noWriteLn() throws AnaliseSintaticaException {
		NoVO noIf = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenWrite = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaWriteLn(linha, tokenWrite)) {
			noIf = criarNo(linha, tokenWrite);
			noIf.setTipoExpressao(TipoExpressaoEnum.CMD_WRITELN);
		}
		return noIf;
	}
	
	private NoVO cmdID() throws AnaliseSintaticaException {
		NoVO cmdID = id();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenIgualAbreParenteses = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaAbreColcheteSemErro(tokenIgualAbreParenteses.getPalavraReservada())) {
			NoVO cmdIDAtribuiArray = cmdIDAtribuiArray();
			cmdID.getUltimoFilho().getFilhos().add(cmdIDAtribuiArray);
		} else if (sintaticoHelper.isPalavraReservadaIgualSemErro(tokenIgualAbreParenteses.getPalavraReservada())) {
			NoVO cmdIDAtribui = cmdIDAtribui();
			cmdIDAtribui.getFilhos().add(0,cmdID);
			cmdID = cmdIDAtribui;
		} else if (sintaticoHelper.isPalavraReservadaAbreParentesesSemErro(tokenIgualAbreParenteses.getPalavraReservada())) {
			NoVO cmdIDFuncao = cmdIDFuncao();
			cmdID.getUltimoFilho().getFilhos().add(cmdIDFuncao);
		} 
		return cmdID;
	}

	private NoVO cmdIDAtribui() throws AnaliseSintaticaException {
		NoVO cmdIDAtribui = igual();
		NoVO expressao = expressao();
		NoVO pontoVirgula = pontoVirgula();
		cmdIDAtribui.getFilhos().add(expressao);
		return cmdIDAtribui;
	}
	
	private NoVO cmdIDAtribuiArray() throws AnaliseSintaticaException {
		NoVO cmdIDAtribuiArray = abreColchete();
		NoVO expressao = expressao();
		cmdIDAtribuiArray.getFilhos().add(expressao);
		NoVO fechaColchete = fechaColchete();
		expressao.getUltimoFilho().getFilhos().add(fechaColchete);
		NoVO igual = igual();
		fechaColchete.getFilhos().add(igual);
		NoVO expressao2 = expressao();
		igual.getFilhos().add(expressao2);
		NoVO pontoVirgula = pontoVirgula();
		expressao2.getUltimoFilho().getFilhos().add(pontoVirgula);
		return cmdIDAtribuiArray;
	}
	
	//TODO
	private NoVO cmdIDFuncao() throws AnaliseSintaticaException {
		NoVO cmdIDFuncao = abreParenteses();
		NoVO expressao = new NoVO();
		expressao = declaraExpressao(expressao);
		if (expressao.getFilhos().isEmpty()) {
			expressao = cmdIDFuncao;
		} else {
			cmdIDFuncao.getFilhos().add(expressao);
		}
		NoVO fechaParenteses = fechaParenteses();
		expressao.getUltimoFilho().getFilhos().add(fechaParenteses);
		NoVO pontoVirgula = pontoVirgula();
		fechaParenteses.getFilhos().add(pontoVirgula);
		return cmdIDFuncao;
	}
	
	private NoVO listaArg() throws AnaliseSintaticaException {
		NoVO noListaArg = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(tokenTipoPrimitivo.getPalavraReservada())) {
			noListaArg = arg();
			linha = getLinhaAtual();
			TokenVO tokenVirgula = getTokenAtual();
			if (sintaticoHelper.isPalavraReservadaVirgulaSemErro(tokenVirgula.getPalavraReservada())) {
				noListaArg = listaArgL(noListaArg);
			}
		}
		NoVO noOrdenado = new NoVO();
		if (!noListaArg.getFilhos().isEmpty()) {
			noOrdenado.getFilhos().addAll(getNos(noListaArg));
		}
		return noOrdenado;
	}
	
	private NoVO listaArgL(NoVO noPai) throws AnaliseSintaticaException {
		NoVO noVirgula = virgula();
		NoVO noArg = arg();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenVirgula = getTokenAtual();
		if (sintaticoHelper.isPalavraReservadaVirgulaSemErro(tokenVirgula.getPalavraReservada())) {
			NoVO noListaArgL = listaArgL(noArg);
			noPai.getUltimoFilho().getFilhos().add(noListaArgL);
			return noPai;
		} else {
			noPai.getUltimoFilho().getFilhos().add(noArg);
			return noPai;
		}
	}
	
}
