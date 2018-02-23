package kasirgalabs;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportWriting {
	private static final String FILE_NAME = "reportForTeacher.pdf";
	Document document;
	String total;;

	public ReportWriting() {
		document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		document.open();
		printHead("E-XAM REPORT FOR TEACHER");
	}

	// pdf e baslik icin
	public void printHead(String s) {
		document.open();
		Font f = new Font();
		f.setStyle(Font.BOLD);
		f.setSize(16);
		Paragraph p = new Paragraph();
		p.setAlignment(Element.ALIGN_CENTER);
		p.add(new Paragraph(s, f));
		try {
			document.add(p);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addImage() {
		document.open();
		Image image1;
		try {
			for (int i = 0; i < 10; i++) {
				image1 = Image.getInstance("FullScreenshot"+i+".jpg");
				document.add(image1);
				document.add(Chunk.NEWLINE);
			}
			for (int i = 0; i < 10; i++) {
				image1 = Image.getInstance("Photo"+i+".png");
				document.add(image1);
			}

			
		} catch (IOException | DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		document.close();
	}

	// metin icin
	public void addText(String s) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		if(s!=null){
			total = dateFormat.format(date) + "    " + s + "\n" + total;
		//System.out.println(total);
		}
		}

	public void submitText() {
		try {
			document.open();
			Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);			
			if (total != null) {
				//System.out.println(total);
				Chunk chunk = new Chunk(total, font);

				document.add(chunk);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void closed() {
		document.close();
	}

}
