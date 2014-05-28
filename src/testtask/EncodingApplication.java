package testtask;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.VideoAttributes;
import it.sauronsoftware.jave.VideoSize;
import testtask.Chart;


import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class EncodingApplication {
	static File source = new File("source.yuv");
	static File target = new File("target.mkv");
	static File decodedTarget = new File("decodedTarget.yuv");
	static String [] yKey = {"PSNR Y","PSNR U","PSNR V"};
	VideoSize videoSize = null;
	
	public void startApplication(String option, ProgressListener listener){
		
		VideoAttributes video = new VideoAttributes();
		if (videoSize != null){
			video.setSourceSize(videoSize);//source frame size
		}
		switch (option) {
			case "presets":	encodePresets(video, listener);//encoding with different presets
							break;
			case "bitrates": encodeBitrates(video, listener);//encoding with different bitrates
						 	 break;
			case "decode": decode(listener);//decoding target to rawvideo
						   break;
			default: System.exit(-1);
		}
	}
	private void encodePresets(VideoAttributes attributes, ProgressListener listener){
		float fps,
		      psnrY,
		      psnrU,
		      psnrV;
		String currPreset;
		LinkedHashSet <String> presets = new LinkedHashSet<String>();		
		LinkedHashMap <String,Float> presetToPsnrY = new LinkedHashMap<String,Float>();
		LinkedHashMap <String,Float> presetToPsnrU = new LinkedHashMap<String,Float>();
		LinkedHashMap <String,Float> presetToPsnrV = new LinkedHashMap<String,Float>();
		Chart presetPsnrChart = new Chart();
		
		presets.add("slow");
		presets.add("Medium");
		presets.add("fast");
		presets.add("veryfast");	
		
		Iterator <String> preset= presets.iterator();
		while(preset.hasNext()){
			currPreset = preset.next();
			EncodingAttributes encodingAttributes = new EncodingAttributes();
			encodingAttributes.setPreset(currPreset);//Set encoding option -preset preset
			encodingAttributes.setVideoAttributes(attributes);//Set video attributes, e.g. source frame size
		
			Encoder encoder = new Encoder();
			try {
				encoder.encode(source, target, encodingAttributes, listener); //start encoding of source to target with attributes
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				listener.message(e.getMessage());
			} catch (InputFormatException e) {
				// TODO Auto-generated catch block
				listener.message(e.getMessage());
			} catch (EncoderException e) {
				// TODO Auto-generated catch block
				listener.message(e.getMessage());
			}
			//extract fps and PSNR from encoding process
			fps = encoder.getFps();
			psnrY = encoder.getPsnrY();
			psnrU = encoder.getPsnrU();
			psnrV = encoder.getPsnrV();
			//prepare dataset for a chart
			presetToPsnrY.put(currPreset, psnrY);
			presetToPsnrU.put(currPreset, psnrU);
			presetToPsnrV.put(currPreset, psnrV);
			presetPsnrChart.fillDataset(Double.valueOf(presetToPsnrY.get(currPreset)), "PSNR Y", currPreset);
			presetPsnrChart.fillDataset(Double.valueOf(presetToPsnrU.get(currPreset)), "PSNR U", currPreset);
			presetPsnrChart.fillDataset(Double.valueOf(presetToPsnrV.get(currPreset)), "PSNR V", currPreset);
		}
		//configure and show the chart
		presetPsnrChart.configurePlot("PSNR/preset", "preset", "PSNR", presets, yKey);
		presetPsnrChart.showChart();
	}
	
	private void encodeBitrates(VideoAttributes attributes, ProgressListener listener){
		float psnrY,
		  	  psnrU,
		      psnrV;
		String currBitrate;
	
		LinkedHashSet <String> bitrates = new LinkedHashSet<String>();
		LinkedHashMap <String,Float> bitrateToPsnrY = new LinkedHashMap<String,Float>();
		LinkedHashMap <String,Float> bitrateToPsnrU = new LinkedHashMap<String,Float>();
		LinkedHashMap <String,Float> bitrateToPsnrV = new LinkedHashMap<String,Float>();
		Chart bitratePsnrChart = new Chart();
		
		bitrates.add("1.5M");
		bitrates.add("2.5M");
		bitrates.add("4M");
		bitrates.add("6M");
		bitrates.add("10M");
		
		Iterator <String> bitrate= bitrates.iterator();
		while(bitrate.hasNext()){
			//VideoAttributes video = new VideoAttributes();
			currBitrate = bitrate.next();
			attributes.setBitRate(currBitrate);//Set video bitrate, -b option
			EncodingAttributes encodingAttributes = new EncodingAttributes();
			encodingAttributes.setVideoAttributes(attributes);//Set video attributes, e.g. source frame size
			Encoder encoder = new Encoder();
			try {
				encoder.encode(source, target, encodingAttributes, listener);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				listener.message(e.getMessage());
			} catch (InputFormatException e) {
				// TODO Auto-generated catch block
				listener.message(e.getMessage());
			} catch (EncoderException e) {
				// TODO Auto-generated catch block
				listener.message(e.getMessage());
			}
			//extract PSNR from encoding process
			psnrY = encoder.getPsnrY();
			psnrU = encoder.getPsnrU();
			psnrV = encoder.getPsnrV();
			//prepare dataset for a chart
			bitrateToPsnrY.put(currBitrate, psnrY);
			bitrateToPsnrU.put(currBitrate, psnrU);
			bitrateToPsnrV.put(currBitrate, psnrV);
			bitratePsnrChart.fillDataset(Double.valueOf(bitrateToPsnrY.get(currBitrate)), "PSNR Y", currBitrate);
			bitratePsnrChart.fillDataset(Double.valueOf(bitrateToPsnrU.get(currBitrate)), "PSNR U", currBitrate);
			bitratePsnrChart.fillDataset(Double.valueOf(bitrateToPsnrV.get(currBitrate)), "PSNR V", currBitrate);
		}
		//configure and show the chart
		bitratePsnrChart.configurePlot("PSNR/bitrate", "bitrate", "PSNR", bitrates, yKey);
		bitratePsnrChart.showChart();
	}
	
	private void decode(ProgressListener listener){
		EncodingAttributes decodingAttributes = new EncodingAttributes();
		decodingAttributes.setFormat("rawvideo");
		Encoder decoder = new Encoder();
		try {
			decoder.encode(source, decodedTarget, decodingAttributes, listener);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			listener.message(e.getMessage());
		} catch (InputFormatException e) {
			// TODO Auto-generated catch block
			listener.message(e.getMessage());
		} catch (EncoderException e) {
			// TODO Auto-generated catch block
			listener.message(e.getMessage());
		}
	}
	public void setVideoSize(int width, int height){
		this.videoSize = new VideoSize(width,height);
	}
}
