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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportWriting {

    private static final String FILE_NAME = "reportForTeacher.pdf";
    Document document;
    public static String total = "";

    public ReportWriting() {
        document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(
                    FILE_NAME)));
        }
        catch(FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(DocumentException e) {
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
        }
        catch(DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void addImage(String path) {
        document.open();
        Image image1 = null;
        try {

            try {
                image1 = Image.getInstance(path);
            }
            catch(BadElementException ex) {
                Logger.getLogger(ReportWriting.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
            catch(IOException ex) {
                Logger.getLogger(ReportWriting.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
            document.add(image1);

        }
        catch(DocumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    // metin icin
    public void addText(String s) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        if(s != null) {
            total = dateFormat.format(date) + "    " + s + "\n" + total;
            //System.out.println(total);
        }
    }

    public void submitText() {
        try {
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 14,
                    BaseColor.BLACK);
            if(total != null) {
                //System.out.println(total);
                Chunk chunk = new Chunk(total, font);
                  document.add(chunk);
                  System.out.println("chuunkkkkkkk "+chunk);
                
            }
             document.close();
        }
        catch(DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void closed() {
        document.close();
    }

}
