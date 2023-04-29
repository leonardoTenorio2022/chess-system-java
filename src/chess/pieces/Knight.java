package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece{

	public Knight(Board board, Color color) {
		super(board, color);
	}
	
	private boolean canMove(Position position) {// Método p/ testar se a posição não é vazia nem adversária
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean [getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);
		
			// mov 1
			p.setValues(position.getRow() + 2, position.getColumn() - 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			// mov 2 
			p.setValues(position.getRow() + 1, position.getColumn() - 2);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			// mov 3 
			p.setValues(position.getRow() + 2, position.getColumn() + 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			// mov 4
			p.setValues(position.getRow() + 1, position.getColumn() + 2);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			// mov 5
			p.setValues(position.getRow() - 1, position.getColumn() - 2);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			// mov 6
			p.setValues(position.getRow() - 2, position.getColumn() - 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			// mov 7 
			p.setValues(position.getRow() - 1, position.getColumn() + 2);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			//se = sudeste
			p.setValues(position.getRow() - 2, position.getColumn() + 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		
		return mat;
	}
	
	@Override
	public String toString() {
		return "N";
	}

}
