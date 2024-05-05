import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;

public class Frame extends JFrame implements ActionListener {

  JTextField txtField;
  JButton replay;
  JButton quit;

  Scanner reader;
  Line[] grid;

  int initial = 30; //set initial poition
  int space = 20; //set side between boxes
  int side = 50; //set side length

  int fileLength;
  int currentRow;
  String input;
  int columns;
  String word;
  int lines;

  public Frame() {
    super("Wordle"); //create JFrame
    //count file's lines
    createScanner();
    while (reader.hasNextLine()) {
      reader.nextLine();
      fileLength++;
    }

    //set up standard frame
    this.setLayout(null);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(new Color(150, 150, 150));

    setup(); //set up everything else

    this.setVisible(true);
  }

  public void setup() {
    getWord();
    System.out.println(word);
    columns = word.length();
    lines = 5 + columns / 3;

    grid = new Line[lines];

    currentRow = 0;

    //scale frame size depending on word lenght and attempts
    this.setSize(
        initial * 2 + (side + space) * columns,
        initial + 60 + (side + space) * lines
      );

    //create textField
    txtField = new JTextField();
    txtField.addActionListener(this);
    txtField.setHorizontalAlignment(JTextField.CENTER);
    txtField.setBounds(
      this.getSize().width / 6,
      this.getSize().height - 70,
      this.getSize().width * 2 / 3 - 80,
      20
    );
    txtField.addFocusListener(
      new FocusListener() {
        //when focus is gained placeholder text is cleared
        public void focusGained(FocusEvent e) {
          txtField.setText(null);
          txtField.setForeground(Color.BLACK);
          txtField.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 12));
        }

        //when focus is lost placeholder text is created
        public void focusLost(FocusEvent e) {
          txtField.setForeground(Color.GRAY);
          txtField.setText("Invalid word");
          txtField.setFont(new Font(Font.DIALOG, Font.ITALIC, 11));
        }
      }
    );

    //create replay button
    createReplayButton();
    replay.setFont(new Font("Serif", Font.PLAIN, 17));
    replay.setBounds(
      this.getSize().width * 5 / 6 - 70,
      this.getSize().height - 70,
      50,
      20
    );

    //initialize lines
    for (int row = 0; row < lines; row++) {
      grid[row] = new Line(Frame.this, word, row, initial, side, space);
    }

    this.add(replay);
    this.add(txtField);
    revalidate();
    repaint();
  }

  //create end game screen
  public void end(boolean won) {
    remove();

    //create end text
    JLabel endLabel = new JLabel();
    endLabel.setFont(new Font("Serif", Font.PLAIN, 24));
    if (won) {
      endLabel.setText("YOU WON");
    } else {
      endLabel.setText("YOU LOST");
    }

    //create panel for end text
    JPanel endPanel = new JPanel();
    if (won) {
      endPanel.setBackground(Color.GREEN);
    } else {
      endPanel.setBackground(Color.RED);
    }
    endPanel.setLayout(new GridBagLayout());
    endPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    endPanel.setBounds(
      (this.getSize().width - 210) / 2,
      this.getSize().height / 5,
      210,
      50
    );
    endPanel.add(endLabel);

    //create replay button
    createReplayButton();
    replay.setFont(new Font("Serif", Font.PLAIN, 19));
    replay.setBounds(
      (this.getSize().width - 175) / 2 + 125,
      this.getSize().height * 3 / 5,
      50,
      50
    );

    //create quit button
    quit = new JButton("\u2716");
    quit.addActionListener(this);
    quit.setVerticalAlignment(JButton.CENTER);
    quit.setHorizontalAlignment(JButton.CENTER);
    quit.setFont(new Font("Serif", Font.PLAIN, 20));
    quit.setBounds(
      (this.getSize().width - 175) / 2,
      this.getSize().height * 3 / 5,
      50,
      50
    );

    JPanel attemptPanel = new JPanel();
    attemptPanel.setLayout(new GridBagLayout());
    attemptPanel.setBackground(new Color(150, 150, 150));
    JLabel attemptLabel = new JLabel();

    if (won) {
      //display number of attempts
      attemptPanel.setBounds(
        (this.getSize().width - 150) / 2,
        this.getSize().height * 2 / 5,
        150,
        30
      );
      if (currentRow == 0) {
        attemptLabel.setText("In " + 1 + " attempt.");
      } else {
        attemptLabel.setText("In " + (currentRow + 1) + " attempts.");
      }
    } else {
      //display correct word
      attemptPanel.setBounds(
        (this.getSize().width - 200) / 2,
        this.getSize().height * 2 / 5,
        200,
        30
      );
      attemptLabel.setText("The correct word was: " + word);
    }

    attemptLabel.setFont(new Font("Serif", Font.ITALIC, 16));
    attemptPanel.add(attemptLabel);
    this.add(attemptPanel);

    this.add(quit);
    this.add(replay);
    this.add(endPanel);
    revalidate();
    repaint();
  }

  public void createReplayButton() {
    replay = new JButton("\u27F3");
    replay.setFocusPainted(false);
    replay.addActionListener(this);
    replay.setVerticalAlignment(JButton.CENTER);
    replay.setHorizontalAlignment(JButton.CENTER);
  }

  public void createScanner() {
    try {
      reader = new Scanner(new File("words.txt"));
    } catch (IOException e) {
      System.out.println("Ther's something wrong, I can feel it");
    }
  }

  public void nextRow(boolean correct) {
    if (correct) {
      end(true);
      return;
    }
    currentRow++;
    if (currentRow >= lines) {
      end(false);
    }
  }

  //remove all frame assets
  public void remove() {
    this.getContentPane().removeAll();
  }

  //pick random word from list
  public void getWord() {
    createScanner();
    int ran = (int) (Math.random() * fileLength);
    for (int i = 0; i < ran; i++) {
      reader.nextLine();
    }
    word = reader.nextLine();
  }

  //check if input is part of the valid words
  public boolean wordExists(String s) {
    createScanner();
    while (reader.hasNextLine()) {
      int i = s.compareTo(reader.nextLine());
      if (i == 0) {
        return true;
      } else if (i < 0) {
        return false;
      }
    }
    return false;
  }

  public void actionPerformed(ActionEvent e) {
    //textField action
    if (e.getSource() == txtField) {
      //get text input
      input = txtField.getText().toUpperCase();
      if (input != null && input.length() == columns && wordExists(input)) {
        //clear field & start row animation
        txtField.setText("");
        grid[currentRow].flipRow(input);
        //invalid word
      } else {
        replay.requestFocus();
      }
    }
    //replay button action
    else if (e.getSource() == replay) {
      remove();
      setup();
    }
    //end button action
    else if (e.getSource() == quit) {
      System.exit(0);
    }
  }
}
