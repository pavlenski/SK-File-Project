package dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import exceptions.*;
import interfaces.File_Manipulation_Interface;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DropBoxFile extends AbstractDropBoxProvider implements File_Manipulation_Interface {

    public DropBoxFile() {/* :D */}

    @Override
    public void create_file(String s, String s1) throws Create_File_Exception, Invalid_Path_Exception {

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
            String full_path = destination + "\\" + split[split.length - 1];

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

    @Override
    public void download_multiple_files(List<File> list, String s, String s1) throws Download_Multiple_Exception {

    }


    @Override
    public void upload_file(String source, String destination) throws Upload_Exception {

        if(!source.isEmpty()) {

            File file = new File(source);

            if(file.exists()) {

                String full_path = ROOT_DIRECTORY_PATH + destination + file.getName();

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

    @Override
    public void upload_multiple_files(List<File> list, String s, String s1) throws Upload_Multiple_Exception {

    }


    @Override
    public void generate_archive_file(String s, String s1) throws Archive_Exception {

    }

    @Override
    public void upload_multiple_archived_files(List<File> list, String s, String s1) throws Upload_Multiple_Archives_Exception {

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
    public void rename_file(String s, String s1) throws Rename_Exception, Invalid_Path_Exception {

    }
}
