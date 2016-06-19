package br.unibh.pyscal.analisador;

import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import lombok.Getter;
import lombok.Setter;

public abstract class AnalisadorAbstrato {
	
	@Getter @Setter protected ArquivoVO arquivo;
	@Getter @Setter protected int numLinhaAtual;
	@Getter @Setter protected int numTokenAtual;
	
	public abstract void analisar(ArquivoVO arquivo) throws Exception;
	
	protected void init() {
		this.arquivo = null;
		this.numLinhaAtual = 1;
		this.numTokenAtual = 1;
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
	
}
