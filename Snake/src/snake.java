import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;


public class snake {

	public static void main(String[] args){
		
		JFrame MFrame = new JFrame();
		snakePanel play = new snakePanel();
		
		MFrame.addKeyListener(play.A);
		
		MFrame.setTitle("Gluttonous snake");
		MFrame.setSize(21*12+1,19*12+6);
		MFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MFrame.setLocationRelativeTo(null);
		MFrame.setResizable(false);
		MFrame.setVisible(true);
		
		MFrame.add(play);
		play.start();
	}
}