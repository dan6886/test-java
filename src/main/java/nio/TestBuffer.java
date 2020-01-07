package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.PrimitiveIterator;

/**
 * 缓冲区中的四个属性
 * capacity：缓冲区中的最大储存数据容量，一旦声明不能改变
 * limit:界限，便是缓冲区中可以操作的数据大小，(limit后的数据不能进行读写)
 * position：位置，表示缓冲区中正在操作的数据的位置
 */
public class TestBuffer {
    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.toString("你好啊".getBytes()));
        test2();
        Charset charset = StandardCharsets.UTF_8;
    }

    public static void test1() throws IOException {
        FileChannel channel = FileChannel.open(Path.of("1.txt", ""), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);
        byteBuffer.flip();
        byte[] dst = new byte[byteBuffer.limit()];
        byteBuffer.get(dst);
        System.out.println(new String(dst));
    }

    public static void test2() throws IOException {
        FileChannel channel = FileChannel.open(Path.of("file/1.txt", ""), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        ChannelReader channelReader = new ChannelReader(channel);
        String line = null;
        while ((line = channelReader.readLine()) != null) {
            System.out.println(line);
        }
        channelReader.close();
    }

    /**
     * 一个基于channel的文件安行数读取类,对比bufferreader
     */
    static class ChannelReader implements AutoCloseable {
        private FileChannel mChannel;
        private ByteBuffer mBuffer;
        private long mPosition = 0;
        private Charset charset = StandardCharsets.UTF_8;
        public ChannelReader(FileChannel mChannel) {
            this.mChannel = mChannel;
            mBuffer = ByteBuffer.allocate(1024);
        }

        private String readLine() throws IOException {
            StringBuffer sb = new StringBuffer();
            byte[] holder = new byte[1];
            ByteBuffer resultLine = ByteBuffer.allocate(512);
            mChannel.position(mPosition);
            byte preByte = -1;
            while (mChannel.read(mBuffer) > 0) {
                mBuffer.flip();
                while (mBuffer.hasRemaining()) {
                    mBuffer.get(holder);
                    if (isNextLineByte(preByte, holder[0])) {
                        int position = resultLine.position();
                        resultLine.limit(position - 1);
                        mPosition++;
                        // 检查到换行符，需要停止并返回结果
                        mBuffer.clear();
                        resultLine.flip();
                        byte[] r = new byte[resultLine.limit()];
                        resultLine.get(r);
                        resultLine.clear();
                        return sb.append(new String(r)).toString();
                    } else {
                        //todo 这里要注意处理result超过大小的时候
                        // 正常的字节
                        if (resultLine.position() == resultLine.capacity()) {
                            System.out.println("读取满了扩充");
                            byte[] r = new byte[resultLine.limit()];
                            resultLine.flip();
                            resultLine.get(r);
//                            CharBuffer decode = charset.decode(resultLine);
//                            char[] chars = new char[decode.limit()];
//                            decode.get(chars);
                            sb.append(new String(r));
                            System.out.println("读取满了扩充:" + sb.toString());
                            resultLine.clear();
                        }
                        resultLine.put(holder);
                    }
                    preByte = holder[0];
                    mPosition++;
                }
                mBuffer.clear();
            }
            return null;
        }

        /**
         * 换行符是两个字节组成分别为 13和10 连续的字节
         *
         * @param pre
         * @param cur
         * @return
         */
        private boolean isNextLineByte(byte pre, byte cur) {
            return pre == 13 && cur == 10;
        }

        @Override
        public void close() throws IOException {
            mChannel.close();
        }
    }
}