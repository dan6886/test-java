import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EncrypDES {

    /**
     * 默认构造方法，使用默认密钥
     */

    public EncrypDES() throws Exception {
        this(strDefaultKey);
    }

    /**
     * 指定密钥构造方法
     *
     * @param strKey 指定的密钥
     * @throws Exception
     */

    public EncrypDES(String strKey) throws Exception {
        // Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * 字符串默认键值
     */

    private static String strDefaultKey = "inventec2017@#$%^&";
//    private static String strDefaultKey = "danlovecx9999&^%$#";

    /**
     * 加密工具
     */

    private Cipher encryptCipher = null;

    /**
     * 解密工具
     */

    private Cipher decryptCipher = null;

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813，和public static byte[]
     * <p>
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */

    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用2个字符才能表示，所以字符串的长度是数组长度的2倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }

            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组，和public static String byteArr2HexStr(byte[] arrB)
     * <p>
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     */

    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     */

    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     *
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     */

    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     */

    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     *
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     */

    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     */

    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        args = new String[]{"-mode", "encode", "-str", "阿斯兰的卡三等奖"};
        System.out.println(Arrays.toString(args));
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            map.put(args[i], args[i + 1]);
        }

        String mode = map.getOrDefault("-mode", "null");
        String str = map.getOrDefault("-str", "");

        str = new String(str.getBytes("utf-8"), "gbk");
        System.out.println(str);
        if (mode.equals("encode")) {
            encode(str);
            return;
        } else if (mode.equals("decode")) {
            decode(str);
            return;
        }

        System.out.println("debug");

        try {
            // 加密
            String msg1 = "dan116307";
            EncrypDES des1 = new EncrypDES();// 使用默认密钥
//            System.out.println("加密前的字符：" + msg1);
            String encrypt = des1.encrypt(msg1);
            System.out.println("加密后的字符：" + encrypt);
            System.out.println("=====================================================");
            //解密
            String msg2 = "9299dc24e7e8c3beff3d05bcec45f56866489904fa298e3fcb0674c1e3b05d2dea8464e294502c3ce8036a87a47c7542c99732e90468e53bcbe0e5042904c4d4f5c3b390beb0736eddec85cc17ca2eeee9aa1035df149d264a41df7f8a63a1437a5a7a1f97b5a5e7f3c62e244929d5e5834cac844dfcf39933529029157c0aea94e909f3df8d0a3f56ae3cbee667d0e2b1dd2cceaed24450bf1da4309650177f0eac8db22af33f483ed251c812facfe4cadc4d7c82d5a963fce08da3291cf5e3321c5e5d9317f4f8";
            System.out.println("解密前的字符：" + msg2);
            String decrypt = des1.decrypt(msg2);
            System.out.println("解密后的字符：" + decrypt);
            System.out.println("=====================================================");
            decrypt = decrypt.replace("PrintParam:", "");
            String[] split = decrypt.split("&");

            for (int i = 0; i < split.length; i++) {
                System.out.println("");
                String decode = URLDecoder.decode(split[i]);
                System.out.println(split[i]);
                System.out.println(decode);
            }


            String msg3 = "appid=&userid=28ede01431cd&text=%E4%BB%8A%E5%A4%A9%E5%A4%A9%E6%B0%94%E6%80%8E%E4%B9%88%E6%A0%B7&cmd=chat&location=%E6%B7%B1%E5%9C%B3%E5%B8%82";

/*          String key = "inventec2017@#$%^&";

            EncrypDES des2 = new EncrypDES(key);// 自定义密钥

            System.out.println("加密前的字符：" + msg2);

            System.out.println("加密后的字符：" + des2.encrypt(msg2));

            System.out.println("解密后的字符：" + des2.decrypt(des2.encrypt(msg2)));*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String decode(String str) {
        /* 使用默认密钥 */
        try {
            EncrypDES des1 = new EncrypDES();
            String decrypt = des1.decrypt(str);
            System.out.println(decrypt);
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encode(String str) {
        // 使用默认密钥
        try {
            EncrypDES des1 = new EncrypDES();
            String encrypt = des1.encrypt(str);
            System.out.println(encrypt);
            return encrypt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}