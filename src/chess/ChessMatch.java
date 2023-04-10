package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {// Essa classe deve saber o tamanho do tabuleiro, por isso essa instacia abaixo
							// 8/8
	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();// chama o m�todo no construtor para, quando iniciar a partida j� existam as
						// pe�as
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);//M�todo para validar posi��o de origem
		Piece capturedPiece = makeMove(source, target);//makeMove() realiza o movimento, capturedPiece � a pe�a a ser alterada 
		return (ChessPiece)capturedPiece;//Fazer downcasting para sair da posi��o de matriz e virar uma pe�a
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);//retira poss�vel pe�a de destino
		board.placePiece(p, target);
		return capturedPiece;
		
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece in source position");//Essa exce��o tamb�m � uma BoardException
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {//testa movimentos poss�veis dentro do tabuleiro
			throw new ChessException("There is no possible moves for the chosen piece");
		}
		
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());// Coloca o toPosition para converter para
																				// posi��o de matriz

	}

	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
