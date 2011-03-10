package boxcript.operator;

import boxcript.box.Box;

public interface TwoParamOperator extends Operator {
	Box run(Box param1, Box param2);
}
