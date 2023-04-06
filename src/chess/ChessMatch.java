package chess;

import boardgame.Board;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {//Essa classe deve saber o tamanho do tabuleiro, por isso essa instacia abaixo 8/8
	
	private Board board;
	
	public ChessMatch() {
		board = new Board(8,8);
		initialSetup();//chama o m�todo no construtor para, quando iniciar a partida j� existam as pe�as
	}
		
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece)board.piece(i, j);
			}			
		}
		return mat;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());//Coloca o toPosition para converter para posi��o de matriz
		
	}
	
	private void initialSetup() {
		placeNewPiece('b', 6, new Rook(board, Color.WHITE));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
	}

}
