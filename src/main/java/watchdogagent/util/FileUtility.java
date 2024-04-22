package watchdogagent.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.tools.ant.DirectoryScanner;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ran Zhang
 * @since 2024/4/13
 */
public class FileUtility {

    /**
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public static void extractJar(String zipFilePath, String destDirectory) throws IOException {
        java.util.jar.JarFile jarfile = new java.util.jar.JarFile(new File(zipFilePath));
        java.util.Enumeration<java.util.jar.JarEntry> enu = jarfile.entries();
        while (enu.hasMoreElements()) {
            java.util.jar.JarEntry je = enu.nextElement();
            File fl = new File(destDirectory, je.getName());
            if (!fl.exists()) {
                fl.getParentFile().mkdirs();
                fl = new File(destDirectory, je.getName());
            }
            if (je.isDirectory()) {
                continue;
            }
            java.io.InputStream is = jarfile.getInputStream(je);
            java.io.FileOutputStream fo = new java.io.FileOutputStream(fl);
            while (is.available() > 0) {
                fo.write(is.read());
            }
            fo.close();
            is.close();
        }

    }

    public static String createTempdir() {
        return Files.createTempDir().getAbsolutePath();
    }


    public static List<String> filterFile(String baseDir, String[] include, String[] exclude) {
        DirectoryScanner scanner = new DirectoryScanner();
        //new String[]{"**/*.java"}
        scanner.setIncludes(include);
        scanner.setExcludes(exclude);
        scanner.setBasedir(baseDir);
        scanner.setCaseSensitive(false);
        scanner.scan();
        return Arrays.asList(scanner.getIncludedFiles());
    }


    public static void zip(String targetPath, String destinationFilePath, String password) {
        try {
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            if (password != null && password.length() > 0) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                parameters.setPassword(password);
            }

            ZipFile zipFile = new ZipFile(destinationFilePath);

            File targetFile = new File(targetPath);
            if (targetFile.isFile()) {
                zipFile.addFile(targetFile, parameters);
            } else if (targetFile.isDirectory()) {
                zipFile.addFolder(targetFile, parameters);
            } else {
                //neither file nor directory
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unzip(String targetZipFilePath, String destinationFolderPath, String password) {
        try {
            ZipFile zipFile = new ZipFile(targetZipFilePath);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(destinationFolderPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String objectKey(String md5, String fileName) {
        return "codedb" + "/" + md5 + "/" + fileName;
    }

    public static String md5(String filePath) throws IOException {
        File file = new File(filePath);
        HashCode hashCode = Files.asByteSource(file).hash(Hashing.md5());
        return hashCode.toString();
    }

    public static long size(String path) throws IOException {
        Path imageFilePath = Paths.get(path);
        FileChannel fileChannel = FileChannel.open(imageFilePath);
        return fileChannel.size();
    }
}
