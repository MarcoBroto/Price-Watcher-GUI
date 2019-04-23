/**
 * DropdownList.java
 * @author Marco Soto
 *
 * Concrete generic Observable list used for JavaFX Dropdown Menus.
 */

package Models;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import java.util.*;
import java.lang.Comparable;

public class DropdownList<T extends Comparable<T>> implements ObservableList {

    private ArrayList<T> items;

    public DropdownList() {
        this.items = new ArrayList<>();
    }

    public DropdownList(Object[] objects) {
        this.items = new ArrayList<>();
        for (Object o: objects) {
            if (o instanceof java.lang.Comparable) {
                this.items.add((T) o);
                System.out.println((T) o);
            }
        }
    }

    public DropdownList(Collection<T> list) {
        this.items = new ArrayList<>(list);
    }

    @Override
    public void addListener(ListChangeListener listener) {

    }

    @Override
    public void removeListener(ListChangeListener listener) {

    }

    @Override
    public boolean addAll(Object[] elements) {
        return false;
    }

    @Override
    public boolean setAll(Object[] elements) {
        return false;
    }

    @Override
    public boolean setAll(Collection col) {
        this.items = new ArrayList<T>(col);
        return true;
    }

    @Override
    public boolean removeAll(Object[] elements) {
        this.items = new ArrayList<>();
        return true;
    }

    @Override
    public boolean retainAll(Object[] elements) {
        return false;
    }

    @Override
    public void remove(int from, int to) {
        while (from < to) {
            this.items.remove(from);
            from++;
        }
    }

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.items.contains(o);
    }

    @Override
    public Iterator iterator() {
        return this.items.iterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        if ((o instanceof java.lang.Comparable)) {
            this.items.add((T) o);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        this.items.remove(o);
        return true;
    }

    @Override
    public boolean addAll(Collection c) {
        return this.items.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection c) {
        this.items.addAll(index, c);
        return true;
    }

    @Override
    public void clear() {
        this.items = new ArrayList<>();
    }

    @Override
    public Object get(int index) {
        return this.items.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        if (element instanceof java.lang.Comparable) {
            return this.items.set(index, (T) element);
        }
        return null;
    }

    @Override
    public void add(int index, Object element) {
        if (element instanceof java.lang.Comparable) {
            this.items.add((T) element);
        }
    }

    @Override
    public Object remove(int index) {
        return this.items.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.items.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.items.lastIndexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        return this.items.listIterator();
    }

    @Override
    public ListIterator listIterator(int index) {
        return this.items.listIterator(index);
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return this.items.subList(fromIndex, toIndex);
    }

    @Override
    public boolean retainAll(Collection c) {
        this.items.retainAll(c);
        return true;
    }

    @Override
    public boolean removeAll(Collection c) {
        this.items.removeAll(c);
        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        return this.items.containsAll(c);
    }

    @Override
    public Object[] toArray(Object[] a) {
        return this.items.toArray(a);
    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }
}
