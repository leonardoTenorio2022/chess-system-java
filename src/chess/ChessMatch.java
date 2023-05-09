package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {// Essa classe deve saber o tamanho do tabuleiro, por isso essa instacia abaixo
							// 8/8
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;//Inicia sempre como false
	private boolean checkMate;//Inicia sempre como false
	private ChessPiece enPassantVulnerable;// Inicia como null
	private ChessPiece promoted;
	
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
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
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
		
		if (testCheck(currentPlayer)) {//Execulta o testCheck do currentPlayer, se estiver em check, desfaz o movimento e d� ChessException abaixo
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can put yourself in check");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);//Para pegar a pe�a que se moveu
		
		//#Special move promotion [Fazer antes do testCheck pois, ap�s a promo��o, pode ser que o advers�rio fique em check
		promoted = null;//Iniciar desta forma
		if (movedPiece instanceof Pawn) {//Ver se � um pe�o
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {//Se a pe�a for branca e a linha detino for igual a zero, ou a preta igual a 7 � pq chegou ao final
				promoted = (ChessPiece)board.piece(target);	//Promoted recebe a posi��o de destino
				promoted = replacePromotedPiece("Q");//Por padr�o troca-se pela Rainha, depois pergunta por qual pe�a deseja alterar
			}
		}		
		
		//Abaixo = se o testCheck do opponent do currentPlayer for true, o check retorna true, sen�o false
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponent(currentPlayer))) {//Se der true o jogo acaba
			checkMate = true;
		}
		else {
			nextTurn();			
		}
		
		// # Special move en passant
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)){
			// Ver se a pe�a movida � um Pawn e se a pos de target � igual a de origem - 2/+2
			enPassantVulnerable = movedPiece;// Recebe a pe�a movida		
		}
		else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece)capturedPiece;//Fazer downcasting para sair da posi��o de matriz e virar uma pe�a
	}
	
	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {//Se a pe�a promovida for null n�o � poss�vel alt, somente se for Pawn
			throw new IllegalStateException("There is no piece to be promoted");
		}
		//Por isso que colocou a rainha no lugar do pe�o, sen�o d�ria null e n�o funcionaria a troca
		if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
			throw new InvalidParameterException("Invalid type for promotion");// Se n�o for nenhuma das letras acima, apresentar excess�o
		}
		
		Position pos = promoted.getChessPosition().toPosition();//Criar a variavel com a posi��o da promoted
		Piece p = board.removePiece(pos);//Para remover o pe�o
		piecesOnTheBoard.remove(p);//para excluir o pe�o
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());//Usa o m�todo abaixo para instanciar a nova pe�a nos par�metros escolhidos
		board.placePiece(newPiece, pos);//Coloca no tabuleiro
		piecesOnTheBoard.add(newPiece);//Adiciona a lista de pe�as criadas
		
		return newPiece;
	}
	
	private ChessPiece newPiece (String type, Color color) {//M�todo aux para instanciar uma nova pe�a a ser usada no m�todo acima
		if (type.equals("B")) return new Bishop(board, color);
		if (type.equals("N")) return new Knight(board, color);
		if (type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);		
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);//retira a pe�a de onde est�
		p.increaseMoveCount();//Altera para ChessPiece para o m�todo increase funcionar
		Piece capturedPiece = board.removePiece(target);//retira poss�vel pe�a de destino
		board.placePiece(p, target);//Coloca a pe�a p no lugar que estava a pe�a capturada
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);//Tirar do tabuleiro
			capturedPieces.add(capturedPiece);//Adicionar na lista de capturados
		}
		
		//#Special move = Roque pequeno (Castling Kingside Rook)
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) {//testa se o rei moveu duas casas a direita
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);//Pos da Rook, mesma linha que o rei mais tr�s colunas
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);//Pos da Rook uma coluna a dir do rei
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);//Para tirar o Rook de onde est�
			board.placePiece(rook, targetT);//Coloca na posi��o que deve ir
			rook.increaseMoveCount();//pelo fato de ter movido a torre
		}
		
		//#Special move = Roque grande (Castling Queenside Rook)
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) {//testa se o rei moveu duas casas a direita
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);//Pos da Rook, mesma linha que o rei mais tr�s colunas
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);//Pos da Rook uma coluna a dir do rei
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);//Para tirar o Rook de onde est�
			board.placePiece(rook, targetT);//Coloca na posi��o que deve ir
			rook.increaseMoveCount();//pelo fato de ter movido a torre
		}		
		
		//Special move En passant
		if(p instanceof Pawn) {//Para ver se � um pe�o
			if (source.getColumn() != target.getColumn() && capturedPiece == null){//Col de origem for diferente da col de destino e se a pe�a capturada for null
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) { // se for a pe�a branca, deve capturar a pe�a preta que est� abaixo
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());//Para captura da pe�a preta
				}
				else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());//para captura da pe�a branca
				}
				capturedPiece = board.removePiece(pawnPosition);//o capturedPiece ir� receber o que estiver nessa posi��o
				capturedPieces.add(capturedPiece);//Para adicionar na lista de capturados
				piecesOnTheBoard.remove(capturedPiece);//Para remover as pe�as capturadas acima no tabuleiro
			}
		}
		return capturedPiece;		
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {//M�todo para desfazer o movimento
		ChessPiece p = (ChessPiece)board.removePiece(target);//tirar a pe�a da posi��o de destino
		p.decreaseMoveCount();//Altera para ChessPiece para o m�todo decrease funcionar
		board.placePiece(p, source);//Devolver a pe�a a posi��o de origem
		
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);// retorna para posi��o de destino
			capturedPieces.remove(capturedPiece); //Tirar pe�a capturada da lista
			piecesOnTheBoard.add(capturedPiece);// Devolve para o tabuleiro
		}
		
		//#Special move = Roque pequeno (Castling Kingside Rook)
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) {//testa se o rei moveu duas casas a direita
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);//Pos da Rook, mesma linha que o rei mais tr�s colunas
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);//Pos da Rook uma coluna a dir do rei
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);//Para tirar o Rook de onde est�
			board.placePiece(rook, sourceT);//Coloca na posi��o que deve ir, aqui � o inverso do anterior
			rook.decreaseMoveCount();//pelo fato de ter movido a torre
		}
		
		//#Special move = Roque grande (Castling Queenside Rook)
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) {//testa se o rei moveu duas casas a direita
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);//Pos da Rook, mesma linha que o rei mais tr�s colunas
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);//Pos da Rook uma coluna a dir do rei
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);//Para tirar o Rook de onde est�
			board.placePiece(rook, sourceT);//Coloca na posi��o que deve ir
			rook.decreaseMoveCount();//pelo fato de ter movido a torre
		}
		
		//#Special move en passant
		if(p instanceof Pawn) {//Para ver se � um pe�o
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable){//Col de origem for diferente da col de destino e se a pe�a capturada for null
				ChessPiece pawn = (ChessPiece)board.removePiece(target);//Para pegar o Pawn capturado e devolver ao local certo
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) { // se for a pe�a branca, deve capturar a pe�a preta que est� abaixo
					pawnPosition = new Position(3, target.getColumn());//Para captura da pe�a preta, devolve direto para linha 3
				}
				else {
					pawnPosition = new Position(4, target.getColumn());//para captura da pe�a branca, devolve direto para linha 4
				}
				board.placePiece(pawn, pawnPosition);
							
			}
		}
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
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ?  Color.BLACK : Color.WHITE;
		// Se a cor for igual white retorna Black, sen�o retorn White
	}
	
	private ChessPiece king(Color color) {//ABAIXO = Filtro em Lambda para varrer a lista e procurar nas pe�as a cor passada de par�metro
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {//Para cada pe�a p na lista list
			if (p instanceof King) {
				return (ChessPiece)p;
			}			
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		//Faz-se isto para pegar a posi��o do King no formato de matriz
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		//Para pegar as pe�as do oponente
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();//Matriz de movimentos poss�veis da pe�a advers�ria p
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {//Se a kingPosition na posi��o row e column for true, o King est� em check
				return true;
			}
		}
		return false;		
	}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {//Testa primeiro para ver e o testChech j� da negativo, a� ent�o vai para as condi��es
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();//Cria a matriz booleana para ser percorrida pelo for
			for (int i = 0; i < board.getRows(); i++) {// i deve ser menor que a extens�o de linhas(rows)
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {//pegar a Piece p, mover para a posi��o de mat e ver se sai do check
						Position source = ((ChessPiece)p).getChessPosition().toPosition(); //Fazer downcasting para ChessPiece/Posi��o em formato de Xadrez e converter para toPosition
						Position target = new Position(i, j);//Posi��o de destino, pega os dados da matriz mat
						Piece capturedPiece = makeMove(source, target);//Cria-se uma pe�a recebendo um makeMove com atributos acima
						boolean testCheck = testCheck(color);// Isso testa se o rei da cor declarada ainda est� em check
						undoMove(source, target, capturedPiece);//Precisa desfazer o movimento
						if (!testCheck) {//Se n�o estiver em check � porque o movimento tirou do check
							return false;
						}
						
					}
				}				
			}
		}
		return true;
	}

	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));//Usa-se a palavra this devido a mudan�a no contrutor
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));
		
		
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));// Passa a pr�pria partida na hora de instanciar
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));		
	}
}
