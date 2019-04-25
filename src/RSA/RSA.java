package RSA;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * RSA加/解密
 * @author 计科1602 王雪松 2016014307
 * @date 2019-04-25 14:24
 */
public class RSA {


    /**
     * 获取随机公钥、私钥函数
     * @param privatekey 密钥输入文件
     * @param publickey  公钥输入文件
     * @throws IOException 抛出IO异常
     * @throws NoSuchAlgorithmException 抛出未找到算法的异常
     */
    private static void genKeyPair(String privatekey, String publickey) throws IOException, NoSuchAlgorithmException {

        try (
                BufferedWriter bw1 = new BufferedWriter(new FileWriter(privatekey));
                BufferedWriter bw2 = new BufferedWriter(new FileWriter(publickey));
        ) {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(1024,new SecureRandom());

            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            //将私钥和公钥存入文件中
            bw1.write(new String(Base64.encodeBase64((privateKey.getEncoded()))));
            bw2.write(new String(Base64.encodeBase64(publicKey.getEncoded())));
        }


    }


    /**
     * 加密函数
     * @param input 输入文件路径
     * @param output 输出文件路径
     * @param publickey 公钥输入文件路径
     * @throws Exception 抛出异常
     */
    private static void encrypt(String input, String output, String publickey) throws Exception {
        try (
                BufferedReader in = new BufferedReader(new FileReader(input));
                BufferedReader key = new BufferedReader(new FileReader(publickey));
                BufferedWriter out = new BufferedWriter(new FileWriter(output));
        ) {

            //base64编码的公钥
            byte[] decoded = Base64.decodeBase64(key.readLine());
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));

            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            String outStr = Base64.encodeBase64String(cipher.doFinal(in.readLine().getBytes()));
            out.write(outStr);
        }
    }


    /**
     * 解密函数
     * @param input 输入文件路径
     * @param output 输出文件路径
     * @param privatekey 密钥输入文件路径
     * @throws Exception 抛出异常
     */
    private static void decrypt(String input, String output, String privatekey) throws Exception {
        try (
                BufferedReader in = new BufferedReader(new FileReader(input));
                BufferedReader key = new BufferedReader(new FileReader(privatekey));
                BufferedWriter out = new BufferedWriter(new FileWriter(output));
        ) {
            byte[] inputByte = Base64.decodeBase64(in.readLine().getBytes());

            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(key.readLine());
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));

            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            String outStr = new String(cipher.doFinal(inputByte));
            out.write(outStr);
        }
    }


    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        //input 输入文件路径
        String input = "./src/RSA/input.txt";

        //output 输出文件路径
        String output = "./src/RSA/output.txt";

        //publickey 公钥输入文件路径 在文件中读取公钥
        String publickey = "./src/RSA/Public_Key.txt";

        //privatekey 私钥输入文件路径 在文件中读取私钥
        String privatekey = "./src/RSA/Private_Key.txt";

        //生成随机公钥和私钥 如果文件中有现成的公钥和私钥 就将这行注释掉
        genKeyPair(privatekey, publickey);

        //加密函数调用
        encrypt(input, output, publickey);

        //解密函数调用
        decrypt(output, input, privatekey);

        //计算算法运行时间
        System.out.println("加/解密结束，用时：" + (System.currentTimeMillis() - startTime) + " ms。");
    }


}
