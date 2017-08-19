package lock;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class Main {
	public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, 
	                                                                   IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, SignatureException{
		//初始化设置
		EncrypDES encrypDES = new EncrypDES();
		String path = "D:\\test.jpg";
		String path1 = "D:\\11.jpg";
		String path2 = "D:\\22.jpg";
		String path3 = "D:\\33.jpg";
		String path4 = "D:\\44.jpg";
		MsgSignature m = new MsgSignature();
		SecretKey key = encrypDES.getSecretKey();
		SecretKey keyMac = HMAC.init();
		KeyPair keyPair = m.init();
		RSAPublicKey publicKey = m.getPublicKey(keyPair);
		RSAPrivateKey privateKey = m.getPrivateKey(keyPair);
		
		//进行加密，数字签名和计算HMAC
		byte[] buf = JPEG.readJpeg(path);
		byte[] SigResult = m.RSAEncry(buf, privateKey);
//		JPEG.HmacEncryp(path, path1, keyMac);
//		buf = JPEG.readJpeg(path1);
		JPEG.jpegEncryp(buf, SigResult ,key, path1);
		
		//解密 验证消息完整性和数字签名验证
		byte[] bMac =JPEG.jpegDecryp(path1, path2, key);
//		byte[] reSigResult =JPEG.getSigResult(path2);
//		byte[] b = JPEG.readJpeg(path4);
//		byte[] cMac = HMAC.encryptHMAC(b, keyMac);
//		System.out.println("消息是否正确 ： "+Arrays.equals(bMac,cMac));
//		System.out.println("数字签名验证 ： "+m.Check(b, reSigResult, publicKey));
		
		/*for(int i = 0;i < bMac.length;i++){
			System.out.print(img.byteToHexString(bMac[i])+"   ");
			
		}
		System.out.println("");
		System.out.println("");
		byte[] b1 = img.readJpeg("D:/44.jpg");
		byte[] dMac = hmac.encryptHMAC(b1, keyMac);
		for(int i = 0;i < dMac.length;i++){
			System.out.print(img.byteToHexString(dMac[i])+"   ");
			
		}*/
	}
}
