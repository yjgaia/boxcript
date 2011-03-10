package boxcript.operator.block;

import boxcript.operator.Operator;

public interface Block extends Operator {
	String getEndString();
	boolean hasInnerBlock();
}
