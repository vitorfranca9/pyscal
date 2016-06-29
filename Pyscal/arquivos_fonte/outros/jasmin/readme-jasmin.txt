Tinapoc - 0.3 Alpha release
The Java Reverse Engineering Toolkit
By Daniel Reynaud-Plantey <reynaud.danyel@wanadoo.fr>
25/10/05




Preamble - why a new Jasmin language ?
**
Jasmin is the de-facto standard Java assembly language. It is useful to explore
the possibilities of bytecode, but it does not offer a real low level control
over the produced output. Therefore it is not suitable to generate test cases
for virtual machines or bytecode verifier. This new version of the Jasmin
language, called JasminXT, provides optional directives and other syntax
updates to have more control over the output and to stick with the latest
changes of the Java language.





JasminXT Syntax
***
This new version is an extension of the existing Jasmin language, therefore old
Jasmin files should still compile correctly with newer versions of Jasmin.
JasminXT is supported by Jasmin 2.0 or higher.

In the rest of this document, words between '[' and ']' are optional. The
syntax of a JasminXT file is the following :

<jas_file> {
    <jasmin_header>
    [<fields>]
    [<methods>]
}




<jasmin_header> {
    [.bytecode <x.y>]
    [.source <sourcefile>]
    <class_spec>
    <super_spec>
    <implements>
    [.signature "<signature>"]
    [.debug "<debug_source_extension>"]
    [.enclosing method <method_name>]
}

example :
.bytecode 49.0
.source hello.j
.class hello
.super java/lang/Object
.signature "<my::own>Signature()"
.debug "this string will be included in the SourceDebugExtension attribute"
.enclosing method foo/bar/Whatever/someMethod()

The .bytecode directive sets the version of the bytecode in the class file.

The .signature directive, when used in the header of the Jasmin file, sets the
Signature attribute for the class (the argument is a string between double
quotes)

The .debug directive sets the SourceDebugExtension attribute for the class (the
argument is also a string between double quotes)

The .enclosing directive sets the EnclosingMethod attribute for the class. The
argument is a supposed to be a method name, but it can be any string between
double quotes.




<class_spec> {
    .class <access_spec> <class_name>
}

where <access_spec> is any number of words taken from this list : public,
private, protected, static, final, synchronized, native, final, super,
interface, abstract, annotation, enum, bridge/volatile, transient/varargs

and <class_name> is the fully qualified internal form of the class, such as
my/package/MyClass




<super_spec> {
    .super <class_name>
}
(this directive has not been modified in JasminXT)




<implements> {
    .implements <class_name>
    (...)
}
(this directive has not been modified in JasminXT)
(it can be repeated in order to implement multiple interfaces)




<fields> {
   .field <access_spec> <field_name> <descriptor> [signature <signature>] 
           [ = <value> ]
   (...)
}

The only addition is the optional signature attribute. If present, the
Signature attribute will be set in the class file for this field with the given
quoted string as an argument.

example :
.field enum myField Ljava/lang/String; signature "<my::own>Signature()" = "val"




<methods> {
    <method>
    (...)
}




<method> {
    .method <access_spec> <method_name> <descriptor>
        <statement>
        (...)
    .end method
}
(this has not been modified in JasminXT)




<statement> {
   .limit stack <integer>
   | .limit locals <integer>
   | .line <integer>
   | .var <var_number> is <var_name> <descriptor> from <label1> to <label2>
   | .var <var_number> is <var_name> <descriptor> from <offset1> to <offset2>
   | .throws <classname>
   | .catch <classname> from <label1> to <label2> using <label3>
   | .catch <classname> from <offset1> to <offset2> using <offset3>
   | .signature "<signature>"
   | .stack
         offset <pc>
         [locals <verification_type> [<verification_arg>]]
         (...)
         [stack  <verification_type> [<verification_arg>]]
         (...)
     .end stack
   | <instruction> [<instruction_args>]
   | <Label>:
}

In Jasmin XT you can now use offsets instead of labels for the local variable
definitions and for the exception handlers definitions.

The .signature sets the Signature attribute for this method with the given
quoted string.

You can now also define StackMap attributes using the .stack directive. <pc> is
an offset in the local bytecode array. <verification_type> is one of the
following keywords : Top, Integer, Float, Long, Double, Null, 
UninitializedThis, Object or Uninitialized. Object takes a <classname> as a
parameter. Uninitialized takes an integer as a parameter.

example :

.stack
    offset 16
    locals Null
    locals Top
    locals Object allo
    stack Uninitialized 12
.end stack

This statement defines a single stack map frame. All the stack map frames
defined in a method are then aggregated and form the StackMap attribute for the
method.




<instruction> {
    [<pc>:] <opcode> [<instruction_args>]
}

The main change in JasminXT is that it is now possible to put the offset of the
instruction before the opcode (the <pc>: statement). The pc is processed as a
label, therefore you can virtually put any number as the pc but it won't change
the actual pc of the bytecode.

Another update is that it is now possible to use offsets (both relative and
absolute) as branch targets instead of labels. The offset is considered to be
relative if it begins with a plus or a minus sign.

example :
goto  n  ; absolute offset : go to the bytecode labelled n
goto +n  ; relative offset : go n bytes forward (from the offset of this goto)
goto -n  ; relative offset : go n bytes backwards




If something hasn't been documented here, it means that it hasn't changed, so
you can still refer to the Jasmin documentation available at
http://jasmin.sourceforge.net