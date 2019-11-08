package model;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.GetMetadataErrorException;
import com.dropbox.core.v2.files.Metadata;

public class DropBoxChecker {

    public static boolean check_path(DbxClientV2 client, String path) {
        try {
//      String query =
//          (path.startsWith(repository.getRoot().toString()) ? path : repository.getRoot().toString() + "/" + path);
            //System.out.println(path.toString());
           client.files().getMetadata(path);
        } catch (GetMetadataErrorException e) {
            if (e.errorValue.isPath() && e.errorValue.getPathValue().isNotFound()) {
                //System.out.println("File not found.");
                return false;
            }
        } catch (DbxException e) {
            return false;
        }
        return true;
    }

    public static boolean check_folder_meta_data(DbxClientV2 client, String remote_path) {
        try {
            FolderMetadata fdata = (FolderMetadata) client.files().getMetadata(remote_path);
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean check_file_meta_data(DbxClientV2 client, String remote_path) {
        try {
            FileMetadata fdata = (FileMetadata) client.files().getMetadata(remote_path);
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean check_extension(String[] extension, String file_suffix) {

        if(extension == null) return true;

        for (int i = 0 ; i < extension.length ; i++) {
            if(file_suffix.equals(extension[i])) return true;
        }
        return false;
    }

}
