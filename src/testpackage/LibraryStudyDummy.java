package testpackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LibraryStudyDummy {
    static void main() {
        pBuilder("");
        }
    public static void fileCreate(String[] data, String filename){
        File file=new File(filename);
        try {
            if(file.createNewFile())
            {
                System.out.println(filename+" is created");
            }
            else System.out.println(filename+ " already exists");
            FileWriter fileWriter=new FileWriter(file,true);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            int x=0;
            while (x<data.length){
                bufferedWriter.append(data[x]).append("\n");
                bufferedWriter.flush();
                x++;
            }
            bufferedWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static  void pBuilder(String command){
        ProcessBuilder processBuilder=new ProcessBuilder("cmd.exe","/c",command);

        try {
            Process p= processBuilder.start();
            int code=p.waitFor();
            if(code==0) System.out.println("successfully ran command");
            System.out.println(p.pid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
