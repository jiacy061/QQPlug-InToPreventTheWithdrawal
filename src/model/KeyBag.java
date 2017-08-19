package model;

import java.io.Serializable;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.SecretKey;

@SuppressWarnings("serial")
public class KeyBag implements Serializable {
	private SecretKey key;
	private SecretKey keyMac;
	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;
	
	public SecretKey getKey() {
		return key;
	}
	public void setKey(SecretKey key) {
		this.key = key;
	}
	public SecretKey getKeyMac() {
		return keyMac;
	}
	public void setKeyMac(SecretKey keyMac) {
		this.keyMac = keyMac;
	}
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(RSAPublicKey publicKey) {
		this.publicKey = publicKey;
	}
	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(RSAPrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	
}
