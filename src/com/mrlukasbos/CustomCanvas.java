package com.mrlukasbos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class CustomCanvas extends JPanel {
	
	private int canvasHeight;
	private int canvasWidth;
	private static final int scaling = 5;

	LidarPoint[] lidarPoints; 
    
    public CustomCanvas(int height,int width) {
    	this.canvasHeight = height;
		this.canvasWidth = width; 
    }
    
	public void setCanvasSize(int height,int width) {
		this.canvasHeight = height;
		this.canvasWidth = width; 
	}
    
    public void setlidarPoints(LidarPoint[] lidarPoints) {
    	this.lidarPoints = lidarPoints;
    }
	
	public void drawBackground(Graphics2D g) {
    	g.setFont(new Font("Helvetica", Font.BOLD, 10)); 
        
    	g.setColor(Color.BLACK);
        drawOvalWithRadius(1000, g);
        drawOvalWithRadius(2000, g);
        drawOvalWithRadius(3000, g);
        drawOvalWithRadius(4000, g);
        
        g.setColor(Color.GRAY);
        drawOvalWithRadius(500, g);
        drawOvalWithRadius(1500, g);
        drawOvalWithRadius(2500, g);
        drawOvalWithRadius(3500, g);
	}
	
	public void drawlidarPoints(Graphics2D g) { 
		
        for (int i = 0; i<lidarPoints.length; i++) { 
        	int colorValue = lidarPoints.length - (255/lidarPoints.length * i); 
            g.setColor(new Color(0, 0, 0, colorValue));
            LidarPoint lidarPoint = lidarPoints[i];
            AffineTransform old = g.getTransform();
            //draw shape/image which will be rotated

            g.translate(canvasWidth/2, 50);
            g.rotate(Math.toRadians(lidarPoint.getAngle() - 90)); // degrees
            g.fillRect (-2, 0, 4, lidarPoint.getDistance()/scaling);
            g.setTransform(old);
         }  
	}
	
    public void paint(Graphics graphics) {
    	super.paintComponent(graphics);
        setDoubleBuffered(true);
        Graphics2D g = (Graphics2D) graphics;  
    	// System.out.println(MouseInfo.getPointerInfo().getLocation());
    	drawBackground(g);
    	drawlidarPoints(g); 
    }
    
    private void drawOvalWithRadius(int distance, Graphics2D g) {
        int ovalRadius = distance*2/scaling; // 10 meters
        g.drawOval(canvasWidth/2 - ovalRadius/2, 50-ovalRadius/2, ovalRadius, ovalRadius);
        g.drawString(distance + "cm", canvasWidth/2 + ovalRadius/2, 40);	
    }
  }