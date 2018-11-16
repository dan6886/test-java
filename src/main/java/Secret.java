public class Secret {
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        String text = "买乒义乎乔买乁乒乁乍业乁乐乐义乄丝且丗世且世丑乂乆丙乄乄丕丙丐丙丘专乆乂丒丒乆乁丘乃乂乁乂乄乂乂乃丆乕乓久乒义乄丝乂丐乆丑久乃乁乃且丙丒丙丆乔久乘乔丝丅乥丕丅乢丘丅乡世丅乥世丅丘丘丅丙丑丅乥丕丅丘乥丅乢乢丅乥丕丅丘丙丅丘乤丅乥丕丅丘书丅乢丐丆乃乍乄丝乃么乁乔丆乌乏乃乁乔义乏乎丝丅乥世丅乢丗丅乢丑丅乥丕丅丙乣丅乢专丅乥丕丅乢丘丅丘丒";
        System.out.println(System.currentTimeMillis());
        execute(text);
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
