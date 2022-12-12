package servlets;

import java.io.Serializable;

public class CalculationResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private double n1, n2;
	private String _operation, tipoOp;

	public CalculationResult(Double n1, Double n2, String operation) {
		super();
		this.n1 = n1;
		this.n2 = n2;
		this.tipoOp = "";
		this._operation = operation;
	}

	public Double calculate() {
		switch (this._operation) {
		case "+": {
			this.tipoOp = "add";
			return (n1 + n2);
		}
		case "-": {
			this.tipoOp = "sub";
			return (n1 - n2);
		}
		case "/":{
			this.tipoOp = "div";
			return (n1 / n2);
		}
		case "*":{
			this.tipoOp = "prod";
			return (n1 * n2);
		}
		default:
			return Double.NaN;
		}
	}
}