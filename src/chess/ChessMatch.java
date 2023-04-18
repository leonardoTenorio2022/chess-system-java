package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {// Essa classe deve saber o tamanho do tabuleiro, por isso essa instacia abaixo
							// 8/8
	private int turn;
	private Color currentPlayer;
	private Board board;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();// chama o m�todo no construtor para, quando iniciar a partida j� existam as
						// pe�as
	}
	
	

	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
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
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){// Apenas a posi��o de origem. Destino solicita depois.
		Position position = sourcePosition.toPosition();//Converter p/ posi��o de matriz
		validateSourcePosition(position);//Possivel valida��o agora
		return board.piece(position).possibleMoves();
		//M�todo serve para imprimir movimentos poss�veis a partir da posi��o de origem
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);//M�todo para validar posi��o de origem
		validateTargetPosition(source, target);//M�todo para validar posi��o de destino
		Piece capturedPiece = makeMove(source, target);//makeMove() realiza o movimento, capturedPiece � a pe�a a ser alterada 
		nextTurn();
		return (ChessPiece)capturedPiece;//Fazer downcasting para sair da posi��o de matriz e virar uma pe�a
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);//retira poss�vel pe�a de destino
		board.placePiece(p, target);
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
		
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece in source position");//Essa exce��o tamb�m � uma BoardException
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {//fazer downcasting p/ ChessPiece sen�o n�o analisa a cor
			throw new ChessException("The chosen piece is not yours");
			
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {//testa movimentos poss�veis dentro do tabuleiro
			throw new ChessException("There is no possible moves for the chosen piece");
		}
		
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).PossibleMove(target)) {
			//chamar uma pe�a no tabuleiro na posi��o de origem chamando um possibleMove() da posi��o de destino
			throw new ChessException("The chosen piece can't move to target position");//Essa exce��o tamb�m � uma BoardException
		}		
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		//L�-se: se o jogador atual for cor branca, passa a ser preto, sen�o passa a ser cor branca
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());// Coloca o toPosition para converter para
		piecesOnTheBoard.add(piece);// Para adicionar na lista pe�as do tabuleiro 	// posi��o de matriz
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
