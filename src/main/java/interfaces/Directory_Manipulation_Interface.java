package interfaces;

import exceptions.*;

import java.io.File;
import java.util.List;

public interface Directory_Manipulation_Interface {

    /**
     * Creates new directory on given path.
     * @param directory_name Name of the directory.
     * @param path Path of the directory on the storage.
     */
    void create_directory(String path, String directory_name) throws Create_Directory_Exception;

    /**
     * Creates multiple new directories on given path.
     * @param path Path of the directory on the storage (where we want to create our directories).
     * @param common_directories_name Common name of all new directories.
     * @param no_directories Number of wanted directories.
     */
    void create_multiple_directories(String path, String common_directories_name, int no_directories) throws Create_Multiple_Directories_Exception;

    /**
     * Deletes directory from given path.
     * @param path Path of the directory on the storage.
     */
    void delete_directory(String path) throws Delete_Exception;

    /**
     * Uploads directory on given path.
     * @param source  Path of the directory on the storage.
     * @param destination Path of the directory where we want to upload.
     */
    void upload_directory(String source, String destination) throws Upload_Exception;

    /**
     * Downloads directory on given path.
     * @param source  Path of the directory on the storage.
     * @param destination Path of the directory where we want to download.
     */
    void download_directory(String source, String destination) throws Download_Exception;

    /**
     * Uploads multiple directories on given path in storage.
     * @param directories List of directories.
     * @param destination Path on the storage where we want to upload directories.
     * @param name        Name of created archive.
     */
    void upload_multiple_directories(List<File> directories, String destination, String name) throws Upload_Multiple_Exception;

    /**
     * Downloads multiple directories on given path in storage.
     * @param directories List of directories.
     * @param destination Path on the storage where we want to download directories.
     * @param name        Name of created archive.
     */
    void download_multiple_directories(List<File> directories, String destination, String name) throws Download_Multiple_Exception;

    /**
     * Generates archive file (zip) from given directory (by using it's path).
     * @param path Path of the directory on the storage.
     * @param archive_name Name for the newly created archive.
     */
    void generate_archive_directory(String path, String archive_name) throws Archive_Exception;

    /**
     * Uploads multiple archived directories to given path in storage.
     * @param files List of directories.
     * @param destination Path on the storage where we want to upload archived directories.
     * @param name Name of created archive.
     */
    void upload_multiple_archived_directories(List<File> files, String destination, String name) throws Upload_Multiple_Archives_Exception;

    /**
     * Downloads multiple archived directories to given path in storage.
     * @param files List of directories.
     * @param destination Path on the storage where we want to download archived directories.
     * @param name Name of created archive.
     */
    void download_multiple_archived_directories(List<File> files, String destination, String name) throws Download_Multiple_Archives_Exception;

    /**
     * Moves directory to given path.
     * @param source  Path of the directory on the storage.
     * @param destination Path where we want to move directory.
     */
    void move_directory(String source, String destination) throws Move_Exception;

    /**
     * Renames given directory.
     * @param new_name New name for the directory.
     * @param path Path of the directory we want to rename on the storage.
     */
    void rename_directory(String path, String new_name) throws Rename_Exception;

    /**
     * Lists files with given extensions in directory from given path.
     * @param path       Path of the directory on the storage.
     * @param extension_filter Array of file extensions (if null, it list files with all extensions).
     * @param recursive True if we want to list files from all sub-directories.
     */
    void list_files(String path, String[] extension_filter, boolean recursive) throws List_Files_Exception;

    /**
     * Lists directories in directory from given path.
     * @param path       Path of the directory on the storage.
     * @param recursive True if we want to list directories from all sub-directories.
     */
    void list_directories(String path, boolean recursive) throws List_Directories_Exception;

    /**
     * Adds file extensions to blacklist.
     * @param path       Path of the the storage.
     * @param extensions Array of file extensions we want to add to blacklist.
     */
    void create_extension_blacklist(String path, String[] extensions) throws Create_Extension_Blacklist_Exception;

    /**
     * Lists files in storage with given.
     * @param file_name Name of the file we are searching for.
     */
    void search_files(String file_name) throws Search_Files_Exception;

}
