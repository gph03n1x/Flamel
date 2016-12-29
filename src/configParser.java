import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class configParser {
	private HashMap<String, String> config = new HashMap<String, String>();
	
	public configParser(){
		Properties prop = new Properties();
		InputStream input = null;
		

		try {
			input = new FileInputStream("flamel.properties");
			// load a properties file
			prop.load(input);
			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				config.put(key, value);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	public String get(String Key){
		return config.get(Key);
	}
}
	
