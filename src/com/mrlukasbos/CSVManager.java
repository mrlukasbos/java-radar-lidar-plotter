package com.mrlukasbos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CSVManager {

	PrintWriter pw = null;
	
	public CSVManager() {
        String fileName = "measurement-data-" + getTime(); 
	        
		try {
		    pw = new PrintWriter(new File(fileName));
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		
		initCSV();
	}
	
	private void initCSV() {
		StringBuilder sb = new StringBuilder();
        sb.append("Time");
        sb.append(',');
        sb.append("Lidar-Angle");
        sb.append(',');
        sb.append("Lidar-distance");
        sb.append('\n');

        pw.write(sb.toString());
	}
	
	public void writeToCSV(LidarPoint point) {
		StringBuilder sb = new StringBuilder();
        sb.append(getTime());
        sb.append(',');
        sb.append(point.getAngle());
        sb.append(',');
        sb.append(point.getDistance());
        sb.append('\n');

        pw.write(sb.toString());
	}
	
	private String getTime() {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
	}
}

