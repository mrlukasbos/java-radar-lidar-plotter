package com.mrlukasbos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CSVManager {

	PrintWriter pw = null;
	
	public CSVManager() {
	}
	
	public void startExport() {
		String fileName = "data/measurement-data-" + getDate() +".csv"; 

		try {
		    pw = new PrintWriter(new File(fileName));
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		exportForRadar();
	}
	
	private void exportForRadar() {
		StringBuilder sb = new StringBuilder();
        sb.append("Time");
        sb.append(',');
        sb.append("velocity");
        sb.append(',');
        sb.append("direction");
        sb.append('\n');

        pw.write(sb.toString());
        System.out.println("starting CSV Export for Radar");
	}
	
	public void writeToCSV(RadarPoint point) {
		StringBuilder sb = new StringBuilder();
        sb.append(point.getTime());
        sb.append(',');
        sb.append(point.getVelocityKmPh());
        sb.append(',');
        sb.append(point.getDirection());
        sb.append('\n');

        pw.write(sb.toString());
	}
	
	public void stop() {
		pw.close();
	}
	
	private String getDate() {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
	}
}

