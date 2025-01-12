package org.example.firstyearproject;
import org.example.firstyearproject.AdressSearch.Trie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


public class TrieTest {
    Trie trie;
    @BeforeEach
    void setUp() {
        List<String> testWords = new ArrayList<>();
        testWords.add("Lion");
        testWords.add("Doplhin");
        testWords.add("Cow");
        trie = new Trie(testWords);
    }

    @Test
    void TestConstructor(){
        assertNotNull(trie);
    }

    @Test
    void testInsert() {
        trie.getRoot().insert("cat");
        assertTrue(trie.getRoot().getChildren().containsKey('c'));
        assertTrue(trie.getRoot().getChildren().get('c').getChildren().containsKey('a'));
        assertTrue(trie.getRoot().getChildren().get('c').getChildren().get('a').getChildren().containsKey('t'));
        assertTrue(trie.getRoot().getChildren().get('c').getChildren().get('a').getChildren().get('t').getIsWord());
    }

    @Test
    void testLowerCase(){
        assertTrue(trie.getRoot().getChildren().containsKey('l'));
        assertTrue(trie.getRoot().getChildren().containsKey('d'));
        assertTrue(trie.getRoot().getChildren().containsKey('c'));
        assertFalse(trie.getRoot().getChildren().containsKey('L'));
        assertFalse(trie.getRoot().getChildren().containsKey('D'));
        assertFalse(trie.getRoot().getChildren().containsKey('C'));
    }

    @Test
    void testNullWord(){
        trie.getRoot().insert(null);
        assertTrue(trie.getRoot().getChildren().containsKey('l'));
        assertTrue(trie.getRoot().getChildren().containsKey('d'));
        assertTrue(trie.getRoot().getChildren().containsKey('c'));
        assertEquals(3, trie.getRoot().getChildren().size());
    }

    @Test
    public void testSuggestHelper() {
        Trie.TrieNode root = trie.getRoot();
        List<String> suggestions = new ArrayList<>();
        StringBuffer currentWord = new StringBuffer();
        trie.suggestHelper(root, suggestions, currentWord);
        List<String> expectedSuggestions = new ArrayList<>();
        //add in lexicographical order
        expectedSuggestions.add("cow");
        expectedSuggestions.add("doplhin");
        expectedSuggestions.add("lion");
        assertEquals(expectedSuggestions, suggestions);
    }
    @Test
    void testSuggestHelperEmptyRoot() {
        Trie.TrieNode root = trie.getRoot();
        root.setChildren(null);
        List<String> suggestions = new ArrayList<>();
        StringBuffer currentWord = new StringBuffer();
        trie.suggestHelper(root, suggestions, currentWord);
        assertTrue(suggestions.isEmpty());
    }
    @Test
    void testSuggest() {
        List<String> suggestions = trie.suggest("l");
        assertEquals(1, suggestions.size());
        assertTrue(suggestions.contains("lion"));
    }

    @Test
    void testSuggestLastNodeIsNull() {
        List<String> suggestions = trie.suggest("Tiger");
        assertTrue(suggestions.isEmpty());
    }

}