package DES;

import java.io.*;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;


/**
 * DES加/解密类
 * @author 计科1602 王雪松 2016014307
 * @date 2019-04-25 00:58
 */
public class DES {


    /**
     * getDES函数
     * @param text 传入需要加/解密的字节数组
     * @param key 传入密钥
     * @param mode 是否为加密
     * @throws Exception 抛出异常
     * @return byte[] 返回加/解密的字节数组
     */
    private static byte[] getDES(byte[] text, String key, boolean mode) throws Exception{

        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();

        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(key.getBytes());

        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");

        // 用密匙初始化Cipher对象
        if (mode) {
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            return cipher.doFinal(text);
        }
        else {
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            return cipher.doFinal(Base64.decodeBase64(text));
        }

    }


    /**
     * DES加密函数
     * @param input 输入文件路径
     * @param output 输出文件路径
     * @param key 密钥
     * @throws Exception 抛出异常
     */
    private static void encrypt(String input, String output, String key) throws Exception {
        try (
                BufferedReader br = new BufferedReader(new FileReader(input));
                BufferedWriter bw = new BufferedWriter(new FileWriter(output));
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                byte[] text = line.getBytes();

                bw.write(Base64.encodeBase64String(getDES(text, key, true)));
                bw.newLine();
            }
        }
    }


    /**
     * DES解密函数
     * @param input 输入文件路径
     * @param output 输出文件路径
     * @param key 密钥
     * @throws Exception 抛出异常
     */
    private static void decrypt(String input, String output, String key) throws Exception {
        try (
                BufferedReader br = new BufferedReader(new FileReader(input));
                BufferedWriter bw = new BufferedWriter(new FileWriter(output));
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                byte[] text = line.getBytes();
                bw.write(new String(getDES(text, key, false)));
                bw.newLine();
            }
        }
    }

    public static void main (String[]args) throws Exception {
        long startTime = System.currentTimeMillis();

        //输入输出路径
        String input = "./src/DES/input.txt";
        String output = "./src/DES/output.txt";

        //密钥
        String key = "A1B2C3D4E5F60708";

        //调用加密函数
        encrypt(input, output, key);

        //计算该算法运行时间
        System.out.println("加/解密结束，用时：" + (System.currentTimeMillis() - startTime) + " ms。");
    }

}