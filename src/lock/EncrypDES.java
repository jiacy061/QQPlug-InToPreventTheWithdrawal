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
      
    //KeyGenerator �ṩ�Գ���Կ�������Ĺ��ܣ�֧�ָ����㷨  
    private KeyGenerator keygen;  
    //SecretKey ���𱣴�Գ���Կ  
    private static SecretKey deskey;  
    //Cipher������ɼ��ܻ���ܹ���  
    private Cipher c;  
    //���ֽ����鸺�𱣴���ܵĽ��  
    private byte[] cipherByte;  
    
    public SecretKey getSecretKey(){
    	return deskey;
    }
      
    public EncrypDES() throws NoSuchAlgorithmException, NoSuchPaddingException{  
        Security.addProvider(new com.sun.crypto.provider.SunJCE());  
        //ʵ����֧��DES�㷨����Կ������(�㷨���������谴�涨�������׳��쳣)  
        keygen = KeyGenerator.getInstance("DES");  
        //������Կ  
        deskey = keygen.generateKey();  
        //System.out.println("desket :"+deskey);
        //����Cipher����,ָ����֧�ֵ�DES�㷨  
        c = Cipher.getInstance("DES");  ///ECB/NoPadding
    }  
      
    /** 
     * ���ַ������� 
     *  
     * @param str 
     * @return 
     * @throws InvalidKeyException 
     * @throws IllegalBlockSizeException 
     * @throws BadPaddingException 
     */  
    public byte[] Encrytor(byte[] src,SecretKey key) throws InvalidKeyException,  
            IllegalBlockSizeException, BadPaddingException {  
        // ������Կ����Cipher������г�ʼ����ENCRYPT_MODE��ʾ����ģʽ  
        c.init(Cipher.ENCRYPT_MODE, key);  
        //byte[] src = str.getBytes();  
        // ���ܣ���������cipherByte  
        cipherByte = c.doFinal(src);  
        return cipherByte;  
    }  
  
    /** 
     * ���ַ������� 
     *  
     * @param buff 
     * @return 
     * @throws InvalidKeyException 
     * @throws IllegalBlockSizeException 
     * @throws BadPaddingException 
     */  
    public byte[] Decryptor(byte[] buff,SecretKey key) throws InvalidKeyException,  
            IllegalBlockSizeException, BadPaddingException {  
        // ������Կ����Cipher������г�ʼ����DECRYPT_MODE��ʾ����ģʽ  
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
        System.out.println("������:" + b);  
        System.out.println("���ܺ�:" );  
        for(int i = 0;i < encontent.length;i++){
        	System.out.print(encontent[i]+"   ");
        }
        System.out.println("���ܺ�:" + new String(decontent));  
      
    }

  
}  