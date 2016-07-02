.class public Comandos2
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 30
   .limit locals 30
      new Comandos2
      dup
      invokespecial Comandos2/<init>()V
      ldc "Hello World!"
      astore 0
      getstatic java/lang/System/out Ljava/io/PrintStream;
      aload 0
      invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
      return
   .end method
