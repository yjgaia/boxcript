package boxcript.operator.impl;

import java.util.List;

import boxcript.box.Box;
import boxcript.box.NameBox;
import boxcript.box.NativeBox;
import boxcript.operator.TwoParamOperator;

public class IsOperator implements TwoParamOperator {
	private String operatorString = "=";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public IsOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box run(Box param1, Box param2) {
		if (param1 instanceof NameBox) {
			String name = ((NameBox) param1).getName();
			if (param1.getMomBox().getInnerBox(name) != null)	param1.getMomBox().getInnerBoxMap().get(name).setBox(param2.getOriginBox());
			else												param1.getMomBox().putInnerBox(name, param2.getOriginBox());
			return param2;
		}
		else if (param2.getOriginBox() instanceof NativeBox && ((NativeBox) param1.getOriginBox()).getNativeValueBox().getObject() instanceof List &&
				param2.getOriginBox() instanceof NativeBox && ((NativeBox) param2.getOriginBox()).getNativeValueBox().getObject() instanceof List) {
			@SuppressWarnings("unchecked") List<NameBox> list1 = (List<NameBox>) ((NativeBox) param1.getOriginBox()).getNativeValueBox().getObject();
			@SuppressWarnings("unchecked") List<NameBox> list2 = (List<NameBox>) ((NativeBox) param2.getOriginBox()).getNativeValueBox().getObject();
			for (int i = 0 ; i < list1.size() ; i++) run(list1.get(i), list2.get(i));
			return param1;
		}
		else throw new Error();
	}
}
