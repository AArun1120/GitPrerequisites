import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Blob {
    private String sha1;
    private String filePath;

    public Blob(String filePath) {
        this.filePath = filePath;
        this.sha1 = sha1Hash(readFileContent(filePath));
    }

    public String getSHA1() {
        return sha1;
    }

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
