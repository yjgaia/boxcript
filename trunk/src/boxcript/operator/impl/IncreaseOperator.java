package boxcript.operator.impl;

import boxcript.box.Box;
import boxcript.box.NameBox;
import boxcript.operator.LeftParamOperator;
import boxcript.operator.RightParamOperator;

public class IncreaseOperator implements LeftParamOperator, RightParamOperator {
	private String operatorString = "++";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public IncreaseOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box runL(Box param) {
		if (param instanceof NameBox && param.getOriginBox().getInnerBox("increase") != null) {
			Box result = param.getOriginBox().getInnerBox("increase").run();
			param.getMomBox().getInnerBoxMap().get(((NameBox) param).getName()).setBox(result);
			return result;
		}
		else throw new Error();
	}
	@Override
	public Box runR(Box param) {
		if (param instanceof NameBox && param.getOriginBox().getInnerBox("increase") != null) {
			Box result = param.getOriginBox().getInnerBox("increase").run();
			param.getMomBox().getInnerBoxMap().get(((NameBox) param).getName()).setBox(result);
			return result;
		}
		else throw new Error();
	}
}
