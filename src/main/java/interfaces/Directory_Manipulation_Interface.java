package interfaces;

import exceptions.*;

import java.io.File;
import java.util.List;

public interface Directory_Manipulation_Interface {

    void create_directory(String path, String file_name) throws Create_Directory_Exception;

    void delete_directory(String path) throws Delete_Exception;

    void download_directory(String source, String destination) throws Download_Exception;

    void upload_directory(String source, String destination) throws Upload_Exception;

    void upload_multiple_directories(List<File> files, String destination, String name) throws Upload_Multiple_Exception;

    void generate_archive_directory(String path, String archive_name) throws Archive_Exception;

    void upload_multiple_archived_directories(List<File> files, String destination, String name) throws Upload_Multiple_Archives_Exception;

    void move_directory(String source, String destination) throws Move_Exception;

    void rename_directory(String path, String new_name) throws Rename_Exception;

}
