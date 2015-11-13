import com.thetruthbeyond.chatterbean.aiml.AIMLElement;
import com.thetruthbeyond.chatterbean.aiml.Date;
import com.thetruthbeyond.chatterbean.aiml.Template;
import com.thetruthbeyond.chatterbean.aiml.Text;

import org.junit.Test;

/**
 * Created by Peter Siatkowski on 2015-10-12.
 * Some tests.
 */
public class AIMLElementTest {

    @Test
    public void testAppendChild() throws Exception {
        AIMLElement template = new Template();

        AIMLElement text = new Text("Test case");
        template.appendChild(text);

        AIMLElement date = new Date();
        template.appendChild(date);
    }
}