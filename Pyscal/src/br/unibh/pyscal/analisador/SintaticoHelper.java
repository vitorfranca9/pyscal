package br.unibh.pyscal.analisador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.unibh.pyscal.enumerador.PalavraReservada;
import br.unibh.pyscal.exception.AnaliseSintaticaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;

//HELPER
public class SintaticoHelper {
		
	private final int MAX_ERROS = 5;
	public int errosCount = 0;
	public String errors = "";
	private static SintaticoHelper instancia;
	
	private SintaticoHelper() { }
	
	public static SintaticoHelper getInstancia() {
		if (instancia == null) {
			instancia = new SintaticoHelper();
		}
		return instancia;
	}
	
	private List<PalavraReservada> tiposPrimitivos = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.BOOL,
		PalavraReservada.INTEGER,
		PalavraReservada.STRING,
		PalavraReservada.DOUBLE,
		PalavraReservada.VOID
	});
	
	private List<PalavraReservada> op = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.OR,
		PalavraReservada.AND,
		PalavraReservada.MENOR,
		PalavraReservada.MENOR_IGUAL,
		PalavraReservada.MAIOR,
		PalavraReservada.MAIOR_IGUAL,
		PalavraReservada.IGUAL_IGUAL,
		PalavraReservada.DIFERENTE,
		PalavraReservada.DIVIDIR,
		PalavraReservada.MULTIPLICAR,
		PalavraReservada.SUBTRAIR,
		PalavraReservada.SOMAR
	});
	
	private List<PalavraReservada> opUnario = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.NOT,
		PalavraReservada.SUBTRAIR
	});
	
	private List<PalavraReservada> tiposPrimitivosSemVoid = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.BOOL,
		PalavraReservada.INTEGER,
		PalavraReservada.STRING,
		PalavraReservada.DOUBLE,
	});
	
	private List<PalavraReservada> constantes = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.CONST_STRING,
		PalavraReservada.CONSTDOUBLE,
		PalavraReservada.CONSTINTEGER,
		PalavraReservada.TRUE,
		PalavraReservada.FALSE
	});
	
	private List<PalavraReservada> cmd = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.IF,
		PalavraReservada.WHILE,
		PalavraReservada.WRITE,
		PalavraReservada.WRITELN,
		PalavraReservada.ID
	});
	
	public ArquivoVO getArquivoExecutavel(ArquivoVO arquivo) {
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
	
	public List<TokenVO> getTokensAExecutar(ArquivoVO arquivo) {
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
	
	public boolean isPalavraReservadaPonto(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.PONTO, palavra, "esperava ponto final");
	}
	
	public boolean isPalavraReservadaClass(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.CLASS, palavra, "esperava class");
	}
	
	public boolean isPalavraReservadaString(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.STRING, palavra, "esperava string");
	}
	
	public boolean isPalavraReservadaReturn(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.RETURN, palavra, "funcao esperava return");
	}
	
	public boolean isPalavraReservadaPontoVirgula(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.PONTO_VIRGULA, palavra, "funcao espera ponto virgula dps de end");
	}
	
	public boolean isPalavraReservadaVirgula(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.VIRGULA, palavra, "funcao esperava token virgula");
	}
	
	public boolean isPalavraReservadaEnd(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.END, palavra, "funcao espera end dps de retorno");
	}
	
	public boolean isPalavraReservadaElse(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.ELSE, palavra, "funcao esperava else");
	}
	
	public boolean isPalavraReservadaDoisPontos(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.DOIS_PONTOS, palavra, "funcao espera dois pontos dps de fecha parenteses");
	}
	
	public boolean isPalavraReservadaAbreParenteses(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.ABRE_PARENTESES, palavra, "funcao espera abre parenteses dps do id");
	}
	
	public boolean isPalavraReservadaFechaParenteses(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.FECHA_PARENTESES, palavra, "funcao espera fecha parenteses dps de lista arg");
	}
	
	public boolean isPalavraReservadaAbreColchete(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.ABRE_COLCHETE, palavra, "Esperava abre colchete");
	}
	
	public boolean isPalavraReservadaFechaColchete(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.FECHA_COLCHETE, palavra, "Esperava fecha colchete");
	}
	
	public boolean isPalavraReservadaDef(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.DEF, palavra, "funcao deve iniciar com def");
	}
	
	public boolean isPalavraReservadaDefStatic(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.DEFSTATIC, palavra, "funcao main deve iniciar com defstatic");
	}
	
	public boolean isPalavraReservadaVoid(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.VOID, palavra, "esperava void");
	}
	
	public boolean isPalavraReservadaMain(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.MAIN, palavra, "esperava main");
	}
	
	public boolean isPalavraReservadaIf(PalavraReservada palavraReservada) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.IF, palavraReservada, "esperava if");
	}
	
	public boolean isPalavraReservadaOp(PalavraReservada palavraReservada) throws AnaliseSintaticaException {
		if(isPalavraReservadaOpSemErro(palavraReservada)) {
			return true;
		}
		erro("Esperava op");
		return false;
	}
	
