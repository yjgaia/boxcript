package boxcript.operator.impl;

import java.io.File;

import boxcript.box.Box;
import boxcript.box.BoxUtil;
import boxcript.box.NameBox;
import boxcript.box.NativeBox;
import boxcript.operator.RightParamOperator;
import boxcript.operator.TwoParamOperator;

public class ImportOperator implements TwoParamOperator, RightParamOperator {
	private String operatorString = "<<";	@Override public String getOperatorString() { return operatorString; }
	private int orderNum;					@Override public int getOrderNum() { return orderNum; }
	public ImportOperator(int orderNum) {
		this.orderNum = orderNum;
	}
	@Override
	public Box run(Box param1, Box param2) {
		if (param1 instanceof NameBox && param2.getOriginBox() instanceof NativeBox && ((NativeBox) param2.getOriginBox()).getNativeValueBox().getObject() instanceof String) {
			if (param1.getOriginBox() == null)	param1.getMomBox().putInnerBox(((NameBox) param1).getName(), new Box(getFilePath(param2.getOriginBox().getMomBox(), (NativeBox) param2.getOriginBox()), param1.getMomBox(), param1.getLn(), param1.getCol()).init());
			else								BoxUtil.extendsProcess(param1.getOriginBox(), getFilePath(param1.getOriginBox(), (NativeBox) param2.getOriginBox()));
			return param1;
		}
		else throw new Error();
	}
	@Override
	public Box runR(Box param) {
		if (param.getOriginBox() instanceof NativeBox && ((NativeBox) param.getOriginBox()).getNativeValueBox().getObject() instanceof String) {
			BoxUtil.extendsProcess(param.getOriginBox().getMomBox(), getFilePath(param.getOriginBox().getMomBox(), (NativeBox) param.getOriginBox()));
			return param.getOriginBox();
		}
		else throw new Error();
	}
	private String getFilePath(Box box, NativeBox nativeStringBox) {
		return BoxUtil.getRealFilePath(box.getFilePath().substring(0, box.getFilePath().lastIndexOf(File.separatorChar) + 1), (String) nativeStringBox.getNativeValueBox().getObject());
	}
}
