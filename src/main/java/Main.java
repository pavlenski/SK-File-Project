import dropbox.DropBoxFile;
import exceptions.Upload_Exception;

public class Main {

    public static void main(String[] args) {
        DropBoxFile db_file = new DropBoxFile();

        try {
            //db_file.upload_file("C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\galantic.txt", "/");
            //db_file.download_file("/pricko.txt", "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP");
            //db_file.delete_file("/pricko.txt");
            db_file.move_file("/Nebojsa/galantic.txt", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
