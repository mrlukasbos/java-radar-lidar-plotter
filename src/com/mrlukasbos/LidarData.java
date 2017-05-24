package com.mrlukasbos;

public class LidarData {
	int distance; 
	int angle;
	
	public LidarData() {
		this.angle = 0; 
		this.distance = 0; 
	}
	
	public LidarData(int distance, int angle) {
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
