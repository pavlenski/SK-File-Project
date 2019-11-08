package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zipper {

    public static boolean unzip(String file_to_unzip, File destination_dir) {

        byte[] buffer = new byte[1024];
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file_to_unzip));
            ZipEntry zipEntry = zis.getNextEntry();
            while(zipEntry != null) {
                File new_file = new File(destination_dir, zipEntry.getName());
                FileOutputStream fos = new FileOutputStream(new_file);
                int len;
                while((len = zis.read(buffer)) > 0) { fos.write(buffer, 0, len); }
                fos.close();
                zipEntry = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
