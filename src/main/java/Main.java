import com.dropbox.core.v2.DbxClientV2;
import dropbox.CloudStroage;
import dropbox.DropBoxDirectory;
import dropbox.DropBoxFile;
import exceptions.Create_Directory_Exception;
import exceptions.Invalid_Path_Exception;
import exceptions.Upload_Exception;
import model.DropBoxChecker;
import model.UserPriority;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            run_remote();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void start_dir() {

        DropBoxDirectory db_dir = new DropBoxDirectory();
        //start_file();
        try {
            //db_dir.create_directory("ROOT", "Kemcun");
            //db_dir.create_multiple_directories("/Kemcun", "testFile", 4);
            //db_dir.delete_directory("/Kemcun/testFile4");
            //db_dir.download_directory("/Multipla", "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\unzip");
            //db_dir.upload_directory("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\unzip", "/Nebojsa");
            //db_dir.move_directory("/Kemcun", "ROOT");
//            File file1 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\googootka");
//            File file2 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\pinoy");
//            File file3 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\unzip");
//            List<File> file_list = new ArrayList<File>();
//            file_list.add(file1) ; file_list.add(file2) ; file_list.add(file3);
//            db_dir.upload_multiple_archived_directories(file_list, "/Kemcun", "vo_lare"); //racunam da iz samo jednog direktorijuma uzimam vise fajlova, ne iz razlicitih.
            //String[] extensions = {".zip", ".txtd"};
            //db_dir.list_files("/Kemcun", extensions, true);
            //db_dir.list_directories("/Kemcun/Goran", true);
            db_dir.search_files("test");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void start_file() {
        DropBoxFile db_file = new DropBoxFile();

        try {
            //db_file.upload_file("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\galantic.txt", "ROOT");
            //db_file.download_file("/pricko.txt", "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP");
            //db_file.delete_file("/pricko.txt");
            //db_file.move_file("/Nebojsa/galantic.txt", "");
            db_file.create_file("ROOT", "udbmila.legenda");
            //db_file.rename_file("/Nebojsa/milomir.txt", "nojan");

//            File file1 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\aebre.txt");
//            File file2 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\sreckovic.txt");
//            File file3 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\ulantic.txt");
//            List<File> file_list = new ArrayList<File>();
//            file_list.add(file1);file_list.add(file2);file_list.add(file3);
//            db_file.upload_multiple_files(file_list, "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina", "/Multipla"); //ili salji poslednji parametar kao ROOT

//            File file1 = new File("/Multipla/aebre.txt");
//            File file2 = new File("/Multipla/sreckovic.txt");
//            File file3 = new File("/Multipla/ulantic.txt");
//            List<File> file_list = new ArrayList<File>();
//            file_list.add(file1); file_list.add(file2); file_list.add(file3);
//            db_file.download_multiple_files(file_list, "", "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina");

            //db_file.generate_archive_file("/Nebojsa/nojan.txt", "lipe_cvatu");
            //db_file.upload_multiple_archived_files(file_list, "/Nebojsa", "borivoje");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run_remote() throws Exception {

        System.out.println("Enter storage path: ");
        Scanner scanner = new Scanner(System.in);
        String storage_path = scanner.nextLine();

        File storage = null;
        File storage_meta_file = null;
        File users_meta_file = null;

        CloudStroage cloud_storage = new CloudStroage();
        DbxClientV2 client = cloud_storage.getClient();

        DropBoxDirectory db_dir = new DropBoxDirectory();
        DropBoxFile db_file = new DropBoxFile();

        if (!DropBoxChecker.check_path(client, storage_path)) {

            storage_meta_file = new File("scramble" + File.separator + "storage.meta");
            storage_meta_file.createNewFile();
            storage_meta_file.deleteOnExit();;
            users_meta_file = new File("scramble" + File.separator + "users.meta");
            users_meta_file.createNewFile();
            users_meta_file.deleteOnExit();

            try {
                FileWriter fw = new FileWriter(storage_meta_file);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(storage_path);               /// ovde treba da promenim upis u meta fajl
                pw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileWriter fw = new FileWriter(users_meta_file);
                PrintWriter pw = new PrintWriter(fw);
                pw.println("admin:admin:ADMIN");
                pw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            db_dir.create_directory(storage_path, "Cloud-Storage-Test");  //fiksno za sad treba da uzima skener
            CloudStroage.setStorageRootPath(storage_path + "Cloud-Storage-Test");
            db_file.upload_file(storage_meta_file.getAbsolutePath(), storage_path + "Cloud-Storage-Test");
            db_file.upload_file(users_meta_file.getAbsolutePath(), storage_path + "Cloud-Storage-Test");

            System.out.println("Storage created at path: \"" + storage_path + "\".");

        } else {
            if (!DropBoxChecker.check_folder_meta_data(client, storage_path)) {
                System.out.println("Path you entered is not directory!");
                throw new Invalid_Path_Exception();
            } else {
                if (!DropBoxChecker.check_file_meta_data(client, storage_path + "/storage.meta") || !DropBoxChecker.check_file_meta_data(client, storage_path + "/users.meta")) throw new Invalid_Path_Exception();
                else System.out.println("Storage found at path: \"" + storage_path + "\".");
            }
        }



        if (storage == null || storage_meta_file == null || users_meta_file == null) return;

//        Local_User local_user_obj = new Local_User();
//        Local_File local_file_obj = new Local_File();
//        Local_Directory local_directory_obj = new Local_Directory();
//
//        local_user_obj.setUsers_file(users_meta_file.getAbsolutePath());
//        local_file_obj.setStorage_path(storage.getAbsolutePath());              //get storage root
//        local_directory_obj.setStorage_path(storage.getAbsolutePath());
//
//        local_user_obj.init_users();        //

       /* while (true) {
            //System.out.print("[Local storage]: ");

            Scanner option_scanner = new Scanner(System.in);
            String option = option_scanner.nextLine();
            option = option.toLowerCase();

            switch (option) {

                case "log in":
                    if (local_user_obj.getCurrent_user() == null) {
                        System.out.println("Please enter your username:");
                        Scanner sc = new Scanner(System.in);
                        String username = sc.nextLine();
                        System.out.println("Please enter your password:");
                        String password = sc.nextLine();
                        local_user_obj.log_in(username, password);
                    } else {
                        System.out.println("You need to log out first!");
                    }
                    break;

                case "log out":
                    if (local_user_obj.getCurrent_user() != null) local_user_obj.log_out();
                    else System.out.println("You have to be loged in first!");
                    break;

                case "create user":
                    if (local_user_obj.getCurrent_user() != null ){
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN){
                            System.out.println("Please enter new user username:");
                            Scanner sc = new Scanner(System.in);
                            String username = sc.nextLine();
                            System.out.println("Please enter new user password:");
                            String password = sc.nextLine();
                            System.out.println("Please enter new user priority:");
                            String priority = sc.nextLine();
                            priority = priority.toLowerCase();
                            switch (priority){
                                case "admin":
                                    local_user_obj.create_user(username, password, UserPriority.ADMIN);
                                    break;
                                case "basic":
                                    local_user_obj.create_user(username, password, UserPriority.BASIC);
                                    break;
                            }
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be loged in first!");
                    }
                    break;

                case "delete user":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter username of user you want to delete:");
                            Scanner sc = new Scanner(System.in);
                            String username = sc.nextLine();
                            local_user_obj.delete_user(username);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter new file name:");
                            Scanner sc = new Scanner(System.in);
                            String name = sc.nextLine();
                            System.out.println("Please enter path where you want to create this new file:");
                            String path = sc.nextLine();
                            local_file_obj.create_file(path, name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create multiple files":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter path where you want to create these new files:");
                            String path = sc.nextLine();
                            System.out.println("Please enter common name for these new files:");
                            String common_name = sc.nextLine();
                            System.out.println("Please enter how many similar files you want to create:");
                            int no_files = sc.nextInt();
                            local_file_obj.create_multiple_files(path, common_name, no_files);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "delete file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to delete:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            local_file_obj.delete_file(path);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "copy file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to copy:");
                            Scanner sc = new Scanner(System.in);
                            String source = sc.nextLine();
                            System.out.println("Please enter path where you want to paste copied file:");
                            String destination = sc.nextLine();
                            local_file_obj.download_file(source, destination);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "copy multiple files":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter paths of files you want to copy: (separate paths with ';')");
                            String paths = sc.nextLine();
                            String[] parse_paths = paths.split(";");
                            List<File> files = new ArrayList<>();
                            for (String s:parse_paths){
                                File f = new File(s);
                                files.add(f);
                            }
                            System.out.println("Please enter path where you want to paste copied file:");
                            String destination = sc.nextLine();
                            local_file_obj.download_multiple_files(files, "", destination);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "archive file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to archive:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter how you want to name this archive:");
                            String name = sc.nextLine();
                            local_file_obj.generate_archive_file(path, name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "archive multiple files":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter paths of files you want to archive: (separate paths with ';')");
                            String paths = sc.nextLine();
                            String[] parse_paths = paths.split(";");
                            List<File> files = new ArrayList<>();
                            for (String s:parse_paths){
                                File f = new File(s);
                                files.add(f);
                            }
                            System.out.println("Please enter how you want to name this archive:");
                            String archive_name = sc.nextLine();
                            System.out.println("Please enter path where you want to save this archive:");
                            String destination = sc.nextLine();
                            local_file_obj.generate_archive_from_multiple_files(files, archive_name, destination);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "move file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to move:");
                            Scanner sc = new Scanner(System.in);
                            String source = sc.nextLine();
                            System.out.println("Please enter path where you want to move selected file:");
                            String destination = sc.nextLine();
                            local_file_obj.move_file(source, destination);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "rename file":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a file you want to rename:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter this file's new name:");
                            String new_name = sc.nextLine();
                            local_file_obj.rename_file(path, new_name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of where you want to create a directory:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter how you want to name this directory:");
                            String name = sc.nextLine();
                            local_directory_obj.create_directory(path, name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create multiple dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            Scanner sc = new Scanner(System.in);
                            System.out.println("Please enter path where you want to create these new directories:");
                            String path = sc.nextLine();
                            System.out.println("Please enter common name for these new directories:");
                            String common_name = sc.nextLine();
                            System.out.println("Please enter how many similar directories you want to create:");
                            int no_directories = sc.nextInt();
                            local_directory_obj.create_multiple_directories(path, common_name, no_directories);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "delete dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to delete:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            local_directory_obj.delete_directory(path);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "copy dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to copy:");
                            Scanner sc = new Scanner(System.in);
                            String source = sc.nextLine();
                            System.out.println("Please enter path where you want to paste copied directory:");
                            String destination = sc.nextLine();
                            local_directory_obj.download_directory(source, destination);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "archive dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to archive:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter how you want to name this archive:");
                            String name = sc.nextLine();
                            local_directory_obj.generate_archive_directory(path, name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "move dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to move:");
                            Scanner sc = new Scanner(System.in);
                            String source = sc.nextLine();
                            System.out.println("Please enter path where you want to move selected directory:");
                            String destination = sc.nextLine();
                            local_directory_obj.move_directory(source, destination);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "rename dir":
                    if (local_user_obj.getCurrent_user() != null ) {
                        if (local_user_obj.getCurrent_user_priority() == UserPriority.ADMIN) {
                            System.out.println("Please enter path of a directory you want to rename:");
                            Scanner sc = new Scanner(System.in);
                            String path = sc.nextLine();
                            System.out.println("Please enter this directory's new name:");
                            String new_name = sc.nextLine();
                            local_directory_obj.rename_directory(path, new_name);
                        } else {
                            System.out.println("You don't have permission to do this command!");
                        }
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        local_directory_obj.list_files(path, null, false);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files r":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        local_directory_obj.list_files(path, null, true);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files with":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        System.out.println("Please enter extensions of files you want to list: (separate them with ';')");
                        String extension_str = sc.nextLine();
                        String[] extensions = extension_str.split(";");
                        local_directory_obj.list_files(path, extensions, false);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list files with r":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose files you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        System.out.println("Please enter extensions of files you want to list: (separate them with ';')");
                        String extension_str = sc.nextLine();
                        String[] extensions = extension_str.split(";");
                        local_directory_obj.list_files(path, extensions, true);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list dirs":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose directories you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        local_directory_obj.list_directories(path, false);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "list dirs r":
                    if (local_user_obj.getCurrent_user() != null ) {
                        System.out.println("Please enter path of a directory whose directories you want to list:");
                        Scanner sc = new Scanner(System.in);
                        String path = sc.nextLine();
                        local_directory_obj.list_directories(path, true);
                    } else {
                        System.out.println("You have to be logged in first!");
                    }
                    break;

                case "create blacklist":
                    break;

            }

        } */
    }

}
