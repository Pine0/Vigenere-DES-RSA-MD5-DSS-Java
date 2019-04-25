package Vigenere;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

/**
 * Vigenere加/解密
 * @author 计科1602 王雪松 2016014307
 * @date 2019-04-24 23:46
 */
public class Vigenere {


    /**
     * 加密函数
     * @param input 输入文件路径
     * @param output 输出文件路径
     * @param key 密钥
     * @throws IOException 抛出IO异常
     */
    private static void encrypt(String input, String output, String key) throws IOException {
        try (
                BufferedReader br = new BufferedReader(new FileReader(input));
                BufferedWriter bw = new BufferedWriter(new FileWriter(output));
                ) {
            String text;
            while ((text = br.readLine()) != null) {
                StringBuilder res = new StringBuilder(text.length());
                for (int i = 0; i < text.length(); i++) {
                    int temp1 = text.charAt(i) - ' ';
                    int temp2 = key.charAt(i % key.length()) - ' ';
                    res.append((char) (' ' + (temp1 + temp2) % (127 - ' ')));
                }
                bw.write(res.toString());
                bw.newLine();
            }
        }
    }


    /**
     * 解密函数
     * @param input 输入文件路径
     * @param output 输出文件路径
     * @param key 密钥
     * @throws IOException 抛出IO异常
     */
    private static void decrypt(String input, String output, String key) throws IOException {
        try (
                BufferedReader br = new BufferedReader(new FileReader(input));
                BufferedWriter bw = new BufferedWriter(new FileWriter(output));
        ) {
            String text;
            while ((text = br.readLine()) != null) {
                StringBuilder res = new StringBuilder(text.length());
                for (int i = 0; i < text.length(); i++) {
                    int temp1 = text.charAt(i) - ' ';
                    int temp2 = key.charAt(i % key.length()) - ' ';
                    res.append((char) (' ' + (temp1 - temp2 + 127 - ' ') % (127 - ' ')));
                }
                bw.write(res.toString());
                bw.newLine();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        //输入输出文件路径
        String input = "./src/Vigenere/input.txt";
        String output = "./src/Vigenere/output.txt";

        //密钥
        String key = "computer";

        //加密函数
        encrypt(input, output, key);

        //计算算法时间
        System.out.println("加/解密结束，用时：" + (System.currentTimeMillis() - startTime) + " ms。");
    }
}
