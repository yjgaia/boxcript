package boxcript.box;

public class NativeBox extends Box {
	public NativeBox(NativeValueBox nativeValueBox, String filePath, Box momBox, int ln, int col, String code) { super(filePath, momBox, ln, col, code); nativeValueBox.setMomBox(this); this.nativeValueBox = nativeValueBox; }
	public NativeBox(NativeValueBox nativeValueBox, String filePath, Box momBox, int ln, int col) { super(filePath, momBox, ln, col); nativeValueBox.setMomBox(this); this.nativeValueBox = nativeValueBox; }
	public NativeBox(NativeValueBox nativeValueBox, String filePath) {
		super(filePath);
		nativeValueBox.setMomBox(this);
		setBaseUpBoxFilePath(null);
		this.nativeValueBox = nativeValueBox;
	}
	private NativeValueBox nativeValueBox; public NativeValueBox getNativeValueBox() { return nativeValueBox; }
	
	@Override
	public String toString() {
		return nativeValueBox.getObject().toString();
	}
}
