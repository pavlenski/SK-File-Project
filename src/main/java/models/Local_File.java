package models;

import exceptions.*;
import interfaces.File_Manipulation_Interface;

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

        if(!path.isEmpty() && path.startsWith(storage_path)) {
            File file = new File(path + File.separator + file_name);
            if(!file.exists()) {
                try {
                    file.createNewFile();
                    System.out.println("File \"" + file.getName() + "\" successfully created.");
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
    public void delete_file(String path) throws Delete_Exception {

        if(!path.isEmpty() && path.startsWith(storage_path)) {
            File file = new File(path);
            if(file.exists() && file.isFile()) {
                file.delete();
                System.out.println("File \"" + file.getName() + "\" successfully deleted.");
            } else {
                throw new Delete_Exception();
            }
        } else {
            throw new Delete_Exception();
        }

    }

    @Override
    public void download_file(String source, String destination) throws Download_Exception {

        if(!source.isEmpty() && !destination.isEmpty() && destination.startsWith(storage_path)) {
            File file = new File(source);
            if(file.exists() && file.isFile()) {
                File downloaded_file = new File(destination + File.separator + file.getName());
                try {
                    downloaded_file.createNewFile();
                    Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(destination + File.separator + file.getName()), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File " + file.getName() + " successfully copied to \"" + destination + "\".");
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

        if (!source.isEmpty() && !destination.isEmpty() && source.startsWith(storage_path) && destination.startsWith(storage_path)){
            for (File file : files){
                if (file.exists() && file.isFile()){
                    try{
                        try {
                            Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(destination + File.separator + file.getName()), StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("Files are successfully copied to \"" + destination + "\".");
                        } catch (Exception e) {
                            throw new Download_Multiple_Exception();
                        }
                    } catch (Download_Multiple_Exception e) {
                        throw new Download_Multiple_Exception();
                    }
                } else {
                    continue;
                }
            }
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

        if(!path.isEmpty() && !archive_name.isEmpty()) {
            File file = new File(path);
            if(file.isFile() && file.exists()) {
                try {
                    if (!archive_name.endsWith(".zip")) archive_name += ".zip";

                    FileOutputStream fos = new FileOutputStream(path.substring(0, path.length() - file.getName().length()) +  archive_name);
                    ZipOutputStream zipOut = new ZipOutputStream(fos);

                    FileInputStream fis = new FileInputStream(file);
                    ZipEntry zipEntry = new ZipEntry(file.getName());

                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;

                    while((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }

                    zipOut.close();
                    fis.close();
                    fos.close();
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

    @Override
    public void move_file(String source, String destination) throws Move_Exception {

        if(!source.isEmpty() && !destination.isEmpty() && destination.startsWith(storage_path)) {
            File file = new File(source);
            if(file.exists() && file.isFile()){
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

        if(!path.isEmpty() && path.startsWith(storage_path)) {
            File file = new File(path);
            if(file.exists() && file.isFile()) {
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
