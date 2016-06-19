package br.unibh.pyscal.analisador;

import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.vo.ArquivoVO;
import br.unibh.pyscal.vo.ClasseVO;
import br.unibh.pyscal.vo.LinhaVO;
import br.unibh.pyscal.vo.MetodoVO;

public class AnalisadorSemantico extends AnalisadorAbstrato {

//	private Map<String, TokenVO> mapaVariaveis;
//	private List<String> funcoes;	
	private ClasseVO classe;
	private SintaticoHelper sintaticoHelper;
	
	@Override
	protected void init() {
		super.init();
		classe = new ClasseVO();
		sintaticoHelper = SintaticoHelper.getInstancia();
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
			if (sintaticoHelper.isPalavraReservadaClassSemErro(getTokenAtual().getPalavraReservada())) {
				contarProximaLinha();
			} else {
				
				if (sintaticoHelper.isPalavraReservadaDefSemErro(getTokenAtual().getPalavraReservada())) {
					System.out.println();
					MetodoVO metodoVO = new MetodoVO();
				}
				
			}
			System.out.println();
		}
	}
	
}
