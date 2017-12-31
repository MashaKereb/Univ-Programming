/**
 * Created by Masha Kereb on 11-Dec-16.
 */


import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkipListMain {
    static SkipList skipList;
    static Random rnd;
    static int t;

    public static void main(String[] args) {
        skipList = new SkipList();
        rnd = new Random();

        ExecutorService exec = Executors.newFixedThreadPool(4);
        try {
            for (t = 0; t < 4; t++) {
                exec.submit(new Runnable(){
                    @Override
                    public void run() {
                        int num = t;
                        for (int i = 0; i < 10 * (num + 1); i++) {
                            int n = rnd.nextInt(10 * (num + 1));
                            skipList.add(n);
                            System.out.println(n);
                        }
                    }
                });
            }
        } finally {
            exec.shutdown();
        }
    }
}

class SkipList {
    private int height = 5;
    private Node head;
    private float probability = 0.5f;

    public SkipList() {
        head = new Node(Integer.MAX_VALUE);
        for (int i = 0; i < height; i++)
            head.next.add(null);
    }

    protected class Node {
        ArrayList<Node> next = null;
        int val;
        AtomicBoolean busy = new AtomicBoolean(false);

        public Node(int v) {
            val = v;
            next = new ArrayList<>();
        }

        public void print() {
            System.out.println(val);
        }
    }

    public void add(int val) {
        Node newNode = new Node(val);
        addToNextLevel(0, newNode);
        int level = 1;
        Random rnd = new Random();
        while (rnd.nextFloat() < probability && level < height) {
            addToNextLevel(level, newNode);
            level++;
        }
        if (head.next.get(0).next.get(0) == null)
            for (int i = level; i < height; i++)
                addToNextLevel(i, newNode);
    }

    private void addToNextLevel(int minLevel, Node newNode) {
        boolean error;
        do {
            error = false;
            Node curr = head;
            int level = height - 1;
            while (level >= minLevel) {
                while (curr.next.get(level) != null && curr.next.get(level).val > newNode.val)
                    curr = curr.next.get(level);
                level--;
            }
            level++;

            //grab
            while (!curr.busy.compareAndSet(false, true)) ;

            Node change = curr;
            curr = head;
            level = height - 1;
            while (level >= minLevel) {
                while (curr.next.get(level) != null && curr.next.get(level).val > newNode.val)
                    curr = curr.next.get(level);
                level--;
            }
            level++;

            if (change == curr) {
                newNode.next.add(curr.next.get(level));
                curr.next.set(level, newNode);
            } else error = true;
            //release
            while (!change.busy.compareAndSet(true, false)) ;
        } while (error);
    }

    public void delete(int val) {
        Node toDelete = find(val);
        if (toDelete != null) {
            for (int minLevel = 0; minLevel < height; minLevel++) {
                boolean error;
                do {
                    error = false;
                    Node curr = head;
                    int level = height - 1;
                    while (level >= minLevel) {
                        while (curr.next.get(level) != null && curr.next.get(level).val > val)
                            curr = curr.next.get(level);
                        level--;
                    }
                    level++;

                    //grab
                    while (!curr.busy.compareAndSet(false, true)) ;

                    Node change = curr;
                    curr = head;
                    level = height - 1;
                    while (level >= minLevel) {
                        while (curr.next.get(level) != null && curr.next.get(level).val > val)
                            curr = curr.next.get(level);
                        level--;
                    }
                    level++;

                    if (change == curr) {
                        if (curr.next.get(level) != null && curr.next.get(level).val == val)
                            curr.next.set(level, curr.next.get(level).next.get(level));
                        else {
                            //release
                            while (!curr.busy.compareAndSet(true, false)) ;
                            return;
                        }
                    } else error = true;

                    //release
                    while (!change.busy.compareAndSet(true, false)) ;
                } while (error);
            }
        }
    }

    public Node find(int val) {
        Node curr = head;
        int level = height - 1;
        Node founded = null;
        while (level >= 0 && (founded == null || founded.val != val)) {
            while (curr.next.get(level) != null && curr.next.get(level).val > val)
                curr = curr.next.get(level);
            founded = curr.next.get(level);
            level--;
        }
        level++;
        if (founded != null && founded.val == val)
            return founded;
        return null;
    }

    public void print() {
        Node curr = head.next.get(0);
        while (curr != null) {
            curr.print();
            curr = curr.next.get(0);
        }
    }
}

