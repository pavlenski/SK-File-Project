package interfaces;

import exceptions.*;
import jdk.tools.jlink.internal.Archive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface File_Manipulation_Interface {

    void create_file(String path, String file_name) throws Create_File_Exception, Invalid_Path_Exception;

    void delete_file(String path) throws Delete_Exception;

    void download_file(String source, String destination) throws Download_Exception;

    void download_multiple_files(ArrayList<File> files, String source, String destination) throws Download_Multiple_Exception;

    void upload_file(String source, String destination) throws Upload_Exception;

    void upload_multiple_files(ArrayList<File> files, String destination, String name) throws Upload_Multiple_Exception;

    void generate_archive_file(String path, String archive_name) throws Archive_Exception;

    void upload_multiple_archived_files(ArrayList<File> files, String destination, String name) throws Upload_Multiple_Archives_Exception;

    void move_file(String source, String destination) throws Move_Exception;

    void rename_file(String path, String new_name) throws Rename_Exception, Invalid_Path_Exception;

}
