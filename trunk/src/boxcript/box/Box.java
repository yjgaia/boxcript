package boxcript.box;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import boxcript.operator.Operator;
import boxcript.operator.OperatorUtil;
import boxcript.operator.block.Block;
import boxcript.operator.block.impl.CodeBlock;
import boxcript.operator.block.impl.NameBlock;
import boxcript.operator.block.impl.PreBlock;
import boxcript.operator.block.impl.StringBlock;
import boxcript.operator.impl.DotOperator;
import boxcript.operator.impl.MinusOperator;

public class Box {
	private String filePath; public String getFilePath() { return filePath; } // 파일 경로
	private Box momBox; public Box getMomBox() { return momBox; } public void setMomBox(Box momBox) { this.momBox = momBox; }
	private int ln, col; // 줄, 칸
	public int getLn() { return ln; } public int getCol() { return col; }
	private String code; public String getCode() { return code; } // 코드
	public Box(String filePath, Box momBox, int ln, int col, String code) {
		this.filePath = BoxUtil.getRealFilePath(filePath);
		this.momBox = momBox;
		this.ln = ln; this.col = col;
		this.code = code;
	}
	public Box(String filePath, Box momBox, int ln, int col) { this(filePath, momBox, ln, col, BoxUtil.getSourceFromFile(filePath)); }
	public Box(String filePath) { this(filePath, null, 1, 1); }
	
	private String baseUpBoxFilePath = "box/Box.box"; public void setBaseUpBoxFilePath(String baseUpBoxFilePath) { this.baseUpBoxFilePath = baseUpBoxFilePath; }
	private Set<String> upBoxFilePathSet; public Set<String> getUpBoxFilePathSet() { return upBoxFilePathSet; }
	
	private long innerBoxCount; public long getInnerBoxCount() { return innerBoxCount; } // 내부 박스 개수
	private Map<String, BoxWrapper> innerBoxMap; public Map<String, BoxWrapper> getInnerBoxMap() { return innerBoxMap; } // 내부 박스 맵
	public Box getInnerBox (String name) {
		if (innerBoxMap == null) return null;
		if (innerBoxMap.get(name) == null) {
			if (name.equals("__size__")) return BoxUtil.createNativeBox(new BigDecimal(innerBoxCount), "box/native/Number.box", this, 0, 0).init();
			return null;
		}
		return innerBoxMap.get(name).getBox();
	}
	public void putInnerBox (String name, Box box) { putInnerBox (name, box, true); }
	public void putInnerBox (String name, Box box, boolean increaseCount) {
		if (increaseCount) innerBoxCount++; // 내부 박스 개수 증가
		innerBoxMap.put(name, new BoxWrapper(box));
	}
	
