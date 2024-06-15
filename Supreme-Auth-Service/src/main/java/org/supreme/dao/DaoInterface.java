package org.supreme.dao;

public interface DaoInterface <T>{

    public void insert(T data);

    public T fetch(String hashKey);


}
