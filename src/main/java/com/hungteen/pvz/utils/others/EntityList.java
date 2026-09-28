package com.hungteen.pvz.utils.others;

import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class EntityList<T extends Entity> extends ArrayList<T> {

    private static final Predicate<Entity> IS_DEAD = entity -> entity == null || ! entity.isAlive();

    public boolean cleanDead() {
        return this.removeIf(IS_DEAD);
    }

    //clear 语义在本容器中是"连同实体一起回收"：只丢弃引用会让已离开列表的实体继续留在世界里。
    @Override
    public void clear() {
        for (T entity : this) {
            if (! IS_DEAD.test(entity)) {
                entity.discard();
            }
        }
        super.clear();
    }

    @Override
    public Iterator<T> iterator() {
        this.cleanDead();
        return super.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        this.cleanDead();
        super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        this.cleanDead();
        return super.spliterator();
    }

    @Override
    public Stream<T> stream() {
        this.cleanDead();
        return super.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        this.cleanDead();
        return super.parallelStream();
    }

    @Override
    public ListIterator<T> listIterator() {
        this.cleanDead();
        return super.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        this.cleanDead();
        return super.listIterator(Math.min(index, this.size()));
    }

    @Override
    public int size() {
        this.cleanDead();
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        this.cleanDead();
        return super.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        this.cleanDead();
        return super.contains(o);
    }

    @Override
    public T get(int index) {
        this.cleanDead();
        if (index < 0 || index >= super.size()) {
            return null;
        }
        return super.get(index);
    }

    @Override
    public Object[] toArray() {
        this.cleanDead();
        return super.toArray();
    }

    @Override
    public <E> E[] toArray(E[] a) {
        this.cleanDead();
        return super.toArray(a);
    }
}