class SACDExtractGUI{
    public static void main(String args[]){
        if(OSDetect.isMac()){
            System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
        }
        AppFrame app = AppFrame.getInstance();
        app.setVisible(true);
    }
}

