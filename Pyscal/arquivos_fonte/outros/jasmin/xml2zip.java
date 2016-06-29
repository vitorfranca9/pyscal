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
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;
import tina.utils.*;

public class xml2zip extends DefaultHandler {
    private SAXParser parser;
    private File inputfile;
    private DataOutputStream out;
    private Element current;
    private StringBuilder data;
    private enum Element {
        value,   // file data
        crc_32,
        compressed_size,
        uncompressed_size,
        archive_extra_data_signature,
        archive_extra_field_length,
        extra_field_length,
        extra_field,
        local_file_header_signature,
        central_file_header_signature,
        version_made_by,
        compatibility_of_file_attribute_information,
        version_needed_to_extract,
        compatibility_of_file_attribute_information_bis,
        general_purpose_bit_flag,
        compression_method,
        last_mod_file_time,
        last_mod_file_date,
        file_name_length,
        file_comment_length,
        disk_number_start,
        internal_file_attributes,
        external_file_attributes,
        relative_offset_of_local_header,
        file_name,
        file_comment,
        header_signature,
        size_of_data,
        signature_data,
        end_of_central_dir_signature,
        number_of_this_disk,
        number_of_the_disk_with_start_of_central_directory,
        number_of_central_directory_entries_on_this_disk,
        total_number_of_central_directory_entries,
        size_of_central_directory,
        central_directory_offset,
        archive_comment_length,
        archive_comment,

  // nothing to do with these :
        archive,
        file_data,
        file_header,
        local_file_header,
        data_descriptor,
        archive_extra_data,
        digital_signature,
        end_of_central_dir;
    }
    
    public xml2zip(String filename) throws Exception {
        inputfile = new File(filename);
        data = new StringBuilder();
        SAXParserFactory f = SAXParserFactory.newInstance();
        parser = f.newSAXParser();
    }
    
    public void build(OutputStream out) throws Exception {
        this.out = new DataOutputStream(out);
        parser.parse(new FileInputStream(inputfile), this);
        this.out.close();
    }
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        try {
            current = Enum.valueOf(Element.class, qName);
        } catch(IllegalArgumentException e) {
            System.out.println("Unknown element : "+qName);
        }
    } 

    public void endElement(String uri, String localName, String qName) {
        try {
            Element e = Enum.valueOf(Element.class, qName);
            String s = data.toString();
            try {
                switch(e) {
// 1 byte values
                    case compatibility_of_file_attribute_information : 
                    case compatibility_of_file_attribute_information_bis : 
                        out.writeByte(Byte.parseByte(removeSpecialChars(s).trim())); break;



// special 1 byte values
                    case version_made_by : 
                    case version_needed_to_extract : 
                        s = removeSpecialChars(s);
                        int i = s.indexOf(".");
                        if(i<0) {
                            System.out.println("Invalid version number. Expecting x.y value.");
                            System.exit(1);
                        }
                        String s1 = s.substring(0, i).trim();
                        String s2 = s.substring(i+1, s.length()).trim();
                        out.writeByte(Integer.parseInt(s1)*10+Integer.parseInt(s2));
                        break;


// 2 bytes values
                    case extra_field_length : 
                    case general_purpose_bit_flag : 
                    case compression_method : 
                    case last_mod_file_time : 
                    case last_mod_file_date : 
                    case file_name_length : 
                    case file_comment_length : 
                    case disk_number_start : 
                    case internal_file_attributes : 
                    case size_of_data : 
                    case number_of_this_disk : 
                    case number_of_the_disk_with_start_of_central_directory : 
                    case number_of_central_directory_entries_on_this_disk : 
                    case total_number_of_central_directory_entries : 
                    case archive_comment_length : 
                        writeShort(removeSpecialChars(s).trim()); break;




// 4 bytes values
                    case size_of_central_directory : 
                    case central_directory_offset : 
                    case external_file_attributes : 
                    case relative_offset_of_local_header : 
                    case end_of_central_dir_signature : 
                    case archive_extra_data_signature : 
                    case local_file_header_signature : 
                    case central_file_header_signature : 
                    case header_signature : 
                    case archive_extra_field_length : 
                    case crc_32 : 
                    case compressed_size : 
                    case uncompressed_size : 
                        writeInt(removeSpecialChars(s).trim()); break;



// base 64 fields
                    case signature_data : 
                    case extra_field : 
                    case file_comment : 
                    case archive_comment : 
                    case value : 
                        printFileData(removeSpecialChars(data.toString()));   // print the data
                        break;


// string field
                    case file_name : 
                        s = removeSpecialChars(s);
                        out.write(s.getBytes(), 0, s.length());
                        break;


// nothing to output with these
                    case archive : 
                    case file_data : 
                    case file_header :
                    case local_file_header :
                    case data_descriptor :
                    case archive_extra_data :
                    case digital_signature :
                    case end_of_central_dir :
                        break;


// default state, should not happen !
                    default : 
                        System.out.println("undefined state, element was "+e);
                        System.exit(1);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            data = new StringBuilder();                           // reset the buffer
        } catch(IllegalArgumentException e) {
            System.out.println("Unknown element : "+qName);
        }
    } 

    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        if(!s.startsWith("\n ")) { // exclude indentation
            data.append(s);
        }
    }
    
    public void printFileData(String s) throws IOException {
        Base64.InputStream b64in = new Base64.InputStream(new ByteArrayInputStream(s.getBytes()), Base64.DECODE);
        int read;
        while((read=b64in.read())!=-1) 
            out.writeByte(read);
        out.flush();
    }

// this methids deals with comments and the deprotection of special chars
    private String removeSpecialChars(String str) {
        StringBuilder res = new StringBuilder();
        boolean commenting = false;
        boolean deprotected = false;
        for(int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            switch(c) {
                case '(' :
                    if(!commenting && !deprotected) commenting = true;
                    else if(deprotected) {
                        res.append('(');
                        deprotected = false;
                    }
                    break; 
                case ')' :
                    if(commenting) commenting = false;
                    else if(deprotected) {
                        res.append(')');
                        deprotected = false;
                    }
                    break;
                case '\\' :
                    if(!commenting) { 
                        if(deprotected) { 
                            res.append('\\');
                            deprotected = false;
                        }
                        else deprotected = true;
                    }
                    break;
                default : 
                    if(!commenting) res.append(c);
                    else if(deprotected) {
                        res.append(c);
                        deprotected = false;
                    }
            }
        }
        return res.toString();
    }

    private void writeInt(String s) throws IOException {
        if(s==null || s.length()==0) return;
        try {
            if(s.startsWith("0x"))
                writeInt((int)Long.parseLong(s.substring(2, s.length()), 16));
            else
                writeInt((int)Long.parseLong(s));
        } catch(NumberFormatException e) {
            System.out.println("current element : "+current+", current string : "+s);
        }
    }

    private void writeInt(int i) throws IOException {
        out.writeInt(Integer.reverseBytes(i));
    }

    private void writeShort(String s) throws IOException {
        if(s==null || s.length()==0) return;
        if(s.startsWith("0x")) 
            writeShort((short)Integer.parseInt(s.substring(2, s.length()), 16));
        else 
            writeShort((short)Integer.parseInt(s));
    }

    private void writeShort(short s) throws IOException {
        out.writeShort(Short.reverseBytes(s));
    }
     
    public static void main(String[] arg) {
        if(arg.length!=1) {
            System.out.println("Usage : java xml2zip <filename>");
            System.exit(1);
        }
        try {
            xml2zip builder = new xml2zip(arg[0]);
            builder.build(System.out);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}