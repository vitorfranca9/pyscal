Tinapoc - 0.3 Alpha release
The Java Reverse Engineering Toolkit
By Daniel Reynaud-Plantey <reynaud.danyel@wanadoo.fr>
25/10/05




Preamble - what is Tinapoc ?
**
Tinapoc (or just tina) is a low level reverse engineering toolkit for Java,
written in Java. Using these tools you can produce test files, such as invalid
zip files or invalid class files. The programs included in tinapoc generally
don't check your input, so that you have an almost absolute control of the 
output files. The same approach is used to analyse jar or class files, so that
you can analyse malformed or specially crafted files. This can be particularly
interesting against obfuscated programs or malware.

This package includes the following tools :
zip2xml  - convert the binary zip file format to xml
xml2zip  - reverse operation. You can use this to experiment with the zip format
dejasmin - a Java disassembler
jasmin   - Jasmin 2.0
justice  - a "frontend" for the JustIce verifier included in the BCEL

Jasmin is a popular Java assembler available at http://jasmin.sourceforge.net/



License
***
This software comes free of charge under the GNU GPL License. 
See the included license.txt file.



Installing and Running Tinapoc
*****
Tinapoc is written in Java, therefore it should run on any system with a Java
virtual machine (Java 1.5 or higher). If you don't have a Java virtual machine
or if it can't run Java 1.5, download the latest Java Runtime Environment from
http://java.sun.com/


Once you have Java installed, you can install tinapoc :

1. add bin/tinapoc.jar to your classpath. You can either :
 - modify your CLASSPATH environment variable so that it points to tinapoc.jar
or
 - put tinapoc.jar in a folder where your runtime looks for libraries, such as
%JAVA_HOME%/jre/lib/ext. Doing so can slow down other Java applications.

Refer to your virtual machine documentation if you don't know how to set your
classpath.


2. tinapoc uses the ByteCode Engineering Library version 5.1. If you don't
already have it installed, add the bin/bcel-5.1.jar file to your CLASSPATH,
as described above. 

WARNING : some features might NOT work with future versions of the BCEL. Be
sure that you're using the BCEL 5.1.


3. run one of the following commands :
java xml2zip <filename>
java zip2xml <filename>
java dejasmin [options] <classfile>
java jasmin [-d <directory>] [-version] <file> [<file> ...]
java justice <classname>



Examples
*******
> java zip2xml test.zip
will output the xml representation of test.zip on the standard output

> java zip2xml test.zip > test.xml
will write the output in test.xml.

> java xml2zip test.xml > test2.zip
will produce the zip file described in test.xml

> java dejasmin Test.class
will disassemble Test.class and produce Jasmin output

> java dejasmin --warmode Test.class > Test.dump
will dump the WHOLE content of the class in Test.dump
See dejasmin documentation for more details.

> java jasmin test.j
assembles test.j. See Jasmin documentation for more details.



XML Syntax
***********
To see the elements used in the xml file, just disassemble a test zip file with
zip2xml. If you want to modify the xml file, notice that :
 - you can use comments between brackets, '(' and ')'. Comments can't be nested.
 - you can represent numbers in decimal (default), or in hexadecimal by
   prepending them with '0x'.



Contact
*************
Report bugs and submit feature requests to reynaud.danyel@wanadoo.fr
Don't hesitate to send feedback !