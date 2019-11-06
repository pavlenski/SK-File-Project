package dropbox;

import exceptions.*;
import interfaces.Directory_Manipulation_Interface;

import java.io.File;
import java.util.List;

public class DropBoxDirectory extends AbstractDropBoxProvider implements Directory_Manipulation_Interface {

    @Override
    public void create_directory(String s, String s1) throws Create_Directory_Exception {

    }

    @Override
    public void delete_directory(String s) throws Delete_Exception {

    }

    @Override
    public void download_directory(String s, String s1) throws Download_Exception {

    }

    @Override
    public void upload_directory(String s, String s1) throws Upload_Exception {

    }

    @Override
    public void upload_multiple_directories(List<File> list, String s, String s1) throws Upload_Multiple_Exception {

    }

    @Override
    public void generate_archive_directory(String s, String s1) throws Archive_Exception {

    }

    @Override
    public void upload_multiple_archived_directories(List<File> list, String s, String s1) throws Upload_Multiple_Archives_Exception {

    }

    @Override
    public void move_directory(String s, String s1) throws Move_Exception {

    }

    @Override
    public void rename_directory(String s, String s1) throws Rename_Exception {

    }
}
