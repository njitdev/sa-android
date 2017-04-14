package com.njitdev.sa_android;

import com.njitdev.sa_android.home.HomeActivity;
import com.njitdev.sa_android.messageboard.Models;
import com.njitdev.sa_android.utils.ModelListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

    }

    @Mock
    private Models models;

    @Mock
    private ModelListener mModelListener;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

}