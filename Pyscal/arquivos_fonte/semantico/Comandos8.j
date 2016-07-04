.class public Comandos8
.super java/lang/Object

   .method public <init>()V
      aload_0
      invokenonvirtual java/lang/Object/<init>()V
      return
   .end method

   .method public static main([Ljava/lang/String;)V
   .limit stack 50
   .limit locals 50
   new Comandos8
   dup
   invokespecial Comandos8/<init>()V
      ldc2_w 2.4
      dstore_3
         ldc 1
         istore_2
            ldc 1
            istore_2
            getstatic java/lang/System/out Ljava/io/PrintStream;
            iload_2
            invokevirtual java/io/PrintStream/println(Z)V
               ldc2_w 2.4
               dstore_3
               getstatic java/lang/System/out Ljava/io/PrintStream;
               dload_3
               invokevirtual java/io/PrintStream/println(D)V
               return
            .end method
