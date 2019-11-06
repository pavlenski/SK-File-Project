package models;

import exceptions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestingClass2 {

    public static void main(String[] args) {
        Local_File local = new Local_File();
        Local_Directory directory = new Local_Directory();
        local.setStorage_path("C:\\Users\\Nikola\\Desktop\\SK Test");
        directory.setStorage_path("D:\\SK Test");
        /*try {
            local.create_file("C:\\Users\\Nikola\\Desktop\\SK Test", "Test file");
        } catch (Create_File_Exception e) {
            e.printStackTrace();
        } catch (Invalid_Path_Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            local.delete_file("C:\\Users\\Nikola\\Desktop\\SK Test\\Test file");
        } catch (Delete_Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            local.download_file("D:\\display.png", "C:\\Users\\Nikola\\Desktop\\SK Test");
        } catch (Download_Exception e) {
            e.printStackTrace();
        }*/

        /*final File folder = new File("D:\\");
        List<File> result = new ArrayList<>();
        search(".*\\.png", folder, result);
        local.download_multiple_files(result, , );*/

        /*try {
            local.generate_archive_file("C:\\Users\\Nikola\\Desktop\\SK Test\\Test.txt", "test.");
        } catch (Archive_Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            local.move_file("D:\\display.png", "C:\\Users\\Nikola\\Desktop\\SK Test\\");
        } catch (Move_Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            local.rename_file("C:\\Users\\Nikola\\Desktop\\SK Test\\display.png", "new_name.png");
        } catch (Rename_Exception e) {
            e.printStackTrace();
        } catch (Invalid_Path_Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            directory.create_directory("C:\\Users\\Nikola\\Desktop\\SK Test\\", "new_dir");
        } catch (Create_Directory_Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            directory.delete_directory("D:\\SK Test\\inner");
        } catch (Delete_Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            directory.download_directory("D:\\SK Test\\Other folder\\naziv.zip", "D:\\SK Test");
        } catch (Download_Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            directory.generate_archive_directory("D:\\SK Test\\Other folder", "naziv");
        } catch (Archive_Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            directory.move_directory("D:\\SK Test\\Other folder\\Inner other folder", "D:\\SK Test");
        } catch (Move_Exception e) {
            e.printStackTrace();
        }*/

        try {
            directory.rename_directory("D:\\SK Test\\Other folder", "Novo ime");
        } catch (Rename_Exception e) {
            e.printStackTrace();
        }
    }

    public static void search(final String pattern, final File folder, List<File> result) {
        for (final File f : folder.listFiles()) {
/*
            if (f.isDirectory()) {
                search(pattern, f, result);
            }
*/
            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f);
                }
            }

        }
    }

}
