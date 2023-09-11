import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.util.*;
import javax.sql.rowset.serial.SerialBlob;

public class BlobIndex {

    public static String sha1Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Blob {
        private String sha1;
        private String filePath;

        public Blob(String filePath) {
            this.filePath = filePath;
            this.sha1 = sha1Hash(readFileContent(filePath));
        }

        public String getSHA1() {
            return sha1;
        }

        public void writeToDisk() {
            String objectFolder = "./objects/";
            File objectDir = new File(objectFolder);

            if (!objectDir.exists()) {
                objectDir.mkdir();
            }

            String objectFileName = objectFolder + sha1;
            try (FileOutputStream outputStream = new FileOutputStream(objectFileName)) {
                byte[] fileData = readFileBytes(filePath);
                outputStream.write(fileData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String readFileContent(String filePath) {
            try {
                StringBuilder content = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                return content.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }

        public byte[] readFileBytes(String filePath) {
            try {
                FileInputStream inputStream = new FileInputStream(filePath);
                byte[] data = new byte[(int) new File(filePath).length()];
                inputStream.read(data);
                inputStream.close();
                return data;
            } catch (IOException e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
    }

    public static class Index {
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
            String entry = originalFilename + " : " + blob.getSHA1();
            appendToFile(INDEX_FILE, entry);
        }

        public void removeBlob(String originalFilename) {
            List<String> lines = readLinesFromFile(INDEX_FILE);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                if (!line.startsWith(originalFilename + " : ")) {
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
}
