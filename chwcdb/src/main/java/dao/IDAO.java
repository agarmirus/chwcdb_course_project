package dao;

import java.util.List;
import java.util.Optional;

import javafx.util.Pair;

import appexception.CHWCDBException;

public interface IDAO<T>
{
    public void setConnection(String url, String user, String pswd) throws CHWCDBException;

    public Optional<T> get(final T entity) throws CHWCDBException;
    public Optional<List<T>> get(final List<T> entities) throws CHWCDBException;
    public Optional<List<T>> get(String attributeName, String value) throws CHWCDBException;

    public void create(final T entity) throws CHWCDBException;
    public void create(final List<T> entities) throws CHWCDBException;

    public void update(final T entity) throws CHWCDBException;
    public void update(final List<T> entities) throws CHWCDBException;
    public void update(final T entity, String attributeName, String value) throws CHWCDBException;
    public void update(final T entity, List<Pair<String, String>> updates) throws CHWCDBException;
    public void update(final T entity, String attributeName, final int delta) throws CHWCDBException;

    public void delete(final T entity) throws CHWCDBException;

    public void close() throws CHWCDBException;
}
