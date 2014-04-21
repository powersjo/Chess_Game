package model;

import java.util.Random;

public class Computer {
	private Player player;
	private int level;
	private ChessModel localModel;
	private boolean completeMove;
	public Computer(Player p, int l, ChessModel cm) {
		player = p;
		level = l;
		localModel = cm;
		completeMove = false;
	}
	public Player getPlayer() {
		return player;
	}
	public int getLevel() {
		return level;
	}
	public void resetLevel(int l) {
		level = l;
	}
	public void move() {
		if(level < 2) {
			levelOne();
		} else if(level == 2) {
			
			if(!completeMove) {
				levelOne();
			}
		} else if(level == 3) {
			
		} else if(level == 4) {
			
		} else {
			
		}
	}
	private void levelOne() {
		Random random = new Random();
		while(!completeMove) {
			int tempRow = random.nextInt(8);
			int tempCol = random.nextInt(8);
			Move m;
			if (localModel.pieceAt(tempRow, tempCol).player().equals(player)) {
				if(player.equals(Player.WHITE)) {
					for (int i = 7; i > -1; i--) {
						for (int k = 7; k > -1; k--) {
							if(localModel.isValidMove(m = new Move(tempRow, tempCol, i, k))) {
								localModel.move(m);
							}
						}
					}
				} else {
					for (int i = 0; i < 8; i++) {
						for (int k = 0; k < 8; k++) {
							if(localModel.isValidMove(m = new Move(tempRow, tempCol, i, k))) {
								localModel.move(m);
							}
						}
					}
				}
			}
		}
	}
}