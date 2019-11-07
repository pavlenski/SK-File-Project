import dropbox.DropBoxFile;
import exceptions.Upload_Exception;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        DropBoxFile db_file = new DropBoxFile();

        try {
            //db_file.upload_file("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\galantic.txt", "");
            //db_file.download_file("/pricko.txt", "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP");
            //db_file.delete_file("/pricko.txt");
            //db_file.move_file("/Nebojsa/galantic.txt", "");
            //db_file.create_file("/Nebojsa", "udbmila.legenda");
            //db_file.rename_file("/Nebojsa/milomir.txt", "nojan");

            File file1 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\aebre.txt");
            File file2 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\sreckovic.txt");
            File file3 = new File("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\ulantic.txt");
//            List<File> file_list = new ArrayList<File>();
//            file_list.add(file1);file_list.add(file2);file_list.add(file3);
//            db_file.upload_multiple_files(file_list, "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina", "/Multipla"); //ili salji poslednji parametar kao ROOT

//            File file1 = new File("/Multipla/aebre.txt");
//            File file2 = new File("/Multipla/sreckovic.txt");
//            File file3 = new File("/Multipla/ulantic.txt");
            List<File> file_list = new ArrayList<File>();
            file_list.add(file1); file_list.add(file2); file_list.add(file3);
//            db_file.download_multiple_files(file_list, "", "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina");

            //db_file.generate_archive_file("/Nebojsa/nojan.txt", "lipe_cvatu");
            db_file.upload_multiple_archived_files(file_list, "/Nebojsa", "borivoje");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
