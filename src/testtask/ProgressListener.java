package testtask;

import it.sauronsoftware.jave.EncoderProgressListener;
import it.sauronsoftware.jave.MultimediaInfo;

public class ProgressListener implements EncoderProgressListener{
	private String progress;

	@Override
	public void sourceInfo(MultimediaInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void progress(int permil) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void message(String message){
		this.progress = message;
	}	
	public String getProgress() {
		return this.progress;
	}
		
}
