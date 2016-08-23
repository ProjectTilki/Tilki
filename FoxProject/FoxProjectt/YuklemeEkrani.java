import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;


import java.awt.*;
import java.text.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class YuklemeEkrani extends JFrame{
	CaptureDesktop cam;
	JLabel time;
	private File selectedFile;
	private JFrame frame;
	long startTime = System.currentTimeMillis();
	String codePath;
	
	public void close(){
		frame.dispose();
		frame.setVisible(false);
	}
	public CaptureDesktop getCam(){
		return cam;
	}
	
	public YuklemeEkrani(CaptureDesktop cam){
		this.frame = this;
		this.cam = cam;
		setSize(351,250);
		setTitle("Tilki");
		setLocation(450,225);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		JLabel lblGecenZaman = new JLabel("Gecen Sure");
		lblGecenZaman.setForeground(Color.GRAY);
		lblGecenZaman.setBounds(40, 45, 134, 31);
		lblGecenZaman.setFont(new Font("Tahoma", Font.PLAIN, 20));
		getContentPane().add(lblGecenZaman);
		JButton btnBrowse = new JButton("Arastir");
		btnBrowse.setBounds(40, 101, 100, 23);
		setResizable(false);
		btnBrowse.addActionListener(new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			int returnValue = fileChooser.showOpenDialog(null);
        
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				codePath = fileChooser.getSelectedFile().getAbsolutePath();
			}
             
          
		}
      
		});

		
	time = new JLabel("");
	time.setBounds(184, 45, 88, 31);
	time.setFont(new Font("SansSerif", Font.BOLD, 20));
	time.setForeground(Color.GRAY);
	getContentPane().add(time);
	getContentPane().add(btnBrowse);
	JButton btnFinishExam = new JButton("Sinavi bitir");
	btnFinishExam.addActionListener(new ActionListener() {
	
		public void actionPerformed(ActionEvent arg0) {
	
			try{
				getCam().StopCaptureDesktop();
				close();
			
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
	
		}
		});
	
	btnFinishExam.setBounds(175, 101, 127, 23);
	getContentPane().add(btnFinishExam);

	//starting new Thread which will update time
		new Thread(new Runnable(){
			public void run(){ 
				try{
					updateTime(); 
				} 
				catch (Exception ie){ 
			
				}
			}
		}).start();
	
	}

	public void updateTime(){
		try{
			while(true){
				//geting Time in desire format
				time.setText(getTimeElapsed());
				//Thread sleeping for 1 sec
				Thread.currentThread().sleep(1000);
			}
		}
		catch (Exception e){
			System.out.println("Exception in Thread Sleep : "+e);
		}
	}

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



	public static void main(String[] args){
		JFrame obj = new YuklemeEkrani(null);
		obj.setVisible(true);
	}
}
