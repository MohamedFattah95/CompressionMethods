package com.mohamedfattah95.compressionmethods;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HuffmanActivity extends AppCompatActivity {

    @BindView(R.id.tvResult)
    TextView tvResult;

    @BindView(R.id.etText)
    EditText etText;

    @BindView(R.id.btnCompress)
    Button btnCompress;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    int[] countOfChar;
    ArrayList<Character> characters;

    char[] chars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huffman);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.huffman);
        ButterKnife.bind(this);

        btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etText.getText().toString();
                if (msg.length() <= 0) {
                    Toast.makeText(getBaseContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
                } else {
                    tvInfo.setText(process(msg));

                    chars = new char[characters.size()];
                    for (int i = 0; i < characters.size(); i++) {
                        chars[i] = characters.get(i);
                    }

                    HuffTree hTree = new HuffTree(countOfChar, chars);
                    tvResult.setText(hTree.encode(msg));

                }
            }
        });

    }

    private String process(String msg) {

        char[] msgChar = msg.toCharArray();
        characters = new ArrayList<>();

        for (char aMsgChar : msgChar) {
            if (!(characters.contains(aMsgChar))) {
                characters.add(aMsgChar);
            }
        }

        countOfChar = new int[characters.size()];
        for (int i = 0; i < characters.size(); i++) {
            char checker = characters.get(i);
            for (char aMsgChar : msgChar) {
                if (checker == aMsgChar) {
                    countOfChar[i]++;
                }
            }
        }

        /* Sort the arrays is descending order */
        for (int i = 0; i < countOfChar.length - 1; i++) {
            for (int j = 0; j < countOfChar.length - 1; j++) {
                if (countOfChar[j] < countOfChar[j + 1]) {
                    int temp = countOfChar[j];
                    countOfChar[j] = countOfChar[j + 1];
                    countOfChar[j + 1] = temp;

                    char tempChar = characters.get(j);
                    characters.set(j, characters.get(j + 1));
                    characters.set(j + 1, tempChar);
                }
            }
        }

        /* Print Out The Frequencies of the Characters */
        StringBuilder info = new StringBuilder();
        for (int x = 0; x < countOfChar.length; x++) {
            info.append(characters.get(x)).append(" - ").append(countOfChar[x]).append("\n");
        }

        return info.toString();
    }


    public static class HuffNode implements Comparable<HuffNode> {

        int value;
        int weight;
        HuffNode leftTree;
        HuffNode rightTree;
        HuffNode parent;

        HuffNode() {
            parent = null;
        }

        HuffNode(int v, int w, HuffNode lTree, HuffNode rTree, HuffNode par) {
            value = v;
            weight = w;
            leftTree = lTree;
            rightTree = rTree;
            parent = par;
        }

        @Override
        public int compareTo(HuffNode rhs) {
            return weight - rhs.weight;
        }

        @NonNull
        @Override
        public String toString() {
            String str = "";
            str += this.value;
            return str;
        }
    }

    public static class HuffTree {

        private int size = 0;
        private HuffNode root = new HuffNode();
        private PriorityQueue<HuffNode> huffQueue = new PriorityQueue();
        ArrayList<String> pathTable = new ArrayList();
        ArrayList<Character> valueTable = new ArrayList();

        public HuffTree(int[] freq, char[] code) {
            // get the counts
            this.size = freq.length;

            // throw exception if arrays are different sizes
            if (freq.length != code.length) {
                throw new UnsupportedOperationException("Error: Character and code length mismatch.");
            }

            // build huffQueue from frequencies given
            for (int i = 0; i < this.size; i++) {
                huffQueue.offer(new HuffNode(code[i], freq[i], null, null, null));
            }

            // build huffman tree from queue
            createTree();

            // build code table from huffman tree
            createTable(this.root, "");
        }

        private void createTree() {
            // while elements remain in huffQueue, add to tree
            while (huffQueue.size() > 1) {
                // pop off two minimum elements in huffQueue
                HuffNode tempL = huffQueue.poll();
                HuffNode tempR = huffQueue.poll();

                // create root for two minimum elements and build tree
                HuffNode parent = new HuffNode(0, tempL.weight + tempR.weight, tempL, tempR, null);
                tempL.parent = parent;
                tempR.parent = parent;

                // add new tree back in huffQueue
                huffQueue.offer(parent);
                this.size++;
            }

            // set HuffTree root to remaining element in huffQueue
            this.root = huffQueue.peek();
        }

        private void createTable(HuffNode curr, String str) {
            // if iterator is null, return
            if (curr == null) {
                return;
            }

            // else if leaf, display path and value
            if (curr.leftTree == null && curr.rightTree == null) {
                char tempChar;
                if (curr.value == 32) {
                    tempChar = ' ';
                }

                if (curr.value == 10) {
                    tempChar = 'n';
                } else {
                    tempChar = (char) curr.value;
                }
                // add value and path to code tables
                this.valueTable.add(tempChar);
                this.pathTable.add(str);
            }

            // add 0 if before moving to left child
            str += "0";
            // recursively call in pre-order
            createTable(curr.leftTree, str);

            // adjust path and add 1 before moving to right child
            str = str.substring(0, str.length() - 1);
            str += "1";
            createTable(curr.rightTree, str);
        }

        String tacks = "";

        void getTree(HuffNode curr) {
            // if iterator is null, return
            if (curr == null) {
                return;
            }

            // else if leaf, display level, weight, and value
            if (curr.leftTree == null && curr.rightTree == null) {
                // case statements to handle displaying space and newline
                switch (curr.value) {
                    case 32:
                        System.out.println(tacks + curr.weight + ": sp");
                        break;
                    case 10:
                        System.out.println(tacks + curr.weight + ": nl");
                        break;
                    default:
                        System.out.println(tacks + curr.weight + ": " + (char) curr.value);
                        break;
                }
            } // else display level and weight
            else {
                System.out.println(tacks + curr.weight);
            }

            // increment level marker
            tacks += "- ";
            // recursively call in pre-order
            getTree(curr.leftTree);
            getTree(curr.rightTree);
            // decrement level marker
            tacks = tacks.substring(0, tacks.length() - 2);
        }

        public int getSize() {
            return this.size;
        }

        String encode(String input) {
            // create empty string to hold code
            String str = "";

            // iterate through given string
            for (int x = 0; x < input.length(); x++) {
                // iterate through code tables
                for (int i = 0; i < valueTable.size(); i++) {
                    // if char in string matches code in table, add path to string
                    if (valueTable.get(i) == input.charAt(x)) {
                        str += pathTable.get(i) + " ";
                    }
                }
            }
            return str;
        }

        public String decode(String bits) {
            // create empty string to hold decoded message
            String decodedStr = "";

            // iterate through bits
            for (int i = 0; i < bits.length(); i++) {
                if (!getChar(bits.substring(0, i + 1)).equals("")) {
                    decodedStr += getChar(bits.substring(0, i + 1));
                    bits = bits.substring(i + 1);
                    i = 0;
                }
            }
            return decodedStr;
        }

        private String getChar(String bits) {
            // create string to hold potential character
            String character = "";
            // traverse code table to seek match
            for (int i = 0; i < pathTable.size(); i++) {
                // add to string if match is found
                if (pathTable.get(i).equals(bits)) {
                    character = valueTable.get(i).toString();
                }
            }
            return character;
        }
    }
}
