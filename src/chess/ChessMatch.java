package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	private boolean check;//Inicia sempre como false
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();// chama o método no construtor para, quando iniciar a partida já existam as
						// peças
	}

	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
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
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){// Apenas a posição de origem. Destino solicita depois.
		Position position = sourcePosition.toPosition();//Converter p/ posição de matriz
		validateSourcePosition(position);//Possivel validação agora
		return board.piece(position).possibleMoves();
		//Método serve para imprimir movimentos possíveis a partir da posição de origem
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);//Método para validar posição de origem
		validateTargetPosition(source, target);//Método para validar posição de destino
		Piece capturedPiece = makeMove(source, target);//makeMove() realiza o movimento, capturedPiece é a peça a ser alterada 
		
		if (testCheck(currentPlayer)) {//Execulta o testCheck do currentPlayer, se estiver em check, desfaz o movimento e dá ChessException abaixo
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can put yourself in check");
		}
		//Abaixo = se o testCheck do opponent do currentPlayer for true, o check retorna true, senão false
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		nextTurn();
		return (ChessPiece)capturedPiece;//Fazer downcasting para sair da posição de matriz e virar uma peça
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);//retira possível peça de destino
		board.placePiece(p, target);//Coloca a peça p no lugar que estava a peça capturada
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);//Tirar do tabuleiro
			capturedPieces.add(capturedPiece);//Adicionar na lista de capturados
		}
		return capturedPiece;
		
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {//Método para desfazer o movimento
		Piece p = board.removePiece(target);//tirar a peça da posição de destino
		board.placePiece(p, source);//Devolver a peça a posição de origem
		
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);// retorna para posição de destino
			capturedPieces.remove(capturedPiece); //Tirar peça capturada da lista
			piecesOnTheBoard.add(capturedPiece);// Devolve para o tabuleiro
		}
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece in source position");//Essa exceção também é uma BoardException
		}
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {//fazer downcasting p/ ChessPiece senão não analisa a cor
			throw new ChessException("The chosen piece is not yours");
			
		}
		if (!board.piece(position).isThereAnyPossibleMove()) {//testa movimentos possíveis dentro do tabuleiro
			throw new ChessException("There is no possible moves for the chosen piece");
		}		
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).PossibleMove(target)) {
			//chamar uma peça no tabuleiro na posição de origem chamando um possibleMove() da posição de destino
			throw new ChessException("The chosen piece can't move to target position");//Essa exceção também é uma BoardException
		}		
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		//Lê-se: se o jogador atual for cor branca, passa a ser preto, senão passa a ser cor branca
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());// Coloca o toPosition para converter para
		piecesOnTheBoard.add(piece);// Para adicionar na lista peças do tabuleiro 	// posição de matriz
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ?  Color.BLACK : Color.WHITE;
		// Se a cor for igual white retorna Black, senão retorn White
	}
	
	private ChessPiece king(Color color) {//ABAIXO = Filtro em Lambda para varrer a lista e procurar nas peças a cor passada de parâmetro
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {//Para cada peça p na lista list
			if (p instanceof King) {
				return (ChessPiece)p;
			}			
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		//Faz-se isto para pegar a posição do King no formato de matriz
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		//Para pegar as peças do oponente
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();//Matriz de movimentos possíveis da peça adversária p
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {//Se a kingPosition na posição row e column for true, o King está em check
				return true;
			}
		}
		return false;		
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
