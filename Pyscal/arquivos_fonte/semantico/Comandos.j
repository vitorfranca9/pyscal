.class public Comandos
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

   .method public imprimir()V
   .limit stack 30
   .limit locals 30
      ldc "Hello World"
      astore 0
      getstatic java/lang/System/out Ljava/io/PrintStream;
      aload 0
      invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 30
   .limit locals 30
      new Comandos
      dup
      invokespecial Comandos/<init>()V
      invokevirtual Comandos.imprimir()V
      return
   .end method