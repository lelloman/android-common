package com.lelloman.common.androidtestutils;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.lelloman.common.androidtestutils.test", appContext.getPackageName());
    }
}
