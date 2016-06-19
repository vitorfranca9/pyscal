package br.unibh.pyscal.analisador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.TokenVO;

public class AnalisadorSemantico extends AnalisadorAbstrato {

	private Map<String, TokenVO> mapaVariaveis;
//	private List<String> funcoes;	
	
	@Override
	protected void init() {
		super.init();
		mapaVariaveis = new HashMap<String, TokenVO>();
	}
	
	@Override
	public void analisar(ArquivoVO arquivo) throws Exception {
		init();
		this.arquivo = arquivo;
		compilar();
	}
	
	private void compilar() throws AnaliseSemanticaException {
		for (LinhaVO linha : arquivo.getLinhas()) {
			
			
			
		}
	}
	
	
	
}
