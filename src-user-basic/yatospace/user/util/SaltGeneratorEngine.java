package yatospace.user.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Машина за генерисање салт кодова. 
 * Десет декадних цифара у низу. 
 * @author MV
 * @version 1.0
 */
public class SaltGeneratorEngine implements Serializable{
	private static final long serialVersionUID = -2032227335008120075L;
	private List<String> generateCodes = new ArrayList<>();

	public synchronized List<String> getGenerateCodes() {
		return new ArrayList<>(generateCodes);
	}

	public synchronized void setGenerateCodes(List<String> generateCodes) {
		this.generateCodes = new ArrayList<>(generateCodes);
	} 
	
	public synchronized String generateNewCode() {
		String code = ""; 
		Random randomizer = new Random();
		do for(int i=0; i<10; i++) 
			code+= ""+Math.abs(randomizer.nextInt()%10); 
		while(generateCodes.contains(code));
		generateCodes.add(code);
		return code; 
	}
}
