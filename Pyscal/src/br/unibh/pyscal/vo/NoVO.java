package br.unibh.pyscal.vo;

import java.util.ArrayList;
import java.util.List;

import br.unibh.pyscal.enumerador.TipoExpressaoEnum;
import lombok.Getter;
import lombok.Setter;

public class NoVO implements Cloneable {
	@Getter @Setter private List<NoVO> filhos = new ArrayList<>();
	@Getter @Setter private TipoExpressaoEnum tipoExpressao;
	@Getter @Setter private LinhaVO linha;
	@Getter @Setter private List<TokenVO> tokens = new ArrayList<>();
	
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
	
	@Override
	public String toString() {
		return "NoVO [filhos=" + filhos + ", tipoExpressao=" + tipoExpressao + ", linha=" + linha + ", tokens=" + tokens + "]";
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
