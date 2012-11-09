package test;

import java.io.*;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.hslf.model.TextBox;

public class PptCreator {

	public static void main(String[] args) {
		try {
			
			SlideShow slideShow = new SlideShow();
			Slide slide = slideShow.createSlide();
			TextBox txt = new TextBox();
			txt.setText("First page\nyou can write here");
			slide.addShape(txt);
			
			Slide slide2 = slideShow.createSlide();
			TextBox txt2 = new TextBox();
			txt2.setMarginLeft(200);
			txt2.setMarginTop(200);
			txt2.setText("Second page\nyou can read from here");
			slide2.addShape(txt2);
			
			FileOutputStream out = new FileOutputStream("Hello.ppt");
			slideShow.write(out);			
			out.close();
			
		} catch (Exception e) {
		}
	}
}
