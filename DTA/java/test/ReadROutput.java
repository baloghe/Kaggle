package test;

import java.io.File;

import dta.*;

public final class ReadROutput{

	public static void main(String[] args) throws Exception{
		//ROutputReader ror = new ROutputReader("S:/java/tidewspc/DTA/data/rtrf");
		ROutputReader ror = new ROutputReader("rtrf");
		File f = Util.createFile("submission.csv", "driver_trip,prob");
		Main.submit(f , ror.getResults());
	}

}