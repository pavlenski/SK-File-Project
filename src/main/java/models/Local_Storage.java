package models;

import exceptions.*;
import interfaces.File_Manipulation_Interface;
import model.User;
import model.UserPriority;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class Local_Storage {

    private static Local_Storage instance = null;

    private Local_Storage() {

        //runLocal();
        test_start();
    }

    void test_start() {
        Local_File local_file = new Local_File();
        try {
            //local_file.create_file("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina", "Sreckovic");
            //local_file.delete_file("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina" + File.separator + "Srecko");
            //local_file.download_file("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\galantic.txt", "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP");

//            File file1 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\galantic.txt");
//            File file2 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\pricko.txt");
//            File file3 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\sreckovic.txt");
//
//            ArrayList<File> file_list = new ArrayList<>();
//
//            file_list.add(file1);
//            file_list.add(file2);
//            file_list.add(file3);
//
//            local_file.download_multiple_files(file_list, "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina", "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP");
            //local_file.rename_file("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\nebosja.txt", "galantic.txt");
            //local_file.move_file("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\pricko.txt", "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP");
            local_file.generate_archive_file("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\sreckovic.txt", "kiseli");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void runLocal() {

        //C:\Users\pavlenski\Desktop\SOFT KOMP\malina

        String path;

        Scanner sc = new Scanner(System.in);
        System.out.println("ENTER DESIRED PATH: ");
        path = sc.nextLine();

        String username;
        String password;

        if(!path.isEmpty()) {
            System.out.println("PATH SET..");

           // List<String> result = new ArrayList<>();

            try {
                File file = new File(path);

                if(file.exists()) {
                    System.out.println("FILE EXISTS");
                    System.out.println("FILE CREATED..\n..PLEASE LOGIN..\nUSERNAME:");

                    if(file.isDirectory()) {
                        //search(".*\\.txt", file, result);
                    }
                } else {
                    System.out.println("FILE DOESNT EXIST");
                    file.mkdir();
                    System.out.println("FILE CREATED.. \n..ENTER YOUR CREDENTIALS..\nUSERNAME:");
                    username = sc.nextLine();
                    System.out.println("PASSWORD:");
                    password = sc.nextLine();

                    User admin = new User(username, password, UserPriority.ADMIN);
                    System.out.println(admin + " CREATED!");
                }

                /*for (String s : result) {
                    System.out.println(s);
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("printerror");
        }

    }

    public static Local_Storage getInstance() {
        if(instance == null) instance = new Local_Storage();
        return instance;
    }

}
