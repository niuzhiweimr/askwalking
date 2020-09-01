package test.base;

import com.cloud.askwalking.bootstrap.AskwalkingApplication;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes = AskwalkingApplication.class)
public class TestBase {

    public static Logger logger = LoggerFactory.getLogger(TestBase.class);
}
