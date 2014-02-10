package com.Soham.MSProject.GUI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import com.Soham.MSProject.Input.CreateInputFiles;

public class InputCreation {

  protected Shell shlCreateInput;
  private Text txtInputTextBox;

  /**
   * Launch the application.
   * @param args
   */
  public static void main(String[] args) {
    try {
      InputCreation window = new InputCreation();
      window.open();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Open the window.
   */
  public void open() {
    Display display = Display.getDefault();
    createContents();
    shlCreateInput.open();
    shlCreateInput.layout();
    while (!shlCreateInput.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }

  /**
   * Create contents of the window.
   */
  protected void createContents() {
    shlCreateInput = new Shell();
    shlCreateInput.setToolTipText("Choose the end which you want to flip the bits.");
    shlCreateInput.setSize(509, 249);
    shlCreateInput.setText("Create Input");
    
    Label lblInputString = new Label(shlCreateInput, SWT.NONE);
    lblInputString.setBounds(10, 33, 84, 17);
    lblInputString.setText("Input String");
    
    txtInputTextBox = new Text(shlCreateInput, SWT.BORDER);
    txtInputTextBox.setToolTipText("Input the string that you want to have combinations with its "
        + "own bits flipped.");
    txtInputTextBox.setText("Input text");
    txtInputTextBox.setBounds(102, 33, 371, 27);
    
    Label lblFlipEnd = new Label(shlCreateInput, SWT.NONE);
    lblFlipEnd.setBounds(10, 69, 70, 17);
    lblFlipEnd.setText("Flip end");
    
    final CCombo flipEndCombo = new CCombo(shlCreateInput, SWT.BORDER);
    flipEndCombo.setToolTipText("Choose the end of the string that you want to flip bits off.");
    flipEndCombo.setItems(new String[]{"Starting", "Middle", "Ending"});
    flipEndCombo.setBounds(102, 66, 106, 27);
    
    Label lblNumberOfFlips = new Label(shlCreateInput, SWT.NONE);
    lblNumberOfFlips.setBounds(287, 69, 118, 17);
    lblNumberOfFlips.setText("Number of Flips");
    
    final Combo numberOfFlipsCombo = new Combo(shlCreateInput, SWT.NONE);
    numberOfFlipsCombo.setToolTipText("This will decide how many bits will be flipped. That will "
        + "determine the number of pairs.");
    numberOfFlipsCombo.setItems(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"});
    numberOfFlipsCombo.setBounds(411, 66, 62, 27);
    
    Button btnCreateInputFiles = new Button(shlCreateInput, SWT.NONE);
    btnCreateInputFiles.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseDoubleClick(MouseEvent arg0) {
        CreateInputFiles cif = new CreateInputFiles();
        cif.createFile(txtInputTextBox.getText(), flipEndCombo.getText(),
            numberOfFlipsCombo.getText());
      }
    });
    btnCreateInputFiles.setToolTipText("Click this button to create the input files.");
    btnCreateInputFiles.setBounds(10, 143, 142, 27);
    btnCreateInputFiles.setText("Create input files");
  }
}
