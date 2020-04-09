package com.yun.learn.util;
/**
 * 加密算法
 * @author 马亚民
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Security {
	public final static String AES = "AES";
	public final static String DES = "DES";
	public final static String DESEDE = "DESede";
	
	// mode:    ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128
	// padding: Nopadding/PKCS5Padding/ISO10126Padding/ 

	public final static String getMD5(String s,String charsetname) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp;
			if(charsetname==null||"".equals(charsetname)) {
				strTemp = s.getBytes();
			}
			else {
				strTemp = s.getBytes(charsetname);
			}
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}

	}
	
	/**
	 * 默认使用utf-8编码
	 * @param s
	 * @return
	 */
	public final static String getMD5(String s) {
		return getMD5(s,"UTF-8");
	}
	  
	public final static String getSHA1(String s) {
		MessageDigest alga = null;
		try {
			alga = MessageDigest.getInstance("SHA-1");
			alga.update(s.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] digesta = alga.digest();
		String stmp = "";
		String hs = "";
		for (int n = 0; n < digesta.length; n++) {
			stmp = (java.lang.Integer.toHexString(digesta[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;

	}
	
	/**
	 * RC4加密解密函数。输出编码后的字符串，可能包含不可见字符，需要base64编码
	 * @param input
	 * @param key
	 * @return
	 */
	public final static String getRC4(String input,String key)
	{
		int[] iS = new int[256];
		byte[] iK = new byte[256];
		
		for (int i=0;i<256;i++)
			iS[i]=i;
			
		int j = 1;
		
		for (short i= 0;i<256;i++)
		{
			iK[i]=(byte)key.charAt((i % key.length()));
		}
		
		j=0;
		
		for (int i=0;i<256;i++)
		{
			j=(j+iS[i]+iK[i]) % 256;
			int temp = iS[i];
			iS[i]=iS[j];
			iS[j]=temp;
		}
	
	
		int i=0;
		j=0;
		char[] iInputChar = input.toCharArray();
		char[] iOutputChar = new char[iInputChar.length];
		for(short x = 0;x<iInputChar.length;x++)
		{
			i = (i+1) % 256;
			j = (j+iS[i]) % 256;
			int temp = iS[i];
			iS[i]=iS[j];
			iS[j]=temp;
			int t = (iS[i]+(iS[j] % 256)) % 256;
			int iY = iS[t];
			char iCY = (char)iY;
			iOutputChar[x] =(char)( iInputChar[x] ^ iCY) ;	
		}
		return new String(iOutputChar);
	}
	
	/**
	 * 使用DES算法加密字符串
	 * @param content 要加密的字符串
	 * @return 加密后的字符串
	 */
	public  final static String encryptDES(String content,String keyWord){
		SecretKey key=Security.createSecretKey("DES", keyWord);
    	return Security.encrypt2String(content, "DES", key);
	}
    
	/**
	 * 使用AES算法加密字符串
	 * @param content 要加密的字符串
	 * @return 加密后的字符串
	 */
    public static String encryptAES(String content,String keyWord){
    	byte[] iv={ 109, 121, 109, 98, 53, 50, 104, 97, 110, 103, 97, 114, 121, 108, 119, 115 };
    	SecretKey key=Security.createSecretKey("AES", keyWord);
    	//@SuppressWarnings("unused")
		//String keyStr=Security.parseByte2HexStr(key.getEncoded());
    	return Security.encrypt2String(content, "AES/CBC/PKCS5Padding", key,iv);
    }
    
    /**
     * 使用AES算法加密字符串
     * @param content要加密的字符串
     * @param key 加密使用的key
     * @return
     */
    public static String encryptAES(String content,SecretKey key){
    	byte[] iv={ 109, 121, 109, 98, 53, 50, 104, 97, 110, 103, 97, 114, 121, 108, 119, 115 };
    	return Security.encrypt2String(content, "AES/CBC/PKCS5Padding", key,iv);
    }
    
	/**
	 * 使用DES算法加密字符串
	 * @param content 要加密的字符串
	 * @return 加密后的字符串
	 */
	public  final static String decryptDES(String content,String keyWord){
		SecretKey key=Security.createSecretKey("DES", keyWord);
    	return Security.decrypt2String(content, "DES", key);
	}
    
	/**
	 * 使用AES算法加密字符串
	 * @param content 要加密的字符串
	 * @return 加密后的字符串
	 */
    public static String decryptAES(String content,String keyWord){
    	byte[] iv={ 109, 121, 109, 98, 53, 50, 104, 97, 110, 103, 97, 114, 121, 108, 119, 115 };
    	SecretKey key=Security.createSecretKey("AES", keyWord);
    	return Security.decrypt2String(content, "AES/CBC/PKCS5Padding", key,iv);
    }
    
	/**
	 * 使用AES算法解密字符串
	 * @param content 要解密的字符串
	 * @param key
	 * @return 解密后的字符串
	 */
    public static String decryptAES(String content,SecretKey key){
    	byte[] iv={ 109, 121, 109, 98, 53, 50, 104, 97, 110, 103, 97, 114, 121, 108, 119, 115 };
    	return Security.decrypt2String(content, "AES/CBC/PKCS5Padding", key,iv);
    }
    
	/***
	 * 加密的时候要使用的密钥，这个方法就是生成一个密钥并序列化保存在文件中
	 * @param keyFilePath 密钥文件的路径
	 * @param algorithm 加密算法(AES,DES,DESede)
	 * @return 生成是否成功
	 */
    @SuppressWarnings("unused")
	private static Boolean createSecretKeyFile(String algorithm,String keyFilePath) {
        try {
            // 得到密钥的实例 以什么方式加密。
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            int keySize=128;
            if(algorithm.equalsIgnoreCase("DES")){
            	keySize=56;
            }
            else if(algorithm.equalsIgnoreCase("DESede")){
            	keySize=168;
            }
            kg.init(keySize);
            SecretKey key = kg.generateKey();
            // 将生成的密钥对象写入文件。
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(new File(keyFilePath)));
            objectOutputStream.writeObject(key);
            return true;
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 生成加密的时候要使用的密钥
     * @param algorithm 加密算法(AES,DES,DESede)
     * @return SecretKey
     */
    @SuppressWarnings("unused")
	private static SecretKey createSecretKey(String algorithm) {
        try {
            // 得到密钥的实例 以什么方式加密。
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            int keySize=128;
            if(algorithm.equalsIgnoreCase("DES")){
            	keySize=56;
            }
            else if(algorithm.equalsIgnoreCase("DESede")){
            	keySize=168;
            }
            kg.init(keySize);
            SecretKey key = kg.generateKey();
            return key;
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 以输入的字符串为种子生成加密的时候要使用的密
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param keyString 种子字符串，可以当作密码
     * @return
     */
    private static SecretKey createSecretKey(String algorithm,String keyString) {
    	try {
    		// 得到密钥的实例 以什么方式加密。
			KeyGenerator kg = KeyGenerator.getInstance(algorithm);
			SecureRandom sr = new SecureRandom(keyString.getBytes());
			int keySize = 128;
			if (algorithm.equalsIgnoreCase("DES")) {
				keySize = 56;
			} else if (algorithm.equalsIgnoreCase("DESede")) {
				keySize = 168;
			}
			kg.init(keySize,sr);
			SecretKey key = kg.generateKey();
			//System.out.println(key.getEncoded());
			return key;
    	} catch (NoSuchAlgorithmException e) {
    		//e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * 生成AES加密时用的key，主要是为了和c#端生成的key一样
     * @param keyString
     * @return
     */
    public static SecretKey generateAesKey(String keyString) {
    	try {
    		// 得到密钥的实例 以什么方式加密。
    		keyString=Security.getMD5(keyString);
    		byte[] pwdBytes = keyString.getBytes("utf-8");
    		byte[] keyBytes = new byte[16];
            for(int i=0;i<16;i++){
            	keyBytes[i]=pwdBytes[i];
            }
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
			return key;
    	}
    	catch(UnsupportedEncodingException e){
    		//e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * 用指定的key和加密算法加密content字符串
     * @param content
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key
     * @return 加密后的字节数组
     */
    public static byte[] encrypt(String content,String algorithm,Key key){
    	return Security.encrypt(content, algorithm, key,null);
    }
    /**
     * 用指定的key和加密算法加密content字符串
     * @param content
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key
     * @param iv 初始化向量
     * @return 加密后的字节数组
     */
    public static byte[] encrypt(String content,String algorithm,Key key,byte[] iv){
    	try {
			return Security.encrypt(content.getBytes("UTF-8"), algorithm, key,iv);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * 用指定的key将明文加密成密文
     * @param content 内容
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key 密钥
     * @return 加密后的字符串
     */
    public static String encrypt2String(String content,String algorithm,Key key){
    	return Security.encrypt2String(content, algorithm, key, null);
    }
    
    /**
     * 用指定的key将明文加密成密文
     * @param content 内容
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key 密钥
     * @param iv 初始化向量
     * @return 加密后的字符串
     */
    public static String encrypt2String(String content,String algorithm,Key key,byte[] iv){
    	try {
			byte[] byteContent= Security.encrypt(content.getBytes("UTF-8"), algorithm, key,iv);
			return Security.parseByte2HexStr(byteContent);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * 用指定的key和加密算法加密byteContent字节数组
     * @param byteContent 原字节数组
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key 密钥
     * @return
     */
    public static byte[] encrypt(byte[] byteContent,String algorithm,Key key){
    	return Security.encrypt(byteContent, algorithm, key,null);
    }
    /**
     * 用指定的key和加密算法加密byteContent字节数组
     * @param byteContent 原字节数组
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key 密钥
     * @param iv 初始化向量
     * @return
     */
    public static byte[] encrypt(byte[] byteContent,String algorithm,Key key,byte[] iv){
    	byte[] result;
    	try {
			Cipher cipher = Cipher.getInstance(algorithm);
			if(iv!=null&&iv.length>0){
				IvParameterSpec ivs = new IvParameterSpec(iv);
				cipher.init(Cipher.ENCRYPT_MODE,key,ivs);// 初始化
			}
			else{
				cipher.init(Cipher.ENCRYPT_MODE,key);// 初始化
			}
			result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result=null;
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result=null;
		}
    	catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result=null;
		}catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result=null;
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result=null;
		}catch(Exception e){
    		e.printStackTrace();
			result=null;
    	}
    	// 创建密码器
    	return result;
    }
    
    /**
     * 将16进制字符串表示的密文解码成明文字节数组
     * @param hexStr 密文字符串，16进制字符
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key 密钥
     * @return 明文字节数组
     */
    public static byte[] decrypt(String hexStr,String algorithm,Key key){
    	byte[] byteContent=Security.parseHexStr2Byte(hexStr);
    	return Security.decrypt(byteContent, algorithm, key);
    }
    
    /**
     * 将16进制字符串表示的密文解码成明文
     * @param hexStr 密文字符串，16进制字符
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key 密钥
     * @return 明文
     */
    public static String decrypt2String(String hexStr,String algorithm,Key key){
    	return Security.decrypt2String(hexStr, algorithm, key, null);
    }
    
    /**
     * 将16进制字符串表示的密文解码成明文
     * @param hexStr 密文字符串，16进制字符
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key 密钥
     * @param iv 初始化向量
     * @return 明文
     */
    public static String decrypt2String(String hexStr,String algorithm,Key key,byte[] iv){
    	byte[] byteContent=Security.parseHexStr2Byte(hexStr);
    	byte[] content= Security.decrypt(byteContent, algorithm, key,iv);
    	if(content==null){
    		return null;
    	}
    	try {
			return new String(content,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * 用指定的key和解密算法对密文进行解密
     * @param byteCipherContent 密文字节数组
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key 密钥
     * @return 解密后的字节数组
     */
    public static byte[] decrypt(byte[] byteCipherContent,String algorithm,Key key){
    	return Security.decrypt(byteCipherContent, algorithm, key, null);
    }
    
    /**
     * 用指定的key和解密算法对密文进行解密
     * @param byteCipherContent 密文字节数组
     * @param algorithm 加密算法(AES,DES,DESede)
     * @param key 密钥
     * @param iv 初始化向量
     * @return 解密后的字节数组
     */
    public static byte[] decrypt(byte[] byteCipherContent,String algorithm,Key key,byte[] iv){
    	byte[] result;
    	try {
			Cipher cipher = Cipher.getInstance(algorithm);
			if(iv!=null&&iv.length>0){
				IvParameterSpec ivs = new IvParameterSpec(iv);
				cipher.init(Cipher.DECRYPT_MODE,key,ivs);// 初始化
			}
			else{
				cipher.init(Cipher.DECRYPT_MODE,key);// 初始化
			}
			result = cipher.doFinal(byteCipherContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result=null;
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result=null;
		}
    	catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result=null;
		}catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result=null;
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			result=null;
		}catch(Exception e){
    		e.printStackTrace();
			result=null;
    	}
    	// 创建密码器
    	return result;
    }
    
    //RSA加密算法
    /**
     * 生成公钥和私钥，使用1024bits长度密钥
     * @throws NoSuchAlgorithmException
     *
     */
    public static HashMap<String, Object> getRSAKeys() throws NoSuchAlgorithmException{
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
    }
    
    //RSA加密算法
    //1. 随意选择两个大的质数p和q，p不等于q，计算N=pq。
    //2. 根据欧拉函数，不大于N且与N互质的整数個数為(p-1)(q-1)。
    //3. 选择一个整数e与(p-1)(q-1)互质，并且e小于(p-1)(q-1)。
    //4. 用以下这个公式计算d：(d*e)mod((p-1)*(q-1))=1。
    //5. 将p和q的记录销毁。
    //6. A=B^e mod n；B=A^d mod n 
    // 以上内容中，(N,e)是公钥，(N,d)是私钥。
    /**
     * 生成公钥和私钥,RSA的密钥长度根据加密强度不同进行选择，一般在96-1024bits
     * @throws NoSuchAlgorithmException
     *
     */
    public static HashMap<String, Object> getRSAKeys(int keyLength) throws NoSuchAlgorithmException,Exception{
    	if(keyLength<96||keyLength>1024) {
    		throw(new Exception("密钥长度不正确！"));
    	}
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(keyLength);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
    }
    
    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus
     *            模
     * @param exponent
     *            指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus
     *            模
     * @param exponent
     *            指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 密钥长度
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 密钥长度-11
        List<String> datas = splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于密钥长度-11则要分组加密
        //为了计算数组总长度，先加密最后一组。
        byte[] tmp=cipher.doFinal(datas.get(datas.size()-1).getBytes());
        
        int l=(datas.size()-1)*128+tmp.length;
        byte[] result=new byte[l];
        byte[] tmp2;
        int i=0;
        for (i=0;i<datas.size()-1;i++) {
        	tmp2=cipher.doFinal(datas.get(i).getBytes());
        	System.arraycopy(tmp2,0,result,i*128,tmp2.length);
        	/*
        	if(result==null||result.length<1) {
        		result=tmp;
        	}
        	else {
        		tmp2=new byte[result.length+tmp.length];
        		System.arraycopy(result,0,tmp2,0,result.length);
        		System.arraycopy(tmp,0,tmp2,result.length,tmp.length);
        		result=tmp2;
        	}
        	*/
        }
        System.arraycopy(tmp,0,result,i*128,tmp.length);
        return Base64.encodeBase64String(result);
    }
    
    /**
     * 私钥加密(签名)
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        // 密钥长度
        int key_len = privateKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 密钥长度-11
        List<String> datas = splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于密钥长度-11则要分组加密
        //为了计算数组总长度，先加密最后一组。
        byte[] tmp=cipher.doFinal(datas.get(datas.size()-1).getBytes());
        
        int l=(datas.size()-1)*128+tmp.length;
        byte[] result=new byte[l];
        byte[] tmp2;
        int i=0;
        for (i=0;i<datas.size()-1;i++) {
        	tmp2=cipher.doFinal(datas.get(i).getBytes());
        	System.arraycopy(tmp2,0,result,i*128,tmp2.length);
        	/*
        	if(result==null||result.length<1) {
        		result=tmp;
        	}
        	else {
        		tmp2=new byte[result.length+tmp.length];
        		System.arraycopy(result,0,tmp2,0,result.length);
        		System.arraycopy(tmp,0,tmp2,result.length,tmp.length);
        		result=tmp2;
        	}
        	*/
        }
        System.arraycopy(tmp,0,result,i*128,tmp.length);
        return Base64.encodeBase64String(result);
    }
 
    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //密钥长度
        int key_len = privateKey.getModulus().bitLength() / 8;
        //byte[] bytes = parseHexStr2Byte(data);
        byte[] bytes=Base64.decodeBase64(data);
        //如果密文长度大于密钥长度则要分组解密
        String result = "";
        List<byte[]> arrays = splitArray(bytes, key_len);
        for(byte[] arr : arrays){
        	result += new String(cipher.doFinal(arr),"UTF-8");
        }
        return result;
    }
    
    /**
     * 公钥解密(验证签名)
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        //密钥长度
        int key_len = publicKey.getModulus().bitLength() / 8;
        //byte[] bytes = parseHexStr2Byte(data);
        byte[] bytes=Base64.decodeBase64(data);
        //如果密文长度大于密钥长度则要分组解密
        String result = "";
        List<byte[]> arrays = splitArray(bytes, key_len);
        for(byte[] arr : arrays){
        	result += new String(cipher.doFinal(arr),"UTF-8");
        }
        return result;
    }


    
  //RSA加密算法结束
    
    /**
     * 将字节转换成16进制数字符串
     * @param buf
     * @return 转换后的字符串
     */
	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制字符串转换成数组
	 * @param hexStr
	 * @return 字节数组
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
    /**
     * 将字符串拆分成数组
     */
    public static List<String> splitString(String string, int len) {
    	List<String> listStr=new ArrayList<String>();
    	int strLen=string.length();
    	String tmp = "";
    	for(int i=0;i<strLen;) {
    		if(i+len>=strLen) {
    			tmp=string.substring(i);
    		}
    		else {
    			tmp=string.substring(i, i+len);
    		}
    		i+=len;
    		listStr.add(tmp);
    	}
    	return listStr;
    }
    
    /**
     *将数组拆分成二维数组
     */
    public static List<byte[]> splitArray(byte[] data,int len){
    	List<byte[]> listArray=new ArrayList<byte[]>();
    	int strLen=data.length;
    	byte[] tmp;
    	for(int i=0;i<strLen;) {
    		if(i+len>=strLen) {
    			tmp = new byte[strLen-i];
    			System.arraycopy(data, i, tmp, 0, strLen-i);
    		}
    		else {
    			tmp = new byte[len];
    			System.arraycopy(data, i, tmp, 0, len);
    		}
    		i+=len;
    		listArray.add(tmp);
    	}
    	return listArray;
    }
    
  


	public static void main(String[] args) {
	    /*
	    String ids="";
	    String[] aa=StringUtils.delimitedListToStringArray(ids, ";");
	    int index=ids.indexOf(";");
	    int n=0;
	    List<String> list=new ArrayList<>();
	        while(index>=0&&ids.length()>=n) {
	            System.out.println(ids.substring(n,index));
	            list.add(ids.substring(n,index));
	            n=index+1;
	            index=ids.indexOf(";",n);
	            if(index<0&&ids.length()>n) {
	        	index=ids.length();
	            }
	        }
	        
	        System.out.println("****");
	        String[] result=new String[list.size()];
	        System.out.println(list.toArray(result));
	        */
		//"367770a9b2658c77d8741cdac49d0fde"//hangar_mymb52 MD5
				//System.out.println(Security.getMD5(""));
				//System.out.println(Security.getSHA1("mymb52@netcomm95598"));
				//String aa="<p>中国aaK\r\nszdfas</P>";
				//System.out.println(getRC4(getRC4("123456","asdf"),"sss"));
				//System.out.print(aa.replaceAll("(?is:^<p>|</p>$)",""));
				//System.out.println(new String(Base64.encodeBase64(result.getBytes("ISO8859-1"))));
				//@SuppressWarnings("unused")
				//String str="中国人";//热线没有内容\r\n用电业务申请页有一个申请所需资料链接，没有内容\r\n\r\n后台可以直接进index页，没有登录验证\r\n用户管理不能修改个人信息\r\n修改用户信息提示借误\r\n-----------------------------7daab19e0b48\r\nContent-Disposition: form-data; .core/tmp0/wtpwebapps/webadmin/\r\n-----------------------------7daab19e0b48\r\nContent-Disposition: form-data;n-----------------------------7daab19e0b48--\r\n";
				//int len=str.indexOf("-----------------------------7daab19e0b48");
				//String encryptStr=Security.encryptAES(str,Security.generateAesKey("hangar"));
				//System.out.println(encryptStr);
				//encryptStr="610BB807838E656C1A393CB356222529";
				//str=Security.decryptAES(encryptStr,Security.generateAesKey("hangar"));
				//System.out.println(str);
				//
				//--31257D0D49176ABB08D5E77F2D16CD54
				//--6D796D62353268616E676172796C7773
//				str=Security.encryptAES("lianqinbu", WebGlobal.KEY_STR);
//				System.out.println(str);
				//System.out.println(Security.getSHA1("user@123456"));
				//str=Security.encryptAES("123456", WebGlobal.KEY_STR);
				//System.out.println(str);
		/*
		//RSA测试
		HashMap<String, Object> map=new HashMap<String,Object>();
		try {
			map = Security.getRSAKeys();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //生成公钥和私钥
        RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
        RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
 
        //模
        String modulus = publicKey.getModulus().toString();
        //公钥指数
        String public_exponent = publicKey.getPublicExponent().toString();
        //私钥指数
        String private_exponent = privateKey.getPrivateExponent().toString();
        //明文
        String ming = "RSA的安全性依赖于大数分解，但是否等同于大数分解一直未能得到理论上的证明，因为没有证明破解RSA就一定需要作大数分解。假设存在一种无须分解大数的算法，那它肯定可以修改成为大数分解算法。 RSA 的一些变种算法已被证明等价于大数分解。不管怎样，分解n是最显然的攻击方法。人们已能分解多个十进制位的大素数。因此，模数n必须选大一些，因具体适用情况而定。RSA的安全性依赖于大数分解，但是否等同于大数分解一直未能得到理论上的证明，因为没有证明破解RSA就一定需要作大数分解。假设存在一种无须分解大数的算法，那它肯定可以修改成为大数分解算法。 RSA 的一些变种算法已被证明等价于大数分解。不管怎样，分解n是最显然的攻击方法。人们已能分解多个十进制位的大素数。因此，模数n必须选大一些，因具体适用情况而定。RSA的安全性依赖于大数分解，但是否等同于大数分解一直未能得到理论上的证明，因为没有证明破解RSA就一定需要作大数分解。假设存在一种无须分解大数的算法，那它肯定可以修改成为大数分解算法。 RSA 的一些变种算法已被证明等价于大数分解。不管怎样，分解n是最显然的攻击方法。人们已能分解多个十进制位的大素数。因此，模数n必须选大一些，因具体适用情况而定。RSA的安全性依赖于大数分解，但是否等同于大数分解一直未能得到理论上的证明，因为没有证明破解RSA就一定需要作大数分解。假设存在一种无须分解大数的算法，那它肯定可以修改成为大数分解算法。 RSA 的一些变种算法已被证明等价于大数分解。不管怎样，分解n是最显然的攻击方法。人们已能分解多个十进制位的大素数。因此，模数n必须选大一些，因具体适用情况而定。";
        try {
			ming=Base64.encodeBase64String(ming.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println(ming.length());
        //使用模和指数生成公钥和私钥
        RSAPublicKey pubKey = Security.getPublicKey(modulus, public_exponent);
        RSAPrivateKey priKey = Security.getPrivateKey(modulus, private_exponent);
        //加密后的密文
        String mi;
		try {
			mi = Security.encryptByPublicKey(ming, pubKey);
			System.out.println(mi);
			//解密后的明文
	        ming = Security.decryptByPrivateKey(mi, priKey);
	        System.out.println(ming);
	        ming=new String(Base64.decodeBase64(ming.getBytes("UTF-8")));
	        System.out.println(ming);
	        System.out.println(ming.length());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}
