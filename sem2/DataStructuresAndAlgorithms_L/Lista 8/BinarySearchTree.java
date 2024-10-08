import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<T>> {

    Node<T> root;
    BinarySearchTree() {
        this.root = null;
    }

    private static class Node<T extends Comparable<T>> {
        T value;
        Node<T> parent;
        Node<T> left;
        Node<T> right;

        public Node(T value) {
            this.value = value;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != Node.class) return false;
            Node<T> node = (Node) obj;
            return this.value.equals(node.value);
        }
    }

    public void add(T value) throws DuplicateElementException {
        Node<T> newNode = new Node<>(value);
        if (root == null) {
            root = newNode;
        } else {
            Node<T> currentNode = root;
            while (true) {
                if (newNode.equals(currentNode)) throw new DuplicateElementException();
                if (value.compareTo(currentNode.value) < 0) {
                    if (currentNode.left == null) {
                        currentNode.left = newNode;
                        newNode.parent = currentNode;
                        return;
                    }
                    currentNode = currentNode.left;
                } else {
                    if (currentNode.right == null) {
                        currentNode.right = newNode;
                        newNode.parent = currentNode;
                        return;
                    }
                    currentNode = currentNode.right;
                }
            }
        }
    }

    public boolean contains(T value) {
        return findNodeByValue(value)!=null;
    }

    public void delete(T value) {
        Node<T> nodeToDelete = findNodeByValue(value);
        Node<T> parent = null;
        if(nodeToDelete!=null){
            if(nodeToDelete.left == null && nodeToDelete.right==null){
                if(nodeToDelete==root){
                    root=null;
                }else if(nodeToDelete.parent.left==nodeToDelete){
                    nodeToDelete.parent.left=null;
                }else nodeToDelete.parent.right=null;
            }
            else if(nodeToDelete.left!=null && nodeToDelete.right==null){
                if(nodeToDelete==root){
                    root = root.left;
                    root.parent=null;
                }else if(nodeToDelete.parent.left==nodeToDelete){
                    nodeToDelete.left.parent=nodeToDelete.parent;
                    nodeToDelete.parent.left=nodeToDelete.left;
                }else{
                    nodeToDelete.left.parent=nodeToDelete.parent;
                    nodeToDelete.parent.right=nodeToDelete.left;
                }
            }
            else if(nodeToDelete.left == null){
                if(nodeToDelete==root){
                    root=root.right;
                    root.parent=null;
                }else if(nodeToDelete.parent.left==nodeToDelete){
                    nodeToDelete.right.parent=nodeToDelete.parent;
                    nodeToDelete.parent.left=nodeToDelete.right;
                }else{
                    nodeToDelete.right.parent=nodeToDelete.parent;
                    nodeToDelete.parent.right=nodeToDelete.right;
                }
            }else{
                Node<T> successor = findSuccessor(nodeToDelete.right);
                //System.out.println("Successor: "+successor.value);
                T tmp = successor.value;
                delete(tmp);
                nodeToDelete.value=tmp;
            }
        }

        // TODO: Usunięcie wskazanej wartości z drzewa.
    }

    //root,left,right
    public String toStringPreOrder() {
        String PreOrder = "";
        if (root == null) return PreOrder;
        Stack<Node<T>> stack = new Stack<>();
        stack.push(root);
        while (!stack.empty()) {
            Node<T> currentNode = stack.pop();
            if (currentNode != null) {
                PreOrder += currentNode.value.toString() + ", ";

                if (currentNode.right != null) {
                    stack.push(currentNode.right);
                }
                if (currentNode.left != null) {
                    stack.push(currentNode.left);
                }
            }
        }
        return PreOrder.substring(0, PreOrder.length() - 2);
    }

    //left,root,right
    public String toStringInOrder() {
        String InOrder = "";
        if(root==null) return InOrder;
        Stack<Node<T>> stack = new Stack<>();
        Node<T> currentNode = root;
        while(!stack.isEmpty() || currentNode!=null){
            while(currentNode!=null){
                stack.push(currentNode);
                currentNode=currentNode.left;
            }
            currentNode=stack.pop();
            InOrder+=currentNode.value+", ";
            currentNode=currentNode.right;
        }
       return InOrder.substring(0,InOrder.length()-2);
    }


    //left,right,root
    public String toStringPostOrder() {
        String PostOrder = "";
        if(root==null) return PostOrder;
        Stack<Node<T>> stack = new Stack<>();
        Stack<Node<T>> stackToPrint = new Stack<>();
        stack.push(root);
        while(!stack.isEmpty()){
            Node<T> currentNode = stack.pop();
            stackToPrint.push(currentNode);
            if(currentNode.left!=null){
                stack.push(currentNode.left);
            }
            if(currentNode.right!=null){
                stack.push(currentNode.right);
            }
        }
        while(!stackToPrint.isEmpty()){
            PostOrder+=stackToPrint.pop().value+", ";
        }
        return PostOrder.substring(0,PostOrder.length()-2);
    }

    private Node<T> findNodeByValue(T value) {
        Node<T> current = root;
        while (current != null) {
            int compare = value.compareTo(current.value);
            if (compare == 0) {
                return current;
            } else if (compare < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    Node<T> findSuccessor(Node<T> node){
        Node<T> currentNode = node;
        if(node==null){
            return currentNode;
        }
        while (currentNode.left!=null){
            currentNode=currentNode.left;
        }
        return currentNode;
    }

    public List<T> toListInOrder() {
        List<T> list = new ArrayList<>();
        if(root==null) return list;
        Stack<Node<T>> stack = new Stack<>();
        Node<T> currentNode = root;
        while(!stack.isEmpty() || currentNode!=null){
            while(currentNode!=null){
                stack.push(currentNode);
                currentNode=currentNode.left;
            }
            currentNode=stack.pop();
            list.add(currentNode.value);
            currentNode=currentNode.right;
        }
        return list;
    }



    private void replaceValues(Node<T> firstNode, Node<T> secondNode){
        T tmp = firstNode.value;
        firstNode.value=secondNode.value;
        secondNode.value=tmp;
    }


    //moze sie przyda
    private String toStringReverseInOrder() {
        String InOrder = "";
        if(root==null) return InOrder;
        Stack<Node<T>> stack = new Stack<>();
        Node<T> currentNode = root;
        while(!stack.isEmpty() || currentNode!=null){
            while(currentNode!=null){
                stack.push(currentNode);
                currentNode=currentNode.right;
            }
            currentNode=stack.pop();
            InOrder+=currentNode.value+", ";
            currentNode=currentNode.left;
        }
        return InOrder.substring(0,InOrder.length()-2);
        // TODO: Zwróć wartość String reprezentującą drzewo po przejściu metodą in-order.
    }
}
