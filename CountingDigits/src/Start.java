import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Start {
	public static int number; //The digit to be repeated
	public static int repeat; //The number of times the digit repeats
	public static String identify; //The number to be identified
	public static JFrame start;
	public static JFrame frame;
	public static JLabel time_L;
	public static JLabel input_L;
	public static JTextField input_F;
	public static JPanel pane;
	public static boolean[] answers; //whether the human was right or not
	public static int count = 0; //question number
	public static double timePass = 0; //the time passed
	public static double timeStart = 0; //the time started
	public static double timeEnd = 0; //the time next is clicked
	public static double[] times; //list of times for each question
	public static Timer run;
	public static final int QUESTIONS = 4;
	
	/**
	 * Display start screen and start the game when play is pressed.
	 * @param args
	 */
	public static void main(String[] args){
		//Start Screen
		start = new JFrame("Counting Digits");
		JPanel instructions = new JPanel();
		JLabel instr_Label = new JLabel();
		instr_Label.setText("Welcome to counting digits! The goal is to... count digits! A number will appear. Type the number of digits in the box beside it as fast as possible. Remember to click next to proceed! The timer will keep counting until you press next.");
		JButton play = new JButton(new playAction("Play"));
		instructions.add(instr_Label);
		instructions.add(play);
		start.add(instructions);
		start.pack();
		start.setVisible(true);
	}
	
	/**
	 * Set up the game
	 */
	public static void setup(){
		//set up swing
				frame = new JFrame("Counting Digits");
				pane = new JPanel();
				input_L = new JLabel();
				input_F = new JTextField(5);
				time_L	= new JLabel("0");
				answers = new boolean[QUESTIONS];
				times = new double[QUESTIONS];
				
				//create first set
				generate();
				
				//set up more swing
				pane.setLayout(new GridLayout(1,3));
				pane.add(input_L);
				pane.add(input_F);
				pane.add(new JButton(new NextAction("Next")));
				pane.add(time_L);
				
				frame.add(pane);
				frame.setSize(600, 70);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				
				//setup timer
				run = new Timer();
				run.schedule(new TimerTask(){
					@Override
					public void run() {
						timePass++;
						time_L.setText(Double.toString((timePass/100)));
					}
				}, 10L,10L);
	}
	
	/**create a string of numbers.
	 * 
	 */
	public static void generate(){
		number = (int)(Math.random()*10);
		repeat = (int)(Math.random()*10);
		identify = Integer.toString(number);
		for(int i = 0; i < repeat-1; i++){
			identify += number;
		}
		System.out.println(identify);
		input_L.setText("Number: " + identify + "    ");
		timeStart = 0;
	}
	
	/**Check the answer.
	 * 
	 * @param input: the answer to check
	 * @return correct: is the answer right?
	 */
	public static boolean check(String input){
		timeEnd = timePass;
		times[count] = (timeEnd - timeStart);
		System.out.println(timeEnd + " " + timeStart + " " + times[count]);
		if(input.equals(Integer.toString(repeat))){
			return true;
		}
		else
			return false;
	}
	
	/**End the game:
	 * displays win screen.
	 */
	public static void end(){
		run.cancel();
		
		JFrame win = new JFrame();
		JPanel dispAnswers = new JPanel();
		GridLayout grid = new GridLayout(3, QUESTIONS+1);
		
		JLabel answered = new JLabel();
		JLabel timed = new JLabel();
		String answerText = "The Answers: ";
		String timeText= "The Times: ";
		JLabel [] answerBoxes = new JLabel[QUESTIONS];
		JLabel [] timeBoxes = new JLabel[QUESTIONS];
		JLabel averageLabel;
		double correct = 0;
		
		grid.setVgap(5);
		grid.setHgap(10);
		dispAnswers.setLayout(grid);
		
		answered.setText(answerText);
		timed.setText(timeText);
		dispAnswers.add(answered);
		for(int i = 0; i < QUESTIONS; i++){
			answerBoxes[i] = new JLabel();
			answerBoxes[i].setText(Boolean.toString(answers[i]));
			dispAnswers.add(answerBoxes[i]);
			if(answers[i]){
				correct++;
			}
		}
		
	
		dispAnswers.add(timed);
		for(int i = 0; i < QUESTIONS; i++){
			timeBoxes[i] = new JLabel();
			timeBoxes[i].setText(Double.toString(times[i]/100) + " sec");
			dispAnswers.add(timeBoxes[i]);
		}
		
		averageLabel = new JLabel("Average: " + Double.toString(100 * correct/QUESTIONS) + "%.");
		dispAnswers.add(averageLabel);
		for(int i = 0; i < QUESTIONS; i++){
			dispAnswers.add(new JLabel());
		}
		
		win.add(dispAnswers);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.pack();
		win.setVisible(true);
	}
	
	/**
	 * Start the game
	 *
	 */
	public static class playAction extends AbstractAction{
		public playAction(String name){
			super(name);
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			start.setVisible(false);
			setup();
			
		}
		
	}
	
	/**
	 * Start the next round
	 * @author Noah
	 *
	 */
	public static class NextAction extends AbstractAction{
		public NextAction(String name){
			super(name);
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(count < QUESTIONS - 1){ //if it's not the last question
				answers[count] = check(input_F.getText()); //check if the answer is right
				System.out.println(check(input_F.getText()));
				generate(); //generate a new set
				input_F.setText(null); //clear the input
				input_F.requestFocus(); //focus on the input
				count++;
				timePass = 0; //the time passed is now 0
			}
			else{
				answers[count] = check(input_F.getText()); //it is the last question
				System.out.println(check(input_F.getText()));
				frame.setVisible(false); //hide the question frame
				end(); //end the game
			}
		}
		
	}
}