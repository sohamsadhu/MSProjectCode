package com.Soham.MSProject.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import com.Soham.MSProject.Input.CreateInputPairs;
import com.Soham.MSProject.Input.CreateInputPairsImpl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Experiment {

  private JFrame frame;
  private JTextField seedipbox;
  private JLabel lblNumberOfPairs;
  private JTextField opfilename;
  
  private enum GroupOfFlippedBits {    
    Starting( "Starting" ),
    Middle( "Middle" ),
    Trailing( "Trailing" );
    
    private String bitposition;
    
    private GroupOfFlippedBits( String bitpos ) {
      this.bitposition = bitpos;
    }
    
    @Override
    public String toString() {
      return bitposition;
    }
  }

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Experiment window = new Experiment();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public Experiment() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 952, 564);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);
    
    JLabel seediptxtlbl = new JLabel("Seed Input String");
    seediptxtlbl.setBounds(0, 0, 109, 22);
    frame.getContentPane().add(seediptxtlbl);
    
    seedipbox = new JTextField();
    seedipbox.setToolTipText("Input the seed string that will have bits flipped for experiment.");
    seediptxtlbl.setLabelFor(seedipbox);
    seedipbox.setText("Input String");
    seedipbox.setBounds(121, 0, 580, 22);
    frame.getContentPane().add(seedipbox);
    seedipbox.setColumns(10);
    
    lblNumberOfPairs = new JLabel("Number of Pairs");
    lblNumberOfPairs.setBounds(0, 35, 109, 16);
    frame.getContentPane().add(lblNumberOfPairs);
    
    final JComboBox<Integer> numberofpairs = new JComboBox<Integer>();
    numberofpairs.setToolTipText("This selects the number of strings with flips to produce.");
    lblNumberOfPairs.setLabelFor(numberofpairs);
    numberofpairs.setModel(new DefaultComboBoxModel<Integer>
      (new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}));
    numberofpairs.setMaximumRowCount(20);
    numberofpairs.setBounds(121, 35, 56, 22);
    frame.getContentPane().add(numberofpairs);
    
    JLabel lblOutputFile = new JLabel("Output File");
    lblOutputFile.setBounds(211, 35, 79, 16);
    frame.getContentPane().add(lblOutputFile);
    
    opfilename = new JTextField();
    opfilename.setToolTipText("You can enter the file directory to better organize the results.");
    lblOutputFile.setLabelFor(opfilename);
    opfilename.setText("Output File");
    opfilename.setBounds(279, 35, 421, 22);
    frame.getContentPane().add(opfilename);
    opfilename.setColumns(10);
    
    JLabel lblFlipBits = new JLabel("Flip bits");
    lblFlipBits.setBounds(713, 3, 56, 19);
    frame.getContentPane().add(lblFlipBits);
    
    final JComboBox<GroupOfFlippedBits> flipBitValue = new JComboBox<GroupOfFlippedBits>();
    flipBitValue.setModel(new DefaultComboBoxModel<GroupOfFlippedBits>(GroupOfFlippedBits.values()));
    flipBitValue.setToolTipText("This will select from which end the flipping to start.");
    flipBitValue.setBounds(781, 0, 92, 22);
    frame.getContentPane().add(flipBitValue);
    
    JButton createipbtn = new JButton("Create input text file.");
    createipbtn.setToolTipText("Click the button to create input file with given parameters.");
    createipbtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        CreateInputPairs c = new CreateInputPairsImpl();
        Object [] success = c.createFile( seedipbox.getText(), 
            flipBitValue.getSelectedItem().toString(),
            ( Integer )numberofpairs.getSelectedItem(), opfilename.getText() );
        if( success.length != 2 ) {
          JOptionPane.showMessageDialog(null, "Something bad happened");
        } else {
          JOptionPane.showMessageDialog(null, success[1]);
        }
      }
    });
    createipbtn.setBounds(723, 25, 175, 32);
    frame.getContentPane().add(createipbtn);
  }
}
