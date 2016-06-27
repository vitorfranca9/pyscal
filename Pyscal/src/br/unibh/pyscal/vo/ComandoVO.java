package br.unibh.pyscal.vo;

import java.util.List;

import br.unibh.pyscal.enumerador.TipoComandoEnum;
import lombok.Data;

@Data
public class ComandoVO {
	private List<LinhaVO> linhas;
	private TipoComandoEnum tipoComando;
}
