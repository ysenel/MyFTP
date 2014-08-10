package gui;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBar extends JPanel{
	
	 private JProgressBar bar;
	 
	 
	 public ProgressBar(){
		 this.setLayout(new FlowLayout());
		 
		 this.bar  = new JProgressBar(0, 100);
		 this.bar.setValue(0);
		 this.bar.setStringPainted(true);
		 
		 this.add(bar);
	 }
	 
	 
	 public JProgressBar getBar(){
		 return this.bar;
	 }
	 


}
