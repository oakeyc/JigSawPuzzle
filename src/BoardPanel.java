import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * BoardPanel 		Puzzle Project		Mason Liu, Nonu Bajaj, Yamini Nambiar
 * 
 * BoardPanel extends JPanel and serves as the visual board for the puzzle.
 * Methods include snap() and drawSolvedPuzzle(). BoardPanel is responsible for
 * making sure that Pieces, BufferedImages, Puzzle, Display, and Puzzle can work together.  
 *  * 
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel{
	private JLabel[][] squares;

	//default ctor
	public BoardPanel(){}
	//ctor given Puzzle, creates new JLabel for each square on board
	public BoardPanel(Puzzle p){
		setLayout(new GridLayout(p.getNumRows(), p.getNumCols()));
		squares=new JLabel[p.getNumRows()][p.getNumCols()];
		for(int i=0; i<p.getNumRows(); i++){
			for(int j=0; j<p.getNumCols(); j++){
				squares[i][j]=new JLabel();
				squares[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				add(squares[i][j]);
			}}

	}
	//snaps GraphicPiece into place when within a specified distance of the spot on the board
	public void snap(GraphicsPiece p, int extraWidth, Puzzle puzzle){
		double pl = p.getY()+p.getLength()/5;
		double pw = p.getX()+p.getWidth()/5; 
		for(int i=0; i<squares.length; i++){
			for(int j=0; j<squares[i].length; j++){
				if (Math.abs(squares[i][j].getX()+this.getX()+extraWidth-pw)<15 && Math.abs(squares[i][j].getY()+this.getY()-pl)<15 && puzzle.canFit(p.getPiece(),i,j)){
					p.setCoords(squares[i][j].getX()+this.getX()+extraWidth-p.getWidth()/5, squares[i][j].getY()+this.getY()-p.getLength()/5);
					puzzle.setSpot(p.getPiece(), i, j);
					try {
						Random r=new Random();
						 AudioInputStream audio;
						switch(r.nextInt(3)){
						case 0:
							audio = AudioSystem.getAudioInputStream(new File("ding.wav"));
							break;
						case 1:
							 audio = AudioSystem.getAudioInputStream(new File("ding2.wav"));
							 break;
						default:
							 audio = AudioSystem.getAudioInputStream(new File("ding3.wav"));
							 break;
						}
						 Clip clip= AudioSystem.getClip();
						 clip.open(audio);
						clip.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}
		}
	}
	//draws the GraphicsPiece objects in the correct spot as specified by the Puzzle class's solve() method
	public void drawSolvedPuzzle(GraphicsPiece[] pieces, int extraWidth, Puzzle p){
		for(int k=0; k<pieces.length; k++)
			pieces[k].resyncSides();
		for(int i=0; i<p.getNumRows(); i++)
			for(int j=0; j<p.getNumCols(); j++)
				for(int k=0; k<pieces.length; k++)
					if(p.getSpot(i, j)!=null && p.getSpot(i, j).equals(pieces[k].getPiece()))
						pieces[k].setCoords(squares[i][j].getX()+this.getX()+extraWidth-pieces[k].getWidth()/5, squares[i][j].getY()+this.getY()-pieces[k].getLength()/5);
	}
}
