package contral;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import lock.EncrypDES;
import lock.HMAC;
import lock.JPEG;
import lock.MsgSignature;
import model.ConfigBag;
import model.ImageBag;
import model.KeyBag;
import tool.BareBonesBrowserLaunch;
import tool.ContralUtil;
import tool.FileUtil;
import tool.PicUtil;
import tool.SendmailUtil;
import tool.SerializeToFlatFileUtil;
import tool.SysInfoManager;
import view.ConfigDialog;
import view.MainView;

public class MainContral {
	private boolean executableFlag;
	private MainView mainView;
	private ConfigDialog configDialog;
	private String[] targetPath;
	private ImageHandleUnit imageHandleUnit;
	
	private final String EmailReceiverAddress = "jiacyc@sohu.com";
	private final String configFileName = "config.dat";
	private final String url = "https://gist.github.com/jiacy061/952b4813cd2f3efe05fc130d95ab40c0";
	private final int maxWidth = 320;
	private final int maxHeight = 200;
	
	private SysInfoManager sysInfoManager;
	private String wlanAddress;
	private String computerName;
	
	private KeyBag keyBag;
	private EncrypDES encrypDES;
	private MsgSignature m;
	
	public MainContral() {
		ContralUtil.setMainContral(this);
		mainView = new MainView();
		sysInfoManager = new SysInfoManager();
		start();
	}

	private void start() {
		getConfig();
		getKey();
		if(executableFlag)
			loadSavedImageFile();
	}
	
	private void loadSavedImageFile() {
		imageHandleUnit = new ImageHandleUnit(targetPath);
		imageHandleUnit.loadSavedImageFile();
		mainView.refreshTableData();
	}

	private void getKey() {
		wlanAddress = sysInfoManager.getWlanAddress();
		computerName = sysInfoManager.getComputerName();
		if(wlanAddress.equals(ConfigBag.getWlanAddress())) {
			keyBag = SerializeToFlatFileUtil.getKeyBag();
			//System.out.println("load keyBag!"+keyBag.getPrivateKey());
		} else {
			// 初始化设置
			try {
				encrypDES = new EncrypDES();
				keyBag = new KeyBag();
				m = new MsgSignature();
				keyBag.setKey(encrypDES.getSecretKey());
				keyBag.setKeyMac(HMAC.init());
				KeyPair keyPair = m.init();
				keyBag.setPublicKey(m.getPublicKey(keyPair));
				keyBag.setPrivateKey(m.getPrivateKey(keyPair));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}
			SerializeToFlatFileUtil.saveKeyBag(keyBag);
			//System.out.println("save keyBag!");
		}
	}

	public void stopImageHandle() {
		if(imageHandleUnit!=null)
			imageHandleUnit.endAll();
	}

	public void startImageHandle() {
		if(executableFlag) {
			//System.out.println("start threads!");
			if(imageHandleUnit==null)
				imageHandleUnit = new ImageHandleUnit(targetPath);
			imageHandleUnit.startAll();
		} else {
			
		}
		
	}

	private void getConfig() {
		String projectPath = System.getProperty("user.dir");
//		//System.out.println(projectPath);
		File configFile = new File(projectPath+"//"+configFileName);
		if( configFile.exists() ) {
			//System.out.println("configFile exists!");
			setConfig(configFile);
			setTargetPath();
			executableFlag = true;
		} else {
			//System.out.println("configFile doesn't exist!");
			executableFlag = false;
		}
		
	}

