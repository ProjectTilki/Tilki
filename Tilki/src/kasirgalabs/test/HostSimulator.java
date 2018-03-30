package kasirgalabs.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HostSimulator {
	   
      InputStream is = null;
      DataInputStream dis = null;
      FileOutputStream fos = null;
      DataOutputStream dos = null;

      public  DataInputStream getDataInputStreamFor( String[] s){
	      try {
	    	 String fileName = System.getProperty("java.io.tmpdir")+"HostSimulatorTestFile.txt";
	         File f = new File(fileName);
	         if (f.exists()){
	        	 f.delete();
	         }
	         f = new File(fileName);
	         
	         
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
	         	        
	      } catch(Exception e) {
	      
	         // if any error occurs
	         e.printStackTrace();
	      } finally {
	         
	         // releases all system resources from the streams
	         try {
				if(is!=null)
				    is.close();
				 if(dis!=null)
				    dis.close();
				 if(fos!=null)
				    fos.close();
				 if(dos!=null)
				    dos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	      }
	      return dis;
	}

	public void closeHostSimulatorResouces() {
		// releases all system resources from the streams
        try {
       	 	 if(is!=null)
       	 		 is.close();
			 if(dis!=null)
				 dis.close();
			 if(fos!=null)
				 fos.close();
			 if(dos!=null)
				 dos.close();
		} catch (Exception e) {}
		
	}


}