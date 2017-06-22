import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;
import junit.framework.AssertionFailedError;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by zhuangh7 on 17-6-20.
 */

public class TestMain {

    XingeApp xinge = new XingeApp(2100261173, "53f616db7283e77ec6bb738e509a918b");
    @Test
    public void test() {
        Assert.assertEquals("value_3",mmpSdk.get("key_3"));
        mmpSdk.删库();
    }


    protected JSONObject demoPushSingleAccount() {
        Message message = new Message();
        message.setTitle("天王盖被子");
        message.setContent("宝塔镇腰子");
        message.setType(Message.TYPE_NOTIFICATION);
        message.setStyle(new Style(0,1,1,1,0,1,0,1));
        JSONObject ret = xinge.pushSingleAccount(0, "593202119@qq.com", message);
        return ret;
    }
}
