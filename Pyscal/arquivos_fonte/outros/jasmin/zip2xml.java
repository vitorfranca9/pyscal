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

import java.io.*;
import tina.utils.Base64;

public class zip2xml {
    private final static int LOCAL_FILE_HEADER = 0x504B0304, 
                             ARCHIVE_EXTRA_DATA = 0x504B0608,   
                             CENTRAL_FILE_HEADER = 0x504B0102,    
                             HEADER = 0x504B0505,
                             ZIP64_END_OF_CENTRAL_DIR = 0x504B0606,
                             ZIP64_END_OF_CENTRAL_DIR_LOCATOR = 0x504b0607,
                             END_OF_CENTRAL_DIR = 0x504B0506;

    RandomAccessFile file;
    PrintStream out;
    private static final String[] OS = { "MS-DOS, OS/2", "Amiga", "OpenVMS", "Unix", "VM/CMS", "Atari ST", "OS/2 HPFS", "Macintosh", "Z-System", "CP/M", "Windows NTFS", "MVS", "VSE", "Acorn Risc", "VFAT", "alternate MVS", "BeOS", "Tandem", "OS/400", "OS/X" };
    private static final String[] compressions = {
          " (the file is stored, no compression)",
          " (the file is Shrunk)",
          " (the file is Reduced with compression factor 1)",
          " (the file is Reduced with compression factor 2)",
          " (the file is Reduced with compression factor 3)",
          " (the file is Reduced with compression factor 4)",
          " (the file is Imploded)",
          " (reserved for Tokenizing compression algorithm)",
          " (the file is Deflated)",
          " (enhanced Deflating using Deflate64)",
          " (PKWARE Data Compression Library Imploding)",
          " (reserved by PKWARE)",
          " (file is compressed using BZIP2 algorithm)"
    };
    private boolean descriptor;

    public zip2xml(String str) throws IOException {
        file = new RandomAccessFile(str, "r");
    }

    public void printReport(PrintStream s) throws IOException {
        long filelength = file.length();
        out = s;
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        out.println("<archive>");

        while(file.getFilePointer()!=filelength) { // more bytes available
            int readsig = file.readInt();
            file.seek(file.getFilePointer()-4);

            switch(readsig) {
                case LOCAL_FILE_HEADER : 
                    printFile();
                    break;

                case ARCHIVE_EXTRA_DATA :
                    printArchiveExtraData();
                    break;

                case CENTRAL_FILE_HEADER :
                    printFileHeader(false);
                    break;

                case HEADER :
                    printDigitalSignature();
                    break;

                case ZIP64_END_OF_CENTRAL_DIR :
                    System.out.println("Zip64 end of central dir not implemented");
                    file.seek(filelength);  // get out of the loop
                    break;

                case ZIP64_END_OF_CENTRAL_DIR_LOCATOR :
                    System.out.println("Zip64 end of central dir locator not implemented");
                    file.seek(filelength);  // get out of the loop
                    break;

                case END_OF_CENTRAL_DIR :
                    printEndOfCentralDir();
                    break;

                default : 
                    out.println("Unknown signature : 0x"+Integer.toHexString(readsig));
                    file.seek(filelength);  // get out of the loop
            }
        }
        out.println("</archive>");
    }

    private void printFile() throws IOException {
        long length = printFileHeader(true);
        printFileData(length);
        if(descriptor) {
            descriptor = false;
            out.println("  <data_descriptor>");
            printDataDescriptor(2);
            out.println("  </data_descriptor>");
        }
    }

    private void printFileData(long length) throws IOException {
        if(length==0) 
            out.println("  <file_data><value>(empty)</value></file_data>");
        else {
            out.println("  <file_data>");
            out.println("    <value>");
            printData(length);
            out.println("    </value>");
            out.println("  </file_data>");
        }
    }

    private long printDataDescriptor(int indent) throws IOException {
        long compressed;
        StringBuffer buf = new StringBuffer();
        for(int i=0; i<indent; i++) 
            buf.append(" ");
        String indstr = buf.toString();
        out.println(indstr+"<crc_32>0x"+Integer.toHexString(readInt())+"</crc_32>");
        out.println(indstr+"<compressed_size>"+(compressed=readUnsignedInt())+"</compressed_size>");
        out.println(indstr+"<uncompressed_size>"+readUnsignedInt()+"</uncompressed_size>");
        return compressed;
    }

