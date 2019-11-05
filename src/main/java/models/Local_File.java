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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Local_File implements File_Manipulation_Interface {

    public Local_File() {

    }

    @Override
    public void create_file(String path, String file_name) throws Create_File_Exception, Invalid_Path_Exception {

        if(!path.isEmpty()) {

            File file = new File(path + File.separator + file_name);

            if(!file.exists()) {

                try {
                    file.createNewFile();
                } catch (Exception e) {
                    throw new Create_File_Exception();
                }

                System.out.println("File successfully created.");

            } else {
                throw new Create_File_Exception();
            }

        } else {
            throw new Invalid_Path_Exception();
        }

    }

    @Override
    public void delete_file(String path) throws Delete_Exception {

        if(!path.isEmpty()) {

            File file = new File(path);
            if(file.exists() && file.isFile()) {
                file.delete();
            } else {
                throw new Delete_Exception();
            }

            System.out.println("File successfully deleted.");

        } else {
            throw new Delete_Exception();
        }

    }

    private void copy_file(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }

    }

    @Override
    public void download_file(String source, String destination) throws Download_Exception {

        if(!source.isEmpty() && !destination.isEmpty()) {

            File file = new File(source);
            if(file.exists() && file.isFile()) {

                File downloaded_file = new File(destination + File.separator + file.getName());
                try {
                    downloaded_file.createNewFile();
                    Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(destination + File.separator + file.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    throw new Download_Exception();
                }

                System.out.println("File successfully downloaded to " + destination + ".");

            } else {
                throw new Download_Exception();
            }

        } else {
            throw new Download_Exception();
        }

    }

    @Override
    public void download_multiple_files(ArrayList<File> files, String source, String destination) throws Download_Multiple_Exception {

        if (!source.isEmpty() && !destination.isEmpty()){

            for (File file : files){
                if (file.exists() && file.isFile()){
                    try{
                        try {
                            Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(destination + File.separator + file.getName()), StandardCopyOption.REPLACE_EXISTING);
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
            System.out.println("Files are successfully downloaded to " + destination + ".");

        } else {
            throw new Download_Multiple_Exception();
        }

    }

    @Override
    public void upload_file(String source, String destination) throws Upload_Exception {

        //download_file(destination, source);
    }

    @Override
    public void upload_multiple_files(ArrayList<File> files, String source, String destination) throws Upload_Multiple_Exception {


    }

    @Override
    public void generate_archive_file(String path, String archive_name) throws Archive_Exception {

        if(!path.isEmpty() && !archive_name.isEmpty()) {
            File file = new File(path);
            if(file.isFile() && file.exists()) {

                try {
                    FileInputStream in = new FileInputStream(file.getAbsolutePath());
                    ZipOutputStream out = new ZipOutputStream(new FileOutputStream("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\" + archive_name + ".zip"));
                    out.putNextEntry(new ZipEntry(file.getName()));

                    byte[] b = new byte[1024];
                    int count;

                    while ((count = in.read(b)) > 0) {
                        out.write(b, 0, count);
                    }
                    out.close();
                    in.close();
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
    public void upload_multiple_archived_files(ArrayList<File> list, String s, String s1) throws Upload_Multiple_Archives_Exception {

    }

    @Override
    public void move_file(String source, String destination) throws Move_Exception {

        if(!source.isEmpty() && !destination.isEmpty()) {

            File file = new File(source);
            if(file.exists() && file.isFile()){

                try {
                    Files.move(Paths.get(file.getAbsolutePath()), Paths.get(destination + File.separator + file.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    throw new Move_Exception();
                }

                System.out.println("File " + file.getName() + " successfully moved to " + destination + ".");

            } else {
                throw new Move_Exception();
            }
        } else {
            throw new Move_Exception();
        }

    }

    @Override
    public void rename_file(String path, String new_name) throws Rename_Exception, Invalid_Path_Exception {

        if(!path.isEmpty()) {

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



}
