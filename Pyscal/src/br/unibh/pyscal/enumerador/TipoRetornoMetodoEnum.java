package br.unibh.pyscal.enumerador;

public enum TipoRetornoMetodoEnum {
	BOOL,
	INTEGER,
	STRING,
	DOUBLE,
	VOID;
	
	public String getAssembleInvokeType() {
		String assembleReturnType = "";  
		switch (this) {
			case VOID:
				assembleReturnType = "V";
				break;
			case INTEGER:
				assembleReturnType = "I";
			case DOUBLE:
				assembleReturnType = "D";
			case BOOL:
				assembleReturnType = "B";
			default: break;
		}
		return assembleReturnType;
	}
	
	public String getAssembleReturnType() {
		String assembleReturnType = null;  
		switch (this) {
			case VOID:
				assembleReturnType = "";
				break;
			case STRING:
				assembleReturnType = "a";
				break;
			case INTEGER:
				assembleReturnType = "i";
				break;
			case DOUBLE:
				assembleReturnType = "d";
				break;
			default: break;
		}
		return assembleReturnType;
	}
	
	
	
}
