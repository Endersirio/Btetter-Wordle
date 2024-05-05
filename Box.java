import java.awt.*;
import javax.swing.*;

public class Box extends JFrame {

  String word;
  char letter;
  Line line;

  Frame frame;
  JLabel label;
  JPanel panel;

  int initial;
  int space;
  int side;

  int xPos, yPos, col, row, l;
  boolean first = true;

  Color color;
  int score;

  public Box(
    Frame frame,
    Line line,
    String word,
    int col,
    int row,
    int initial,
    int side,
    int space
  ) {
    xPos = initial + (side + space) * col;
    yPos = initial + (side + space) * row;
    l = side;
    color = Color.WHITE;
    this.col = col;
    this.word = word;
    this.side = side;
    this.line = line;
    this.space = space;
    this.frame = frame;
    this.initial = initial;

    //set up label that will contain the letter
    label = new JLabel();
    label.setBounds(xPos + 10, yPos - 10, 10, 10);

    //set up colored panel
    panel = new JPanel();
    panel.setBackground(color);
    panel.setBounds(xPos, yPos, side, side);
    panel.setLayout(new GridBagLayout());
    panel.setBorder(BorderFactory.createLineBorder(Color.black));

    frame.add(panel);
  }

  //move box to next animation phase
  public void flip(int time, int score) {
    //change color
    if (time == 5) {
      if (score == 1) {
        color = Color.GREEN;
      } else if (score == 2) {
        color = Color.YELLOW;
      } else {
        color = Color.GRAY;
      }
      panel.add(label);
      first = false;
    }
    //animate
    else if (time < 5) {
      yPos += side / 10;
      l -= side / 5;
    } else {
      yPos -= side / 10;
      l += side / 5;
    }
    panel.setBackground(color);
    panel.setBounds(xPos, yPos, side, l);
  }

  public void setInput(String input) {
    letter = input.charAt(col);
    label.setText("" + letter);
  }
}
