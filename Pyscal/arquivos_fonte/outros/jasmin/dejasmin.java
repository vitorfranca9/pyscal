/******************************************************************************
Tinapoc Project - The Java Reverse Engineering Toolkit
Copyright (C) 2005  Daniel Reynaud-Plantey

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
******************************************************************************/

import java.io.RandomAccessFile;
import java.io.IOException;
import tina.classparsing.*;
import tina.utils.*;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

public class dejasmin {
    private final static String version = "dejasmin v0.3";

    private RandomAccessFile datain;

// class data
    private TinaConstantPool cp;
    private int access_flags, this_class, super_class;
    private int[] interfaces;
    private TinaField[] fields;
    private TinaMethod[] methods;
    private TinaAttribute[] attributes;

// parsing parameters
    private boolean warmode = false,       // don't parse sequentially
                    printcp = false,       // don't print the constant pool
                    nocp = false,          // hide the constant pool in warmode
                    printoffsets = true,   // print offsets (both modes)
                    exitonerror = true,    // stop parsing on error
                    oldjasmin = false,     // target jasmin v1.1
                    nolabels = false;      // print labels

    public static void main(String arg[]) {
        dejasmin disasm = new dejasmin();
        CommandLineParser p = new CommandLineParser(arg);
        if (arg.length==0) {
            System.out.println("Usage : java dejasmin [options] <classfile>");
            System.out.println("Use --help or -h to see a list of options");
            System.exit(1);
        }
        if (p.isOptionSet('h') || p.isOptionSet("help")) {
            StringBuilder h = new StringBuilder(); 
            h.append("Usage : java dejasmin [options] <classfile>\n");
            h.append("List of common options :\n");
            h.append("  --help or -h : prints this help message\n");
            h.append("  --version or -e : prints the version and exit\n");
            h.append("  --dontprintoffsets or -o : don't print offsets for "+
                     "the supported entries in the class file\n");

            h.append("List of normal options :\n");
            h.append("  --printcp or -p : print the constant pool\n");
            h.append("  --oldjasmin or -j : produces output for Jasmin 1.1\n");
            h.append("  --nolabels or -l : use offsets instead of labels"+
                                         " (won't work with --oldjasmin)\n");

            h.append("  --warmode or -w : enable warmode\n");

            h.append("\nList of warmode options :\n");
            h.append("  --nocp or -c : don't print the constant pool\n");
            h.append("  --noexitonerror or -n : try to continue class file "+
                     "parsing after an error (experimental)\n");
            h.append("\nThe target must be a classfile, such as Test.class");
            System.out.println(h.toString());
            System.exit(0);
        }
        if (p.isOptionSet('e') || p.isOptionSet("version")) {
            System.out.println(version);
            System.exit(0);
        }
        if (p.isOptionSet('v') || p.isOptionSet("verbose"))
            disasm.warmode = true;
        if (p.isOptionSet('w') || p.isOptionSet("warmode"))
            disasm.warmode = true;
        if (p.isOptionSet('p') || p.isOptionSet("printcp"))
            disasm.printcp = true;
        if (p.isOptionSet('o') || p.isOptionSet("dontprintoffsets"))
            disasm.printoffsets = false;
        if (p.isOptionSet('n') || p.isOptionSet("noexitonerror"))
            disasm.exitonerror = false;
        if (p.isOptionSet('c') || p.isOptionSet("nocp"))
            disasm.nocp = true;
        if (p.isOptionSet('j') || p.isOptionSet("oldjasmin"))
            disasm.oldjasmin = true;
        if (p.isOptionSet('l') || p.isOptionSet("nolabels")) {
            disasm.nolabels = true;
            if(disasm.oldjasmin) {
                System.err.println("Offsets instead of labels is a feature "+
                   "of Jasmin v1.2 or higher. Can't be used with --oldjasmin");
                System.exit(1);
            }
        }
        if (!arg[arg.length-1].startsWith("-"))
            disasm.disassemble(arg[arg.length-1]);
        else
            System.err.println("You must specify a target, use --help option");
    }

