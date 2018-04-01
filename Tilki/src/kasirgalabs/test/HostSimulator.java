package kasirgalabs.test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HostSimulator {
	FileOutputStream fos;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	
      public  DataInputStream getDataInputStreamFor( String[] s){
	      try {
	    	 String fileName = "HostSimulatorTestFile.txt";
	         File f1 = new File(fileName);
	         if (f1.exists()){
	        	 f1.delete();
	         }
	         File f = new File(fileName);
	         f.createNewFile();
	         
	         // create file output stream
	          fos = new FileOutputStream(fileName);
	           
	         // create data output stream
	          dos = new DataOutputStream(fos);
	           
	         // write string encoded as modified UTF-8
	         for(String j:s) {
	             
	             // write string encoded as modified UTF-8
	             dos.writeUTF(j);
	          }
	 
	         // force data to the underlying file output stream
	         dos.flush();
	         
	         // create file input stream
	          is = new FileInputStream(fileName);
	         
	         // create new data input stream
	          dis = new DataInputStream(is);
	         
	         return dis;
	         	        
	      } catch(Exception e) {
	       System.out.println("Error on getDataInputStreamFor");
	      }
		return null; 
	      
	}

	public void closeHostSimulatorResouces() {
		// releases all system resources from the streams
        try {
       	 	 if(is!=null)	{is.close();}
			 if(dis!=null)	{dis.close();}
			 if(fos!=null)	{fos.close();}
			 if(dos!=null)	{dos.close();}
		} catch (Exception e) {
			System.out.println("Error on closeHostSimulatorResouces");
		}
		
	}

	public InputStream getInputStream(String s) {
		InputStream is = new ByteArrayInputStream( s.getBytes(StandardCharsets.UTF_8) );
		return is;
	}


}