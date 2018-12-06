import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ProcessingPanel extends JPanel{
    private BoxLayout boxLayout = new BoxLayout(this, BoxLayout.LINE_AXIS);

    private JPanel panelProcess = new JPanel();
    private BoxLayout boxLayoutProcess = new BoxLayout(panelProcess, BoxLayout.PAGE_AXIS);

    private JPanel panelOptions = new JPanel();
    private BoxLayout boxLayoutOptions = new BoxLayout(panelOptions, BoxLayout.PAGE_AXIS);

    private JPanel panelAreas = new JPanel();
    private BoxLayout boxLayoutAreas = new BoxLayout(panelAreas, BoxLayout.PAGE_AXIS);

    private GuiControl guiControl;

    // Radio buttons for process selection
    private ButtonGroup buttonGroup_process = new ButtonGroup();
    private JRadioButton radButtonDSF = new JRadioButton("DSF");
    private JRadioButton radButtonDSDIFF = new JRadioButton("DSDIFF");
    private JRadioButton radButtonDSDIFFEM = new JRadioButton("DSDIFF edit master");
    private JRadioButton radButtonISO = new JRadioButton("ISO");
    private JRadioButton radButtonISO_DSF = new JRadioButton("ISO+DSF");
    private JRadioButton radButtonISO_DSDIFF = new JRadioButton("ISO+DSDIFF");
    private JRadioButton radButtonNone = new JRadioButton("none");

    private JCheckBox checkBoxPrint = new JCheckBox("Print disc summary");
    private JCheckBox checkBoxDecompress = new JCheckBox("DST decompression");
    private JCheckBox checkBoxSelectTracks = new JCheckBox("Select tracks (ex: 1,4,5)");
    private JTextField textFieldTracks = new JTextField();
    private JCheckBox checkBoxNoPad = new JCheckBox("Padding-less DSF");
    private JCheckBox checkBoxCue= new JCheckBox("Cue sheet");
    private JCheckBox checkBoxStereo = new JCheckBox("Stereo");
    private JCheckBox checkBoxMulti = new JCheckBox("Multi-channel");
    private JRadioButton radButtons[] = new JRadioButton[7];
    private JCheckBox checkBoxes[] = new JCheckBox[7];
    public static final int PROCESS_DSF = 0;
    public static final int PROCESS_DSDIFF = 1;
    public static final int PROCESS_DSDIFFEM = 2;
    public static final int PROCESS_ISO = 3;
    public static final int PROCESS_ISO_DSF = 4;
    public static final int PROCESS_ISO_DSDIFF = 5;
    public static final int PROCESS_NONE = 6;

    public static final int OPTION_STEREO = 0;
    public static final int OPTION_MULTI = 1;
    public static final int OPTION_DECOMPRESS = 2;
    public static final int OPTION_SELECTTRACKS = 3;
    public static final int OPTION_NOPAD = 4;
    public static final int OPTION_CUE = 5;
    public static final int OPTION_PRINT = 6;

    public ProcessingPanel(){
        setupGui();
    }

    public void setProcess(int process){
        radButtons[process].setSelected(true);
    }

    public void clickOption(int option, boolean s){
        checkBoxes[option].setSelected(s);
    }

    public void setSelectedOptionIfEnabled(int option, boolean s){
        if(checkBoxes[option].isEnabled()){
            checkBoxes[option].setSelected(s);
        }
    }

    public void doClickOptionIfEnabled(int option, boolean s){
        if(checkBoxes[option].isEnabled()){
            checkBoxes[option].doClick();
        }
    }

    public void setSelectedOptionIfEnabledByClick(int option, boolean s){
        if(checkBoxes[option].isEnabled() && (checkBoxes[option].isSelected() == false) && s){
            checkBoxes[option].doClick();
        }
    }

    public void setOption(int option, CheckBoxState s){
        checkBoxes[option].setEnabled(s.enabled);
        checkBoxes[option].setSelected(s.selected);
    }

    public boolean getRadButtonState(int process){
        return radButtons[process].isSelected();
    }

    public CheckBoxState getOption(int option){
        CheckBoxState s = new CheckBoxState();

        s.selected = checkBoxes[option].isSelected();
        s.enabled = checkBoxes[option].isEnabled();

        return s;
    }

    public void setSelectedCheckBoxCue(CheckBoxState s){
        checkBoxCue.setEnabled(s.enabled);
        checkBoxCue.setSelected(s.selected);
    }

    public void setSelectedCheckBoxPrint(CheckBoxState s){
        checkBoxPrint.setEnabled(s.enabled);
        checkBoxPrint.setSelected(s.selected);
    }

    public void setControl(GuiControl c){
        guiControl = c;
    }
    public void setEnabledTextFieldTracks(boolean e){
        textFieldTracks.setEnabled(e);
    }

    public void setTextTextFieldTracks(String s){
        textFieldTracks.setText(s);
    }

    public String getTextTextFieldTracks(){
        return textFieldTracks.getText();
    }

    public void clickProcess(int p){
        radButtons[p].doClick();
    }

    private void setupGui(){
        this.setBorder(BorderFactory.createTitledBorder("Processing"));
        this.setLayout(boxLayout);

        // Process button group
        buttonGroup_process.add(radButtonDSF);
        buttonGroup_process.add(radButtonDSDIFF);
        buttonGroup_process.add(radButtonDSDIFFEM);
        buttonGroup_process.add(radButtonISO);
        buttonGroup_process.add(radButtonISO_DSF);
        buttonGroup_process.add(radButtonISO_DSDIFF);
        buttonGroup_process.add(radButtonNone);
        
        radButtons[PROCESS_DSF] = radButtonDSF;
        radButtons[PROCESS_DSDIFF] = radButtonDSDIFF;
        radButtons[PROCESS_DSDIFFEM] = radButtonDSDIFFEM;
        radButtons[PROCESS_ISO] = radButtonISO;
        radButtons[PROCESS_ISO_DSF] = radButtonISO_DSF;
        radButtons[PROCESS_ISO_DSDIFF] = radButtonISO_DSDIFF;
        radButtons[PROCESS_NONE] = radButtonNone;

        checkBoxes[OPTION_STEREO] = checkBoxStereo;
        checkBoxes[OPTION_MULTI] = checkBoxMulti;
        checkBoxes[OPTION_DECOMPRESS] = checkBoxDecompress;
        checkBoxes[OPTION_SELECTTRACKS] = checkBoxSelectTracks;
        checkBoxes[OPTION_NOPAD] = checkBoxNoPad;
        checkBoxes[OPTION_CUE] = checkBoxCue;
        checkBoxes[OPTION_PRINT] = checkBoxPrint;

        radButtonISO.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton s = (JRadioButton) e.getSource(); if(s.isSelected()){guiControl.radButtonISO();}}});

        radButtonDSF.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton s = (JRadioButton) e.getSource(); if(s.isSelected()){guiControl.radButtonDSF();}}});

        radButtonDSDIFF.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton s = (JRadioButton) e.getSource(); if(s.isSelected()){guiControl.radButtonDSDIFF();}}});

        radButtonDSDIFFEM.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton s = (JRadioButton) e.getSource(); if(s.isSelected()){guiControl.radButtonDSDIFFEM();}}});

        radButtonISO_DSF.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton s = (JRadioButton) e.getSource(); if(s.isSelected()){guiControl.radButtonISO_DSF();}}});

        radButtonISO_DSDIFF.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton s = (JRadioButton) e.getSource(); if(s.isSelected()){guiControl.radButtonISO_DSDIFF();}}});

        radButtonNone.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JRadioButton s = (JRadioButton) e.getSource(); if(s.isSelected()){guiControl.radButtonNone();}}});

        // Process panel
        panelProcess.setLayout(boxLayoutProcess);
        panelProcess.add(radButtonDSF);
        panelProcess.add(radButtonDSDIFF);
        panelProcess.add(radButtonDSDIFFEM);
        panelProcess.add(radButtonISO);
        panelProcess.add(radButtonISO_DSF);
        panelProcess.add(radButtonISO_DSDIFF);
        panelProcess.add(radButtonNone);
        panelProcess.setAlignmentY(Component.TOP_ALIGNMENT);
        this.add(panelProcess);

        // Areas panel
        panelAreas.setLayout(boxLayoutAreas);
        panelAreas.add(checkBoxStereo);
        panelAreas.add(checkBoxMulti);
        panelAreas.setAlignmentY(Component.TOP_ALIGNMENT);

        checkBoxStereo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox checkBox = (JCheckBox) e.getSource();
                    CheckBoxState s = new CheckBoxState(checkBox.isSelected(), checkBox.isEnabled());
                    guiControl.checkBoxStereo(s);
                    }
                }
                );

        checkBoxMulti.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox checkBox = (JCheckBox) e.getSource();
                    CheckBoxState s = new CheckBoxState(checkBox.isSelected(), checkBox.isEnabled());
                    guiControl.checkBoxMulti(s);
                    }
                }
                );

        this.add(panelAreas);
        
        // Options panel
        panelOptions.setLayout(boxLayoutOptions);
        panelOptions.add(checkBoxDecompress);
        panelOptions.add(checkBoxSelectTracks);
        textFieldTracks.setMaximumSize(new Dimension(Integer.MAX_VALUE, textFieldTracks.getPreferredSize().height));
        textFieldTracks.setEnabled(false);
        panelOptions.add(textFieldTracks);
        panelOptions.add(checkBoxNoPad);
        panelOptions.add(checkBoxCue);
        panelOptions.add(checkBoxPrint);
        panelOptions.setAlignmentY(Component.TOP_ALIGNMENT);

        checkBoxSelectTracks.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox checkBox = (JCheckBox) e.getSource();
                    CheckBoxState s = new CheckBoxState(checkBox.isSelected(), checkBox.isEnabled());
                    guiControl.checkBoxSelectTracks(s);
                    }
                }
                );
        checkBoxNoPad.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox checkBox = (JCheckBox) e.getSource();
                    CheckBoxState s = new CheckBoxState(checkBox.isSelected(), checkBox.isEnabled());
                    guiControl.checkBoxNoPad(s);
                    }
                }
                );

        checkBoxCue.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox checkBox = (JCheckBox) e.getSource();
                    CheckBoxState s = new CheckBoxState(checkBox.isSelected(), checkBox.isEnabled());
                    guiControl.checkBoxCue(s);
                    }
                }
                );

        checkBoxPrint.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JCheckBox checkBox = (JCheckBox) e.getSource();
                    CheckBoxState s = new CheckBoxState(checkBox.isSelected(), checkBox.isEnabled());
                    guiControl.checkBoxPrint(s);
                    }
                }
                );

        this.add(panelOptions);
    }
}

