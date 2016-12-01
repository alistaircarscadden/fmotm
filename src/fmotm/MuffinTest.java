package fmotm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.muffin.FileMuffin;
import org.newdawn.slick.util.FileSystemLocation;

public class MuffinTest {
	public MuffinTest() {
		HashMap<Integer, String> data = new HashMap<Integer, String>();
		
		data.put(0, "zero");
		data.put(1, "one");
		data.put(2, "two");
		data.put(4, "four");
		data.put(8, "eight");
		data.put(16, "sixteen");
		data.put(32, "thirty two");
		data.put(64, "sixty four");
		
		FileMuffin fm = new FileMuffin();
		
		try {
			fm.saveFile(data, "/res/muffin.sav");
			System.out.println("muffintest written");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MuffinTest mt = new MuffinTest();
	}

}
