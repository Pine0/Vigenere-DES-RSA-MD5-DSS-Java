package MD5;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5加/解密
 * @author 计科1602 王雪松 2016014307
 * @date 2019-04-25 15:42
 */
public class MD5 {


    /**
     * 转换函数
     * 将text转换为MD5格式
     * @param text 传入字符串
     * @return 返回MD5格式的字符串
     * @throws NoSuchAlgorithmException 抛出未找到算法的异常
     */
    private static String toMD5(String text) throws NoSuchAlgorithmException{
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        byte[] byteArray = text.getBytes();

        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte b : md5Bytes){
            int val = ((int) b) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


    /**
     * 加密函数
     *
     * @param input  输入文件路径
     * @param output 输出文件路径
     * @param isEncrypt 是否加密
     * @throws IOException 抛出IO异常
     * @throws NoSuchAlgorithmException 抛出找不到算法异常
     */
    private static void encrypt(String input, String output, boolean isEncrypt) throws IOException, NoSuchAlgorithmException {
        try (
                BufferedReader br = new BufferedReader(new FileReader(input));
                BufferedWriter bw = new BufferedWriter(new FileWriter(output));
        ) {
            String text = br.readLine();
            if (isEncrypt) {
                System.out.println("MD5 : " + toMD5(text));
            }
            char[] arr = text.toCharArray();
            for (int i = 0; i < arr.length; i++){
                arr[i] = (char) (arr[i] ^ 't');
            }

            bw.write(String.valueOf(arr));
        }
    }


    /**
     * 解密函数
     *
     * @param input  输入文件路径
     * @param output 输出文件路径
     * @throws Exception 抛出异常
     */
    private static void decrypt(String input, String output) throws Exception {
        //再次调一边加密函数即可解密
        encrypt(input, output, false);
    }


    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        //文件输入输出路径
        String input = "./src/MD5/input.txt";
        String output = "./src/MD5/output.txt";

        //加密
        encrypt(input, output, true);

        //输出算法运行时间
        System.out.println("加/解密结束，用时：" + (System.currentTimeMillis() - startTime) + " ms。");
    }

}
