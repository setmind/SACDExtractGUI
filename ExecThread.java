import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

class ExecThread extends Thread {
    interface Callback{
        public void printLog(String s);
        public void printStatus(String s);
        public void processExit(boolean s);
    }
    
    JTextArea outputArea;
    private boolean stop = false;

    OutputPanel outputPanel;
    List<String> cmd = new ArrayList<String>();
    List<ArrayList<String>> cmds = new ArrayList<ArrayList<String>>();

    String line;
    Process process;
    ProcessBuilder pb;
    boolean die = false;
    private Callback callback;

    public ExecThread(Callback c){
        callback = c;
    }

    public void requestStop(){
        stop = true;
    }

    public void setTextArea(JTextArea t){
        outputArea = t;
    }

    public void setCommand(ArrayList<String> s){
        cmds.add(s);
    }
    
    public void setCommands(ArrayList<ArrayList<String>> s){
        cmds = s;
    }

    public void run(){
        BufferedReader reader;
        int i, n;
        n = cmds.size();
        i = 0;

        for(ArrayList<String> cmd : cmds){
            i ++;
            callback.printLog("[RUNNING]" + cmd + "\n");
            if(n > 1){
                callback.printStatus('(' + Integer.toString(i) + '/' + Integer.toString(n)+')');
            }
            else{
                callback.printStatus("");
            }


            pb = new ProcessBuilder(cmd);

            try {
                pb.redirectErrorStream(true);
                process = pb.start();
                reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                while((line = reader.readLine()) != null){
                    callback.printLog(line);

                    if(stop){
                        process.destroy();
                        cmds.clear();
                        callback.printLog("\n[TERMINATED]");
                        callback.processExit(false);
                        stop = false;
                        return;
                    }
                }
                reader.close();
            }
            catch(Exception e){
            }
        }
        cmds.clear();
        callback.processExit(true);
    }
}
