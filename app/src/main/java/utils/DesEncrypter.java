package utils;

import android.util.Base64;
//import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Decoder;

// 参数加密
public class DesEncrypter {
	Cipher ecipher;
	Cipher dcipher;
	byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
			(byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

	/**
	 * 构造方法
	 * @param passPhrase 将用户的apikey作为密钥传入
	 * @throws Exception
	 */
	public DesEncrypter(String passPhrase) throws Exception {
		int iterationCount = 2;
		KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,
				iterationCount);
		SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
				.generateSecret(keySpec);
		ecipher = Cipher.getInstance(key.getAlgorithm());
		dcipher = Cipher.getInstance(key.getAlgorithm());
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
				iterationCount);
		ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	}

	/**
	 * 加密
	 * @param str 要加密的字符串
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String str) throws Exception {
		 str = new String(str.getBytes(), "UTF-8");
//		return Base64.encodeBase64String(ecipher.doFinal(str.getBytes()));//后台的commons-codec-1.10.jar包里的方法
		return Base64.encodeToString(ecipher.doFinal(str.getBytes()),Base64.DEFAULT);//Android自身的BASE64方法
	}

//	/**
//	 * 解密
//	 * @param str 要解密的字符串
//	 * @return
//	 * @throws Exception
//	 */
//	public String decrypt(String str) throws Exception {
//		return new String(dcipher.doFinal(Base64.decodeBase64(str)), "UTF-8");
//	}

	//图片解码
	public static void decodeBASE64(String imgStr, File file) {
		BASE64Decoder decoder = new BASE64Decoder();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			byte[] decoderBytes = decoder.decodeBuffer(imgStr);
			fos.write(decoderBytes);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		DesEncrypter desEncrypter = new DesEncrypter("您的apikey");
		// str为加密前的参数字符串
		String str = "idCardName= 程XX&idCardCode=513722112907200XXX";
		//String str1 = "jY/t5a8oboawIyZOrzV9wB3gb9NGn9YC";
		// 加密
		System.out.println(desEncrypter.encrypt(str));
		// 解密
		//System.out.println(desEncrypter.decrypt(str1));
	}
}
