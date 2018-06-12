package com.wujingchao.android.demo;

import com.wujingchao.android.demo.util.Trie;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testTrie() {
        Trie.Node root = new Trie.Node();
        root.c = ' ';
        Trie.addNode(root, "and");
        Trie.addNode(root, "as");
        Trie.addNode(root, "at");
        Trie.addNode(root, "cn");
        Trie.addNode(root, "com");



        ArrayList<String> result = new ArrayList<>();
        Trie.getDFS(root, new StringBuilder(), result);
        System.out.println("DFS : " + result);

        System.out.println("BFS : \n" + Trie.getBFS(root));

        Trie.delNode(root, "com");
        Trie.delNode(root, "cn");
        Trie.delNode(root, "and");
        result.clear();
        Trie.getDFS(root, new StringBuilder(), result);
        System.out.println("DFS : " + result);
    }
}