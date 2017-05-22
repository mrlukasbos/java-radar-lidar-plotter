package com.mrlukasbos;
/* 
 * Lukas Bos, may 2017
 * Bachelor Project for Creative Technology, the University of Twente, NL
 * In thankful collaboration with Indes B.V., Enschede, The Netherlands
 */

import com.fazecast.jSerialComm.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;

public class SimpleRead extends JFrame {
	private static MyCanvas canvas;
	private static DistanceWithAngle[] DWAs;
	
	// Constants
	private static final int SCREENHEIGHT = 400;
	private static final int SCREENWIDTH = 800;
	private static final int RESOLUTIONWIDTH = 10;
	private static final int BAUDRATE = 115200;
	private static final int SERIALPORT = 4;
	
    public SimpleRead() {    
    	DWAs = new DistanceWithAngle[SCREENWIDTH / RESOLUTIONWIDTH];
    	
    	// Initialize all distances to 0
    	for (DistanceWithAngle DWA : DWAs) { DWA = new DistanceWithAngle(); }
    	
    	canvas = new MyCanvas();
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
		         Thread.sleep(2);
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
			      for (int j = DWAs.length - 2; j >= 0; j--) {                
			          DWAs[j+1] = DWAs[j];
			      }
			      DWAs[0] = new DistanceWithAngle(distance, angle);
		      }
		      canvas.repaint();
		   }
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
    	
    	System.out.println("Program aborted");
		port.closePort();	
    }

    private class MyCanvas extends Canvas {
      public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;  
        DistanceWithAngle lastDWA = DWAs[DWAs.length - 1];

        g.setFont(new Font("Helvetica", Font.BOLD, 30)); 
        g.drawString(Integer.toString(lastDWA.getDistance()) + " cm", 20, 80);
        g.drawString(Integer.toString(lastDWA.getAngle()) + " degrees", 20, 120);
        
        AffineTransform old = g.getTransform();
        //draw shape/image which will be rotated

        g.translate(SCREENWIDTH/2, 50);
        g.rotate(Math.toRadians(lastDWA.getAngle() - 90)); // degrees
        g.fillRect (-5, 0, 10, lastDWA.getDistance()/6);
        g.setTransform(old);
        


//        for (int i = 0; i<DWAs.length; i++) { 
//            g.setBackground(Color.BLACK);
//            g.fillRect (i*RESOLUTIONWIDTH, SCREENHEIGHT-(DWAs[i].getDistance()/6), RESOLUTIONWIDTH, DWAs[i].getDistance()/6);
//        }
      }
    }
      
    private static void logPorts(SerialPort[] ports) {   	
    	for(int i = 0; i < ports.length; i++) {
      		SerialPort port = ports[i];
      		System.out.println(i + " " + port.getSystemPortName() + "(" + port.getDescriptivePortName() + ")");
      	}
    }
}
