
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Index {
        private static final String INDEX_FILE = "index";

        public void init() {
            File indexFile = new File(INDEX_FILE);
            if (!indexFile.exists()) {
                try {
                    indexFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File objectDir = new File("./objects/");
            if (!objectDir.exists()) {
                objectDir.mkdir();
            }
        }

        public void addBlob(Blob blob, String originalFilename) {
            String entry = originalFilename + ":" + blob.getSHA1();
            appendToFile(INDEX_FILE, entry);
        }

        public void removeBlob(String originalFilename) {
            List<String> lines = readLinesFromFile(INDEX_FILE);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                if (!line.startsWith(originalFilename + ":")) {//removed space around colon for consistency
                    updatedLines.add(line);
                }
            }

            writeLinesToFile(INDEX_FILE, updatedLines);
        }

        public void appendToFile(String filename, String content) {
            try (FileWriter writer = new FileWriter(filename, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
                bufferedWriter.write(content);
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public List<String> readLinesFromFile(String filename) {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lines;
        }

        public void writeLinesToFile(String filename, List<String> lines) {
            try (FileWriter writer = new FileWriter(filename);
                    BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
                for (String line : lines) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }