package bupt.fnl.dht.control;

import bupt.fnl.dht.utils.Message;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void transfer0() throws Exception {
        Message message0 = new Message();
        message0.setType("getNodeList");
        Controller.transfer("10.108.146.88","2050",message0);
    }
    @Test
    public void transfer1() throws Exception {
        Message message1 = new Message();
        message1.setType("register");
        message1.setIdentity("bupt/123");
        message1.setMappingData("www.bupt.edu.cn");
        Controller.transfer("10.108.146.88","2050",message1);
    }
    @Test
    public void transfer2() throws Exception {
        Message message2 = new Message();
        message2.setType("resolve");
        message2.setIdentity("bupt/123");
        Controller.transfer("10.108.146.88","2050",message2);
    }
}