package br.unibh.pyscal.vo;

import java.util.List;

import br.unibh.pyscal.enumerador.TipoRetornoMetodo;
import lombok.Data;

@Data
public class MetodoVO {
	private List<LinhaVO> linhas;
	private List<VariavelVO> parametros;
	private List<VariavelVO> variaveis;
	private List<ComandoVO> funcoes;
	private TipoRetornoMetodo tipoRetornoMetodo;
	private boolean main;
	private boolean estatico;
	
}
