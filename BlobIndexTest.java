import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class BlobIndexTest {
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        File file = new File("asdf.txt");
        FileWriter fw = new FileWriter(file);
        fw.write("asdf");
        fw.close();
        File file2 = new File("ghk.txt");
        FileWriter fw2 = new FileWriter(file2);
        fw2.write("ghjk");
        fw2.close();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        /*
         * Utils.deleteFile("junit_example_file_data.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }
    
    @Test
    @DisplayName("Magicks a blob from nowhere")
    public void testBlub() throws Exception{
        BlobIndex.Blob blub = new BlobIndex.Blob("asdf.txt");
        blub.writeToDisk();
        File file2 = new File("./objects/"+blub.getSHA1());
        assertTrue(file2.exists());
        BufferedReader br = new BufferedReader(new FileReader(file2));
        String str = "";
        while(br.ready()){str+=br.read();}
        br.close();
        BufferedReader br2 = new BufferedReader(new FileReader("asdf.txt"));
        String str2 = "";
        while(br2.ready()){str+=br2.read();}
        br2.close();
        assertEquals(str,str2);
    }

    @Test
    @DisplayName("initializes the index")
    public void testInit(){
        BlobIndex.Index index = new BlobIndex.Index();
        index.init();
        File file3 = new File("index");
        assertTrue(file3.exists());
        Path path = Paths.get("./objects/");
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("adds a blob")
    public void testAdd() throws IOException{
        StringBuilder bob = new StringBuilder();
        BlobIndex.Index indy = new BlobIndex.Index();
        BufferedReader br3 = new BufferedReader(new FileReader("index"));
        while(br3.ready()){
              bob.append(br3.readLine()+"\n");
        }
        br3.close();
        BlobIndex.Blob b = new BlobIndex.Blob("ghjk.txt");
        bob.append("ghjk.txt : "+b.getSHA1()+"\n");
        indy.addBlob(new BlobIndex.Blob("ghjk.txt"), "ghjk.txt");
        StringBuilder idk = new StringBuilder();
        BufferedReader br4 = new BufferedReader(new FileReader("index"));
        while(br4.ready()){
              idk.append(br4.readLine()+"\n");
        }
        br4.close();
        assertEquals(bob.toString(),idk.toString());
    }


}
