.class public Comandos2
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

   .method public imprimir()V
   .limit stack 30
   .limit locals 30
      ldc2_w 0.5
      dstore_0
      getstatic java/lang/System/out Ljava/io/PrintStream;
      dload_0
      invokevirtual java/io/PrintStream/print(D)V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 30
   .limit locals 30
      new Comandos2
      dup
      invokespecial Comandos2/<init>()V
      invokevirtual Comandos2.imprimir()V
      return
   .end method
