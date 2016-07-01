package br.unibh.pyscal.enumerador;

public enum TipoRetornoMetodoEnum {
	BOOL,
	INTEGER,
	STRING,
	DOUBLE,
	VOID;
	
	public String getAssembleReturnType() {
		String assembleReturnType = null;  
		switch (this) {
			case VOID:
				assembleReturnType = "V";
				break;
			default: break;
		}
		return assembleReturnType;
	}
	
}
