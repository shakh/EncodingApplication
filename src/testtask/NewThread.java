package testtask;

import javax.swing.JTextArea;

public class NewThread extends Thread {

    private ProgressListener listener;
    JTextArea textArea;
    private String prevProgress = "";

    public NewThread(ProgressListener listener, JTextArea textArea){
        this.listener = listener;
        this.textArea = textArea;
    }

    public void run(){
    	while(true){
    		String text = textArea.getText();
    		String progress = listener.getProgress();
    		text += "\n" + progress;
    		if (progress != null){
    			if (!progress.equals(prevProgress)){
    				//textArea.setText(text);
    				prevProgress = progress;
    			}
    		}
    		//System.out.println(progress);
    	}
    }

}
