package boxcript.operator.impl;

import java.math.BigDecimal;

import boxcript.box.Box;
import boxcript.box.BoxUtil;
import boxcript.box.NativeValueBox;
import boxcript.operator.TwoParamOperator;

public class LargeEqualOperator implements TwoParamOperator {
	private String operatorString = ">=";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public LargeEqualOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box run(Box param1, Box param2) {
		if (param1.getOriginBox() instanceof NativeValueBox && param2.getOriginBox() instanceof NativeValueBox) {
			Object object1 = ((NativeValueBox) param1.getOriginBox()).getObject();
			Object object2 = ((NativeValueBox) param2.getOriginBox()).getObject();
			if (object1 instanceof BigDecimal && object2 instanceof BigDecimal) {
				int compare = ((BigDecimal) object1).compareTo((BigDecimal) object2);
				return BoxUtil.createNativeBox(compare == 0 || compare == 1, "box/native/Boolean.box", param1.getMomBox(), param1.getLn(), param1.getCol()).init();
			}
			if (object1 instanceof String && object2 instanceof String)
				return BoxUtil.createNativeBox(((String) object1).length() >= ((String) object2).length(), "box/native/Boolean.box", param1.getMomBox(), param1.getLn(), param1.getCol()).init();
			else throw new Error();
		}
		if (param1.getOriginBox().getInnerBox("largerOrEqualsThan") != null)
			return param1.getOriginBox().getInnerBox("largerOrEqualsThan").run(param2.getOriginBox());
		else throw new Error();
	}
}
