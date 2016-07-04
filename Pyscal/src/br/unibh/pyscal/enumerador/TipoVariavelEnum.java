package br.unibh.pyscal.enumerador;

import br.unibh.pyscal.vo.MetodoVO;
import br.unibh.pyscal.vo.TokenVO;

public enum TipoVariavelEnum {
	BOOL,
	INTEGER,
	STRING,
	DOUBLE,
	ID //buscar do mapa
	,VOID//?
	;
	
	public String getAssembleType() {
		switch (this) {
			case INTEGER:
				return "i";
			case STRING:
				return "a";
			case DOUBLE:
				return "d";
			case BOOL:
				return "i";
			default: return null;
		}
	}
	
	public String getAssembleInvokeType() {
		switch (this) {
			case INTEGER:
				return "I";
			case DOUBLE:
				return "D";
//				return "Ljava/lang/Double;";
			case BOOL:
				return "Z";
			case STRING:
				return "Ljava/lang/String;";
			case VOID:
				return "V";
			default: return null;
		}
//		return getAssembleType().toUpperCase();
	}
	
	public static TipoVariavelEnum getTipoVariavel(TokenVO tokenAtual) {
		switch (tokenAtual.getPalavraReservada()) {
			case TRUE:
				return TipoVariavelEnum.BOOL;
			case FALSE:
				return TipoVariavelEnum.BOOL;
			case BOOL:
				return TipoVariavelEnum.BOOL;
			case INTEGER:
				return TipoVariavelEnum.INTEGER;
			case CONSTINTEGER:
				return TipoVariavelEnum.INTEGER;
			case DOUBLE:
				return TipoVariavelEnum.DOUBLE;
			case CONSTDOUBLE:
				return TipoVariavelEnum.DOUBLE;
			case STRING:
				return TipoVariavelEnum.STRING;
			case CONSTSTRING:
				return TipoVariavelEnum.STRING;
			case ID:
				return TipoVariavelEnum.ID;
			default: return null;
		}
	}
	
}
