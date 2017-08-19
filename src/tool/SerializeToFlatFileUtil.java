package tool;

import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;

import model.KeyBag;  
  
  
public class SerializeToFlatFileUtil {  
    public static void main(String[] args) {  
        KeyBag keyBag = new KeyBag();
        saveKeyBag(keyBag); 
        getKeyBag();
    }
    
    private static String projectPath = System.getProperty("user.dir");
    private static final String keyPath = "Key.dat";
      
    public static void saveKeyBag(KeyBag keyBag){  
        try {  
            FileOutputStream fos = new FileOutputStream(projectPath+"//"+keyPath);  
            ObjectOutputStream oos = new ObjectOutputStream(fos);  
            
            oos.writeObject(keyBag);  
            oos.flush();  
            oos.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    public static KeyBag getKeyBag() {
    	KeyBag keyBag = null;
        try {  
            FileInputStream fis = new FileInputStream(projectPath+"//"+keyPath);
            ObjectInputStream ois = new ObjectInputStream(fis);  
            
            keyBag = (KeyBag)ois.readObject();  
            ois.close();
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return keyBag;
    }
}  