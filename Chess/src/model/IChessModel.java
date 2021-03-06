package model;

/**
 * Objects implementing this interface represent the state of a chess game.
 * Notice that this interface is designed to maintain the game state only,
 * it does not provide any methods to control the flow of the game.
 * @author Jonathan Powers, Jacob Hunsberger and Jared Thomas
 */

public interface IChessModel {

   /**
    * Returns whether the game is complete.
    * @return true if complete, false otherwise.
    */
   boolean isComplete();

   /**
    * Returns whether the piece at location [move.fromRow, move.fromColumn]
    * is allowed to move to location [move.fromRow, move.fromColumn].
    * @param move a Move object describing the move to be made.
    * @return true if the proposed move is valid, false otherwise. 
    * move.fromColumn] or [move.toRow,move.toColumn] 
    * don't represent valid locations on the board.
    */
   boolean isValidMove(Move move);

   /**
    * Moves the piece from location [move.fromRow, move.fromColumn] to
    * location [move.fromRow,move.fromColumn].
    * @param move a Move object describing the move to be made.
    */
   void move(Move move);

   /**
    * Report whether the current player is in check.
    * @return true if the current player is in check, false otherwise.
    * @param m input the move that the player makes.
    */
   boolean inCheck(Move m);

   /**
    * Return the current player.
    * @return the current player
    */
   Player currentPlayer();
      
   /**
    * Return the ChessPiece object at location [row, column].
    *
    * @param row    the row (numbered 0 through numRows -1
    * @param column the column (numbered {@code 0} through {@code numColumns -1}
    * @return the {@code ChessPiece} object at location {@code [row, column]}.
    */
    IChessPiece pieceAt(int row, int column);
	
}
