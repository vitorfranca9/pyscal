package br.unibh.pyscal.analisador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.unibh.pyscal.enumerador.PalavraReservadaEnum;
import br.unibh.pyscal.exception.AnaliseSintaticaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;

//HELPER
public class AnalisadorSintaticoHelper {
		
	private final int MAX_ERROS = 5;
	public int errosCount = 0;
	public String errors = "";
	private static AnalisadorSintaticoHelper instancia;
	
	private AnalisadorSintaticoHelper() { }
	
	public static AnalisadorSintaticoHelper getInstancia() {
		if (instancia == null) {
			instancia = new AnalisadorSintaticoHelper();
		}
		return instancia;
	}
	
	private final List<PalavraReservadaEnum> tiposPrimitivos = Arrays.asList(new PalavraReservadaEnum[]{
		PalavraReservadaEnum.BOOL,
		PalavraReservadaEnum.INTEGER,
		PalavraReservadaEnum.STRING,
		PalavraReservadaEnum.DOUBLE,
		PalavraReservadaEnum.VOID
	});
	public final List<PalavraReservadaEnum> op = Arrays.asList(new PalavraReservadaEnum[]{
		PalavraReservadaEnum.OR,
		PalavraReservadaEnum.AND,
		PalavraReservadaEnum.MENOR,
		PalavraReservadaEnum.MENOR_IGUAL,
		PalavraReservadaEnum.MAIOR,
		PalavraReservadaEnum.MAIOR_IGUAL,
		PalavraReservadaEnum.IGUAL_IGUAL,
		PalavraReservadaEnum.DIFERENTE,
		PalavraReservadaEnum.DIVIDIR,
		PalavraReservadaEnum.MULTIPLICAR,
		PalavraReservadaEnum.SUBTRAIR,
		PalavraReservadaEnum.SOMAR
	});
	public final List<PalavraReservadaEnum> opUnario = Arrays.asList(new PalavraReservadaEnum[]{
		PalavraReservadaEnum.NOT,
		PalavraReservadaEnum.SUBTRAIR
	});
	private List<PalavraReservadaEnum> tiposPrimitivosSemVoid = Arrays.asList(new PalavraReservadaEnum[]{
		PalavraReservadaEnum.BOOL,
		PalavraReservadaEnum.INTEGER,
		PalavraReservadaEnum.STRING,
		PalavraReservadaEnum.DOUBLE,
	});
	private final List<PalavraReservadaEnum> constantes = Arrays.asList(new PalavraReservadaEnum[]{
		PalavraReservadaEnum.CONSTSTRING,
		PalavraReservadaEnum.CONSTDOUBLE,
		PalavraReservadaEnum.CONSTINTEGER,
		PalavraReservadaEnum.TRUE,
		PalavraReservadaEnum.FALSE
	});
	private final List<PalavraReservadaEnum> cmd = Arrays.asList(new PalavraReservadaEnum[]{
		PalavraReservadaEnum.IF,
		PalavraReservadaEnum.WHILE,
		PalavraReservadaEnum.WRITE,
		PalavraReservadaEnum.WRITELN,
		PalavraReservadaEnum.ID
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
	
	public boolean isPalavraReservadaConstStringSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.CONSTSTRING, palavra);
	}
	
	public boolean isPalavraReservadaConstBoolSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.TRUE, palavra) || 
			isPalavraReservadaSemErro(PalavraReservadaEnum.FALSE, palavra);
	}
	
	public boolean isPalavraReservadaConstIntegerSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.CONSTINTEGER, palavra);
	}
	
	public boolean isPalavraReservadaConstDoubleSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.CONSTDOUBLE, palavra);
	}
	
	public boolean isPalavraReservadaEndSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.END, palavra);
	}
	
	public boolean isPalavraReservadaElseSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.ELSE, palavra);
	}
	
	public boolean isPalavraReservadaAbreParentesesSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.ABRE_PARENTESES, palavra);
	}
	
	public boolean isPalavraReservadaIgualSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.IGUAL, palavra);
	}
	
	public boolean isPalavraReservadaAbreColcheteSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.ABRE_COLCHETE, palavra);
	}
	
	public boolean isPalavraReservadaDefOuDefstaticSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaDefSemErro(palavra) || isPalavraReservadaDefStaticSemErro(palavra);
	}
	
	public boolean isPalavraReservadaDefSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.DEF, palavra);
	}
	
	public boolean isPalavraReservadaDefStaticSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.DEFSTATIC, palavra);
	}
	
	public boolean isPalavraReservadaReturnSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.RETURN, palavra);
	}
	
	public boolean isPalavraReservadaVirgulaSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.VIRGULA, palavra);
	}
	
	public boolean isPalavraReservadaIDSemErro(PalavraReservadaEnum palavra) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.ID, palavra);
	}
	
	public boolean isPalavraReservadaIfSemErro(PalavraReservadaEnum palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.IF, palavraReservada);
	}
	
	public boolean isPalavraReservadaWhileSemErro(PalavraReservadaEnum palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.WHILE, palavraReservada);
	}
	
	public boolean isPalavraReservadaWriteSemErro(PalavraReservadaEnum palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.WRITE, palavraReservada);
	}
	
	public boolean isPalavraReservadaWriteLnSemErro(PalavraReservadaEnum palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.WRITELN, palavraReservada);
	}
	
	public boolean isPalavraReservadaClassSemErro(PalavraReservadaEnum palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.CLASS, palavraReservada);
	}
	
	public boolean isPalavraReservadaVoidSemErro(PalavraReservadaEnum palavraReservada) {
		return isPalavraReservadaSemErro(PalavraReservadaEnum.VOID, palavraReservada);
	}
	
	public boolean isPalavraReservadaSemErro(PalavraReservadaEnum palavraReservada, PalavraReservadaEnum palavraAtual) {
		return palavraReservada.equals(palavraAtual);
	}
	
	public boolean isPalavraReservadaOpSemErro(PalavraReservadaEnum palavra) {
		return op.contains(palavra);
	}
	
	public boolean isPalavraReservadaTipoPrimitivoSemErro(PalavraReservadaEnum palavra) {
		return tiposPrimitivos.contains(palavra);
	}
	
	public boolean isPalavraReservadaTipoPrimitivoSemVoidSemErro(PalavraReservadaEnum palavra) {
		return tiposPrimitivosSemVoid.contains(palavra);
	}
	
	public boolean isPalavraReservadaConstanteSemErro(PalavraReservadaEnum palavra) {
		return constantes.contains(palavra);
	}
	
	public boolean isPalavraReservadaCmdSemErro(PalavraReservadaEnum palavra) {
		return cmd.contains(palavra);
	}
	
	public boolean isPalavraReservadaOpUnarioSemErro(PalavraReservadaEnum palavraReservada) {
		return opUnario.contains(palavraReservada);
	}
	
	public boolean isPalavraReservadaVectorSemErro(PalavraReservadaEnum palavraReservada) {
		return PalavraReservadaEnum.VECTOR.equals(palavraReservada);
	}
	
	public boolean isPalavraReservadaFechaParentesesSemErro(PalavraReservadaEnum palavraReservada) {
		return PalavraReservadaEnum.FECHA_PARENTESES.equals(palavraReservada);
	}
	
	public boolean isPalavraReservadaPontoVirgulaSemErro(PalavraReservadaEnum palavraReservada) {
		return PalavraReservadaEnum.PONTO_VIRGULA.equals(palavraReservada);
	}
	
	public boolean isComentarioSemErro(TokenVO tokenVO) {
		return PalavraReservadaEnum.COMENTARIO_GERAL.equals(tokenVO.getPalavraReservada()) ||
				PalavraReservadaEnum.COMENTARIO_LINHA.equals(tokenVO.getPalavraReservada());
	}
	
	// ----------------------- LANÇANDO EXCEPTIONS ----------------------------//
	public boolean isPalavraReservadaPonto(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.PONTO, "esperava ponto final", linha, token);
	}
	
	public boolean isPalavraReservadaIgual(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.IGUAL, "esperava ponto final", linha, token);
	}
	
	public boolean isPalavraReservadaClass(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.CLASS, "esperava class", linha, token);
	}
	
	public boolean isPalavraReservadaString(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.STRING, "esperava string", linha, token);
	}
	
	public boolean isPalavraReservadaReturn(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.RETURN, "funcao esperava return", linha, token);
	}
	
	public boolean isPalavraReservadaPontoVirgula(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.PONTO_VIRGULA, "funcao espera ponto virgula dps de end", linha, token);
	}
	
	public boolean isPalavraReservadaVirgula(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.VIRGULA, "funcao esperava token virgula", linha, token);
	}
	
	public boolean isPalavraReservadaEnd(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.END, "funcao espera end dps de retorno", linha, token);
	}
	
	public boolean isPalavraReservadaElse(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.ELSE, "funcao esperava else", linha, token);
	}
	
	public boolean isPalavraReservadaDoisPontos(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.DOIS_PONTOS, "funcao espera dois pontos dps de fecha parenteses", linha, token);
	}
	
	public boolean isPalavraReservadaAbreParenteses(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.ABRE_PARENTESES, "funcao espera abre parenteses dps do id", linha, token);
	}
	
	public boolean isPalavraReservadaFechaParenteses(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.FECHA_PARENTESES, "funcao espera fecha parenteses dps de lista arg", linha, token);
	}
	
	public boolean isPalavraReservadaAbreColchete(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.ABRE_COLCHETE, "Esperava abre colchete", linha, token);
	}
	
	public boolean isPalavraReservadaFechaColchete(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.FECHA_COLCHETE, "Esperava fecha colchete", linha, token);
	}
	
	public boolean isPalavraReservadaDef(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.DEF, "funcao deve iniciar com def", linha, token);
	}
	
	public boolean isPalavraReservadaDefStatic(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.DEFSTATIC, "funcao main deve iniciar com defstatic", linha, token);
	}
	
	public boolean isPalavraReservadaVoid(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.VOID, "esperava void", linha, token);
	}
	
	public boolean isPalavraReservadaMain(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.MAIN, "esperava main", linha, token);
	}
	
	public boolean isPalavraReservadaIf(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.IF, "esperava if", linha, token);
	}
	
	public boolean isPalavraReservadaWhile(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.WHILE, "esperava while", linha, token);
	}

	public boolean isPalavraReservadaWrite(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.WRITE, "esperava write", linha, token);
	}
	
	public boolean isPalavraReservadaWriteLn(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.WRITELN, "esperava writeln", linha, token);
	}
	
	public boolean isPalavraReservadaTipoPrimitivo(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservadaTipoPrimitivo("Não é um tipo primitivo", linha, token);
	}
	
	public boolean isPalavraReservadaID(LinhaVO linha, TokenVO token) throws AnaliseSintaticaException {
		return isPalavraReservada(PalavraReservadaEnum.ID, "funcao espera id dps de tipo macro", linha, token);
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
	
	public boolean isPalavraReservada(PalavraReservadaEnum palavraReservada, String mensagemValidacao, LinhaVO linha, TokenVO token) 
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
