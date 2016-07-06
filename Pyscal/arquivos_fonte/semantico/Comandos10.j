.class public Comandos10
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

   .method public funcao(I)V
   .limit stack 50
   .limit locals 50
      ldc 2
      istore_1
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_1
      invokevirtual java/io/PrintStream/print(I)V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 50
   .limit locals 50
   new Comandos10
   dup
   invokespecial Comandos10/<init>()V
   aload_0
      new Comandos10
      dup
      invokespecial Comandos10/<init>()V
      ldc 2
      istore_3
      iload_3
      invokevirtual Comandos10.funcao(I)V
      return
   .end method
