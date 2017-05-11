package com.mrlukasbos;
/* 
 * Lukas Bos, may 2017
 * Bachelor Project for Creative Technology, the University of Twente, NL
 * In thankful collaboration with Indes B.V., Enschede, The Netherlands
 */

import com.fazecast.jSerialComm.*;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;

public class SimpleRead extends JFrame {
	static String serialtext;
	static MyCanvas canvas;
	
    public SimpleRead() {    
    	serialtext = "";
    	canvas = new MyCanvas();
        add("Center", canvas);
        setSize(400, 400);
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
		      System.out.println(text);
		      serialtext = text;
		      
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
      }
    }
      
    private static void logPorts(SerialPort[] ports) {   	
    	for(int i = 0; i < ports.length; i++) {
      		SerialPort port = ports[i];
      		System.out.println(i + " " + port.getSystemPortName() + "(" + port.getDescriptivePortName() + ")");
      	}
    }
}
