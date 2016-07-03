.class public Comandos3
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

   .method public imprimir(II)V
   .limit stack 30
   .limit locals 30
      istore_1
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_1
      invokevirtual java/io/PrintStream/println(I)V
      istore_2
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_2
      invokevirtual java/io/PrintStream/println(I)V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 30
   .limit locals 30
      new Comandos3
      dup
      invokespecial Comandos3/<init>()V
      ldc 2
      istore_0
      iload_0
      invokevirtual Comandos3.imprimir(I)V
      return
   .end method
