package com.zx.utils;

import java.util.Random;

/**
 * 随机生成指定大小的字符串
 *
 * @return 生成字符串
 */
public class RandomStringUtils {

    public String generateRandomString(int maxLength) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxLength; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }


    public String generateRandomNumberString(int maxLength) {
        String characters = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxLength; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        RandomStringUtils utils = new RandomStringUtils();
        System.out.println(utils.generateRandomNumberString(6));
    }
}
