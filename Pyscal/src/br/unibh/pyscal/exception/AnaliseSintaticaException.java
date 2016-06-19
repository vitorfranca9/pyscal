package br.unibh.pyscal.exception;

import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;
import lombok.Getter;
import lombok.Setter;

public class AnaliseSintaticaException extends Exception {
	private static final long serialVersionUID = 4790611655304230694L;
	private static final String FRASE_ERRO = "Erro na linha %s próximo a palavra %s próximo a coluna %s";
	
	@Getter @Setter private LinhaVO linha;
	@Getter @Setter private TokenVO token;
	@Getter @Setter private Integer coluna;
	
	public AnaliseSintaticaException() {
		super();
	}
	public AnaliseSintaticaException(String message) {
		super(message);
	}
	
	public AnaliseSintaticaException(LinhaVO linha, TokenVO token) {
		super(String.format(FRASE_ERRO, linha.getNumero(),token.getValor()));
		this.linha = linha;
		this.token = token;
	}

	public AnaliseSintaticaException(String message, LinhaVO linha, TokenVO token) {
		super(String.format(FRASE_ERRO, linha.getNumero(),token.getValor(), 
				valorColuna(linha, token))+": " + message);
		this.linha = linha;
		this.token = token;
		this.coluna = valorColuna(linha,token);
	}
	
	private static int valorColuna(LinhaVO linha, TokenVO token) {
		for (TokenVO t : linha.getTokens()) {
			if (t.equals(token)) {
				System.out.println();
				return linha.getConteudo().indexOf(t.getValor());
			}
		} 
		
		return 0;
	}

}
