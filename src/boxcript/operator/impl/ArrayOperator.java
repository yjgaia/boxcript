package boxcript.operator.impl;

import java.util.ArrayList;
import java.util.List;

import boxcript.box.Box;
import boxcript.box.BoxUtil;
import boxcript.box.NativeBox;
import boxcript.operator.TwoParamOperator;

public class ArrayOperator implements TwoParamOperator {
	private String operatorString = ",";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public ArrayOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Box run(Box param1, Box param2) {
		Box box; List<Box> list;
		if (param1.getOriginBox() instanceof NativeBox && ((NativeBox) param1.getOriginBox()).getNativeValueBox().getObject() instanceof List) {
			box = param1;
			list = (List<Box>) ((NativeBox) box.getOriginBox()).getNativeValueBox().getObject();
		} else {
			box = BoxUtil.createNativeBox(new ArrayList<Box>(), "box/native/Array.box", param1.getMomBox(), param1.getLn(), param1.getCol()).init();
			list = (List<Box>) ((NativeBox) box.getOriginBox()).getNativeValueBox().getObject();
			list.add(param1);
		}
		if (param2.getOriginBox() instanceof NativeBox && ((NativeBox) param2.getOriginBox()).getNativeValueBox().getObject() instanceof List) {
				list.addAll((List<Box>) ((NativeBox) param2.getOriginBox()).getNativeValueBox().getObject());
		} else	list.add(param2);
		return box;
	}
}
