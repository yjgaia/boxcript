package boxcript.operator.impl;

import boxcript.box.Box;
import boxcript.box.BoxUtil;
import boxcript.box.NativeValueBox;
import boxcript.operator.RightParamOperator;

public class NotOperator implements RightParamOperator {
	private String operatorString = "!";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public NotOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box runR(Box param) {
		if (param.getOriginBox() instanceof NativeValueBox && ((NativeValueBox) param.getOriginBox()).getObject() instanceof Boolean) {
			return BoxUtil.createNativeBox(!(Boolean) ((NativeValueBox) param.getOriginBox()).getObject(), "box/native/Boolean.box", param.getMomBox(), param.getLn(), param.getCol()).init();
		}
		else if (param.getOriginBox().getInnerBox("not") != null)
			return param.getOriginBox().getInnerBox("not").run();
		else throw new Error();
	}
}
