package SBWI.TokkiOCR;

import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.python.util.PythonInterpreter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@SpringBootApplication
public class TokkiOcrApplication {
	public static ProcessBuilder inputCommand(String cmd) {
		ProcessBuilder builder = new ProcessBuilder();
		try {
			if (System.getProperty("os.name").indexOf("Windows") > -1) {
					builder.command("cmd.exe", "/c", cmd);
					builder.directory(new File("./image"));
			} else {
				builder.command("sh", "-c", cmd);
				builder.directory(new File("/usr/local/bin/"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder;
	}

	public static List<String> execCommandList(ProcessBuilder builder) {
		try {
			String line = null;
			ArrayList<String> list = new ArrayList();

			Process process = builder.start();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));

			if(bufferedReader != null || !bufferedReader.equals("")){
				list.add("	[ProcessBuilder Success] : ");
				while ((line = bufferedReader.readLine()) != null) {
					list.add(line);
				}
			}

			bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			if(bufferedReader != null || !bufferedReader.equals("")){
				list.add("	[ProcessBuilder Error] : ");
				while ((line = bufferedReader.readLine()) != null) {
					list.add("		" + line);
				}
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String execCommandString(ProcessBuilder builder) {
		try {
			String line = "";
			String temp = null;
			Process process = builder.start();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));

			if(bufferedReader != null || !bufferedReader.equals("")){
				while ((temp = bufferedReader.readLine()) != null) {
					line += temp;
				}
			}

			bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			if(bufferedReader != null || !bufferedReader.equals("")){
				while ((temp = bufferedReader.readLine()) != null) {
					line += temp;
				}
			}

			return line;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String imageToBase64(String filePath, String fileName) {
		String base64Img = "";

		File f = new File(filePath +"//"+ fileName);
		if (f.exists() && f.isFile() && f.length() > 0) {
			byte[] bt = new byte[(int) f.length()];
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(f);
				fis.read(bt);
				base64Img = new String(Base64.getEncoder().encode(bt));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
				} catch (Exception e) {
				}
			}
		}

		return base64Img;
	}
	public static String OC(String base64) {
		String res = "";
		res = execCommandString(inputCommand("python pycmd.py --image64 " + base64));
		return res;
	}
	public static String OCR(String base64) {
		PythonInterpreter interpreter = new PythonInterpreter();
		interpreter.execfile("./image/pycmd.py");

		PyFunction func = (PyFunction) interpreter.get("predict", PyFunction.class);
		PyObject pobj =  func.__call__(new PyString(base64));
		return pobj.toString();
	}

	public static void main(String[] args) {
		//SpringApplication.run(TokkiOcrApplication.class, args);
		System.out.println(OCR(imageToBase64("./image", "test.png")));

	}
}