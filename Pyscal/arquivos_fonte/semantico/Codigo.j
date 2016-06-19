.class public Codigo
	.super java/lang/Object
		.method public static func(II)I
			.limit stack 10
			.limit locals 10
				iload 1
				ldc 5
				imul
				iload 0
				iadd
				ireturn
		.end method
		.method public static main([Ljava/lang/String;)V
			.limit stack 10
			.limit locals 10
			ldc 5
			istore 0
			ldc 2
			istore 1
			;println
			getstatic java/lang/System/out/println Ljava/io/PrintStream;
			iload 0
			iload 1
			invokestatic Codigo/func(II)I
			invokevirtual java/io/PrintStream/println(I)V 
			return
		.end method