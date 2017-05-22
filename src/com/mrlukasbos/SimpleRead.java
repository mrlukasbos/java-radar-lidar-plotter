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
	private static int[] distances;
	
	// Constants
	private static final int SCREENHEIGHT = 400;
	private static final int SCREENWIDTH = 800;
	private static final int RESOLUTIONWIDTH = 10;
	private static final int BAUDRATE = 115200;
	private static final int SERIALPORT = 4;
	
    public SimpleRead() {    
    	distances = new int[SCREENWIDTH / RESOLUTIONWIDTH];
    	
    	// Initialize all distances to 0
    	for (int distance : distances) { distance = 0; }
    	
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
		         Thread.sleep(5);
		      }
		      
		      byte[] readBuffer = new byte[port.bytesAvailable()];
		      port.readBytes(readBuffer, readBuffer.length); // log this statement to get amount of bytes received.
		      String text = new String(readBuffer);
		      String lines[] = text.split("\\r?\\n");
		      
		      for (int i = 0; i < lines.length; i++) {
			      int distance = 0;
				  try {
					  if (!lines[i].trim().isEmpty()) {
						  distance = Integer.parseInt(lines[i].trim());
					  }
				  } catch(Exception e) {
				      System.out.println("Getting error, value is " + lines[i] );
					  distance = 0 ;
				  }
			      
				  // Shift the whole array. Crucial to loop backwards
			      for (int j = distances.length - 2; j >= 0; j--) {                
			          distances[j+1] = distances[j];
			      }
			      distances[0] = distance;
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
        
        AffineTransform old = g.getTransform();
        g.rotate(Math.toRadians(50)); // degrees
        //draw shape/image which will be rotated
        g.setBackground(Color.RED);

        g.fillRect (150, 50, 50, 50 );

        
        g.setTransform(old);
        
        g.setFont(new Font("Helvetica", Font.BOLD, 60)); 
        g.drawString(Integer.toString(distances[distances.length - 1]) + " cm", 20, 80);
        for (int i = 0; i<distances.length; i++) { 
            g.setBackground(Color.BLACK);
            g.fillRect (i*RESOLUTIONWIDTH, SCREENHEIGHT-(distances[i]/6), RESOLUTIONWIDTH, distances[i]/6);
        }
      }
    }
      
    private static void logPorts(SerialPort[] ports) {   	
    	for(int i = 0; i < ports.length; i++) {
      		SerialPort port = ports[i];
      		System.out.println(i + " " + port.getSystemPortName() + "(" + port.getDescriptivePortName() + ")");
      	}
    }
}
