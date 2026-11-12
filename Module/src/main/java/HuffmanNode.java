/**
 * A single node in a Huffman tree, implemented as a record.
 *
 * A HuffmanNode represents either a leaf (an actual character together
 * with its occurrence count) or an internal node created by combining two
 * subtrees during tree construction.
 *
 * Records are immutable and give you charAscii(), freq(), left(), and
 * right() accessor methods automatically, along with
 * equals()/hashCode()/toString(). Do not rename the components below.
 */
public record HuffmanNode(int charAscii, int freq, HuffmanTree left, HuffmanTree right)
        implements HuffmanTree{

    // Returns whether this tree precedes other by frequency, then ASCII value
    public boolean comesBefore(HuffmanTree other) {
         if(this.freq == other.freq()){
             return this.charAscii < other.charAscii();
         }
         return this.freq < other.freq();
    }

    // Returns a new parent combining this tree and other, placing the smaller tree left
    public HuffmanTree combine(HuffmanTree other) {
        int sum = this.freq + other.freq();
        int charVal = Math.min(this.charAscii, other.charAscii());

        if(this.comesBefore(other)) {
            return new HuffmanNode(charVal, sum, this, other);
        }
        else{
            return new HuffmanNode(charVal, sum, other, this);
        }
    }

    /**
     * Natural ordering consistent with comesBefore(), so that HuffmanNode
     * objects can be stored directly in a sorted structure (e.g. a
     * PriorityQueue or a sorted List) while building the tree.
     */

    // Returns a negative, zero, or positive value when this tree precedes, ties, or follows other
    @Override
    public int compareTo(HuffmanTree other) {
        if(this.comesBefore(other)){
            return -1;
        }
        else if(other.comesBefore(this)){
            return 1;
        }
        return 0;
    }
}
