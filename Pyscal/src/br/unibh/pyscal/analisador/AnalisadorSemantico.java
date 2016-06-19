package br.unibh.pyscal.analisador;

import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.ClasseVO;
import br.unibh.pyscal.vo.LinhaVO;

public class AnalisadorSemantico extends AnalisadorAbstrato {

//	private Map<String, TokenVO> mapaVariaveis;
//	private List<String> funcoes;	
	private ClasseVO classe;
	
	@Override
	protected void init() {
		super.init();
		classe = new ClasseVO();
//		mapaVariaveis = new HashMap<String, TokenVO>();
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
