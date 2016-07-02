package br.unibh.pyscal.enumerador;

public enum TipoVariavelEnum {
	BOOL,
	INTEGER,
	STRING,
	DOUBLE
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
}
