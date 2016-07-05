.class public Comandos10
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

   .method public funcao(null)V
   .limit stack 50
   .limit locals 50
      ldc x
      nullstore_1
      getstatic java/lang/System/out Ljava/io/PrintStream;
      nullload_1
      invokevirtual java/io/PrintStream/println(null)V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 50
   .limit locals 50
   new Comandos10
   dup
   invokespecial Comandos10/<init>()V
   aload_0
      ldc funcao
      nullstore_3
      getstatic java/lang/System/out Ljava/io/PrintStream;
      nullload_3
      invokevirtual java/io/PrintStream/println(null)V
      ldc 5
      istore_4
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_4
      invokevirtual java/io/PrintStream/println(I)V
      return
   .end method
