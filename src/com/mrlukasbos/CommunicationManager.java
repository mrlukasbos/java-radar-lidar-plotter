package com.mrlukasbos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.fazecast.jSerialComm.SerialPort;

public class CommunicationManager {
	private static final int BAUDRATE = 115200;
	private static final int SERIALPORT = 4;
	private SerialPort port;
	private PrintWriter pw = null;

	public CommunicationManager() { 
	}
	
	public void start() {
		SerialPort[] ports = SerialPort.getCommPorts();
    	logPorts(ports);
    	port = ports[SERIALPORT];
		port.setBaudRate(BAUDRATE);
		port.openPort();
	}
	
	public void stop() {
		port.closePort();
	}
	
	public String[] getData() {
	  	try {
	  		if (port != null) {
		  		byte[] readBuffer = new byte[port.bytesAvailable()];
		      	port.readBytes(readBuffer, readBuffer.length); // log this statement to get amount of bytes received.
		      	String text = new String(readBuffer);
		      	return text.split("\\r?\\n");
	  		}
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
	  	return null;
	}
	
	// Logs available serial ports in console
	private static void logPorts(SerialPort[] ports) {   	
    	for(int i = 0; i < ports.length; i++) {
      		SerialPort port = ports[i];
      		System.out.println(i + " " + port.getSystemPortName() + "(" + port.getDescriptivePortName() + ")");
      	}
    }
}
