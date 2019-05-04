package ten_o_nine;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TenONine {

	 public static void main(String args[]) {
		 givenUsingTimer_whenSchedulingDailyTask_thenCorrect();
	 }
	 
	 
	 public  static  void theTask() {
		 JFrame frame = new JFrame();
		  ImageIcon icon = new ImageIcon("C:\\Users\\Cris\\Pictures\\ten_o_nine.png");
		  JLabel label = new JLabel(icon);
		  frame.add(label);
		  frame.setDefaultCloseOperation
		         (JFrame.EXIT_ON_CLOSE);
		  frame.pack();
		  frame.setVisible(true);
		
	 }
	 
	 public static void givenUsingTimer_whenSchedulingDailyTask_thenCorrect() {
		    TimerTask repeatedTask = new TimerTask() {
		        public void run() {
		        	theTask();
		            System.out.println("Task performed on " + new Date());
		        }
		    };
		    Timer timer = new Timer("Timer");
		     
		    long delay = 1000L;
		    long period = 1000L * 60L * 60L * 24L;
		    timer.scheduleAtFixedRate(repeatedTask, delay, period);
		}
	 
}
	


