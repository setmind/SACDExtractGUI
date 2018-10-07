import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.text.ParseException;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class InputPanel extends JPanel{
    private GuiControl guiControl;
    private BoxLayout boxLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

    // Radio buttons for input type selection
    private ButtonGroup buttonGroupSelect = new ButtonGroup();

    // Server Panel
    private JPanel panelServer  = new JPanel();
    private BoxLayout boxLayoutServer = new BoxLayout(panelServer, BoxLayout.LINE_AXIS);
    private JRadioButton radbuttonServer = new JRadioButton("Server");
    // Address panel
    private JPanel panelAddress = new JPanel();
    private BoxLayout boxLayoutAddress = new BoxLayout(panelAddress, BoxLayout.LINE_AXIS);
    private JFormattedTextField textFieldAddress;
    private JTextField textFieldAddress0 = new JTextField("000");
    private JLabel labelDot0 = new JLabel(".");
    private JTextField textFieldAddress1 = new JTextField("000");
    private JLabel labelDot1 = new JLabel(".");
    private JTextField textFieldAddress2 = new JTextField("000");
    private JLabel labelDot2 = new JLabel(".");
    private JTextField textFieldAddress3 = new JTextField("000");
    private JLabel labelColon= new JLabel(":");
    private JTextField textFieldAddressPort = new JTextField("00000");
   
    private JButton buttonPing = new JButton("Ping");
    private JButton buttonServerTest = new JButton("Test");
    // File panel
    private JPanel panelFile = new JPanel();
    private BoxLayout boxLayoutFile = new BoxLayout(panelFile, BoxLayout.LINE_AXIS);
    private JRadioButton radbuttonFile = new JRadioButton("File");
    private JTextField textFieldPath = new JTextField();
    private JButton buttonBrowse = new JButton("Browse...");

    public static final int INPUT_TYPE_SERVER = 0;
    public static final int INPUT_TYPE_FILE = 1;
    public static final int INPUT_TYPE_FILES = 2;

    // File list buttons panel
    private JButton buttonAdd = new JButton("Add");
    private JButton buttonRemove = new JButton("Remove");
    private JPanel panelButtonsFileList = new JPanel();
    private BoxLayout boxLayoutButtonsFileList = new BoxLayout(panelButtonsFileList, BoxLayout.PAGE_AXIS);

    // File list panel
    private DefaultListModel<String> listModel = new DefaultListModel<String>();
    private JRadioButton radbuttonFiles = new JRadioButton("Files");
    private JPanel panelFileList = new JPanel();
    private JList<String> listFiles = new JList<String>(listModel);
    private JScrollPane scrollPane = new JScrollPane(listFiles);
    private BoxLayout boxLayoutFileList = new BoxLayout(panelFileList, BoxLayout.LINE_AXIS);


    public void setInputType(int i){
        switch(i){
            case INPUT_TYPE_SERVER:
                radbuttonServer.doClick();
                break;
            case INPUT_TYPE_FILE:
                radbuttonFile.doClick();
                break;
            case INPUT_TYPE_FILES:
                radbuttonFiles.doClick();
                break;
        }
    }

    public int getInputType(){
        int ret = -1;
        if(radbuttonServer.isSelected()){
            ret = INPUT_TYPE_SERVER;
        }
        else if(radbuttonFile.isSelected()){
            ret = INPUT_TYPE_FILE;
        }
        else if(radbuttonFiles.isSelected()){
            ret = INPUT_TYPE_FILES;
        }
        return ret;
    }

    public InputPanel(){
        setupGui();
    }

    public void setControl(GuiControl c){
        guiControl = c;
    }

    public boolean getRadButtonStateServer()
    {
        return radbuttonServer.isSelected();
    }
    public boolean getRadButtonStateFile()
    {
        return radbuttonFile.isSelected();
    }
    public boolean getRadButtonStateFiles()
    {
        return radbuttonFiles.isSelected();
    }

    public void setEnabledButtonPing(boolean e){
        buttonPing.setEnabled(e);
    }

    public void setEnabledButtonBrowse(boolean e){
        buttonBrowse.setEnabled(e);
    }

    public void setEnabledButtonTest(boolean e){
        buttonServerTest.setEnabled(e);
    }

    public String getTextTextFieldPath(){
        return textFieldPath.getText();
    }

    public void setEnabledTextFieldsAddress(boolean e){
        textFieldAddress0.setEnabled(e);
        textFieldAddress1.setEnabled(e);
        textFieldAddress2.setEnabled(e);
        textFieldAddress3.setEnabled(e);
        textFieldAddressPort.setEnabled(e);
    }

    public void setEnabledTextFieldPath(boolean e){
        textFieldPath.setEnabled(e);
    }
    public void setTextTextFieldPath(String s){
        textFieldPath.setText(s);
    }
    
    public void setTextTextFieldAddress0(String s, boolean warn){
        if(s != null){
            textFieldAddress0.setText(s);
        }
        if(warn){
            textFieldAddress0.setBackground(Color.YELLOW);
        }
        else{
            textFieldAddress0.setBackground(Color.WHITE);
        }
    }

    public void setTextTextFieldAddress1(String s, boolean warn){
        if(s != null){
            textFieldAddress1.setText(s);
        }
        if(warn){
            textFieldAddress1.setBackground(Color.YELLOW);
        }
        else{
            textFieldAddress1.setBackground(Color.WHITE);
        }
    }

    public void setTextTextFieldAddress2(String s, boolean warn){
        if(s != null){
            textFieldAddress2.setText(s);
        }
        if(warn){
            textFieldAddress2.setBackground(Color.YELLOW);
        }
        else{
            textFieldAddress2.setBackground(Color.WHITE);
        }
    }

    public void setTextTextFieldAddress3(String s, boolean warn){
        if(s != null){
            textFieldAddress3.setText(s);
        }
        if(warn){
            textFieldAddress3.setBackground(Color.YELLOW);
        }
        else{
            textFieldAddress3.setBackground(Color.WHITE);
        }
    }

    public void setTextTextFieldAddressPort(String s, boolean warn){
        if(s != null){
            textFieldAddressPort.setText(s);
        }
        if(warn){
            textFieldAddressPort.setBackground(Color.YELLOW);
        }
        else{
            textFieldAddressPort.setBackground(Color.WHITE);
        }
    }

    public String getTextTextFieldAddress0(){
        return textFieldAddress0.getText();
    }

    public String getTextTextFieldAddress1(){
        return textFieldAddress1.getText();
    }

    public String getTextTextFieldAddress2(){
        return textFieldAddress2.getText();
    }

    public String getTextTextFieldAddress3(){
        return textFieldAddress3.getText();
    }

    public String getTextTextFieldAddressPort(){
        return textFieldAddressPort.getText();
    }

    public void clickServer(){
        radbuttonServer.doClick();
    }

    public void clickFile(){
        radbuttonFile.doClick();
    }

    public void removeFiles(int [] i){
        int k = 0;
        for(int j : i){
            listModel.removeElementAt(j - k);
            k ++;
        }
    }

    public void addFile(String s){
        listModel.addElement(s);
    }

    public ArrayList<String> getFiles(){
        ArrayList<String> files = new ArrayList<String>();
        int i;
        for(i = 0; i < listModel.getSize(); i ++){
            files.add(listModel.get(i).toString());
        }
        return files;
    }

    public void setEnabledListFiles (boolean e){
        listFiles.setEnabled(e);
    }

    public void setEnabledButtonAdd (boolean e){
        buttonAdd.setEnabled(e);
    }

    public void setEnabledButtonRemove (boolean e){
        buttonRemove.setEnabled(e);
    }

    private void setupGui(){
        this.setBorder(BorderFactory.createTitledBorder("Input"));
        this.setLayout(boxLayout);
        // Process button group
        buttonGroupSelect.add(radbuttonServer);
        buttonGroupSelect.add(radbuttonFile);
        buttonGroupSelect.add(radbuttonFiles);

        panelServer.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFile.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFileList.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Server panel
        panelServer.setLayout(boxLayoutServer);
        panelServer.add(radbuttonServer);
        radbuttonServer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton s = (JRadioButton) e.getSource(); if(s.isSelected()){guiControl.radButton_server();}}});

        // Address panel
        panelAddress.setLayout(boxLayoutAddress);
        panelAddress.add(textFieldAddress0);
        textFieldAddress0.setMaximumSize(new Dimension(textFieldAddress0.getPreferredSize().width, textFieldAddress0.getPreferredSize().height));
        textFieldAddress0.addFocusListener(new FocusListener () {
            public void focusLost(FocusEvent e){
                JTextField s = (JTextField) e.getSource(); 
                guiControl.textFieldAddress0(s.getText());
            }
            public void focusGained(FocusEvent e){
            }
        });
        panelAddress.add(labelDot0);
        panelAddress.add(textFieldAddress1);
        textFieldAddress1.addFocusListener(new FocusListener () {
            public void focusLost(FocusEvent e){
                JTextField s = (JTextField) e.getSource(); 
                guiControl.textFieldAddress1(s.getText());
            }
            public void focusGained(FocusEvent e){
            }
        });
        textFieldAddress1.setMaximumSize(new Dimension(textFieldAddress1.getPreferredSize().width, textFieldAddress1.getPreferredSize().height));
        panelAddress.add(labelDot1);
        panelAddress.add(textFieldAddress2);
        textFieldAddress2.addFocusListener(new FocusListener () {
            public void focusLost(FocusEvent e){
                JTextField s = (JTextField) e.getSource(); 
                guiControl.textFieldAddress2(s.getText());
            }
            public void focusGained(FocusEvent e){
            }
        });
        textFieldAddress2.setMaximumSize(new Dimension(textFieldAddress2.getPreferredSize().width, textFieldAddress2.getPreferredSize().height));
        panelAddress.add(labelDot2);
        panelAddress.add(textFieldAddress3);
        textFieldAddress3.addFocusListener(new FocusListener () {
            public void focusLost(FocusEvent e){
                JTextField s = (JTextField) e.getSource(); 
                guiControl.textFieldAddress3(s.getText());
            }
            public void focusGained(FocusEvent e){
            }
        });
        textFieldAddress3.setMaximumSize(new Dimension(textFieldAddress3.getPreferredSize().width, textFieldAddress3.getPreferredSize().height));
        panelAddress.add(labelColon);
        panelAddress.add(textFieldAddressPort);
        textFieldAddressPort.addFocusListener(new FocusListener () {
            public void focusLost(FocusEvent e){
                JTextField s = (JTextField) e.getSource(); 
                guiControl.textFieldAddressPort(s.getText());
            }
            public void focusGained(FocusEvent e){
            }
        });
        textFieldAddressPort.setMaximumSize(new Dimension(textFieldAddressPort.getPreferredSize().width, textFieldAddressPort.getPreferredSize().height));
        
        radbuttonFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton s = (JRadioButton) e.getSource(); if(s.isSelected()){guiControl.radButton_file();}}});

        panelServer.add(panelAddress);

        buttonPing.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonPing();}});

        buttonServerTest.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonServerTest();}});

        panelServer.add(buttonPing);
        panelServer.add(buttonServerTest);

        this.add(panelServer);

        // File panel
        panelFile.setLayout(boxLayoutFile);
        textFieldPath.setMaximumSize(new Dimension(Integer.MAX_VALUE, textFieldPath.getPreferredSize().height));

        panelFile.add(radbuttonFile);
        panelFile.add(textFieldPath);
        panelFile.add(buttonBrowse);
        buttonBrowse.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonBrowseFile();}});
        this.add(panelFile);

        // File list panel
        radbuttonFiles.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton s = (JRadioButton) e.getSource(); if(s.isSelected()){guiControl.radButton_files();}}});
        
        buttonAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonAdd();}});
        panelFileList.setLayout(boxLayoutFileList);
        panelButtonsFileList.setLayout(boxLayoutButtonsFileList);
        panelButtonsFileList.add(buttonAdd);
        panelButtonsFileList.add(buttonRemove);
        listFiles.setTransferHandler(null);

        buttonRemove.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonRemove(listFiles.getSelectedIndices());}});

        buttonAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guiControl.buttonAdd(listModel);}});

        listFiles.setVisibleRowCount(7);
        panelFileList.add(radbuttonFiles);
        panelFileList.add(scrollPane);
        panelFileList.add(panelButtonsFileList);
        this.add(panelFileList);
    }
}

