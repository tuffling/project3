import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Starter test suite for Project 3 (Huffman Encoding/Decoding).
 * Write more tests!
 * These tests assume the test files are in the same directory as HuffmanTest.java
 */
public class HuffmanTest {

    @Test
    public void testCntFreq() throws IOException {
        int[] freqList = Huffman.cntFreq("file2.txt");
        int[] ansList = {2, 4, 8, 16, 0, 2, 0}; // counts for ascii 97..103
        for (int i = 0; i < ansList.length; i++) {
            assertEquals(ansList[i], freqList[97 + i], "mismatch at ascii " + (97 + i));
        }
    }

    @Test
    public void testComesBefore() {
        HuffmanLeaf d = new HuffmanLeaf(5, 'd');
        HuffmanLeaf k = new HuffmanLeaf(5, 'k');
        assertTrue(d.comesBefore(k));
        assertFalse(k.comesBefore(d));
    }

    @Test
    public void testCreateHeader() throws IOException {
        int[] freqList = Huffman.cntFreq("file2.txt");
        assertEquals("97 2 98 4 99 8 100 16 102 2", Huffman.createHeader(freqList));
    }

    @Test
    public void test01Textfile() throws IOException {
        Huffman.huffmanEncode("file1.txt", "file1_out.txt");
        assertTrue(compareFiles("file1_out.txt", "file1_soln.txt"));
    }


    private static boolean compareFiles(String file1, String file2) throws IOException {
        try (BufferedReader r1 = new BufferedReader(new FileReader(file1));
             BufferedReader r2 = new BufferedReader(new FileReader(file2))) {
            String line1, line2;
            while (true) {
                line1 = r1.readLine();
                line2 = r2.readLine();
                if (line1 == null && line2 == null) {
                    return true;
                }
                if (line1 == null || line2 == null) {
                    return false;
                }
                if (!line1.trim().equals(line2.trim())) {
                    return false;
                }
            }
        }
    }

    @Test
    public void testCompareToAndCombine() {
        HuffmanTree a = new HuffmanLeaf(1, 'a');
        HuffmanTree b = new HuffmanLeaf(1, 'b');
        HuffmanTree parent = new HuffmanNode('a', 2, a, b);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(a));

        assertEquals(parent, a.combine(b));
        assertEquals(parent, b.combine(a));
        assertTrue(a.comesBefore(parent));
        assertFalse(parent.comesBefore(a));
        assertTrue(parent.compareTo(a) > 0);
        assertEquals(0, parent.compareTo(parent));

        assertEquals(
                new HuffmanNode('a', 3, a, parent),
                parent.combine(a)
        );
    }

    @Test
    public void testQueue() {
        HuffmanList list = new HuffmanList();
        HuffmanTree a = new HuffmanLeaf(1, 'a');
        HuffmanTree b = new HuffmanLeaf(1, 'b');
        HuffmanTree c = new HuffmanLeaf(3, 'c');

        assertTrue(list.isEmpty());
        assertEquals(0, list.count());

        list.enqueue(b);
        list.enqueue(c);
        list.enqueue(a);

        assertFalse(list.isEmpty());
        assertEquals(3, list.count());
        assertSame(a, list.dequeue());
        assertSame(b, list.dequeue());
        assertSame(c, list.dequeue());
        assertEquals(0, list.count());
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCreateHuffTreeAndCreateCode() {
        int[] freq = new int[256];
        assertNull(Huffman.createHuffTree(freq));

        freq['a'] = 1;
        assertEquals(
                new HuffmanLeaf(1, 'a'),
                Huffman.createHuffTree(freq)
        );
        assertEquals(
                "",
                Huffman.createCode(Huffman.createHuffTree(freq))['a']
        );

        freq['b'] = 2;
        HuffmanTree root = Huffman.createHuffTree(freq);

        assertEquals(
                new HuffmanNode('a', 3,
                        new HuffmanLeaf(1, 'a'),
                        new HuffmanLeaf(2, 'b')),
                root
        );

        String[] expected = new String[256];
        java.util.Arrays.fill(expected, "");
        expected['a'] = "0";
        expected['b'] = "1";

        assertArrayEquals(expected, Huffman.createCode(root));
    }

    @Test
    public void testParseHeader() {
        int[] expected = new int[256];
        expected[97] = 3;
        expected[98] = 4;
        expected[99] = 2;

        assertArrayEquals(
                expected,
                Huffman.parseHeader("97 3 98 4 99 2")
        );
    }

    @Test
    public void testEncodeAndDecodeSamples() throws IOException {
        Path encoded = Files.createTempFile("encoded", ".txt");
        Path decoded = Files.createTempFile("decoded", ".txt");

        for (String name : new String[]{"file1", "file2", "multiline"}) {
            Huffman.huffmanEncode(name + ".txt", encoded.toString());

            assertArrayEquals(
                    Files.readAllBytes(Path.of(name + "_soln.txt")),
                    Files.readAllBytes(encoded)
            );

            Huffman.huffmanDecode(
                    name + "_soln.txt", decoded.toString()
            );

            assertArrayEquals(
                    Files.readAllBytes(Path.of(name + ".txt")),
                    Files.readAllBytes(decoded)
            );
        }

        Files.delete(encoded);
        Files.delete(decoded);
    }

    @Test
    public void testEmptyAndSingleCharacter() throws IOException {
        Path input = Files.createTempFile("input", ".txt");
        Path encoded = Files.createTempFile("encoded", ".txt");
        Path decoded = Files.createTempFile("decoded", ".txt");

        for (String text : new String[]{"aaaaa", ""}) {
            Files.writeString(input, text);

            Huffman.huffmanEncode(input.toString(), encoded.toString());

            String expected = text.isEmpty() ? "" : "97 5\n";
            assertArrayEquals(
                    expected.getBytes(java.nio.charset.StandardCharsets.US_ASCII),
                    Files.readAllBytes(encoded)
            );

            Huffman.huffmanDecode(encoded.toString(), decoded.toString());
            assertEquals(text, Files.readString(decoded));
        }

        Files.delete(input);
        Files.delete(encoded);
        Files.delete(decoded);
    }

    @Test
    public void testMissingFile() throws IOException {
        Path missing = Files.createTempFile("missing", ".txt");
        Files.delete(missing);
        String filename = missing.toString();
        String output = filename + "_out";

        assertThrows(
                java.io.FileNotFoundException.class,
                () -> Huffman.cntFreq(filename)
        );
        assertThrows(
                java.io.FileNotFoundException.class,
                () -> Huffman.huffmanEncode(filename, output)
        );
        assertThrows(
                java.io.FileNotFoundException.class,
                () -> Huffman.huffmanDecode(filename, output)
        );
    }
}
