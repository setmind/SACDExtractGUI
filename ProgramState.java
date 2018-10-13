import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

class ProgramState{
    private String program;
    private int inputType;
    private int ipAddr0, ipAddr1, ipAddr2, ipAddr3, ipAddrPort;
    private int processingType;
    private boolean stereo, multi;
    private boolean decompress;
    private boolean nopad;
    private boolean cue;
    private boolean print;
    private boolean output1Selected, output2Selected;
    private String output1, output2;
    private AppFrame appFrame;
    private String inputDirectory;

    private ProgramPanel programPanel;
    private InputPanel inputPanel;
    private ProcessingPanel processingPanel;
    private OutputPanel outputPanel;
    
    private void init(){
        program = OSDetect.isWindows() ? "sacd_extract.exe" : "sacd_extract";
        inputType = InputPanel.INPUT_TYPE_SERVER;
        ipAddr0 = 192; ipAddr1 = 168; ipAddr2 = 1; ipAddr3 = 100; ipAddrPort = 2002;
        processingType = ProcessingPanel.PROCESS_DSF;
        stereo = true;
        multi = false;
        decompress = true;
        nopad = false;
        cue = false;
        print = false;
        output1Selected = false; output2Selected = false;
        output1 = ""; output2 = "";
        inputDirectory = null;
    }

    public void setFrame(AppFrame f){
        appFrame = f;
    }

    public void setPanels(ProgramPanel p, InputPanel i, ProcessingPanel r, OutputPanel o){
        programPanel = p;
        inputPanel = i;
        processingPanel = r;
        outputPanel = o;
    }

    public void renderState(){
        programPanel.setTextTextFieldProgram(program);
        inputPanel.setInputType(inputType);
        inputPanel.setTextTextFieldAddress0(Integer.toString(ipAddr0), false);
        inputPanel.setTextTextFieldAddress1(Integer.toString(ipAddr1), false);
        inputPanel.setTextTextFieldAddress2(Integer.toString(ipAddr2), false);
        inputPanel.setTextTextFieldAddress3(Integer.toString(ipAddr3), false);
        inputPanel.setTextTextFieldAddressPort(Integer.toString(ipAddrPort), false);

        processingPanel.clickProcess(processingType);
        processingPanel.setSelectedOptionIfEnabled(ProcessingPanel.OPTION_STEREO, stereo);
        processingPanel.setSelectedOptionIfEnabled(ProcessingPanel.OPTION_MULTI, multi);
        processingPanel.setSelectedOptionIfEnabled(ProcessingPanel.OPTION_DECOMPRESS, decompress);
        processingPanel.doClickOptionIfEnabled(ProcessingPanel.OPTION_NOPAD, nopad);
        processingPanel.setSelectedOptionIfEnabled(ProcessingPanel.OPTION_CUE, cue);
        processingPanel.setSelectedOptionIfEnabled(ProcessingPanel.OPTION_PRINT, print);

        outputPanel.setTextTextFieldOutput1(output1);
        outputPanel.setTextTextFieldOutput2(output2);
        outputPanel.setSelectedOutput1IfEnabled(output1Selected);
        outputPanel.setSelectedOutput2IfEnabled(output2Selected);
    }

    public void setInputDirectory(String s){
        inputDirectory = s;
    }

