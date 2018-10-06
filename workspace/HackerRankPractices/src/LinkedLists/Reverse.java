package LinkedLists;

import LinkedLists.SortedInsert.DoublyLinkedListNode;

public class Reverse {
	// Complete the reverse function below.

	/*
	 * For your reference:
	 *
	 * DoublyLinkedListNode { int data; DoublyLinkedListNode next;
	 * DoublyLinkedListNode prev; }
	 *
	 */
	static DoublyLinkedListNode reverse(DoublyLinkedListNode head) {
		// if list is empty, return null
		if (head == null)
			return null;

		// swap next and prev
		DoublyLinkedListNode temp = head.next;
		head.next = head.prev;
		head.prev = temp;

		// if prev isn't null, keep going
		if (head.prev != null) {
			return reverse(head.prev);
		} else {
			return head;
		}
	}
}
