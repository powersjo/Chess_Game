package view;

import javax.swing.JFrame;
import presenter.ChessPresenter;

/**
 * The main class to start the game.
 * @author Jonathan Powers, Jacob Hunsberger and Jared Thomas
 */
public final class ChessView {
	/**
	 * Default private constructor.
	 */
	/**
	 * The JFrame for the whole chess game.
	 */
	private static JFrame frame = new JFrame("Chess Game");
	private static ChessMenu menu = new ChessMenu();
	private ChessView() {
	}
	/**
	 * The main method to call to start the view of the chess game.
	 * @param args the default way to start the main method
	 */
	public static void main(final String[] args) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ChessPresenter panel = new ChessPresenter();
		frame.getContentPane().add(panel);
		frame.setJMenuBar(menu);
		frame.pack();
		frame.setVisible(true);
		
	}
	/**
	 * This method resets the game of chess.
	 */
	protected static void reset(){
		frame.dispose();
		frame.removeAll();
		frame = new JFrame("Chess Game");
	}
	/**
	 * This method closes the game of chess. 
	 */
	protected static void exit() {
		frame.dispose();
		frame.getDefaultCloseOperation();
	}
}