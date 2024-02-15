package ADTPackage;
import java.util.EmptyStackException;

public final class LinkedStack<T> implements StackInterface<T> {
	private Node topNode; // References the first node in the chain
	private int size; // Variable to keep track of stack size

	public LinkedStack() {
		topNode = null;
		size = 0;
	} // end default constructor

	public void push(T newEntry) {
		Node newNode = new Node(newEntry, topNode);
		topNode = newNode;
		size++; // Increment size when pushing a new entry
	} // end push

	public T peek() {
		if (isEmpty()) {
			throw new EmptyStackException();
		} else {
			return topNode.getData();
		}
	} // end peek

	public T pop() {
		if (isEmpty()) {
			throw new EmptyStackException();
		} else {
			T top = peek(); // Might throw EmptyStackException
			topNode = topNode.getNextNode();
			size--; // Decrement size when popping an entry
			return top;
		}
	} // end pop

	public boolean isEmpty() {
		return topNode == null;
	} // end isEmpty

	public void clear() {
		topNode = null; // Causes deallocation of nodes in the chain
		size = 0; // Reset size to 0 when clearing the stack
	} // end clear

	public int size() {
		return size; // Return the size of the stack
	} // end size

	private class Node {
		private T data; // Entry in stack
		private Node next; // Link to next node

		private Node(T dataPortion) {
			this(dataPortion, null);
		} // end constructor

		private Node(T dataPortion, Node linkPortion) {
			data = dataPortion;
			next = linkPortion;
		} // end constructor

		private T getData() {
			return data;
		} // end getData

		private Node getNextNode() {
			return next;
		} // end getNextNode
	} // end Node
} // end LinkedStack