	private Box paramBox; public Box getParamBox() { return paramBox; } // 파라미터 박스
	public Box run(Box paramBox) {
		upBoxFilePathSet = new HashSet<String>(); upBoxFilePathSet.add(filePath); // 상위 박스 세팅
		innerBoxCount = 0; // 내부 박스 개수 초기화
		innerBoxMap = new HashMap<String, BoxWrapper>(); // 내부 박스 초기화
		if (baseUpBoxFilePath != null)	BoxUtil.extendsProcess(this, baseUpBoxFilePath); // 기본 상위 박스 생성
		
										putInnerBox("this", this, false); // 현재 박스를 this란 이름으로 저장
		if (momBox != null)				putInnerBox("mom", momBox, false); // 부모 박스를 mom이란 이름으로 저장
		if (this instanceof NativeBox)	putInnerBox("__native_value__", ((NativeBox) this).getNativeValueBox()); // Native 박스일 경우 Native Value를 저장
		this.paramBox = paramBox;
		Box resultBox = null;
		//================================================== 실행 ==================================================		
		int ln = this.ln, col = this.col;
		Stack<Box> boxStack = new Stack<Box>();
		Stack<Operator> operatorStack = new Stack<Operator>();
		int numberSign = 1;
		for (int i = 0; i < code.length(); i++) { // 코드 탐색
			if (code.charAt(i) == '\n' || code.charAt(i) == '\r') { // 줄넘김 처리
				if (i + 1 < code.length()) {
					if (code.charAt(i) == '\n' && code.charAt(i + 1) == '\r') i++; // \n\r -> i 값 증가
					if (code.charAt(i) == '\r' && code.charAt(i + 1) == '\n') i++; // \r\n -> i 값 증가
				}
				ln++; col = 1; // 줄 증가, 칸 초기화
				Box result = OperatorUtil.runOperators(operatorStack, boxStack); // 남아있는 연산자 실행
				if (result != null) resultBox = result;
			}
			else if (code.charAt(i) == '#') {
				for (int j = i + 1 ; j < code.length() ; j++) { // 코드 탐색
					if (code.charAt(j) == '\n' || code.charAt(j) == '\r') { // 줄넘김 처리
						if (j + 1 < code.length()) {
							if (code.charAt(j) == '\n' && code.charAt(j + 1) == '\r') j++; // \n\r -> j 값 증가
							if (code.charAt(j) == '\r' && code.charAt(j + 1) == '\n') j++; // \r\n -> j 값 증가
						}
						ln++; break;
					}
					col += j - i + 1; i = j;
				}
			}
			else if (Character.isDigit(code.charAt(i))) { // 숫자 처리
				boolean dot = false; // 소수점 출현 여부
				for (int j = i + 1 ; j <= code.length() ; j++) { // 코드 탐색
					if (j < code.length() && !dot && code.charAt(j) == '.' && Character.isDigit(code.charAt(j + 1))) dot = true; // 소수점
					else if (j == code.length() || !Character.isDigit(code.charAt(j))) { // 코드의 끝이거나, 숫자가 아니면,
						if (boxStack.isEmpty() || // 박스 스택이 비었거나,
								boxStack.peek() instanceof OperatorBox) { // 박스 스택의 마지막이 연산자 박스면,
							boxStack.push(BoxUtil.createNativeBox(new BigDecimal(code.substring(i, j)).multiply(new BigDecimal(numberSign)), "box/native/Number.box", this, ln, col).init()); // 숫자 박스 저장
							numberSign = 1;
						} else throw new Error(); // 아니면 오류
						col += j - i; i = j - 1; break;
					}
				}
			}
			else if (Character.isLetter(code.charAt(i)) || code.charAt(i) == '_') { // 문자 혹은 언더바일 경우
				for (int j = i + 1 ; j <= code.length() ; j++) { // 코드 탐색
					if (j == code.length() || // 코드의 끝이거나
							!(Character.isLetter(code.charAt(j)) || Character.isDigit(code.charAt(j)) || code.charAt(j) == '_')) { // 문자 혹은 숫자 혹은 언더바가 아닐 경우 (단어 문자가 아니면),
						String name = code.substring(i, j);
						if (boxStack.isEmpty() || // 박스 스택이 비었거나,
								boxStack.peek() instanceof OperatorBox) { // 박스 스택의 마지막이 연산자 박스면,
							// 예약어 처리
							if (name.equals("true"))		boxStack.push(BoxUtil.createNativeBox(true, "box/native/Boolean.box", this, ln, col).init()); // 참일때 Boolean 박스 저장
							else if (name.equals("false"))	boxStack.push(BoxUtil.createNativeBox(false, "box/native/Boolean.box", this, ln, col).init()); // 거짓일때 Boolean 박스 저장
							else 							boxStack.push(new NameBox(name, this, ln, col).init()); // 이름 박스 저장
						} else throw new Error();
						col += j - i; i = j - 1; break;
					}
				}
			}
			else if (OperatorUtil.isOperatorChar(code.charAt(i))) { // 연산자 문자일 경우
				Operator savedOperator = null;
				for (Operator operator : OperatorUtil.getOperatorSet()) {
					String operatorString = operator.getOperatorString();
					if (i + operatorString.length() <= code.length()
							&& operatorString.equals(code.substring(i, i + operatorString.length()))) { // 연산자일 경우
						if (savedOperator == null || savedOperator.getOperatorString().length() < operatorString.length()) {
							savedOperator = operator;
						}
					}
				}
				if (savedOperator instanceof Block) { // 블록일 경우
					Stack<Block> blockStack = new Stack<Block>();
					blockStack.add((Block) savedOperator);
					
					int ln2 = 0;
					for (int j = i + 1 ; j < code.length() ; j++) { // 코드 탐색
						if (code.charAt(j) == '\n' || code.charAt(j) == '\r') { // 줄넘김 처리
							if (j + 1 < code.length()) {
								if (code.charAt(j) == '\n' && code.charAt(j + 1) == '\r') j++; // \n\r -> j 값 증가
								if (code.charAt(j) == '\r' && code.charAt(j + 1) == '\n') j++; // \r\n -> j 값 증가
							}
							ln2++; // 줄 증가
						}
						else {
							String lastBlockEndString = blockStack.peek().getEndString();
							if (!(j > 0 && code.charAt(j) == '"' && code.charAt(j - 1) == '\\')) {
								if (j + lastBlockEndString.length() <= code.length()
										&& lastBlockEndString.equals(code.substring(j, j + lastBlockEndString.length()))) { // 블록이 닫힐 경우
									blockStack.pop();
									if (blockStack.isEmpty()) { // 블록 스택이 비게 될 경우,
										// 블록 완성
										if (savedOperator instanceof StringBlock)		boxStack.push(BoxUtil.createNativeBox(BoxUtil.stringProcess(code.substring(i + 1, j)), "box/native/String.box", this, ln, col).init()); // 문자열 박스 저장
										else if (savedOperator instanceof CodeBlock)	boxStack.push(new Box(this.filePath, this, ln, col, code.substring(i + 1, j))); // 박스 저장
										else if (savedOperator instanceof PreBlock)		boxStack.push(new Box(this.filePath, this, ln, col, code.substring(i + 1, j)).run(null)); // 코드 박스를 실행한 후 저장합니다.
										else if (savedOperator instanceof NameBlock)	{ // 이름 블록일 경우,
											Box box = new Box(this.filePath, this, ln, col, code.substring(i + 1, j)).run(null);
											if (box instanceof NativeBox) {
												Object object = ((NativeBox) box).getNativeValueBox().getObject();
												if (object instanceof String || object instanceof BigDecimal) {
													for (Operator operator : OperatorUtil.getOperatorSet()) {
														if (operator instanceof DotOperator) {
															boxStack.push(new OperatorBox(operator, this, ln, col)); // 연산자 박스 저장
															operatorStack.push(operator); // 연산자 저장
														}
													}
													if (object instanceof String)		boxStack.push(new NameBox(BoxUtil.stringProcess((String) object), this, ln, col).init()); // 이름 박스를 저장합니다.
													if (object instanceof BigDecimal)	boxStack.push(box); // 숫자 박스를 저장합니다.
												}
											}
										}
										else throw new Error();
										ln += ln2; col += j - i + 1; i = j - 1 + 1; break;
									}
								} else if (blockStack.peek().hasInnerBlock()) { // 마지막 블록이 내부 블록을 가질 수 있으면,
									Operator savedBlock = null;
									for (Operator block : OperatorUtil.getOperatorSet()) {
										if (block instanceof Block) {
											String operatorString = block.getOperatorString();
											if (j + operatorString.length() <= code.length()
													&& operatorString.equals(code.substring(j, j + operatorString.length()))) { // 연산자일 경우
												if (savedBlock == null || savedBlock.getOperatorString().length() < operatorString.length()) {
													savedBlock = block;
												}
											}
										}
									}
									if (savedBlock != null) {
										blockStack.add((Block) savedBlock); // 새 블록 저장
									}
								}
							}
						}
					}
				} else {
					if (savedOperator instanceof MinusOperator && 
							(boxStack.isEmpty() || // 박스 스택이 비었거나,
									boxStack.peek() instanceof OperatorBox)) { // 박스 스택의 마지막이 연산자 박스면,
						numberSign *= -1;					
					}
					else {
						if (!operatorStack.isEmpty() && operatorStack.peek().getOrderNum() <= savedOperator.getOrderNum()) { // 저장된 연산자보다 순서가 뒤일 경우
							boxStack.push(OperatorUtil.runOperator(operatorStack.pop(), boxStack)); // 저장된 연산자 실행
						}
						boxStack.push(new OperatorBox(savedOperator, this, ln, col)); // 연산자 박스 저장
						operatorStack.push(savedOperator); // 연산자 저장
					}
					col	+= savedOperator.getOperatorString().length();
					i	+= savedOperator.getOperatorString().length() - 1;
				}
			}
			else if (Character.isWhitespace(code.charAt(i))) col++; // 공백 문자일 경우 칸 증가
			else throw new Error();
		}
		Box result = OperatorUtil.runOperators(operatorStack, boxStack); // 남아있는 연산자 실행
		if (result != null) resultBox = result;
		//================================================== 종료 ==================================================
		return resultBox;
	}
	public Box run() { return run(null); }
	public Box init(Box paramBox) { run(paramBox); return this; }
	public Box init() { return init(null); }
	
	public Box getOriginBox() {
		return this;
	}
	
	@Override
	public String toString() {
		return innerBoxMap != null && getInnerBox("toString") != null ?
				getInnerBox("toString").run().toString() :
				"Box[" + filePath + "]";
	}
}
