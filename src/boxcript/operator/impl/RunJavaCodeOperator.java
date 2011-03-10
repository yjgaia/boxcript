package boxcript.operator.impl;

import java.math.BigDecimal;

import boxcript.box.Box;
import boxcript.box.BoxUtil;
import boxcript.operator.RightParamOperator;
import bsh.EvalError;

public class RunJavaCodeOperator implements RightParamOperator {
	private String operatorString = ">>";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public RunJavaCodeOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box runR(Box param) {
		bsh.Interpreter bshi = new bsh.Interpreter();
		Object result;
		try {
			result = bshi.eval(param.getOriginBox().toString());
		} catch (EvalError e) {
			throw new Error();
		}
		String filePath;
		if (result instanceof String)		filePath = "box/native/String.box";
		if (result instanceof BigDecimal)	filePath = "box/native/Number.box";
		if (result instanceof Boolean)		filePath = "box/native/Boolean.box";
		else								filePath = "box/native/Unknown.box";
		return BoxUtil.createNativeBox(result, filePath, param.getMomBox(), param.getLn(), param.getCol()).init();
	}
}
