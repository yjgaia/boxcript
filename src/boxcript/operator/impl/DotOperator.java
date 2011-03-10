package boxcript.operator.impl;

import java.math.BigDecimal;
import java.util.List;

import boxcript.box.Box;
import boxcript.box.BoxUtil;
import boxcript.box.NameBox;
import boxcript.box.NativeBox;
import boxcript.box.NativeValueBox;
import boxcript.operator.TwoParamOperator;

public class DotOperator implements TwoParamOperator {
	private static long GENERATED_VAR_NO = 0;
	private String operatorString = ".";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public DotOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Box run(Box param1, Box param2) {
		if (param1.getOriginBox() instanceof NativeBox && ((NativeBox) param1.getOriginBox()).getNativeValueBox().getObject() instanceof List &&
				param2 instanceof NativeBox && ((NativeBox) param2).getNativeValueBox().getObject() instanceof BigDecimal) {
			return (Box) ((List<Box>) ((NativeBox) param1.getOriginBox()).getNativeValueBox().getObject()).get(((BigDecimal) ((NativeBox) param2).getNativeValueBox().getObject()).intValue() - 1);
		}
		else if (param2 instanceof NameBox) {
			if (((NameBox) param2).getName().equals("__size__")) {
				long value = param1.getOriginBox().getInnerBoxCount();
				if (param1.getOriginBox() instanceof NativeValueBox) {
					Object object = ((NativeValueBox) param1.getOriginBox()).getObject();
					if (object instanceof List)			value = ((List<?>) object).size();
					else if (object instanceof String)	value = ((String) object).length();
					else throw new Error();
				}
				return BoxUtil.createNativeBox(new BigDecimal(value), "box/native/Number.box", param1, 0, 0).init();
			}
			String name = "$$$__generated_var_for_._operator_no_" + GENERATED_VAR_NO++ + "__";
			param1.getMomBox().getInnerBoxMap().put(name, param1.getOriginBox().getInnerBoxMap().get(((NameBox) param2).getName()));
			return new NameBox(name, param1.getMomBox(), param1.getLn(), param1.getCol());
		}
		else throw new Error();
	}
}
