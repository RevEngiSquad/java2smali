package java2smali;

import com.android.tools.r8.CompilationFailedException;
import com.android.tools.r8.D8;
import com.android.tools.r8.D8Command;
import com.android.tools.r8.OutputMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Class2Dex {
    public static void dexClassFile(String[] inputClassFilePaths, String outputDirPath)
            throws IOException, CompilationFailedException {
        Path outputDir = Paths.get(outputDirPath);
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }
        Path[] inputPaths = new Path[inputClassFilePaths.length];
        for (int i = 0; i < inputClassFilePaths.length; i++) {
            inputPaths[i] = Paths.get(inputClassFilePaths[i]);
            if (!inputPaths[i].toFile().exists()) {
                throw new IOException("Input class file does not exist: " + inputClassFilePaths[i]);
            }
        }
        D8Command.Builder builder = D8Command.builder()
                .addProgramFiles(inputPaths) // You can add more files as needed
                .setOutput(outputDir, OutputMode.DexIndexed)
                .setMinApiLevel(21); // Set the minimum API level as required

        D8.run(builder.build());
    }
}
