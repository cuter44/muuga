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
    protected ProfileDao profileDao;

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
    public Book create(Long ownerId, String isbn)
    {
        Profile owner   = (Profile)entFound(this.profileDao.get(ownerId));
        Book    book    = new Book(isbn, owner);

        book.setRegDate(new Date(System.currentTimeMillis()));

        this.save(book);

        return(book);
    }

    //public void softRemove(Long id)
    //{
        //Book book = this.get(id);

        //book.setStatus(Book.STATUS_REMOVED);

        //this.update(book);

        //return;
    //}

    public boolean isOwnedBy(Long bookId, Long userId)
        throws EntityNotFoundException
    {
        Book book = (Book)entFound(this.get(bookId));
        return(book.ownedBy(userId));
    }

}