    public final void disassemble(String cls) {
        if (warmode) warDisassemble(cls);
        else jasminDisassemble(cls);
    }

// uses the BCEL parser (the whole class must be valid !)
    public final void jasminDisassemble(String cls) {
        ClassParser parser=null;
        JavaClass java_class;

        try {
            if (printcp) {
                datain = new RandomAccessFile(cls, "r");
                readConstantPoolCount(datain);
                readConstantPool(datain);
                System.out.println();
            }

            if ((java_class = Repository.lookupClass(cls)) == null)
            java_class = new ClassParser(cls).parse();
            new NeoJasminVisitor(java_class, System.out,
                oldjasmin, nolabels, printoffsets).disassemble();
        } catch (Exception e) {
            error("can't produce Jasmin output for the following reason :\n"+e);
            e.printStackTrace();
        }
    }


// as opposed to the NeoJasminVisitor.disassemble() method
// print information sequentially with built-in parser
// no assumption about the validity of the class file
    public final void warDisassemble(String cls) {
        try {
            datain = new RandomAccessFile(cls, "r");

            readMagic(datain);
            readByteCodeVersion(datain);

            if (warmode) System.out.println();
            readConstantPoolCount(datain);
            readConstantPool(datain);

            if (warmode || printcp) System.out.println();
            readAccessFlags(datain);
            readThisClassIndex(datain);
            readSuperClassIndex(datain);

            if (warmode) System.out.println();
            readInterfacesCount(datain);
            readInterfaces(datain);

            if (warmode) System.out.println();
            readFieldsCount(datain);
            readFields(datain);

            if (warmode) System.out.println();
            readMethodsCount(datain);
            readMethods(datain);

            if (warmode) System.out.println();
            readAttributesCount(datain);
            readAttributes(datain);

            System.out.println();
            System.out.println("; End of file reached successfully. Enjoy :)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





/*****************************************************************************/
/*                           CLASS PARSING METHODS                           */
/*****************************************************************************/

    private void readMagic(RandomAccessFile in) throws IOException {
        byte[] magic = new byte[4];
        in.read(magic);
        if (warmode)
            System.out.println("; magic number 0x"+toHexString(magic[0])
        +toHexString(magic[1])+toHexString(magic[2])+toHexString(magic[3]));
    }

    private void readByteCodeVersion(RandomAccessFile in) throws IOException {
        int[] v = new int[2];
        v[0] = in.readUnsignedShort();
        v[1] = in.readUnsignedShort();
        if (warmode)
            System.out.println("; bytecode version "+v[1]+"."+v[0]);
    }

    private void readConstantPoolCount(RandomAccessFile in)
                                         throws IOException {
        datain.seek(8);
        cp = new TinaConstantPool(datain.readUnsignedShort()+1);
        if (printcp || (warmode && !nocp)) 
            System.out.println("; constant pool count "+(cp.length()-1));
    }

    private void readConstantPool(RandomAccessFile in) 
                                          throws IOException {
        int idx = 1;
        while (idx < cp.length()-1) {
            cp.setElementAt(readConstantPoolEntry(in), idx);
            if (printcp || (warmode && !nocp)) {
                System.out.print("; cp["+idx+"]");
                if (printoffsets) {
                    long fp = in.getFilePointer();
                    System.out.print(" (offset 0x"+Long.toHexString(fp)+")");
                }
                try {
                    System.out.println(" -> "+cp.elementAt(idx));
                } catch(TinaConstantPoolException e) {
                    error(e);
                }
            }
            int type;
            try {
                type = cp.elementAt(idx).getType();
                if (type==TinaConstantPoolEntry.CONSTANT_Long
                    || type==TinaConstantPoolEntry.CONSTANT_Double)
                    idx++;
            } catch(TinaConstantPoolException e) {
                error(e);
            }
            idx++;
        }
    }

    private TinaConstantPoolEntry readConstantPoolEntry(final RandomAccessFile in) throws IOException {
        byte tag = in.readByte();
        switch(tag) {
            case 1 : return new TinaConstantPoolEntry<String>(1,
                                    stripNewLines(in.readUTF()), cp);
            case 3 : return new TinaConstantPoolEntry<Integer>(3, 
                                    in.readInt(), cp);
            case 4 : return new TinaConstantPoolEntry<Float>(4, 
                                    in.readFloat(), cp);
            case 5 : return new TinaConstantPoolEntry<Long>(5, 
                                    in.readLong(), cp);
            case 6 : return new TinaConstantPoolEntry<Double>(6, 
                                    in.readDouble(), cp);
            case 7 : return new TinaConstantPoolEntry<Integer>(7, 
                                    in.readUnsignedShort(), cp);
            case 8 : return new TinaConstantPoolEntry<Integer>(8, 
                                    in.readUnsignedShort(), cp);
            case 9 : return new TinaConstantPoolEntry<Integer>(9, 
                                    in.readUnsignedShort(),
                                    in.readUnsignedShort(), cp);
            case 10 : return new TinaConstantPoolEntry<Integer>(10,
                                    in.readUnsignedShort(), 
                                    in.readUnsignedShort(), cp);
            case 11 : return new TinaConstantPoolEntry<Integer>(11, 
                                    in.readUnsignedShort(), 
                                    in.readUnsignedShort(), cp);
            case 12 : return new TinaConstantPoolEntry<Integer>(12, 
                                    in.readUnsignedShort(), 
                                    in.readUnsignedShort(), cp);
            default : error("Unknown constant pool tag "+tag);
                      System.exit(1);  // must exit on this error
                      return null;
        }
    }

    private void readAccessFlags(RandomAccessFile in) throws IOException {
        int flag = datain.readUnsignedShort();
        access_flags = flag;
        if (warmode) {
            System.out.print("; access flags = 0x"
                             +Integer.toHexString(flag)+" [ ");
            if ((flag & 0x4000) == 0x4000) {
                flag -= 0x4000;
                System.out.print("ACC_ENUM ");
            }
            if ((flag & 0x2000) == 0x2000) {
                flag -= 0x2000;
                System.out.print("ACC_ANNOTATION ");
            }
            if ((flag & 0x0400) == 0x0400) {
                flag -= 0x0400;
                System.out.print("ACC_ABSTRACT ");
            }
            if ((flag & 0x0200) == 0x0200) {
                flag -= 0x0200;
                System.out.print("ACC_INTERFACE ");
            }
            if ((flag & 0x0020) == 0x0020) {
                flag -= 0x0020;
                System.out.print("ACC_SUPER ");
            }
            if ((flag & 0x0010) == 0x0010) {
                flag -= 0x0010;
                System.out.print("ACC_FINAL ");
            }
            if ((flag & 0x0001) == 0x0001) {
                flag -= 0x0001;
                System.out.print("ACC_PUBLIC ");
            }
            System.out.print("]");
            if (flag != 0) System.out.print(" UNKNOWN_FLAG : 0x"
                                           +Integer.toHexString(flag));
            System.out.println();
        }
    }

    private void readThisClassIndex(RandomAccessFile in) throws IOException {
        this_class=in.readUnsignedShort();
        if (warmode) System.out.println("; this_class index = "+this_class);
    }

    private void readSuperClassIndex(RandomAccessFile in) throws IOException {
        super_class=in.readUnsignedShort();
        if (warmode) System.out.println("; super_class index = "+super_class);
    }

    private void readInterfacesCount(RandomAccessFile in) throws IOException {
        interfaces = new int[in.readUnsignedShort()];
        if (warmode)
            System.out.println("; interfaces_count = "+interfaces.length);
    }

    private void readInterfaces(final RandomAccessFile in) throws IOException {
        for (int i=0; i<interfaces.length; i++) {
            if (warmode) {
                System.out.print("; interfaces["+i+"]");
                if (printoffsets)
                    System.out.print(" (offset 0x"
        +Long.toHexString(in.getFilePointer())+")");
                interfaces[i]=in.readUnsignedShort();
                System.out.println("  -> "+interfaces[i]);
            }
        }
        if (warmode) System.out.println();
    }

    private void readFieldsCount(RandomAccessFile in) throws IOException {
        fields = new TinaField[in.readUnsignedShort()];
        if (warmode) System.out.println("; fields_count = "+fields.length);
    }

    private void readFields(RandomAccessFile in) throws IOException {
        for (int i=0; i<fields.length; i++) {
            if (warmode) {
                System.out.print("\n; fields["+i+"]");
                if (printoffsets) {
                    long fp = in.getFilePointer();
                    System.out.print(" (offset 0x"+Long.toHexString(fp)+")");
                }
                System.out.println(" :");
            }
            fields[i]=readField(in);
        }
    }

    private TinaField readField(RandomAccessFile in) throws IOException {
        int af = in.readUnsignedShort();
        if (warmode) {
            int flag = af;
            System.out.print(";     access_flags 0x"
        +Integer.toHexString(af)+" [ ");
            if ((flag & 0x4000) == 0x4000) {
                flag -= 0x4000;
                System.out.print("ACC_ENUM ");
            }
            if ((flag & 0x0080) == 0x0080) {
                flag -= 0x0080;
                System.out.print("ACC_TRANSIENT ");
            }
            if ((flag & 0x0040) == 0x0040) {
                flag -= 0x0040;
                System.out.print("ACC_VOLATILE ");
            }
            if ((flag & 0x0010) == 0x0010) {
                flag -= 0x0010;
                System.out.print("ACC_FINAL ");
            }
            if ((flag & 0x0008) == 0x0008) {
                flag -= 0x0008;
                System.out.print("ACC_STATIC ");
            }
            if ((flag & 0x0004) == 0x0004) {
                flag -= 0x0004;
                System.out.print("ACC_PROTECTED ");
            }
            if ((flag & 0x0002) == 0x0002) {
                flag -= 0x0002;
                System.out.print("ACC_PRIVATE ");
            }
            if ((flag & 0x0001) == 0x0001) {
                flag -= 0x0001;
                System.out.print("ACC_PUBLIC ");
            }
            if (flag != 0)
                System.out.print("UNKNOWN_FLAG 0x"+Integer.toHexString(flag));
            System.out.println("]");
        }

        int name = in.readUnsignedShort();
        if (warmode) {
            System.out.print(";     name_index "+name+" (");
            try {
                System.out.println(cp.getDirectName(name)+")");
            } catch(TinaConstantPoolException e) {
                System.out.println("TINAPOC_ERROR_INVALID_INDEX)");
                error(e);
            }
        }

        int desc = in.readUnsignedShort();
        if (warmode) {
            System.out.print(";     descriptor_index "+desc+" (");
            try {
                System.out.println(cp.getDirectName(desc)+")");
            } catch(TinaConstantPoolException e) {
                System.out.println("TINAPOC_ERROR_INVALID_INDEX)");
                error(e);
            }
        }

        int acount = in.readUnsignedShort();
        if (warmode) 
            System.out.println(";     attributes_count "+acount);

        TinaAttribute[] attr = new TinaAttribute[acount];
        for (int i=0; i<acount; i++) {
            if (warmode)
                System.out.println(";     field_attributes["+i+"] :");
            attr[i] = readAttribute(in, 9);
        }

        return new TinaField(af, name, desc, attr);
    }

    private TinaAttribute readAttribute(RandomAccessFile in)
                                          throws IOException {
        return readAttribute(in, 1);
    }

    private TinaAttribute readAttribute(RandomAccessFile in, int indent)
                                                     throws IOException {
        return readAttribute(in, indent, null);
    }

    private TinaAttribute readAttribute(RandomAccessFile in, int indent,
                                     TinaAttribute parent) throws IOException {
        int name_index = in.readUnsignedShort();
        StringBuilder b = new StringBuilder(";");
        for (int i=0; i<indent; i++) b.append(" ");
        String indstr = b.toString();

        if (warmode) 
            System.out.print(b.append("name_index ").append(name_index));
        String name = null;
        try {
            name = cp.getDirectName(name_index);
        } catch(TinaConstantPoolException e) {
            error(e);
        }
        TinaAttribute result = new TinaAttribute(name);
        result.length = readUnsignedInt(in);    // we expect an unsigned int !
        if(((int)result.length)<0)
            System.err.println("WARNING: attribute length too large : "+result.length);
        if (warmode) {
            String type = result.getType().toString();
            if(type.equals("UNKNOWN"))
                System.out.println(" (UNKOWN : "+name+")");
            else
                System.out.println(" ("+type+")");
            System.out.println(indstr+"attribute_length "+result.length);
        }
        switch(result.getType()) {
            case SourceFile :
                return readSourceFileAttribute(in, result, indstr);
            case ConstantValue :
                return readConstantValueAttribute(in, result, indstr);
            case Code :
                return readCodeAttribute(in, result);
            case Exceptions :
                return readExceptionsAttribute(in, result);
            case InnerClasses :
                return readInnerClassesAttribute(in, result, indstr);
            case Synthetic :
                return readSyntheticAttribute(in, result);
            case LineNumberTable :
                return readLineNumberTableAttribute(in, result, indstr);
            case LocalVariableTable :
                return readLocalVariableTableAttribute(in, result, indstr);
            case Deprecated :
                return readDeprecatedAttribute(in, result);
            case StackMap :
                return readStackMapAttribute(in, result, indstr, parent);
            case SourceDebugExtension :
                return readSourceDebugExtensionAttribute(in, result, indstr);
            case EnclosingMethod :
                return readEnclosingMethodAttribute(in, result, indstr);
            case Signature :
                return readSignatureAttribute(in, result, indstr);
            case LocalVariableTypeTable :
                return readLocalVariableTypeTableAttribute(in, result, indstr);
            case UNKNOWN :
                return readUNKNOWNAttribute(in, result, indstr);
            default :
                return null; // SHOULD NEVER REACH THIS POINT
        }
    }

    private TinaAttribute readLocalVariableTypeTableAttribute(RandomAccessFile in,
                           TinaAttribute attr, String ind) throws IOException {
        return readLocalVariableTableAttribute(in, attr, ind, true);
    }


    private TinaAttribute readSignatureAttribute(RandomAccessFile in,
                             TinaAttribute attr, String i) throws IOException {
        attr.signature_index = in.readUnsignedShort();
        if (warmode) {
            try {
                System.out.println(i+"signature_index "
                    +attr.signature_index+" ("
                    +cp.elementAt(attr.signature_index).smartDisplay()+")");
            } catch(TinaConstantPoolException e) {
                System.out.println("TINAPOC_ERROR_INVALID_INDEX)");
                error(e);
            }
        }
        return attr;
    }

    private TinaAttribute readEnclosingMethodAttribute(RandomAccessFile in,
                             TinaAttribute attr, String i) throws IOException {
        attr.class_index = in.readUnsignedShort();
        if (warmode) {
            try {
                System.out.println(i+"class_index "
                    +attr.class_index+" ("
                    +cp.elementAt(attr.class_index).smartDisplay()+")");
            } catch(TinaConstantPoolException e) {
                System.out.println("TINAPOC_ERROR_INVALID_INDEX)");
                error(e);
            }
        }

        attr.method_index = in.readUnsignedShort();
        if (warmode) {
            try {
                int idx = attr.method_index;
                System.out.println(i+"method_index "
                    +idx+" ("
                    +(idx==0?"":cp.elementAt(attr.method_index).smartDisplay())+")");
            } catch(TinaConstantPoolException e) {
                System.out.println(i+"method_index "
                               +attr.method_index);
                error(e);
            }
        }
        return attr;
    }

    private TinaAttribute readSourceDebugExtensionAttribute(RandomAccessFile in,
                             TinaAttribute attr, String i) throws IOException {
        attr.debug_extension = in.readUTF();
        if (warmode)
            System.out.println(i+"debug_extension \""
                               +attr.debug_extension+"\"");
        return attr;
    }

    private TinaAttribute readConstantValueAttribute(RandomAccessFile in,
                             TinaAttribute attr, String i) throws IOException {
        attr.constantvalue_index = in.readUnsignedShort();
        if (warmode)
            System.out.println(i+"constantvalue_index "
                               +attr.constantvalue_index);
        return attr;
    }

    private TinaAttribute readSourceFileAttribute(RandomAccessFile in,
                            TinaAttribute attr, String i) throws IOException {
        attr.sourcefile_index = in.readUnsignedShort();
        if (warmode)
            System.out.println(i+"sourcefile_index "+attr.sourcefile_index);
        return attr;
    }

    private TinaAttribute readCodeAttribute(RandomAccessFile in,
                                TinaAttribute attr) throws IOException {
        attr.max_stack = in.readUnsignedShort();
        if (warmode) 
            System.out.println(";         max_stack "+attr.max_stack);
 
        attr.max_locals = in.readUnsignedShort();
        if (warmode)
            System.out.println(";         max_locals "+attr.max_locals);

        attr.code_length = readUnsignedInt(in);
        if (warmode) {
            System.out.println(";         code_length "+attr.code_length);
            System.out.println(";         code :");
        }

        readCode(in, attr);

        attr.exception_table_length = in.readUnsignedShort();
        attr.exception_table = new int[attr.exception_table_length][4];
        if (warmode) 
            System.out.println(";         exception_table_length "
                                +attr.exception_table_length);

        for (int i=0; i<attr.exception_table_length; i++) {
            attr.exception_table[i][0] = in.readUnsignedShort();
            attr.exception_table[i][1] = in.readUnsignedShort();
            attr.exception_table[i][2] = in.readUnsignedShort();
            attr.exception_table[i][3] = in.readUnsignedShort();

            if (warmode) {
                String ind = ";                 ";
                int ct = attr.exception_table[i][3];
                System.out.println(";             exception_table["+i+"] :");
                System.out.println(ind+"start_pc "+attr.exception_table[i][0]);
                System.out.println(ind+"end_pc "+attr.exception_table[i][1]);
                System.out.println(ind+"handler_pc "+attr.exception_table[i][2]);
                System.out.print(ind+"catch_type "+ct);
                try {
                    System.out.println((ct==0?"":" ("+cp.getIndirectName(ct)+")"));
                } catch(TinaConstantPoolException e) {
                    System.out.println(" (TINAPOC_ERROR_INVALID_INDEX)");
                    error(e);
                }
                System.out.println();
            }
        }
        attr.attributes_count = in.readUnsignedShort();
        if (warmode) 
            System.out.println(";         attributes_count "
                               +attr.attributes_count);
        for (int i=0; i<attr.attributes_count; i++) {
            if (warmode) 
                System.out.println(";         code_attributes["+i+"] : ");
            readAttribute(in, 13, attr);
        } 
        return attr;
    }


// information just read, not stored in the object !
    private TinaAttribute readStackMapAttribute(RandomAccessFile in, 
  TinaAttribute attr, String indstr, TinaAttribute parent) throws IOException {
        long nbentries, offset, nblocals, nbstack;
        boolean shortcode = (parent.code_length <= 0xFFFF),
                shortlocals = (parent.max_locals <= 0xFFFF),
                shortstack = (parent.max_stack <= 0xFFFF);

        if(shortcode) nbentries = in.readUnsignedShort();
        else nbentries = readUnsignedInt(in);

        if(warmode)
            System.out.println(indstr+"nb_of_entries "+nbentries);

        for(int i=0; i<nbentries; i++) {
            if(warmode)
                System.out.println(indstr+"stack_map_frame["+i+"]");

            if(shortcode) offset = in.readUnsignedShort();
            else offset = readUnsignedInt(in);
            if(warmode)
                System.out.println(indstr+"    offset "+offset);

    // reading local variable info
            if(shortlocals) nblocals = in.readUnsignedShort();
            else nblocals = readUnsignedInt(in);
            if(warmode)
                System.out.println(indstr+"    nb_of_locals "+nblocals);
            for(int j=0; j<nblocals; j++)
                readVerificationTypeInfo(in, shortcode, indstr+"        ");

    // reading stack items
            if(shortstack) nbstack = in.readUnsignedShort();
            else nbstack = readUnsignedInt(in);
            if(warmode)
                System.out.println(indstr+"    nb_of_stack_items "+nbstack);
            for(int j=0; j<nbstack; j++) 
                readVerificationTypeInfo(in, shortcode, indstr+"        ");
        }
        return attr;
    }

    private int readVerificationTypeInfo(RandomAccessFile in, boolean shortcd,
                                            String indstr) throws IOException {
        int tag = in.readByte();
        String tagstr = null;
        switch(tag) {
            case 0 : tagstr = "ITEM_Top";
                     break;
            case 1 : tagstr = "ITEM_Integer";
                     break;
            case 2 : tagstr = "ITEM_Float";
                     break;
            case 3 : tagstr = "ITEM_Double";
                     break;
            case 4 : tagstr = "ITEM_Long";
                     break;
            case 5 : tagstr = "ITEM_Null";
                     break;
            case 6 : tagstr = "ITEM_UninitializedThis";
                     break;
            case 7 : tagstr = "ITEM_Object";
                     break;
            case 8 : tagstr = "ITEM_Uninitialized";
                     break;
            default : tagstr = "TINAPOC_ERROR_UNKNOWN_ITEM";
                      error("Unknown tag in StackMap attribute : "+tag);
        }
        if(warmode)
            System.out.print(indstr+tagstr);
        if(tag==7) { // ITEM_Object
            int cpool_idx = in.readUnsignedShort();
            if(warmode) {
                System.out.print(", cpool_index "+cpool_idx+" (");
                try {
                    System.out.print(cp.getIndirectName(cpool_idx)+")");
                } catch(TinaConstantPoolException e) {
                    System.out.print("TINAPOC_ERROR_INVALID_INDEX)");
                    error(e);
                }
            }
        }
        if(tag==8) { // ITEM_Uninitialized
            long offset = shortcd?in.readUnsignedShort():readUnsignedInt(in);
            if(warmode)
                System.out.print(", offset "+offset);
        }
        System.out.println();
        return tag;
    }


// this method is responsible for reading the code array
    private void readCode(RandomAccessFile in, TinaAttribute attr)
                                               throws IOException {
        int read = 0;
        String indent = ";             ";
        long originaloffset = in.getFilePointer();
        while ((read=(int)(in.getFilePointer()-originaloffset))<attr.code_length) {
            int inst = in.readUnsignedByte();

            TinaBytecode.Mnemonic mnemo = TinaBytecode.getMnemonic(inst);
            if (mnemo!=null) {
                TinaBytecode bc = new TinaBytecode(inst, 
                          (int)(in.getFilePointer()-originaloffset-1), in, cp);
                if (warmode) {
                    try {
                        System.out.print(bc.smartDisplay(indent, printoffsets));
                    } catch(TinaConstantPoolException e) {
                        System.out.print(bc);
                        error(e);
                    }
                }
            } else {
                if (warmode) 
                    System.out.println("unrecognized_opcode "+inst);
                error("Unknown opcode : "+inst);
            }
            if (warmode) System.out.println();
        }
    }

    private TinaAttribute readExceptionsAttribute(final RandomAccessFile in,
                                      TinaAttribute attr) throws IOException {
        attr.number_of_exceptions = in.readUnsignedShort();
        if (warmode)
            System.out.println("\n;    number_of_exceptions "
                               +attr.number_of_exceptions);
        attr.exception_index_table = new int[attr.number_of_exceptions];
        for (int i=0; i<attr.number_of_exceptions; i++) {
            attr.exception_index_table[i] = in.readUnsignedShort();
            if (warmode)
                System.out.println(";   exception_index_table["+i+"] -> "
                                   +attr.exception_index_table[i]);
        }
        return attr;
    }

    private TinaAttribute readInnerClassesAttribute(final RandomAccessFile in,
                           TinaAttribute attr, String ind) throws IOException {
        attr.number_of_classes = in.readUnsignedShort();
        attr.classes = new int[attr.number_of_classes][4];
        if (warmode)
            System.out.println(ind+"nb_of_classes "+attr.number_of_classes);
        for (int i=0; i<attr.number_of_classes; i++) {
            attr.classes[i][0] = in.readUnsignedShort();
            if (warmode) {
                System.out.println(ind+"classes["+i+"] :");
                System.out.println(ind+"    inner_class_info_index "
                                   +attr.classes[i][0]);
            }
            attr.classes[i][1] = in.readUnsignedShort();
            if (warmode){
                int inner = attr.classes[i][1];
                try {
                    System.out.println(ind+"    inner_name_index "
                                       +inner+(inner==0?"":(" ("
                         +cp.elementAt(inner).smartDisplay()+")")));
                } catch(TinaConstantPoolException e) {
                    System.out.println(ind+"    inner_name_index "+inner);
                    error(e);
                }
            }

            attr.classes[i][2] = in.readUnsignedShort();
            if (warmode) {
                int outer = attr.classes[i][2];
                try {
                    System.out.println(ind+"    outer_name_index "
                                       +outer+(outer==0?"":(" ("
                         +cp.elementAt(outer).smartDisplay()+")")));
                } catch(TinaConstantPoolException e) {
                    System.out.println(ind+"    outer_name_index "+outer);
                    error(e);
                }
            }

            attr.classes[i][3] = in.readUnsignedShort();
            if (warmode) {
                System.out.print(ind+"    inner_class_access_flags 0x"
                               +Integer.toHexString(attr.classes[i][3])+" [ ");

                int flag = attr.classes[i][3];
                if ((flag & 0x4000) == 0x4000) {
                    flag -= 0x4000;
                    System.out.print("ACC_ENUM ");
                }
                if ((flag & 0x2000) == 0x2000) {
                    flag -= 0x2000;
                    System.out.print("ACC_ANNOTATION ");
                }
                if ((flag & 0x1000) == 0x1000) {
                    flag -= 0x1000;
                    System.out.print("ACC_SYNTHETIC ");
                }
                if ((flag & 0x0400) == 0x0400) {
                    flag -= 0x0400;
                    System.out.print("ACC_ABSTRACT ");
                }
                if ((flag & 0x0200) == 0x0200) {
                    flag -= 0x0200;
                    System.out.print("ACC_INTERFACE ");
                }
                if ((flag & 0x0010) == 0x0010) {
                    flag -= 0x0010;
                    System.out.print("ACC_FINAL ");
                }
                if ((flag & 0x0008) == 0x0008) {
                    flag -= 0x0008;
                    System.out.print("ACC_STATIC ");
                }
                if ((flag & 0x0004) == 0x0004) {
                    flag -= 0x0004;
                    System.out.print("ACC_PROTECTED ");
                }
                if ((flag & 0x0002) == 0x0002) {
                    flag -= 0x0002;
                    System.out.print("ACC_PRIVATE ");    
                }
                if ((flag & 0x0001) == 0x0001) {
                    flag -= 0x0001;
                    System.out.print("ACC_PUBLIC ");
                }
                if (flag != 0) 
                    System.out.print("(UNKNOWN_FLAG 0x"
                                     +Integer.toHexString(flag)+") ");
                System.out.println("]");
            }
        }
        return attr;
    }

    private TinaAttribute readSyntheticAttribute(RandomAccessFile in,
                                     TinaAttribute attr) throws IOException {
        return attr;
    }

    private TinaAttribute readDeprecatedAttribute(RandomAccessFile in,
                                      TinaAttribute attr) throws IOException {
        return attr;
    }

    private TinaAttribute readLineNumberTableAttribute(RandomAccessFile in,
                         TinaAttribute attr, String ind) throws IOException {
        attr.line_number_table_length = in.readUnsignedShort();
        if (warmode)
            System.out.println(ind+"line_number_table_length "
                               +attr.line_number_table_length);
        
        attr.line_number_table = new int[attr.line_number_table_length][2];
        for (int i=0; i<attr.line_number_table_length; i++) {
            attr.line_number_table[i][0] = in.readUnsignedShort();
            attr.line_number_table[i][1] = in.readUnsignedShort();

            if (warmode) {
                System.out.print(ind+"    line_nb_table["+i+"] -> start_pc ");
                System.out.print(attr.line_number_table[i][0]+", line_nb ");
                System.out.println(attr.line_number_table[i][1]);
            }
        }
        return attr;
    }

    private TinaAttribute readLocalVariableTableAttribute(RandomAccessFile in,
                        TinaAttribute attr, String indstr) throws IOException {
        return readLocalVariableTableAttribute(in, attr, indstr, false);
    }

    private TinaAttribute readLocalVariableTableAttribute(RandomAccessFile in,
                         TinaAttribute attr, String indstr, boolean signature)
                                                           throws IOException {
        attr.local_variable_table_length = in.readUnsignedShort();
        if (warmode)
            System.out.println(indstr+"local_variable_table_length "
                               +attr.local_variable_table_length);
        for (int i=0; i<attr.local_variable_table_length; i++) {
            System.out.println(indstr+"local_variable_table["+i+"] :");
            int j = in.readUnsignedShort();
            if(warmode)
                System.out.println(indstr+"    start_pc "+j);
            j = in.readUnsignedShort();
            if(warmode)
                System.out.println(indstr+"    length "+j);
            j = in.readUnsignedShort();
            try {
                System.out.println(indstr+"    name_index "+j+" ("
                    +cp.elementAt(j).smartDisplay()+")");
            } catch(TinaConstantPoolException e) {
                System.out.println("TINAPOC_ERROR_INVALID_INDEX)");
                error(e);
            }
            j = in.readUnsignedShort();
            try {
                String s = signature ? "signature_index " : "descriptor_index ";
                System.out.println(indstr+"    "+s+j+" ("
                    +cp.elementAt(j).smartDisplay()+")");
            } catch(TinaConstantPoolException e) {
                System.out.println("TINAPOC_ERROR_INVALID_INDEX)");
                error(e);
            }
            System.out.println(indstr+"    index "+in.readUnsignedShort());
        }
        return attr;
    }

    private TinaAttribute readUNKNOWNAttribute(RandomAccessFile in,
              TinaAttribute attr, String indstr) throws IOException {
        attr.info = new byte[(int)attr.length];
        if (warmode)
            System.out.print(indstr+"info = ");
        for (int j=0; j<attr.info.length; j++) {
            attr.info[j] = in.readByte();
            if (warmode) System.out.print("0x"+toHexString(attr.info[j])+" ");
        }
        if(warmode)
            System.out.println();
        return attr;
    }


    private void readMethodsCount(RandomAccessFile in) throws IOException {
        methods = new TinaMethod[in.readUnsignedShort()];
        if (warmode)
            System.out.println("; methods_count "+methods.length+"\n");
    }

    private void readMethods(RandomAccessFile in) throws IOException {
        int access_flags, name_index, descriptor_index;
        TinaAttribute[] attrib;
        for (int i=0; i<methods.length; i++) {
            if (warmode) {
                System.out.print("; methods["+i+"]");
                if (printoffsets) {
                    long fp = in.getFilePointer();
                    System.out.print(" (offset 0x"+Long.toHexString(fp)+")");
                }
                System.out.println(" :");
            }
            access_flags = in.readUnsignedShort();
            int flag = access_flags;
            if (warmode) {
                System.out.print(";     access_flags 0x"
                                 +Integer.toHexString(flag)+" [ ");
                if ((flag & 0x0800) == 0x0800) {
                    flag -= 0x0800;
                    System.out.print("ACC_STRICT ");
                }
                if ((flag & 0x0400) == 0x0400) {
                    flag -= 0x0400;
                    System.out.print("ACC_ABSTRACT ");
                }
                if ((flag & 0x0100) == 0x0100) {
                    flag -= 0x0100;
                    System.out.print("ACC_NATIVE ");
                }
                if ((flag & 0x0080) == 0x0080) {
                    flag -= 0x0080;
                    System.out.print("ACC_VARARGS ");
                }
                if ((flag & 0x0040) == 0x0040) {
                    flag -= 0x0040;
                    System.out.print("ACC_BRIDGE ");
                }
                if ((flag & 0x0020) == 0x0020) {
                    flag -= 0x0020;
                    System.out.print("ACC_SYNCHRONIZED ");
                }
                if ((flag & 0x0010) == 0x0010) {
                    flag -= 0x0010;
                    System.out.print("ACC_FINAL ");
                }
                if ((flag & 0x0008) == 0x0008) {
                    flag -= 0x0008;
                    System.out.print("ACC_STATIC ");
                }
                if ((flag & 0x0004) == 0x0004) {
                    flag -= 0x0004;
                    System.out.print("ACC_PROTECTED ");
                }
                if ((flag & 0x0002) == 0x0002) {
                    flag -= 0x0002;
                    System.out.print("ACC_PRIVATE ");
                }
                if ((flag & 0x0001) == 0x0001) {
                    flag -= 0x0001;
                    System.out.print("ACC_PUBLIC ");
                }
                if (flag!=0) 
                    System.out.print("UNKNOWN_FLAG : 0x"
                                     +Integer.toHexString(flag)+" ");
                System.out.println("]");
            }
            name_index = in.readUnsignedShort();
            if (warmode) {
                System.out.print(";     name_index "+name_index+" (");
                try {
                    System.out.println(cp.getDirectName(name_index)+")");
                } catch(TinaConstantPoolException e) {
                    System.out.println("TINAPOC_ERROR_INVALID_INDEX)");
                    error(e);
                }
            }
            descriptor_index = in.readUnsignedShort();
            if (warmode) {
                System.out.print(";     descriptor_index "+descriptor_index+" (");
                try {
                    System.out.println(cp.getDirectName(descriptor_index)+")");
                } catch(TinaConstantPoolException e) {
                    System.out.println("TINAPOC_ERROR_INVALID_INDEX)");
                    error(e);
                }
            }
            attrib = new TinaAttribute[in.readUnsignedShort()];
            if (warmode) 
                System.out.println(";     attributes_count "+attrib.length);
            for (int j=0; j<attrib.length; j++) {
                if (warmode) 
                    System.out.println(";     method_attributes["+j+"] :");
                attrib[j] = readAttribute(in, 9);
            }
            if (warmode) System.out.print("\n\n");
            methods[i] = new TinaMethod(access_flags, name_index,
                                        descriptor_index, attributes);
        }
    }

    private void readAttributesCount(RandomAccessFile in) throws IOException {
        attributes = new TinaAttribute[in.readUnsignedShort()];
        if (warmode) 
            System.out.println("; attributes_count "+attributes.length);
    }

    private void readAttributes(RandomAccessFile in) throws IOException {
        for (int i=0; i<attributes.length; i++) {
            if (warmode) {
                System.out.print("\n; class_attributes["+i+"]");
                if (printoffsets) {
                    long fp = in.getFilePointer();
                    System.out.print(" (offset 0x"+Long.toHexString(fp)+")");
                }
                System.out.println(" :");
            }
            attributes[i] = readAttribute(in, 5);
        }
    }





/*****************************************************************************/
/*                                HELPER METHODS                             */
/*****************************************************************************/
    private void error(String msg) {
        System.err.println("ERROR: "+msg);
        if (exitonerror)
            System.exit(1);
    }

    private void error(Exception ex) {
        ex.printStackTrace();
        if (exitonerror)
            System.exit(1);
    }

    private long readUnsignedInt(RandomAccessFile in) throws IOException {
        long l = 0l;
        l  = in.readUnsignedByte() << 24;
        l |= in.readUnsignedByte() << 16;
        l |= in.readUnsignedByte() << 8;
        l |= in.readUnsignedByte();
        return l;
    }

    private String toHexString(byte b) {
        byte firstbyte = (byte)((b & 0xF0) >> 4);
        byte secondbyte = (byte)(b & 0x0F);
        StringBuilder res = new StringBuilder().append(getHexChar(firstbyte));
        return res.append(getHexChar(secondbyte)).toString();
    }

    private String stripNewLines(String str) {
        int i;
        StringBuffer buf = new StringBuffer(str);
        while ((i=str.indexOf("\n"))!=-1) {
            buf.deleteCharAt(i);
            buf.insert(i, "\\n");
            str = buf.toString();
        }
        while ((i=str.indexOf("\r"))!=-1) {
            buf.deleteCharAt(i);
            buf.insert(i, "\\r");
            str = buf.toString();
        }
        return str;
    }

    private char getHexChar(byte b) {
        switch (b) {
            case 0 : return '0';
            case 1 : return '1';
            case 2 : return '2';
            case 3 : return '3';
            case 4 : return '4';
            case 5 : return '5';
            case 6 : return '6';
            case 7 : return '7';
            case 8 : return '8';
            case 9 : return '9';
            case 10 : return 'A';
            case 11 : return 'B';
            case 12 : return 'C';
            case 13 : return 'D';
            case 14 : return 'E';
            case 15 : return 'F';
            default : return '*'; // SHOULD NEVER HAPPEN
        }
    }
}
