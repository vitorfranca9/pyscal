.class public Comandos5
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

   .method public imprimir1(IIII)V
   .limit stack 30
   .limit locals 30
      ldc 3
      istore_0
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_0
      invokevirtual java/io/PrintStream/println(I)V
      ldc 4
      istore_1
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_1
      invokevirtual java/io/PrintStream/println(I)V
      ldc 1
      istore_2
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_2
      invokevirtual java/io/PrintStream/println(I)V
      ldc 2
      istore_3
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_3
      invokevirtual java/io/PrintStream/println(I)V
      new Comandos5
      dup
      invokespecial Comandos5/<init>()V
      invokevirtual Comandos5.imprimir2()V
      new Comandos5
      dup
      invokespecial Comandos5/<init>()V
      invokevirtual Comandos5.imprimir3()V
      new Comandos5
      dup
      invokespecial Comandos5/<init>()V
      invokevirtual Comandos5.imprimir2()V
      new Comandos5
      dup
      invokespecial Comandos5/<init>()V
      invokevirtual Comandos5.imprimir3()V
      return
   .end method

   .method public imprimir2()V
   .limit stack 30
   .limit locals 30
      ldc "2"
      astore_0
      getstatic java/lang/System/out Ljava/io/PrintStream;
      aload_0
      invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
      new Comandos5
      dup
      invokespecial Comandos5/<init>()V
      invokevirtual Comandos5.imprimir4()V
      return
   .end method

   .method public imprimir3()V
   .limit stack 30
   .limit locals 30
      ldc "3"
      astore_0
      getstatic java/lang/System/out Ljava/io/PrintStream;
      aload_0
      invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
      return
   .end method

   .method public imprimir4()V
   .limit stack 30
   .limit locals 30
      new Comandos5
      dup
      invokespecial Comandos5/<init>()V
      invokevirtual Comandos5.imprimir5()V
      ldc 40
      istore_1
      getstatic java/lang/System/out Ljava/io/PrintStream;
      iload_1
      invokevirtual java/io/PrintStream/println(I)V
      new Comandos5
      dup
      invokespecial Comandos5/<init>()V
      invokevirtual Comandos5.imprimir5()V
      new Comandos5
      dup
      invokespecial Comandos5/<init>()V
      invokevirtual Comandos5.imprimir5()V
      return
   .end method

   .method public imprimir5()V
   .limit stack 30
   .limit locals 30
      ldc2_w 5.0
      dstore_0
      getstatic java/lang/System/out Ljava/io/PrintStream;
      dload_0
      invokevirtual java/io/PrintStream/println(D)V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 30
   .limit locals 30
   new Comandos5
   dup
   invokespecial Comandos5/<init>()V
      new Comandos5
      dup
      invokespecial Comandos5/<init>()V
      ldc 1
      istore_0
      iload_0
      ldc 2
      istore_1
      iload_1
      ldc 3
      istore_2
      iload_2
      ldc 4
      istore_3
      iload_3
      invokevirtual Comandos5.imprimir1(IIII)V
      return
   .end method
