package br.unibh.pyscal.vo;

import java.util.List;

import br.unibh.pyscal.enumerador.EscopoVariavelEnum;
import br.unibh.pyscal.enumerador.TipoVariavelEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(of={"linha","nome","tokem"})
public class VariavelVO {
	private LinhaVO linha;
	private TipoVariavelEnum tipoVariavel;
	private String nome;
	private EscopoVariavelEnum escopoVariavel;
	private boolean array;
	private int qtdPosicoes;
//	private List<VariavelVO> valoresArray;
	private TokenVO tokem;
	
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
