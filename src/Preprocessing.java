/*************************************************************
 * Copyright 2014 Vincent Rubino
 * 
 * This code works to pre-process input for the EEGoVid project
 * In general, this code takes in videos in mp4 format and extracts
 * 10 frames from a point of interest and then saves those images 
 * to jpeg.
 * 
 * Here, the preprocessing code has several optional command line arugments
 * 
 * 1.)		filename.mp4
 * 
 * When given just a filename, the preprocessing program takes in the name of 
 * the mp4 video and extracts a jpeg file at every second of the video
 * 
 * 2.)		filename.mp4 timepoint1 timepoint2 ... etc.
 * 
 * When given timepoints, the preprocessing program takes in the name of the 
 * mp4 video and extracts 10 frames from immediately before each of the timepoints.
 * For example if the input is a 23 fps video, and "10" is given as an input,
 * the frames extracted will be frames 221-230
 * 
 * 3.)		-csv filename.csv
 * 
 * The third option is to take in a CSV file that includes the following information:
 * videoID, interestlevel1, interestlevel2, etc
 * 
 * Where videoID is the filename of the video, and interestlevel1 through interestleveln 
 * are the interest levels at each time sampling point.  Here the time samples are assumed
 * to be every second.
 * 
 * The current implementation finds the point of greatest "interest" based on the interest values
 * and then extracts 10 frames from immediately prior to this maximum interest.
 * 
 * The input videos are expected to be in a video/ directory
 * 
 * The images for the csv funtionality will be generated in the outputimages/ directory.
 * 
 * The output images follow the following naming convention:
 * 
 * XimgY.jpg where X is the name/number of the input video, and Y is the Yth image from the 10 
 * frames.  Y is not the actual frame from the video.  For example, if the video ID is "1", and if
 * FPS is 23, and the maximum interest is at 10 seconds, frames 221-230 will be captured and 
 * named 1img1.jpg through 1img10.jpg.
 * 
 *************************************************************/

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;


public class Preprocessing{

