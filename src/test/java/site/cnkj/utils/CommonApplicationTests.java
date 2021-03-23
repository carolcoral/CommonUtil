package site.cnkj.utils;

import com.mongodb.*;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import site.cnkj.common.utils.io.DES;
import site.cnkj.common.utils.io.RSAEncrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommonApplicationTests {

	private static final String DEFAULT_PUBLIC_KEY=
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChDzcjw/rWgFwnxunbKp7/4e8w" + "\r" +
					"/UmXx2jk6qEEn69t6N2R1i/LmcyDT1xr/T2AHGOiXNQ5V8W4iCaaeNawi7aJaRht" + "\r" +
					"Vx1uOH/2U378fscEESEG8XDqll0GCfB1/TjKI2aitVSzXOtRs8kYgGU78f7VmDNg" + "\r" +
					"XIlk3gdhnzh+uoEQywIDAQAB" + "\r";

	private static final String DEFAULT_PRIVATE_KEY=
			"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKEPNyPD+taAXCfG" + "\r" +
					"6dsqnv/h7zD9SZfHaOTqoQSfr23o3ZHWL8uZzINPXGv9PYAcY6Jc1DlXxbiIJpp4" + "\r" +
					"1rCLtolpGG1XHW44f/ZTfvx+xwQRIQbxcOqWXQYJ8HX9OMojZqK1VLNc61GzyRiA" + "\r" +
					"ZTvx/tWYM2BciWTeB2GfOH66gRDLAgMBAAECgYBp4qTvoJKynuT3SbDJY/XwaEtm" + "\r" +
					"u768SF9P0GlXrtwYuDWjAVue0VhBI9WxMWZTaVafkcP8hxX4QZqPh84td0zjcq3j" + "\r" +
					"DLOegAFJkIorGzq5FyK7ydBoU1TLjFV459c8dTZMTu+LgsOTD11/V/Jr4NJxIudo" + "\r" +
					"MBQ3c4cHmOoYv4uzkQJBANR+7Fc3e6oZgqTOesqPSPqljbsdF9E4x4eDFuOecCkJ" + "\r" +
					"DvVLOOoAzvtHfAiUp+H3fk4hXRpALiNBEHiIdhIuX2UCQQDCCHiPHFd4gC58yyCM" + "\r" +
					"6Leqkmoa+6YpfRb3oxykLBXcWx7DtbX+ayKy5OQmnkEG+MW8XB8wAdiUl0/tb6cQ" + "\r" +
					"FaRvAkBhvP94Hk0DMDinFVHlWYJ3xy4pongSA8vCyMj+aSGtvjzjFnZXK4gIjBjA" + "\r" +
					"2Z9ekDfIOBBawqp2DLdGuX2VXz8BAkByMuIh+KBSv76cnEDwLhfLQJlKgEnvqTvX" + "\r" +
					"TB0TUw8avlaBAXW34/5sI+NUB1hmbgyTK/T/IFcEPXpBWLGO+e3pAkAGWLpnH0Zh" + "\r" +
					"Fae7oAqkMAd3xCNY6ec180tAe57hZ6kS+SYLKwb4gGzYaCxc22vMtYksXHtUeamo" + "\r" +
					"1NMLzI2ZfUoX" + "\r";


	/**
	 * example for use cn.migu.log.common.utils.io.RSAEncrypt
	 */
	private void test1(){
		RSAEncrypt rsaEncrypt= new RSAEncrypt();
		//rsaEncrypt.genKeyPair();

		//加载公钥
		try {
			rsaEncrypt.loadPublicKey(DEFAULT_PUBLIC_KEY);
			System.out.println("加载公钥成功");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("加载公钥失败");
		}

		//加载私钥
		try {
			rsaEncrypt.loadPrivateKey(DEFAULT_PRIVATE_KEY);
			System.out.println("加载私钥成功");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("加载私钥失败");
		}
		//测试字符串，最大62
		String encryptStr= "11111111111111111111111111111111111111111111111111111111111111";
		try {
			//加密
			byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), encryptStr.getBytes());
			//解密
			byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), cipher);
			System.out.println("密文长度:"+ cipher.length);
			System.out.println(RSAEncrypt.byteArrayToString(cipher));
			System.out.println("明文长度:"+ plainText.length);
			System.out.println(RSAEncrypt.byteArrayToString(plainText));
			System.out.println(new String(plainText));
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/**
	 * example for use cn.migu.log.common.utils.io.DES
	 */
	private void test2(){
		try {
			String encrypt = DES.encode("jbXGHTytBAxFqleSbJ", "460007111667279");
			String decrypt = DES.decode("jbXGHTytBAxFqleSbJ", "4617f68fda750c32cdc3e160764f723f");
			System.out.println(encrypt);
			System.out.println(decrypt);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private static HashMap<String, MongoDatabase> mongoClientDatabases = new HashMap<>();

	private static String getDatabase(String url) {
		String databaseName = "";
		String substring = url.substring(10, url.length());
		String[] split = substring.split("/");
		String database = split[1];
		if (database.contains("?")){
			databaseName = database.split("\\?")[0];
		}else {
			databaseName = database;
		}
		return databaseName;
	}

	private static List<ServerAddress> createServerAddress(String url){
		List<ServerAddress> serverAddresses = new ArrayList<>();
		String substring = url.substring(10, url.length());
		String[] split = substring.split("/");
		String[] hosts = split[0].split(",", -1);
		for (String host : hosts) {
			if (StringUtils.isNotEmpty(host)){
				String[] strings = host.split(":", -1);
				ServerAddress serverAddress = new ServerAddress(strings[0], Integer.valueOf(strings[1]));
				serverAddresses.add(serverAddress);
			}
		}
		return serverAddresses;
	}

	private static void test3(){
		String url = "mongodb://10.25.245.121:17017/log_storage_stable?connectTimeoutMS=50&maxPoolSize=5&waitQueueTimeoutMS=100&socketTimeoutMS=1000&maxIdleTimeMS=60000";
		try {
			//配制连接池
			MongoClientOptions.Builder mongoClientBuilder = new MongoClientOptions.Builder();
			mongoClientBuilder.connectionsPerHost(30);
			mongoClientBuilder.connectTimeout(30000);
			mongoClientBuilder.retryWrites(true);
			//MongoClientOptions mongoClientOptions = mongoClientBuilder.build();

			MongoClient mongoClient = new MongoClient(new MongoClientURI(url, mongoClientBuilder));
			MongoDatabase mongoClientDatabase = mongoClient.getDatabase(getDatabase(url));
			MongoCursor<Document> cursor = mongoClientDatabase.getCollection("a_10000_00062").find().limit(10).iterator();
			while (cursor.hasNext()){
				System.out.println(cursor.next().toJson());
			}
		}catch (MongoClientException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		test3();
	}

}
