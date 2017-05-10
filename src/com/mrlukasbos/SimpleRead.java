package com.mrlukasbos;
/* 
 * Lukas Bos, may 2017
 * Bachelor Project for Creative Technology, the University of Twente, NL
 * In thankful collaboration with Indes B.V., Enschede, The Netherlands
 */

import com.fazecast.jSerialComm.*;

public class SimpleRead {
    public static void main(String[] args) {
    	SerialPort[] ports = SerialPort.getCommPorts();
    	logPorts(ports);
    	
    	SerialPort port = ports[4];
    	port.openPort();

    	try {
    		while (true) {
		      while (port.bytesAvailable() == 0) {
		         Thread.sleep(20);
		      }
		      byte[] readBuffer = new byte[port.bytesAvailable()];
		      int numRead = port.readBytes(readBuffer, readBuffer.length);
		      System.out.println("Read " + numRead + " bytes.");
		   }
		} catch (Exception e) { e.printStackTrace(); }
		port.closePort();	
    }
    
    private static void logPorts(SerialPort[] ports) {   	
    	for(int i = 0; i < ports.length; i++) {
    		SerialPort port = ports[i];
    		System.out.println(i + " " + port.getSystemPortName() + "(" + port.getDescriptivePortName() + ")");
    	}
    }
}
