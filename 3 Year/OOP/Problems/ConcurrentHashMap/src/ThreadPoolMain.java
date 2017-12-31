/**
 * Created by Masha Kereb on 11-Dec-16.
 */

import java.util.ArrayList;
import java.util.concurrent.*;

public class ThreadPoolMain {
    static ArrayList<Future<Integer>> result = new ArrayList<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPool<Integer> threadPool = new ThreadPool<>(8);

        for (int t = 0; t < 4; t++) {
            result.add(threadPool.submit(new Task(t)));
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int t = 0; t < result.size(); t++) {
                    try {
                        System.out.println(result.get(t).get());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        threadPool.shutdown();
    }
}

class Task implements Callable<Integer> {
    int i;

    public Task(int t) {
        i = t;
    }

    @Override
    public Integer call() throws Exception {
        Thread.sleep(1000 * (i + 1));
        System.out.println("Task: " + i);
        return i;
    }
}

class ThreadPool<T> {
    private ArrayList<Pair<Callable<T>, Future<T>>> queue = new ArrayList<>();
    private ArrayList<Executor> executor = new ArrayList<>();

    public ThreadPool(int s) {
        for (int i = 0; i < s; i++) {
            executor.add(new Executor(queue));
            executor.get(i).start();
        }
    }

    public Future<T> submit(Callable<T> task) {
        Future<T> future = new Result();
        synchronized (queue) {
            queue.add(new Pair(task, future));
            queue.notify();
        }
        return future;
    }

    public void shutdown() {
        for (Executor e : executor)
            e.shutdown();
        synchronized (queue) {
            queue.notify();
        }
        for (Executor e : executor)
            try {
                e.join();
            } catch (Exception ex) {
            }
    }

    class Executor extends Thread {
        private ArrayList<Pair<Callable<T>, Future<T>>> queue;
        private boolean finish = false;
        Future<T> future;

        public Executor(ArrayList<Pair<Callable<T>, Future<T>>> q) {
            queue = q;
        }

        @Override
        public void run() {
            while (!finish || queue.size() > 0) {
                Callable<T> task;
                synchronized (queue) {
                    try {
                        while (queue.size() == 0 && !finish) {
                            queue.wait();
                        }
                        if (queue.size() == 0 && finish) {
                            queue.notify();
                            System.out.println("finished");
                            return;
                        }
                    } catch (Exception e) {
                    }
                    Pair<Callable<T>, Future<T>> pair = queue.get(0);
                    queue.remove(0);
                    task = pair.getLeft();
                    future = pair.getRight();
                    queue.notify();
                }
                try {
                    T result = task.call();
                    ((Result) future).setResult(result);
                } catch (Exception e) {
                }
            }
            System.out.println("finished");
        }

        public void shutdown() {
            finish = true;
        }
    }

    class Result implements Future {
        T result = null;
        boolean done = false;
        Object flag = new Object();

        void setResult(T r) {
            result = r;
            synchronized (flag) {
                done = true;
                flag.notify();
            }
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            synchronized (flag) {
                while (!isDone()) {
                    flag.wait();
                }
            }
            return result;
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }
    }

    class Pair<L, R> {

        private final L left;
        private final R right;

        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        @Override
        public int hashCode() {
            return left.hashCode() ^ right.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair)) return false;
            Pair pairo = (Pair) o;
            return this.left.equals(pairo.getLeft()) &&
                    this.right.equals(pairo.getRight());
        }

    }
}
