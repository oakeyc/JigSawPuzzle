import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/*
 * Display 		Puzzle Project		Mason Liu, Nonu Bajaj, Yamini Nambiar
 * 
 * The Display class contains all code relating to the graphical 
 * and visual representation of this project. Methods include prepareEndingAnimation(), initialize(), initializePieces(), 
 * setFont(), and resetPieces(). 
 */
public class Display {
	private JFrame frame;
	private JPanel entirePanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JLabel bestTime;
	private JLabel currentTime;
	private JButton solve;
	private JButton clear;
	private BoardPanel board;
	private JPanel gPane;
	private GraphicsPiece[] pieces;
	private Puzzle puzzle;
	private GraphicsPiece selectedPiece;
	private Color bgColor;
	private Color leftBgColor;
	private Timer timer;
	private Timer bgTimer;
	private Timer victoryTimer;
	private JLabel fireworks;
	private boolean isSolved;
	private boolean isAutoSolved;
	private double oldW;
	private double oldH;
	private long start;
	private Clip clip;
	private Mario mario;
	
	public Display() {
		initializePieces();
		initialize();
	}
	// sets up all JLabels, JPanels, Timers, MouseEvents, Graphics, etc.
	@SuppressWarnings("serial")
	public void initialize(){ 
		bgColor=new Color(0,67,180);
		leftBgColor=new Color(0,0,75);
		frame = new JFrame();
		frame.setTitle("Puzzle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(500, 500));
		entirePanel = new JPanel();
		entirePanel.setPreferredSize(new Dimension(1000, 800));
		oldH=800;
		oldW=1000;
		isAutoSolved=false;
		frame.add(entirePanel);
		gPane=new JPanel(){
			public void paintComponent(Graphics g){
				Graphics2D g2 = (Graphics2D) g;
				for(int i=0; i<9; i++)
					g2.drawImage(pieces[i].getB(), (int)pieces[i].getX(), (int)pieces[i].getY(), (int)pieces[i].getWidth(), (int)pieces[i].getLength(), null);
				if(isSolved)
					g2.drawImage(mario.move(clear.getX()+leftPanel.getWidth()), mario.getX(), mario.getY(), null);
			}
		};
		gPane.setLayout(new BorderLayout());
		frame.setGlassPane(gPane);
		gPane.setVisible(true);
		gPane.setOpaque(false);
		frame.pack();

		ImageIcon firework=new ImageIcon("fireworks.gif");
		fireworks=new JLabel(firework, JLabel.CENTER);
		fireworks.setPreferredSize(new Dimension(500,500));
		fireworks.setVisible(false);
		gPane.add(fireworks, BorderLayout.CENTER);
		
		rightPanel = new JPanel();
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(entirePanel.getWidth()/5, entirePanel.getHeight()));
		leftPanel.setBackground(leftBgColor);
		rightPanel.setPreferredSize(new Dimension(entirePanel.getWidth() - entirePanel.getWidth()/5, entirePanel.getHeight()));
		rightPanel.setBackground(bgColor);

		rightPanel.setLayout(new GridBagLayout());
		int score = 0;
		try {
			FileReader reader = new FileReader("score.txt");
			Scanner in = new Scanner(reader);
			score = in.nextInt();
			in.close();
		} catch (Exception e2) {
			File f=new File("score.txt");
			try {
				f.createNewFile();
			} catch (Exception e1) {
			}
			score=0;
		}

		bestTime = new JLabel("Best Time - " + ((score==0)?"<>":score) + " Seconds", JLabel.CENTER);
		currentTime = new JLabel("Current Time - 0 Seconds", JLabel.CENTER);

		start = new Date().getTime();

		timer = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent event) {//updates currentTime 
				currentTime.setText("Current Time - "+
						((new Date().getTime()-start)/1000)+" Seconds");
			}			
		});

		timer.start();
		bgTimer= new Timer(100, new ActionListener(){
			public void actionPerformed(ActionEvent event) {//updates the background color
				bgColor=createNewBgColor();
				leftBgColor=createNewLeftBgColor();
				leftPanel.setBackground(leftBgColor);
				rightPanel.setBackground(bgColor);
			}			
		});
		bgTimer.start();
		victoryTimer = new Timer(700, new ActionListener(){
			public void actionPerformed(ActionEvent event) {//moves mario and checks if fireworks should be played
				if(mario.isDone())
					fireworks.setVisible(true);
				gPane.repaint();
			}			
		});
		
		bestTime.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		bestTime.setBackground(Color.WHITE);
		bestTime.setOpaque(true);
		currentTime.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		currentTime.setBackground(Color.WHITE);
		currentTime.setOpaque(true);

		GridBagConstraints bag = new GridBagConstraints();

		bag.fill = GridBagConstraints.HORIZONTAL;
		bag.gridwidth = 1;
		bag.gridx = 0;
		bag.gridy = 0;
		bag.weightx = 1;
		rightPanel.add(bestTime, bag);
		bag.gridx = 1;
		rightPanel.add(currentTime, bag);

		bag.gridwidth = 2;
		bag.gridx = 0;
		bag.gridy = 1;
		JPanel padding1 = new JPanel();
		padding1.setPreferredSize(new Dimension(rightPanel.getWidth(),50));
		rightPanel.add(padding1, bag);

		bag.gridy = 2;
		bag.fill = GridBagConstraints.NONE;
		board = new BoardPanel(puzzle);
		board.setPreferredSize(new Dimension(210, 210));
		rightPanel.add(board, bag);

		bag.fill = GridBagConstraints.HORIZONTAL;
		bag.gridx = 0;
		bag.gridy = 3;
		JPanel padding2 = new JPanel();
		padding2.setPreferredSize(new Dimension(rightPanel.getWidth(),50));
		rightPanel.add(padding2, bag);

		padding1.setOpaque(false);
		padding2.setOpaque(false);

		solve = new JButton("Solve");
		solve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {//solves the puzzle and repaints all the pieces in their correct positions
				isAutoSolved=true;
				puzzle.solve();
				board.drawSolvedPuzzle(pieces, leftPanel.getWidth(), puzzle);
				frame.repaint();
			}
		}); 
		clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {//clears the board
				if(isSolved){
					isSolved=false;
					start = new Date().getTime();
					timer.start();
					fireworks.setVisible(false);
					clip.close();
					victoryTimer.stop();
				}
				isAutoSolved=false;
				resetPieces();
				puzzle.clear();
				frame.repaint();
			}
		});

		bag.gridwidth = 1;
		bag.gridy = 4;
		rightPanel.add(solve, bag);
		bag.gridx = 1;
		rightPanel.add(clear, bag);
		setFont();

		entirePanel.setLayout(new BorderLayout());
		entirePanel.add(leftPanel, BorderLayout.LINE_START);
		entirePanel.add(rightPanel, BorderLayout.CENTER);

		entirePanel.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {//resizes the font, images, and some components
				leftPanel.setPreferredSize(new Dimension(entirePanel.getWidth()/5, entirePanel.getHeight()));
				board.setPreferredSize(new Dimension(entirePanel.getWidth()*21/100, 21*entirePanel.getHeight()/80));
				setFont();
				if(isSolved)
				mario.setCoords(mario.getX()*entirePanel.getWidth()/oldW, mario.getY()*entirePanel.getHeight()/oldH);
				
				for(int i=0; i<pieces.length; i++){
					pieces[i].setWidth(118*entirePanel.getWidth()/1000);
					pieces[i].setLength(118*entirePanel.getHeight()/800);
					pieces[i].setCoords(pieces[i].getX()*entirePanel.getWidth()/oldW, pieces[i].getY()*entirePanel.getHeight()/oldH);
				}
				oldW=entirePanel.getWidth();
				oldH=entirePanel.getHeight();
			}
			public void componentHidden(ComponentEvent arg0) {
			}
			public void componentMoved(ComponentEvent arg0) {
			}
			public void componentShown(ComponentEvent arg0) {
			}
		});
		gPane.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {//if a piece is currently selected, the selected piece will follow the mouse
				if(selectedPiece!=null){
					selectedPiece.setCoords(Math.max(-selectedPiece.getWidth()/5,Math.min(e.getX()-selectedPiece.getWidth()/2, frame.getWidth()-4*selectedPiece.getWidth()/5)), Math.max(-selectedPiece.getLength()/5, Math.min(frame.getHeight()-selectedPiece.getLength(),e.getY()-selectedPiece.getLength()/2)));
					gPane.repaint();
				}
			}
			public void mouseMoved(MouseEvent e) {}

		});
		gPane.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e) && !isAutoSolved && !isSolved){//rotates the piece
					for(int i=pieces.length-1; i>=0; i--)
						if(pieces[i].contains(e.getPoint())){
							pieces[i].rotate(puzzle);
							break;
						}
					frame.repaint();
				}
				else if(SwingUtilities.isLeftMouseButton(e)){//checks to see if a button was clicked
					Point glassPanePoint = e.getPoint();
					Container container = frame.getContentPane();
					Point containerPoint = SwingUtilities.convertPoint( gPane,  glassPanePoint, container);
					Component component = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);
					if (component != null && (component.equals(solve) || component.equals(clear))) {
						((JButton) component).doClick();
					}}}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e) && !isAutoSolved && !isSolved){//if a piece is clicked, it is set as the selected piece
					for(int i=pieces.length-1; i>=0; i--)
						if(pieces[i].contains(e.getPoint())){
							selectedPiece=pieces[i];
							movePieceToFront(i);
							break;
						}
					if(selectedPiece!=null)
						selectedPiece.removeFromBoard(puzzle);
				}
			}
			public void mouseReleased(MouseEvent e) {//if a piece is selected, it is deselected and snapped into position on the board if necessary
				if(selectedPiece!=null)
					board.snap(selectedPiece, leftPanel.getWidth(), puzzle);
				if(puzzle.isSolved() && !isAutoSolved && !isSolved){
					isSolved=true;
					timer.stop();
					int score = 0;
					try {
						FileReader reader = new FileReader("score.txt");
						Scanner in = new Scanner(reader);
						score = in.nextInt();
						in.close();
					} catch (Exception e2) {
						score=-1;
					}
					if(score==-1 || (new Date().getTime()-start)/1000 < score){
						try {
							PrintWriter out = new PrintWriter("score.txt");
							out.println((new Date().getTime()-start)/1000);
							bestTime.setText("Best Time - " + (new Date().getTime()-start)/1000 + " Seconds");
							bestTime.repaint();
							out.close();
						} catch (FileNotFoundException e1) {
						}
					}
					prepareEndingAnimation();
				}
				frame.repaint();
				selectedPiece=null;
			}
		});

		for(int i=0; i<pieces.length; i++)
			pieces[i].setCoords(40, i*800/pieces.length);
		frame.setVisible(true);
	}
	//moves the GraphicsPiece at the given index to the last spot in the array, effectively bringing it to the front
	public void movePieceToFront(int index){
		for(int i=index; i<pieces.length-1; i++){
			GraphicsPiece temp=pieces[i];
			pieces[i]=pieces[i+1];
			pieces[i+1]=temp;
					}
	}
	//starts the victory sound and initializes a mario object to prepare for the ending animation
	public void prepareEndingAnimation(){
		try {
			AudioInputStream audio= AudioSystem.getAudioInputStream(new File("mario.wav"));
			clip =AudioSystem.getClip();
			clip.open(audio);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mario=new Mario(leftPanel.getWidth(), solve.getY()-30);
		victoryTimer.start();
	}
	//creates new background color for the right JPanel
	public Color createNewBgColor() {
		int piecesUsed = 0;
		int g;
		int b;
			for (int i = 0; i < puzzle.getNumRows(); i++)
				for (int j = 0; j < puzzle.getNumCols(); j++)
					if (puzzle.getSpot(i, j) != null)
						piecesUsed++;
			int r = (Math.abs(bgColor.getRed() - 255 * piecesUsed
					/ puzzle.getNumberOfPieces()) < 8 || isAutoSolved) ? bgColor
							.getRed() : (bgColor.getRed() > 255 * piecesUsed
									/ puzzle.getNumberOfPieces()) ? bgColor.getRed() - 8 : bgColor
											.getRed() + 8;
									if (bgColor.getGreen() > 255 - r / 2)
										g = (bgColor.getGreen() % 2 != 0) ? bgColor.getGreen() - 4
												: bgColor.getGreen() - 5;
										else
											g = (bgColor.getGreen() % 2 != 0) ? bgColor.getGreen() - 2
													: bgColor.getGreen() + 2;
											if (bgColor.getBlue() > 255 - r / 2)
												b = (bgColor.getBlue() % 2 != 0) ? bgColor.getBlue() - 4 : bgColor
														.getBlue() - 5;
												else
													b = (bgColor.getBlue() % 2 != 0) ? bgColor.getBlue() - 4 : bgColor
															.getBlue() + 4;
													return new Color(Math.max(55 - r / 5, Math.min(255, r)), Math.max(
															55 - r / 5, Math.min(255, g)), Math.max(40, Math.min(255, b)));
	}
	//creates new background color for the left JPanel
	public Color createNewLeftBgColor(){
		int increment=Math.abs((leftBgColor.getBlue()-75)/3);
		if (increment==0)
			increment=2;
		if(increment%2==1)
			increment++;
		int b=Math.max(0, Math.min(255, (leftBgColor.getBlue()%2 !=0)? leftBgColor.getBlue()-increment: leftBgColor.getBlue()+increment));
		return new Color(0,0,b);
	}
	// sets Fonts for currentTime, solve, and clear
	public void setFont() {
		bestTime.setFont(new Font(currentTime.getFont().getName(), Font.PLAIN,Math.min(entirePanel.getWidth() / 30,entirePanel.getHeight() / 30)));
		currentTime.setFont(new Font(currentTime.getFont().getName(),Font.PLAIN, Math.min(entirePanel.getWidth() / 30,entirePanel.getHeight() / 30)));
		solve.setFont(new Font(solve.getFont().getName(), Font.PLAIN, Math.min(entirePanel.getWidth() / 30, entirePanel.getHeight() / 30)));
		clear.setFont(new Font(clear.getFont().getName(), Font.PLAIN, Math.min(entirePanel.getWidth() / 30, entirePanel.getHeight() / 30)));
	}

	// lays out Pieces on leftPanel
	public void resetPieces() {
		for (int i = 0; i < pieces.length; i++)
			pieces[i].setCoords(leftPanel.getWidth() / 2 - pieces[i].getWidth()
					/ 2, i * leftPanel.getHeight() / pieces.length);
	}

	// adds all 9 Pieces to Piece[] and all 9 Files to File[]
	// Piece[] and File[] are used to read in the image corresponding to each
	// piece
	public void initializePieces() {
		selectedPiece = null;
		Piece[] test = new Piece[9];
		test[0] = new Piece(2, 3, -4, -2);
		test[1] = new Piece(1, 4, -1, -3);
		test[2] = new Piece(3, 1, -1, -2);
		test[3] = new Piece(3, 4, -2, -2);
		test[4] = new Piece(1, 1, -3, -2);
		test[5] = new Piece(3, 4, -4, -3);
		test[6] = new Piece(1, 4, -3, -4);
		test[7] = new Piece(2, 3, -1, -3);
		test[8] = new Piece(2, -2, -4, 4);
		puzzle = new Puzzle(test, 3, 3);

		File[] f = new File[9];
		pieces = new GraphicsPiece[9];
		f[0] = new File("piece_1.png");
		f[1] = new File("piece_2.png");
		f[2] = new File("piece_3.png");
		f[3] = new File("piece_4.png");
		f[4] = new File("piece_5.png");
		f[5] = new File("piece_6.png");
		f[6] = new File("piece_7.png");
		f[7] = new File("piece_8.png");
		f[8] = new File("piece_9.png");
		for (int i = 0; i < 9; i++) {
			pieces[i] = new GraphicsPiece(f[i], test[i]);

		}
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Display display = new Display();
	}
}
