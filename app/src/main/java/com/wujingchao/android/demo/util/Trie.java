package com.wujingchao.android.demo.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 字典树
 */
public class Trie {

    private static String TAG = "Trie";


    public static void addNode(Node root, String word) {

        if (word.length() == 0) {
            return;
        }

        int index = word.charAt(0) - 'a';

        if (root.childNodes[index] == null) {
            root.childNodes[index] = new Node();
            root.childNodes[index].c = word.charAt(0);
        }

        addNode(root.childNodes[index], word.substring(1));
    }


    public static boolean delNode(Node root, String word) {
        if (word.length() == 0) {
            return true;
        }

        int index = word.charAt(0) - 'a';

        Node childN = root.childNodes[index];
//        if (childN == null) {//        if (childN == null) {
////
//            return false;
//        }

        boolean del = true;
        for (Node n : root.childNodes) {
            if (n != null && n.c != childN.c) {
                del = false;
            }
        }

        if (del) {
            return true;
        }


        if (delNode(childN, word.substring(1))) {
            root.childNodes[index] = null;
        }

        return false;
    }

    public static void getDFS(Node root, StringBuilder builder, ArrayList<String> result) {
        builder.append(root.c);
        boolean hasChild = false;
        for (Node n: root.childNodes) {
            if (n != null) {
                getDFS(n, builder, result);
                builder.deleteCharAt(builder.length() - 1);
                hasChild = true;
            }
        }
        if (!hasChild) {
            result.add(builder.toString());
        }
    }

    public static String getBFS(Node root) {
        StringBuilder builder = new StringBuilder();
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        Node tmp;
        int curLevelCnt = 1;
        int nextLevelCnt = 0;
        while ((tmp = queue.poll()) != null) {
            curLevelCnt--;
            for (Node n : tmp.childNodes) {
                if (n != null) {
                    builder.append(n.c);
                    queue.offer(n);
                    nextLevelCnt++;
                }
            }
            if (curLevelCnt == 0) {
                builder.append("\n");
                curLevelCnt = nextLevelCnt;
                nextLevelCnt = 0;
            }
        }
        return builder.toString();
    }


    public static class Node {

        public final Node[] childNodes = new Node[26];

        public char c;

    }

}
