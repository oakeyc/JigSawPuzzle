import java.awt.Point;
import java.util.ArrayList; 


/*
 * is a jigsaw puzzle
 * contains Board and an ArrayList of pieces
 * can solve itself with the solve method
 * 
 * @author Henry Grover and Courtney Oka
 */
public class Puzzle 
{ 
	private ArrayList<Piece> pieceList = new ArrayList<Piece>(); 
	private Board board;

	/*
	 * ctor
	 * takes in an array of pieces and a dimensions for a board
	 * adds all the pieces from the array into the arraylist and makes a new board
	 */
	public Puzzle(Piece[] p,int row, int col){ 
		for(Piece piece:p)
			pieceList.add(piece);
		board = new Board(row,col); 

	} 

	/*
	 * ctor
	 * takes in array of pieces and dimensions for a board
	 * calls other ctor
	 */
	public Puzzle(ArrayList<Piece> p, int row, int col) { 
		this(p, new Board(row, col));
	} 

	/*
	 * ctor
	 * takes in arraylist and board
	 * sets the board to the data and arraylist to the data
	 */
	public Puzzle(ArrayList<Piece> p, Board board){
		this.board=board;
		pieceList=p;

	}

	/*
	 * receives nothing
	 * solves the board with the pieces
	 * if it's already solved, finds another solution (aka rotate everything)
	 * returns nothing
	 */
	public void solve(){
		if(board.isFull())
		{
			Board temp = new Board(board.getNumRows(), board.getNumCols());

			for(int row = 0; row<board.getNumRows(); row++)
			{
				for(int col = 0; col<board.getNumCols(); col++)
				{
					Point point = findOtherSolvePieceLocation(row, col);
					Piece p = board.getSpot(row, col);
					p.rotate();
					temp.setSpot(p, point.x, point.y );
				}
			}

			for(int row = 0; row<board.getNumRows(); row++)
				for(int col = 0; col<board.getNumCols(); col++)
				{
					board.setSpot(temp.getSpot(row, col), row, col);
				}		
			return;
		}

		ArrayList<Piece> piecesLeft = new ArrayList<Piece>(pieceList);
		clear();
		solve(0,0,piecesLeft);
	}

	/*
	 * receives a location, and arraylist of pieces left
	 * recursively backtracks 
	 * figures out if a piece belongs in that spot, if it does put it there
	 * then find the piece for the next spot
	 * otherwise it goes through the list of pieces
	 * if it reaches the end, then it returns false
	 * if it returns true, then that piece fits there
	 */
	public boolean solve(int row, int col, ArrayList<Piece> piecesLeft) {	
		if (isSolved())
			return true;
		for (Piece p : piecesLeft){
			for(int orientation = 0;!p.isUsed() && orientation < 4; orientation++){
				if (canFit(p, row, col)){
					Piece temp = board.setSpot(p, row, col);
					ArrayList<Piece> piecesLeft2 = new ArrayList<Piece>(piecesLeft);
					piecesLeft2.remove(p);
					if(temp!=null)
						piecesLeft2.add(temp);

					Point next = findNextLocation(row, col);
					if(next==null)
						return true;

					if(!solve(next.x, next.y, piecesLeft2)){
						board.remove(row, col);
					}
					else{
						solve(next.x, next.y, piecesLeft2); return true;
					}
				}
				p.rotate();
			}
		}
		return false;
	}

	/*
	 * receives a location
	 * figures out what the next location when solving the board should be
	 * returns the next Point
	 * otherwise returns null is the spot is at the end
	 */
	public Point findNextLocation(int row, int col){		
		if(row<board.getNumRows()-1){
			if(col<board.getNumCols()-1)
				return new Point(row,col+1);
			else
				return new Point(row+1, 0);
		}
		else if(row==board.getNumRows()-1){
			if(col<board.getNumCols()-1)
				return new Point(row, col+1);
			else return null;
		}
		else return null;
	}

	/*
	 * receives a location
	 * returns a new Point that is the location when the board is rotated once clockwise
	 */
	public Point findOtherSolvePieceLocation(int row, int col)
	{
		return new Point(col,(board.getNumCols()-row)-1);
	}

	/*
	 * receives a piece and a location
	 * figures out if the piece is compatible with the pieces around it
	 * returns a boolean of whether it can go there or not
	 */
	public boolean canFit(Piece p, int row, int col){
		if(!board.isValid(row, col))
			return false;
		if(board.getSpot(row-1, col)==null|| p.getSide(Piece.NORTH) + board.getSpot(row-1, col).getSide(Piece.SOUTH)==0)
			if(board.getSpot(row, col+1)==null||p.getSide(Piece.EAST) + board.getSpot(row, col+1).getSide(Piece.WEST)==0)
				if(board.getSpot(row, col-1)==null|| p.getSide(Piece.WEST) + board.getSpot(row, col-1).getSide(Piece.EAST)==0)
					if(board.getSpot(row+1, col)==null||  p.getSide(Piece.SOUTH) + board.getSpot(row+1, col).getSide(Piece.NORTH)==0)
						return true;
		return false;
	}

	/*
	 * receives nothing 
	 * returns the size of the arraylist of pieces
	 */
	public int getNumberOfPieces(){ 
		return pieceList.size(); 
	} 

	/*
	 * receives nothing
	 * clears the board
	 * returns nothing
	 */
	public void clear(){ 
		board.clearAll();  
	} 

	/*
	 * receives a piece and location
	 * returns a piece from the board setSpot(piece, int, int)
	 */
	public Piece setSpot(Piece p, int row, int col){  
		return board.setSpot(p,row,col); 
	} 

	/*
	 * receives location
	 * returns the piece in that location
	 */
	public Piece getSpot(int row, int col){ 
		return board.getSpot(row, col);  
	} 

	/*
	 * receives a location
	 * returns the piece that was there, and sets that spot to null
	 */
	public Piece remove(int row, int col){ 
		return board.remove(row, col);  
	} 

	/*
	 * receives nothing
	 * returns the number of rows
	 */
	public int getNumRows(){ 
		return board.getNumRows();  
	} 

	/*
	 * receives nothing
	 * return the number of columns on the board
	 */
	public int getNumCols(){ 
		return board.getNumCols(); 
	} 

	/*
	 * receives nothing
	 * if any of the pieces are not used
	 * returns false
	 * if they are all used
	 * returns true
	 */
	public boolean isSolved(){
		for(Piece p: pieceList){
			if(!p.isUsed())
				return false;
		}
		return true;
	}
} 