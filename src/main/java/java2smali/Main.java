package java2smali;

import org.apache.commons.io.FileUtils;

import com.android.tools.r8.CompilationFailedException;

import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, CompilationFailedException {
        String javaPath = args[0];
        var compiler = ToolProvider.getSystemJavaCompiler();
        String classpath = System.getProperty("java.class.path");
        String appClassPath = System.getenv("APP_CLASSPATH");
        if (appClassPath != null) {
            classpath = classpath + ":" + appClassPath;
        }
        List<String> optionList = Arrays.asList("-classpath", classpath);
        try {
            var fileManager = compiler.getStandardFileManager(null, null, null);
            var fileObjects = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(javaPath));
            var task = compiler.getTask(null, null, null, optionList, null, fileObjects);
            Boolean result = task.call();
            if (result == null || !result) {
                throw new RuntimeException("Compilation failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        File javaFile = new File(javaPath);
        File javaDir = javaFile.getParentFile();
        String javaNameExt = javaFile.getName();
        String javaName = javaNameExt.substring(0, javaNameExt.lastIndexOf('.'));
        String[] classFiles = javaDir.list((dir, name) -> name.startsWith(javaName) && name.endsWith(".class"));

        List<String> inputClassFiles = new ArrayList<>();
        for (String classFile : classFiles) {
            inputClassFiles.add(javaDir.getAbsolutePath() + "/" + classFile);
        }

        Path tempDir = Files.createTempDirectory("java2smali-");
        Class2Dex.dexClassFile(inputClassFiles.toArray(new String[0]), tempDir.toString());

        for (String classFile : classFiles) {
            String classFilePath = javaDir + "/" + classFile;
            String dexFilePath = tempDir.toString() + "/" + "classes.dex";
            Dex2Smali.disassembleDexFile(dexFilePath, tempDir.toString());
            List<Path> list = Files.walk(tempDir).filter(Files::isRegularFile).collect(Collectors.toList());
            FileUtils.deleteQuietly(new File(classFilePath));
            String smaliFileName = classFile.replace(".class", ".smali");
            Path smaliFilePath = list.stream().filter(path -> path.getFileName().toString().equals(smaliFileName))
                    .findFirst().orElse(null);
            if (smaliFilePath != null) {
                FileUtils.copyFile(new File(smaliFilePath.toString()),
                        new File(classFilePath.replace(".class", ".smali")));
            }
        }

        FileUtils.deleteDirectory(tempDir.toFile());
    }
}
