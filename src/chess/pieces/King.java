package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{
	
	private ChessMatch chessMatch;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}
	
	@Override
	public String toString() {
		return "K";
	}
	
	private boolean canMove(Position position) {// Método p/ testar se a posição não é vazia nem adversária
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}
	
	private boolean testRookCastling (Position position) {//Método para testar a condição especial Castling na pos informada
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
		//Se toda condição acima acontecer, o rei está apto para a jogada Roque
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean [][] mat = new boolean [getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);
		
		//Above = acima
		p.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		//below = abaixo
		p.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		//left = esquerda
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		//right = direita
		p.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		//nw = noroeste
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		//ne = nordeste
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		//sw = sudoeste
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		//se = sudeste
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//# Special move Castling
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {// Método de teste para fazer o Roque
			//# Special move Castling Kingside Rook (lado do rei)
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
			if (testRookCastling(posT1)) {//Usa-se o método criado para ver se a torre está lá
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);//1ª casa do lado do rei
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);//2ª casa do lado do rei
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {//Pos do rei + 2 torna-se mov possível
					mat[position.getRow()][position.getColumn() + 2] = true;
				}
			}			
		}
				
			//# Special move Castling Queenside Rook (lado do rei)
			Position posT2 = new Position(position.getRow(), position.getColumn() - 4);
			if (testRookCastling(posT2)) {//Usa-se o método criado para ver se a torre está lá
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);//1ª casa do lado da rainha
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);//2ª casa do lado da rainha
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);//2ª casa do lado da rainha
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
					mat[position.getRow()][position.getColumn() - 2] = true;
				}
			}		
		
		return mat;
	}

}
