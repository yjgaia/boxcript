package boxcript.box;

import boxcript.operator.Operator;

public class OperatorBox extends Box {
	private final static String filePath = "box/Operator.box";
	public OperatorBox(Operator operator, Box momBox, int ln, int col) {
		super(filePath, momBox, ln, col);
		setBaseUpBoxFilePath(null);
		this.operator = operator;
	}
	private Operator operator; public Operator getOperator() { return operator; }
	
	@Override
	public String toString() {
		return "Native Operator Box, operator is \"" + operator + "\".";
	}
}
