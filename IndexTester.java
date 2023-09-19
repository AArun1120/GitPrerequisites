import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class IndexTester {
    
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
        Files.deleteIfExists(Paths.get("index"));
        Files.deleteIfExists(Paths.get("./objects/"));

    }
    
    @Test
    @DisplayName("initializes the index file and objects folder")
    public void testInit(){
        Index index = new Index();
        index.init();
        File file3 = new File("index");
        // tests if index file has been created
        assertTrue(file3.exists());
        Path path = Paths.get("./objects/");
        // tests if ''objects'' folder has been created
        assertTrue(Files.exists(path));
    }

    @Test
    @DisplayName("adds a blob")
    public void testAddBlob() throws IOException{
        StringBuilder bob = new StringBuilder();
        Index indy = new Index();
        BufferedReader br3 = new BufferedReader(new FileReader("index"));
        while(br3.ready()){
            bob.append(br3.readLine()+"\n");
        }

        br3.close();
        Blob b = new Blob("ghjk.txt");
        bob.append("ghjk.txt:"+b.getSHA1()+"\n");
        indy.addBlob(new Blob("ghjk.txt"), "ghjk.txt");
        StringBuilder idk = new StringBuilder();
        BufferedReader br4 = new BufferedReader(new FileReader("index"));
        while(br4.ready()){
            idk.append(br4.readLine()+"\n");
        }
        br4.close();
        // tests if the index added the blob
        assertEquals(bob.toString(),idk.toString());
    }

    @Test
    @DisplayName("adds a file")
    public void testAddFile() throws IOException{
        Files.deleteIfExists(Paths.get("index"));
        Index ind = new Index();
        ind.addBlob(new Blob("asdf.txt"),"asdf.txt");
        BufferedReader br = new BufferedReader(new FileReader("index"));
        String str = "";
        while(br.ready()){
            str+=br.readLine();
        }
        br.close();
        String str2 = "asdf.txt:3da541559918a808c2402bba5012f6c60b27661c";
        // tests if the index added the file
        assertEquals(str,str2);
        
    }

    @Test
    @DisplayName("tests remove")
    public void testRemove() throws IOException{
        Files.deleteIfExists(Paths.get("index"));
        Index ind = new Index();
        ind.addBlob(new Blob("asdf.txt"),"asdf.txt");
        ind.addBlob(new Blob("ghjk.txt"), "ghjk.txt");
        BufferedReader br4 = new BufferedReader(new FileReader("index"));
        ArrayList<String> list = new ArrayList<String>();
        while(br4.ready()){
            list.add(br4.readLine());
        }
        br4.close();
        list.remove(list.size()-1);
        String checkString = "";
        for(String str : list){
            checkString+=str;
        }
        ind.removeBlob("ghjk.txt");
        String indexString = "";
        BufferedReader br5 = new BufferedReader(new FileReader("index"));
        while(br5.ready()){
            indexString+=br5.readLine();
        }
        br5.close();
        // tests if the index removed the last file
        assertEquals(checkString,indexString);
    }

    @Test
    @DisplayName("Good luck")
    public void testChaos() throws IOException{
        Files.deleteIfExists(Paths.get("index"));
        Index i = new Index();
        i.addBlob(new Blob("1.txt"),"1.txt");
        i.addBlob(new Blob("2.txt"), "2.txt");
        i.removeBlob("1.txt");
        i.removeBlob("3.py");
        i.removeBlob("2.txt");
        String str = "";
        BufferedReader br = new BufferedReader(new FileReader("index"));
        String read = "";
        while(br.ready()){
            read+=br.read();
        }
        br.close();
        //the index should be empty, so we test for that
        assertEquals(str,read);
    }

}
