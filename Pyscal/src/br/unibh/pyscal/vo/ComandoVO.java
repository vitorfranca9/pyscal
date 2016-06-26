package br.unibh.pyscal.vo;

import java.util.List;

import br.unibh.pyscal.enumerador.TipoComando;
import lombok.Data;

@Data
public class ComandoVO {
	private List<LinhaVO> linhas;
	private TipoComando tipoComando;
}
