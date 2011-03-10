package boxcript.box;

public class NameBox extends Box {
	private final static String filePath = "box/Name.box";
	public NameBox(String name, Box momBox, int ln, int col) {
		super(filePath, momBox, ln, col);
		setBaseUpBoxFilePath(null);
		this.name = name;
	}
	private String name; public String getName() { return name; }
	
	@Override
	public Box getOriginBox() {
		Box box = getMomBox().getInnerBox(name);
		if (box instanceof NameBox) {
			return box = ((NameBox) box).getOriginBox();
		}
		return box;
	}
	
	@Override
	public String toString() {
		return "Native Name Box, name is \"" + name + "\".";
	}
}
