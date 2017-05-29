package com.mrlukasbos;

/*
* A LiDAR point is a distance and an angle. 
* (0 is moving towards the sensor and 1 is moving away from the sensor) 
*/

public class LidarPoint {
	long time; 
	int distance; 
	int angle;
	
	public LidarPoint() {
		this.angle = 0; 
		this.distance = 0;
		this.time = 0;
	}
	
	public LidarPoint(long time, int distance, int angle) {
		this.time = time;
		this.angle = angle; 
		this.distance = distance; 
	}
	
	public long getTime() { 
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
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
