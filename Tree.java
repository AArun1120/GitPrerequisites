import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Tree {
    File tree;
    public Tree(){
        tree = new File("tree");
    }
    public void add(String add) throws IOException{
        FileWriter f = new FileWriter("tree");
        f.write(add);
        f.close();
    }

}
