.class public CodigoTeste
	.super java/lang/Object
		.method public static main([Ljava/lang/String;)V
			.limit stack 10
			.limit locals 10
			ldc 5
			istore 0
			;println
			getstatic java/lang/System/out/println Ljava/io/PrintStream;
			iload 0
			invokevirtual java/io/PrintStream/println(I)I 
			return
		.end method