import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.RandomAccess;
import java.util.Scanner;

/**
 * Created by zhuangh7 on 17-6-20.
 */
public class mmpSdk {

    static private boolean opened = false;
    static private RandomAccessFile memoryMappedFile;
    private static MappedByteBuffer out;
    private static long mapStart = 0;
    private static long mapSize = 1024;
    private static long FileLength;
    static private CharBuffer cb;

    static private void map() {
        try {
            out = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, mapStart, mapSize);
            cb = StandardCharsets.UTF_8.newDecoder().decode(out);
        } catch (IOException e) {
            System.out.println("映射过程出错");
        }
    }

    static public void open() {
        try {
            memoryMappedFile = new RandomAccessFile("ZMMP", "rw");
            FileLength = memoryMappedFile.length();
            //map();
            opened = true;
        } catch (Exception e) {

        }
    }

    static public void put(String key, String value) {
        if (opened) {
            try {
                long fileLength = memoryMappedFile.length();
                memoryMappedFile.seek(fileLength);
                memoryMappedFile.write((key + " " + value + "\n").getBytes());
                FileLength = memoryMappedFile.length();
            } catch (Exception e) {

            }
        } else {
            open();
            put(key, value);
        }
    }

    static public String get(String key) {
        if (opened) {
            mapStart = 0;
            mapSize = FileLength-mapStart > 1024?1024:FileLength-mapStart;
            map();
            String temp = readLine();
            Scanner s;
            if (temp != null)
                s = new Scanner(temp);
            else
                return null;

            while (true) {
                if (s.next().equals(key)) {
                    return s.next();
                } else {
                    temp = readLine();
                    if (temp != null)
                        s = new Scanner(temp);
                    else
                        return null;
                }
            }
        } else {
            open();
            return get(key);
        }
    }

    static private String readLine() {
        try {
            StringBuilder result = new StringBuilder();
            char temp = cb.get();
            while (temp != '\n') {
                if(temp == 0)
                    throw new Exception("nothing to show");
                result.append(temp);
                cb.position();
                try {
                    temp = cb.get();
                }catch(Exception e){
                    mapStart += 1024;
                    mapSize = FileLength-mapStart > 1024?1024:FileLength-mapStart;
                    map();
                    temp = cb.get();
                }
            }
            return result.toString();
        } catch (Exception e) {
            return null;
        }
    }

    static void close() {
        if (opened) {
            try {
                memoryMappedFile.close();
            } catch (IOException e) {
                //
                System.out.println("关闭存储文件出错");
            }
        }
    }

    static void 删库(){
        File f = new File("ZMMP");
        if(f.isFile()){
            f.delete();
        }//从删库到跑路
    }
}
