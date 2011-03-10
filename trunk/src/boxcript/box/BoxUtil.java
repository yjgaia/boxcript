package boxcript.box;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public class BoxUtil {
	public static String getRealFilePath(String filePath) {
		return getRealFilePath(null, filePath);
	}
	public static String getRealFilePath(String basePath, String filePath) {
		if (filePath == null) return null;
		try {
			File file;
			if (basePath == null)	file = new File(filePath);
			else					file = new File(basePath + filePath);
			if (!file.exists()) {
				String classPath = Box.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
				file = new File(classPath.substring(0, classPath.lastIndexOf("/") + 1), filePath);
			}
			return file.getCanonicalPath();
			
		} catch (IOException e) {
			throw new Error();
		} catch (URISyntaxException e) {
			throw new Error();
		}
	}
	public static String getSourceFromFile(String filePath) {
		if (filePath == null) return null;
		File file = new File(getRealFilePath(filePath)); // 소스 파일을 불러옵니다.
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // 버퍼를 생성합니다.
		} catch (FileNotFoundException e) {
			throw new Error();
		} catch (UnsupportedEncodingException e) {
			throw new Error();
		}
		StringBuffer sb = new StringBuffer(); // 소스 버퍼
		int readByte = 0;
		try {
			while ((readByte = br.read()) != -1) { // 버퍼를 읽어옵니다.
				sb.append((char) readByte);
			}
		} catch (IOException e) {
			throw new Error();
		}
		return sb.toString();
	}
	public static Box createNativeBox(Object object, String filePath, Box momBox, int ln, int col) {
		return new NativeBox(new NativeValueBox(object), filePath, momBox, ln, col);
	}
	public static String stringProcess(String string) {
		return string.replaceAll("\\\\\"", "\"");
	}
	public static void extendsProcess(Box box1, String filePath) {
		String realFilePath = getRealFilePath(filePath);
		if (!box1.getFilePath().equals(realFilePath) && !box1.getUpBoxFilePathSet().contains(realFilePath)) {
			Box box2 = new Box(filePath).init();
			box1.getUpBoxFilePathSet().addAll(box2.getUpBoxFilePathSet()); // box2의 상위 박스의 경로들을 저장
			box1.getUpBoxFilePathSet().add(realFilePath); // 불러온 박스의 경로를 저장
			for (String key : box2.getInnerBoxMap().keySet()) {
				Box box = box2.getInnerBox(key);
				box.setMomBox(box1);
				box1.putInnerBox(key, box);
			}
		}
	}
}
