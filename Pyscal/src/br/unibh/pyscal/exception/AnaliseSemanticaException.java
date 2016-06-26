package br.unibh.pyscal.exception;

import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;

public class AnaliseSemanticaException extends AnaliseAbstrataException {
	private static final long serialVersionUID = 7136580963972569107L;
	
	public AnaliseSemanticaException(String message, LinhaVO linha, TokenVO token) {
		super(message, linha, token);
	}

}
