package boxcript.operator.impl;

import java.util.List;

import boxcript.box.Box;
import boxcript.box.NameBox;
import boxcript.box.NativeBox;
import boxcript.operator.LeftParamOperator;
import boxcript.operator.RightParamOperator;
import boxcript.operator.TwoParamOperator;

public class ParamOperator implements LeftParamOperator, TwoParamOperator, RightParamOperator {
	private String operatorString = ":";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public ParamOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override public Box runL(Box param)				{ return param.getOriginBox().run(null); }
	@Override public Box run(Box param1, Box param2)	{ return param1.getOriginBox().run(param2); }
	@Override
	public Box runR(Box paramNameBox) {
		Box paramBox = paramNameBox.getMomBox().getParamBox();
		if (paramBox != null) {
			if (paramNameBox instanceof NativeBox && paramBox instanceof NativeBox
					&& ((NativeBox) paramNameBox).getNativeValueBox().getObject() instanceof List
					&& ((NativeBox) paramBox).getNativeValueBox().getObject() instanceof List) {
				@SuppressWarnings("unchecked") List<Box> paramNameArray = (List<Box>) ((NativeBox) paramNameBox).getNativeValueBox().getObject();
				@SuppressWarnings("unchecked") List<Box> paramArray = (List<Box>) ((NativeBox) paramBox).getNativeValueBox().getObject();
				if (paramNameArray instanceof List && paramArray instanceof List) {
					for (int i = 0 ; i < paramNameArray.size() ; i++) {
						paramNameBox.getMomBox().putInnerBox(((NameBox) paramNameArray.get(i)).getName(), paramArray.get(i));	
					}
				}
			}
			else if (paramNameBox instanceof NameBox) paramNameBox.getMomBox().putInnerBox(((NameBox) paramNameBox).getName(), paramBox);
			else throw new Error();
		}
		return null;
	}
}
