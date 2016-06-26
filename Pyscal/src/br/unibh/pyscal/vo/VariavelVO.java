package br.unibh.pyscal.vo;

import br.unibh.pyscal.enumerador.TipoVariavel;
import lombok.Data;

@Data
public class VariavelVO {
	private LinhaVO linha;
	private TipoVariavel tipoVariavel;
	
}
