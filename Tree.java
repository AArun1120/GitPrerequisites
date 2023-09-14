import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Tree {
    ArrayList<String> trees = new ArrayList<String>();
    HashMap<String,String> blobs = new HashMap<String,String>();
    public Tree() throws RuntimeException{
        if(!Files.exists(Paths.get("./objects/"))){
            new File("./objects/").mkdirs();
        }
    }
    public void add(String add) throws IOException{
        String str = add.replaceAll("\\s", "");
        String pre = str.substring(0,str.indexOf(":"));
        if(pre.equals("tree")){
            String pos = str.substring(str.indexOf(":")+1);
            if(!trees.contains(pos)){
            trees.add(pos);
            }
        }   
        else if(pre.equals("blob")){
            String blub = str.substring(str.indexOf(":")+1);
            blobs.put(blub.substring(blub.indexOf(":")+1),blub.substring(0,blub.indexOf(":")));
        }
    }
    public void remove(String remove){
        if(blobs.containsKey(remove)){
            blobs.remove(remove, blobs.get(remove));
        }
        else if(trees.contains(remove)){
            trees.remove(trees.indexOf(remove));
        }
    }

    public void writeToFile() throws IOException{
        String str = "";
        for(String s : blobs.keySet()){
            str+="blob : "+blobs.get(s)+" : "+s+"\n";
        }
        for(String ss : trees){
            str+="tree : "+ss+"\n";
        }
        if (!str.isEmpty()){
            str = str.substring(0, str.length()-1);
        }
        String sha1 = Blob.sha1Hash(str);
        System.out.println(sha1);
        FileWriter fw = new FileWriter("./objects/"+sha1);
        fw.write(str);
        fw.close();
    }
}
