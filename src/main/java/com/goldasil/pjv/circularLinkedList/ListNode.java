package com.goldasil.pjv.circularLinkedList;

class ListNode {
    public ListNode next;
    public Object data;

    public ListNode(Object data, ListNode next) {
        this.next = next;
        this.data = data;
    }
}