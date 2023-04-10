package boardgame;

public abstract class Piece {
	
	protected Position position;// Pe�a fica como null, por isso n�o entra no construtor
	private Board board;
	
	public Piece(Board board) {
		this.board = board; // O construtor tem apenas o tabuleiro
		position = null; // Se n�o colocar, o java j� atribui como null
	}
	// Somente classes do mesmo pacote e subclasses podem acessar esse m�todo, por isso protected
	protected Board getBoard() {// Usa-se somente o get pois n�o instancia outro tabuleiro
		return board;
	}
	
	public abstract boolean[][] possibleMoves();
		
	public boolean PossibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];//m�todo para retornar poss�vel movimento
	}
	
	public boolean isThereAnyPossibleMove() {//M�todo para verif se existe pelo menos 1 movimento poss�vel da pe�a
		boolean[][] mat = possibleMoves();
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				if (mat[i][j]) {
					return true;// se em algum ponto da matriz retornar true � possivel mover a pe�a
				}
			}
		}
		return false;// do contr�rio retorna false
	}
}
