package boxcript.operator;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import boxcript.box.Box;
import boxcript.box.OperatorBox;
import boxcript.operator.block.impl.CodeBlock;
import boxcript.operator.block.impl.NameBlock;
import boxcript.operator.block.impl.PreBlock;
import boxcript.operator.block.impl.StringBlock;
import boxcript.operator.impl.AndOperator;
import boxcript.operator.impl.ArrayOperator;
import boxcript.operator.impl.DecreaseOperator;
import boxcript.operator.impl.DifferOperator;
import boxcript.operator.impl.DivideIsOperator;
import boxcript.operator.impl.DivideOperator;
import boxcript.operator.impl.DotOperator;
import boxcript.operator.impl.EqualOperator;
import boxcript.operator.impl.IfOperator;
import boxcript.operator.impl.ImportOperator;
import boxcript.operator.impl.IncreaseOperator;
import boxcript.operator.impl.IsOperator;
import boxcript.operator.impl.LargeEqualOperator;
import boxcript.operator.impl.LargeOperator;
import boxcript.operator.impl.MinusIsOperator;
import boxcript.operator.impl.MinusOperator;
import boxcript.operator.impl.MultiplyIsOperator;
import boxcript.operator.impl.MultiplyOperator;
import boxcript.operator.impl.NotOperator;
import boxcript.operator.impl.OrOperator;
import boxcript.operator.impl.ParamOperator;
import boxcript.operator.impl.PlusIsOperator;
import boxcript.operator.impl.PlusOperator;
import boxcript.operator.impl.RemainderIsOperator;
import boxcript.operator.impl.RemainderOperator;
import boxcript.operator.impl.RunJavaCodeOperator;
import boxcript.operator.impl.SmallEqualOperator;
import boxcript.operator.impl.SmallOperator;

public class OperatorUtil {

	private static Set<Operator> operatorSet = new HashSet<Operator>(); public static Set<Operator> getOperatorSet() { return operatorSet; }
	static {
		operatorSet.add(new DotOperator(1));
		
		operatorSet.add(new IncreaseOperator(3));
		operatorSet.add(new DecreaseOperator(3));
		
		operatorSet.add(new MultiplyOperator(4));
		operatorSet.add(new DivideOperator(4));
		operatorSet.add(new RemainderOperator(4));
		
		operatorSet.add(new PlusOperator(5));
		operatorSet.add(new MinusOperator(5));
		
		operatorSet.add(new EqualOperator(6));
		operatorSet.add(new DifferOperator(6));
		operatorSet.add(new LargeOperator(6));
		operatorSet.add(new LargeEqualOperator(6));
		operatorSet.add(new SmallOperator(6));
		operatorSet.add(new SmallEqualOperator(6));
		
		operatorSet.add(new NotOperator(7));
		
		operatorSet.add(new AndOperator(8));
		operatorSet.add(new OrOperator(8));

		operatorSet.add(new ArrayOperator(9));
		operatorSet.add(new ParamOperator(10));
		operatorSet.add(new IfOperator(11));
		
		operatorSet.add(new IsOperator(12));
		operatorSet.add(new MultiplyIsOperator(12));
		operatorSet.add(new DivideIsOperator(12));
		operatorSet.add(new RemainderIsOperator(12));
		operatorSet.add(new PlusIsOperator(12));
		operatorSet.add(new MinusIsOperator(12));
		
		operatorSet.add(new RunJavaCodeOperator(97));
		operatorSet.add(new ImportOperator(98));
		
		operatorSet.add(new StringBlock(99));
		operatorSet.add(new CodeBlock(99));
		operatorSet.add(new PreBlock(99));
		operatorSet.add(new NameBlock(99));
	}
	public static boolean isOperatorChar(char ch) {
		for (Operator operator : operatorSet) if (operator.getOperatorString().charAt(0) == ch) return true;
		return false;
	}
	public static Box runOperator(Operator operator, Stack<Box> boxStack) {
		Box unknown1 = boxStack.pop(); // 연산자?
		Box unknown2 = boxStack.pop(); // 연산자?
		if (boxStack.isEmpty() || // 박스 스택이 비었거나,
				boxStack.peek() instanceof OperatorBox) { // 박스 스택의 마지막이 연산자 박스면,
			if (operator instanceof LeftParamOperator && !(unknown2 instanceof OperatorBox))		return ((LeftParamOperator) operator).runL(unknown2); // 왼쪽 파라미터를 갖는 연산자 실행
			else if (operator instanceof RightParamOperator && !(unknown1 instanceof OperatorBox))	return ((RightParamOperator) operator).runR(unknown1); // 오른쪽 파라미터를 갖는 연산자 실행
		}
		else if (operator instanceof TwoParamOperator) { // 두개의 파라미터를 갖는 연산자
			Box param = boxStack.pop();
			return ((TwoParamOperator) operator).run(param, unknown1);
		}		
		return null;
	}
	public static Box runOperators(Stack<Operator> operatorStack, Stack<Box> boxStack) {
		while (!operatorStack.isEmpty()) {
			boxStack.push(runOperator(operatorStack.pop(), boxStack));
		}
		if (!boxStack.isEmpty()) {
			Box box = boxStack.get(0);
			boxStack.remove(0);
			return box;
		}
		return null;
	}
	
}
