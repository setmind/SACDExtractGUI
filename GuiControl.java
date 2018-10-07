import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.File;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.awt.FileDialog;
import java.io.FilenameFilter;

public class GuiControl implements ExecThread.Callback{
    private JFrame frame;
    private ProgramPanel programPanel;
    private InputPanel inputPanel;
    private ProcessingPanel processingPanel;
    private OutputPanel outputPanel;
    private String inputDirectory = null;
    private static final boolean WIN32 = "\\".equals(System.getProperty("file.separator"));
    private static final boolean MACOS = System.getProperty("os.name").toLowerCase().startsWith("mac os x");
    private CheckBoxState checkBoxState_unselected_disabled = new CheckBoxState(false, false);
    private CheckBoxState checkBoxState_unselected_enabled = new CheckBoxState(false, true);
    private CheckBoxState checkBoxState_selected_disabled = new CheckBoxState(true, false);
    private CheckBoxState checkBoxState_selected_enabled = new CheckBoxState(true, true);
    ExecThread execThread;

    private void ExecButtonSetEnabled(boolean e){
        inputPanel.setEnabledButtonPing(e);
        inputPanel.setEnabledButtonTest(e);
        outputPanel.setEnabledButtonRun(e);
        outputPanel.setEnabledButtonStop(!e);
        outputPanel.setEnabledButtonClear(e);
    }

    public void processExit(boolean s){
        outputPanel.freshLineTextArea();
        if(s){
            printLog("[DONE]");
            outputPanel.appendTextArea("\n");
        }
        ExecButtonSetEnabled(true);
    }

    public void printLog(String s){
        if(s.contains("Completed:") || s.contains("Processing ")){
            outputPanel.replacePrevLineTextArea(s);
        }
        else{
            outputPanel.appendTextArea(s + "\n");
        }
    }

    public void printStatus(String s){
        outputPanel.setTextLabelStatus(s);
    }

    public void buttonClear(){
        outputPanel.clearTextAreaLog();
        outputPanel.setTextLabelStatus("");
    }

    public void buttonBrowseProgram(){
        if(MACOS){
            buttonBrowseProgramFD();
        }
        else{
            // On Ubuntu, FileDialog is extremely slow on a NAS with many ISO files.
            buttonBrowseProgramJFC();
        }
    }
    
    public void buttonBrowseProgramJFC(){
        int ret;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a sacd_extract program");
        fileChooser.setApproveButtonText("Add");
        ret = fileChooser.showOpenDialog(frame);
        if(ret == JFileChooser.APPROVE_OPTION){
            programPanel.setTextTextFieldProgram(fileChooser.getSelectedFile().getPath());
        }
    }

