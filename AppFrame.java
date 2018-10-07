import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

@SuppressWarnings("serial")
public class AppFrame  extends JFrame{
    private static AppFrame instance = null;
    private ProgramPanel programPanel;
    private InputPanel inputPanel;
    private ProcessingPanel processingPanel;
    private OutputPanel outputPanel;
    private GridBagLayout gridBagLayoutAppFrame;
    private BoxLayout boxLayout;
    private GuiControl guiControl = new GuiControl();
    private static final String PROPERTIES_PATH = "SACDExtractGUI.properties"; 
    private AppFrame(){
        setupGUI();
    }

    public static AppFrame getInstance(){
        if(instance == null){
            instance = new AppFrame();
        }
        return instance;
    }

    private String getVersion(){
        InputStream is = SACDExtractGUI.class.getResourceAsStream("Version.properties");
        String s;
        Properties props = new Properties();
        try{
            props.load(is);
            s = props.getProperty("version");
            if(s == null){
                s = "";
            }
        }
        catch(Exception e){
            s = "";
        }
        return s;
    }

    private String getRepository(){
        InputStream is = SACDExtractGUI.class.getResourceAsStream("Version.properties");
        String s;
        Properties props = new Properties();
        try{
            props.load(is);
            s = props.getProperty("repository");
            if(s == null){
                s = "";
            }
        }
        catch(Exception e){
            s = "";
        }
        return s;
    }

    public void setupGUI(){
        Properties properties = null;
        String version, repository;
        final ProgramState programState = new ProgramState();
        this.setTitle("SACDExtractGUI");
        this.setMinimumSize(new Dimension(550,800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boxLayout = new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS);
        this.getContentPane().setLayout(boxLayout);
        programPanel = new ProgramPanel();
        inputPanel = new InputPanel();
        processingPanel = new ProcessingPanel();
        outputPanel = new OutputPanel();
        inputPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, inputPanel.getPreferredSize().height));
        processingPanel.setMaximumSize(new Dimension(Short.MAX_VALUE,processingPanel.getPreferredSize().height));
        programPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        processingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        outputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        programState.setFrame(this);
        programState.setPanels(programPanel, inputPanel, processingPanel, outputPanel);

        // Registering panels to guiControl
        guiControl.setFrame(this);
        guiControl.setProgramPanel(programPanel);
        guiControl.setInputPanel(inputPanel);
        guiControl.setProcessingPanel(processingPanel);
        guiControl.setOutputPanel(outputPanel);

        // Registering guiControl to panels
        programPanel.setControl(guiControl);
        inputPanel.setControl(guiControl);
        processingPanel.setControl(guiControl);
        outputPanel.setControl(guiControl);

        this.getContentPane().add(programPanel);
        this.getContentPane().add(inputPanel);
        this.getContentPane().add(processingPanel);
        this.getContentPane().add(outputPanel);
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                // Kill the running process
                guiControl.killExec();
                // Save the GUI state
                programState.setInputDirectory(guiControl.getInputDirectory());
                programState.writeState(PROPERTIES_PATH);
            }
        });

        version = getVersion();
        repository = getRepository();
        guiControl.initGui();
        outputPanel.appendTextArea("SACDExtractGUI " + version + "\n" + repository + "\n\nUse of this program that results in any form of copyright infringement is strictly prohibited.\n\n");
        programState.readState(PROPERTIES_PATH);
        programState.renderState();
        guiControl.setInputDirectory(programState.getInputDirectory());
    }
}

