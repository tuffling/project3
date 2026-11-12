public class HuffmanList implements HQueue{
    private Node head;
    private int size;

    private static class Node {
        HuffmanTree tree;
        Node next;

        Node(HuffmanTree tree, Node next) {
            this.tree = tree;
            this.next = next;
        }
    }

    // Returns true if the list contains no trees
    public boolean isEmpty(){
        return this.head == null;
    }

    // Returns the number of trees in the list
    public int count(){
        return this.size;
    }

    // Inserts tree in ascending order by frequency, then ASCII value
    public void enqueue(HuffmanTree tree){
        if(this.isEmpty()){
            head = new Node(tree, head);
        }
        else if(tree.comesBefore(this.head.tree)){
            head = new Node(tree, head);
        }
        else{
            Node current = head;
            while(current.next != null && !tree.comesBefore(current.next.tree)){
                current = current.next;
            }
            current.next = new Node(tree, current.next);
        }

        size++;
    }

    // Removes and returns the first tree, or null if the list is empty
    public HuffmanTree dequeue(){
        if(isEmpty()){
            return null;
        }

        HuffmanTree returnTree = this.head.tree;
        this.head = this.head.next;
        size--;

        return returnTree;
    }
}