	public static void main( String[] args ) throws IOException{
		
		//load the library
		loadLibrary();
		
		final VideoCapture vc = new VideoCapture();
		
		//check for valid input
		if (args.length == 0){
			System.out.println("INVALID INPUT");
		}
		
		//case for video name input.  the output will be jpg files at each second.
		else if (args.length == 1){
			final boolean result = vc.open(args[0]);
			if(!result) System.out.println("There was an error loading the file");
			Mat newMat = new Mat();
			int fps = (int) vc.get(5);
			int frames = (int) vc.get(7);
			System.out.println("FPS = "+fps);
			System.out.println("Total Frames = "+frames);
			double time = frames/fps;
			System.out.println("Total Time = "+time);
			for (int j = 1; j<=time; j++){
				vc.read(newMat);
				vc.set(1, j*23);
				Highgui.imwrite("img"+j+".jpg", newMat);
			}
		}
		
		//if time points are specified - grab 10 frames around each timepoint
		else if (args.length > 1  && !args[0].equalsIgnoreCase("-csv")){
			final boolean result = vc.open(args[0]);
			if(!result) System.out.println("There was an error loading the file");
			else{
				for(int i=1; i<args.length; i++){
					double timePoint = Double.parseDouble(args[i].trim());
					Mat newMat = new Mat();
					int fps = (int) vc.get(5);
					int frames = (int) vc.get(7);
					double time = frames/fps;
					int startFrame = ((int)timePoint)*fps - 10;
					System.out.println("FPS = "+fps);
					System.out.println("Total Frames = "+frames);
					System.out.println("Running Time = "+time);
					System.out.println("Starting Capture at Frame "+startFrame);
					vc.set(1, startFrame);
					for (int j = 1; j<=10; j++){
						vc.read(newMat);
						Highgui.imwrite("img"+startFrame+j+".jpg", newMat);
					}
				}
			}
		}
		
		//if a CSV file is specified, input the CSV, calculate maximum
		//engagement and extract 10 jpeg frames from immediately before the
		//time of maximum engagement
		else if (args.length > 1  && args[0].equalsIgnoreCase("-csv")){
			FileReader fr = new FileReader(args[1]);
			BufferedReader br = new BufferedReader(fr);
			ArrayList<ArrayList<String>> super2dArray = new ArrayList<ArrayList<String>>();
			String newString = "";
			while((newString = br.readLine())!=null){
				String tempStr[] = newString.split(",");
				ArrayList<String> tempString = new ArrayList<String>();
				for(int i = 0; i< tempStr.length; i++){
					tempString.add(tempStr[i]);
				}
				System.out.println(tempString);
				super2dArray.add(tempString);
			}
			System.out.println(super2dArray.size());
			
			//make CSV file of avg ratings
			ArrayList<ArrayList<String>> ratingsOutput = new ArrayList<ArrayList<String>>();
			for(int i=0; i<super2dArray.size(); i++){
				double avgRating = 0;
				int counter = 0;
				double cumulative = 0;
				for(int j=1; j<super2dArray.get(i).size(); j++){
					if(Double.parseDouble(super2dArray.get(i).get(j).trim())>0){
						counter++;
						cumulative += Double.parseDouble(super2dArray.get(i).get(j).trim());
					}
				}
				avgRating = cumulative/counter;
				ArrayList<String> tempString1 = new ArrayList<String>();
				tempString1.add(super2dArray.get(i).get(0));
				tempString1.add(Double.toString(avgRating));
				ratingsOutput.add(tempString1);
			}
			//recalculate on 1 to 5 scale
			//first find highest number
			double max = 0;
		    for(int j=0; j<ratingsOutput.get(1).size(); j++){
		    		ratingsOutput.get(1).get(j);
		    		if(Double.parseDouble(ratingsOutput.get(1).get(j)) > max){
		    			max = Double.parseDouble(ratingsOutput.get(1).get(j));
		    			System.out.println("The max is " + max);
		    	}
		    }
		    for(int j=0; j<ratingsOutput.size(); j++){
	    		System.out.println("Got here");
	    		double tempDouble = 5*Double.parseDouble(ratingsOutput.get(j).get(1).trim())/max;
	    		tempDouble = tempDouble*10;
	    		double tempDouble2 = (double)Math.round(tempDouble);
	    		System.out.println(tempDouble);
	    		System.out.println(tempDouble2);
	    		ratingsOutput.get(j).set(1, Double.toString(tempDouble2));
	    	}
	    
		    
			
			
			//write ratings to a file
			File file = new File("ratings.csv");
		    file.createNewFile();
		    FileWriter writer = new FileWriter(file); 
		    for(int i=0; i<ratingsOutput.size(); i++){
		    	for(int j=0; j<ratingsOutput.get(i).size(); j+=2){
		    		int r1 = (int) (Math.random()*90000)+10000;
		    		writer.write(r1+","+ratingsOutput.get(i).get(j)+","+ratingsOutput.get(i).get(j+1)+"\n"); 
		    	}
		    }
		    writer.flush();
    		writer.close();
			
			
			for (int j=0; j<super2dArray.size(); j++){
				final boolean result = vc.open("videos/"+super2dArray.get(j).get(0)+".mp4");
				if(!result) System.out.println("There was an error loading the file "+super2dArray.get(j).get(0)+".mp4");
				double[] dataArray = new double[super2dArray.get(j).size()-1];
				double maximumInterest = 0;
				int timePoint = 0;
				for(int i=1; i<super2dArray.get(j).size()-1; i++){

					dataArray[i-1]= Double.parseDouble(super2dArray.get(j).get(i).trim());
					if(dataArray[i-1]>maximumInterest){
						maximumInterest=dataArray[i-1];
						timePoint = i-1;
					}
				}
				int num = j+1;
				System.out.println("***************************");
				System.out.println("Video Number "+num);
				Mat newMat = new Mat();
				int fps = (int) vc.get(5);
				int frames = (int) vc.get(7);
				double time = frames/fps;
				int startFrame = ((int)timePoint)*fps - 10;
				System.out.println("Processing Started on "+super2dArray.get(j).get(0)+".mp4");
				System.out.println("FPS = "+fps);
				System.out.println("Total Frames = "+frames);
				System.out.println("Running Time = "+time);
				System.out.println("Starting Capture at Frame "+startFrame);
				System.out.println("Starting Timepoint is "+timePoint+" seconds");
				vc.set(1, startFrame);
				for (int k = 1; k<=10; k++){
					vc.read(newMat);
					Highgui.imwrite("outputimages/"+super2dArray.get(j).get(0)+"img"+k+".jpg", newMat);
				}
			}
			fr.close();
		}
	}
	
	//LoadLibrary loads the dll or dylib files necessary for opencv to operate
	public static void loadLibrary(){
		try {
			InputStream in = Preprocessing.class.getResourceAsStream("libopencv_java249.dylib");
			File fileOut = File.createTempFile("lib", ".dylib");
			OutputStream out = FileUtils.openOutputStream(fileOut);
			IOUtils.copy(in, out);
			in.close();
			out.close();
			System.load(fileOut.toString());
		} catch (Exception e) {
			throw new RuntimeException("Failed to load opencv native library", e);
		}
	} 
}