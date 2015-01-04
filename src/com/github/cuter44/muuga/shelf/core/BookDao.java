package com.github.cuter44.muuga.shelf.core;

import java.util.Date;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.shelf.model.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.model.*;

public class BookDao extends DaoBase<Book>
{
  // CONSTRUCT
    public BookDao()
    {
        super();
    }

  // SINGLETON
    private static class Singleton
    {
        public static BookDao instance = new BookDao();
    }

    public static BookDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(Book.class);
    }

  // EXTENDED
    public Book create(String isbn, Profile owner)
    {
        Book b = new Book(isbn, owner);
        b.setRegDate(new Date(System.currentTimeMillis()));

        this.save(b);

        return(b);
    }

    public void softRemove(Long id)
    {
        Book b = this.get(id);

        b.setStatus(Book.REMOVED);

        this.update(b);

        return;
    }

    public boolean isOwnedBy(Long bookId, Long userId)
        throws EntityNotFoundException
    {
        Book b = (Book)entFound(this.get(bookId));
        return(b.ownedBy(userId));
    }

}
