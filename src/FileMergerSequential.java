import java.io.*;
import java.util.*;

public class FileMergerSequential {

    public static long mergeSequential(List<File> files, String outputDir) throws IOException {
        List<File> fileList = new ArrayList<>(files);
        long totalCost = 0;
        int step = 1;

        while (fileList.size() > 1) {
            File f1 = fileList.get(0);
            File f2 = fileList.get(1);
            long cost = f1.length() + f2.length(); // custo = soma dos tamanhos
            totalCost += cost;

            File merged = new File(outputDir, "merged_seq_" + step++ + ".txt");
            mergeFiles(f1, f2, merged);

            fileList.remove(0);
            fileList.remove(0);
            fileList.add(0, merged);
        }

        return totalCost;
    }

    private static void mergeFiles(File f1, File f2, File merged) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(merged));
             BufferedReader r1 = new BufferedReader(new FileReader(f1));
             BufferedReader r2 = new BufferedReader(new FileReader(f2))) {

            String line;
            while ((line = r1.readLine()) != null) writer.write(line + "\n");
            while ((line = r2.readLine()) != null) writer.write(line + "\n");
        }
    }
}
