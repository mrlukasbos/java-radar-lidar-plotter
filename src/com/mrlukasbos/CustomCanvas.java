package com.mrlukasbos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class CustomCanvas extends JPanel {
	
	private int canvasHeight;
	private int canvasWidth;
	private static final int scaling = 5;

	RadarPoint[] radarPoints; 

    
    public CustomCanvas(int height,int width) {
    	this.canvasHeight = height;
		this.canvasWidth = width; 
    }
    
	public void setCanvasSize(int height,int width) {
		this.canvasHeight = height;
		this.canvasWidth = width; 
	}
	
    public void setRadarPoints(RadarPoint[] radarPoints) {
    	this.radarPoints = radarPoints;
    }
	
	public void drawRadarPoints(Graphics2D g) { 
		
        for (int i = 0; i<radarPoints.length; i++) { 
        	int colorValue = 0;
        	if (i == 0) { 
        		colorValue = 255;
        	}; 
        	
        	g.setColor(new Color(0, 0, 0, colorValue));
            RadarPoint radarPoint = radarPoints[i];
                        
            int direction = radarPoint.getDirection();
            int velocityCmPh = (int) (radarPoint.getVelocityCmPh());
            
            g.setStroke(new BasicStroke(10));

            if (direction != 0) {
                g.drawLine(10, canvasHeight/2, 10, canvasHeight/2 + (velocityCmPh * direction));
            } else {
                g.fillRect (0, 0, 40, (int) (radarPoint.getVelocityCmPh()));
            }
            g.setFont(new Font("Courier new", Font.BOLD, 80));

            g.drawString(Float.toString(radarPoint.getVelocityKmPh()) + " km/h", 50, 70);
            
            String directionText = "";
            if (radarPoint.getDirection() == -1) {
            	directionText = "Coming to sensor";
            } else if (radarPoint.getDirection() == 1) {
            	directionText = "Going away from sensor";
            }
            g.drawString(directionText, 50, 150);

         }  
	}
	
    public void paint(Graphics graphics) {
    	super.paintComponent(graphics);
        setDoubleBuffered(true);
        Graphics2D g = (Graphics2D) graphics;  
    	drawRadarPoints(g);
    }
  }