package br.unibh.pyscal.vo;

import br.unibh.pyscal.enumerador.EscopoVariavelEnum;
import br.unibh.pyscal.enumerador.TipoVariavelEnum;

//@Data
//@ToString(of={"linha","nome","tokem"})
public class VariavelVO {
	private LinhaVO linha;
	private TipoVariavelEnum tipoVariavel;
	private String nome;
	private EscopoVariavelEnum escopoVariavel;
	private boolean array;
	private int qtdPosicoes;
//	private List<VariavelVO> valoresArray;
	private TokenVO tokem;
	
	
	public LinhaVO getLinha() {
		return linha;
	}

	public void setLinha(LinhaVO linha) {
		this.linha = linha;
	}

	public TipoVariavelEnum getTipoVariavel() {
		return tipoVariavel;
	}

	public void setTipoVariavel(TipoVariavelEnum tipoVariavel) {
		this.tipoVariavel = tipoVariavel;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EscopoVariavelEnum getEscopoVariavel() {
		return escopoVariavel;
	}

	public void setEscopoVariavel(EscopoVariavelEnum escopoVariavel) {
		this.escopoVariavel = escopoVariavel;
	}

	public boolean isArray() {
		return array;
	}

	public void setArray(boolean array) {
		this.array = array;
	}

	public int getQtdPosicoes() {
		return qtdPosicoes;
	}

	public void setQtdPosicoes(int qtdPosicoes) {
		this.qtdPosicoes = qtdPosicoes;
	}

	public TokenVO getTokem() {
		return tokem;
	}

	public void setTokem(TokenVO tokem) {
		this.tokem = tokem;
	}
	
	@Override
	public String toString() {
		return "VariavelVO [linha=" + linha + ", nome=" + nome + ", tokem=" + tokem + "]";
	}

	public Object getValor() throws Exception {
		Object valor = null;
		try {
			if (tokem != null) {
				switch (tipoVariavel) {
					case BOOL:
						valor = Boolean.valueOf(tokem.getValor().toLowerCase());
						break;
					case INTEGER:
						valor = Integer.valueOf(tokem.getValor());
						break;
					case DOUBLE:
						valor = Integer.valueOf(tokem.getValor());
						break;
					case STRING:
						valor = tokem.getValor();
						break;
					default: break;
				}
			}
		} catch (Exception e) {
			throw new Exception("Erro ao converter valor");
		}
		return valor;
	}
	
}
