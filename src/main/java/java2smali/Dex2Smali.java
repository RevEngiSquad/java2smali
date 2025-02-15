package java2smali;

import com.android.tools.smali.baksmali.BaksmaliOptions;
import com.android.tools.smali.baksmali.Baksmali;

import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.iface.DexFile;
import com.android.tools.smali.dexlib2.DexFileFactory;

import java.io.File;
import java.io.IOException;

public class Dex2Smali {
    public static void disassembleDexFile(String dexFilePath, String outputDir) throws IOException {
        Opcodes opCodes = Opcodes.getDefault();
        Object dexBackedDexFile = DexFileFactory.loadDexFile(dexFilePath, opCodes);

        BaksmaliOptions options = new BaksmaliOptions();
        options.apiLevel = opCodes.api;
        Baksmali.disassembleDexFile((DexFile) dexBackedDexFile, new File(outputDir), 6, options);
    }
}
