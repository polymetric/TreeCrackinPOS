package TreeCrackinPOS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
    public static void writeStringToFile(String path, String contents) throws IOException {
        File file = new File(path);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);

        writer.append(contents);

        writer.flush();
        writer.close();
    }
}
