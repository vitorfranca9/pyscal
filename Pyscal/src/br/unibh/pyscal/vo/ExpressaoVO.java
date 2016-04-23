package br.unibh.pyscal.vo;

import java.util.List;

import br.unibh.pyscal.enumerador.TipoExpressao;

public class ExpressaoVO {

	private List<TokenVO> tokens;
	private TipoExpressao tipoExpressao;
	
	public List<TokenVO> getTokens() {
		return tokens;
	}
	public void setTokens(List<TokenVO> tokens) {
		this.tokens = tokens;
	}
	public TipoExpressao getTipoExpressao() {
		return tipoExpressao;
	}
	public void setTipoExpressao(TipoExpressao tipoExpressao) {
		this.tipoExpressao = tipoExpressao;
	}
	
}
