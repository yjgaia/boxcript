package boxcript.operator.impl;

import java.math.BigDecimal;

import boxcript.box.Box;
import boxcript.box.BoxUtil;
import boxcript.box.NativeBox;
import boxcript.box.NativeValueBox;
import boxcript.operator.TwoParamOperator;

public class PlusOperator implements TwoParamOperator {
	private String operatorString = "+";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public PlusOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box run(Box param1, Box param2) {
		if (param1.getOriginBox() instanceof NativeValueBox && param2.getOriginBox() instanceof NativeValueBox) {
			Object object1 = ((NativeValueBox) param1.getOriginBox()).getObject();
			Object object2 = ((NativeValueBox) param2.getOriginBox()).getObject();
			if (object1 instanceof BigDecimal && object2 instanceof BigDecimal)	return BoxUtil.createNativeBox(((BigDecimal) object1).add((BigDecimal) object2), "box/native/Number.box", param1.getMomBox(), param1.getLn(), param1.getCol()).init();
			else 																return BoxUtil.createNativeBox(object1.toString() + object2.toString(), "box/native/String.box", param1.getMomBox(), param1.getLn(), param1.getCol()).init();
		}
		if (param2.getOriginBox() instanceof NativeBox && ((NativeBox) param2.getOriginBox()).getNativeValueBox().getObject() instanceof String) {
			return BoxUtil.createNativeBox(param1.getOriginBox().toString() + param2.getOriginBox().toString(), "box/native/String.box", param1.getMomBox(), param1.getLn(), param1.getCol()).init();
		}
		if (param1.getOriginBox().getInnerBox("plus") != null)		return param1.getOriginBox().getInnerBox("plus").run(param2.getOriginBox());
		else if (param1.getOriginBox().getInnerBox("add") != null)	return param1.getOriginBox().getInnerBox("add").run(param2.getOriginBox());
		else throw new Error();
	}
}
