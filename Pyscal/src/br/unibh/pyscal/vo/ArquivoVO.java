package br.unibh.pyscal.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArquivoVO {

	private String nomeArquivo = "";
	private String conteudoOriginal = "";
	private List<LinhaVO> linhas = new ArrayList<>();
	private ArvoreVO arvore = new ArvoreVO();
	private Map<Integer, NoVO> mapa = new HashMap<Integer, NoVO>();
	private static Integer nivelAtual = 1;
	private NoVO noRaiz;
	
	public void adicionarNoMapa(NoVO no) {
		if (mapa.get(nivelAtual) == null) {
			mapa.put(nivelAtual, new NoVO());
		}
		adicionarNoMapa(nivelAtual,no);
		
		if (arvore.getNoRaiz() == null) {
			arvore.setNoRaiz(no);
		}
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
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public String getConteudoOriginal() {
		return conteudoOriginal;
	}
	public void setConteudoOriginal(String conteudoOriginal) {
		this.conteudoOriginal = conteudoOriginal;
	}
	public List<LinhaVO> getLinhas() {
		return linhas;
	}
	public void setLinhas(List<LinhaVO> linhas) {
		this.linhas = linhas;
	}
	public ArvoreVO getArvore() {
		return arvore;
	}
	public void setArvore(ArvoreVO arvore) {
		this.arvore = arvore;
	}
	public Map<Integer, NoVO> getMapa() {
		return mapa;
	}
	public void setMapa(Map<Integer, NoVO> mapa) {
		this.mapa = mapa;
	}
	public NoVO getNoRaiz() {
		return noRaiz;
	}
	public void setNoRaiz(NoVO noRaiz) {
		this.noRaiz = noRaiz;
	}
	
}
