package br.unibh.pyscal.exception;

import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;
import lombok.Getter;
import lombok.Setter;

public abstract class AnaliseAbstrataException extends Exception {
	private static final long serialVersionUID = 4714121030532839267L;
	protected static final String FRASE_ERRO = "Erro na linha %s próximo a palavra %s próximo a coluna %s: ";
	
	@Getter @Setter protected LinhaVO linha;
	@Getter @Setter protected TokenVO token;
	@Getter @Setter protected Integer coluna;
	
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

}
