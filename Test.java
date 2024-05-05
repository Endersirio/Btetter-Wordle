import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;

import java.util.Collections;
import java.util.ArrayList;

public class Test {
   public static void main(String[] args) throws IOException {
   
      //select and sort words form another file
      Scanner reader = new Scanner(new File("words2.txt"));
      FileWriter writer = new FileWriter(new File("words.txt"));
      
      ArrayList<String> list = new ArrayList<String>();
               
      String current;
      while (reader.hasNextLine()) {
         current = reader.nextLine().toUpperCase();
         if (current.length() > 2 && current.length() < 8 && current.indexOf("'") == -1) {
            if (!list.contains(current)) {
               list.add(current);
            }
         }
      }
      
      Collections.sort(list);
      
      for (String s: list) {
         writer.write(s + "\n");
      }
      
      writer.close(); 
      reader.close();
   }
}