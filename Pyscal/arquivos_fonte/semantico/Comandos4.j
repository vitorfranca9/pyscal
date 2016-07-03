.class public Comandos4
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

   .method public imprimir(II)V
   .limit stack 30
   .limit locals 30
      ldc 2
      istore_0
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_0
      invokevirtual java/io/PrintStream/println(I)V
      ldc 
      istore_1
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_1
      invokevirtual java/io/PrintStream/println(I)V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 30
   .limit locals 30
      new Comandos4
      dup
      invokespecial Comandos4/<init>()V
      ldc 2
      istore_0
      iload_0
      invokevirtual Comandos4.imprimir(I)V
      return
   .end method
