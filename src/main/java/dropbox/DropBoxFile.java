package dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import exceptions.*;
import interfaces.File_Manipulation_Interface;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class DropBoxFile extends AbstractDropBoxProvider implements File_Manipulation_Interface {

    public DropBoxFile() {/* :D */}

    @Override
    public void create_file(String path, String file_name) throws Create_File_Exception, Invalid_Path_Exception {

        if(path.isEmpty()) throw new Invalid_Path_Exception();
        if(file_name.isEmpty()) throw  new Create_File_Exception();

            if(path.equals("ROOT")) path = CloudStroage.getStorageRootPath();

            String split[] = file_name.split("\\.");
            String suffix = "." + split[split.length - 1];
            String name = "";
            for (int i = 0 ; i < split.length - 1 ; i++) {
                name = name + split[i] + ".";
            }
            name = name.substring(0, name.length() - 1);

            try {
                File file = File.createTempFile(name, suffix);
                //System.out.println(file.getName());

                String full_path = ROOT_DIRECTORY_PATH + path + "/" + file_name;

                try (InputStream in = new FileInputStream(file)) {
                    getClient().files().uploadBuilder(full_path).uploadAndFinish(in);
                } catch (Exception e) {
                    e.printStackTrace();                //uploading excpetion
                    throw new Create_File_Exception();
                }

            } catch (Exception e) {
                e.printStackTrace();  //creating file exception
                throw new Create_File_Exception();
            }
            System.out.println("Successfully created " + file_name + " file on the cloud.");
    }

    @Override
    public void create_multiple_files(String s, String s1, int i) throws Create_Multiple_Files_Exception {
        //sutra
    }

    @Override
    public void delete_file(String path) throws Delete_Exception {

        if(!path.isEmpty()){
            String file_name = "";
            try {
                //Metadata data = getClient().files().getMetadata(path);                             -> data used in general
                //FolderMetadata fldata = (FolderMetadata) getClient().files().getMetadata(path);    -> data used for directories
                FileMetadata fdata = (FileMetadata) getClient().files().getMetadata(path);         //-> data used for files
                file_name = fdata.getName();
                getClient().files().deleteV2(fdata.getPathLower());
            } catch (Exception e) {
                //e.printStackTrace();
                throw new Delete_Exception();
            }

            System.out.println("Successfully deleted " + file_name + " from the cloud.");

        } else {
            throw new Delete_Exception();
        }


    }

    @Override
    public void download_file(String source, String destination) throws Download_Exception {

        if(!source.isEmpty() && !destination.isEmpty()) {

            String split[] = source.split("/");
            String full_path = destination + "\\" + split[split.length - 1];  //ovde treba file.separatror!

            try (OutputStream downloadFile = new FileOutputStream(full_path)) {
                getClient().files().downloadBuilder(source).download(downloadFile);
            } catch (DbxException | IOException e) {
                e.printStackTrace();
            }

            System.out.println("File " + split[split.length - 1] + " successfully downloaded to local machine.");

        } else {
            throw new Download_Exception();
        }

    }

    @Override                            //file_list, "", "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina"
    public void download_multiple_files(List<File> files, String source, String destination) throws Download_Multiple_Exception {

        if(!destination.isEmpty()) {
            for (File f : files) {
                String split_dir[] = f.getAbsolutePath().split("\\\\");
                String file_name = split_dir[split_dir.length - 1];
                String path = ROOT_DIRECTORY_PATH + '/';                //morao sam da dodam jer mi sam dodaje C: na pocetku ruta od fajla
                for (int i = 1 ; i < split_dir.length - 1 ; i++) {
                    path = path + split_dir[i] + "/";                   //prvi split je uvek prazan string jer se nasamom pocetku stringa nalazi '/'
                }
                path = path.substring(0, path.length() - 1);
                path = path + "/" + file_name;                              //odakle da skinem

                String full_destination = destination + "\\" + f.getName(); //gde da skinem

                try (OutputStream downloadFile = new FileOutputStream(full_destination)) {
                    FileMetadata file_data = (FileMetadata) getClient().files().getMetadata(path);
                    getClient().files().downloadBuilder(path).download(downloadFile);
                } catch (DbxException | IOException e) {
                    e.printStackTrace();
                    throw new Download_Multiple_Exception();
                }

            }
            System.out.println("Successfully downloaded desired files.");
        } else {
            throw new Download_Multiple_Exception();
        }
    }


    @Override
    public void upload_file(String source, String destination) throws Upload_Exception {

        if(!source.isEmpty()) {

            File file = new File(source);

            if(file.exists()) {

                if(destination.equals("/")) destination = "";
                String full_path = destination + "/" + file.getName();



                try (InputStream in = new FileInputStream(file)) {
                    getClient().files().uploadBuilder(full_path).uploadAndFinish(in);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Upload_Exception();
                }

                System.out.println("Successfully uploaded " + file.getName() + " to the cloud.");
            } else {
                throw new Upload_Exception();
            }
        } else {
            throw new Upload_Exception();
        }


    }

    @Override                        //file_list, "C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina", "/Multipla"
    public void upload_multiple_files(List<File> files, String source, String destination) throws Upload_Multiple_Exception {

        if(!source.isEmpty() && !destination.isEmpty()) {      //destinacija nece nikad biti prazna, ili ce biti neko zeljeno mesto ili ce biti prosledjena kao ROOT

            for(File f : files) { if(!f.exists() || !f.isFile()) throw new Upload_Multiple_Exception(); } //provera fajlova samo da se ne bi jedan ili dva uploadovala pa prekid i onda ostali ne

            for (File f : files) {
                if(f.exists() && f.isFile()) {

                    String full_path = ROOT_DIRECTORY_PATH + destination + "/" + f.getName();

                    try (InputStream in = new FileInputStream(f)) {
                        getClient().files().uploadBuilder(full_path).uploadAndFinish(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Upload_Multiple_Exception();
                    }

                } else {
                    throw new Upload_Multiple_Exception();
                }
            }
            System.out.println("Successfully uploaded desired files to the cloud.");
        } else {
            throw new Upload_Multiple_Exception();
        }
    }


    private boolean zip_file(ZipOutputStream zos, File file) {

        byte[] buffer = new byte[1024];
        int len;

        try {
            FileInputStream fis = new FileInputStream(file);
            zos.putNextEntry(new ZipEntry(file.getName()));

            while((len = fis.read(buffer)) > 0) { zos.write(buffer, 0, len); }

            zos.closeEntry();
            fis.close();
            //zos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override                           //"/Nebojsa/nojan.txt", "lipe_cvatu"
    public void generate_archive_file(String path, String archive_name) throws Archive_Exception {

        if(!path.isEmpty() && !archive_name.isEmpty()) {

            String split_dir[] = path.split("/");
            String temporary_file_path = split_dir[split_dir.length - 1];
            String innitial_path = ROOT_DIRECTORY_PATH;

            for(int i = 0 ; i < split_dir.length - 1 ; i++) { innitial_path = innitial_path + split_dir[i] + "/"; }
            innitial_path = innitial_path.substring(0, innitial_path.length() - 1);

            try (OutputStream downloadFile = new FileOutputStream(temporary_file_path)) {
                getClient().files().downloadBuilder(path).download(downloadFile);

                String zip_file_path = archive_name + ".zip";
                File file = new File(temporary_file_path);   //fajl koji cemo zipovati i poslati na klaud pa da ga skine veniger
                file.deleteOnExit();
                File zip_file = new File(zip_file_path);
                zip_file.deleteOnExit();

                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip_file_path));

                zip_file(zos, file);                            //zipuje mi fajl

                String full_path = innitial_path + "/" + archive_name + ".zip";
                FileInputStream in = new FileInputStream(zip_file);
                getClient().files().uploadBuilder(full_path).uploadAndFinish(in); //upload zip fajla nazad na klaud

                in.close();
                zos.close();

            } catch (DbxException | IOException e) { //razmisli da inicijalizujes stream-ove iznad try-a da bi mogao da ih close-ujes u catch-u u slucaju da pukne program
                e.printStackTrace();
                throw new Archive_Exception();
            }

            System.out.println("Successfully archived wanted item.");

        } else {
            throw new Archive_Exception();
        }
    }

    private boolean zip_multiple_files(ZipOutputStream zos, List<File> files) {

        byte[] buffer = new byte[1024];

        try {
            for(File f : files) {
                FileInputStream fis = new FileInputStream(f);
                zos.putNextEntry(new ZipEntry(f.getName()));
                int len;
                while((len = fis.read(buffer)) > 0) { zos.write(buffer, 0, len); }

                fis.close();
                zos.closeEntry();

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override                                   // file_list, "/Nebojsa", "borivoje"
    public void upload_multiple_archived_files(List<File> files, String destination, String archive_name) throws Upload_Multiple_Archives_Exception {
        if(!destination.isEmpty()) {
            if(destination == "ROOT") destination = ROOT_DIRECTORY_PATH + '/';
            for(File f : files) { if(!f.exists() || !f.isFile()) throw new Upload_Multiple_Archives_Exception(); }

            String zip_file_path = archive_name + ".zip";
            File zip_file = new File(zip_file_path);
            zip_file.deleteOnExit();
            ZipOutputStream zos = null;

            String full_path = destination + "/" + archive_name + ".zip";

            try {
                zos = new ZipOutputStream(new FileOutputStream(zip_file_path));
                zip_multiple_files(zos, files);

                FileInputStream in = new FileInputStream(zip_file);
                getClient().files().uploadBuilder(full_path).uploadAndFinish(in);

                in.close();
                zos.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Upload_Multiple_Archives_Exception();
            }
            System.out.println("Successfully uploaded multiple files as an archive on the cloud.");
        } else {
            throw new Upload_Multiple_Archives_Exception();
        }
    }

    @Override
    public void generate_archive_from_multiple_files(List<File> list, String s, String s1) throws Generate_Archive_From_Multiple_Files_Exception {
            //opvo iznad je to TO JE TOOOO!!
    }

    @Override
    public void move_file(String source, String destination) throws Move_Exception {

        if(!source.isEmpty()){
            String file_name = "";
            try {
                //Metadata data = getClient().files().getMetadata(path);                             -> data used in general
                //FolderMetadata fldata = (FolderMetadata) getClient().files().getMetadata(path);    -> data used for directories
                FileMetadata fdata = (FileMetadata) getClient().files().getMetadata(source);         //-> data used for files
                file_name = fdata.getName();

                String split[] = source.split("/");
                String full_path = destination + "/" + split[split.length - 1];

                getClient().files().moveV2(source, full_path);

            } catch (Exception e) {
                e.printStackTrace();
                throw new Move_Exception();
            }

            System.out.println("Succesfully moved " + file_name + " to the desired destination on the cloud.");

        } else {
            throw new Move_Exception();
        }

    }

    @Override
    public void rename_file(String path, String new_name) throws Rename_Exception, Invalid_Path_Exception {

        if(!path.isEmpty() && !new_name.isEmpty()) {

            String split_dir[] = path.split("/");
            String split_suffix[] = split_dir[split_dir.length - 1].split("\\.");       //pravim inicijalnu putanju koju cu da koristim za upload
            String suffix = "." + split_suffix[split_suffix.length - 1];                      //takodje uzimam odgovarajuci sufiks koji cu dodati na novonapravljen fajl (koji se posle uploaduje)
            String innitial_path = ROOT_DIRECTORY_PATH;                                       //skidam fajl u zeljenom formatu (imenu) -> kazem mu deleteOnExit() ->
            for (int i = 0 ; i < split_dir.length - 1 ; i++) {                                //-> taj lokalni fajl saljem nazad na innitial_path u remote repo.
                innitial_path = innitial_path + split_dir[i] + "/";
            }
            innitial_path = innitial_path.substring(0, innitial_path.length() - 1);

            String temporary_file_path = new_name + suffix; //also the name
            String full_path = innitial_path + "/" + temporary_file_path;

            try (OutputStream downloadFile = new FileOutputStream(temporary_file_path)) {
                FileMetadata file_data = (FileMetadata) getClient().files().getMetadata(path); // this basically checks if the selected file is a file and not something else
                getClient().files().downloadBuilder(path).download(downloadFile);

                File file_to_return = new File(temporary_file_path);
                file_to_return.deleteOnExit();

                try (InputStream in = new FileInputStream(file_to_return)) {
                    getClient().files().uploadBuilder(full_path).uploadAndFinish(in);
                    getClient().files().deleteV2(path);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Rename_Exception();
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new Rename_Exception();
            }

            System.out.println("Successfully renamed the desired file to: " + temporary_file_path);

        } else {
            throw new Invalid_Path_Exception();
        }

    }
}
