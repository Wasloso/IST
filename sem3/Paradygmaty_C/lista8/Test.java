public class Test {
    public static void main(String[] args) throws FullException, EmptyException {
        MyQueue<String> queue = new CyclicArrayQueue<>(3);
        queue.enqueue("rower");
        queue.enqueue("samochod");
        queue.enqueue("rolki");
        System.out.println(queue.first());
        System.out.println("czy pelna? : "+queue.isFull());
        queue.dequeue();
        queue.enqueue("deskorolka");
        System.out.println(queue.first());
        queue.dequeue();
        queue.enqueue("motocykl");
        System.out.println(queue.first());
        queue.dequeue();
        System.out.println(queue.first());
        queue.dequeue();
        System.out.println(queue.first());
        queue.dequeue();
        System.out.println("czy pusta ? : "+queue.isEmpty());
        //wyjatek System.out.println(queue.first());
    }

}
