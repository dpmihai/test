package new1_7;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
 

public class TraverseFileTree {
 
    public static void main(String[] args) throws IOException {
        Path startPath = Paths.get("D:/Downloads");
 
        Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println(">>>>Dir : " + dir);
                return FileVisitResult.CONTINUE;
            }
 
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("File : " + file);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
