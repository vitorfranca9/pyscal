package br.unibh.pyscal.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class ArquivoVO {

	private static Integer nivelAtual = 1;
	
	@Getter @Setter private String nomeArquivo = "";
	@Getter @Setter private String conteudoOriginal = "";
	@Getter @Setter private List<LinhaVO> linhas = new ArrayList<>();
	@Getter @Setter private Map<Integer, NoVO> mapa = new HashMap<Integer, NoVO>();
	@Getter @Setter private NoVO noRaiz;
	
	public void adicionarNoMapa(NoVO no) {
		if (mapa.get(nivelAtual) == null) {
			mapa.put(nivelAtual, new NoVO());
		}
		adicionarNoMapa(nivelAtual,no);
	}
	
	public void adicionarNoMapa(Integer nivel, NoVO no) {
		NoVO noAtual = mapa.get(nivelAtual);
		noAtual.getFilhos().add(no);
		mapa.put(nivel, noAtual);
		nivelAtual++;
//		nivelAtual = nivel;
	}
	
	public void adicionarLinha(int numeroLinha, String conteudo) {
		linhas.add(new LinhaVO(numeroLinha, conteudo));
	}
	
}
