package br.unibh.pyscal.analisador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.unibh.pyscal.enumerador.PalavraReservada;
import br.unibh.pyscal.enumerador.TipoExpressao;
import br.unibh.pyscal.exception.AnaliseSintaticaException;
import br.unibh.pyscal.util.FileUtil;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.NoVO;
import br.unibh.pyscal.vo.TokenVO;

//TODO FALTA TRATAR: listaFuncao, funcao, listaCmd, cmd, expressao
public class AnalisadorSintatico {
//	private NoVO noRoot;
//	private List<TokenVO> tokens;
	private ArquivoVO arquivo;
	private int numLinhaAtual;
	private int numTokenAtual;
	private int nivelAtual;
	private SintaticoHelper sintaticoHelper = SintaticoHelper.getInstancia();
	
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
		this.nivelAtual = 0;
		sintaticoHelper.errors = "";
		sintaticoHelper.errosCount = 0;
	}

	public void analisar(ArquivoVO arquivo) throws AnaliseSintaticaException {
		init();
		ArquivoVO arquivoExecutavel = sintaticoHelper.getArquivoExecutavel(arquivo);
		/*List<TokenVO> tokens = */sintaticoHelper.getTokensAExecutar(arquivo);
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
		if (noFuncao.getFilhos().isEmpty()) { //fazer isso já nos métodos(passar noPai)
			noFuncao = noClasse;
		} else {
			noClasse.getUltimoFilho().getFilhos().add(noFuncao);
		}
		NoVO noMain = main();
		noFuncao.getUltimoFilho().getFilhos().add(noMain);
		NoVO noEndPonto = endPonto();
		noMain.getUltimoFilho().getFilhos().add(noEndPonto);
		
		arquivo.setNoRaiz(noClasse);
//		FileUtil.imprimirAST(arquivo);
	}
	
	private NoVO classe() throws AnaliseSintaticaException {
		NoVO noClasse = noClass();
		NoVO noID = id();
		noClasse.getFilhos().add(noID);
		NoVO noDoisPontos = doisPontos();
		noID.getFilhos().add(noDoisPontos);
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
		TokenVO tokenDef = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaDefSemErro(tokenDef.getPalavraReservada())) {
			noFuncao = listaFuncaoL(noFuncao);
		}
		return noFuncao;
	}
	
	private NoVO listaFuncaoL(NoVO noPai) throws AnaliseSintaticaException {
		NoVO noFuncao = funcao();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDef = linha.getTokens().get(numTokenAtual-1);
//		noPai.getUltimoFilho().getFilhos().add(noFuncao);
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
	}
	
	private NoVO funcao() throws AnaliseSintaticaException {
		NoVO noFuncao = def();
			
		NoVO noTipoMacro = tipoMacro();
		noFuncao.getFilhos().add(noTipoMacro);
	
		NoVO noID = id();
		noTipoMacro.getFilhos().add(noID);
		NoVO noAbreParenteses = abreParenteses();
		noID.getFilhos().add(noAbreParenteses);

		NoVO noListaArg = listaArg();
		if (listaArg().getFilhos().isEmpty()) {
			noListaArg = noAbreParenteses;
		} else {
			noAbreParenteses.getFilhos().add(noListaArg);
		}
		
		NoVO noFechaParenteses = fechaParenteses();
		noListaArg.getUltimoFilho().getFilhos().add(noFechaParenteses);
		NoVO noDoisPontos = doisPontos();
		noFechaParenteses.getFilhos().add(noDoisPontos);
		
		NoVO noDeclaraID = declaraIDS();
		if (noDeclaraID.getFilhos().isEmpty()) {
			noDeclaraID = noDoisPontos;
		} else {
			noDoisPontos.getFilhos().add(noDeclaraID);
		}
		NoVO noListaCmd = listaCmd();
		if (noListaCmd.getFilhos().isEmpty()) {
			noListaCmd = noDeclaraID;
		} else {
			noDeclaraID.getUltimoFilho().getFilhos().add(noListaCmd);
		}
		
		NoVO noRetorno = retorno();
		if (noRetorno.getFilhos().isEmpty()) {
			noRetorno = noListaCmd;
		} else {
			noListaCmd.getFilhos().add(noRetorno);
		}

		NoVO noEndPontoVirgula = endPontoVirgula();
		noRetorno.getUltimoFilho().getFilhos().add(noEndPontoVirgula);
		return noFuncao;
	}
	
	private NoVO main() throws AnaliseSintaticaException {
		NoVO noMain = defstatic();
		NoVO noVoid = noVoid();
		noMain.getFilhos().add(noVoid);
		NoVO main = noMain();
		noVoid.getFilhos().add(main);
		NoVO noAbreParenteses = abreParenteses();
		main.getFilhos().add(noAbreParenteses);
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
		
		if (declaraIDS.getFilhos().isEmpty()) {
			declaraIDS = noDoisPontos;
		} else {
			noDoisPontos.getFilhos().add(declaraIDS);
		}
		
		NoVO noListaCmd = listaCmd();
		if (noListaCmd.getFilhos().isEmpty()) {
			noListaCmd = noDoisPontos;
		} else {
			noDoisPontos.getUltimoFilho().getFilhos().add(noListaCmd);
		}
		NoVO noEndPontoVirgula = endPontoVirgula();
		noListaCmd.getUltimoFilho().getFilhos().add(noEndPontoVirgula);
		
		return noMain;
	}
	
	private NoVO declaraIDS() throws AnaliseSintaticaException {
		NoVO noDeclaraIDS = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(tokenTipoPrimitivo.getPalavraReservada())) {
			noDeclaraIDS = declaraIDL(noDeclaraIDS);
		}
		return noDeclaraIDS;
	}
	
	private NoVO declaraIDL(NoVO noPai) throws AnaliseSintaticaException {
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaTipoPrimitivo(tokenTipoPrimitivo.getPalavraReservada())) {
			NoVO noArg = arg();
			linha = getLinhaAtual();
			TokenVO tokenPontoVirgula = linha.getTokens().get(numTokenAtual-1);
			if (sintaticoHelper.isPalavraReservadaPontoVirgula(tokenPontoVirgula.getPalavraReservada())) {
				NoVO noPontoVirgula = pontoVirgula();
				noArg.getUltimoFilho().getFilhos().add(noPontoVirgula);
				linha = getLinhaAtual();
				TokenVO proxToken = linha.getTokens().get(numTokenAtual-1);
				if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(proxToken.getPalavraReservada())) {
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
	
	private NoVO arg() throws AnaliseSintaticaException {
		NoVO noArg = tipoMacro();
		NoVO noID = id();
		noArg.getFilhos().add(noID);
		return noArg;
	}
	
	private NoVO retorno() throws AnaliseSintaticaException {
		NoVO noRetorno = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenReturn = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaReturnSemErro(tokenReturn.getPalavraReservada())) {
			noRetorno = noReturn();
			
			NoVO noExpressao = expressao();
			noRetorno.getFilhos().add(noExpressao);
			
			linha = getLinhaAtual();
			TokenVO tokenPontoVirgula = linha.getTokens().get(numTokenAtual-1);
			if (sintaticoHelper.isPalavraReservadaPontoVirgula(tokenPontoVirgula.getPalavraReservada())) {
				NoVO noPontoVirgula = pontoVirgula();
				noExpressao.getFilhos().add(noPontoVirgula);
			}
		}
		return noRetorno;
	}
	
	private NoVO tipoMacro() throws AnaliseSintaticaException {
		LinhaVO linha = getLinhaAtual();
		NoVO noTipoMacro = tipoPrimitivo();
		TokenVO tokenAbreColchete = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaAbreColcheteSemErro(tokenAbreColchete.getPalavraReservada())) {
//			noTipoMacro.getTokens().add(tokenAbreColchete);
//			numTokenAtual++;
			NoVO noAbreColchete = criarNo(linha, noTipoMacro, tokenAbreColchete);
			linha = getLinhaAtual();
			TokenVO tokenFechaColchete = linha.getTokens().get(numTokenAtual-1);
			if (sintaticoHelper.isPalavraReservadaFechaColchete(tokenFechaColchete.getPalavraReservada())) {
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
		if (sintaticoHelper.isPalavraReservadaTipoPrimitivo(tokenTipoPrimitivo.getPalavraReservada())) {
			noTipoPrimitivo = criarNo(linha, tokenTipoPrimitivo);
			noTipoPrimitivo.setTipoExpressao(TipoExpressao.TIPO_PRIMITIVO);
		}
		return noTipoPrimitivo;
	}
	
	private NoVO criarNo(LinhaVO linha, TokenVO token) {
		NoVO no = new NoVO();
		no.setLinha(linha);
		no.getTokens().add(token);
		no.setNivel(nivelAtual);
		nivelAtual++;
		imprimir(no);
		contarProximaLinhaToken(linha);
		return no;
	}
	
	private void imprimir(NoVO no) {
		System.out.println("Nivel:"+no.getNivel()+
			" Linha:"+no.getLinha().getNumero()+" No:"+no.getTokens());
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
		if (sintaticoHelper.isPalavraReservadaDefStatic(tokenDefstatic.getPalavraReservada())) {
			noDefstatic = criarNo(linha, tokenDefstatic);
		}
		return noDefstatic;
	}
	
	private NoVO noVoid() throws AnaliseSintaticaException {
		NoVO noVoid = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenVoid = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaVoid(tokenVoid.getPalavraReservada())) {
			noVoid = criarNo(linha, tokenVoid);
		}
		return noVoid;
	}
	
	private NoVO noMain() throws AnaliseSintaticaException {
		NoVO noMain = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenMain = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaMain(tokenMain.getPalavraReservada())) {
			noMain = criarNo(linha, tokenMain);
		}
		return noMain;
	}
	
	private NoVO noClass() throws AnaliseSintaticaException {
		NoVO noClass = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenClass = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaClass(tokenClass.getPalavraReservada())) {
			noClass = criarNo(linha, tokenClass);
		}
		return noClass;
	}
	
	private NoVO string() throws AnaliseSintaticaException {
		NoVO noString = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenString = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaString(tokenString.getPalavraReservada())) {
			noString = criarNo(linha, tokenString);
		}
		return noString;
	}
	
	private NoVO id() throws AnaliseSintaticaException {
		NoVO noID = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenID = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaID(tokenID.getPalavraReservada())) {
			noID = criarNo(linha, tokenID);
		}
		return noID;
	}
	
	private NoVO abreParenteses() throws AnaliseSintaticaException {
		NoVO noAbreParenteses = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenAbreParenteses = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaAbreParenteses(tokenAbreParenteses.getPalavraReservada())) {
			noAbreParenteses = criarNo(linha, tokenAbreParenteses);
		}
		return noAbreParenteses;
	}
	
	private NoVO fechaParenteses() throws AnaliseSintaticaException {
		NoVO noFechaParenteses = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenFechaParenteses = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaFechaParenteses(tokenFechaParenteses.getPalavraReservada())) {
			noFechaParenteses = criarNo(linha, tokenFechaParenteses);
		}
		return noFechaParenteses;
	}
	
	private NoVO abreColchete() throws AnaliseSintaticaException {
		NoVO noAbreColchete = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenAbreColchete = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaAbreColchete(tokenAbreColchete.getPalavraReservada())) {
			noAbreColchete = criarNo(linha, tokenAbreColchete);
		}
		return noAbreColchete;
	}
	
	private NoVO fechaColchete() throws AnaliseSintaticaException {
		NoVO noFechaParenteses = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenFechaParenteses = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaFechaColchete(tokenFechaParenteses.getPalavraReservada())) {
			noFechaParenteses = criarNo(linha, tokenFechaParenteses);
		}
		return noFechaParenteses;
	}
	
	private NoVO doisPontos() throws AnaliseSintaticaException {
		NoVO noDoisPontos = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDoisPontos = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaDoisPontos(tokenDoisPontos.getPalavraReservada())) {
			noDoisPontos = criarNo(linha, tokenDoisPontos);
		}
		return noDoisPontos;
	}
	
	private NoVO def() throws AnaliseSintaticaException {
		NoVO noDEF = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenDEF = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaDef(tokenDEF.getPalavraReservada())) {
			noDEF = criarNo(linha, tokenDEF);
		}
		return noDEF;
	}
	
	private NoVO pontoVirgula() throws AnaliseSintaticaException {
		NoVO noPontoVirgula = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenPontoVirgula = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaPontoVirgula(tokenPontoVirgula.getPalavraReservada())) {
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
		if (sintaticoHelper.isPalavraReservadaEnd(tokenEnd.getPalavraReservada())) {
			noEnd = criarNo(linha, tokenEnd);
		}
		return noEnd;
	}
	
	private NoVO virgula() throws AnaliseSintaticaException {
		NoVO noVirgula = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenVirgula = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaVirgula(tokenVirgula.getPalavraReservada())) {
			noVirgula = criarNo(linha, tokenVirgula);
		}
		return noVirgula;
	}
	
	private NoVO ponto() throws AnaliseSintaticaException {
		NoVO noPonto = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenPonto = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaPonto(tokenPonto.getPalavraReservada())) {
			noPonto = criarNo(linha, tokenPonto);
		}
		return noPonto;
	}
	
	private NoVO noReturn() throws AnaliseSintaticaException {
		NoVO noReturn = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenReturn = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaReturn(tokenReturn.getPalavraReservada())) {
			noReturn = criarNo(linha, tokenReturn);
		}
		return noReturn;
	}
	
	private NoVO expressao() throws AnaliseSintaticaException {
		NoVO noExpressao = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenExpressao = linha.getTokens().get(numTokenAtual-1);
		
		if (sintaticoHelper.isPalavraReservadaConstanteSemErro(tokenExpressao.getPalavraReservada())) {
			noExpressao = expressaoConst();
			linha = getLinhaAtual();
			TokenVO tokenOp = linha.getTokens().get(numTokenAtual-1);
			if (sintaticoHelper.isPalavraReservadaOpSemErro(tokenOp.getPalavraReservada())) {
				NoVO op = op();
				noExpressao.getFilhos().add(op);
				NoVO expressaoL = expressaoL();
				op.getFilhos().add(expressaoL);
			}
		} else if (sintaticoHelper.isPalavraReservadaOpUnarioSemErro(tokenExpressao.getPalavraReservada())) {
			NoVO opUnario = opUnario();
			noExpressao.getFilhos().add(opUnario);
			
			
		} else if (sintaticoHelper.isPalavraReservadaIDSemErro(tokenExpressao.getPalavraReservada())) {
			noExpressao = expressaoID();
		}
		
		return noExpressao;
	}
	
	private NoVO expressaoL() throws AnaliseSintaticaException {
		NoVO noExpressao = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenExpressao = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaConstanteSemErro(tokenExpressao.getPalavraReservada())) {
			noExpressao = expressaoConst();
			linha = getLinhaAtual();
			TokenVO tokenOp = linha.getTokens().get(numTokenAtual-1);
			if (sintaticoHelper.isPalavraReservadaOpSemErro(tokenOp.getPalavraReservada())) {
				NoVO op = op();
				noExpressao.getFilhos().add(op);
				NoVO expressao2 = expressaoL();
				op.getFilhos().add(expressao2);
			}
		} else if (sintaticoHelper.isPalavraReservadaOpUnarioSemErro(tokenExpressao.getPalavraReservada())) {
			NoVO opUnario = opUnario();
			noExpressao.getFilhos().add(opUnario);
			
			
		} else if (sintaticoHelper.isPalavraReservadaIDSemErro(tokenExpressao.getPalavraReservada())) {
			noExpressao = expressaoID();
		}
		
		return noExpressao;
	}
	
	private NoVO expressaoConst() {
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenConst = linha.getTokens().get(numTokenAtual-1);
		NoVO expressaoConst = criarNo(linha, tokenConst);
		return expressaoConst;
	}
	
	private NoVO expressaoID() throws AnaliseSintaticaException {
		NoVO expressaoID = id();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenAbreColchete = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaAbreColcheteSemErro(tokenAbreColchete.getPalavraReservada())) {
			NoVO abreColchete = abreColchete();
			expressaoID.getFilhos().add(abreColchete);
			NoVO expressao2 = expressaoL();
			abreColchete.getFilhos().add(expressao2);
			NoVO noFechaColchete = fechaColchete();
			expressao2.getUltimoFilho().getFilhos().add(noFechaColchete);
		} else if (sintaticoHelper.isPalavraReservadaAbreParentesesSemErro(tokenAbreColchete.getPalavraReservada())) {
			NoVO abreParenteses = abreParenteses();
			expressaoID.getFilhos().add(abreParenteses);
			NoVO expressao2 = expressaoL();
			abreParenteses.getFilhos().add(expressao2);
			NoVO fechaParenteses = fechaParenteses();
			expressao2.getUltimoFilho().getFilhos().add(fechaParenteses);
		}
		
		return expressaoID;
	}

	private NoVO listaCmd() throws AnaliseSintaticaException {
		NoVO noListaCmd = new NoVO();
		noListaCmd = listaCmdL(noListaCmd);
		return noListaCmd;
	}
	
	private NoVO listaCmdL(NoVO noPai) throws AnaliseSintaticaException {
		NoVO noCmd = cmd();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenCmd = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaCmdSemErro(tokenCmd.getPalavraReservada())) {
			NoVO noListaCmd = listaCmd();
			if (noListaCmd.getFilhos().isEmpty()) {
				noPai.getUltimoFilho().getFilhos().add(noCmd);
				return noPai;
			} else {
				if (!noCmd.equals(noListaCmd)) {
					noCmd.getUltimoFilho().getFilhos().add(noListaCmd);
				}
			}
		}
		return noCmd;
	}
	
	private NoVO cmd() throws AnaliseSintaticaException {
		NoVO noCmd = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenCmd = linha.getTokens().get(numTokenAtual-1);
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
		noIf.getFilhos().add(abreParenteses);
		NoVO expressao = expressao();
		abreParenteses.getFilhos().add(expressao);
		NoVO fechaParenteses = fechaParenteses();
		expressao.getUltimoFilho().getFilhos().add(fechaParenteses);
		NoVO doisPontos = doisPontos();
		fechaParenteses.getFilhos().add(doisPontos);
		NoVO listaCmd = listaCmd();
		if (listaCmd.getFilhos().isEmpty()) {
			listaCmd = doisPontos;
		} else {
			doisPontos.getFilhos().add(listaCmd);
		}
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenEndElse = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaElseSemErro(tokenEndElse.getPalavraReservada())) {
			NoVO noElse = noElse();
			listaCmd.getUltimoFilho().getFilhos().add(noElse);
		} else if (sintaticoHelper.isPalavraReservadaEndSemErro(tokenEndElse.getPalavraReservada())) {
			NoVO endPontoVirgula = endPontoVirgula();
			listaCmd.getUltimoFilho().getFilhos().add(endPontoVirgula);
		} else {
			sintaticoHelper.erro("esperava else ou end na expressao if");
		}
		return noIf;
	}
	
	private NoVO noElse() throws AnaliseSintaticaException {
		NoVO noElse = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenElse = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaElse(tokenElse.getPalavraReservada())) {
			noElse = criarNo(linha, tokenElse);
		}
		return noElse;
	}
	
	
	private NoVO noIf() throws AnaliseSintaticaException {
		NoVO noIf = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenIf = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaIf(tokenIf.getPalavraReservada())) {
			noIf = criarNo(linha, tokenIf);
			noIf.setTipoExpressao(TipoExpressao.CMD_IF);
		}
		return noIf;
	}
	
	private NoVO op() {
		NoVO op = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenOp = linha.getTokens().get(numTokenAtual-1);
		op = criarNo(linha, tokenOp);
		op.setTipoExpressao(TipoExpressao.OP);
		return op;
	}
	
	private NoVO opUnario() {
		NoVO opUnario = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenOp = linha.getTokens().get(numTokenAtual-1);
		opUnario = criarNo(linha, tokenOp);
		opUnario.setTipoExpressao(TipoExpressao.OP_UNARIO);
		return opUnario;
	}

	private NoVO cmdWhile() {
		return null;
	}
	
	private NoVO cmdWrite() {
		return null;
	}
	
	private NoVO cmdWriteLn() {
		return null;
	}
	
	private NoVO cmdID() {
		return null;
	}
	
	private NoVO cmdAtribui() {
		return null;
	}
	
	private NoVO cmdFuncao() {
		return null;
	}
	

	private NoVO listaArg() throws AnaliseSintaticaException {
		NoVO noListaArg = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenTipoPrimitivo = linha.getTokens().get(numTokenAtual-1);
		if (sintaticoHelper.isPalavraReservadaTipoPrimitivoSemErro(tokenTipoPrimitivo.getPalavraReservada())) {
			noListaArg = arg();
			linha = getLinhaAtual();
			TokenVO tokenVirgula = linha.getTokens().get(numTokenAtual-1);
			if (sintaticoHelper.isPalavraReservadaVirgulaSemErro(tokenVirgula.getPalavraReservada())) {
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
		if (sintaticoHelper.isPalavraReservadaVirgulaSemErro(tokenVirgula.getPalavraReservada())) {
			NoVO noListaArgL = listaArgL(noVirgula);
			noPai.getUltimoFilho().getFilhos().add(noListaArgL);
			return noPai;
		} else {
			noPai.getUltimoFilho().getFilhos().add(noVirgula);
			return noPai;
		}
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
	
}