    public void buttonBrowseProgramFD(){
        File files[];
        FileDialog fileDialog = new FileDialog(frame, "Select a sacd_extract program", FileDialog.LOAD);
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);
        files = fileDialog.getFiles();
        if(files.length > 0){
            for(File file : files){
                programPanel.setTextTextFieldProgram(file.getPath());
            }
        }
    }

    public void setInputDirectory(String s){
        inputDirectory = s;
    }

    public String getInputDirectory(){
        return inputDirectory;
    }

    public void buttonBrowseFile(){
        if(MACOS){
            buttonBrowseFileFD();
        }
        else{
            // On Ubuntu, FileDialog is extremely slow on a NAS with many ISO files.
            buttonBrowseFileJFC();
        }
    }

    public void buttonBrowseFileJFC(){
        int ret;
        JFileChooser fileChooser = new JFileChooser();
        FileFilter fileFilter = new FileNameExtensionFilter("ISO file", "iso");
        fileChooser.addChoosableFileFilter(fileFilter);
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setDialogTitle("Select an ISO file");
        fileChooser.setApproveButtonText("Add");
        if(inputDirectory != null){
            fileChooser.setCurrentDirectory(new File(inputDirectory));
        }
        ret = fileChooser.showOpenDialog(frame);
        if(ret == JFileChooser.APPROVE_OPTION){
            inputPanel.setTextTextFieldPath(fileChooser.getSelectedFile().getPath());
            setInputDirectory(fileChooser.getCurrentDirectory().toString());
        }
    }

    public void buttonBrowseFileFD(){
        File files[];
        FileDialog fileDialog = new FileDialog(frame, "Select an ISO file", FileDialog.LOAD);
        fileDialog.setMultipleMode(false);
        if(inputDirectory != null){
            fileDialog.setDirectory(inputDirectory);
        }
        fileDialog.setFilenameFilter(new FilenameFilter (){
            public boolean accept(File f, String name){
                return name.toLowerCase().endsWith(".iso");
            }
        });
        fileDialog.setVisible(true);
        files = fileDialog.getFiles();
        if(files.length > 0){
            for(File file : files){
                inputPanel.setTextTextFieldPath(file.getPath());
            }
            setInputDirectory(fileDialog.getDirectory());
        }
    }

    public void buttonAdd(){
        if(MACOS){
            buttonAddFD();
        }
        else{
            // On Ubuntu, FileDialog is extremely slow on a NAS with many ISO files.
            buttonAddJFC();
        }
    }

    public void buttonAddJFC(){
        int ret;
        JFileChooser fileChooser = new JFileChooser();
        FileFilter fileFilter = new FileNameExtensionFilter("ISO file", "iso");
        fileChooser.addChoosableFileFilter(fileFilter);
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setDialogTitle("Select ISO files");
        fileChooser.setApproveButtonText("Add");
        fileChooser.setMultiSelectionEnabled(true);
        if(inputDirectory != null){
            fileChooser.setCurrentDirectory(new File(inputDirectory));
        }
        ret = fileChooser.showOpenDialog(frame);
        if(ret == JFileChooser.APPROVE_OPTION){
            for(File f : fileChooser.getSelectedFiles()){
                inputPanel.addFile(f.getPath());
            }
            setInputDirectory(fileChooser.getCurrentDirectory().toString());
        }
    }

    public void buttonAddFD(){
        File files[];
        FileDialog fileDialog = new FileDialog(frame, "Select ISO files", FileDialog.LOAD);
        fileDialog.setMultipleMode(true);
        if(inputDirectory != null){
            fileDialog.setDirectory(inputDirectory);
        }
        fileDialog.setFilenameFilter(new FilenameFilter (){
            public boolean accept(File f, String name){
                return name.toLowerCase().endsWith(".iso");
            }
        });
        fileDialog.setVisible(true);
        files = fileDialog.getFiles();
        if(files.length > 0){
            for(File file : files){
                inputPanel.addFile(file.getPath());
            }
            setInputDirectory(fileDialog.getDirectory());
        }
    }

    public void buttonOutput1(){
        int ret;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a directory");
        fileChooser.setApproveButtonText("Add");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        ret = fileChooser.showOpenDialog(frame);
        if(ret == JFileChooser.APPROVE_OPTION){
            outputPanel.setTextTextFieldOutput1(fileChooser.getSelectedFile().getPath());
        }
    }

    public void buttonOutput2(){
        int ret;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a directory");
        fileChooser.setApproveButtonText("Add");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        ret = fileChooser.showOpenDialog(frame);
        if(ret == JFileChooser.APPROVE_OPTION){
            outputPanel.setTextTextFieldOutput2(fileChooser.getSelectedFile().getPath());
        }
    }


    public void setFrame(JFrame f){
        frame = f;
    }

    public void setProgramPanel(ProgramPanel p){
        programPanel = p;
    }

    public void setInputPanel(InputPanel p){
        inputPanel = p;
    }

    public void setProcessingPanel(ProcessingPanel p){
        processingPanel = p;
    }

    public void setOutputPanel(OutputPanel p){
        outputPanel = p;
    }

    public void textFieldAddress0(String s){
        int i;
        try{
            i = Integer.parseInt(s); 
            if(i <= 0 || i > 255){
                inputPanel.setTextTextFieldAddress0("", true);
            }
            else{
                inputPanel.setTextTextFieldAddress0(null, false);
            }
        }
        catch(NumberFormatException e){
            inputPanel.setTextTextFieldAddress0("", true);
        }
    }

    public void textFieldAddress1(String s){
        int i;
        try{
            i = Integer.parseInt(s); 
            if(i < 0 || i > 255){
                inputPanel.setTextTextFieldAddress1("", true);
            }
            else{
                inputPanel.setTextTextFieldAddress1(null, false);
            }
        }
        catch(NumberFormatException e){
            inputPanel.setTextTextFieldAddress1("", true);
        }
    }

    public void textFieldAddress2(String s){
        int i;
        try{
            i = Integer.parseInt(s); 
            if(i < 0 || i > 255){
                inputPanel.setTextTextFieldAddress2("", true);
            }
            else{
                inputPanel.setTextTextFieldAddress2(null, false);
            }
        }
        catch(NumberFormatException e){
            inputPanel.setTextTextFieldAddress2("", true);
        }
    }

    public void textFieldAddress3(String s){
        int i;
        try{
            i = Integer.parseInt(s); 
            if(i <= 0 || i > 255){
                inputPanel.setTextTextFieldAddress3("", true);
            }
            else{
                inputPanel.setTextTextFieldAddress3(null, false);
            }
        }
        catch(NumberFormatException e){
            inputPanel.setTextTextFieldAddress3("", true);
        }
    }

    public void textFieldAddressPort(String s){
        int i;
        try{
            i = Integer.parseInt(s); 
            if(i <= 0 || i > 65535){
                inputPanel.setTextTextFieldAddressPort("", true);
            }
            else{
                inputPanel.setTextTextFieldAddressPort(null, false);
            }
        }
        catch(NumberFormatException e){
            inputPanel.setTextTextFieldAddressPort("", true);
        }
    }

    public void radButtonDSF(){
        if(processingPanel.getOption(ProcessingPanel.OPTION_STEREO).enabled == false || processingPanel.getOption(ProcessingPanel.OPTION_MULTI).enabled == false){
            processingPanel.setOption(ProcessingPanel.OPTION_STEREO, checkBoxState_selected_enabled);
            processingPanel.setOption(ProcessingPanel.OPTION_MULTI, checkBoxState_unselected_enabled);
        }
        processingPanel.setOption(ProcessingPanel.OPTION_DECOMPRESS,  checkBoxState_selected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_SELECTTRACKS, checkBoxState_unselected_enabled);
        processingPanel.setEnabledTextFieldTracks(false);
        processingPanel.setOption(ProcessingPanel.OPTION_NOPAD, checkBoxState_unselected_enabled);
        processingPanel.setOption(ProcessingPanel.OPTION_CUE, checkBoxState_unselected_enabled);
        outputPanel.setEnabledOutput1(true, "Output directory ");
        outputPanel.setEnabledOutput2(false, "");
    }

    public void radButtonDSDIFF(){
        if(processingPanel.getOption(ProcessingPanel.OPTION_STEREO).enabled == false || processingPanel.getOption(ProcessingPanel.OPTION_MULTI).enabled == false){
            processingPanel.setOption(ProcessingPanel.OPTION_STEREO, checkBoxState_selected_enabled);
            processingPanel.setOption(ProcessingPanel.OPTION_MULTI, checkBoxState_unselected_enabled);
        }
        processingPanel.setOption(ProcessingPanel.OPTION_DECOMPRESS,  checkBoxState_unselected_enabled);
        processingPanel.setOption(ProcessingPanel.OPTION_SELECTTRACKS, checkBoxState_unselected_enabled);
        processingPanel.setEnabledTextFieldTracks(false);
        processingPanel.setOption(ProcessingPanel.OPTION_NOPAD, checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_CUE, checkBoxState_unselected_enabled);
        outputPanel.setEnabledOutput1(true, "Output directory ");
        outputPanel.setEnabledOutput2(false, "");
    }

    public void radButtonDSDIFFEM(){
        if(processingPanel.getOption(ProcessingPanel.OPTION_STEREO).enabled == false || processingPanel.getOption(ProcessingPanel.OPTION_MULTI).enabled == false){
            processingPanel.setOption(ProcessingPanel.OPTION_STEREO, checkBoxState_selected_enabled);
            processingPanel.setOption(ProcessingPanel.OPTION_MULTI, checkBoxState_unselected_enabled);
        }
        processingPanel.setOption(ProcessingPanel.OPTION_DECOMPRESS,  checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_SELECTTRACKS, checkBoxState_unselected_disabled);
        processingPanel.setEnabledTextFieldTracks(false);
        processingPanel.setOption(ProcessingPanel.OPTION_NOPAD, checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_CUE, checkBoxState_selected_disabled);
        outputPanel.setEnabledOutput1(true, "Output directory ");
        outputPanel.setEnabledOutput2(false, "");
    }

    public void radButtonISO(){
        processingPanel.setOption(ProcessingPanel.OPTION_STEREO, checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_MULTI, checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_DECOMPRESS,  checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_SELECTTRACKS, checkBoxState_unselected_disabled);
        processingPanel.setEnabledTextFieldTracks(false);
        processingPanel.setOption(ProcessingPanel.OPTION_NOPAD, checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_CUE, checkBoxState_unselected_enabled);
        outputPanel.setEnabledOutput1(true, "Output directory ");
        outputPanel.setEnabledOutput2(false, "");
    }

    public void radButtonISO_DSF(){
        if(processingPanel.getOption(ProcessingPanel.OPTION_STEREO).enabled == false || processingPanel.getOption(ProcessingPanel.OPTION_MULTI).enabled == false){
            processingPanel.setOption(ProcessingPanel.OPTION_STEREO, checkBoxState_selected_enabled);
            processingPanel.setOption(ProcessingPanel.OPTION_MULTI, checkBoxState_unselected_enabled);
        }
        processingPanel.setOption(ProcessingPanel.OPTION_DECOMPRESS, checkBoxState_selected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_SELECTTRACKS, checkBoxState_unselected_enabled);
        processingPanel.setEnabledTextFieldTracks(false);
        processingPanel.setOption(ProcessingPanel.OPTION_NOPAD, checkBoxState_unselected_enabled);
        processingPanel.setOption(ProcessingPanel.OPTION_CUE, checkBoxState_unselected_enabled);
        outputPanel.setEnabledOutput1(true, "ISO output directory ");
        outputPanel.setEnabledOutput2(true, "DSF output directory ");
    }

    public void radButtonISO_DSDIFF(){
        if(processingPanel.getOption(ProcessingPanel.OPTION_STEREO).enabled == false || processingPanel.getOption(ProcessingPanel.OPTION_MULTI).enabled == false){
            processingPanel.setOption(ProcessingPanel.OPTION_STEREO, checkBoxState_selected_enabled);
            processingPanel.setOption(ProcessingPanel.OPTION_MULTI, checkBoxState_unselected_enabled);
        }
        processingPanel.setOption(ProcessingPanel.OPTION_DECOMPRESS, checkBoxState_unselected_enabled);
        processingPanel.setOption(ProcessingPanel.OPTION_SELECTTRACKS, checkBoxState_unselected_enabled);
        processingPanel.setEnabledTextFieldTracks(false);
        processingPanel.setOption(ProcessingPanel.OPTION_NOPAD, checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_CUE, checkBoxState_unselected_enabled);
        outputPanel.setEnabledOutput1(true, "ISO output directory ");
        outputPanel.setEnabledOutput2(true, "DSDIFF output directory ");
    }

    public void radButtonNone(){
        processingPanel.setOption(ProcessingPanel.OPTION_STEREO, checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_MULTI, checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_DECOMPRESS, checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_SELECTTRACKS, checkBoxState_unselected_disabled);
        processingPanel.setEnabledTextFieldTracks(false);
        processingPanel.setOption(ProcessingPanel.OPTION_NOPAD, checkBoxState_unselected_disabled);
        processingPanel.setOption(ProcessingPanel.OPTION_CUE, checkBoxState_unselected_enabled);
        processingPanel.setOption(ProcessingPanel.OPTION_PRINT, checkBoxState_selected_enabled);
        outputPanel.setEnabledOutput1(false, "Output directory ");
        outputPanel.setEnabledOutput2(false, "");
    }

    public void checkBoxStereo(CheckBoxState s){
        // Make sure stereo or multi is checked
        if(s.selected == false){
            processingPanel.setOption(ProcessingPanel.OPTION_MULTI, checkBoxState_selected_enabled);
        }
    }

    public void checkBoxMulti(CheckBoxState s){
        // Make sure stereo or multi is checked
        if(s.selected == false){
            processingPanel.setOption(ProcessingPanel.OPTION_STEREO, checkBoxState_selected_enabled);
        }
    }

    public void checkBoxSelectTracks(CheckBoxState s){
        if(s.selected){
            processingPanel.setEnabledTextFieldTracks(true);
            processingPanel.setOption(ProcessingPanel.OPTION_NOPAD, checkBoxState_unselected_disabled);
        }
        else{
            processingPanel.setEnabledTextFieldTracks(false);
            if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_DSF) || processingPanel.getRadButtonState(ProcessingPanel.PROCESS_ISO_DSF)){
                processingPanel.setOption(ProcessingPanel.OPTION_NOPAD, checkBoxState_unselected_enabled);
            }
        }
    }

    public void checkBoxNoPad(CheckBoxState s){
        if(s.selected){
            processingPanel.setOption(ProcessingPanel.OPTION_SELECTTRACKS, checkBoxState_unselected_disabled);
            processingPanel.setEnabledTextFieldTracks(false);
        }
        else{
            processingPanel.setOption(ProcessingPanel.OPTION_SELECTTRACKS, checkBoxState_unselected_enabled);
        }
    }

    public void checkBoxCue(CheckBoxState s){
        if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_NONE)){
            CheckBoxState checkBoxState_print = new CheckBoxState();
            // Make sure stereo or multi is checked
            if(s.selected == false){
                processingPanel.setSelectedCheckBoxPrint(checkBoxState_selected_enabled);
            }
            outputPanel.setEnabledOutput1(s.selected, "Output directory");
        }
    }

    public void checkBoxPrint(CheckBoxState s){
        if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_NONE)){
            if(s.selected == false){
                processingPanel.setSelectedCheckBoxCue(checkBoxState_selected_enabled);
                outputPanel.setEnabledOutput1(true, "Output directory");
            }
        }
    }

    public void radButton_server(){
        inputPanel.setEnabledTextFieldsAddress(true);
        inputPanel.setEnabledTextFieldPath(false);
        inputPanel.setEnabledButtonBrowse(false);
        inputPanel.setEnabledButtonPing(true);
        inputPanel.setEnabledButtonTest(true);
        inputPanel.setEnabledListFiles(false);
        inputPanel.setEnabledButtonAdd(false);
        inputPanel.setEnabledButtonRemove(false);
    }

    public void radButton_file(){
        inputPanel.setEnabledTextFieldsAddress(false);
        inputPanel.setEnabledTextFieldPath(true);
        inputPanel.setEnabledButtonBrowse(true);
        inputPanel.setEnabledButtonPing(false);
        inputPanel.setEnabledButtonTest(false);
        inputPanel.setEnabledListFiles(false);
        inputPanel.setEnabledButtonAdd(false);
        inputPanel.setEnabledButtonRemove(false);
    }

    public void radButton_files(){
        inputPanel.setEnabledTextFieldsAddress(false);
        inputPanel.setEnabledTextFieldPath(false);
        inputPanel.setEnabledButtonBrowse(false);
        inputPanel.setEnabledButtonPing(false);
        inputPanel.setEnabledButtonTest(false);
        inputPanel.setEnabledListFiles(true);
        inputPanel.setEnabledButtonAdd(true);
        inputPanel.setEnabledButtonRemove(true);
    }

    public void buttonAdd(DefaultListModel<String> l){
    }

    public void buttonRemove(int [] i){
        inputPanel.removeFiles(i);
    }

    public void buttonProgramTest(){
        doProgramTest(programPanel.getTextTextFieldProgram());
    }

    public void buttonRun(){
        doRun();
    }

    public void buttonStop(){
        doExecStop();
    }

    public void killExec(){
        doExecStop();
    }

    public void checkBoxOutputdir1(boolean s){
        outputPanel.setEnabledTextFieldOutput1(s);
        outputPanel.setEnabledButtonOutput1(s);
    }

    public void checkBoxOutputdir2(boolean s){
        outputPanel.setEnabledTextFieldOutput2(s);
        outputPanel.setEnabledButtonOutput2(s);
    }

    public void buttonPing(){
        doPing(getAddress());
    }

    public void buttonServerTest(){
        String sAddress, sPort;
        sAddress = getAddress();
        sPort = getPort();
        if(sAddress == null || sPort == null){
            printLog(String.format("[ERROR] Incomplete server IP address or port."));
            return;
        }
        doTest(sAddress, sPort);
    }

    private void doRun(){
        execThread = new ExecThread(this);
        String stringProgram = programPanel.getTextTextFieldProgram();
        File fileProgram = new File(stringProgram);
        CheckBoxState checkBoxState;
        // Check the sacd_extract executable
        if(!fileProgram.canExecute()){
            printLog(String.format("[ERROR] %s doesn't exist or not executable.", stringProgram));
            return;
        }

        // Check the input
        if(inputPanel.getRadButtonStateFiles()){
            ArrayList<String> files;
            files = inputPanel.getFiles();
            boolean go = true;

            if(files.size() < 1){
                printLog("[ERROR] Input file list is empty.");
                return;
            }

            for(String s : files){
                File f = new File(s);
                if(!f.canRead()){
                    printLog(String.format("[ERROR] Input file \"%s\" doesn't exist or is not readable.", s));
                    go = false;
                }
            }
            if(!go){
                return;
            }
        }

        if(inputPanel.getRadButtonStateFile()){
            String stringFile = inputPanel.getTextTextFieldPath();
            File file = new File(stringFile);
            if(!file.canRead()){
                printLog(String.format("[ERROR] Input file \"%s\" doesn't exist or is not readable.", stringFile));
                return;
            }
        }

        if(inputPanel.getRadButtonStateServer()){
            if(getAddress() == null || getPort() == null){
                printLog(String.format("[ERROR] Incomplete server IP address or port."));
                return;
            }
        }

        // Check the output directories
        checkBoxState = outputPanel.getCheckBoxStateOutput1();
        if(checkBoxState.enabled && checkBoxState.selected){
            String fileString = outputPanel.getTextOutput1();
            File f = new File(fileString);
            if(!f.canWrite()){
                printLog(String.format("[ERROR] Output directory \"%s\" doesn't exist or is not writable.", fileString));
                return;
            }
        }
        checkBoxState = outputPanel.getCheckBoxStateOutput2();
        if(checkBoxState.enabled && checkBoxState.selected){
            String fileString = outputPanel.getTextOutput2();
            File f = new File(fileString);
            if(!f.canWrite()){
                printLog(String.format("[ERROR] Output directory \"%s\" doesn't exist or is not writable.", fileString));
                return;
            }
        }
        
        execThread.setTextArea(outputPanel.textArea);
        ExecButtonSetEnabled(false);
        execThread.setCommands(getCommands());
        execThread.start();
    }
    
    private void doExecStop(){
        if(execThread != null){
            if(execThread.isAlive()){
                execThread.requestStop();
                try{
                    execThread.join();
                }
                catch(Exception e){
                }
            }
        }
    }

    private void doProgramTest(String program){
        execThread = new ExecThread(this);
        ArrayList<String> cmd = new ArrayList<String>(Arrays.asList(program, "-v"));
        String stringProgram = programPanel.getTextTextFieldProgram();
        File fileProgram = new File(stringProgram);
        // Check the sacd_extract executable
        if(!fileProgram.canExecute()){
            printLog(String.format("[ERROR] %s doesn't exist or not executable.", stringProgram));
            return;
        }
        ExecButtonSetEnabled(false);
        execThread.setCommand(cmd);
        execThread.start();
    }

    private void doTest(String address, String port){
        Socket socket;
        SocketAddress sockaddr;

        try{
            socket = new Socket();
            sockaddr = new InetSocketAddress(InetAddress.getByName(address), Integer.parseInt(port));
            socket.connect(sockaddr, 1500);
            socket.close();
            printLog(String.format("[SUCCESS] Port %s of %s is accessible.", port, address));
        }
        catch(Exception e){
            printLog(String.format("[FAILURE] Port %s of %s is inaccessible.", port, address));
        }
    }

    private void doPing(String address){
        execThread = new ExecThread(this);
        ArrayList<String> cmd;

        if(WIN32){
            cmd = new ArrayList<String>(Arrays.asList("ping", "-n", "3", "-w", "1", address));
        }
        else{
            cmd = new ArrayList<String>(Arrays.asList("ping", "-c", "3", "-W", "1", address));
        }

        if(getAddress() == null){
                printLog(String.format("[ERROR] Incomplete server IP address."));
                return;
        }
        ExecButtonSetEnabled(false);
        execThread.setCommand(cmd);
        execThread.start();
    }

    private String getAddress(){
        int i;
        String stringAddress0 = inputPanel.getTextTextFieldAddress0();
        String stringAddress1 = inputPanel.getTextTextFieldAddress1();
        String stringAddress2 = inputPanel.getTextTextFieldAddress2();
        String stringAddress3 = inputPanel.getTextTextFieldAddress3();

        try{
            i = Integer.parseInt(stringAddress0); 
            if(i <= 0 || i > 255){
                return null;
            }
            i = Integer.parseInt(stringAddress1); 
            if(i < 0 || i > 255){
                return null;
            }
            i = Integer.parseInt(stringAddress2); 
            if(i < 0 || i > 255){
                return null;
            }
            i = Integer.parseInt(stringAddress3); 
            if(i <= 0 || i > 255){
                return null;
            }
        }
        catch(NumberFormatException e){
            return null;
        }
        return stringAddress0 + "." + stringAddress1 + "." + stringAddress2 + "." + stringAddress3;
    }

    private String getPort(){
        int i;
        String stringPort = inputPanel.getTextTextFieldAddressPort();

        try{
             i = Integer.parseInt(stringPort); 
            if(i <= 0 || i > 65535){
                return null;
            }
        }
        catch(NumberFormatException e){
            return null;
        }
        
        return stringPort;
    }

    private ArrayList<ArrayList<String>> getCommands(){
        ArrayList<ArrayList<String>> cmds = new ArrayList<ArrayList<String>>();
        if(inputPanel.getRadButtonStateFiles()){
            // Multiple files
            for(String s : inputPanel.getFiles()){
                cmds.add(getCommand(s));
            }
        }
        else{
            // Single file or IP address
            cmds.add(getCommand(null));
        }
        return cmds;
    }

    private ArrayList<String> getCommand(String extfile){
        String addressPort;
        String program;
        String str;
        ArrayList<String> cmd = new ArrayList<String>(); 
        CheckBoxState checkBoxState;
        boolean stereo, multi, decompress, selectTracks, nopad, cue, print;
        int process = ProcessingPanel.PROCESS_NONE;
        String inputFile;
        boolean server, files;
        String output1Text, output2Text;
        boolean output1Enabled, output2Enabled;

        program = programPanel.getTextTextFieldProgram();
        addressPort = inputPanel.getTextTextFieldAddress0() + "." + inputPanel.getTextTextFieldAddress1() + "." + inputPanel.getTextTextFieldAddress2() + "." + inputPanel.getTextTextFieldAddress3() + ":" + inputPanel.getTextTextFieldAddressPort();
        inputFile = inputPanel.getTextTextFieldPath();
        server = inputPanel.getRadButtonStateServer();
        files = inputPanel.getRadButtonStateFiles();

        if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_DSF)){
            process = ProcessingPanel.PROCESS_DSF;
        }
        if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_DSDIFF)){
            process = ProcessingPanel.PROCESS_DSDIFF;
        }
        if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_DSDIFFEM)){
            process = ProcessingPanel.PROCESS_DSDIFFEM;
        }
        if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_ISO)){
            process = ProcessingPanel.PROCESS_ISO;
        }
        if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_ISO_DSF)){
            process = ProcessingPanel.PROCESS_ISO_DSF;
        }
        if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_ISO_DSDIFF)){
            process = ProcessingPanel.PROCESS_ISO_DSDIFF;
        }
        if(processingPanel.getRadButtonState(ProcessingPanel.PROCESS_NONE)){
            process = ProcessingPanel.PROCESS_NONE;
        }

        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_STEREO); stereo = checkBoxState.selected; 
        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_MULTI); multi = checkBoxState.selected; 
        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_DECOMPRESS); decompress = checkBoxState.selected; 
        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_SELECTTRACKS); selectTracks = checkBoxState.selected; 
        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_NOPAD); nopad = checkBoxState.selected; 
        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_CUE); cue = checkBoxState.selected; 
        checkBoxState = processingPanel.getOption(ProcessingPanel.OPTION_PRINT); print = checkBoxState.selected; 
        checkBoxState = outputPanel.getCheckBoxStateOutput1(); output1Enabled = checkBoxState.selected; 
        output1Text = outputPanel.getTextOutput1();
        output2Text = outputPanel.getTextOutput2();
        checkBoxState = outputPanel.getCheckBoxStateOutput2(); output2Enabled = checkBoxState.selected; 
        cmd.add(program); cmd.add("-i");
        if(server){
            cmd.add(addressPort);
        }
        else if(files){
            cmd.add(extfile);
        }
        else{
            cmd.add(inputFile);
        }

        switch(process){
            case ProcessingPanel.PROCESS_DSF:
                cmd.add("-s");
                if(nopad){
                    cmd.add("-z");
                }
                break;
            case ProcessingPanel.PROCESS_DSDIFF:
                cmd.add("-p");
                if(decompress){
                    cmd.add("-c");
                }
                break;
            case ProcessingPanel.PROCESS_DSDIFFEM:
                cmd.add("-e");
                break;
            case ProcessingPanel.PROCESS_ISO:
                cmd.add("-I");
                break;
            case ProcessingPanel.PROCESS_ISO_DSF:
                cmd.add("-w"); cmd.add("-I"); cmd.add("-s");
                if(nopad){
                    cmd.add("-z");
                }
                break;
            case ProcessingPanel.PROCESS_ISO_DSDIFF:
                cmd.add("-w"); cmd.add("-I"); cmd.add("-p");
                if(decompress){
                    cmd.add("-c");
                }
                break;
            case ProcessingPanel.PROCESS_NONE:
                break;
        }

        if(stereo){
            cmd.add("-2");
        }

        if(multi){
            cmd.add("-m");
        }

        if(cue){
            cmd.add("-C");
        }

        if(print){
            cmd.add("-P");
        }

        if(selectTracks){
            cmd.add("-t");
            cmd.add(processingPanel.getTextTextFieldTracks());
        }

        if(output1Enabled){
            cmd.add("-o");
            cmd.add(output1Text);
        }

        if(output2Enabled){
            cmd.add("-y");
            cmd.add(output2Text);
        }

        return cmd;
    }

    public void initGui(){
    }
    
    public GuiControl(){
    }
}
