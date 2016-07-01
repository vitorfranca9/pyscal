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
			default: return null;
		}
	}
	
	public String getAssembleInvokeType() {
		switch (this) {
			case INTEGER:
				return "I";
			case STRING:
				return "Ljava/lang/String;";
			default: return null;
		}
//		return getAssembleType().toUpperCase();
	}
}
