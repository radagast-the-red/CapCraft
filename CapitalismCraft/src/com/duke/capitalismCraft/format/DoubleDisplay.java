package com.duke.capitalismCraft.format;

import java.text.DecimalFormat;

public class DoubleDisplay {
	public static String display(double d) {
		DecimalFormat df = new DecimalFormat("###,###.##");
		return df.format(d);
	}
}
