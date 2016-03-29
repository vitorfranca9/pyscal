package br.unibh.exception;

import br.unibh.vo.LinhaVO;

public class AnaliseLexicaException extends Exception {
	private static final long serialVersionUID = -1395958669132540575L;
	private static final String FRASE_ERRO = "Erro na linha %s próximo a palavra %s: ";
	protected LinhaVO linha;
	protected String palavra;
	
	public AnaliseLexicaException() {
		super();
	}

	public AnaliseLexicaException(LinhaVO linha, String palavra) {
		super();
		this.linha = linha;
		this.palavra = palavra;
	}

	public AnaliseLexicaException(String message, LinhaVO linha, String palavra) {
		super(String.format(FRASE_ERRO, linha.getNumero(),palavra) + message);
		this.linha = linha;
		this.palavra = palavra;
	}

}
