package models;

import exceptions.*;
import interfaces.Directory_Manipulation_Interface;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Local_Directory implements Directory_Manipulation_Interface {

    private String storage_path = null;
    private String[] extension_blacklist;

    public Local_Directory() {

    }

    public void init_blacklist() throws Exception {
        File file = new File(storage_path + File.separator + "storage.meta");
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
                    extension_blacklist = new String[all.size()];
                    all.toArray(extension_blacklist);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void rewrite_extensions() {
        File file = new File(storage_path + File.separator + "storage.meta");
        if (file.exists()) {
            try {
                FileWriter fw = new FileWriter(file);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(storage_path);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < extension_blacklist.length; i++) {
                    if (i != extension_blacklist.length - 1) {
                        sb.append(extension_blacklist[i]);
                        sb.append(";");
                    } else {
                        sb.append(extension_blacklist[i]);
                    }
                }
                pw.println(sb);
                pw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void create_directory(String path, String directory_name) throws Create_Directory_Exception {
        if (!path.isEmpty() && path.startsWith(storage_path)) {
            File file = new File(path + File.separator + directory_name);
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("Directory \"" + directory_name + "\" successfully created at \"" + path + "\".");
                } else {
                    throw new Create_Directory_Exception();
                }
            } else {
                throw new Create_Directory_Exception();
            }
        } else {
            throw new Create_Directory_Exception();
        }
    }

    @Override
    public void create_multiple_directories(String path, String common_directories_name, int no_directories) throws Create_Multiple_Directories_Exception {
        if (!path.isEmpty() && path.startsWith(storage_path)) {
            for (int i = 1; i <= no_directories; i++) {
                File file = new File(path + File.separator + common_directories_name + i);
                if (!file.exists()) {
                    if (file.mkdir()) {
                        continue;
                    } else {
                        throw new Create_Multiple_Directories_Exception();
                    }
                } else {
                    throw new Create_Multiple_Directories_Exception();
                }
            }
            System.out.println("Directories \"" + common_directories_name + "{1..." + no_directories + "}\" successfully created at \"" + path + "\".");
        } else {
            throw new Create_Multiple_Directories_Exception();
        }
    }

    @Override
    public void delete_directory(String path) throws Delete_Exception {
        if (!path.isEmpty() && path.startsWith(storage_path)) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    FileUtils.deleteDirectory(file);
                    System.out.println("Directory \"" + file.getName() + "\" successfully deleted from \"" + path.substring(0, path.length() - file.getName().length()) + "\".");
                } catch (IOException e) {
                    throw new Delete_Exception();
                }
            } else {
                throw new Delete_Exception();
            }
        } else {
            throw new Delete_Exception();
        }
    }

    @Override
    public void download_directory(String source, String destination) throws Download_Exception {

        if (!destination.contains(source) && !source.isEmpty() && !destination.isEmpty() && destination.startsWith(storage_path)) {
            File source_directory = new File(source);
            if (source_directory.exists() && source_directory.isDirectory()) {
                File destination_directory = new File(destination + File.separator + source_directory.getName());
                if (!destination_directory.exists()) {
                    try {
                        FileUtils.copyDirectory(source_directory, destination_directory);
                        System.out.println("Directory \"" + source_directory.getName() + "\" successfully copied as \"" + destination_directory.getName() + "\" to \"" + destination + "\".");
                    } catch (IOException e) {
                        throw new Download_Exception();
                    }
                } else {
                    throw new Download_Exception();
                }
            } else {
                throw new Download_Exception();
            }
        } else {
            throw new Download_Exception();
        }

    }

    @Override
    public void upload_directory(String source, String destination) throws Upload_Exception {
        // In local, upload is same as download.
    }

    @Override
    public void upload_multiple_directories(List<File> files, String destination, String name) throws Upload_Multiple_Exception {
        // In local, upload is same as download.
    }

    @Override
    public void download_multiple_directories(List<File> directories, String destination, String name) throws Download_Multiple_Exception {

    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    @Override
    public void generate_archive_directory(String path, String archive_name) throws Archive_Exception {

        if (!path.isEmpty() && !archive_name.isEmpty()) {
            File file = new File(path);
            if (file.isDirectory() && file.exists()) {
                try {
                    if (!archive_name.endsWith(".zip")) archive_name += ".zip";

                    FileOutputStream fos = new FileOutputStream(path.substring(0, path.length() - file.getName().length()) + archive_name);
                    ZipOutputStream zipOut = new ZipOutputStream(fos);

                    zipFile(file, file.getName(), zipOut);

                    zipOut.close();
                    fos.close();

                    System.out.println("Directory \"" + file.getName() + "\" successfully archived as \"" + archive_name + "\" at \"" + path.substring(0, path.length() - file.getName().length()) + "\".");
                } catch (Exception e) {
                    throw new Archive_Exception();
                }
            } else {
                throw new Archive_Exception();
            }
        } else {
            throw new Archive_Exception();
        }

    }

    @Override
    public void upload_multiple_archived_directories(List<File> files, String destination, String name) throws Upload_Multiple_Archives_Exception {

    }

    @Override
    public void download_multiple_archived_directories(List<File> list, String s, String s1) throws Download_Multiple_Archives_Exception {

    }

    @Override
    public void move_directory(String source, String destination) throws Move_Exception {

        if (!destination.contains(source) && !source.isEmpty() && !destination.isEmpty() && destination.startsWith(storage_path)) {
            File source_directory = new File(source);
            if (source_directory.exists() && source_directory.isDirectory()) {
                File destination_directory = new File(destination + File.separator + source_directory.getName());
                if (!destination_directory.exists()) {
                    try {
                        FileUtils.moveDirectory(source_directory, destination_directory);
                        System.out.printf("Directory \"" + source_directory.getName() + "\" successfully moved to \"" + destination + "\".");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new Move_Exception();
                }

            } else {
                throw new Move_Exception();
            }
        } else {
            throw new Move_Exception();
        }

    }

    @Override
    public void rename_directory(String path, String new_name) throws Rename_Exception {

        File old_name_file = new File(path);
        if (old_name_file.exists() && old_name_file.isDirectory()) {
            path = path.substring(0, path.length() - old_name_file.getName().length());
            File new_name_file = new File(path + File.separator + new_name);
            if (!new_name_file.exists()) {
                if (old_name_file.renameTo(new_name_file)) {
                    System.out.println("Directory \"" + old_name_file.getName() + "\" successfully renamed to \"" + new_name + "\".");
                } else {
                    System.out.println("Failed to rename directory.");
                    throw new Rename_Exception();
                }
            } else {
                throw new Rename_Exception();
            }
        } else {
            throw new Rename_Exception();
        }

    }

    @Override
    public void list_files(String path, String[] extension_filter, boolean recursive) throws List_Files_Exception {
        if (!path.isEmpty() && path.startsWith(storage_path)) {
            File directory = new File(path);
            if (directory.exists() && directory.isDirectory()) {
                ArrayList<File> files = new ArrayList<>(FileUtils.listFiles(directory, extension_filter, recursive));
                Collections.sort(files);
                System.out.println("Files in \"" + path + "\" are:");
                for (File f : files) {
                    System.out.println(f.getName());
                }
            } else {
                throw new List_Files_Exception();
            }
        } else {
            throw new List_Files_Exception();
        }
    }

    @Override
    public void list_directories(String path, boolean recursive) throws List_Directories_Exception {
        if (!path.isEmpty() && path.startsWith(storage_path)) {
            File directory = new File(path + File.separator);
            if (directory.exists() && directory.isDirectory()) {
                ArrayList<File> directories = null;
                if (recursive) {
                    directories = new ArrayList<>(FileUtils.listFilesAndDirs(directory, new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY));
                    directories.remove(directory);
                    System.out.println("Directories in \"" + path + "\" are:");
                    for (File d : directories) {
                        System.out.println(d.getName());
                    }
                } else {
                    File[] dirs = directory.listFiles((java.io.FileFilter) FileFilterUtils.directoryFileFilter());
                    for (File d : dirs) {
                        System.out.println(d.getName());
                    }
                }
            } else {
                throw new List_Directories_Exception();
            }
        } else {
            throw new List_Directories_Exception();
        }
    }

    @Override
    public void create_extension_blacklist(String path, String[] new_extensions) throws Create_Extension_Blacklist_Exception {
        List<String> both = new ArrayList<>();
        if (extension_blacklist != null) Collections.addAll(both, extension_blacklist);
        Collections.addAll(both, new_extensions);
        extension_blacklist = new String[both.size()];
        both.toArray(extension_blacklist);
        System.out.println("Extenstions added.");
        rewrite_extensions();

    }

    private void search(File file, String file_name, List<String> results) {
        if (file.isDirectory()) {
            if (file.canRead()) {
                for (File temp : file.listFiles()) {
                    if (temp.isDirectory()) {
                        search(temp, file_name, results);
                    } else {
                        if ((temp.getName().toLowerCase()).equals(file_name)) {
                            results.add(temp.getAbsoluteFile().toString());
                        }

                    }
                }
            }
        }

    }

    @Override
    public void search_files(String file_name) throws Search_Files_Exception {
        File storage = new File(storage_path);
        List<String> results = new ArrayList<>();
        if (storage.exists() && storage.isDirectory()) {
            search(storage, file_name, results);
        } else {
            throw new Search_Files_Exception();
        }
        for (String s : results) {
            System.out.println(s);
        }
    }

    public String getStorage_path() {
        return storage_path;
    }

    public void setStorage_path(String storage_path) {
        this.storage_path = storage_path;
    }

    public String[] getExtension_blacklist() {
        return extension_blacklist;
    }

    public void setExtension_blacklist(String[] extension_blacklist) {
        this.extension_blacklist = extension_blacklist;
    }
}
