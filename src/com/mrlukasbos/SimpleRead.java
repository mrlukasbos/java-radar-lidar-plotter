package com.mrlukasbos;
/* 
 * Lukas Bos, may 2017
 * Bachelor Project for Creative Technology, the University of Twente, NL
 * In thankful collaboration with Indes B.V., Enschede, The Netherlands
 */

import com.fazecast.jSerialComm.*;

import java.util.Date;

import javax.swing.JFrame;

public class SimpleRead extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static CustomCanvas canvas;
	private static LidarPoint[] lidarPoints;
	private static CommunicationManager comm; 
	private static CSVManager csvManager;
	
	private static long startTime = System.currentTimeMillis();
	private static long elapsedTime = 0;

	// Constants
	private static final int SCREENHEIGHT = 800;
	private static final int SCREENWIDTH = 1200;


	public SimpleRead() {    
		lidarPoints = new LidarPoint[200];

		comm = new CommunicationManager();
		csvManager = new CSVManager();

		// Initialize all distances to 0
		for (int i = 0; i<lidarPoints.length; i++) { 
			lidarPoints[i] = new LidarPoint(); 
		}

		canvas = new CustomCanvas(SCREENHEIGHT, SCREENWIDTH);
		canvas.setlidarPoints(lidarPoints);

		add("Center", canvas);
		setSize(SCREENWIDTH, SCREENHEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		SimpleRead app = new SimpleRead();
		comm.start();
		csvManager.startExport();
		

		while (true) { // main loop
			setFrameRate(25);
			
			String lines[] = comm.getData(); // get all data received meanwhile
			
			if (lines != null) {
				for (int i = 0; i < lines.length; i++) {
					String line = lines[i];
					line = line.trim();
	
					int distance = 0;
					int angle = 0;
					try {
						if (!line.isEmpty()) {
							String[] splittedLine = line.split(",");					 
							distance = Integer.parseInt(splittedLine[0]);
							angle = Integer.parseInt(splittedLine[1]);
						}
					} catch(Exception e) {
						System.out.println("Getting error, value is " + line );
					}
	
					// Shift the whole array. Crucial to loop backwards
					for (int j = lidarPoints.length - 2; j >= 0; j--) {                
						lidarPoints[j+1] = lidarPoints[j];
					}
					
				    elapsedTime = (new Date()).getTime() - startTime;

					lidarPoints[0] = new LidarPoint(elapsedTime, distance, angle);
					
					csvManager.writeToCSV(lidarPoints[0]);
				}
				canvas.setlidarPoints(lidarPoints);
				canvas.repaint();
			}
		}

		// comm.stop();
	}
	
	public static void setFrameRate(int frameRate) {
		try {
			Thread.sleep(1000/frameRate);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}


}
