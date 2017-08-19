package lock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class JPEG {
	public static byte[] readJpeg(String path) throws IOException{ 
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		byte[] bis = new byte[(int) f.length()];
		fis.read(bis);
		fis.close();
		return bis;
	}
	
	public static void writeJpeg(String path ,byte[] b) throws IOException{
		File f = new File(path);
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(b);
		fos.flush();
	   fos.close();
	}
	
	public static String byteToHexString(byte b){
		String result = Integer.toHexString(b & 0xff);
		if(result.length() == 1) result = "0"+result;
		return result;
	}
	
	public static void jpegEncryp(byte[] buf,byte[] SigResult,SecretKey key, String path)
						throws NoSuchAlgorithmException, 
	                           NoSuchPaddingException, InvalidKeyException, 
	                           IllegalBlockSizeException, BadPaddingException, IOException{
		File file = new File(path);
		FileOutputStream fop = new FileOutputStream(file);
		EncrypDES des = new EncrypDES();
		int biao = indexFFDA(buf);
		byte[] b = StrByte(buf,biao,buf.length);
		byte[] c = add(StrByte(buf,0, biao),des.Encrytor(b, key));
		fop.write(add(c,SigResult));
		fop.close();
	}
	
	public static byte[] jpegDecryp(String inPath, String outPath, SecretKey key) throws IOException,
	                                                    InvalidKeyException, IllegalBlockSizeException,
	                                                     BadPaddingException, NoSuchAlgorithmException, 
	                                                     NoSuchPaddingException{
		EncrypDES des = new EncrypDES();
		byte[] buf = readJpeg(inPath);
		int biao = indexFFDA(buf);
		File file = new File(outPath);
		FileOutputStream fop = new FileOutputStream(file);
		byte[] bufPrev = StrByte(buf,0,biao);
		byte[] b = StrByte(buf,biao,buf.length-64);
		byte[] bufBeh = des.Decryptor(b, key);
		byte[] buff = StrByte(bufBeh,0,bufBeh.length-20);
		fop.write(add(bufPrev,buff));
		fop.close();
		return StrByte(bufBeh,bufBeh.length-20,bufBeh.length);
	}
	
	public static byte[] getSigResult(String path) throws IOException{
        byte[] buf = readJpeg(path);
		byte[] SigResult = StrByte(buf,buf.length-64,buf.length);
		return SigResult;
	}
	
	public static void HmacEncryp(String inPath, String outPath, SecretKey key) throws IOException{
		byte[] buf = readJpeg(inPath);
		byte[] buf_en = HMAC.encryptHMAC(buf, key);
		writeJpeg(outPath,add(buf,buf_en));
	}
	
	public static byte[] StrByte(byte[] buf,int begin,int end){
		byte[] b = new byte[end-begin];
		for(int i = begin;i < end;i++){
			b[i-begin] = buf[i];
		}
		return b;
	}
	
	public static int indexFFDA(byte[] buf){
		int biao = 0;
		for(int i = 0;i < buf.length;i++){
			if(byteToHexString(buf[i]).equals("ff") && byteToHexString(buf[i+1]).equals("da")){
				biao = i+24;
					break;
			}
		}
		return biao;
	}
	
	public static byte[] add(byte[] buf1,byte[] buf2){
		int len = buf1.length;
		byte[] newbuf = new byte[len+buf2.length];
		for(int i = 0;i < buf1.length;i++){
			newbuf[i] = buf1[i];
		}
		for(int i = 0;i < buf2.length;i++){
			newbuf[i+len] = buf2[i];
		}
		return newbuf;
	}

}
