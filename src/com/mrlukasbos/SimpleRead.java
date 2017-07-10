package com.mrlukasbos;
/* 
 * Lukas Bos, may 2017
 * Bachelor Project for Creative Technology, the University of Twente, NL
 * In thankful collaboration with Indes B.V., Enschede, The Netherlands
 */

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import javax.swing.JFrame;

public class SimpleRead extends JFrame {
	private static final long serialVersionUID = 1L;
	private static CustomCanvas canvas;
	private static RadarPoint[] radarPoints;
	private static CommunicationManager comm; 
	private static CSVManager csvManager;
	
	private static long startTime = System.currentTimeMillis();
	private static long elapsedTime = 0;

	// Constants
	private static final int SCREENHEIGHT = 800;
	private static final int SCREENWIDTH = 1200;


	public SimpleRead() {    
		radarPoints = new RadarPoint[200];

		comm = new CommunicationManager();
		csvManager = new CSVManager();

		// Initialize all radarPoints
		for (int i = 0; i<radarPoints.length; i++) { 
			radarPoints[i] = new RadarPoint(); 
		}

		canvas = new CustomCanvas(SCREENHEIGHT, SCREENWIDTH);

		add("Center", canvas);
		setSize(SCREENWIDTH, SCREENHEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		SimpleRead app = new SimpleRead();
		
		app.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				csvManager.stop();
				System.out.println("closing");
			}
		});
		
		comm.start();
		csvManager.startExport();
		
		while (true) { // main loop
			setFrameRate(25);
			
			String lines[] = comm.getData(); // get all data received meanwhile
			
			if (lines != null) {
				// read all lines to get their data
				// lines are formatted with an identifier (letter) and a number 'D100,A90'
				// This part of code sets the value to the integer which corresponds to the identifier
				// D -> Distance
				// A -> Angle
				// R -> Direction
				// V -> Velocity				
				for (int i = 0; i < lines.length; i++) {
					String line = lines[i];
					line = line.trim();
	
					int distance = 0;
					int angle = 0;
					int velocity = 0;
					int direction = 0;

					try {
						if (!line.isEmpty()) {
							String[] splittedLine = line.split(",");
							
							for(int j = 0; j < splittedLine.length; j++) {
								String identifier = splittedLine[j].substring(0, 1);
								int data = Integer.parseInt(splittedLine[j].substring(1));
								
								switch (identifier) {
									case "A": 
										angle = data;
										break;
									case "D": 
										distance = data;
										break;
									case "R": 
										direction = data;
										break;
									case "V": 
										velocity = data;
										break;
								}
							}
							
							// Shift the whole array. Crucial to loop backwards
							for (int j1 = radarPoints.length - 2; j1 >= 0; j1--) {                
								radarPoints[j1+1] = radarPoints[j1];
							}
							
							// set the value of the radarpoint in the array and export it
						    elapsedTime = (new Date()).getTime() - startTime;
							radarPoints[0] = new RadarPoint(elapsedTime, velocity, direction);
							csvManager.writeToCSV(radarPoints[0]);							
						}
					} catch(Exception e) {
						System.out.println("Getting error, value is " + line );
					}

				}
				// draw points on screen
				canvas.setRadarPoints(radarPoints);
				canvas.repaint();
			}
		}
	}
	
	public static void setFrameRate(int frameRate) {
		try {
			Thread.sleep(1000/frameRate);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

}

