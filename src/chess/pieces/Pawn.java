package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;


public class Pawn extends ChessPiece {
	
	private ChessMatch chessMatch;//Precisa da ref da partida devido o El Passant
	
	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
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
			
			// #Special move en passant White
			if (position.getRow() == 3) { //S� pode ser feito se estiver na pos 5 do Xadrez
				Position left = new Position(position.getRow(), position.getColumn() - 1);// Para ver se tem oponente na coluna anterior
				if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()){
				// teste se a posi��o existe, se exite um oponente e se a pe�a esq est� na posi��o de en passant
				mat[left.getRow() - 1][left.getColumn()] = true;//Ele n�o move para pos do advers�rio e sim para linha acima do adversario
				}				
				Position right = new Position(position.getRow(), position.getColumn() + 1);// Para ver se tem oponente na coluna anterior
				if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()){
					// teste se a posi��o existe, se exite um oponente e se a pe�a esq est� na posi��o de en passant
					mat[right.getRow() - 1][right.getColumn()] = true;//Ele n�o move para pos do advers�rio e sim para linha acima do adversario
				}				
			}	
		}		
		else {
			p.setValues(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {//Se a posi��o na linha acima dele existir e estiver vazia ele pode mover at� l�
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			p.setValues(position.getRow() + 2, position.getColumn());
			Position pos2 = new Position(position.getRow() + 1, position.getColumn());//Testa se a posi��o anterior est� tamb�m vazia, por isso da vari�vel0 
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(pos2) && !getBoard().thereIsAPiece(pos2) && getMoveCount() == 0) {//Se a posi��o na linha acima dele existir e estiver vazia e for a primeira jogada ele pode mover at� l�
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
			
			// #Special move en passant Black
			if (position.getRow() == 4) { //S� pode ser feito se estiver na pos 5 do Xadrez
				Position left = new Position(position.getRow(), position.getColumn() - 1);// Para ver se tem oponente na coluna anterior
				if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()){
				// teste se a posi��o existe, se exite um oponente e se a pe�a esq est� na posi��o de en passant
				mat[left.getRow() + 1][left.getColumn()] = true;//Ele n�o move para pos do advers�rio e sim para linha acima do adversario
				}			
				Position right = new Position(position.getRow(), position.getColumn() + 1);// Para ver se tem oponente na coluna anterior
				if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()){
					// teste se a posi��o existe, se exite um oponente e se a pe�a esq est� na posi��o de en passant
					mat[right.getRow() + 1][right.getColumn()] = true;//Ele n�o move para pos do advers�rio e sim para linha acima do adversario
				}				
			}
		}
		return mat;
	}			
	
	@Override
	public String toString() {
		return "P";
	}

}
