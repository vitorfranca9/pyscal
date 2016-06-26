package br.unibh.pyscal.vo;

import java.util.List;

import br.unibh.pyscal.enumerador.EscopoVariavel;
import br.unibh.pyscal.enumerador.TipoVariavel;
import lombok.Data;

@Data
public class VariavelVO {
	private LinhaVO linha;
	private TipoVariavel tipoVariavel;
	private String nome;
	private EscopoVariavel escopoVariavel;
	private boolean array;
	private int qtdPosicoes;
	private List<VariavelVO> valoresArray;
}
