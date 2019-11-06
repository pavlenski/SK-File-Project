package models;

import exceptions.*;
import interfaces.File_Manipulation_Interface;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Local_File implements File_Manipulation_Interface {

    private String storage_path = null;

    public Local_File() {

    }

    @Override
    public void create_file(String path, String file_name) throws Create_File_Exception, Invalid_Path_Exception {

        if (!path.isEmpty() && path.startsWith(storage_path)) {
            File file = new File(path + File.separator + file_name);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    System.out.println("File \"" + file.getName() + "\" successfully created at \"" + path + "\".");
                } catch (Exception e) {
                    throw new Create_File_Exception();
                }
            } else {
                throw new Create_File_Exception();
            }
        } else {
            throw new Invalid_Path_Exception();
        }

    }

    @Override
    public void create_multiple_files(String path, String common_files_name, int no_files) throws Create_Multiple_Files_Exception {

        if (!path.isEmpty() && path.startsWith(storage_path)) {
            for (int i = 1; i <= no_files; i++) {
                File file = new File(path + File.separator + common_files_name + i);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (Exception e) {
                        throw new Create_Multiple_Files_Exception();
                    }
                } else {
                    throw new Create_Multiple_Files_Exception();
                }
            }
            System.out.println("Files \"" + common_files_name + "{1..." + no_files + "}\" successfully created at \"" + path + "\".");
        } else {
            throw new Create_Multiple_Files_Exception();
        }

    }

    @Override
    public void delete_file(String path) throws Delete_Exception {

        if (!path.isEmpty() && path.startsWith(storage_path)) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                file.delete();
                System.out.println("File \"" + file.getName() + "\" at \"" + path.substring(0, path.length() - file.getName().length()) + "\" successfully deleted.");
            } else {
                throw new Delete_Exception();
            }
        } else {
            throw new Delete_Exception();
        }

    }

    @Override
    public void download_file(String source, String destination) throws Download_Exception {

        if (!source.isEmpty() && !destination.isEmpty() && destination.startsWith(storage_path)) {
            File source_file = new File(source);
            if (source_file.exists() && source_file.isFile()) {
                File destination_file = new File(destination + File.separator + source_file.getName());
                try {
                    FileUtils.copyFile(source_file, destination_file);
                    System.out.println("File \"" + source_file.getName() + "\" successfully copied to \"" + destination + "\".");
                } catch (Exception e) {
                    throw new Download_Exception();
                }
            } else {
                throw new Download_Exception();
            }
        } else {
            throw new Download_Exception();
        }

    }

    @Override
    public void download_multiple_files(List<File> files, String source, String destination) throws Download_Multiple_Exception {

        // Proveriti sa Pajom, source se ovde ne koristi, tako da ako se ne koristi ni na cloud implementaciji, moze da se izbrise iz specifikacije.

        if (!destination.isEmpty() && destination.startsWith(storage_path)) {
            for (File file : files) {
                if (file.exists() && file.isFile()) {
                    File destination_file = new File(destination + File.separator + file.getName());
                    try {
                        FileUtils.copyFile(file, destination_file);
                    } catch (Exception e) {
                        throw new Download_Multiple_Exception();
                    }
                } else {
                    continue;
                }
            }
            System.out.println("Files successfully copied to \"" + destination + "\".");
        } else {
            throw new Download_Multiple_Exception();
        }

    }

    @Override
    public void upload_file(String source, String destination) throws Upload_Exception {
        //In local, it's same as download_file.
    }

    @Override
    public void upload_multiple_files(List<File> list, String s, String s1) throws Upload_Multiple_Exception {
        //In local, it's same as download_multiple_file.
    }

    @Override
    public void upload_multiple_archived_files(List<File> list, String s, String s1) throws Upload_Multiple_Archives_Exception {
        //In local, it's same as download_multiple_archived_files.
    }

    @Override
    public void generate_archive_file(String path, String archive_name) throws Archive_Exception {

        if (!path.isEmpty() && !archive_name.isEmpty()) {
            File file = new File(path);
            if (file.isFile() && file.exists()) {
                try {
                    if (!archive_name.endsWith(".zip")) archive_name += ".zip";

                    FileOutputStream fos = new FileOutputStream(path.substring(0, path.length() - file.getName().length()) + archive_name);
                    ZipOutputStream zipOut = new ZipOutputStream(fos);

                    FileInputStream fis = new FileInputStream(file);
                    ZipEntry zipEntry = new ZipEntry(file.getName());

                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;

                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }

                    zipOut.close();
                    fis.close();
                    fos.close();

                    System.out.println("File " + file.getName() + " successfully archived as \"" + archive_name + "\".");
                } catch (Exception e) {
                    throw new Archive_Exception();
                }
            } else {
                throw new Archive_Exception();
            }
        } else {
            throw new Archive_Exception();
        }

    }

    /*
    try {

        // create byte buffer
        byte[] buffer = new byte[1024];

        FileOutputStream fos = new FileOutputStream(zipFile);

        ZipOutputStream zos = new ZipOutputStream(fos);

        for (int i=0; i < srcFiles.length; i++) {

            File srcFile = new File(srcFiles[i]);

            FileInputStream fis = new FileInputStream(srcFile);

            // begin writing a new ZIP entry, positions the stream to the start of the entry data
            zos.putNextEntry(new ZipEntry(srcFile.getName()));

            int length;

            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();

            // close the InputStream
            fis.close();

        }

        // close the ZipOutputStream
        zos.close();

    }
        catch (IOException ioe) {
        System.out.println("Error creating zip file: " + ioe);
    }
*/
    @Override
    public void generate_archive_from_multiple_files(List<File> files, String archive_name, String destination) throws Generate_Archive_From_Multiple_Files_Exception {

        if (!destination.isEmpty() && destination.startsWith(storage_path) && !archive_name.isEmpty()) {
            try {
                if (!archive_name.endsWith(".zip")) archive_name += ".zip";

                FileOutputStream fos = new FileOutputStream(destination + File.separator + archive_name);
                ZipOutputStream zos = new ZipOutputStream(fos);
                byte[] buffer = new byte[1024];

                for (File file : files) {
                    FileInputStream fis = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    fis.close();
                }

                zos.close();
            } catch (Exception e) {
                throw new Generate_Archive_From_Multiple_Files_Exception();
            }
        } else {
            throw new Generate_Archive_From_Multiple_Files_Exception();
        }

    }

    @Override
    public void move_file(String source, String destination) throws Move_Exception {

        if (!source.isEmpty() && !destination.isEmpty() && destination.startsWith(storage_path)) {
            File file = new File(source);
            if (file.exists() && file.isFile()) {
                try {
                    Files.move(Paths.get(file.getAbsolutePath()), Paths.get(destination + File.separator + file.getName()), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File " + file.getName() + " successfully moved to " + destination + ".");
                } catch (Exception e) {
                    throw new Move_Exception();
                }
            } else {
                throw new Move_Exception();
            }
        } else {
            throw new Move_Exception();
        }

    }

    @Override
    public void rename_file(String path, String new_name) throws Rename_Exception, Invalid_Path_Exception {

        if (!path.isEmpty() && path.startsWith(storage_path)) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                String new_path = path.substring(0, path.length() - file.getName().length());
                file.renameTo(new File(new_path + new_name));
                System.out.println("File " + file.getName() + " successfully renamed to " + new_name + ".");
            } else {
                throw new Rename_Exception();
            }
        } else {
            throw new Invalid_Path_Exception();
        }

    }

    public String getStorage_path() {
        return storage_path;
    }

    public void setStorage_path(String storage_path) {
        this.storage_path = storage_path;
    }
}
