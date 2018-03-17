package kasirgalabs;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportWriting {

    private static final String FILE_NAME = "reportForTeacher.pdf";
    Document document;
    public static String total = "";
    public static String total1 = "";
    public static String total2 = "";
    private static String total3 = "";
    private static String total4 = "";
    private static String total5 = "";

    public ReportWriting() {

    }

    private void createDocument() {
        document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(new File(
                    FILE_NAME)));

        }
        catch(FileNotFoundException | DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated catch block
    }

    // pdf e baslik icin
    public Paragraph printHead(String s, int size) {
        //document.open();
        Font f = new Font(FontFamily.HELVETICA, size, Font.BOLD);
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_CENTER);
        p.setFont(f);
        Chunk c = new Chunk("\n" + s + "\n", f);

        p.add(c + "\n");
        return p;

    }

    // metin icin
    public void addText(String s, int i) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        if(s != null) {
            switch(i) {
                case 0:
                    total = total + "\n" + dateFormat.format(date) + "    " + s;
                    //System.out.println(total);
                    break;
                case 1:
                    total1 = total1 + "\n" + s;
                    break;
                case 2:
                    total2 = total2 + "\n" + s;
                    break;
                case 3:
                    total3 = total3 + "\n" + s;
                    break;
                case 4:
                    total4 = total4 + "\n" + dateFormat.format(date) + "    " + s;
                    break;
                case 5:
                    total5 = total5 + "\n" + s;
                    break;
                default:

                    break;
            }
        }

    }

    /**
     * eklemeler yapıldıktan sonra pdf e submit edilir
     */
    public void submitText() {
        createDocument();
        try {
            document.open();
            Font font = FontFactory.getFont(FontFactory.TIMES, 14,
                    BaseColor.BLACK);
            document.add(printHead("E-XAM REPORT FOR TEACHER", 22));
            if(total5 != "") {
                Paragraph p = printHead("Score", 18);//4
                Chunk chunk5 = new Chunk(total5, font);
                document.add(p);
                document.add(chunk5);
            }
            if(total != "") {
                Paragraph p = printHead("Moments when face can not be found", 18);//0
                Chunk chunk = new Chunk(total, font);
                document.add(p);
                document.add(chunk);
            }
            if(total1 != "") {
                Paragraph p = printHead("Blocked Apps", 18);//1
                Chunk chunk1 = new Chunk(total1, font);
                document.add(p);
                document.add(chunk1);
            }
            if(total2 != "") {
                Paragraph p = printHead("Opened or Open Applications", 18);//2
                Chunk chunk2 = new Chunk(total2, font);
                document.add(p);
                document.add(chunk2);
            }
            if(total3 != "") {
                Paragraph p = printHead("Closed Apps", 18);//3
                Chunk chunk3 = new Chunk(total3, font);
                document.add(p);
                document.add(chunk3);
            }
            if(total4 != "") {
                Paragraph p = printHead("Internet- Tab Information", 18);//4
                Chunk chunk4 = new Chunk(total4, font);
                document.add(p);
                document.add(chunk4);
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

//    public void addImage(String path) {
//        document.open();
//        Image image1 = null;
//        try {
//
//            try {
//                image1 = Image.getInstance(path);
//            }
//            catch(BadElementException ex) {
//                Logger.getLogger(ReportWriting.class.getName()).log(Level.SEVERE,
//                        null, ex);
//            }
//            catch(IOException ex) {
//                Logger.getLogger(ReportWriting.class.getName()).log(Level.SEVERE,
//                        null, ex);
//            }
//            document.add(image1);
//
//        }
//        catch(DocumentException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//    }
}
