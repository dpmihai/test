package new1_7;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
 

public class CopyFileDemo {
 
    public static void main(String[] args) throws IOException {
        // File to be copied
        Path srcFile = Paths.get("C:/srccodes/srcFile.txt");
         
        // Copied file
        Path targetFile = Paths.get("C:/srccodes/destFile.txt");
         
        // configure how to copy or move a file.
        CopyOption[] options = new CopyOption[] {StandardCopyOption.REPLACE_EXISTING};
         
        // Copy srcFile to targetFile
        Files.copy(srcFile, targetFile, options);
    }
 
}
