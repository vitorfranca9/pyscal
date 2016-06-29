package br.unibh.pyscal.vo;

import java.util.ArrayList;
import java.util.List;

import br.unibh.pyscal.enumerador.TipoRetornoMetodoEnum;
import lombok.Getter;
import lombok.Setter;

//@Data
public class MetodoVO {
	@Getter @Setter private List<LinhaVO> linhas;
	@Getter @Setter private List<VariavelVO> parametros;
	@Getter @Setter private List<VariavelVO> variaveis;
	@Getter @Setter private List<ComandoVO> comandos = new ArrayList<>();
	@Getter @Setter private String nome;
	@Getter @Setter private TipoRetornoMetodoEnum tipoRetornoMetodo;
	@Getter @Setter private boolean main;
	@Getter @Setter private boolean estatico;
	@Getter @Setter private Object retorno;
	
}
