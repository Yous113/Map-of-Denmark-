package org.example.firstyearproject.AdressSearch;

import java.io.Serializable;
import org.apache.commons.text.WordUtils;


import java.util.*;

public class Trie implements Serializable {

    public class TrieNode implements Serializable {
        Map<Character, TrieNode> children;
        char c;
        boolean isWord;

        public TrieNode(char c) {
            this.c = c;
            children = new HashMap<>();
        }

        public TrieNode() {
            children = new HashMap<>();
        }

        public void insert(String word) {
            if (word == null || word.isEmpty())
                return;

            word = word.toLowerCase();

            char firstChar = word.charAt(0);
            TrieNode child = children.get(firstChar);
            if (child == null) {
                child = new TrieNode(firstChar);
                children.put(firstChar, child);
            }

            if (word.length() > 1)
                child.insert(word.substring(1));
            else
                child.isWord = true;
        }

        public Map<Character, TrieNode> getChildren() {
            return children;
        }

        public void setChildren(Map<Character, TrieNode> children) {
            this.children = children;
        }

        public boolean getIsWord () {
            return isWord;
        }
    }

    TrieNode root;

    public Trie(List<String> words) {
        root = new TrieNode();
        for (String word : words)
            root.insert(word);

    }

    public void suggestHelper(TrieNode root, List<String> list, StringBuffer curr) {
        if (root.isWord) {
            String endString = curr.toString().replaceAll("ã¸", "ø").replaceAll("ã¥", "å").replaceAll("ã¦", "æ");
            String endResults = WordUtils.capitalize(endString);
            list.add(endResults);
        }
        if (root.children == null || root.children.isEmpty())
            return;

        for (TrieNode child : root.children.values()) {
            suggestHelper(child, list, curr.append(child.c));
            curr.setLength(curr.length() - 1);
        }
    }

    public List<String> suggest(String prefix) {
        prefix = prefix.toLowerCase();
        List<String> list = new ArrayList<>();
        TrieNode lastNode = root;
        StringBuffer curr = new StringBuffer();
        for (char c : prefix.toCharArray()) {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
                return list;
            curr.append(c);
        }
        suggestHelper(lastNode, list, curr);

        return list;
    }

    public TrieNode getRoot() {
        return root;
    }
}