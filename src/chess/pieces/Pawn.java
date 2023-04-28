package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;


public class Pawn extends ChessPiece {
	
	public Pawn(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];//Cria-se a matriz booleana para localizar dentro do tabuleiro

		Position p = new Position(0, 0);// Apenas para ter uma posi��o inicial
		
		if (getColor() == Color.WHITE) {
			p.setValues(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {//Se a posi��o na linha acima dele existir e estiver vazia ele pode mover at� l�
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() - 2, position.getColumn());
			Position p2 = new Position(position.getRow() - 1, position.getColumn());//Testa se a posi��o anterior est� tamb�m vazia, por isso da vari�vel 
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {//Se a posi��o na linha acima dele existir e estiver vazia e for a primeira jogada ele pode mover at� l�
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() - 1, position.getColumn() - 1);//Para verif se h� advers�rio nessa posi��o
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {//Se a posi��o na linha acima dele existir e tiver um oponente nela, pode mover
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() - 1, position.getColumn() + 1);//Para verif se h� advers�rio nessa posi��o
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {//Se a posi��o na linha acima dele existir e tiver um oponente nela, pode mover
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		else {
			p.setValues(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {//Se a posi��o na linha acima dele existir e estiver vazia ele pode mover at� l�
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() + 2, position.getColumn());
			Position p2 = new Position(position.getRow() + 1, position.getColumn());//Testa se a posi��o anterior est� tamb�m vazia, por isso da vari�vel 
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {//Se a posi��o na linha acima dele existir e estiver vazia e for a primeira jogada ele pode mover at� l�
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() + 1, position.getColumn() - 1);//Para verif se h� advers�rio nessa posi��o
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {//Se a posi��o na linha acima dele existir e tiver um oponente nela, pode mover
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() + 1, position.getColumn() + 1);//Para verif se h� advers�rio nessa posi��o
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {//Se a posi��o na linha acima dele existir e tiver um oponente nela, pode mover
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		return mat;
	}
	
	@Override
	public String toString() {
		return "P";
	}

}
