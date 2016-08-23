import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class GirisEkrani extends JFrame {
	private CaptureDesktop cam;
	private String path;
	private String examName;
	private File file;
	private String name;
	private String number;
	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	
	public CaptureDesktop getCam(){
		return cam;
	}
	public String getName(){
		return name;
	}
	public void setPath(String a){
		path = a;
	}
	public String getPath(){
		return path;
	}
	public String getExamName(){
		return examName;
	}
	public String getNumber(){
		return number;
	}
/*	public void setName(File f){
		*this.file = f;
	}
	public void setNumber(File f){
		this.file = f;
	}  */
	long startTime = System.currentTimeMillis();

	public String getTimeElapsed(){
		long elapsedTime = System.currentTimeMillis() - startTime;
		elapsedTime = elapsedTime / 1000;

		String seconds = Integer.toString((int)(elapsedTime % 60));
		String minutes = Integer.toString((int)((elapsedTime % 3600) / 60));
		String hours = Integer.toString((int)(elapsedTime / 3600));

		if (seconds.length() < 2)
			seconds = "0" + seconds;

		if (minutes.length() < 2)
			minutes = "0" + minutes;

		if (hours.length() < 2)
			hours = "0" + hours;

		return hours+":"+minutes+":"+seconds;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GirisEkrani window = new GirisEkrani();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GirisEkrani () {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Tilki");
		frame.setBounds(100, 100, 450, 229);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		textField = new JTextField();
		textField.setBounds(146, 39, 152, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField_1 = new JTextField();
		textField_1.setBounds(146, 70, 152, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		frame.setResizable(false);
		
		JButton btnNewButton = new JButton("Sinava Basla");
		btnNewButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				name=textField.getText();
				number = textField_1.getText();	
				System.out.println(name + "  " + number);;
			    frame.dispose();
			    frame.setVisible(false);
			    cam = new CaptureDesktop();
			    try{
				 cam.StartCaptureDesktop(name, number);
			    }
			    catch(Exception ex){
				  System.out.println(ex.getMessage());
			    }
			 
			  	YuklemeEkrani window = new YuklemeEkrani(cam);
			    window.	setSize(351,250);
			    window.setVisible(true);
		
			    	 
			    }
		});
		
		btnNewButton.setBounds(229, 142, 163, 23);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Isim");
		lblNewLabel.setBounds(47, 42, 46, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Numara");
		lblNewLabel_1.setBounds(47, 73, 46, 14);
		frame.getContentPane().add(lblNewLabel_1);
	}
}
