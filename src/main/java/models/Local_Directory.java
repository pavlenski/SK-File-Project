package models;

import exceptions.*;
import interfaces.Directory_Manipulation_Interface;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Local_Directory implements Directory_Manipulation_Interface {

    private String storage_path = null;

    public Local_Directory() {

    }

    @Override
    public void create_directory(String path, String directory_name) throws Create_Directory_Exception {
        if (!path.isEmpty() && path.startsWith(storage_path)) {
            File file = new File(path + File.separator + directory_name);
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("Directory \"" + directory_name + "\" successfully created at \"" + path + "\".");
                } else {
                    throw new Create_Directory_Exception();
                }
            } else {
                throw new Create_Directory_Exception();
            }
        } else {
            throw new Create_Directory_Exception();
        }
    }

    @Override
    public void delete_directory(String path) throws Delete_Exception {
        if (!path.isEmpty() && path.startsWith(storage_path)) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    FileUtils.deleteDirectory(file);
                    System.out.println("Directory \"" + file.getName() + "\" successfully deleted from \"" + path.substring(0, path.length() - file.getName().length()) + "\".");
                } catch (IOException e) {
                    throw new Delete_Exception();
                }
            } else {
                throw new Delete_Exception();
            }
        } else {
            throw new Delete_Exception();
        }
    }

    @Override
    public void download_directory(String source, String destination) throws Download_Exception {

        if (!destination.contains(source) && !source.isEmpty() && !destination.isEmpty() && destination.startsWith(storage_path)) {
            File source_directory = new File(source);
            if (source_directory.exists() && source_directory.isDirectory()) {
                File destination_directory = new File(destination + File.separator + source_directory.getName());
                if (!destination_directory.exists()) {
                    try {
                        FileUtils.copyDirectory(source_directory, destination_directory);
                    } catch (IOException e) {
                        throw new Download_Exception();
                    }
                } else {
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
    public void upload_directory(String source, String destination) throws Upload_Exception {
        // In local, upload is same as download.
    }

    @Override
    public void upload_multiple_directories(List<File> files, String destination, String name) throws Upload_Multiple_Exception {
        // In local, upload is same as download.
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    @Override
    public void generate_archive_directory(String path, String archive_name) throws Archive_Exception {

        if (!path.isEmpty() && !archive_name.isEmpty()) {
            File file = new File(path);
            if (file.isDirectory() && file.exists()) {
                try {
                    if (!archive_name.endsWith(".zip")) archive_name += ".zip";

                    FileOutputStream fos = new FileOutputStream(path.substring(0, path.length() - file.getName().length()) + archive_name);
                    ZipOutputStream zipOut = new ZipOutputStream(fos);

                    zipFile(file, file.getName(), zipOut);

                    zipOut.close();
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
    public void upload_multiple_archived_directories(List<File> files, String destination, String name) throws Upload_Multiple_Archives_Exception {
        //no point, same as download multiple
    }

    @Override
    public void move_directory(String source, String destination) throws Move_Exception {
        if(!destination.contains(source) && !source.isEmpty() && !destination.isEmpty() && destination.startsWith(storage_path)) {
            File source_directory = new File(source);
            if (source_directory.exists() && source_directory.isDirectory()) {
                File destination_directory = new File(destination + File.separator + source_directory.getName());
                if (!destination_directory.exists()){
                    try {
                        FileUtils.moveDirectory(source_directory, destination_directory);
                        System.out.printf("Directory %s is successfully moved to %s!\n", source, destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
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
    public void rename_directory(String path, String new_name) throws Rename_Exception {

        File old_name_file = new File(path);
        if (old_name_file.exists() && old_name_file.isDirectory()) {
            path = path.substring(0, path.length() - old_name_file.getName().length());
            File new_name_file = new File(path + File.separator + new_name);
            if (!new_name_file.exists()){
                if (old_name_file.renameTo(new_name_file)) {
                    System.out.println("Directory renamed successfully");
                } else {
                    System.out.println("Failed to rename directory");
                    throw new Rename_Exception();
                }
            } else {
                throw new Rename_Exception();
            }
        } else {
            throw new Rename_Exception();
        }

    }

    public String getStorage_path() {
        return storage_path;
    }

    public void setStorage_path(String storage_path) {
        this.storage_path = storage_path;
    }

}
