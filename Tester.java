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

public class Tester {
    
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
    @DisplayName("Magicks a blob from nowhere")
    public void testBlob() throws Exception{
        Blob blub = new Blob("asdf.txt");
        blub.writeToDisk();
        File file2 = new File("./objects/"+blub.getSHA1());
        assertTrue(file2.exists());//tests if the blob exists
        BufferedReader br = new BufferedReader(new FileReader(file2));
        String str = "";
        while(br.ready()){str+=br.read();}
        br.close();
        BufferedReader br2 = new BufferedReader(new FileReader("asdf.txt"));
        String str2 = "";
        while(br2.ready()){str+=br2.read();}
        br2.close();
        assertEquals(str,str2);//tests if the contents of the blob are equal to the contents of the file
    }

    @Test
    @DisplayName("initializes the index file and objects folder")
    public void testInit(){
        Index index = new Index();
        index.init();
        File file3 = new File("index");
        assertTrue(file3.exists());//tests if index exists
        Path path = Paths.get("./objects/");
        assertTrue(Files.exists(path));// tests if objects exists
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
        assertEquals(bob.toString(),idk.toString());//bob is the entry we put in and idk is the contents of the file. Since we only put in ghjk, they should be equal
    }

    @Test
    @DisplayName("gets sha for blob")
    public void testSha() throws RuntimeException{
        Blob b = new Blob("asdf.txt");
        assertEquals("3da541559918a808c2402bba5012f6c60b27661c",b.getSHA1());// the first string is the sha1 code for asdf according to 4 different websites
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
        String str2 = "asdf:3da541559918a808c2402bba5012f6c60b27661c";
        assertEquals(str,str2);//tests if the index contents equal the entry string
        
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
        assertEquals(checkString,indexString);// tests if the index removed the last file
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
        assertEquals(str,read);//the index should be empty, so we test for that
    }

    @Test
    @DisplayName("tests tree methods")
    public void testTree() throws Exception{
        Tree tree = new Tree();
        String str = "blob : "+Blob.sha1Hash("asdf")+" : asdf.txt";
        tree.add(str);
        tree.add("blob : "+Blob.sha1Hash("ghjk")+" : ghjk.txt");
        tree.remove("ghjk.txt");
        tree.writeToFile();
        assertTrue(Files.exists(Paths.get("./objects/"+Blob.sha1Hash(str))));//tests if the file exists
        BufferedReader br = new BufferedReader(new FileReader("./objects/"+Blob.sha1Hash(str)));
        String str2 = br.readLine();
        br.close();
        assertEquals(str,str2);//tests if the tree can add and remove files
    }

}
