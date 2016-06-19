package br.unibh.pyscal.vo;

import java.util.List;

import lombok.Data;

@Data
public class MetodoVO {

	private List<LinhaVO> linhas;
	private List<VariavelVO> variaveis;
	private boolean main;
	
}
