package br.unibh.pyscal.enumerador;

public enum TipoVariavelEnum {
	BOOL,
	INTEGER,
	STRING,
	DOUBLE;
	
	public String getAssembleType() {
		switch (this) {
			case INTEGER:
				return "i";
			case STRING:
				return "a";
			case DOUBLE:
				return "d";
			case BOOL:
				return "b";
			default: return null;
		}
	}
	
	public String getAssembleInvokeType() {
		switch (this) {
			case INTEGER:
				return "I";
			case DOUBLE:
				return "D";
			case BOOL:
				return "B";
			case STRING:
				return "Ljava/lang/String;";
			default: return null;
		}
//		return getAssembleType().toUpperCase();
	}
}
