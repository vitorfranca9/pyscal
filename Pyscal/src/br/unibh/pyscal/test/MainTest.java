package br.unibh.pyscal.test;

import br.unibh.pyscal.exception.AnaliseLexicaException;
import br.unibh.pyscal.exception.AnaliseSemanticaException;
import br.unibh.pyscal.exception.AnaliseSintaticaException;
import br.unibh.pyscal.util.PyscalConstantUtil;
import br.unibh.pyscal.vo.CompiladorVO;

public class MainTest {
	
	public static void main(String[] args) {
		String fullPath = PyscalConstantUtil.ArquivosTesteSemantico.COMANDOS4;
//		ArquivoVO arquivo = FileUtil.montarArquivo(fullPath);
		try {
			CompiladorVO compilador = new CompiladorVO(fullPath);
			compilador.analisar();
			compilador.compilar(fullPath);
			compilador.rodar();
		} catch (Exception e) {
			if (e instanceof AnaliseLexicaException) {
				System.out.println("Erro léxico: "+e.getMessage());
			} else if (e instanceof AnaliseSintaticaException) {
				System.out.println("Erro sintático: "+e.getMessage());
			} else if (e instanceof AnaliseSemanticaException){
				System.out.println("Erro semântico: "+e.getMessage());
			} else {
				e.printStackTrace();
				System.out.println("Erro: "+e.getMessage());
			}
		}
	}
//	System.out.println("Total"+2*(2+2));
}
