package DSS;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;

/**
 * DSS加/解密类
 * @author 计科1602 王雪松 2016014307
 * @date 2019-04-25 18:02
 */
public class DSS {


    /**
     * DSA风格的公钥和私钥
     */
    private static DSAPublicKey publicKey = null;
    private static DSAPrivateKey privateKey = null;


    /**
     * 获取随机公钥、私钥函数
     * @param privateKeyPath 密钥输入文件
     * @param publicKeyPath  公钥输入文件
     * @throws IOException 抛出IO异常
     * @throws NoSuchAlgorithmException 抛出未找到算法的异常
     */
    private static void genKeyPair(String publicKeyPath, String privateKeyPath) throws IOException, NoSuchAlgorithmException {

        try (
                BufferedWriter bw1 = new BufferedWriter(new FileWriter(publicKeyPath));
                BufferedWriter bw2 = new BufferedWriter(new FileWriter(privateKeyPath));
        ) {
            //创建秘钥生成器
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
            kpg.initialize(512);

            //生成秘钥对
            KeyPair keypair = kpg.generateKeyPair();
            DSS.publicKey = (DSAPublicKey)keypair.getPublic();
            DSS.privateKey = (DSAPrivateKey)keypair.getPrivate();

            //将私钥和公钥存入文件中
            bw1.write(new String(Base64.encodeBase64(publicKey.getEncoded())));
            bw2.write(new String(Base64.encodeBase64((privateKey.getEncoded()))));
        }


    }


    /**
     * 签名函数
     * @param input 输入文件路径
     * @param output 输出文件路径
     * @throws Exception 抛出异常
     */
    private static void sign(String input, String output) throws Exception {
        try (
                BufferedReader in = new BufferedReader(new FileReader(input));
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(output))
        ) {
            String text;
            while ((text = in.readLine()) != null) {
                Signature sign = Signature.getInstance("SHA1withDSA");

                //初始化私钥，签名只能是私钥
                sign.initSign(DSS.privateKey);

                //更新签名数据
                sign.update(text.getBytes());

                //签名，返回签名后的字节数组
                byte[] bytes = sign.sign();
                out.write(bytes);
            }
        }
    }

    /**
     * 验证函数
     * @param input 输入文件路径
     * @param output 输出文件路径
     * @throws Exception 抛出异常
     */

    private static boolean validate(String input, String output) throws Exception {
        try (
                BufferedReader in = new BufferedReader(new FileReader(input));
                BufferedInputStream out = new BufferedInputStream(new FileInputStream(output))
        ) {
            String plainText;
            plainText = in.readLine();
            byte[] cipherText = new byte[out.available()];

            out.read(cipherText);
            Signature sign = Signature.getInstance("SHA1withDSA");

            //初始化公钥，验证只能是公钥
            sign.initVerify(DSS.publicKey);

            //更新验证的数据
            sign.update(plainText.getBytes());
            return sign.verify(cipherText);
            }
        }



    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        //输入 输出 公钥 私钥文件路径
        String input = "./src/DSS/input.txt";
        String output = "./src/DSS/output.txt";
        String privateKeyPath = "./src/DSS/Private_Key.txt";
        String publicKeyPath = "./src/DSS/Public_Key.txt";

        //获得公钥和私钥
        genKeyPair(publicKeyPath, privateKeyPath);

        //签名 将签名后的结果写入output路径
        sign(input, output);

        //在控制台输出验证结果
        System.out.println(validate(input, output));

        //计算该算法执行时间
        System.out.println("加/解密结束，用时：" + (System.currentTimeMillis() - startTime) + " ms。");
    }

}
