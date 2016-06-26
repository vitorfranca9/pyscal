package br.unibh.pyscal.exception;

import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;

public class AnaliseSintaticaException extends AnaliseAbstrataException {
	private static final long serialVersionUID = 4790611655304230694L;
	
	public AnaliseSintaticaException(String message, LinhaVO linha, TokenVO token) {
		super(message, linha, token);
		this.linha = linha;
		this.token = token;
	}

}
