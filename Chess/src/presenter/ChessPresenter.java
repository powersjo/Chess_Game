package presenter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import view.ChessView;
import model.ChessModel;
import model.IChessPiece;
import model.Move;
import model.Player;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * The view class to build the view of the game.
 * @author Jonathan Powers, Jacob Hunsberger and Jared Thomas
 */
public class ChessPresenter {

	/**
	 * Size of board.
	 */
	private final int size = 8;
	/**
	 * Create local view.
	 */
	private ChessView view;
	/**
	 * Create local model.
	 */
	private ChessModel model;
	/**
	 * Select or place for the piece.
	 */
	private boolean select;
	
	/**
	 *  Selected space.
	 */
	private JButton space;
	/**
	 *  Local from row int.
	 */
	private int fromRow;
	/**
	 *  Local to row int.
	 */
	private int toRow;
	/**
	 *  Local from column int.
	 */
	private int fromColumn;
	/**
	 *  Local to column int.
	 */
	private int toColumn;
	/**
	 * Boolean in check.
	 */
	private boolean inCheck;
	
	/**
	 * Constructor for ChessPresenter Class.
	 * @param m model of chess
	 * @param v view of chess
	 */
	public ChessPresenter(final ChessModel m, final ChessView v) {
		model = m;
		view = v;
		setupTaken();
		updateBoard();
		select = false;
		inCheck = false;
		view.setVisible(true);
		
		view.addUndoButton(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				undo();
			}
			
		});
	}
	/**
	 * Undo functionality.
	 */
	private void undo() {
		model.undo();
		updateBoard();
		view.refresh();
	}
	/**
	 * Initializes the taken space labels.
	 */
	private void setupTaken() {
		for (int i = 0; i < size / 2; i++) {
			for (int j = 0; j < size / 2; j++) {
				final int fifty = 50;
				JLabel whiteLabel = new JLabel();
				whiteLabel.setPreferredSize(new Dimension(fifty, fifty));
				whiteLabel.setBackground(Color.WHITE);
				whiteLabel.setOpaque(true);
				view.setWhiteLabel(whiteLabel, i, j);
				
				JLabel blackLabel = new JLabel();
				blackLabel.setPreferredSize(new Dimension(fifty, fifty));
				blackLabel.setBackground(Color.WHITE);
				blackLabel.setOpaque(true);
				view.setBlackLabel(blackLabel, i, j);
			}
		}
	}
	/**
	 * Local class for mouse listener.
	 * @author Jonathan Powers, Jacob Hunsberger and Jared Thomas
	 *
	 */
	private class MyMouseListener implements MouseListener {
		/**
		 * The temp location of the row and column.
		 */
		private int tempRow = 0, tempCol = 0;
		@Override
		public void mouseEntered(final MouseEvent e) {
			JButton temp = (JButton) e.getSource();
			final int eight = 8;
			for (int i = 0; i < eight; i++) {
				for (int k = 0; k < eight; k++) {
					if (view.getPieceButton(i, k).equals(temp)) {
						tempRow = i;
						tempCol = k;
					}
				}
			}
			if (!select) {
				for (int i = 0; i < eight; i++) {
					for (int k = 0; k < eight; k++) {
						if (model.isValidMove(new Move(tempRow, 
								tempCol, i, k))) {
							Border thickBorder;
							final int twelve = 12;
							if (model.pieceAt(i, k) != null) {
								thickBorder = new LineBorder(Color.red, twelve);
								view.getPieceButton(i, k)
								.setBorder(thickBorder);
							} else {
								thickBorder = new LineBorder(Color.green,
										twelve);
								view.getPieceButton(i, k)
								.setBorder(thickBorder);
//								final int six = 6;
//								view.getPieceButton(tempRow, tempCol)
//								.setBorder(new LineBorder(Color.blue, six));
							}
						}
					}
				}
			}
		}
		@Override
		public void mouseExited(final MouseEvent e) {
			JButton temp = (JButton) e.getSource();
			final int eight = 8;
			for (int i = 0; i < eight; i++) {
				for (int k = 0; k < eight; k++) {
					if (view.getPieceButton(i, k).equals(temp)) {
						tempRow = i;
						tempCol = k;
					}
				}
			}
			if (!select) {
				for (int i = 0; i < eight; i++) {
					for (int k = 0; k < eight; k++) {
						if (model.isValidMove(new Move(tempRow, 
								tempCol, i, k))) {
						view.getPieceButton(i, k).setBorder(null);
						view.getPieceButton(tempRow, tempCol).setBorder(null);
						}
					}
				}
			}
		}
		@Override
		public void mousePressed(final MouseEvent e) {
		}
		@Override
		public void mouseReleased(final MouseEvent e) {
		}
		@Override
		public void mouseClicked(final MouseEvent e) {
		}
	}
	/**
	 * Local class for the button listener.
	 * @author Jonathan Powers, Jacob Hunsberger and Jared Thomas
	 *
	 */
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			int r = 0, c = 0;
			
			space = (JButton) e.getSource();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (space == view.getPieceButton(i, j)) {
						r = i;
						c = j;
					}
				}
			}
			
			if (!select) {
				if (model.pieceAt(r, c) != null) {
					if (model.pieceAt(r, c).player().
						equals(model.currentPlayer())) {
						select = true;
						fromRow = r;
						fromColumn = c;
						final int six = 6;
						view.getPieceButton(r, c)
						.setBorder(new LineBorder(Color.blue, six));
						r = size;
						c = size;
					}
				}
			} else {
				select = true;
				toRow = r;
				toColumn = c;
				Move m = new Move(fromRow, fromColumn, toRow, toColumn);
				if (!model.inCheck(m)) {
					model.move(m);
					int[] temp = model.findKing(model.currentPlayer());
					Move tempMove = new Move(m.getToRow(), m.getToColumn(), 
							temp[1], temp[0]);
					model.cyclePlayer();
					updateBoard();
					if (model.isValidMove(tempMove)) {
						inCheck = true;
					} else {
						inCheck = false;
					}
					model.cyclePlayer();
					if (inCheck) {
						highlightCheck(temp[1], temp[0]);
						if (model.isComplete()) {
							view.getPieceButton(temp[1], temp[0])
							.setBackground(Color.pink);
							ImageIcon icon = new ImageIcon(getClass()
									.getResource(
							"images/checkmate.gif"));
							JOptionPane.showMessageDialog(null, null,
									"Checkmate!", 1, icon);
						}
					}
				}
				select = false;	
			}
		}
	}
	/**
	 * Updates the board.
	 */
	private void updateBoard() {
		final float hue = 4.0f;
		final float sat = 0.25f;
		final float bright = 0.40f;	
		ButtonListener buttonListener = new ButtonListener();
		MyMouseListener mouseListener = new MyMouseListener();
		for (int i = 0; i < size; i++) {
			final int fifty = 50;
			for (int j = 0; j < size; j++) {
				space = new JButton();
				space.addActionListener(buttonListener);
				space.addMouseListener(mouseListener);
				space.setPreferredSize(new Dimension(fifty, fifty));
				
				if ((j % 2) == (i % 2)) {
					space.setBackground(Color.white);
				} else {
					space.setBackground(Color.getHSBColor(
							hue, sat, bright));
				}
				
				if (model.pieceAt(i, j) != null) {
					setImage(space, i, j, model.pieceAt(
							i, j).player());
				}
				view.setPieceButton(space, i, j);
			}
		}
		
		JLabel label = null;
		Iterator<IChessPiece> taken = model.getWhiteTaken();
		for (int i = 0; i < size / 2; i++) {
			for (int j = 0; j < size / 2; j++) {
				label = view.getWhiteLabel(i, j);
				if (!taken.hasNext()) {
					label.setIcon(null);
				}
				else {
					setImageTaken(label, taken.next());
				}
				view.setWhiteLabel(label, i, j);
			}
		}
		
		taken = model.getBlackTaken();
		for (int i = 0; i < size / 2; i++) {
			for (int j = 0; j < size / 2; j++) {
				label = view.getBlackLabel(i, j);
				if (!taken.hasNext()) {
					label.setIcon(null);
				}
				else {
					setImageTaken(label, taken.next());
				}
				view.setBlackLabel(label, i, j);
			}
		}
		
		view.refresh();
	}
	/**
	 * 
	 * @param label the label of the piece.
	 * @param piece the peice for the label.
	 */
	private void setImageTaken(final JLabel label, 
			final IChessPiece piece) {
		Player p = piece.player();
		try {
			ImageIcon is = null;
			if (piece.type() == "king") {
				is = new ImageIcon(getClass().getResource(
						"images/King/king" + p.toString() + ".png"));
			} else if (piece.type() == "queen") {
				is = new ImageIcon(getClass().getResource(
						"images/Queen/queen" + p.toString() + ".png"));
			} else if (piece.type() == "bishop") {
				is = new ImageIcon(getClass().getResource(
						"images/Bishop/bishop" + p.toString() + ".png"));
			} else if (piece.type() == "rook") {
				is = new ImageIcon(getClass().getResource(
						"images/Rook/rook" + p.toString() + ".png"));
			} else if (piece.type() == "knight") {
				is = new ImageIcon(getClass().getResource(
						"images/Knight/knight" + p.toString() + ".png"));
			} else if (piece.type() == "pawn") {
				is = new ImageIcon(getClass().getResource(
						"images/Pawn/pawn" + p.toString() + ".png"));
			}
			label.setBackground(Color.WHITE);
			label.setOpaque(true);
			label.setIcon(is);
		} catch (NullPointerException e) {
			//e.printStackTrace();
		}
	}
	
	/**
	 * Sets the image.
	 * @param b button
	 * @param r row
	 * @param c column
	 * @param p player
	 */
	private void setImage(final JButton b, final int r,
			final int c, final Player p) {
		try {
			ImageIcon is = null;
			if (model.pieceAt(r, c).player() == p) {
				if (model.pieceAt(r, c).type() == "king") {
					is = new ImageIcon(getClass().getResource(
							"images/King/king" + p.toString() + ".png"));
				} else if (model.pieceAt(r, c).type() == "queen") {
					is = new ImageIcon(getClass().getResource(
							"images/Queen/queen" + p.toString() + ".png"));
				} else if (model.pieceAt(r, c).type() == "bishop") {
					is = new ImageIcon(getClass().getResource(
							"images/Bishop/bishop" + p.toString() + ".png"));
				} else if (model.pieceAt(r, c).type() == "rook") {
					is = new ImageIcon(getClass().getResource(
							"images/Rook/rook" + p.toString() + ".png"));
				} else if (model.pieceAt(r, c).type() == "knight") {
					is = new ImageIcon(getClass().getResource(
							"images/Knight/knight" + p.toString() + ".png"));
				} else if (model.pieceAt(r, c).type() == "pawn") {
					is = new ImageIcon(getClass().getResource(
							"images/Pawn/pawn" + p.toString() + ".png"));
				}
			}
			b.setIcon(is);
		} catch (NullPointerException e) {
			//e.printStackTrace();
		}
	}
	/**
	* Highlights a check.
	* @param row Row to highlight.
	* @param col Column to highlight.
	*/
	private void highlightCheck(final int row, final int col) {
		view.getPieceButton(row, col).setBackground(Color.yellow);
	}
}