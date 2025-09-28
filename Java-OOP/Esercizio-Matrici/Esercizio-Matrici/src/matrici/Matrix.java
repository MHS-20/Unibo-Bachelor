package matrici;

import java.util.StringJoiner;

/**
 * Classe come tipo di dato astratto (ADT)
 * 
 * @author Fondamenti di Informatica T-2 March 2021
 */
public class Matrix {
	private double[][] values;

	/**
	 * Costrutture della matrice
	 * 
	 * @param rows
	 *            Numero righe matrice
	 * @param cols
	 *            Numero colonne matrice
	 */
	private Matrix(int rows, int cols) {
		values = new double[rows][cols];
	}

	/**
	 * Costruttore della matrice
	 * 
	 * @param values
	 *            Valori degli elementi che compongono la matrice
	 */
	public Matrix(double[][] values) {
		this(values.length, values[0].length);
		for (int r = 0; r < getRows(); r++)
			for (int c = 0; c < getCols(); c++) {
				setValue(r, c, values[r][c]);
			}
	}

	/**
	 * Recupera il numero di righe della matrice
	 * 
	 * @return numero righe matrice
	 */
	public int getRows() {
		return this.values.length;
	}

	/**
	 * Recupera il numero di colonne della matrice
	 * 
	 * @return numero colonne matrice
	 */
	public int getCols() {
		return values[0].length;
	}

	/**
	 * Verifica se la matrice ? quadrata
	 * 
	 * @return true se la matrice ? quadrata, false in caso contrario
	 */
	public boolean isSquared() {
		return getRows() == getCols();
	}

	/**
	 * Recupera il valore di un elemento della matrice
	 * 
	 * @param row
	 *            Numero riga elemento da leggere
	 * @param col
	 *            Numero colonna elemento da leggere
	 * 
	 * @return elemento della matrice individuato da row e col
	 */
	public double getValue(int row, int col) {
		return values[row][col];
	}

	/**
	 * Imposta il valore di un elemento della matrice
	 * 
	 * @param row
	 *            Numero riga elemento da scrivere
	 * @param col
	 *            Numero colonna elemento da scrivere
	 * @param value
	 *            Valore da assegnare all'elemento individuato da row e col
	 */
	private void setValue(int row, int col, double value) {
		values[row][col] = value;
	}

	/**
	 * Recupera il valore del determinante della matrice
	 * 
	 * @return valore del determinante della matrice
	 */
	public double det() {
		return isSquared() ? calcDet() : Double.NaN;
	}

	/**
	 * Calcola il determinante della matrice
	 * 
	 * @return valore del determinante della matrice
	 */
	private double calcDet() {
		if (getRows() == 2) {
			return getValue(0, 0) * getValue(1, 1) - getValue(0, 1) * getValue(1, 0);
		} else {
			int row = 0; // Si lavora sempre sulla prima riga
			double result = 0;
			for (int colIdx = 0; colIdx < getCols(); colIdx++) {
				double partial = getValue(row, colIdx) * extractMinor(row, colIdx).det();
				result += colIdx % 2 == 0 ? partial : -partial;
			}
			return result;
		}
	}

	public String toString() {
		StringJoiner result = new StringJoiner(" ", "[", "]");
		for (int rowIdx = 0; rowIdx < getRows(); rowIdx++) {
			for (int colIdx = 0; colIdx < getRows(); colIdx++) {
				result.add(String.valueOf(getValue(rowIdx, colIdx)));
				
			}
			if(rowIdx!=getRows()-1) result.add(System.lineSeparator());
		}
		return result.toString();
	}

	/**
	 * Calcola la somma con un'altra matrice
	 * 
	 * @param m
	 *            Matrice da sommare all'attuale
	 * 
	 * @return nuova matrice risultato della somma
	 */
	public Matrix sum(Matrix m) {
		if (getRows() != m.getRows() || getCols() != m.getCols())
			return null;

		Matrix result = new Matrix(getRows(), getCols());
		for (int rowIdx = 0; rowIdx < getRows(); rowIdx++)
			for (int colIdx = 0; colIdx < getCols(); colIdx++)
				result.setValue(rowIdx, colIdx, getValue(rowIdx, colIdx) + m.getValue(rowIdx, colIdx));
		return result;
	}

	/**
	 * Calcola il prodotto con un'altra matrice
	 * 
	 * @param m
	 *            Matrice da moltiplicare all'attuale
	 * 
	 * @return nuova matrice risultato del prodotto
	 */
	public Matrix mul(Matrix m) {
		if (getCols() != m.getRows())
			return null;

		Matrix result = new Matrix(getRows(), m.getCols());
		for (int masterRowIdx = 0; masterRowIdx < this.getRows(); masterRowIdx++)
			for (int masterColIdx = 0; masterColIdx < m.getCols(); masterColIdx++) {
				double value = 0;
				for (int i = 0; i < getCols(); i++)
					value += getValue(masterRowIdx, i) * m.getValue(i, masterColIdx);
				result.setValue(masterRowIdx, masterColIdx, value);
			}
		return result;
	}

	/**
	 * Estrae dalla matrice attuale una sottomatrice
	 * 
	 * @param startRow
	 *            Riga da cui partire per selezionare la nuova matrice
	 * @param startCol
	 *            Colonna da cui partire per selezionare la nuova matrice
	 * @param rowCount
	 *            Numero di righe da considerare a partire da startRow per
	 *            selezionare la nuova matrice
	 * @param colCount
	 *            Numero di colonne da considerare a partire da startRow per
	 *            selezionare la nuova matrice
	 * 
	 * @return nuova matrice estratta dalla matrice attuale
	 */
	public Matrix extractSubMatrix(int startRow, int startCol, int rowCount, int colCount) {
		if (startRow >= getRows() || startRow + rowCount > getRows() || startCol >= getCols()
				|| startCol + colCount > getCols())
			return null;

		Matrix result = new Matrix(rowCount, colCount);
		for (int rowIdx = 0; rowIdx < rowCount; rowIdx++)
			for (int colIdx = 0; colIdx < colCount; colIdx++)
				result.setValue(rowIdx, colIdx, getValue(rowIdx + startRow, colIdx + startCol));
		return result;
	}

	/**
	 * Estrae il minore dalla matrice
	 * 
	 * @param row
	 * 
	 * @param col
	 * 
	 * @return nuova matrice che rappresenta il minore della matrice attuale
	 */
	public Matrix extractMinor(int row, int col) {
		if (!isSquared())
			return null;

		Matrix minor = new Matrix(getRows() - 1, getRows() - 1);
		for (int rowIdx = 0; rowIdx < getRows(); rowIdx++)
			for (int colIdx = 0; colIdx < getRows(); colIdx++) {
				if (rowIdx != row && colIdx != col) {
					int minorRowIdx = rowIdx < row ? rowIdx : rowIdx - 1;
					int minorColIdx = colIdx < col ? colIdx : colIdx - 1;
					minor.setValue(minorRowIdx, minorColIdx, getValue(rowIdx, colIdx));
				}
			}
		return minor;
	}

}
