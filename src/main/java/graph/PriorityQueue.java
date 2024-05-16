package graph;

import java.util.Arrays;

class PriorityQueue {
    private Node[] heap;
    private int[] positions;
    private int size;

    private class Node {
        int nodeId;
        int priority;

        Node(int nodeId, int priority) {
            this.nodeId = nodeId;
            this.priority = priority;
        }
    }

    public PriorityQueue(int capacity) {
        heap = new Node[capacity];
        positions = new int[capacity];
        Arrays.fill(positions, -1);
        size = 0;
    }

    public void insert(int nodeId, int priority) {
        size++;
        heap[size] = new Node(nodeId, priority);
        positions[nodeId] = size;
        bubbleUp(size);
    }

    public int removeMin() {
        int minNodeId = heap[1].nodeId;
        swap(1, size);
        positions[heap[size].nodeId] = -1;
        size--;
        bubbleDown(1);
        return minNodeId;
    }

    public void reduceKey(int nodeId, int newPriority) {
        int index = positions[nodeId];
        heap[index].priority = newPriority;
        bubbleUp(index);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void bubbleUp(int index) {
        int parentIndex = index / 2;
        if (parentIndex > 0 &&
                (heap[parentIndex].priority > heap[index].priority)) {
            swap(index, parentIndex);
            bubbleUp(parentIndex);
        }
    }

    private void bubbleDown(int index) {
        int leftChildIndex = 2 * index;
        int rightChildIndex = 2 * index + 1;
        int smallestIndex = index;
        if (leftChildIndex <= size &&
                (heap[leftChildIndex].priority < heap[smallestIndex].priority)) {
            smallestIndex = leftChildIndex;
        }
        if (rightChildIndex <= size &&
                (heap[rightChildIndex].priority < heap[smallestIndex].priority)) {
            smallestIndex = rightChildIndex;
        }
        if (smallestIndex != index) {
            swap(index, smallestIndex);
            bubbleDown(smallestIndex);
        }
    }

    private void swap(int i, int j) {
        Node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        positions[heap[i].nodeId] = i;
        positions[heap[j].nodeId] = j;
    }

    public boolean contains(int nodeId) {
        return positions[nodeId] != -1;
    }
}