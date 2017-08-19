package tool;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import contral.MainContral;
import model.ImageBag;

public class ContralUtil {

	private static MainContral mainContral;
	
	public static void setMainContral(MainContral _mainContral) {
		mainContral = _mainContral;
	}
	
	public static ArrayList<ImageBag> getImageFilesData(String path) {
		return mainContral.getImageFilesData(path);
	}
	
	public static void stopImageHandle() {
		mainContral.stopImageHandle();
	}
	
	public static void startImageHandle() {
		mainContral.startImageHandle();
	}
	
	public static void saveConfig() {
		mainContral.saveConfig();
	}
	
	public static void setConfigDialog() {
		mainContral.setConfigDialog();
	}

	public static void showImageFile(String imagePath) {
		mainContral.showImageFile(imagePath);
	}
	
	public static void encrypImageFile(String oldPath, String newPath) {
		mainContral.encrypImageFile(oldPath, newPath);
	}

	public static void endProgram() {
		mainContral.endProgram();
	}

	public static void showNewFileTip() {
		mainContral.showNewFileTip();
	}

	public static void getPassword() {
		mainContral.getPassword();
	}

	public static void checkPassword(String password) {
		mainContral.checkPassword(password);
	}
}
