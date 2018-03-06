package com.mytool.m3u8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.http.entity.FileEntity;

import android.util.Log;

public class EncyptFileEntity extends FileEntity {

	public EncyptFileEntity(File file, String contentType) {
		super(file, contentType);
		// TODO Auto-generated constructor stub
	}

	public void writeTo(OutputStream outstream) throws IOException {
		// TODO Auto-generated method stub
		//super.writeTo(outstream);
		// TODO Auto-generated method stub
				//super.writeTo(outstream);
				//解密过程
		        final InputStream instream = new FileInputStream(this.file);
		        try {
		        	int OUTPUT_BUFFER_SIZE = 4096;
		             byte[] tmp = new byte[OUTPUT_BUFFER_SIZE];
		            int l;
		            boolean flag=true;
		            while ((l = instream.read(tmp)) != -1) {
		            	try {
		            		if(flag && file.getPath().contains(".cbox")){
		            		//flag=false;
		            		for (int i = 0; i < tmp.length; i++) {
								tmp[i]=(byte) (tmp[i]^100); //字节和100 异或 解密
							}
		            		}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                outstream.write(tmp, 0, l);
		            }
		            outstream.flush();
		        } finally {
		            instream.close();
		        }
        
	}
	
	/**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
	
	/**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
 
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
 
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
 
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
 
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
 
        return cipher.doFinal(data);
    }


}
