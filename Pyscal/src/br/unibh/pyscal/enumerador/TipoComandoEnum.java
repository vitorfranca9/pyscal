package br.unibh.pyscal.enumerador;

import lombok.Getter;

public enum TipoComandoEnum {
	IF(""),
	WHILE(""),
	WRITE(getWrite(TipoVariavelEnum.INTEGER, TipoRetornoMetodoEnum.VOID)),
	WRITELN(""),
	ATRIBUI(""),
	FUNCAO("");
	
	private TipoComandoEnum(String jCode) {
		this.jCode = jCode;
	}

	@Getter private final String jCode;
	
	public static String getWrite(TipoVariavelEnum tipoVariavel, TipoRetornoMetodoEnum tipoRetornoMetodoEnum) {
		return new StringBuilder("getstatic java/lang/System/out Ljava/io/PrintStream;\n")
		.append(getType(tipoVariavel))
		.append("load 0\n")
		.append("invokevirtual java/io/PrintStream/print(")
		.append(getUpperType(tipoVariavel))
		.append("")
		.append(")")
		.append(getReturnType(tipoRetornoMetodoEnum))
		.append("\n")
		.toString();
//		String jCode = "";
//		String type = getType(tipoVariavel);
//		jCode += "getstatic java/lang/System/out Ljava/io/PrintStream;\n";
//		jCode += type+"iload 0\n";
//		jCode += "invokevirtual java/io/PrintStream/print(I)V\n";
//		return jCode;
	}
	
	private static String getType(TipoVariavelEnum tipoVariavel) {
		switch (tipoVariavel) {
			case INTEGER:
				return "i";
			default:
				return "";
		}
	}
	
	private static String getUpperType(TipoVariavelEnum tipoVariavel) {
		return getType(tipoVariavel).toUpperCase();
	}
	
	private static String getReturnType(TipoRetornoMetodoEnum tipoRetornoMetodoEnum) {
		switch (tipoRetornoMetodoEnum) {
			case VOID:
				return "V";
			default:
				return "";
		}
	}
	
}
