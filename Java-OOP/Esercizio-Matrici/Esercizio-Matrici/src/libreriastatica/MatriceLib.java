package libreriastatica;



/**
 * Classe Matrice come libreria statica
 * 
 * @author Fondamenti di Informatica T-2 March 2021
 */
public  class MatriceLib {
	
	public static double[][] sommaMatrici(double[][] a, double[][] b) {
		double[][] c = new double[a.length][a[0].length];
		for (int i=0; i < a.length; i++)
			for (int j=0; j < a[0].length; j++) 
				c[i][j] = a[i][j] + b[i][j];
		return c;
	}
	
	public static double[][] prodottoMatrici(double[][] a, double[][] b) {
		double[][] c = new double[a.length][b[0].length];
		
		for (int masterRowIdx = 0; masterRowIdx < a.length; masterRowIdx++)
			for (int masterColIdx = 0; masterColIdx < b[0].length; masterColIdx++) {
				double value = 0;
				for (int i = 0; i < a[0].length; i++)
					value += a[masterRowIdx][i] * b[i][masterColIdx];
				c[masterRowIdx][masterColIdx] = value;
			}
		return c; 
	}
	
	public static void stampaMatrice(double[][] a) {
		StringBuilder result = new StringBuilder();
		result.append("[");
		for (int i=0; i < a.length; i++) {
			for (int j=0; j < a[0].length; j++) 
				result.append(String.valueOf(a[i][j]));
			if(i==a.length-1) result.append("]");
				result.append(System.lineSeparator());
		}
	
		System.out.println(result.toString());
	}
	
}
