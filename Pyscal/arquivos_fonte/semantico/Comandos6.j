.class public Comandos6
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

      .method public imprimir1(nullnullnullnull)V
      .limit stack 50
      .limit locals 50
         ldc j
         nullstore_2
         getstatic java/lang/System/out Ljava/io/PrintStream;
         nullload_2
         invokevirtual java/io/PrintStream/println(null)V
         ldc k
         nullstore_3
         getstatic java/lang/System/out Ljava/io/PrintStream;
         nullload_3
         invokevirtual java/io/PrintStream/println(null)V
         ldc l
         nullstore_4
         getstatic java/lang/System/out Ljava/io/PrintStream;
         nullload_4
         invokevirtual java/io/PrintStream/println(null)V
         return
      .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 50
   .limit locals 50
   new Comandos6
   dup
   invokespecial Comandos6/<init>()V
      new Comandos6
      dup
      invokespecial Comandos6/<init>()V
      ldc "Hello "
      astore_0
      aload_0
      ldc 1
      istore_1
      iload_1
      ldc 1
      istore_2
      iload_2
      ldc2_w 2.32
      dstore_3
      dload_3
      ldc2_w 2.32
      dstore_4
      dload_4
      invokevirtual Comandos6.imprimir1(Ljava/lang/String;IZDD)V
         ldc "Hello WOrld!!"
         astore_15
         getstatic java/lang/System/out Ljava/io/PrintStream;
         aload_15
         invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
         return
      .end method
