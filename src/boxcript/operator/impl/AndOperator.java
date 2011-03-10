package boxcript.operator.impl;

import boxcript.box.Box;
import boxcript.box.BoxUtil;
import boxcript.box.NativeValueBox;
import boxcript.operator.TwoParamOperator;

public class AndOperator implements TwoParamOperator {
	private String operatorString = "&";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public AndOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box run(Box param1, Box param2) {
		if (param1.getOriginBox() instanceof NativeValueBox && param2.getOriginBox() instanceof NativeValueBox) {
			Object object1 = ((NativeValueBox) param1.getOriginBox()).getObject();
			Object object2 = ((NativeValueBox) param2.getOriginBox()).getObject();
			if (object1 instanceof Boolean && object2 instanceof Boolean)	return BoxUtil.createNativeBox((Boolean) object1 && (Boolean) object2, "box/native/Boolean.box", param1.getMomBox(), param1.getLn(), param1.getCol()).init();
			else throw new Error();
		}
		if (param1.getOriginBox().getInnerBox("and") != null)
			return param1.getOriginBox().getInnerBox("and").run(param2.getOriginBox());
		else throw new Error();
	}
}
