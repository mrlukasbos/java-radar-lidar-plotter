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
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;

public class SimpleRead extends JFrame {
	Shape shapes[] = new Shape[5];
	static String serialtext;
	static MyCanvas canvas;
	
    public static void main(String[] args) {
    	SimpleRead app = new SimpleRead();
    	SerialPort[] ports = SerialPort.getCommPorts();
    	logPorts(ports);
    	
    	SerialPort port = ports[4];
    	port.setBaudRate(9600);
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

    
    public SimpleRead() {    
    	canvas = new MyCanvas();
        add("Center", canvas);
        shapes[0] = new Line2D.Double(0.0, 0.0, 100.0, 100.0);
        shapes[1] = new Rectangle2D.Double(10.0, 100.0, 200.0, 200.0);
        shapes[2] = new Ellipse2D.Double(20.0, 200.0, 100.0, 100.0);
        GeneralPath path = new GeneralPath(new Line2D.Double(300.0, 100.0, 400.0, 150.0));
        path.append(new Line2D.Double(25.0, 175.0, 300.0, 100.0), true);
        shapes[3] = path;
        shapes[4] = new RoundRectangle2D.Double(350.0, 250, 200.0, 100.0, 50.0, 25.0);
        
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
      }

      class MyCanvas extends Canvas {
        public void paint(Graphics graphics) {
          Graphics2D g = (Graphics2D) graphics;
          for (int i = 0; i < shapes.length; ++i) {
            if (shapes[i] != null)
                g.drawString(serialtext, 200, 200);
              g.draw(shapes[i]);
            
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
