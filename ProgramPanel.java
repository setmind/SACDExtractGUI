import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ProgramPanel extends JPanel{
    private GuiControl guiControl;
    private GridBagLayout gridBagLayoutMain;
    private BoxLayout boxLayout;
    private JTextField textFieldProgram = new JTextField("sacd_extract executable");
    private JButton buttonBrowse = new JButton("Browse...");
    private JButton buttonTest = new JButton("Test");

    public void setControl(GuiControl c){
        guiControl = c;
    }

    public ProgramPanel(){
        setupGui();
    }

    public String getTextTextFieldProgram(){
        return textFieldProgram.getText();
    }

    public void setTextTextFieldProgram(String s){
        textFieldProgram.setText(s);
    }

    private void setupGui(){
        boxLayout = new BoxLayout(this, BoxLayout.LINE_AXIS);
        textFieldProgram.setMaximumSize(new Dimension(Integer.MAX_VALUE, textFieldProgram.getPreferredSize().height));
        buttonTest.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonProgramTest();}});

        buttonBrowse.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonBrowseProgram();}});

        this.setBorder(BorderFactory.createTitledBorder("Program"));
        this.setLayout(boxLayout);
        this.add(textFieldProgram);
        this.add(buttonBrowse);
        this.add(buttonTest);
    }
}