    private void printArchiveExtraData() throws IOException {
        long eflength;
        out.println("  <archive_extra_data>");
        out.println("    <archive_extra_data_signature>"+Integer.toHexString(readInt())+"</archive_extra_data_signature>");
        out.println("    <archive_extra_field_length>"+(eflength=readUnsignedInt())+"</extra_field_length>");
        out.println("    <extra_field>");
        printData(eflength);
        out.println("    </extra_field>");
        out.println("  </archive_extra_data>");
    }

    private long printFileHeader(boolean local) throws IOException {
        int comp;
        out.println(local?"  <local_file_header>":"  <file_header>");
        if(local)
            out.println("    <local_file_header_signature>0x"+Integer.toHexString(readInt())+"</local_file_header_signature>");
        else 
            out.println("    <central_file_header_signature>0x"+Integer.toHexString(readInt())+"</central_file_header_signature>");

        if(!local) {
            int version = file.readUnsignedByte();
            out.println("    <version_made_by>"+(version/10)+"."+(version%10)+"</version_made_by>");

            out.print("    <compatibility_of_file_attribute_information>"+(comp=file.readUnsignedByte()));
            if(comp>=OS.length) out.print(" (undefined value)");
            else out.print(" ("+OS[comp]+")");
            out.println("</compatibility_of_file_attribute_information>");
        }
        int version = file.readUnsignedByte();
        out.println("    <version_needed_to_extract>"+(version/10)+"."+(version%10)+"</version_needed_to_extract>");

        out.print("    <compatibility_of_file_attribute_information_bis>"+(comp=file.readUnsignedByte()));
        if(comp>=OS.length) out.print(" (undefined value)");
        else out.print(" ("+OS[comp]+")");
        out.println("</compatibility_of_file_attribute_information_bis>");
        int flag;
        out.print("    <general_purpose_bit_flag>0x"+Integer.toHexString(flag=readShort()));
        if((flag&0x0001)!=0) out.print(" (Encrypted)");
        if(descriptor=((flag&0x0008)!=0)) out.print(" (Data Descriptor)"); 
        if((flag&0x0010)!=0) out.print(" (Enhanced Deflating)");
        if((flag&0x0020)!=0) out.print(" (Compressed Patched Data)");
        if((flag&0x0040)!=0) out.print(" (Strong Encryption)");

        out.println("</general_purpose_bit_flag>");

        int compression;
        out.print("    <compression_method>"+(compression=(readShort())));
        if(compression>=compressions.length) out.print(" (undefined value)");
        else out.print(compressions[compression]);
        out.println("</compression_method>");

        out.println("    <last_mod_file_time>0x"+Integer.toHexString(readShort())+"</last_mod_file_time>");
        out.println("    <last_mod_file_date>0x"+Integer.toHexString(readShort())+"</last_mod_file_date>");
        int fnlen, eflen, fclen = 0;
        long compressed;
        compressed = printDataDescriptor(4);
        out.println("    <file_name_length>"+(fnlen=readShort())+"</file_name_length>");
        out.println("    <extra_field_length>"+(eflen=readShort())+"</extra_field_length>");

        if(!local) {
            out.println("    <file_comment_length>"+(fclen=readShort())+"</file_comment_length>");
            out.println("    <disk_number_start>"+readShort()+"</disk_number_start>");
            out.println("    <internal_file_attributes>0x"+Integer.toHexString(readShort())+"</internal_file_attributes>");
            out.println("    <external_file_attributes>0x"+Integer.toHexString(readInt())+" (host system dependent)</external_file_attributes>");
            out.println("    <relative_offset_of_local_header>0x"+Long.toHexString(readUnsignedInt())+"</relative_offset_of_local_header>");
        }

        byte[] buf = new byte[fnlen];
        for(int i=0; i<fnlen; i++)
            buf[i] = file.readByte();
        out.println("    <file_name>"+removeSpecialChars(new String(buf))+"</file_name>");

        if(eflen==0) out.println("    <extra_field>(empty)</extra_field>");
        else {
            out.println("    <extra_field>");
            printData(eflen);
            out.println("    </extra_field>");
        }

        if(!local) {
            if(fclen != (int)fclen)
                System.out.println("ERROR : file_comment_length too big ! (please report this error)");
            else {
                if(fclen==0) 
                    out.println("    <file_comment>(empty)</file_comment>");
                else {
                    byte[] cbuf = new byte[(int)fclen];
                    for(int i=0; i<fclen; i++)
                        cbuf[i] = file.readByte();
                    out.println("    <file_comment>"+new String(cbuf)+"</file_comment>");
                }
            }
        }

        out.println(local?"  </local_file_header>":"  </file_header>");
        return compressed;
    }

