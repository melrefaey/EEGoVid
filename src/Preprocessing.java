import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	  loadLibrary();
	  final VideoCapture vc = new VideoCapture();
	  
	  if (args.length == 0){
		  System.out.println("INVALID INPUT");
	  }
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
		
		  
		  for (int j=0; j<super2dArray.size(); j++){
			  final boolean result = vc.open("videos/"+super2dArray.get(j).get(0)+".mp4");
		      if(!result) System.out.println("There was an error loading the file "+super2dArray.get(j).get(0)+".mp4");
		      double[] dataArray = new double[super2dArray.get(j).size()-1];
		      double maximumInterest = 0;
		      int timePoint = 0;
		      for(int i=1; i<super2dArray.get(j).size()-1; i++){
		    	  
	    		  dataArray[i-1]= Double.parseDouble(super2dArray.get(j).get(i).trim());
	    		  //System.out.println(dataArray[i-1]);
	    		  //System.out.println(maximumInterest);
	    		  if(dataArray[i-1]>maximumInterest){
	    			  maximumInterest=dataArray[i-1];
	    			  timePoint = i-1;
	    			  //System.out.println(timePoint);
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
      /*
      try{
      while (vc.retrieve(newMat)){
    	  BufferedImage image1=new BufferedImage(newMat.cols(),newMat.rows()
        	      ,BufferedImage.TYPE_BYTE_GRAY);
    	  byte[] data1 = new byte[newMat.rows()*newMat.cols()*(int)(newMat.elemSize())];
    	  image1.getRaster().setDataElements(0,0,newMat.cols(),newMat.rows(),data1);
    	  File ouptut = new File("grayscale"+i+".jpg");
          ImageIO.write(image1, "jpg", ouptut);
          System.out.println(result);
          i++;
      }
      }  
      catch (Exception e) {
	         System.out.println("Error: " + e.getMessage());
	      }  
      */
      
      {/*
    	   try {
    	      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
    	      File input = new File("advertisement1.mp4");
    	      BufferedImage
    	      image = ImageIO.read(input);	

    	      byte[] data = ((DataBufferByte) image.getRaster().
    	      getDataBuffer()).getData();
    	      Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
    	      mat.put(0, 0, data);

    	      Mat mat1 = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC1);
    	      Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);

    	      byte[] data1 = new byte[mat1.rows()*mat1.cols()*(int)(mat1.elemSize())];
    	      mat1.get(0, 0, data1);
    	      BufferedImage image1=new BufferedImage(mat1.cols(),mat1.rows()
    	      ,BufferedImage.TYPE_BYTE_GRAY);
    	      image1.getRaster().setDataElements(0,0,mat1.cols(),mat1.rows(),data1);

    	      File ouptut = new File("grayscale.jpg");
    	      ImageIO.write(image1, "jpg", ouptut);
    	      } catch (Exception e) {
    	         System.out.println("Error: " + e.getMessage());
    	      }
    	  
      */
      }
   
   
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