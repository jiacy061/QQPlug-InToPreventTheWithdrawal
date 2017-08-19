package lock;

import java.security.InvalidKeyException;  
import java.security.NoSuchAlgorithmException;  
import java.security.Security;  
  
import javax.crypto.BadPaddingException;  
import javax.crypto.Cipher;  
import javax.crypto.IllegalBlockSizeException;  
import javax.crypto.KeyGenerator;  
import javax.crypto.NoSuchPaddingException;  
import javax.crypto.SecretKey;  
  
public class EncrypDES {  
      
    //KeyGenerator 提供对称密钥生成器的功能，支持各种算法  
    private KeyGenerator keygen;  
    //SecretKey 负责保存对称密钥  
    private static SecretKey deskey;  
    //Cipher负责完成加密或解密工作  
    private Cipher c;  
    //该字节数组负责保存加密的结果  
    private byte[] cipherByte;  
    
    public SecretKey getSecretKey(){
    	return deskey;
    }
      
    public EncrypDES() throws NoSuchAlgorithmException, NoSuchPaddingException{  
        Security.addProvider(new com.sun.crypto.provider.SunJCE());  
        //实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)  
        keygen = KeyGenerator.getInstance("DES");  
        //生成密钥  
        deskey = keygen.generateKey();  
        //System.out.println("desket :"+deskey);
        //生成Cipher对象,指定其支持的DES算法  
        c = Cipher.getInstance("DES");  ///ECB/NoPadding
    }  
      
    /** 
     * 对字符串加密 
     *  
     * @param str 
     * @return 
     * @throws InvalidKeyException 
     * @throws IllegalBlockSizeException 
     * @throws BadPaddingException 
     */  
    public byte[] Encrytor(byte[] src,SecretKey key) throws InvalidKeyException,  
            IllegalBlockSizeException, BadPaddingException {  
        // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式  
        c.init(Cipher.ENCRYPT_MODE, key);  
        //byte[] src = str.getBytes();  
        // 加密，结果保存进cipherByte  
        cipherByte = c.doFinal(src);  
        return cipherByte;  
    }  
  
    /** 
     * 对字符串解密 
     *  
     * @param buff 
     * @return 
     * @throws InvalidKeyException 
     * @throws IllegalBlockSizeException 
     * @throws BadPaddingException 
     */  
    public byte[] Decryptor(byte[] buff,SecretKey key) throws InvalidKeyException,  
            IllegalBlockSizeException, BadPaddingException {  
        // 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式  
        c.init(Cipher.DECRYPT_MODE, key);  
        cipherByte = c.doFinal(buff);
        return cipherByte;  
    }  
  
    /** 
     * @param args 
     * @throws NoSuchPaddingException  
     * @throws NoSuchAlgorithmException  
     * @throws BadPaddingException  
     * @throws IllegalBlockSizeException  
     * @throws InvalidKeyException  
     */  
    public static void main(String[] args) throws Exception {  
        EncrypDES de1 = new EncrypDES();  
        byte[] b = {11,22,33,44};
        SecretKey key = de1.getSecretKey();
        byte[] encontent = de1.Encrytor(b,key);  
        byte[] decontent = de1.Decryptor(encontent,key);  
        System.out.println("明文是:" + b);  
        System.out.println("加密后:" );  
        for(int i = 0;i < encontent.length;i++){
        	System.out.print(encontent[i]+"   ");
        }
        System.out.println("解密后:" + new String(decontent));  
      
    }

  
}  