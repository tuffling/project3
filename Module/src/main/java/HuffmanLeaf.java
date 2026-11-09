public record HuffmanLeaf (int freq, int charAscii) implements HuffmanTree{

    @Override
    public boolean comesBefore(HuffmanTree other) {
        if(this.freq == other.freq()){
            return this.charAscii < other.charAscii();
        }
        return this.freq < other.freq();
    }

    @Override
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

    @Override
    public int compareTo(HuffmanTree other) {
        // unimplemented ...
        return 0;
    }
}
