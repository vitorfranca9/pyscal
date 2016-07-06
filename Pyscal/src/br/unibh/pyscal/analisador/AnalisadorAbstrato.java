package br.unibh.pyscal.analisador;

import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;

public abstract class AnalisadorAbstrato {
	
	protected ArquivoVO arquivo;
	protected int numLinhaAtual;
	protected int numTokenAtual;
	
	public abstract void analisar(ArquivoVO arquivo) throws Exception;
	
	protected void init() {
		this.arquivo = null;
		this.numLinhaAtual = 1;
		this.numTokenAtual = 1;
	}
	
	protected void contarProximoToken(LinhaVO linhaAtual) {
		if (linhaAtual.getTokens().size() > numTokenAtual) {
			numTokenAtual++;
		} else {
			numLinhaAtual++;
			numTokenAtual = 1;
		}
	}
	
	protected void contarProximoToken() {
		if (getLinhaAtual().getTokens().size() > (numTokenAtual+1)) {
			numTokenAtual++;
		} else {
			numLinhaAtual++;
			numTokenAtual = 1;
		}
	}
//	protected boolean contarProximoToken() {
//		if (getLinhaAtual().getTokens().size() > numTokenAtual) {
//			numTokenAtual++;
//			return true;
//		}
//		return false;
//	}
	
	protected void contarProximaLinha() {
		numLinhaAtual++;
		numTokenAtual = 1;
	}
	
	protected TokenVO getTokenAtual() {
		return getToken(numTokenAtual);
//		return getLinhaAtual().getTokens().get(numTokenAtual-1);
	}
	
	protected TokenVO getToken(int num) {
		if (getLinhaAtual().getTokens().size() >= num) {
			return getLinhaAtual().getTokens().get(num-1);
		}
		return null;
	}
	
	protected LinhaVO getLinhaAtual() {
		return getLinha(numLinhaAtual);
	}
	
	protected LinhaVO getLinha(int numLinhaAtual) {
		LinhaVO linha = null;
		for (LinhaVO l : arquivo.getLinhas()) {
			if (l.getNumero() == numLinhaAtual) {
				linha = l;
				break;
			}
		}
		return linha;
	}

	public ArquivoVO getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoVO arquivo) {
		this.arquivo = arquivo;
	}

	public int getNumLinhaAtual() {
		return numLinhaAtual;
	}

	public void setNumLinhaAtual(int numLinhaAtual) {
		this.numLinhaAtual = numLinhaAtual;
	}

	public int getNumTokenAtual() {
		return numTokenAtual;
	}

	public void setNumTokenAtual(int numTokenAtual) {
		this.numTokenAtual = numTokenAtual;
	}
	
}
