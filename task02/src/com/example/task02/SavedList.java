package com.example.task02;

import java.io.File;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SavedList<E extends Serializable> extends AbstractList<E> {

    private final File file;
    private final List<E> data = new ArrayList<>();

    public SavedList(File file) {
        this.file = file;
        loadFromFile();
    }

    @Override
    public E get(int index) {
        return data.get(index);
    }

    @Override
    public E set(int index, E element) {
        E old = data.set(index, element);
        saveToFile();
        return old;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public void add(int index, E element) {
        data.add(index, element);
        saveToFile();
    }

    @Override
    public E remove(int index) {
        E removed = data.remove(index);
        saveToFile();
        return removed;
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        if (file == null || !file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                data.clear();
                data.addAll((List<E>) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            data.clear();
        }
    }

    private void saveToFile() {
        if (file == null) {
            return;
        }

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(data));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save list to file: " + file, e);
        }
    }
}