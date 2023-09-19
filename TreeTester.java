import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class TreeTester {
        
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
    @DisplayName("tests tree add")
    public void testAdd() throws Exception{
        Tree tree = new Tree();
        String str = "blob : "+Blob.sha1Hash("asdf")+" : asdf.txt";
        tree.add(str);
        tree.writeToFile();
        // tests if the tree file exists
        assertTrue(Files.exists(Paths.get("./objects/"+Blob.sha1Hash(str))));
        BufferedReader br = new BufferedReader(new FileReader("./objects/"+Blob.sha1Hash(str)));
        String str2 = br.readLine();
        br.close();
        // tests if the tree added the file
        assertEquals(str,str2);
    }

    @Test
    @DisplayName("tests tree remove")
    public void testRemove() throws IOException{
        Tree tree = new Tree();
        String str = "blob : "+Blob.sha1Hash("ghjk")+" : ghjk.txt";
        tree.add(str);
        tree.remove("ghjk.txt");
        tree.writeToFile();
        BufferedReader br = new BufferedReader(new FileReader("./objects/"+Blob.sha1Hash(str)));
        String str2 = br.readLine();
        br.close();
        // tests if the tree removed the file
        assertEquals(null,str2);
    }
}
