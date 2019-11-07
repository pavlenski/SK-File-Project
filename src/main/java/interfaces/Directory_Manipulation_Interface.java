package interfaces;

import exceptions.*;

import java.io.File;
import java.util.List;

public interface Directory_Manipulation_Interface {

    void create_directory(String path, String directory_name) throws Create_Directory_Exception;

    void create_multiple_directories(String path, String common_directories_name, int no_directories) throws Create_Multiple_Directories_Exception;

    void delete_directory(String path) throws Delete_Exception;

    void upload_directory(String source, String destination) throws Upload_Exception;

    void download_directory(String source, String destination) throws Download_Exception;

    void upload_multiple_directories(List<File> directories, String destination, String name) throws Upload_Multiple_Exception;

    void download_multiple_directories(List<File> directories, String destination, String name) throws Download_Multiple_Exception;

    void generate_archive_directory(String path, String archive_name) throws Archive_Exception;

    void upload_multiple_archived_directories(List<File> files, String destination, String name) throws Upload_Multiple_Archives_Exception;

    void download_multiple_archived_directories(List<File> files, String destination, String name) throws Download_Multiple_Archives_Exception;

    void move_directory(String source, String destination) throws Move_Exception;

    void rename_directory(String path, String new_name) throws Rename_Exception;

    void list_files(String path, String[] extension_filter, boolean recursive) throws List_Files_Exception;

    void list_directories(String path, boolean recursive) throws List_Directories_Exception;

    void create_extension_blacklist(String path, String[] extensions) throws Create_Extension_Blacklist_Exception;

}
