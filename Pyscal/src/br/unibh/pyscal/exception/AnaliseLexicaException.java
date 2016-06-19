package br.unibh.pyscal.exception;

import br.unibh.pyscal.vo.LinhaVO;
import lombok.Getter;
import lombok.Setter;

public class AnaliseLexicaException extends Exception {
	private static final long serialVersionUID = -1395958669132540575L;
	private static final String FRASE_ERRO = "Erro na linha %s próximo a palavra %s próximo a coluna %s";
	
	@Getter @Setter private LinhaVO linha;
	@Getter @Setter private String palavra;
	@Getter @Setter private Integer coluna;
	
	public AnaliseLexicaException(LinhaVO linha, String palavra) {
		super(String.format(FRASE_ERRO, linha.getNumero(),palavra));
		this.linha = linha;
		this.palavra = palavra;
	}

	public AnaliseLexicaException(String message, LinhaVO linha, String palavra) {
		super(String.format(FRASE_ERRO, linha.getNumero(),palavra, valorColuna(linha, palavra))+": " + message);
		this.linha = linha;
		this.palavra = palavra;
	}
	
	private static int valorColuna(LinhaVO linha, String palavra) {
		return 0;
	}

}
