package lock;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

//import org.apache.commons.codec.binary.Hex;


public class MsgSignature {
	
	//1.初始化获得密钥
	public KeyPair init() throws NoSuchAlgorithmException{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(512);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}
	
	public RSAPrivateKey getPrivateKey(KeyPair keyPair){
		return (RSAPrivateKey)keyPair.getPrivate();
	}
	
	public RSAPublicKey getPublicKey(KeyPair keyPair){
		return (RSAPublicKey)keyPair.getPublic();
	}
	
	//进行签名操作
	public byte[] RSAEncry(byte[] data,RSAPrivateKey rsaPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, 
	                                                                                     InvalidKeyException, SignatureException{
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(privateKey);
		signature.update(data);
		byte[] result = signature.sign();
		//System.out.println(result.length+"    "+Hex.encodeHexString(result));
		return result;
	}
	
	//验证签名
	public boolean Check(byte[] data,byte[] result,RSAPublicKey rsaPublicKey) throws InvalidKeySpecException,
	                                           NoSuchAlgorithmException, InvalidKeyException, SignatureException{
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(publicKey);
		signature.update(data);
		return signature.verify(result);
	}
	
//	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, SignatureException {
//		byte[] data = {11,22,33,44};
//		MsgSignature m = new MsgSignature();
//		KeyPair keyPair = m.init();
//		RSAPublicKey publicKey = m.getPublicKey(keyPair);
//		RSAPrivateKey privateKey = m.getPrivateKey(keyPair);
//		byte[] result = m.RSAEncry(data, privateKey);
//		System.out.print(m.Check(data, result, publicKey));
//	}
}
