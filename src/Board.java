/*
 * is an array of Pieces
 * has the basic functions of a Board
 * the purpose is to be used in the Puzzle class to put pieces on it
 * 
 * 3/26/14
 * @author Henry Grover
 */
public class Board {
	private Piece[][] board;

	//Takes in a value for the dimensions of the board
	//uses the dimension to create a board that has "sidelength" number of pieces on each side
	public Board(int sideLength){
		board = new Piece[sideLength][sideLength];	
	}

	//Takes in a number of rows and a number of cols
	//Creates a board with the specified dimension 
	public Board(int numRows, int numCols){
		board=new Piece[numRows][numCols];
	}

	//Takes in only an Array of Pieces
	//Since board is only a Piece array, it just sets the given piece array to board
	public Board(Piece[][] p){
		board = p;
	}

	//Given an ordered pair for a Piece
	//Calls getSpot which contains an isValid(), 
	//	and returns whether the piece is empty or not
	public boolean isEmpty(int row, int col){
		return (getSpot(row,col)==null);
	}

	//Uses the ordered pair given to check if the specified 
	//	spot is on the board, and returns a boolean accordingly
	public boolean isValid(int row, int col){
		return (row<board.length && row>=0 && col>=0 && col<board[0].length);
	}

	//Takes in an ordered pair for the position of a spot on the board
	//uses the coordinates to return what has been placed there, null if nothing
	public Piece getSpot(int row, int col){
		if(isValid(row,col))
			return board[row][col];
		else return null;
	}

	//Uses the ordered pair given and sets that spot on the board 
	//	to whatever piece (or null) given in the parameters
	//*If a piece was there before it was set, the piece replaced is returned
	public Piece setSpot(Piece p, int row, int col){
		Piece temp = getSpot(row,col);
		if(isValid(row,col)){
			board[row][col]=p;
			if(p != null)
				p.setUsed(true);
		}
		if(temp!=null)
			temp.setUsed(false);
		return temp;
	}

	//Receives nothing
	//Iterates through the board and sets all spots to null
	public void clearAll(){	
		for(int row=0;row<getNumRows();row++)
			for(int col=0;col<getNumCols();col++)
				setSpot(null,row,col);
	}

	//Takes in an ordered pair and sets that spot on the board
	//	to null, and returns the Piece replaced if there was one
	public Piece remove(int row, int col){
		return setSpot(null,row,col);
	}

	//Recieves Nothing
	//Iterates through the board using a for-each loop, and if
	//	if finds a piece in any spot it immediately returns false.
	//If it gets through the board without finding a piece, it returns true.
	public boolean isFull(){
		for(Piece[] p: board)
			for(Piece pc: p)
				if(pc==null) 
					return false;
		return true;
	}

	//Receives nothing
	//Finds and returns the number of rows of the board
	public int getNumRows(){
		return board.length;
	}
	
	//Receives nothing
	//Finds and returns the number of cols of the board
	public int getNumCols(){
		return board[0].length;
	}
}
