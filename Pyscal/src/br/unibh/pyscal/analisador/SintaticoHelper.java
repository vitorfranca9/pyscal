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
	
	private final List<PalavraReservada> tiposPrimitivos = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.BOOL,
		PalavraReservada.INTEGER,
		PalavraReservada.STRING,
		PalavraReservada.DOUBLE,
		PalavraReservada.VOID
	});
	public final List<PalavraReservada> op = Arrays.asList(new PalavraReservada[]{
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
	public final List<PalavraReservada> opUnario = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.NOT,
		PalavraReservada.SUBTRAIR
	});
	private List<PalavraReservada> tiposPrimitivosSemVoid = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.BOOL,
		PalavraReservada.INTEGER,
		PalavraReservada.STRING,
		PalavraReservada.DOUBLE,
	});
	private final List<PalavraReservada> constantes = Arrays.asList(new PalavraReservada[]{
		PalavraReservada.CONST_STRING,
		PalavraReservada.CONSTDOUBLE,
		PalavraReservada.CONSTINTEGER,
		PalavraReservada.TRUE,
		PalavraReservada.FALSE
	});
	private final List<PalavraReservada> cmd = Arrays.asList(new PalavraReservada[]{
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
				if (!isComentarioSemErro(tokenVO)){
					tokensLinha.add(tokenVO);
					conteudo += tokenVO.getValor()+" ";
				}
			}
			if (!tokensLinha.isEmpty()) {
				LinhaVO linhaExecutavel = new LinhaVO();
				linhaExecutavel.getTokens().addAll(tokensLinha);
				linhaExecutavel.setNumeroReal(linhaVO.getNumero());
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
				if (!isComentarioSemErro(tokenVO)) {
					tokensAExecutar.add(tokenVO);
				}
			}
		}
		return tokensAExecutar;
	}
	
