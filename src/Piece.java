public class Piece { 

	private int[] sides; 
	//only used in solve - helps with recursion 
	private boolean isUsed = false; 
	//orientation of the piece - must be a multiple of 90
	private int orientation = 0;

	public final static int NORTH = 0; 
	public final static int EAST = 90; 
	public final static int SOUTH = 180; 
	public final static int WEST = 270; 

	public final static int SPADE_IN = -1; 
	public final static int SPADE_OUT = 1; 
	public final static int HEART_IN = -2; 
	public final static int HEART_OUT = 2; 
	public final static int DIAMOND_IN = -3; 
	public final static int DIAMOND_OUT = 3; 
	public final static int CLUB_IN = -4; 
	public final static int CLUB_OUT = 4; 

	//Takes in a value corresponding to each side of a specific piece
	//Creates an array containing the current values for each side of the piece
	public Piece(int n, int e, int s, int w){ 
		sides = new int[]{n,e,s,w};
	} 

	//Takes in the direction of the side it wants to access
	//The direction is a multiple of 90, and corresponds to 
	//	North, East, South, West
	public int getSide(int direction){ 
		return sides[direction/90]; 
	} 

	//Receives Nothing
	//Uses the correct side array and moves each value over one, and 
	//	puts the last value in the front
	public void rotate(){ 
		int temp = sides[3];
		for(int i = 2; i>=0; i--){ 
			sides[i+1]=sides[i];
		} 
		sides[0]=temp;
		orientation+=(orientation<=180)?90:-270;

	} 

	//Takes in nothing
	//Returns if the current Piece object is being currently used or not
	public boolean isUsed(){ 
		return isUsed; 
	} 

	//Receives a boolean 
	//Sets isUsed to specified boolean to indicate if it is being used
	public void setUsed(boolean used){ 
		isUsed=used; 
	} 
	
	//Receives nothing
	//Returns the stored orientation of the current Piece
	public int getOrientation(){
		return orientation;
	}
	
	//Receives the new orientation (multiple of 90)
	//Sets the stored orientation to the specified new orientation
	public void setOrientation(int newOrientation){
		orientation=newOrientation;
	}

} 