    private void printDigitalSignature() throws IOException {
        int datasize;
        out.println("  <digital_signature>");
        out.println("    <header_signature>"+Integer.toHexString(Integer.reverseBytes(file.readInt()))+"</header_signature>");
        out.println("    <size_of_data>"+(datasize=readShort())+"</size_of_data>");
        out.println("    <signature_data>");
        printData(datasize);
        out.println("    </signature_data>");
        out.println("  </digital_signature>");
    }

    private void printEndOfCentralDir() throws IOException {
        int aclen;
        out.println("  <end_of_central_dir>");
        out.println("    <end_of_central_dir_signature>0x"+Integer.toHexString(Integer.reverseBytes(file.readInt()))+"</end_of_central_dir_signature>");
        out.println("    <number_of_this_disk>"+readShort()+"</number_of_this_disk>");
        out.println("    <number_of_the_disk_with_start_of_central_directory>"+readShort()+"</number_of_the_disk_with_start_of_central_directory>");
        out.println("    <number_of_central_directory_entries_on_this_disk>"+readShort()+"</number_of_central_directory_entries_on_this_disk>");
        out.println("    <total_number_of_central_directory_entries>"+readShort()+"</total_number_of_central_directory_entries>");
        out.println("    <size_of_central_directory>"+readUnsignedInt()+"</size_of_central_directory>");
        out.println("    <central_directory_offset>0x"+Long.toHexString(readUnsignedInt())+"</central_directory_offset>");
        out.println("    <archive_comment_length>"+(aclen=readShort())+"</archive_comment_length>");


        if(aclen==0) out.println("    <archive_comment>(empty)</archive_comment>");
        else {
            out.println("    <archive_comment>");
            printData(aclen);
            out.println("    </archive_comment>");
        }
        out.println("  </end_of_central_dir>");
    }

    private void printData(long datalength) throws IOException {
        Base64.OutputStream b64out = new Base64.OutputStream(out, Base64.ENCODE);
        for(int i=0; i<datalength; i++) {

/*** hex bytes output ***/
//            out.printf("%02x", file.readUnsignedByte());
//            out.print((((i+1)%17)==0)?indstr:" ");

/*** base64 output ***/
            b64out.write(file.readUnsignedByte());
        }
        b64out.flushBase64();
        out.println();
    }

    private int swapIntBytes(int i) {
        return (((i&0xFF000000)>>24) + ((i&0x00FF0000)>>8) + ((i&0x0000FF00)<<8) + ((i&0x000000FF)<<24));
    }

    private long swapIntBytes(int i, int j) {
        return (long)(((i&0xFF00)>>8) + ((i&0x00FF)<<8) + ((j&0xFF00)<<8) + (long)((j&0x00FF)<<24));
    }

    private int swapShortBytes(int i) {
        return (((i&0xFF00)>>8) + ((i&0x00FF)<<8));
    }

    private int readInt() throws IOException {
        return Integer.reverseBytes(file.readInt());
    }

    private long readUnsignedInt() throws IOException {
        return swapIntBytes(file.readUnsignedShort(), file.readUnsignedShort());
    }

    private int readShort() throws IOException {
        return swapShortBytes(file.readUnsignedShort());
    }

    private String removeSpecialChars(String str) {
        str = str.replaceAll("\\\\", "\\\\\\\\");
        str = str.replaceAll("\\(", "\\\\\\(");
        str = str.replaceAll("\\)", "\\\\\\)");
        return str;
    }

    public static void main(String[] arg) {
        if(arg.length!=1) {
            System.out.println("Usage : java zip2xml <filename>");
            System.exit(1);
        }
        try {
            zip2xml spy = new zip2xml(arg[0]);
            spy.printReport(System.out);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

