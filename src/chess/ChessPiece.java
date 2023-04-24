package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece{
	
	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {//Somente get pois uma vez passado o color, n�o altera mais, como o board
		return color;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);//Tem que fazer downcasting
		return p != null && p.getColor() != color;//Ver se a pe�a na posi��o n�o est� vazia e se a cor � diferente
	}
		
	
}
