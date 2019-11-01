package cs.f10.t1.nursetraverse.autocomplete;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PrefixWordTest {

    private PrefixWord testPrefixWord = new PrefixWord(
            "test1", "test2", "test3");

    @Test
    public void getSuggestedWord() {
        assertEquals("test3", testPrefixWord.getSuggestedWord());
    }

    @Test
    public void getAssociatedWordList() {
        assertEquals(2, testPrefixWord.getAssociatedWordList().size());
        assertEquals("test1", testPrefixWord.getAssociatedWordList().get(0));
        assertEquals("test2", testPrefixWord.getAssociatedWordList().get(1));
    }

    @Test
    public void getConnectorChar() {
        assertEquals(" ", testPrefixWord.getConnectorChar());
    }
}
