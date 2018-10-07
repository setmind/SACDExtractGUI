import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class OutputPanel extends JPanel{
    private JPanel topMainPanel = new JPanel();
    private BoxLayout boxLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

    private JPanel panelOutput1 = new JPanel();
    private BoxLayout boxLayoutOutput1 = new BoxLayout(panelOutput1, BoxLayout.LINE_AXIS);
    private JCheckBox checkBoxOutput1 = new JCheckBox();
    private JTextField textFieldOutput1 = new JTextField();
    private JButton buttonOutput1 = new JButton("Browse...");

    private JPanel panelOutput2 = new JPanel();
    private JCheckBox checkBoxOutput2 = new JCheckBox();
    private BoxLayout boxLayoutOutput2 = new BoxLayout(panelOutput2, BoxLayout.LINE_AXIS);
    private JTextField textFieldOutput2 = new JTextField();
    private JButton buttonOutput2 = new JButton("Browse...");

    private JPanel panelRun = new JPanel();
    private BoxLayout boxLayoutRun = new BoxLayout(panelRun, BoxLayout.LINE_AXIS);
    private JButton buttonRun = new JButton("Run");
    private JButton buttonStop = new JButton("Stop");

    private JPanel panelStatusClear = new JPanel();
    private BoxLayout boxLayoutStatusClear = new BoxLayout(panelStatusClear, BoxLayout.LINE_AXIS);
    private JLabel labelStatus = new JLabel(" ");
    private JButton buttonClear = new JButton("Clear");

    public JTextArea textArea = new JTextArea(){
        @Override
        public void append(String str) {
            super.append(str);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
        };
    private JScrollPane scrollPane = new JScrollPane(textArea);
    private GuiControl guiControl;

    public OutputPanel(){
        setupGui();
    }

    public void setSelectedOutput1IfEnabled(boolean e){
        if(panelOutput1.isVisible()){
            // Clear the checkbox and click it if e = true
            checkBoxOutput1.setSelected(false);
            if(e){
                checkBoxOutput1.doClick();
            }
        }
    }

    public void setSelectedOutput2IfEnabled(boolean e){
        if(panelOutput2.isVisible()){
            // Clear the checkbox and click it if e = true
            checkBoxOutput2.setSelected(false);
            if(e){
                checkBoxOutput2.doClick();
            }
        }
    }
    
    public void clearTextAreaLog(){
        textArea.setText("");
    }
    public void setEnabledOutput1(boolean enabled, String s){
        checkBoxOutput1.setText(s);
        panelOutput1.setVisible(enabled);
        buttonOutput1.setEnabled(enabled);
        if(!enabled){
            checkBoxOutput1.setSelected(false);
        }
        buttonOutput1.setEnabled(checkBoxOutput1.isSelected());
    }

    public void setEnabledOutput2(boolean enabled, String s){
        checkBoxOutput2.setText(s);
        panelOutput2.setVisible(enabled);
        if(!enabled){
            checkBoxOutput2.setSelected(false);
        }
        buttonOutput2.setEnabled(checkBoxOutput2.isSelected());
    }

    public void setEnabledButtonRun(boolean e){
        buttonRun.setEnabled(e);
    } 

    public void setEnabledButtonStop(boolean e){
        buttonStop.setEnabled(e);
    } 

    public void setEnabledButtonOutput1(boolean e){
        buttonOutput1.setEnabled(e);
    } 

    public void setEnabledButtonOutput2(boolean e){
        buttonOutput2.setEnabled(e);
    } 

    public void setTextTextFieldOutput1(String s){
        textFieldOutput1.setText(s);
    }

    public void setTextTextFieldOutput2(String s){
        textFieldOutput2.setText(s);
    }

    public void setEnabledButtonClear(boolean e){
        buttonClear.setEnabled(e);
    } 

    public void appendTextArea(String str){
        textArea.append(str);
    }

    public void replacePrevLineTextArea(String str){
        try{
            int prevpos1 = textArea.getLineStartOffset(textArea.getLineCount() - 1);
            int prevpos2 = textArea.getLineEndOffset(textArea.getLineCount() - 1);
            textArea.replaceRange(str, prevpos1, prevpos2); 
        }
        catch(Exception e){
        }
    }
    public void freshLineTextArea(){
        try{
            if(textArea.getLineEndOffset(textArea.getLineCount()) != textArea.getLineStartOffset(textArea.getLineCount())){
            }
        }
        catch(Exception e){
            textArea.append("\n");
        }
    }

    public void setControl(GuiControl c){
        guiControl = c;
    }

    public void setEnabledTextFieldOutput1(boolean e){
        textFieldOutput1.setEnabled(e);
    }

    public void setEnabledTextFieldOutput2(boolean e){
        textFieldOutput2.setEnabled(e);
    }
    
    public String getTextOutput1(){
        return textFieldOutput1.getText();
    }

    public String getTextOutput2(){
        return textFieldOutput2.getText();
    }

    public CheckBoxState getCheckBoxStateOutput1(){
        CheckBoxState s = new CheckBoxState();
        s.selected = checkBoxOutput1.isSelected();
        s.enabled = checkBoxOutput1.isEnabled();

        return s;
    }

    public CheckBoxState getCheckBoxStateOutput2(){
        CheckBoxState s = new CheckBoxState();
        s.selected = checkBoxOutput2.isSelected();
        s.enabled = checkBoxOutput2.isEnabled();

        return s;
    }

    public void setTextLabelStatus(String s){
        labelStatus.setText(s);
    }
    private void setupGui(){
        topMainPanel = new JPanel();
        this.setBorder(BorderFactory.createTitledBorder("Output"));
        this.setLayout(boxLayout);
        Font font;
    
        panelOutput1.setLayout(boxLayoutOutput1);
        panelOutput1.add(checkBoxOutput1);
        checkBoxOutput1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox s = (JCheckBox) e.getSource();
                guiControl.checkBoxOutputdir1(s.isSelected());
            }
        });
        panelOutput1.add(textFieldOutput1);
        textFieldOutput1.setMaximumSize(new Dimension(Integer.MAX_VALUE, textFieldOutput1.getPreferredSize().height));
        textFieldOutput1.setEnabled(false);
        buttonOutput1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonOutput1();
            }
        });
        panelOutput1.add(buttonOutput1);

        panelOutput2.setLayout(boxLayoutOutput2);
        panelOutput2.add(checkBoxOutput2);
        checkBoxOutput2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox s = (JCheckBox) e.getSource();
                guiControl.checkBoxOutputdir2(s.isSelected());
            }
        });
        panelOutput2.add(textFieldOutput2);
        textFieldOutput2.setMaximumSize(new Dimension(Integer.MAX_VALUE, textFieldOutput2.getPreferredSize().height));
        textFieldOutput2.setEnabled(false);
        buttonOutput2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonOutput2();
            }
        });
        panelOutput2.add(buttonOutput2);

        panelRun.setLayout(boxLayoutRun);
        buttonRun.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonRun();
            }
        });
        panelRun.add(buttonRun);
        buttonStop.setEnabled(false);
        buttonStop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonStop();
            }
        });
        panelRun.add(buttonStop);

        font = textArea.getFont();
        textArea.setFont(new Font("Courier", Font.PLAIN, font.getSize()));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setMaximumSize(textArea.getPreferredSize());

        panelStatusClear.setLayout(boxLayoutStatusClear);
        panelStatusClear.add(labelStatus);
        labelStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, labelStatus.getPreferredSize().height));
        panelStatusClear.add(labelStatus);
        buttonClear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonClear();
            }
        });
        panelStatusClear.add(buttonClear);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(scrollPane.getSize());
        this.add(panelOutput1);
        this.add(panelOutput2);
        this.add(panelRun);
        this.add(scrollPane);
        this.add(panelStatusClear);
    }
}

