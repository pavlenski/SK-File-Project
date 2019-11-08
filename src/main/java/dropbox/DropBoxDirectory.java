package dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.FolderMetadata;
import exceptions.*;
import interfaces.Directory_Manipulation_Interface;
import model.DropBoxChecker;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DropBoxDirectory extends AbstractDropBoxProvider implements Directory_Manipulation_Interface {

    private List<String> extension_blacklist = new ArrayList<String>();

    @Override
    public void create_directory(String path, String directory_name) throws Create_Directory_Exception {

        if(!path.isEmpty()) {

            if(path.equals("ROOT")) path = CloudStroage.getStorageRootPath(); //gets root
            if(path.equals("/")) path = "";
            String full_path = path + "/" + directory_name;

            if(!DropBoxChecker.check_path(getClient(), full_path)) {
                try {
                    getClient().files().createFolderV2(full_path);
                } catch (DbxException e) {
                    e.printStackTrace();
                    throw new Create_Directory_Exception();
                }
            } else {
                throw new Create_Directory_Exception(); //treba neki existing exception da baci
            }
        } else {
            throw new Create_Directory_Exception();
        }
        System.out.println("Successfully created '" + directory_name + "' directory on the cloud.");
    }

    @Override
    public void create_multiple_directories(String path, String common_directories_name, int no_directories) throws Create_Multiple_Directories_Exception {

        if(!path.isEmpty()) {
            if(path.contains("ROOT")) path = CloudStroage.getStorageRootPath();

            for (int i = 0 ; i < no_directories ; i++) {
                if (DropBoxChecker.check_path(getClient(), path + "/" + common_directories_name + (i + 1))) {
                    throw new Create_Multiple_Directories_Exception(); //treba existing path da baci
                }
            }
            for (int i = 0 ; i < no_directories ; i++) {
                String full_path = path + "/" + common_directories_name + (i + 1);
                try {
                    getClient().files().createFolderV2(full_path);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Create_Multiple_Directories_Exception();
                }
            }


        } else {
            throw new Create_Multiple_Directories_Exception();
        }
        System.out.println("Succesfully created multiple directories on the cloud: {" + common_directories_name + "1. . ." + common_directories_name + no_directories + "}");
    }

    @Override
    public void delete_directory(String path) throws Delete_Exception {

        if(!path.isEmpty()) {

            try {
                FolderMetadata folder_data = (FolderMetadata) getClient().files().getMetadata(path); //dodati exception za kastovanje, tj. ako nije direktorijum da baca nesto
                if(!DropBoxChecker.check_path(getClient(), path)) throw new Delete_Exception();
                getClient().files().deleteV2(path);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Delete_Exception();
            }
        } else {
            throw new Delete_Exception();
        }
        System.out.println("Successfully deleted desired directory.");
    }

    @Override
    public void download_directory(String source, String destination) throws Download_Exception {       //moram ovde da obezbedim da ne moze direktno na C: nego mora u neki folder da se radi akcija
        if(!source.isEmpty() && !destination.isEmpty()) {
            String split_dir[] = source.split("/");
            String split_local_dir[] = destination.split("\\\\");
            String zip_download_path = "";
            for(int i = 0 ; i < split_local_dir.length - 1 ; i++) {
                zip_download_path = zip_download_path + split_local_dir[i] + "\\";
            }
            zip_download_path = zip_download_path.substring(0, zip_download_path.length() - 1);
            String directory_name = split_dir[split_dir.length - 1];
            String full_destination = zip_download_path + File.separator + directory_name + ".zip";
            File zip_file = new File(full_destination);
            zip_file.deleteOnExit();

//            System.out.println(zip_download_path);
//            System.out.println(full_destination);

            try (OutputStream download_file = new FileOutputStream(full_destination)){
                getClient().files().downloadZip(source).download(download_file);
                new net.lingala.zip4j.ZipFile(full_destination).extractAll(destination);   //unzipuje skinuti zip

            } catch (Exception e) {
                e.printStackTrace();
                throw new Download_Exception();
            }

            System.out.println("Successfully downloaded " + directory_name + " to the local machine.");
        } else {
            throw new Download_Exception();
        }
    }

    @Override
    public void download_multiple_directories(List<File> list, String s, String s1) throws Download_Multiple_Exception {
        System.out.println("no pls");
    }

    @Override
    public void upload_multiple_directories(List<File> list, String s, String s1) throws Upload_Multiple_Exception {
        System.out.println("no pls");
    }

    @Override            //"C:\\Users\\pavlenski\\Desktop\\SOFT KOMP\\malina\\unzip", "/Multipla"
    public void upload_directory(String source, String destination) throws Upload_Exception {                   //to be usrano
        if(!source.isEmpty() && !destination.isEmpty()) {
            if(destination.equals("ROOT")) destination = CloudStroage.getStorageRootPath();

            String split_local_dir[] = source.split("\\\\");
            String dir_name = split_local_dir[split_local_dir.length - 1];
            String created_zip_path = "";
            for(int i = 0 ; i < split_local_dir.length - 1 ; i++) {
                created_zip_path = created_zip_path + split_local_dir[i] + "\\";
            }
            created_zip_path = created_zip_path.substring(0, created_zip_path.length() - 1);
            System.out.println(created_zip_path);
            String full_path = destination + "/" + dir_name + ".zip";
            created_zip_path = created_zip_path + "\\" + dir_name + ".zip";

            try {
                File file = new File(created_zip_path);                                     //ukratko on zipuje dir koji oce da posalje gore na cloud
                file.deleteOnExit();                                                        //posalje ga na cloud i onda obrise zip koji je lokalno
                new ZipFile(file).addFolder(new File(source));
                try (InputStream in = new FileInputStream(file)) {
                    getClient().files().uploadBuilder(full_path).uploadAndFinish(in);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Upload_Exception();
                }
            } catch (ZipException e) {
                e.printStackTrace();
            }
            System.out.println("Successfully uploaded directory to the cloud.");
        } else {
            throw new Upload_Exception();
        }
    }

    @Override
    public void generate_archive_directory(String s, String s1) throws Archive_Exception {
        System.out.println("no pls");
    }

    @Override                                               //file_list, "/Kemcun", "vo_lare"
    public void upload_multiple_archived_directories(List<File> files, String destination, String name) throws Upload_Multiple_Archives_Exception {
        if(destination.isEmpty()) throw new Upload_Multiple_Archives_Exception();
        if(name.isEmpty()) throw new Upload_Multiple_Archives_Exception(); // Treba invalid name exception
        if(destination.equals("ROOT")) {
            destination = CloudStroage.getStorageRootPath();  //ovde ostaje samo root_dir_path jer sam dodajem '/' na full_destination kasnije
        } else {
            if(!DropBoxChecker.check_path(getClient(), destination)) throw new Upload_Multiple_Archives_Exception();
            if(!DropBoxChecker.check_folder_meta_data(getClient(), destination)) throw new Upload_Multiple_Archives_Exception();
        }
        for(File f : files) { if(!f.exists() || !f.isDirectory()) throw new Upload_Multiple_Archives_Exception(); }

        String split_local_dir[] = files.get(0).getAbsolutePath().split("\\\\");
        String zip_path = "";
        for(int i = 0 ; i < split_local_dir.length - 1 ; i++) { zip_path = zip_path + split_local_dir[i] + File.separator; }
        zip_path = zip_path.substring(0, zip_path.length() - 1);
        zip_path = zip_path + File.separator + name + ".zip";

        String full_destination = destination + "/" + name + ".zip";

        try {
            File zip_file = new File(zip_path);
            zip_file.deleteOnExit();
            ZipFile zip = new ZipFile(zip_file);
            for(File f : files) {
                zip.addFolder(f);
            }
            try (InputStream in = new FileInputStream(zip_file)) {
                getClient().files().uploadBuilder(full_destination).uploadAndFinish(in);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Upload_Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Upload_Multiple_Archives_Exception();
        }
        System.out.println("Successfully uploaded '" + name + ".zip' to the cloud.");
    }

    @Override
    public void download_multiple_archived_directories(List<File> list, String s, String s1) throws Download_Multiple_Archives_Exception {
        System.out.println("no pls");
    }

    @Override
    public void move_directory(String source, String destination) throws Move_Exception {
        if(!source.isEmpty() && !destination.isEmpty()) {
            if(destination.equals("ROOT")) {
                destination = CloudStroage.getStorageRootPath(); // treba + '/'
            } else {
                if(!DropBoxChecker.check_path(getClient(), destination)) throw new Move_Exception();
                if(!DropBoxChecker.check_folder_meta_data(getClient(), destination)) throw new Move_Exception();
            }
            if(!DropBoxChecker.check_path(getClient(), source)) throw new Move_Exception();
            if(!DropBoxChecker.check_folder_meta_data(getClient(), source)) throw new Move_Exception();

            String split_dir[] = source.split("/");
            String dir_name = split_dir[split_dir.length - 1];
            String full_path = destination + "/" + dir_name;
            if(full_path.equals(source)) throw new Move_Exception();

            try {
                getClient().files().moveV2(source, full_path);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Move_Exception();
            }
            System.out.println("Successfully moved " + dir_name + " to desired location.");
        } else {
            throw new Move_Exception();
        }
    }

    @Override
    public void rename_directory(String s, String s1) throws Rename_Exception {
        System.out.println("no pls");
    }

    @Override                       //"/Kemcun/Goran", null, true
    public void list_files(String path, String[] extension_filter, boolean recursive) throws List_Files_Exception {
        if(path.isEmpty()) throw new List_Files_Exception();
        if(path.equals("ROOT")) {
            path = CloudStroage.getStorageRootPath() + "/";
        } else {
            if(!DropBoxChecker.check_path(getClient(), path)) throw new List_Files_Exception();
        }
        try (OutputStream dld_file = new FileOutputStream("scramble\\test_files.zip")){

            getClient().files().downloadZip(path).download(dld_file);
            File test_zip = new File("scramble\\test_files.zip");
            test_zip.deleteOnExit();
            ZipFile zip = new ZipFile(test_zip);
            List<FileHeader> headers = zip.getFileHeaders();
            boolean no_files = true;
            for (FileHeader fh : headers) {
                if(!fh.isDirectory()) {
                    String suffix[] = fh.getFileName().split("\\.");
                    if(recursive) {
                        if(suffix.length > 1) { if(!DropBoxChecker.check_extension(extension_filter, "." + suffix[suffix.length - 1])) continue; }  //okej u prevodu ako nema extension
                        //else if(extension_filter != null) continue;                                                                        //i extensioni nizu zadati printovace fajl
                        no_files = false;                                                                                               //u suprotnom preskocice korak i nastaviec dalje sa petljom
                        System.out.println(fh.getFileName());                                                                               //ako ima extension i extensioni su zadati proverice dal ga ima u spisku
                    } else {                                                                                                               //i printovace, ako nisu zadati extensioni svakako ce vratiti true (ovaj checker)
                        //System.out.println(suffix.length);
                        if(suffix.length > 1) { if(!DropBoxChecker.check_extension(extension_filter, "." + suffix[suffix.length - 1])) continue; } //i izlistace se zeljeni element
                        //else if(extension_filter != null) continue;
                        String split[] = fh.getFileName().split("/");
                        //System.out.println(fh.getFileName() + " length: " + split.length);
                        if(split.length == 2) {
                            System.out.println(fh.getFileName());
                            no_files = false;
                        }
                    }
                }
            }
            if(no_files) System.out.println("No files found!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new List_Files_Exception();
        }
    }

    @Override
    public void list_directories(String path, boolean recursive) throws List_Directories_Exception {
        if(path.isEmpty()) throw new List_Directories_Exception();
        if(path.equals("ROOT")) {
            path = CloudStroage.getStorageRootPath() + "/";
        } else {
            if(!DropBoxChecker.check_path(getClient(), path)) throw new List_Directories_Exception();
        }
        try (OutputStream dld_file = new FileOutputStream("scramble\\test_dirs.zip")){

            getClient().files().downloadZip(path).download(dld_file);
            File test_zip = new File("scramble\\test_dirs.zip");
            test_zip.deleteOnExit();
            ZipFile zip = new ZipFile(test_zip);
            List<FileHeader> headers = zip.getFileHeaders();
            boolean no_files = true;
            for (FileHeader fh : headers) {
                if(fh.isDirectory()) {
                    if(recursive) {
                        String split[] = fh.getFileName().split("/");
                        if(split.length == 1) continue;
                        no_files = false;
                        System.out.println(fh.getFileName());
                    } else {
                        String split[] = fh.getFileName().split("/");
                        if(split.length == 2) {
                            System.out.println(fh.getFileName());
                            no_files = false;
                        }
                    }
                }
            }
            if(no_files) System.out.println("No directories found!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new List_Directories_Exception();
        }
    }

    public void init_blacklist() throws Exception {
        File file = new File("scramble" + File.separator + "storage.meta");
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = "";
                line = br.readLine(); //to bypass storage path line in file
                List<String> all = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    Collections.addAll(all, line.split(";"));
                }
                if (!all.isEmpty()){
                    for (String s:all){
                        extension_blacklist.add(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void rewrite_extensions() {
        File file = new File("scramble" + File.separator + "storage.meta");
        if (file.exists()) {
            try {
                FileWriter fw = new FileWriter(file);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(CloudStroage.getStorageRootPath());
                StringBuilder sb = new StringBuilder();
                for (String s : extension_blacklist) {
                    sb.append(s);
                    sb.append(";");
                }
                sb.deleteCharAt(sb.length() - 1);
                pw.println(sb);
                pw.close();
                fw.close();

                DropBoxFile db_file = new DropBoxFile();
                db_file.delete_file(CloudStroage.getStorageRootPath() + "/storage.meta");
                db_file.upload_file("scramble" + File.separator + "storage.meta", CloudStroage.getStorageRootPath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void create_extension_blacklist(String path, String[] new_extensions) throws Create_Extension_Blacklist_Exception {
        if (new_extensions == null) throw new Create_Extension_Blacklist_Exception();
        for (String s:new_extensions){
            if (!extension_blacklist.contains(s)) extension_blacklist.add(s);
        }
        Collections.sort(extension_blacklist);
        System.out.println("Extenstions added.");
        rewrite_extensions();
    }

    @Override
    public void search_files(String name) throws Search_Files_Exception {
        if(name.isEmpty()) throw new Search_Files_Exception();
        String path = CloudStroage.getStorageRootPath(); // ovo promeni JEBOTE
        if(!DropBoxChecker.check_path(getClient(), path)) throw new Search_Files_Exception();

        try (OutputStream dld_file = new FileOutputStream("scramble\\test_search.zip")) {
            getClient().files().downloadZip(path).download(dld_file);
            File test_zip = new File("scramble\\test_search.zip");
            test_zip.deleteOnExit();
            ZipFile zip = new ZipFile(test_zip);
            List<FileHeader> headers = zip.getFileHeaders();
            boolean no_files = true;

            for(FileHeader fh : headers) {
                String split[] = fh.getFileName().split("/");
                if(split[split.length - 1].contains(name)) {
                    System.out.println(fh.getFileName());
                    no_files = false;
                }
            }
            if(no_files) System.out.println("No matching items found!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Search_Files_Exception();
        }
    }

    public List<String> getExtension_blacklist() {
        return extension_blacklist;
    }
}
