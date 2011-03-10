package boxcript.operator.impl;

import boxcript.box.Box;
import boxcript.box.NameBox;
import boxcript.operator.TwoParamOperator;

public class RemainderIsOperator implements TwoParamOperator {
	private String operatorString = "%=";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public RemainderIsOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box run(Box param1, Box param2) {
		if (param1 instanceof NameBox && param1.getOriginBox().getInnerBox("remainder") != null) {
			Box result = param1.getOriginBox().getInnerBox("remainder").run(param2);
			param1.getMomBox().getInnerBoxMap().get(((NameBox) param1).getName()).setBox(result);
			return result;
		}
		else throw new Error();
	}
}
