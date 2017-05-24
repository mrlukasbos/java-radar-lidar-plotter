package com.mrlukasbos;

public class LidarPoint {
	int distance; 
	int angle;
	
	public LidarPoint() {
		this.angle = 0; 
		this.distance = 0; 
	}
	
	public LidarPoint(int distance, int angle) {
		this.angle = angle; 
		this.distance = distance; 
	}
	
	public int getDistance() { 
		return distance;
	}
	
	public void setDistance(int distance) { 
		this.distance = distance;
	}
	
	public int getAngle() { 
		return angle;
	}
	
	public void setAngle(int angle) { 
		this.angle = angle;
	}
}
