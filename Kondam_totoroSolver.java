import java.util.*;

public class Kondam_totoroSolver{

	private PriorityQueue<Board> boardSol = new PriorityQueue<Board>();
	private Board initBoard;  
	private final int[] VERT_DISP = {0,0,1,-1};  
	private final int[] HORZ_DISP = {-1,1,0,0};

	//constructor
	public Kondam_totoroSolver(int [][] board) {
		initBoard = new Board(board, 0, null);
		boardSol.add(initBoard);
	}

	//implements a search algorithm to determine the optimal moves in order to solve the totoro slider
	public Board searchAlg() {

		//algorithm isn't run if the initial board isn't solveable
		if(!initBoard.isSolveable()) {
			System.out.println("The board is unsolveable.");
			return initBoard; 
		}

		//continuously runs until the board has generated the final state		
		while(!boardSol.peek().gameOver()) {


			Board highPriority = boardSol.remove(); 

			for(int i = 0; i < VERT_DISP.length; i++) {

				int totRow = highPriority.totLoc[0];
				int totCol = highPriority.totLoc[1];

				//checks if possible moves are within dimensions
				if(!highPriority.equals(highPriority.previousBoard) && totRow + VERT_DISP[i] < highPriority.curBoard.length && totRow + VERT_DISP[i] >= 0 && 
				   totCol + HORZ_DISP[i] < highPriority.curBoard.length && totCol + HORZ_DISP[i] >=0){ 

					//creates new board with the potential swaps and adds to the priority queue
					Board newMove = highPriority.Swap(totRow + VERT_DISP[i], totCol + HORZ_DISP[i]); 

					if(!newMove.equals(highPriority.previousBoard))
						boardSol.add(newMove);
				}
			}
		}

		Board finalBoard = boardSol.peek(); 
		Stack<Board> printMoves = new Stack<Board>(); 

		//adds all of the previous moves on the stack
		while(finalBoard.previousBoard!=null) {
			finalBoard = finalBoard.previousBoard; 
			printMoves.add(finalBoard);
		}

		//prints all of the previous board
		System.out.println("All Grids");
		while(!printMoves.isEmpty()) 	
			printMoves.pop().print();

		System.out.println("Total Moves: " + boardSol.peek().movesMade);

		return boardSol.peek();

	}


	//INNER CLASS
	public class Board implements Comparable<Board> {

		int [] [] curBoard; 
		int movesMade; 
		Board previousBoard; 
		int [] totLoc; 
		int manhattan; 

		public Board(int [][] board, int moves, Board prev) {

			curBoard = new int[board.length][board.length];
			totLoc = new int[2];
			previousBoard = prev; 
			movesMade = moves; 


			for(int row = 0; row < board.length; row ++) {

				for(int col = 0; col < board.length; col ++) {

					curBoard[row][col] = board[row][col];

					if(board[row][col] == 0) {
						totLoc[0] = row; 
						totLoc[1] = col;
					}
				}
			}

			manhattan = Manhattan();

		}

		//computes how close the total board is to the solution
		public int Manhattan() {

			int manhattan = 0;

			for(int row = 0; row < curBoard.length; row ++) {

				for(int col = 0; col < curBoard.length; col++) {

					curBoard[totLoc[0]][totLoc[1]] =  (curBoard.length * row) + col + 1; 

					int origRow = (curBoard[row][col]-1) / curBoard.length; 
					int origCol = (curBoard[row][col]-1) % curBoard.length; 
					manhattan += Math.abs(row - origRow) + Math.abs(col - origCol);

				}
			}

			curBoard[totLoc[0]][totLoc[1]] = 0; 
			return manhattan; 
		}

		//prints out the board
		public void print() {

			for(int row = 0; row < curBoard.length;row++) {

				System.out.println("\n");

				for(int col = 0; col < curBoard[row].length;col++) 
					System.out.print(curBoard[row][col] + "\t");
			}

			System.out.println("\n");	
		}


		//swaps out the totoro value with the row and col
		public Board Swap(int row, int col) {

			Board updateBoard = new Board(curBoard,movesMade+1,this);

			updateBoard.curBoard[row][col] = 0; 
			updateBoard.curBoard[totLoc[0]][totLoc[1]] = curBoard[row][col];

			return updateBoard; 
		}

		//checks if the board can be solved
		public boolean isSolveable() {

			int totalLength = curBoard.length * curBoard[0].length; 
			int outOfOrder = 0; 
			int curRow = 0; 
			int curCol = 0; 

			while(totalLength!=0) {

				if(curCol == curBoard.length - 1) {
					curCol = 0; 
					curRow++; 
				}

				for(int row = curRow; row < curBoard.length; row ++) {

					for(int col = curCol; col < curBoard.length; col ++) {

						//inspects each position to its position and the positions after
						if(curBoard[curRow][curCol] > curBoard[row][col]) 
							outOfOrder++;		
					}
				}

				curCol++; 
				totalLength--; 
			}

			if(curBoard.length % 2 !=0) {

				if(outOfOrder % 2 == 0) 
					return true; 

				else
					return false; 
			}

			else {

				if(totLoc[0] % 2 == 0 || totLoc[0] == 0 && outOfOrder % 2 != 0)
					return true;

				else if(totLoc[0] % 2 != 0 && outOfOrder % 2 == 0)
					return true; 
			}

			return false; 
		}

		public boolean gameOver() { 
			return manhattan == 0; 
		}

		public int compareTo(Board other) {
			return (this.manhattan+ this.movesMade) - 
					(other.manhattan+other.movesMade);
		}

		public boolean equals(Object other){

			if(!(other instanceof Board))
				return false;

			Board otherBoard = (Board)other;

			for(int r = 0; r < curBoard.length; r++){
				for(int c = 0; c < curBoard[0].length; c++)
					if(curBoard[r][c] != otherBoard.curBoard[r][c])
						return false;
			}
			return true;
		}
	}

	public static void main(String[]args) {

		int mat[][] = {{1,2,3}, {4,5,6},{7,0,8}};
		Kondam_totoroSolver obj = new Kondam_totoroSolver(mat);
		obj.searchAlg();
	}
}