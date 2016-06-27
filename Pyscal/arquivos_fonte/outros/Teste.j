.class public Teste
.super java/lang/Object
        .method public static main([Ljava/lang/String;)V
        .limit stack 50
        .limit locals 50
            ldc 6
            istore 0
            ldc 6
            iload 0
            imul
            istore 0
            ldc "Uma String"
            astore 1
            getstatic java/lang/System/out Ljava/io/PrintStream;
                iload 0
            invokevirtual java/io/PrintStream/println(I)V
            getstatic java/lang/System/out Ljava/io/PrintStream;
                aload 1
            invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
            return
        .end method