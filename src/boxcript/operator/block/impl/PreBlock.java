package boxcript.operator.block.impl;

import boxcript.operator.block.Block;

public class PreBlock implements Block {
	private String operatorString = "(";	@Override public String getOperatorString() { return operatorString; }
	private String endString = ")";			@Override public String getEndString() { return endString; }
	private boolean hasInnerBlock = true;	@Override public boolean hasInnerBlock() { return hasInnerBlock; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public PreBlock(int orderNum) {
		this.orderNum = orderNum;
	}
}
