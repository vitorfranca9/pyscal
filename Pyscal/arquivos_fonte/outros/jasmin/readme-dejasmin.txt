Tinapoc - 0.3 Alpha release
The Java Reverse Engineering Toolkit
By Daniel Reynaud-Plantey <reynaud.danyel@wanadoo.fr>
25/10/05




Preamble - why a new Java disassembler ?
**
There are already a lot of Java disassemblers, so why bother using a new one ?

Because the only standard disassembler is javap (from sun). Its output is
merely descriptive and can't be parsed easily by an assembler. Therefore Jon
Meyer created the Jasmin assembler along with its own assembly language. So
there was a need for new disassemblers to print the content of a class file
in Jasmin format. Some of them have appeared but they also have the same flaws
as javap :

- if they encounter an invalid class file, they will probably crash
- they might not show all the content of the class file (for example they
usually don't print the constant pool or the class attributes)
- they seem to be no longer maintained. Even javap doesn't support many of the
newer attributes such as StackMap.

dejasmin has been designed to solve these problems.




Features
***
Here are some of the dejasmin features :

- can parse even a truncated class file (--warmode option)
- can print the whole content of a class file (--warmode) such as unreferenced
items in the constant pool, or unknown attributes
- support for the newest attributes : SourceDebugExtension, Signature,
StackMap, EnclosingMethod. Note that the annotations-related attributes have
not been implemented yet.
- supports different output formats :
    --warmode : the dejasmin own format. It produces a lot of output because it
shows everything as it appears in the constant pool. The comments (between
brackets) are here only to make the output more readable.

    --oldjasmin : the old Jasmin language, as implemented in Jasmin v1.1

    (default) : the new JasminXT language.




What can I do with it ?
*****
- analyse what a class file does, in case it has been specially crafted or
obfuscated. It has been designed with malware analysis in mind.

- check if there are problems with a class file, and where they are (you might
be interested in that if you work on compilers or other code generating
software)

- study the class file format.

- generate test cases for verifiers and virtual machines (along with jasmin)




Running dejasmin
*******
dejasmin can be run with the following command :
% java dejasmin [options] <classfile>


Here are the options :
  --help or -h
I think this one is self-descriptive enough.

  --version or -e
Well...

  --verbose or -v
Same as --warmode

  --dontprintoffsets (or -o)
tells dejasmin not to print the offsets for the supported entries in warmode and
the pcs for Jasmin output.

  --printcp or -p
prints the constant pool as Jasmin comments when not in warmode

  --oldjasmin or -j
produces output for Jasmin 1.1

  --nolabels or -l
use offsets instead of labels in Jasmin output. This is a new JasminXT feature
so it won't work with --oldjasmin

  --warmode or -w
enables the warmode. You should try this if you want to see what is really in
your class file or if you can't get Jasmin output for some reason. If the file
is truncated, it will be parsed as far as possible. If it is semantically
invalid (for example the class files tries to load a constant outside the
constant pool), it should pointed out.

  --noexitonerror or -n
when used in warmode, tells dejasmin to ignore non-fatal errors, and print more
explicit exceptions.

  --nocp or -c
when used in warmode, does not print the constant pool.




Contact
***********
Report bugs and submit feature requests to reynaud.danyel@wanadoo.fr
Don't hesitate to send feedback !