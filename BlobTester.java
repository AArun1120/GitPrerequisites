import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
public class BlobTester {

        
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        File file = new File("asdf.txt");
        FileWriter fw = new FileWriter(file);
        fw.write("asdf");
        fw.close();
        File file2 = new File("ghjk.txt");
        FileWriter fw2 = new FileWriter(file2);
        fw2.write("ghjk");
        fw2.close();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        Files.deleteIfExists(Paths.get("asdf.txt"));
        Files.deleteIfExists(Paths.get("ghjk.txt"));
        Files.deleteIfExists(Paths.get("./objects/"));

    }

    @Test
    @DisplayName("tests a blob")
    public void testBlob() throws Exception{
        Blob blub = new Blob("asdf.txt");
        blub.writeToDisk();
        File file2 = new File("./objects/"+blub.getSHA1());
        // tests if the blob exists
        assertTrue(file2.exists());
        String str = blub.readFileContent("./objects/"+blub.getSHA1());
        String str2 = "asdf";
        // tests if the blob file content matches the test file content
        assertEquals(str,str2);
    }
    
    @Test
    @DisplayName("gets sha for blob")
    public void testSha() throws Exception{
        Blob b = new Blob("asdf.txt");
        // tests if the blob file is encoded correctly
        assertEquals("3da541559918a808c2402bba5012f6c60b27661c",b.getSHA1());
    }

}
