package com.mrlukasbos;
/* 
 * Lukas Bos, may 2017
 * Bachelor Project for Creative Technology, the University of Twente, NL
 * In thankful collaboration with Indes B.V., Enschede, The Netherlands
 */

import com.fazecast.jSerialComm.*;
import javax.swing.JFrame;

public class SimpleRead extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static CustomCanvas canvas;
	private static LidarPoint[] lidarPoints;
	
	// Constants
	private static final int SCREENHEIGHT = 800;
	private static final int SCREENWIDTH = 1200;
	private static final int BAUDRATE = 115200;
	private static final int SERIALPORT = 4;
	
    public SimpleRead() {    
    	lidarPoints = new LidarPoint[200];
    	
    	// Initialize all distances to 0
    	for (LidarPoint point : lidarPoints) { point = new LidarPoint(); }
    	
    	canvas = new CustomCanvas(SCREENHEIGHT, SCREENWIDTH);
        add("Center", canvas);
        setSize(SCREENWIDTH, SCREENHEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
	
    public static void main(String[] args) {
    	SimpleRead app = new SimpleRead();
    	SerialPort[] ports = SerialPort.getCommPorts();
    	
    	// Logs available serial ports in console
    	logPorts(ports);
    	
    	// Connect to port
    	SerialPort port = ports[SERIALPORT];
    	port.setBaudRate(BAUDRATE);
    	port.openPort();

    	try {
    		while (true) { // main loop
    			
		      while (port.bytesAvailable() == 0) {
		         Thread.sleep(40);
		      }
		      
		      byte[] readBuffer = new byte[port.bytesAvailable()];
		      port.readBytes(readBuffer, readBuffer.length); // log this statement to get amount of bytes received.
		      String text = new String(readBuffer);
		      String lines[] = text.split("\\r?\\n");
		      
		      for (int i = 0; i < lines.length; i++) {
			      int distance = 0;
			      int angle = 0;
				  try {
					  String line = lines[i].trim();
					  if (!line.isEmpty()) {
					      String[] splittedLine = line.split(",");					 
						  distance = Integer.parseInt(splittedLine[0]);
						  angle = Integer.parseInt(splittedLine[1]);
					  }
				  } catch(Exception e) {
				      System.out.println("Getting error, value is " + lines[i] );
				     // angle = 0;
					 // distance = 0 ;
				  }
			      
				  // Shift the whole array. Crucial to loop backwards
			      for (int j = lidarPoints.length - 2; j >= 0; j--) {                
			          lidarPoints[j+1] = lidarPoints[j];
			      }
			      lidarPoints[0] = new LidarPoint(distance, angle);
		      }
		      canvas.setlidarPoints(lidarPoints);
		      canvas.repaint();
		   }
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
    	
    	System.out.println("Program aborted");
		port.closePort();	
    }

    private static void logPorts(SerialPort[] ports) {   	
    	for(int i = 0; i < ports.length; i++) {
      		SerialPort port = ports[i];
      		System.out.println(i + " " + port.getSystemPortName() + "(" + port.getDescriptivePortName() + ")");
      	}
    }
}
