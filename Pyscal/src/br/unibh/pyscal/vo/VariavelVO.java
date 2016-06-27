package br.unibh.pyscal.vo;

import java.util.List;

import br.unibh.pyscal.enumerador.EscopoVariavelEnum;
import br.unibh.pyscal.enumerador.TipoVariavelEnum;
import lombok.Data;

@Data
public class VariavelVO {
	private LinhaVO linha;
	private TipoVariavelEnum tipoVariavel;
	private String nome;
	private EscopoVariavelEnum escopoVariavel;
	private boolean array;
	private int qtdPosicoes;
	private List<VariavelVO> valoresArray;
}
