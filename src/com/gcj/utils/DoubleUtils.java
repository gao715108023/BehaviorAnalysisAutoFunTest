package com.gcj.utils;

public class DoubleUtils {

    public static double convert(double value) {
        double ret = (double) (Math.round(value * 100)) / 100.0;
        return ret;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        System.out.println(DoubleUtils.convert(123.456));
	}
}
