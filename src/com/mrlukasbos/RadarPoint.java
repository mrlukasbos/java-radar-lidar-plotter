package com.mrlukasbos;

/*
* A radar point is a velocity (km.h up to 1 decimal) and optionally a direction. 
* (0 is moving towards the sensor and 1 is moving away from the sensor) 
*/

public class RadarPoint {
	float velocity;  // now in cm / h
	int direction;
	long time;
	
	public RadarPoint() {
		this.velocity = 0; 
		this.direction = 0; 
		this.time = 0;
	}
	
	public RadarPoint(long time, int velocity, int direction) {
		this.time = time;
		this.velocity = velocity; 
		this.direction = direction; 
	}
	
	public long getTime() { 
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public int getDirection() { 
		return direction;
	}
	
	public void setDirection(int direction) { 
		this.direction = direction;
	}
	
	public float getVelocityCmPh() { 
		return velocity;
	}
	
	public float getVelocityKmPh() { 
		return velocity / 100;
	}
	
	public void setVelocity(int velocity) { 
		this.velocity = velocity;
	}
}
