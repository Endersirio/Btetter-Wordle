import java.awt.event.*;
import javax.swing.*;

public class Line implements ActionListener {

   boolean[] used;
   int[] score;
   Box[] line;
   
   String input;
   String word;
   
   int currentIndex;
   Frame frame;
   Timer timer;
   int time;
   
   int initial;
   int space;
   int side;
   int row;
   int col;
   
   public Line(Frame frame, String word, int row, int initial, int side, int space) {
      col = word.length();
      
      used = new boolean[col];
      score = new int[col];
      line = new Box[col];
      
      this.initial = initial;
      this.frame = frame;
      this.space = space;
      this.word = word;
      this.side = side;
      this.row = row;
      
      //create row of boxes
      for (int i = 0; i < col; i++) {
         line[i] = new Box(frame, Line.this, word, i, row, initial, side, space);
      }
   }
   
    //begin flipping animation
   public void flipRow(String input) {
      this.input = input;
      line[0].setInput(input);
      timer = new Timer(50, this);
      timer.start();
      check();
   }
    
    //check presence multiple letters
   public void check() {
   
      //primary check
      for (int i = 0; i < col; i++) {
         if (input.charAt(i) == word.charAt(i)) {
            used[i] = true;
            score[i] = 1;
         }
      }
      
      //secondary check
      for (int i = 0; i < col; i++) {
         if (score[i] != 1) {
            int j = 0;
            while (true) {
               j = word.indexOf(input.charAt(i), j);
               if (j < 0) {
                  break;
               }
               if (!used[j]) {
                  used[j] = true;
                  score[i] = 2;                  
                  break;
               }
               j++;
            }
         }
      }
   }
   
    //return if all letters are correct
   public boolean correct() {
      for (int i: score) {
         if (i != 1) {
            return false;
         }
      }
      return true;
   }
   
   public void actionPerformed(ActionEvent e) {
   
      //timer action
      if (e.getSource() == timer) {
         time++;
         line[currentIndex].flip(time, score[currentIndex]);
         frame.revalidate();
         if (time >= 9) {
            time = 0;
            currentIndex++;
            if (currentIndex >= col) {
               timer.stop();
               currentIndex = 0;
               frame.nextRow(correct());
            } else {
               line[currentIndex].setInput(input);
            }
         }
      }
   }
}