//		private boolean isPalavraReservadaID(PalavraReservada palavra) throws AnaliseSintaticaException {
//			return isPalavraReservada(PalavraReservada.ID, palavra, "funcao espera id dps de tipo macro");
//		}
	
	public boolean isPalavraReservadaConstIntegerSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.CONSTINTEGER, palavra);
	}
	
	public boolean isPalavraReservadaConstDoubleSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.CONSTDOUBLE, palavra);
	}
	
	public boolean isPalavraReservadaEndSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.END, palavra);
	}
	
	public boolean isPalavraReservadaElseSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.ELSE, palavra);
	}
	
	public boolean isPalavraReservadaAbreParentesesSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.ABRE_PARENTESES, palavra);
	}
	
	public boolean isPalavraReservadaIgualSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.IGUAL, palavra);
	}
	
	public boolean isPalavraReservadaAbreColcheteSemErro(PalavraReservada palavra) {
		return isPalavraReservadaSemErro(PalavraReservada.ABRE_COLCHETE, palavra);
	}
	
	public boolean isPalavraReservadaDefSemErro(PalavraReservada palavra) {
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
	
	public boolean isPalavraReservadaClassSemErro(PalavraReservada palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservada.CLASS, palavraReservada);
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
		return opUnario.contains(palavraReservada);
	}
	
	public boolean isPalavraReservadaVectorSemErro(PalavraReservada palavraReservada) {
		return PalavraReservada.VECTOR.equals(palavraReservada);
	}
	
	public boolean isPalavraReservadaFechaParentesesSemErro(PalavraReservada palavraReservada) {
		return PalavraReservada.FECHA_PARENTESES.equals(palavraReservada);
	}
	
	public boolean isPalavraReservadaPontoVirgulaSemErro(PalavraReservada palavraReservada) {
		return PalavraReservada.FECHA_PARENTESES.equals(palavraReservada);
	}
	
	public boolean isComentarioSemErro(TokenVO tokenVO) {
		return PalavraReservada.COMENTARIO_GERAL.equals(tokenVO.getPalavraReservada()) ||
				PalavraReservada.COMENTARIO_LINHA.equals(tokenVO.getPalavraReservada());
	}
	
	///////////////////////// lançando exceptions //////////////////////////
	public boolean isPalavraReservadaPonto(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.PONTO, "esperava ponto final", linha, token);
	}
	
	public boolean isPalavraReservadaIgual(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.IGUAL, "esperava ponto final", linha, token);
	}
	
	public boolean isPalavraReservadaClass(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.CLASS, "esperava class", linha, token);
	}
	
	public boolean isPalavraReservadaString(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.STRING, "esperava string", linha, token);
	}
	
	public boolean isPalavraReservadaReturn(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.RETURN, "funcao esperava return", linha, token);
	}
	
	public boolean isPalavraReservadaPontoVirgula(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.PONTO_VIRGULA, "funcao espera ponto virgula dps de end", linha, token);
	}
	
	public boolean isPalavraReservadaVirgula(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.VIRGULA, "funcao esperava token virgula", linha, token);
	}
	
	public boolean isPalavraReservadaEnd(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.END, "funcao espera end dps de retorno", linha, token);
	}
	
	public boolean isPalavraReservadaElse(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.ELSE, "funcao esperava else", linha, token);
	}
	
	public boolean isPalavraReservadaDoisPontos(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.DOIS_PONTOS, "funcao espera dois pontos dps de fecha parenteses", linha, token);
	}
	
	public boolean isPalavraReservadaAbreParenteses(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.ABRE_PARENTESES, "funcao espera abre parenteses dps do id", linha, token);
	}
	
	public boolean isPalavraReservadaFechaParenteses(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.FECHA_PARENTESES, "funcao espera fecha parenteses dps de lista arg", linha, token);
	}
	
	public boolean isPalavraReservadaAbreColchete(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.ABRE_COLCHETE, "Esperava abre colchete", linha, token);
	}
	
	public boolean isPalavraReservadaFechaColchete(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.FECHA_COLCHETE, "Esperava fecha colchete", linha, token);
	}
	
	public boolean isPalavraReservadaDef(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.DEF, "funcao deve iniciar com def", linha, token);
	}
	
	public boolean isPalavraReservadaDefStatic(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.DEFSTATIC, "funcao main deve iniciar com defstatic", linha, token);
	}
	
	public boolean isPalavraReservadaVoid(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.VOID, "esperava void", linha, token);
	}
	
	public boolean isPalavraReservadaMain(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.MAIN, "esperava main", linha, token);
	}
	
	public boolean isPalavraReservadaIf(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.IF, "esperava if", linha, token);
	}
	
	public boolean isPalavraReservadaWhile(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.WHILE, "esperava while", linha, token);
	}

	public boolean isPalavraReservadaWrite(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.WRITE, "esperava write", linha, token);
	}
	
	public boolean isPalavraReservadaWriteLn(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.WRITELN, "esperava writeln", linha, token);
	}
	
	public boolean isPalavraReservadaTipoPrimitivo(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservadaTipoPrimitivo("Não é um tipo primitivo", linha, token);
	}
	
	public boolean isPalavraReservadaID(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservada.ID, "funcao espera id dps de tipo macro", linha, token);
	}
	
	public boolean isPalavraReservadaOp(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		if(isPalavraReservadaOpSemErro(token.getPalavraReservada())) {
			return true;
		}
		erro("Esperava OP", linha, token);
		return false;
	}
	
	public boolean isPalavraReservadaOpUnario(String mensagemValidacao, LinhaVO linha, TokenVO token) 
			throws AnaliseSintaticaException {
		if (isPalavraReservadaOpUnarioSemErro(token.getPalavraReservada())) {
			return true;
		} 
		erro(mensagemValidacao, linha, token);
		return false;
	}
	
	public boolean isPalavraReservada(PalavraReservada palavraReservada, String mensagemValidacao, LinhaVO linha, TokenVO token) 
			throws AnaliseSintaticaException {
		if (palavraReservada.equals(token.getPalavraReservada())) {
			return true;
		} 
		erro(mensagemValidacao, linha, token);
		return false;
	}
	
	public boolean isPalavraReservadaTipoPrimitivo(String mensagemValidacao, LinhaVO linha, TokenVO token) 
			throws AnaliseSintaticaException {
		if (tiposPrimitivos.contains(token.getPalavraReservada())) {
			return true;
		}
		erro(mensagemValidacao, linha, token);
		return false;
	}
	
	public boolean isPalavraReservadaTipoPrimitivoSemVoid(String mensagemValidacao, LinhaVO linha, TokenVO token) 
			throws AnaliseSintaticaException {
		if (tiposPrimitivosSemVoid.contains(token.getPalavraReservada())) {
			return true;
		}
		erro(mensagemValidacao, linha, token);
		return false;
	}
	
	public void erro(String mensagem, LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		errosCount++;
		String msgErro = String.format("Erro %s na linha %s no lexema '%s' proximo a coluna %s: %s",
			errosCount, linha.getNumeroReal()+"", token.getValor(), getIndexToken(linha, token), mensagem)+"\n";
		errors += msgErro;
		System.out.println(msgErro);
		if (errosCount >= MAX_ERROS) {
			throw new AnaliseSintaticaException(errors, linha, token);
		}
	}
	
	private int getIndexToken(LinhaVO linha, TokenVO token) {
		int indexOf = linha.getConteudo().indexOf(token.getValor());
		return indexOf;
	}
	
}
