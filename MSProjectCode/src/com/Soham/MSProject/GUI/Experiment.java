package com.Soham.MSProject.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
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
    frame.setBounds(100, 100, 990, 536);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);
    
    JLabel seediptxtlbl = new JLabel("Seed Input String");
    seediptxtlbl.setBounds(0, 0, 109, 22);
    frame.getContentPane().add(seediptxtlbl);
    
    seedipbox = new JTextField();
    seediptxtlbl.setLabelFor(seedipbox);
    seedipbox.setText("Input String");
    seedipbox.setBounds(121, 0, 599, 22);
    frame.getContentPane().add(seedipbox);
    seedipbox.setColumns(10);
    
    lblNumberOfPairs = new JLabel("Number of Pairs");
    lblNumberOfPairs.setBounds(0, 27, 109, 16);
    frame.getContentPane().add(lblNumberOfPairs);
    
    JComboBox numberofpairs = new JComboBox();
    lblNumberOfPairs.setLabelFor(numberofpairs);
    numberofpairs.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"}));
    numberofpairs.setMaximumRowCount(20);
    numberofpairs.setBounds(121, 24, 56, 22);
    frame.getContentPane().add(numberofpairs);
    
    JLabel lblOutputFile = new JLabel("Output File");
    lblOutputFile.setBounds(212, 27, 79, 16);
    frame.getContentPane().add(lblOutputFile);
    
    opfilename = new JTextField();
    lblOutputFile.setLabelFor(opfilename);
    opfilename.setText("Output File");
    opfilename.setBounds(288, 24, 421, 22);
    frame.getContentPane().add(opfilename);
    opfilename.setColumns(10);
    
    JButton createipbtn = new JButton("Create input text file.");
    createipbtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        CreateInputPairs c = new CreateInputPairsImpl();
      }
    });
    createipbtn.setBounds(752, 13, 171, 34);
    frame.getContentPane().add(createipbtn);
  }
}
