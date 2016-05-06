import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;

/*
 * Mario	Puzzle Project		Mason Liu, Nonu Bajaj, Yamini Nambiar
 * 
 *  Mario contains methods move(), run(), jump(), dance(), isDone(), getters, and setters for private data. The Mario class 
 *  stores all relevant data related to the Mario animation upon completing the puzzle
 */
public class Mario {
	private double x;
	private double y;
	private int rIndex;
	private int dIndex;
	private int jIndex;
	private boolean isDone;
	private BufferedImage[][] sprites;
	private long lastCalled;
	//constructor-reads the sprite sheet to generate a 2d array of BufferedImages- one array for running, jumping, and victory pose
	public Mario(int x, int y){
		lastCalled=new Date().getTime();
		this.x=x;
		this.y=y;
		dIndex=-1;
		rIndex=-1;
		jIndex=-1;
		isDone=false;
		sprites=new BufferedImage[3][8];
		BufferedImage spriteSheet;
		try {
			spriteSheet=ImageIO.read(new File("mario_sprites.png"));
			sprites[0][0]=spriteSheet.getSubimage(137, 0, 16, 30);
			sprites[0][1]=spriteSheet.getSubimage(157, 0, 16, 30);
			sprites[0][2]=spriteSheet.getSubimage(178, 0, 21, 30);
			sprites[1][0]=spriteSheet.getSubimage(16, 0, 16, 30);
			sprites[1][1]=spriteSheet.getSubimage(31, 0, 21, 30);
			sprites[1][2]=spriteSheet.getSubimage(57, 0, 16, 30);
			sprites[1][3]=spriteSheet.getSubimage(77, 0, 17, 30);
			sprites[2][0]=spriteSheet.getSubimage(147, 32, 15, 29);
			sprites[2][1]=spriteSheet.getSubimage(166, 32, 16, 29);
			sprites[2][2]=spriteSheet.getSubimage(185, 32, 16, 29);
			sprites[2][3]=spriteSheet.getSubimage(204, 32, 16, 29);
			sprites[2][4]=spriteSheet.getSubimage(223, 32, 21, 29);
			sprites[2][5]=spriteSheet.getSubimage(247, 32, 20, 29);
			sprites[2][6]=spriteSheet.getSubimage(271, 32, 16, 29);
			sprites[2][7]=spriteSheet.getSubimage(90, 62, 16, 28);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//decides whether mario will run or dance
	public BufferedImage move(int limit){
		if(x>limit)
			return dance();
		return run();
	}
	
	//returns the next Image for running and adjusts x and y accordingly
	public BufferedImage run(){
		if(new Date().getTime()>lastCalled+700){
		x+=3;
		rIndex=(rIndex+1)%3;
		}
		return sprites[0][Math.max(0, rIndex)];
	}
	//returns the next Image for jumping and adjusts x and y accordingly
	public BufferedImage jump(){
		if(new Date().getTime()>lastCalled+700){
		jIndex=Math.min(3, jIndex+1);
		}
		return sprites[1][Math.max(0, jIndex)];
	}
	//returns the next Image for dancing 
	public BufferedImage dance(){
		if(new Date().getTime()>lastCalled+700){
		dIndex=Math.min(dIndex+1, 7);
		if(dIndex==7)
			isDone=true;
		}
		return sprites[2][Math.max(0, dIndex)];
	}
	//checks if the mario animation is done
	public boolean isDone(){
		return isDone;
	}
	//gets X Coordinate
	public int getX() {
		return (int)x;
	}
	//gets Y Coordinate 
	public int getY() {
		return (int)y;
	}
	//sets the X and Y Coordinates of the animation
	public void setCoords(double x, double y) {
		this.x = x;
		this.y= y;
	}
}
