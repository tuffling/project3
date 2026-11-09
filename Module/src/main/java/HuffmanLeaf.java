public record HuffmanLeaf (int freq, int charAscii) implements HuffmanTree{

    @Override
    public boolean comesBefore(HuffmanTree other) {
        if(this.freq == other.freq()){
            return this.charAscii >= other.charAscii();
        }
        return this.freq > other.freq();
    }

    @Override
    public HuffmanTree combine(HuffmanTree other) {
        // unimplemented...
        return this;
    }

    @Override
    public int compareTo(HuffmanTree other) {
        // unimplemented ...
        return 0;
    }
}
