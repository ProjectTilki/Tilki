package kasirgalabs.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.Test;

import kasirgalabs.ZipAndUpload;

public class TestZipAndUpload {

	
	
	@Test
	 public void createZipFileTest() throws IOException {
		int numberOfFile = 3;
		File[] files = createTestFileList(numberOfFile);
		
		
		// String zipFileName = videoFiles[0].getName();
		 String zipFileName = "testZipFile";
         int pos = zipFileName.lastIndexOf('.');
         if(pos > 0) {
             zipFileName = zipFileName.substring(0, pos) + ".zip";
         }
         else {
             zipFileName += ".zip";
         }
//         if(codeFilesAreDone) {
//             zipFileName = "videos_" + zipFileName;
//         }
//         else {
//             zipFileName = "codes_" + zipFileName;
//         }

         FileOutputStream fos = null;
         FileInputStream fis = null;
         ZipOutputStream zos = null;
         try {
             fos = new FileOutputStream(zipFileName);
             zos = new ZipOutputStream(fos);
             String fileName;
             long fileSize;
             for(File file : files) {
                 int progress = 0;
                 long sentBytes = 0;
                 fileName = file.getName();
                 fileSize = file.length();
                 fis = new FileInputStream(file);
//                 if(Thread.currentThread().isInterrupted()) {
//                     return null;
//                 }
//                 queue.add("Dosya zipleniyor: " + file.getName() + "\n");
                 ZipEntry zipEntry = new ZipEntry(fileName);
                 zos.putNextEntry(zipEntry);
                 byte[] buffer = new byte[4096];
                 int length;
                 while((length = fis.read(buffer)) > 0 && !Thread.
                         currentThread().isInterrupted()) {
                     zos.write(buffer, 0, length);
                     sentBytes += length;
                     if(sentBytes >= fileSize / 100) {
                         sentBytes = 0;
  //                       setProgress(progress++);
                     }
                 }
                 zos.closeEntry();
                 fis.close();
             }
         }
         catch(Exception e) {
             throw e;
         }
         finally {
             if(zos != null) {
                 zos.close();
             }
             if(fos != null) {
                 fos.close();
             }
             if(fis != null) {
                 fis.close();
             }
         }
         
         if(new File(zipFileName).exists()) // File exists
		    {
				File f = new File(zipFileName);
				assert(f.exists());
				f.delete();
				for (int i = 0; i < numberOfFile; i++) {
					f = new File("testFile"+Integer.toString(i)+".txt");
					f.delete();
				}
				 
		    }
		
	 }
	
	public File[] createTestFileList(int numberOfFile) {
		File[] testFiles = new File[numberOfFile];
		for (int i = 0; i < numberOfFile; i++) {
			testFiles[i]= createTestFile("testFile"+Integer.toString(i)+".txt");
		}
		return testFiles;
	}
	public File createTestFile(String fileName) {
		BufferedWriter writer = null;
		File testFile = new File(fileName);
		 try {
			writer = new BufferedWriter(new FileWriter(testFile));
			 writer.write("This is a sample text.");
			 writer.close();
		} catch (Exception e) {

		} finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
		return testFile;
	}
}
