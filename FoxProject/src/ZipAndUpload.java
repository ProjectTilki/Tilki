import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.SwingWorker;

public class ZipAndUpload extends javax.swing.JFrame implements ActionListener,
                                                                PropertyChangeListener {
    private File[] files;
    private String name;
    private String id;
    private String checksum;
    private ZipAndUpload.Task task;
    private static volatile int state;
    private static int ZIP_STATE = 0;
    private static int UPLOAD_STATE = 1;
    private static volatile String CURRENT_FILE;
    private static final ArrayList<String> list = new ArrayList<String>();

    private class Task extends SwingWorker<String, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public String doInBackground() throws IOException {
            try {
                String zipName = createZipFile(files);
                checksum = sendFile(zipName, name, id);
            }catch(Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            progressBar.setValue(100);
            taskOutput.append("İşlem başarıyla tamamlandı.\n\n\n");
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            ZipAndUpload.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            startButton.setText("<html>Programı<br>Kapat<html>");
            startButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    System.exit(0);
                }
            });
            setCursor(null); //turn off the wait cursor
            FileWriter fw;
            File f = null;
            try {
                f = new File("checksum.log");
                fw = new FileWriter(f);
                fw.write(checksum);
                fw.close();
            }catch(IOException ex) {
                Logger.getLogger(ZipAndUpload.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
            taskOutput.append("Gönderdiğiniz dosyanın MD5 doğrulama kodu:\n");
            taskOutput.append("----------\n" + checksum + "\n----------\n");
            taskOutput.append("MD5 doğrulama kodunuz kaydedilmiştir:\n");
            taskOutput.append(
                    "----------\n" + f.getAbsolutePath() + "\n----------\n");
            taskOutput.append("Belirtilen dizinde bulabilirsiniz.\n");
        }

        /**
         * Creates a zip file on the current working directory which contains
         * file(s) specified with the parameter. Original files will not be
         * moved and their contents will not be changed.
         * <p>
         * The name of the zip file will be same with the first
         * {@link java.io.File} object's name specified with the parameter. If
         * the file already exists it will be overwritten.
         *
         * @param files Array list of {@link java.io.File} objects, which will
         *              be zipped. All files must be in the current working
         *              directory.
         *
         * @return The name of the zip file.
         *
         * @throws FileNotFoundException File with the specified pathname does
         *                               not exist or is a directory.
         * @throws IOException           If an I/O error occurs.
         */
        private String createZipFile(File[] files) throws FileNotFoundException,
                                                          IOException {
            state = ZIP_STATE;
            String zipFileName = files[0].getName();
            int pos = zipFileName.lastIndexOf('.');
            if(pos > 0)
                zipFileName = zipFileName.substring(0, pos) + ".zip";
            else
                zipFileName += ".zip";
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            String fileName;
            long fileSize;
            long sentBytes = 0;
            for(File file : files) {
                int progress = 0;
                sentBytes = 0;
                fileName = file.getName();
                //fileSize = new File(fileName).length();
                fileSize = file.length();
                FileInputStream fis = new FileInputStream(file);
                CURRENT_FILE = file.getName();
                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);
                byte[] buffer = new byte[4096];
                int length;
                while((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                    sentBytes += length;
                    if(sentBytes >= fileSize / 100) {
                        sentBytes = 0;
                        setProgress(progress++);
                    }
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
            return zipFileName;
        }

        /**
         * Sends a file to a predefined host by creating a
         * {@link java.net.Socket} object. On success returns the MD5 checksum
         * of the file.
         *
         * @param fileName Relative path name of the file for current working
         *                 directory.
         * @param id       The student id.
         * @param exam     The exam name.
         * @param object
         *
         * @return Checksum of the file.
         *
         * @throws java.io.FileNotFoundException If the file does not exist, is
         *                                       a directory rather than a
         *                                       regular file, or for some other
         *                                       reason cannot be opened for
         *                                       reading.
         * @throws java.lang.SecurityException   If a security manager exists
         *                                       and its checkRead method denies
         *                                       read access to the file.
         * @throws java.io.IOException           If an I/O error occurs.
         */
        private String sendFile(String fileName, String id, String exam) throws FileNotFoundException, SecurityException, IOException {
            // Create a socket and initialize it's streams.// Create a socket and initialize it's streams.
            state = UPLOAD_STATE;
            Socket socket = new Socket("localhost", 50101);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("Sending file.");
            out.writeUTF(fileName);
            out.writeUTF(id);
            out.writeUTF(exam);
            out.flush();

            // Read file and send it over the socket.
            long fileSize = new File(fileName).length();
            FileInputStream fileIn = new FileInputStream(fileName);
            CURRENT_FILE = fileName;
            OutputStream os_out = socket.getOutputStream();
            long sentBytes = 0;
            int progress = 0;
            int bytesCount;
            byte[] fileData = new byte[1024];
            do {
                bytesCount = fileIn.read(fileData);
                if(bytesCount > 0) {
                    os_out.write(fileData, 0, bytesCount);
                    sentBytes += bytesCount;
                    if(sentBytes >= fileSize / 100) {
                        sentBytes = 0;
                        setProgress(progress++);
                    }
                }
            }while(bytesCount > 0);

            os_out.flush();
            socket.shutdownOutput(); // Shut down output to tell server no more data.
            String checksum;
            if(in.readUTF().equals("Exam file is found."))
                checksum = in.readUTF(); //Read checksum from socket.
            else
                checksum = null;
            fileIn.close();
            socket.close();
            return checksum;
        }

        private void FileWriter(File file) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public ZipAndUpload() {
        files = null;
        name = null;
        id = null;
        initComponents();
    }

    public ZipAndUpload(File[] files, String name, String id) {
        this();
        this.files = files;
        this.name = name;
        this.id = id;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar(0, 100);
        startButton = new javax.swing.JButton();
        taskOutputScrollPane = new javax.swing.JScrollPane();
        taskOutput = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(350, 250));
        setMinimumSize(new java.awt.Dimension(350, 250));
        setResizable(false);

        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        startButton.setText("<html>Dosyaları<br>Yükle</html>");
        startButton.setToolTipText("");
        startButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        startButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        startButton.setMaximumSize(new java.awt.Dimension(75, 48));

        taskOutput.setEditable(false);
        taskOutput.setColumns(20);
        taskOutput.setRows(5);
        taskOutputScrollPane.setViewportView(taskOutput);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(taskOutputScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(taskOutputScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton startButton;
    private javax.swing.JTextArea taskOutput;
    private javax.swing.JScrollPane taskOutputScrollPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(state == ZIP_STATE && "progress".equals(evt.getPropertyName())) {
            if(!list.contains(CURRENT_FILE)) {
                taskOutput.append("Dosya zipleniyor: " + CURRENT_FILE + "\n");
                list.add(CURRENT_FILE);
            }
            int progress = (Integer) evt.getNewValue();
            if(progressBar.getValue() == 100)
                progressBar.setValue(0);
            else
                progressBar.setValue(progress);
        }else if(state == UPLOAD_STATE && "progress".equals(evt.
                getPropertyName())) {
            if(!list.contains(CURRENT_FILE)) {
                taskOutput.append("Dosyalar yükleniyor..\n");
                list.add(CURRENT_FILE);
            }
            int progress = (Integer) evt.getNewValue();
            if(progressBar.getValue() == 100)
                progressBar.setValue(0);
            else
                progressBar.setValue(progress);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new ZipAndUpload.Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }
}