	private void setTargetPath() {
		String homePath = System.getProperty("user.home");
		String tencentFilesPath = homePath + "//Documents//Tencent Files";
		tencentFilesPath = FileUtil.formatPath(tencentFilesPath);
//		//System.out.println(homePath);
//		//System.out.println("tencentFilesPath:" + tencentFilesPath);
		if(ConfigBag.isSelectAll()) {
			File file = new File(tencentFilesPath);
			File[] fs = file.listFiles();
			targetPath = new String[2*(fs.length-1)];
			for(int i=0; i<fs.length; i++) {
				if(!fs[i].getName().equals("All Users")) {
					targetPath[2*i] = tencentFilesPath + "//" + fs[i].getName() + "//Image//C2C";
					targetPath[2*i+1] = tencentFilesPath + "//" + fs[i].getName() + "//Image//Group";
				}
			}
		} else {
			targetPath = new String[2];
			targetPath[0] = tencentFilesPath + "//" + ConfigBag.getQQNumber() + "//Image//C2C";
			targetPath[1] = tencentFilesPath + "//" + ConfigBag.getQQNumber() + "//Image//Group";
		}
		
//		for(String s : targetPath)
			//System.out.println(s);
	}

	public void saveConfig() {
		StringBuffer sb = new StringBuffer();
		sb.append("<QQNumber>");
		sb.append(configDialog.getQQNumber());
		sb.append("</QQNumber>\r\n");
		sb.append("<isRememberNum>");
		sb.append(configDialog.isRememberNum()?"true":"false");
		sb.append("</isRememberNum>\r\n");
		sb.append("<isSelectAll>");
		sb.append(configDialog.isSelectAll()?"true":"false");
		sb.append("</isSelectAll>\r\n");
		sb.append("<savePath>");
		sb.append(FileUtil.formatPath(configDialog.getSavePath()));
		sb.append("</savePath>\r\n");
		sb.append("<wlanAddress>");
		sb.append(sysInfoManager.getWlanAddress());
		sb.append("</wlanAddress>\r\n");
		sb.append("<computerName>");
		sb.append(sysInfoManager.getComputerName());
		sb.append("</computerName>\r\n");
		//System.out.println(sb.toString());
		
		String projectPath = System.getProperty("user.dir");
//		//System.out.println(projectPath);
		File configFile = new File(projectPath+"//"+configFileName);
		if(configFile.exists()) {
			configFile.delete();
		}
		FileWriter fw;
		try {
			fw = new FileWriter(configFile);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.print(sb.toString());
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setConfig(configFile);
		setTargetPath();
		executableFlag = true;
	}

	private void setConfig(File configFile) {
		String s;
		try {
			FileReader fr = new FileReader(configFile);
			BufferedReader br = new BufferedReader(fr);
			while( (s = br.readLine()) != null ) {
				if(s.indexOf("<QQNumber>")!=-1) {
					s = s.substring(s.indexOf(">")+1, s.indexOf("</"));
					ConfigBag.setQQNumber(s);
				} else if(s.indexOf("<isRememberNum>")!=-1) {
					boolean isRememberNum;
					s = s.substring(s.indexOf(">")+1, s.indexOf("</"));
					if(s.equals("true")) {
						isRememberNum = true;
					} else {
						isRememberNum = false;
						ConfigBag.setQQNumber("");
					}
					ConfigBag.setRememberNum(isRememberNum);
				} else if(s.indexOf("<isSelectAll>")!=-1) {
					boolean isSelectAll;
					s = s.substring(s.indexOf(">")+1, s.indexOf("</"));
					if(s.equals("true")) {
						isSelectAll = true;
					} else {
						isSelectAll = false;
					}
					ConfigBag.setSelectAll(isSelectAll);
				} else if(s.indexOf("<savePath>")!=-1) {
					s = s.substring(s.indexOf(">")+1, s.indexOf("</"));
					s = FileUtil.formatPath(s);
					ConfigBag.setSavePath(s);
				} else if(s.indexOf("<wlanAddress>")!=-1) {
					s = s.substring(s.indexOf(">")+1, s.indexOf("</"));
					ConfigBag.setWlanAddress(s);
				} else if(s.indexOf("<computerName>")!=-1) {
					s = s.substring(s.indexOf(">")+1, s.indexOf("</"));
					ConfigBag.setComputerName(s);
				}
				//System.out.println(s);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<ImageBag> getImageFilesData(String path) {
		if(imageHandleUnit==null)
			return null;
		
		if(path.equals("全选")) {
			return imageHandleUnit.getAllImageFileList();
		} else {
			return imageHandleUnit.getPartImageList(path);
		}
	}

	public void setConfigDialog() {
		configDialog = mainView.getConfigDialog();
	}

	public void endProgram() {
		if(imageHandleUnit==null)
			return;
		
		ArrayList<ImageBag> list = imageHandleUnit.getAllImageFileList();
		for(ImageBag imageBag : list) {
			if(imageBag.isWithdrawal()) {
				continue;
			} else {
				imageBag.getFile().delete();
			}
		}
		
		File showPic = new File(ConfigBag.getSavePath() + "//showPic.jpg");
		showPic.delete();
	}
	
	public void encrypImageFile(String oldPath, String newPath) {
		try {
			// 进行加密，数字签名和计算HMAC
			m = new MsgSignature();
			byte[] buf = JPEG.readJpeg(oldPath);
			byte[] SigResult = m.RSAEncry(buf, keyBag.getPrivateKey());
//			JPEG.HmacEncryp(oldPath, newPath, keyMac);
//			buf = JPEG.readJpeg(newPath);
			JPEG.jpegEncryp(buf, SigResult, keyBag.getKey(), newPath);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
	}
	
	private void decrypImageFile(String imageName) {
		String imagePath = ConfigBag.getSavePath() + imageName;
		StringBuffer newPath = new StringBuffer(ConfigBag.getSavePath());
		newPath.append("//DecrypFiles");
		File f = new File(newPath.toString());
		if(!f.exists())
			f.mkdir();
		newPath.append("//Decryp-");
		newPath.append(imageName);
		// 解密 验证消息完整性和数字签名验证
		try {
			byte[] bMac = JPEG.jpegDecryp(imagePath, newPath.toString(), keyBag.getKey());
//			byte[] reSigResult = JPEG.getSigResult("D:\\22.jpg");
//			byte[] b = JPEG.readJpeg("D:\\44.jpg");
//			byte[] cMac = HMAC.encryptHMAC(b, keyMac);
//			//System.out.println("消息是否正确 ： " + Arrays.equals(bMac, cMac));
//			//System.out.println("数字签名验证 ： " + m.Check(b, reSigResult, publicKey));			
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showImageFile(String imagePath) {
		String newPath = ConfigBag.getSavePath() + "//showPic.jpg";
		// 解密 验证消息完整性和数字签名验证
		try {
			JPEG.jpegDecryp(imagePath, newPath, keyBag.getKey());
			PicUtil picUtil = new PicUtil(maxWidth, maxHeight);
			picUtil.compressPhoto(newPath);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showNewFileTip() {
		mainView.showNewFileTip();
	}

	public void getPassword() {
		new Thread() {			
			@Override
			public void run() {
				BareBonesBrowserLaunch.openURL(url);
				StringBuffer sb = new StringBuffer();
				String title = computerName+"的返回值";
				sb.append("<p>计算机名："+computerName+"</p><br>");
				sb.append("<p>MAC地址："+wlanAddress+"</p><br>");
				sb.append("<p>HASH值："+wlanAddress.hashCode()+"</p><br>");
				//System.out.println(sb.toString());
				//System.out.println(wlanAddress.hashCode()+"::HASH");
				SendmailUtil.doSendHtmlEmail(title, sb.toString(), EmailReceiverAddress);
			}
		}.start();
	}

	public void checkPassword(String password) {
		//System.out.println("password:"+password);
		//System.out.println("HashCode:"+wlanAddress.hashCode());
		if(password.equals(""+wlanAddress.hashCode())) {
			//System.out.println("password is right!");
			decrypImageFile(mainView.getSelectedImageName());
		} else {
			//System.out.println("password is wrong!");
		}
	}
}