//		private boolean isPalavraReservadaID(PalavraReservada palavra) throws AnaliseSintaticaException {
//			return isPalavraReservada(PalavraReservada.ID, palavra, "funcao espera id dps de tipo macro");
//		}
	
	public boolean isPalavraReservadaEndSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.END, palavra);
	}
	
	public boolean isPalavraReservadaElseSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.ELSE, palavra);
	}
	
	public boolean isPalavraReservadaAbreParentesesSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.ABRE_PARENTESES, palavra);
	}
	
	public boolean isPalavraReservadaAbreColcheteSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.ABRE_COLCHETE, palavra);
	}
	
	public boolean isPalavraReservadaDefSemErro(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservadaSemErro(PalavraReservada.DEF, palavra);
	}
	
	public boolean isPalavraReservadaReturnSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.RETURN, palavra);
	}
	
	public boolean isPalavraReservadaVirgulaSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.VIRGULA, palavra);
	}
	
	public boolean isPalavraReservadaIDSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.ID, palavra);
	}
	
	public boolean isPalavraReservadaIfSemErro(PalavraReservada palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservada.IF, palavraReservada);
	}
	
	public boolean isPalavraReservadaWhileSemErro(PalavraReservada palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservada.WHILE, palavraReservada);
	}
	
	public boolean isPalavraReservadaWriteSemErro(PalavraReservada palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservada.WRITE, palavraReservada);
	}
	
	public boolean isPalavraReservadaWriteLnSemErro(PalavraReservada palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservada.WRITELN, palavraReservada);
	}
	
	public boolean isPalavraReservadaSemErro(PalavraReservada palavraReservada, PalavraReservada palavraAtual) {
		return palavraReservada.equals(palavraAtual);
	}
	
	public boolean isPalavraReservadaOpSemErro(PalavraReservada palavra) {
		return op.contains(palavra);
	}
	
	public boolean isPalavraReservadaTipoPrimitivoSemErro(PalavraReservada palavra) {
		return tiposPrimitivos.contains(palavra);
	}
	
	public boolean isPalavraReservadaTipoPrimitivoSemVoidSemErro(PalavraReservada palavra) {
		return tiposPrimitivosSemVoid.contains(palavra);
	}
	
	public boolean isPalavraReservadaConstanteSemErro(PalavraReservada palavra) {
		return constantes.contains(palavra);
	}
	
	public boolean isPalavraReservadaCmdSemErro(PalavraReservada palavra) {
		return cmd.contains(palavra);
	}
	
	public boolean isPalavraReservadaOpUnarioSemErro(PalavraReservada palavraReservada) {
		return PalavraReservada.SUBTRAIR.equals(palavraReservada) || PalavraReservada.NOT.equals(palavraReservada);
	}
	
	public boolean isPalavraReservadaOpUnario(PalavraReservada palavraReservada, String mensagemValidacao) 
			throws AnaliseSintaticaException {
		if (isPalavraReservadaOpUnarioSemErro(palavraReservada)) {
			return true;
		} 
		erro(mensagemValidacao);
		return false;
	}
	
	public boolean isPalavraReservada(PalavraReservada palavraReservada, PalavraReservada palavraAtual, String mensagemValidacao) 
			throws AnaliseSintaticaException {
		if (palavraReservada.equals(palavraAtual)) {
			return true;
		} 
		erro(mensagemValidacao);
		return false;
	}
	
	public boolean isPalavraReservadaTipoPrimitivo(PalavraReservada palavra, String mensagemValidacao) 
			throws AnaliseSintaticaException {
		if (tiposPrimitivos.contains(palavra)) {
			return true;
		}
		erro(mensagemValidacao);
		return false;
	}
	
	public boolean isPalavraReservadaTipoPrimitivoSemVoid(PalavraReservada palavra, String mensagemValidacao) 
			throws AnaliseSintaticaException {
		if (tiposPrimitivosSemVoid.contains(palavra)) {
			return true;
		}
		erro(mensagemValidacao);
		return false;
	}
	
	public boolean isPalavraReservadaTipoPrimitivo(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservadaTipoPrimitivo(palavra, "Não é um tipo primitivo");
	}
	
	public boolean isComentario(TokenVO tokenVO) {
		return PalavraReservada.COMENTARIO_GERAL.equals(tokenVO.getPalavraReservada()) ||
				PalavraReservada.COMENTARIO_LINHA.equals(tokenVO.getPalavraReservada());
	}
	
	public boolean isPalavraReservadaID(PalavraReservada palavra) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.ID, palavra, "funcao espera id dps de tipo macro");
	}
	
	public void erro(String mensagem) throws AnaliseSintaticaException {
		errosCount++;
		errors += mensagem+"\n";
		System.out.println(String.format("Erro %s: %s",errosCount,mensagem));
		if (errosCount >= MAX_ERROS) {
			throw new AnaliseSintaticaException(errors);
		}
	}
	
}
