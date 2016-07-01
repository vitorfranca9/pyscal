.class public Comandos
.super java/lang/Object
.method public static main([Ljava/lang/String;)V
.limit stack 50
.limit locals 50
ldc "Hello World"
astore 0
getstatic java/lang/System/out Ljava/io/PrintStream;
aload 0
invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
    return
.end method
