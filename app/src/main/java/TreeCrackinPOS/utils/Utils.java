package TreeCrackinPOS.utils;

import java.io.*;

public class Utils {
    public static void writeStringToFile(String path, String contents) throws IOException {
        File file = new File(path);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);

        writer.append(contents);

        writer.flush();
        writer.close();
    }

    public static String readFileToString(String path) throws IOException {
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        reader.close();

        return sb.toString();
    }

    public static void printRuler() {
        for (int i = 0; i < 8; i++) {
            int x = (8 - i) * 8;
            System.out.printf("%d", x);
            if (i != 7) {
                for (int j = 0; j < 8 - Integer.toString(x).length(); j++) {
                    System.out.print(' ');
                }
            }
        }
        for (int j = 0; j < 6; j++) {
            System.out.print(' ');
        }
        System.out.print('1');
        System.out.println(" < bit ruler");
        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0 || i == 63) {
                System.out.print('v');
            } else {
                System.out.print(' ');
            }
        }
        System.out.print('\n');
    }
}
