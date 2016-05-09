package br.unibh.pyscal.vo;

import java.util.ArrayList;
import java.util.List;

import br.unibh.pyscal.enumerador.TipoExpressao;

public class NoVO implements Cloneable {
	private List<NoVO> filhos = new ArrayList<>();
	private TipoExpressao tipoExpressao;
	private LinhaVO linha;
	private List<TokenVO> tokens = new ArrayList<>();
	private int nivel;
	
	public NoVO getUltimoFilho() {
		if (filhos != null && !filhos.isEmpty()) {
			return getUltimoFilhoL(filhos.get(0));
		} else {
			return this;
		}
	}
	
	private NoVO getUltimoFilhoL(NoVO no) {
		if (no.getFilhos() != null && !no.getFilhos().isEmpty()) {
			return getUltimoFilhoL(no.getFilhos().get(0));
		} else {
			return no;
		}
	}
	
	public List<NoVO> getFilhos() {
		return filhos;
	}
	public void setFilhos(List<NoVO> filhos) {
		this.filhos = filhos;
	}
	public TipoExpressao getTipoExpressao() {
		return tipoExpressao;
	}
	public void setTipoExpressao(TipoExpressao tipoExpressao) {
		this.tipoExpressao = tipoExpressao;
	}
	public LinhaVO getLinha() {
		return linha;
	}
	public void setLinha(LinhaVO linha) {
		this.linha = linha;
	}
	public List<TokenVO> getTokens() {
		return tokens;
	}
	public void setTokens(List<TokenVO> tokens) {
		this.tokens = tokens;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	@Override
	public String toString() {
		return "NoVO [tipoExpressao=" + tipoExpressao + ", linha=" + linha + ", tokens=" + tokens + ", nivel=" + nivel
				+ "]";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
