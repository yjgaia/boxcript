package boxcript.operator.impl;

import boxcript.box.Box;
import boxcript.box.BoxUtil;
import boxcript.box.NativeValueBox;
import boxcript.operator.TwoParamOperator;

public class EqualOperator implements TwoParamOperator {
	private String operatorString = "==";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public EqualOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box run(Box param1, Box param2) {
		if (param1.getOriginBox() instanceof NativeValueBox && param2.getOriginBox() instanceof NativeValueBox) {
			Object object1 = ((NativeValueBox) param1.getOriginBox()).getObject();
			Object object2 = ((NativeValueBox) param2.getOriginBox()).getObject();
			return BoxUtil.createNativeBox(object1.equals(object2), "box/native/Boolean.box", param1.getMomBox(), param1.getLn(), param1.getCol()).init();
		}
		if (param1.getOriginBox().getInnerBox("equals") != null)
			return param1.getOriginBox().getInnerBox("equals").run(param2.getOriginBox());
		else throw new Error();
	}
}
