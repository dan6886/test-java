public class Secret {
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        String text = "丑";
        execute(text);
        System.out.println(System.currentTimeMillis());

    }


    public static void execute(String password) {
        //讲获取的字符串转成字符数组
        char[] c = password.toCharArray();
        //使用for循环给字符数组加密
        for (int i = 0; i < c.length; i++) {
            c[i] = (char) (c[i] ^ 20000);
        }
        //输出加密或者解密结果
        System.out.println("加密或者解密之后的结果如下:");
        System.out.println(new String(c));
    }
}
