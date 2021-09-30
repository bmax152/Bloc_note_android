package mb.solo.blocnote;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import mb.solo.blocnote.model.Note;
import mb.solo.blocnote.orm.NoteDao;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("mb.solo.blocnote", appContext.getPackageName());
    }

    @Test
    public void test1(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        NoteDao dao = new NoteDao(appContext);
        /*dao.create(new Note(0, "Exemple1", "Je suis un exemple"));
        dao.create(new Note(0, "Exemple2", "Je suis un autre exemple"));*/
        List<Note> list = dao.list();

        Assert.assertNotNull(list);

        for (Note n: list) {
            Log.i("orm", "test1: " + n.getNom() + ": " + n.getTexte());
        }
    }
}