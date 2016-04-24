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
		
		NoVO noClasse = classe(numLinhaAtual);
		System.out.println(noClasse);
		numLinhaAtual++;
		NoVO noFuncao = listaFuncao();
		
		arquivo.getArvore().setNoRaiz(noClasse);
		arquivo.getArvore().getNoRaiz().getFilhos().add(noFuncao);
		
		System.out.println(noFuncao);
	}
	
	private NoVO classe(int numLinhaAtual) throws AnaliseSintaticaException {
		LinhaVO linha = getLinha(numLinhaAtual);
		NoVO no = new NoVO();
		if (SintaticoHelper.isExpressaoClasseValida(linha, "")) {
			ordenar(linha);
			no.setTipoExpressao(TipoExpressao.CLASSE);
			no.setLinha(linha);
			arquivo.adicionarNoMapa(no);
		}
		return no;
	}
	
	private NoVO listaFuncao() throws AnaliseSintaticaException {
//		LinhaVO linha = getLinha(arquivo, numLinhaAtual);
		NoVO noFilho = funcao();
		NoVO noPai = null;
		noPai = listaFuncao();
		if (noPai != null) {
			noPai.getFilhos().add(noFilho);
			return noPai;
		}
		return noFilho;
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
		if (SintaticoHelper.isPalavraReservadaDEF(tokenDEF.getPalavraReservada())) {
			noDEF = criarNo(linha, tokenDEF);
		}
		return noDEF;
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
	
	private NoVO pontoVirgula() throws AnaliseSintaticaException {
		NoVO noPontoVirgula = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenPontoVirgula = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaEnd(tokenPontoVirgula.getPalavraReservada())) {
			noPontoVirgula = criarNo(linha, tokenPontoVirgula);
		}
		return noPontoVirgula;
	}
	
	private NoVO endPontoVirgula() throws AnaliseSintaticaException {
		NoVO noEnd = new NoVO();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenEnd = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaEnd(tokenEnd.getPalavraReservada())) {
			noEnd = criarNo(linha, tokenEnd);
			NoVO noPontoVirgula = new NoVO();
			linha = getLinhaAtual();
			TokenVO tokenPontoVirgula = linha.getTokens().get(numTokenAtual-1);
			if (SintaticoHelper.isPalavraReservadaEnd(tokenPontoVirgula.getPalavraReservada())) {
				noPontoVirgula = criarNo(linha, tokenPontoVirgula);
			}
			noEnd.getFilhos().add(noPontoVirgula);
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
	
	private NoVO funcao() throws AnaliseSintaticaException {
		LinhaVO linha = getLinhaAtual();
		NoVO noFuncao = new NoVO();
		NoVO noDEF = def();
		noFuncao.getFilhos().add(noDEF);
			
		NoVO noTipoMacro = tipoMacro();
		noDEF.getFilhos().add(noTipoMacro);
	
		NoVO noID = id();
		noTipoMacro.getFilhos().add(noID);
		NoVO noAbreParenteses = abreParenteses();
		noID.getFilhos().add(noAbreParenteses);

		NoVO noListaArg = listaArg();
		noAbreParenteses.getFilhos().add(noListaArg);
		
		//pular listaArg
//		numTokenAtual = numTokenAtual+5; 
		
		NoVO noFechaParenteses = fechaParenteses();
		noListaArg.getFilhos().add(noFechaParenteses);
		NoVO noDoisPontos = doisPontos();
		noFechaParenteses.getFilhos().add(noDoisPontos);
		
		//TODO
		NoVO noDeclaraIDLinha = declaraIDLinha();
		noDoisPontos.getFilhos().add(noDeclaraIDLinha);
		NoVO noListaCmd = listaCmd();
		noDeclaraIDLinha.getFilhos().add(noListaCmd);
		NoVO noRetorno = retorno();
		noListaCmd.getFilhos().add(noRetorno);
		//pular 
		LinhaVO proxLinha = arquivo.getLinhas().get(numLinhaAtual);
		linha = proxLinha;
		contarProximaLinhaToken(linha);
		LinhaVO proxProxLinha = arquivo.getLinhas().get(numLinhaAtual);
		linha = proxProxLinha;
		
//		NoVO noEnd = end();
		NoVO noEndPontoVirgula = endPontoVirgula();
		
		numLinhaAtual = numLinhaAtual+2;
		//fim funcao
		
//		TokenVO tokenEnd = linha.getTokens().get(numTokenAtual-1);
//		if (SintaticoHelper.isPalavraReservadaEnd(tokenEnd.getPalavraReservada())) {
//			NoVO noEnd = new NoVO();
//			noEnd.setLinha(linha);
//			noEnd.getTokens().add(tokenEnd);
			
//			TokenVO tokenPontoVirgula = linha.getTokens().get(numTokenAtual);
//			if (SintaticoHelper.isPalavraReservadaPontoVirgula(tokenPontoVirgula.getPalavraReservada())) {
//				NoVO noPontoVirgula = new NoVO();
//				noPontoVirgula.setLinha(linha);
//				noPontoVirgula.getTokens().add(tokenEnd);
//				noEnd.getFilhos().add(noPontoVirgula);
//				
//				
//			}
//		}
				
		return noFuncao;
	}
	
	private NoVO retorno() {
//		NoVO noRetorno = new NoVO();
//		return noRetorno;
		return null;
	}

	private NoVO listaCmd() {
		NoVO noListaCmd = new NoVO();
		return noListaCmd;
	}

	private NoVO declaraID() {
		NoVO noDeclaraID = new NoVO();
		return noDeclaraID;
	}
	
	private NoVO declaraIDLinha() throws AnaliseSintaticaException {
		NoVO noDeclaraIDLinha = new NoVO();
		LinhaVO linha = getLinha(numLinhaAtual);
		TokenVO tokenTipoPrimitivo = linha.getTokens().get(numTokenAtual);
		
		if (SintaticoHelper.isPalavraReservadaTipoPrimitivo(tokenTipoPrimitivo.getPalavraReservada())) {
			
		}
		
		NoVO noDeclaraID = declaraID();
		return noDeclaraIDLinha;
	}
	
	private NoVO listaArg() throws AnaliseSintaticaException {
		NoVO nolistaArg = arg();
		LinhaVO linha = getLinhaAtual();
		TokenVO tokenVirgula = linha.getTokens().get(numTokenAtual-1);
		if (SintaticoHelper.isPalavraReservadaVirgulaSemErro(tokenVirgula.getPalavraReservada())) {
			/*NoVO noListaArgL = */listaArgL(nolistaArg);
//			nolistaArg.getUltimoFilho().getFilhos().add(noListaArgL);
		}
		return nolistaArg;
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
		LinhaVO linha = getLinha(numLinhaAtual);
		NoVO noTipoMacro = tipoPrimitivo();
		TokenVO tokenAbreColchete = linha.getTokens().get(numTokenAtual);
		if (SintaticoHelper.isPalavraReservadaAbreColcheteSemErro(tokenAbreColchete.getPalavraReservada())) {
//			noTipoMacro.getTokens().add(tokenAbreColchete);
//			numTokenAtual++;
			NoVO noAbreColchete = criarNo(linha, noTipoMacro, tokenAbreColchete);
			linha = getLinhaAtual();
			TokenVO tokenFechaColchete = linha.getTokens().get(numTokenAtual);
			if (SintaticoHelper.isPalavraReservadaFechaColchete(tokenFechaColchete.getPalavraReservada())) {
//				noTipoMacro.getTokens().add(tokenFechaColchete);
//				numTokenAtual++;
				NoVO noFechaColchete = criarNo(linha, noAbreColchete, tokenFechaColchete);
				
			}
		}
		return noTipoMacro;
	}
	
	private NoVO tipoPrimitivo() throws AnaliseSintaticaException {
		LinhaVO linha = getLinha(numLinhaAtual);
		TokenVO tokenTipoPrimitivo = linha.getTokens().get(numTokenAtual-1);
		NoVO noTipoPrimitivo = new NoVO();
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
		
		private static PalavraReservada[] sequenciaClasse = new PalavraReservada[]{
			PalavraReservada.CLASS,
			PalavraReservada.ID,
			PalavraReservada.DOIS_PONTOS
		};
		
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
		
		private static TokenVO getTokenPorLinha(LinhaVO linha, int numeroLinha) {
			if (!linha.getTokens().isEmpty() &&
					linha.getTokens().size() >= numeroLinha) {
				return linha.getTokens().get(numeroLinha);
			}
			return null;
		}
		
		private static boolean isExpressaoClasseValida(LinhaVO linha, String mensagemValidacao) throws AnaliseSintaticaException {
			if (isExpressaoClasseValida(linha)) {
				return true;
			}
			erro(mensagemValidacao);
			return false;
		}
		
		private static boolean isExpressaoClasseValida(LinhaVO linha) {
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
		
		private static boolean isPalavraReservadaVirgulaSemErro(PalavraReservada palavra) {
			return isPalavraReservadaSemErro(PalavraReservada.VIRGULA, palavra);
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
		
		private static boolean isPalavraReservadaFechaParenteses(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.FECHA_PARENTESES, palavra, "funcao espera fecha parenteses dps de lista arg");
		}
		
		private static boolean isPalavraReservadaAbreParenteses(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.ABRE_PARENTESES, palavra, "funcao espera abre parenteses dps do id");
		}
		
		private static boolean isPalavraReservadaFechaColchete(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.FECHA_COLCHETE, palavra, "Esperava fecha colchete");
		}
		
		private static boolean isPalavraReservadaAbreColcheteSemErro(PalavraReservada palavra) {
			return isPalavraReservadaSemErro(PalavraReservada.ABRE_COLCHETE, palavra);
		}
		
		private static boolean isPalavraReservadaDEF(PalavraReservada palavra) throws AnaliseSintaticaException {
			return isPalavraReservada(PalavraReservada.DEF, palavra, "funcao deve iniciar com def");
		}
		
//		private boolean isPalavraReservadaID(PalavraReservada palavra) throws AnaliseSintaticaException {
//			return isPalavraReservada(PalavraReservada.ID, palavra, "funcao espera id dps de tipo macro");
//		}
		
		private static boolean isPalavraReservadaSemErro(PalavraReservada palavraReservada, PalavraReservada palavraAtual) {
			return palavraReservada.equals(palavraAtual);
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
			if (errosCount >= MAX_ERROS) {
				throw new AnaliseSintaticaException(errors);
			}
		}
				
		
	}

}
