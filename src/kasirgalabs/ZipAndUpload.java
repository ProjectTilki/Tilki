package kasirgalabs;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.DEFAULT_OPTION;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.SwingWorker;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class ZipAndUpload extends javax.swing.JFrame implements ActionListener,
        PropertyChangeListener {

    private File[] codeFiles;
    private File logFile;
    private boolean codeFilesAreDone = false;
    private File[] otherFiles;
    private String name;
    private String id;
    private String codes_md5;
    private String videos_md5;
    private ZipAndUpload.Task task;
    private JDialog dialog1;
    private JDialog dialog2;
    private JDialog dialog3;
    private ConcurrentLinkedQueue<String> queue
            = new ConcurrentLinkedQueue<String>();
    private String instructorKey;
    private int rpSkor;
    private int fdSkor;

    private class Task extends SwingWorker<String, Void> {

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public String doInBackground() throws IOException {
            String zipName = createZipFile(codeFiles);
            codes_md5 = sendFile(zipName, name, id);
            codeFilesAreDone = true;
            codes_md5 = codes_md5.toUpperCase();
            queue.add(
                    "\u00C7\u00F6z\u00FCmleriniz kar\u015F\u0131ya"
                    + " y\u00FCklenmi\u015Ftir. Program\u0131 kapatmadan"
                    + " \u00F6nce\n" + "kullan\u0131m verilerinizin"
                    + " kar\u015F\u0131ya y\u00FCklenmesini bekleyiniz.\n\n");
            logFile = new File("tilki.log");
            task.firePropertyChange("enableCloseButton", 0, 1);
            String temp = createZipFile(otherFiles);
            if(Thread.currentThread().isInterrupted()) {
                return "";
            }
            videos_md5 = sendFile(temp, name, id);
            queue.add(
                    "Kullan\u0131m verileriniz kar\u015F\u0131ya"
                    + " y\u00FCklenmi\u015Ftir.\n\n");
            queue.add("Do\u011Frulama kodu:");
            queue.add("\n" + codes_md5 + " " + videos_md5 + "\n");
            queue.add("Do\u011Frulama kodunuz kaydedilmi\u015Ftir:");
            queue.add("\n" + logFile.getAbsolutePath() + "\n");
            queue.add("Belirtilen dizinde bulabilirsiniz.\n\n");
            queue.add(
                    "\u0130\u015Flem ba\u015Far\u0131yla tamamland\u0131."
                    + "\nProgram\u0131 kapat\u0131p, s\u0131navdan"
                    + " \u00E7\u0131kabilirsiniz.");
            task.firePropertyChange("message", 0, 1);
            return "success";
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            ZipAndUpload.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            startButton.setText("<html>Program\u0131<br>Kapat<html>");
            for(ActionListener listener : startButton.getActionListeners()) {
                startButton.removeActionListener(listener);
            }
            

            ZipAndUpload.this.setCursor(null); //turn off the wait cursor
            if(!task.isCancelled()) {
                progressBar.setValue(100);
            }
            try {
                get();
            }
            catch(Exception ex) {
                Object[] option = {"Tamam"};
                String message = "\u00DCzg\u00FCn\u00FCz bir sorun meydana"
                        + " geldi.\nL\u00FCtfen g\u00F6zetmeni"
                        + " \u00E7a\u011F\u0131r\u0131n\u0131z\nve program\u0131"
                        + " yeniden ba\u015Flat\u0131n\u0131z.\n";
                JOptionPane pane = new JOptionPane(message, WARNING_MESSAGE,
                        DEFAULT_OPTION, null,
                        option);
                JDialog dialog = pane.createDialog(null,
                        "Ba\u011Flant\u0131 Hatas\u0131");
                ZipAndUpload.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                dialog.setVisible(true);
            }
            startButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.err.println(".actionPerformed()");
                    System.exit(0);
                    
                }

            });
        }

        /**
         * Creates a zip file on the current working directory which
         * contains
         * file(s) specified with the parameter. Original files will not be
         * moved and their contents will not be changed.
         * <p>
         * The name of the zip file will be same with the first
         * {@link java.io.File} object's name specified with the parameter.
         * If
         * the file already exists it will be overwritten.
         *
         * @param files Array queue of {@link java.io.File} objects, which
         *              will
         *              be zipped. All files must be in the current working
         *              directory.
         *
         * @return The name of the zip file.
         *
         * @throws FileNotFoundException File with the specified pathname
         *                               does
         *                               not exist or is a directory.
         * @throws IOException           If an I/O error occurs.
         */

        private String createZipFile(File[] files) throws IOException {
            String zipFileName = otherFiles[0].getName();
            int pos = zipFileName.lastIndexOf('.');
            if(pos > 0) {
                zipFileName = zipFileName.substring(0, pos) + "_" + MainClient.getExamName() + ".zip";
            }
            else {
                zipFileName += MainClient.getExamName() + ".zip";
            }
            if(codeFilesAreDone) {
                zipFileName = "security_" + zipFileName;
            }
            else {
                zipFileName = "codes_" + zipFileName;
            }

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
                    if(Thread.currentThread().isInterrupted()) {
                        return null;
                    }
                    queue.add("Dosya zipleniyor: " + file.getName() + "\n");
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
                            setProgress(progress++);
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
            return zipFileName;
        }

        /**
         * Sends a file to a predefined host by creating a
         * {@link java.net.Socket} object. On success returns the MD5 codes_md5
         * of the file.
         *
         * @param fileName Relative path name of the file for current working
         *                 directory.
         * @param id       The student id.
         * @param exam     The exam name.
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
        private String sendFile(String fileName, String id, String exam)
                throws IOException {

            URL url = new URL(
                    "https://examwebserver.herokuapp.com/tilki/upload");
            File uploadFile = new File(fileName);
            final MultipartUtility http = new MultipartUtility(url);
            task.firePropertyChange("connectionEstablished", 0, 1);
            http.addFormField("number", id);
            http.addFormField("exam", exam);
            http.addFormField("someButton", "Submit");
            http.addFilePart("fileName", uploadFile);
            final byte[] bytes = http.finish();
            String hash = bytes.toString();
            return hash;
        }

        private String xorHex(String a, String b) {
            char[] chars = new char[a.length()];
            for(int i = 0; i < chars.length; i++) {
                chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
            }
            return new String(chars);
        }

        private int fromHex(char c) {
            if(c >= '0' && c <= '9') {
                return c - '0';
            }
            if(c >= 'A' && c <= 'F') {
                return c - 'A' + 10;
            }
            if(c >= 'a' && c <= 'f') {
                return c - 'a' + 10;
            }
            throw new IllegalArgumentException();
        }

        private char toHex(int index) {
            if(index < 0 || index > 15) {
                throw new IllegalArgumentException();
            }
            return "0123456789ABCDEF".charAt(index);
        }
    }

    public ZipAndUpload() {
        codeFiles = null;
        otherFiles = null;
        name = null;
        id = null;
        initComponents();

        try {
            ImageIcon img = new ImageIcon(getClass().getResource(
                    "images/Tilki.png"));
            setIconImage(img.getImage());
            setLocationRelativeTo(null);
        }
        catch(NullPointerException ex) {
        }
    }

    public ZipAndUpload(File[] codeFiles, File[] videoFiles, String name,
            String id, String instructorKey, int rpskor, int fdskor) {
        this();
        this.codeFiles = codeFiles;
        this.otherFiles = videoFiles;
        this.name = name;
        this.id = id;
        this.instructorKey = instructorKey;
        this.rpSkor = rpskor;
        this.fdSkor = fdskor;
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        taskOutputScrollPane = new javax.swing.JScrollPane();
        taskOutput = new javax.swing.JTextArea();
        startButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar(0, 100);
        jPanel2 = new javax.swing.JPanel();
        cancelLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        cancelPassword = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Tilki");
        setLocation(new java.awt.Point(0, 0));
        setMinimumSize(new java.awt.Dimension(502, 303));
        setResizable(false);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(502, 303));

        taskOutput.setEditable(false);
        taskOutput.setColumns(20);
        taskOutput.setRows(5);
        taskOutputScrollPane.setViewportView(taskOutput);

        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        startButton.setText("<html>Dosyalar\u0131<br>Y\u00FCkle</html>");
        startButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        startButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        startButton.setMaximumSize(new java.awt.Dimension(75, 48));

        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
                    .addComponent(taskOutputScrollPane))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(taskOutputScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, "card2");

        jPanel2.setMinimumSize(new java.awt.Dimension(502, 303));

        cancelLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cancelLabel.setText("G\u00F6zetmen Kodu");

        jButton1.setText("Tamam");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        cancelPassword.setMinimumSize(new java.awt.Dimension(115, 27));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(180, 180, 180)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addComponent(cancelPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(180, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelLabel, cancelPassword, jButton1});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(cancelLabel)
                .addGap(18, 18, 18)
                .addComponent(cancelPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cancelLabel, jLabel1});

        getContentPane().add(jPanel2, "card3");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        if(cancelPassword.getPassword() == null) {
            return;
        }
        char[] password = cancelPassword.getPassword();
        String temp = "";
        for(int i = 0; i < password.length; i++) {
            temp += password[i];
        }
        if(instructorKey.equals(temp)) {
            Object[] option = {"Tamam"};
            String message = "L\u00FCtfen a\u015Fa\u011F\u0131daki kodu"
                    + " imza ka\u011F\u0131d\u0131ndaki bo\u015F yere"
                    + " yaz\u0131n\u0131z.\n\n";
            message += codes_md5 + " " + videos_md5;
            JOptionPane pane = new JOptionPane(message, WARNING_MESSAGE,
                    DEFAULT_OPTION, null,
                    option);
            dialog1 = pane.createDialog(null, "Do\u011Frulama Kodu");
            dialog1.setModal(false);
            dialog1.setVisible(true);
            jPanel1.setVisible(true);
            jPanel2.setVisible(false);
            startButton.setEnabled(true);
            for(ActionListener listener : startButton.getActionListeners()) {
                startButton.removeActionListener(listener);
            }
            startButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    System.exit(0);
                }
            });
        }
        else {
            jLabel1.setText("G\u00F6zetmen kodu yanl\u0131\u015F.");
            ZipAndUpload.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cancelLabel;
    private javax.swing.JPasswordField cancelPassword;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton startButton;
    private javax.swing.JTextArea taskOutput;
    private javax.swing.JScrollPane taskOutputScrollPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("progress".equals(evt.getPropertyName())) {
            while(!queue.isEmpty()) {
                taskOutput.append(queue.poll());
            }
            int progress = (Integer) evt.getNewValue();
            if(progressBar.getValue() == 100) {
                progressBar.setValue(0);
            }
            else {
                progressBar.setValue(progress);
            }
        }
        if("enableCloseButton".equals(evt.getPropertyName())) {
            startButton.setEnabled(true);
            startButton.setText("\u0130ptal Et");
            for(ActionListener listener : startButton.getActionListeners()) {
                startButton.removeActionListener(listener);
            }
            startButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    task.cancel(true);
                    taskOutput.append(
                            "\nDo\u011Frulama kodu:");
                    taskOutput.append("\n" + codes_md5 + "\n");
                    taskOutput.append(
                            "Do\u011Frulama kodunuz kaydedilmi\u015Ftir:");
                    taskOutput.append(
                            "\n" + logFile.getAbsolutePath() + "\n");
                    taskOutput.append("Belirtilen dizinde bulabilirsiniz.\n\n");
                    while(!queue.isEmpty()) {
                        taskOutput.append(queue.poll());
                    }
                    ZipAndUpload.this.setDefaultCloseOperation(
                            DO_NOTHING_ON_CLOSE);
                    jPanel1.setVisible(false);
                    jPanel2.setVisible(true);
                }
            });
        }
        if("message".equals(evt.getPropertyName())) {
            while(!queue.isEmpty()) {
                taskOutput.append(queue.poll());
            }
            Object[] option = {"Tamam"};
            String message = "L\u00FCtfen a\u015Fa\u011F\u0131daki kodu"
                    + " imza ka\u011F\u0131d\u0131ndaki bo\u015F yere"
                    + " yaz\u0131n\u0131z.\n\n";
            message += codes_md5 + " " + videos_md5;
            JOptionPane pane = new JOptionPane(message, WARNING_MESSAGE,
                    DEFAULT_OPTION, null, option);
            dialog2 = pane.createDialog(this, "Do\u011Frulama Kodu");
            dialog2.setModal(false);
            dialog2.setVisible(true);
        }
        if("connectionError".equals(evt.getPropertyName())) {
            if(dialog3 != null && dialog3.isVisible()) {
                return;
            }
            Object[] option = {"Tamam"};
            String message = "Ba\u011Flant\u0131 kurulamad\u0131, yeniden"
                    + " ba\u011Flan\u0131l\u0131yor.\nL\u00FCtfen internet"
                    + " ba\u011Flant\u0131nız\u0131 kontrol ediniz.";
            JOptionPane pane = new JOptionPane(message, WARNING_MESSAGE,
                    DEFAULT_OPTION, null, option);
            dialog3 = pane.createDialog(this, "Do\u011Frulama Kodu");
            dialog3.setModal(false);
            dialog3.setVisible(true);
        }
        if("connectionEstablished".equals(evt.getPropertyName())) {
            if(dialog3 != null) {
                dialog3.setVisible(false);
            }
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
