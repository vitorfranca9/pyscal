.class public Comandos7
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 50
   .limit locals 50
   new Comandos7
   dup
   invokespecial Comandos7/<init>()V
      ldc 22
      istore_2
         ldc 0
         istore_3
            ldc 22
            istore_2
            getstatic java/lang/System/out Ljava/io/PrintStream;
            iload_2
            invokevirtual java/io/PrintStream/println(I)V
               ldc 0
               istore_3
               getstatic java/lang/System/out Ljava/io/PrintStream;
               iload_3
               invokevirtual java/io/PrintStream/println(Z)V
               return
            .end method
