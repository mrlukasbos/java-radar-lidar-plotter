package com.mrlukasbos;
/* 
 * Lukas Bos, may 2017
 * Bachelor Project for Creative Technology, the University of Twente, NL
 * In thankful collaboration with Indes B.V., Enschede, The Netherlands
 */

import com.fazecast.jSerialComm.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;

public class SimpleRead extends JFrame {
	static String serialtext;
	static MyCanvas canvas;
	static int[] distances;
	
	int SCREENHEIGHT = 400;
	int SCREENWIDTH = 800;
	int RESOLUTIONWIDTH = 10;
	
    public SimpleRead() {    
    	serialtext = "";
    	distances = new int[SCREENWIDTH / RESOLUTIONWIDTH];
    	
    	// init all distances to 0
    	for (int distance : distances) {
    		distance = 0;
    	}
    	
    	canvas = new MyCanvas();
        add("Center", canvas);
        setSize(SCREENWIDTH, SCREENHEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
	
    public static void main(String[] args) {
    	SimpleRead app = new SimpleRead();
    	SerialPort[] ports = SerialPort.getCommPorts();
    	logPorts(ports);
    	
    	SerialPort port = ports[4];
    	port.setBaudRate(115200);
    	port.openPort();

    	try {
    		while (true) { // main loop
    			
		      while (port.bytesAvailable() == 0) {
		         Thread.sleep(20);
		      }
		      byte[] readBuffer = new byte[port.bytesAvailable()];
		      port.readBytes(readBuffer, readBuffer.length); // log this statement to get amount of bytes received.
		      String text = new String(readBuffer);
		      serialtext = text;
		      String lines[] = serialtext.split("\\r?\\n");
		      
		      for (int i = 0; i<lines.length; i++) {
			      System.out.println(lines[i]);
			      	
			      int distance = 0;
				  try {
					  distance = Integer.parseInt(lines[i]);
				  } catch(Exception e) {
					  distance = 0 ;
				  }
			      
			      
			      for (int j = 80-2; j >= 0; j--) {                
			          distances[j+1] = distances[j];
			      }
			      distances[0] = distance;
			     
		      }

		      canvas.repaint();
		   }
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
    	
    	System.out.println("port is closed! oh no!");
		port.closePort();	
    }

    class MyCanvas extends Canvas {
      public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.drawString(serialtext, 20, 20);
       
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
