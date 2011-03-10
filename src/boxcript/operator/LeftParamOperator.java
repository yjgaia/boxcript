package boxcript.operator;

import boxcript.box.Box;

public interface LeftParamOperator extends Operator {
	Box runL(Box param);
}
