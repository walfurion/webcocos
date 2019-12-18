/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author josee
 */
public class Zip  {
    
    
    public InputStream makeZip(String nameZip, List<Object[]> files) throws FileNotFoundException, IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(out);
        for (Object[] srcFile : files) {            
            ZipEntry zipEntry = new ZipEntry(((String) srcFile[0]));
            zipOut.putNextEntry(zipEntry);
            ByteArrayOutputStream fi = (ByteArrayOutputStream) srcFile[1];
            zipOut.write(fi.toByteArray());
        }
        zipOut.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
    
}
