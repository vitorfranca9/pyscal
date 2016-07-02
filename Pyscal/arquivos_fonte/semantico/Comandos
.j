.class public Comandos

.super java/lang/Object

   .method public static imprimir()V
   .limit stack 5
   .limit locals 10
      ldc "Hello World!"
      astore 0
      getstatic java/lang/System/out Ljava/io/PrintStream;
      aload 0
      invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 100
   .limit locals 100
invokestatic Comandos/imprimir()V
   return
.end method
