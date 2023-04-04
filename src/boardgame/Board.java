package boardgame;

public class Board {
	
	private int rows;
	private int columns;
	private Piece[][] pieces;//Tabuleiro possui uma matriz de pe�as
	
	public Board(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	public Piece piece(int row, int column) {
		return pieces[row][column];
	}
	
	public Piece piece (Position position) {//para retornar a posi��o da pe�a
		return pieces[position.getRow()][position.getColumn()];
	}
}