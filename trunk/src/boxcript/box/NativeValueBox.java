package boxcript.box;

public class NativeValueBox extends Box {
	public NativeValueBox(Object object) {
		super(null);
		this.object = object;
	}
	private Object object; public Object getObject() { return object; }
	
	@Override
	public String toString() {
		return "Native Java Class [" + object.getClass() + "], toString() is \"" + object.toString() + "\".";
	}
}
