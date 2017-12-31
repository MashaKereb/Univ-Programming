import java.util.LinkedList;

/**
 * Created by Masha Kereb on 05-Dec-16.
 */
public class ConcurrentHashMap<T>{

    final int capacity;
    Node[] nodes;

    class Node<T>{
        LinkedList<T> objects;

        Node(){
            objects=new LinkedList<T>();
        }

        synchronized void add(T e){
            objects.add(e);
            System.out.println(Thread.currentThread().getName()+" add "+e.toString());
        }

        synchronized boolean remove(T e){
            System.out.println(Thread.currentThread().getName()+" remove "+e.toString());
            return objects.remove(e);
        }

        synchronized int size(){
            return objects.size();
        }

        @Override
        public String toString(){
            String res="";
            for (T element:objects) {
                res+=element.toString()+" ";
            }
            return res;
        }
    }

    ConcurrentHashMap(int capacity){
        this.capacity=capacity;
        nodes=new Node[capacity];
    }

    void add(T e){
        int index= hash(e);
        if(nodes[index]==null)
            nodes[index]=new Node();
        nodes[index].add(e);
    }

    boolean remove(T e){
        int index= hash(e);
        if(nodes[index]!=null)
            return nodes[index].remove(e);
        return  false;
    }

    int hash(T e){
        return e.hashCode()%capacity;
    }

    int size(){
        int size=0;
        for(int i=0;i<capacity;i++)
            if(nodes[i]!=null)
                size+=nodes[i].size();
        return size;
    }

    @Override
    public String toString(){
        String res="";
        for(int i=0;i<capacity;i++)
            if(nodes[i]!=null)
                res+="Node " + i +" " + nodes[i].toString() + " \n";
        return res;
    }


    }


