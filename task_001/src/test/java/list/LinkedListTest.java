package list;

import org.junit.Before;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class LinkedListTest {
    private LinkedList<Integer> list;

    @Before
    public void beforeTest() {
        list = new LinkedList<>();
        list.add(11);
        list.add(77);
        list.add(55);
        list.add(22);
        list.add(3, 777);

    }


    @Test
    public void whenAddedFiveThenGetFirstInRowElement() {
        assertThat(list.get(0), is(11));
    }

    @Test
    public void whenAddedFiveSizeIsFive() {
        assertThat(list.getSize(), is(5));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void concurrentModificationExceptionTest() {
        Iterator<Integer> it = list.iterator();
        it.next();
        it.next();
        list.add(2);
        it.next();
    }

    @Test
    public void whenAddedFiveThenGetThirdInRowElement() {
        assertThat(list.get(3), is(777));
    }

    @Test
    public void whenAddFiveElementsThenDeleteFirstAndLast() {
        assertThat(list.dropLast(), is(22));
        assertThat(list.dropFirst(), is(11));
        assertThat(list.get(0), is(77));
    }
}