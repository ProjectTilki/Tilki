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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JDialog;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.DEFAULT_OPTION;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.SwingWorker;

public class ZipAndUpload extends javax.swing.JFrame implements ActionListener,
                                                                PropertyChangeListener {
    private File[] codeFiles;
    private boolean codeFilesAreDone = false;
    private File[] videoFiles;
    private String name;
    private String id;
    private String checksum;
    private ZipAndUpload.Task task;
    private static int ZIP_STATE = 0;
    private static int UPLOAD_STATE = 1;
    private static ConcurrentLinkedQueue<String> list = new ConcurrentLinkedQueue<String>();

    private class Task extends SwingWorker<String, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public String doInBackground() throws IOException {
            try {
                String zipName = createZipFile(codeFiles);
                checksum = sendFile(zipName, name, id);
                codeFilesAreDone = true;
                checksum = checksum.toUpperCase();
                list.add(
                        "\u0130\u015Flem ba\u015Far\u0131yla tamamland\u0131.\n"
                        + "Çözümleriniz karşıya yüklenmiştir. Programı kapatmadan önce\n"
                        + "kullanım verilerinizin karşıya yüklenmesini bekleyiniz.\n\n");

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
                list.add(
                        "G\u00F6nderdi\u011Finiz dosyan\u0131n MD5 do\u011Frulama kodu:");
                list.add("\n" + checksum + "\n");
                list.add("MD5 do\u011Frulama kodunuz kaydedilmi\u015Ftir:");
                list.add("\n" + f.getAbsolutePath() + "\n");
                list.add("Belirtilen dizinde bulabilirsiniz.\n\n");
                task.firePropertyChange("message", 0, 1);
                String temp = createZipFile(videoFiles);
                //task.firePropertyChange("test", 1, 2);
                sendFile(temp, name, id);
                list.add(
                        "\u0130\u015Flem ba\u015Far\u0131yla tamamland\u0131.");
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
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            ZipAndUpload.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            startButton.setText("<html>Program\u0131<br>Kapat<html>");
            startButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    System.exit(0);
                }
            });
            setCursor(null); //turn off the wait cursor
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
            String zipFileName = videoFiles[0].getName();
            int pos = zipFileName.lastIndexOf('.');
            if(pos > 0)
                zipFileName = zipFileName.substring(0, pos) + ".zip";
            else
                zipFileName += ".zip";
            if(codeFilesAreDone)
                zipFileName = "videos_" + zipFileName;
            else
                zipFileName = "codes_" + zipFileName;
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
                list.add("Dosya zipleniyor: " + file.getName() + "\n");
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
            list.add("Dosyalar y\u00FCkleniyor...\n");
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
    }

    public ZipAndUpload() {
        codeFiles = null;
        videoFiles = null;
        name = null;
        id = null;
        initComponents();
    }

    public ZipAndUpload(File[] codeFiles, File[] videoFiles, String name,
                        String id) {
        this();
        this.codeFiles = codeFiles;
        this.videoFiles = videoFiles;
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
        setTitle("Tilki");
        setMaximumSize(new java.awt.Dimension(350, 250));
        setMinimumSize(new java.awt.Dimension(350, 250));
        setResizable(false);

        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        startButton.setText("<html>Dosyalar\u0131<br>Y\u00FCkle</html>");
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
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)))
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
                .addComponent(taskOutputScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
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
        if("progress".equals(evt.getPropertyName())) {
            while(!list.isEmpty())
                taskOutput.append(list.poll());
            int progress = (Integer) evt.getNewValue();
            if(progressBar.getValue() == 100)
                progressBar.setValue(0);
            else
                progressBar.setValue(progress);
        }
        if("message".equals(evt.getPropertyName())) {
            Object[] option = {"Tamam"};
            String message = "L\u00FCtfen a\u015Fa\u011F\u0131daki kodu imza ka\u011F\u0131d\u0131ndaki bo\u015F yere yaz\u0131n\u0131z.\n\n";
            message += checksum.substring(0, 5) + " - " + checksum.
                    substring(5, 10) + " - " + checksum.
                    substring(10, 15);
            JOptionPane pane = new JOptionPane(message, WARNING_MESSAGE,
                                               DEFAULT_OPTION, null, option);
            JDialog dialog = pane.createDialog(this, "Doğrulama Kodu");
            dialog.setModal(false);
            dialog.setVisible(true);
        }
        if("test".equals(evt.getPropertyName())) {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            ZipAndUpload.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            startButton.setText("\u0130ptal Et");
            startButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    System.exit(0);
                }
            });
            setCursor(null); //turn off the wait cursor
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
