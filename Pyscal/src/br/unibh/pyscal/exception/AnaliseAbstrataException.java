package br.unibh.pyscal.exception;

import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;

public abstract class AnaliseAbstrataException extends Exception {
	private static final long serialVersionUID = 4714121030532839267L;
	protected static final String FRASE_ERRO = "Erro na linha %s próximo a palavra %s próximo a coluna %s: ";
	
	protected LinhaVO linha;
	protected TokenVO token;
	protected Integer coluna;
	
	public AnaliseAbstrataException(String message, LinhaVO linha, TokenVO token) {
		super(String.format(FRASE_ERRO, linha.getNumero(), token.getValor(), valorColuna(linha, token)) + message);
		this.linha = linha;
		this.token = token;
		this.coluna = valorColuna(linha,token);
	}
	
	private static int valorColuna(LinhaVO linha, TokenVO token) {
		for (TokenVO t : linha.getTokens()) {
			if (t.equals(token)) {
				return linha.getConteudo().indexOf(t.getValor());
			}
		} 
		return 0;
	}

	public LinhaVO getLinha() {
		return linha;
	}

	public void setLinha(LinhaVO linha) {
		this.linha = linha;
	}

	public TokenVO getToken() {
		return token;
	}

	public void setToken(TokenVO token) {
		this.token = token;
	}

	public Integer getColuna() {
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}
	
}
