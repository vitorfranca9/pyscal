package br.unibh.pyscal.vo;

import java.util.ArrayList;
import java.util.List;

import br.unibh.pyscal.enumerador.TipoComandoEnum;
import br.unibh.pyscal.enumerador.TipoRetornoMetodoEnum;
import lombok.Getter;
import lombok.Setter;

//@Data
public class MetodoVO {
	@Getter @Setter private List<LinhaVO> linhas;
	@Getter @Setter private List<VariavelVO> parametros;
	@Getter @Setter private List<VariavelVO> variaveis;
	@Getter @Setter private String nome;
	@Getter @Setter private TipoRetornoMetodoEnum tipoRetornoMetodo;
	@Getter @Setter private boolean main;
	@Getter @Setter private boolean estatico;
	@Getter @Setter private Object retorno;
	
	@Getter @Setter private boolean isComando;
	@Getter @Setter private TipoComandoEnum tipoComando;
	@Getter @Setter private VariavelVO variavelRetorno;
	@Getter @Setter private List<VariavelVO> variaveisRetorno = new ArrayList<>();
	@Getter @Setter private List<MetodoVO> subMetodos = new ArrayList<>();
	@Getter @Setter private List<MetodoVO> subMetodosFalse = new ArrayList<>();
	@Getter @Setter private MetodoVO metodoPai;
}