    public boolean readState(String fname){
        Properties prop = new Properties();
        String s;
        int i;
        boolean b;
        FileInputStream fis;

        try{
            fis = new FileInputStream(fname); 
            prop.load(fis);

            if((s = prop.getProperty("program")) != null){
                program = s; 
            }
            else{
                program = OSDetect.isWindows() ? "sacd_extract.exe" : "sacd_extract";
            }
            if((s = prop.getProperty("inputType")) != null){
                try{
                    inputType = Integer.parseInt(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("ipAddr0")) != null){
                try{
                    ipAddr0 = Integer.parseInt(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("ipAddr1")) != null){
                try{
                    ipAddr1 = Integer.parseInt(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("ipAddr2")) != null){
                try{
                    ipAddr2 = Integer.parseInt(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("ipAddr3")) != null){
                try{
                    ipAddr3 =  Integer.parseInt(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("ipAddrPort")) != null){
                try{
                    ipAddrPort = Integer.parseInt(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("processingType")) != null){
                try{
                    processingType = Integer.parseInt(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("stereo")) != null){
                try{
                    stereo = Boolean.parseBoolean(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("multi")) != null){
                try{
                    multi = Boolean.parseBoolean(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("decompress")) != null){
                try{
                    decompress = Boolean.parseBoolean(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("nopad")) != null){
                try{
                    nopad = Boolean.parseBoolean(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("cue")) != null){
                try{
                    cue = Boolean.parseBoolean(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("print")) != null){
                try{
                    print = Boolean.parseBoolean(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("output1Selected")) != null){
                try{
                    output1Selected = Boolean.parseBoolean(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("output2Selected")) != null){
                try{
                    output2Selected = Boolean.parseBoolean(s);
                }
                catch(Exception e){}
            }
            if((s = prop.getProperty("output1")) != null){
                output1 = s;
            }
            if((s = prop.getProperty("output2")) != null){
                output2 = s;
            }
            if((s = prop.getProperty("inputDirectory")) != null){
                inputDirectory = s;
            }
        }
        catch(Exception e){
            // State not read from file.
            return false;
        }
        return true;
    }

    public void writeState(String fname){
        Properties properties = getState();
        FileOutputStream fos;
        try{
            fos = new FileOutputStream(fname);
            properties.store(fos, "");
        }
        catch(Exception e){
        }
        
    }

    public String getInputDirectory(){
        return inputDirectory;
    }

    public void getInputDirectory(String s){
        inputDirectory  = s;
    }

    private Properties getState(){
        Properties properties = new Properties();
        CheckBoxState checkBoxState;

        properties.setProperty("program", programPanel.getTextTextFieldProgram());
        properties.setProperty("inputType", Integer.toString(inputPanel.getInputType()));
        properties.setProperty("ipAddr0", inputPanel.getTextTextFieldAddress0());
        properties.setProperty("ipAddr1", inputPanel.getTextTextFieldAddress1());
        properties.setProperty("ipAddr2", inputPanel.getTextTextFieldAddress2());
        properties.setProperty("ipAddr3", inputPanel.getTextTextFieldAddress3());
        properties.setProperty("ipAddrPort", inputPanel.getTextTextFieldAddressPort());
        if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_DSF)){
            properties.setProperty("processingType", Integer.toString(ProcessingPanel.PROCESS_DSF));
        }
        else if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_DSDIFF)){
            properties.setProperty("processingType", Integer.toString(ProcessingPanel.PROCESS_DSDIFF));
        }
        else if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_DSDIFFEM)){
            properties.setProperty("processingType", Integer.toString(ProcessingPanel.PROCESS_DSDIFFEM));
        }
        else if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_ISO)){
            properties.setProperty("processingType", Integer.toString(ProcessingPanel.PROCESS_ISO));
        }
        else if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_ISO_DSF)){
            properties.setProperty("processingType", Integer.toString(ProcessingPanel.PROCESS_ISO_DSF));
        }
        else if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_ISO_DSDIFF)){
            properties.setProperty("processingType", Integer.toString(ProcessingPanel.PROCESS_ISO_DSDIFF));
        }
        else if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_NONE)){
            properties.setProperty("processingType", Integer.toString(ProcessingPanel.PROCESS_NONE));
        }
        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_STEREO);
        properties.setProperty("stereo", Boolean.toString(checkBoxState.selected));

        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_MULTI);
        properties.setProperty("multi", Boolean.toString(checkBoxState.selected));

        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_DECOMPRESS); 
        properties.setProperty("decompress", Boolean.toString(checkBoxState.selected));
    
        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_NOPAD);
        properties.setProperty("nopad", Boolean.toString(checkBoxState.selected));

        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_CUE);
        properties.setProperty("cue", Boolean.toString(checkBoxState.selected));
  
        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_PRINT); 
        properties.setProperty("print", Boolean.toString(checkBoxState.selected));

        checkBoxState = outputPanel.getCheckBoxStateOutput1(); 
        properties.setProperty("output1Selected", Boolean.toString(checkBoxState.selected));
        checkBoxState = outputPanel.getCheckBoxStateOutput2(); 
        properties.setProperty("output2Selected", Boolean.toString(checkBoxState.selected));

        properties.setProperty("output1", outputPanel.getTextOutput1());
        properties.setProperty("output2", outputPanel.getTextOutput2());

        if(inputDirectory != null){
            properties.setProperty("inputDirectory", inputDirectory);
        }

        return properties;
    }

    
    public void printLog(String s){
        if(s.contains("Completed:") || s.contains("Processing ")){
            outputPanel.replacePrevLineTextArea(s);
        }
        else{
            outputPanel.appendTextArea(s + "\n");
        }
    }
        
    public ProgramState(){
        init();
    }
}
