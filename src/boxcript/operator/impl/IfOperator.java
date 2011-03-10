package boxcript.operator.impl;

import boxcript.box.Box;
import boxcript.box.NativeBox;
import boxcript.operator.TwoParamOperator;

public class IfOperator implements TwoParamOperator {
	private String operatorString = "?";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public IfOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box run(Box param1, Box param2) {
		if (param1.getOriginBox() instanceof NativeBox && ((NativeBox) param1.getOriginBox()).getNativeValueBox().getObject() instanceof Boolean) {
			if ((Boolean) ((NativeBox) param1.getOriginBox()).getNativeValueBox().getObject()) return param2.getOriginBox().run();
			return null;
		}
		else throw new Error();
	}
}
