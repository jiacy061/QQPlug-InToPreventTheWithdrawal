package lock;

import javax.crypto.KeyGenerator;  
import javax.crypto.Mac;  
import javax.crypto.SecretKey;  
import java.security.NoSuchAlgorithmException;  
  
/** 
 * Created by xiang.li on 2015/2/27. 
 */  
public class HMAC {  
    /** 
     * ������ܷ�ʽ 
     * MAC�㷨��ѡ���¶����㷨 
     * <pre> 
     * HmacMD5 
     * HmacSHA1 
     * HmacSHA256 
     * HmacSHA384 
     * HmacSHA512 
     * </pre> 
     */  
    private final static String KEY_MAC = "HmacSHA1";  
  
    /** 
     * ȫ������ 
     */  
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",  
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
  
    /** 
     * ���캯�� 
     */  
    public HMAC() {  
  
    }  
  
    /** 
     * BASE64 ���� 
     * @param key ��Ҫ���ܵ��ֽ����� 
     * @return �ַ��� 
     * @throws Exception 
     */  
/*    public static String encryptBase64(byte[] key) throws Exception {  
        return (new BASE64Encoder()).encodeBuffer(key);  
    }  */
  
    /** 
     * BASE64 ���� 
     * @param key ��Ҫ���ܵ��ַ��� 
     * @return �ֽ����� 
     * @throws Exception 
     */  
/*    public static byte[] decryptBase64(String key) throws Exception {  
        return (new BASE64Decoder()).decodeBuffer(key);  
    }  */
  
    /** 
     * ��ʼ��HMAC��Կ 
     * @return 
     */  
    public static SecretKey init() {  
        SecretKey key = null;  
        //String str = "";  
        try {  
            KeyGenerator generator = KeyGenerator.getInstance(KEY_MAC);  
            key = generator.generateKey();  
            //str = encryptBase64(key.getEncoded());  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return key;  
    }  
  
    /** 
     * HMAC���� 
     * @param data ��Ҫ���ܵ��ֽ����� 
     * @param key ��Կ 
     * @return �ֽ����� 
     */  
    public static byte[] encryptHMAC(byte[] data, SecretKey key) {  
        byte[] bytes = null;  
        try {  
            Mac mac = Mac.getInstance(key.getAlgorithm());  
            mac.init(key);  
            bytes = mac.doFinal(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bytes;  
    }  
    
/*    public static byte[] encryptHMAC(byte[] data, String key) {  //��Կ���ַ����ļ��ܷ���
        byte[] bytes = null;  
        try {  
        	SecretKey secretKey  = new SecretKeySpec(decryptBase64(key), KEY_MAC);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
            mac.init(secretKey);  
            bytes = mac.doFinal(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bytes;  
    } */
  
    /** 
     * HMAC���� 
     * @param data ��Ҫ���ܵ��ַ��� 
     * @param key ��Կ 
     * @return �ַ��� 
     */  
   /* public static byte[] encryptHMAC(byte[] data, SecretKey key) {  //��Կ����Կ��ʽ�ļ��ܷ���
        if (data == null) {  
            return null;  
        }  
        byte[] bytes = encryptHMAC(data, key);  
        return bytes;  
    }  */
  
  
    /** 
     * ��һ���ֽ�ת����ʮ��������ʽ���ַ��� 
     * @param b �ֽ����� 
     * @return �ַ��� 
     */  
    private static String byteToHexString(byte b) {  
        int ret = b;  
        if (ret < 0) {  
            ret += 256;  
        }  
        int m = ret / 16;  
        int n = ret % 16;  
        return hexDigits[m] + hexDigits[n];  
    }  
  
    /** 
     * ת���ֽ�����Ϊʮ�������ַ��� 
     * @param bytes �ֽ����� 
     * @return ʮ�������ַ��� 
     */  
    private static String byteArrayToHexString(byte[] bytes) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < bytes.length; i++) {  
            sb.append(byteToHexString(bytes[i]));  
        }  
        return sb.toString();  
    }  
  
    /** 
     * ���Է��� 
     * @param args 
     */  
    public static void main(String[] args) throws Exception {  
        SecretKey key = HMAC.init();  
        System.out.println("Mac��Կ:\n" + key);  
        byte[] word = {11,22,33,44,55,66,77,88,99,00};
        //System.out.println(encryptHMAC(word, key).length);  
    }  
}
