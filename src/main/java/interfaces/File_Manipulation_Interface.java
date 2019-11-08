package interfaces;

import exceptions.*;
import jdk.tools.jlink.internal.Archive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface File_Manipulation_Interface {

    /**
     * Creates new file on given path.
     * @param path Path of the file on the storage.
     * @param file_name Name of the file.
     */
    void create_file(String path, String file_name) throws Create_File_Exception, Invalid_Path_Exception;

    /**
     * Creates multiple new files on given path.
     * @param path Path of the file on the storage.
     * @param common_files_name Common name of all files.
     * @param no_files Number of wanted files.
     */
    void create_multiple_files(String path, String common_files_name, int no_files) throws Create_Multiple_Files_Exception;

    /**
     * Deletes file with given path.
     * @param path Path of the file on the storage.
     */
    void delete_file(String path) throws Delete_Exception;

    /**
     * Downloads file (with given path - source) to given path (destination).
     * @param source  Path of the file on the storage.
     * @param destination Path on the storage where we want to download file.
     */
    void download_file(String source, String destination) throws Download_Exception;

    /**
     * Downloads multiple files (list of files) to given path (destination).
     * @param files  Files for downloading.
     * @param destination Path on the storage where we want to download file.
     */
    void download_multiple_files(List<File> files, String source, String destination) throws Download_Multiple_Exception;

    /**
     * Uploads file (with given path - source) to given path (destination).
     * @param source  Path of the file on the storage.
     * @param destination Path on the storage where we want to upload file.
     */
    void upload_file(String source, String destination) throws Upload_Exception;

    /**
     * Uploads multiple files (list of files) to given path (destination).
     * @param files  Files for uploading.
     * @param destination Path on the storage where we want to download file.
     */
    void upload_multiple_files(List<File> files, String destination, String name) throws Upload_Multiple_Exception;

    /**
     * Generates archive file (zip) from given file (by using it's path).
     * @param path Path of the file on the storage.
     * @param archive_name Name for the newly created archive.
     */
    void generate_archive_file(String path, String archive_name) throws Archive_Exception;

    /**
     * Generates archive file (zip) from given files.
     * @param files Files for archiving.
     * @param archive_name Name for the newly created archive.
     * @param destination Path on the storage where we want to archive files.
     */
    void generate_archive_from_multiple_files(List<File> files, String archive_name, String destination) throws Generate_Archive_From_Multiple_Files_Exception;

    /**
     * Uploads archive files to given path.
     * @param files Files for archiving.
     * @param destination Path on the storage where we want to upload our archived files.
     */
    void upload_multiple_archived_files(List<File> files, String destination, String name) throws Upload_Multiple_Archives_Exception;

    /**
     * Moves file to given path.
     * @param source  Path of the file on the storage.
     * @param destination Path on the storage where we want to move file.
     */
    void move_file(String source, String destination) throws Move_Exception;

    /**
     * Renames given file.
     * @param new_name New name for the file.
     * @param path Path of the file we want to rename on the storage.
     */
    void rename_file(String path, String new_name) throws Rename_Exception, Invalid_Path_Exception;

}
