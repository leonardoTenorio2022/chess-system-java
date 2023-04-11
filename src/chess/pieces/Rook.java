package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "R";//Imprime a letra no tabuleiro, por isso toString() desta forma 
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean [][] mat = new boolean [getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);//Apenas para ter uma posição inicial
		
		//above = acima 
		p.setValues(position.getRow() - 1, position.getColumn());
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {//enquanto existir a posição mas sem peças 
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow()-1);			
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//left = esquerda
		p.setValues(position.getRow() , position.getColumn() - 1);
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {//enquanto existir a posição mas sem peças 
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1);			
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//right = esquerda
		p.setValues(position.getRow() , position.getColumn() + 1);
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {//enquanto existir a posição mas sem peças 
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);			
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//below = abaixo 
		p.setValues(position.getRow() + 1, position.getColumn());
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {//enquanto existir a posição mas sem peças 
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);			
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		return mat;
	}

}
