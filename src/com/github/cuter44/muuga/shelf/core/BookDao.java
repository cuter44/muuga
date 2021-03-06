package com.github.cuter44.muuga.shelf.core;

import java.util.Date;

import org.hibernate.criterion.*;
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

        this.profileDao = ProfileDao.getInstance();

        return;
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
    public Book get(Long ownerId, String isbn)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(Book.class)
            .createAlias("owner", "owner")
            .add(Restrictions.eq("owner.id", ownerId))
            .add(Restrictions.eq("isbn", isbn));

        return(
            this.get(dc)
        );
    }

    public Book create(Long ownerId, String isbn)
    {
        Profile owner   = (Profile)entFound(this.profileDao.get(ownerId));
        Book    book    = new Book(isbn, owner);

        book.setRegDate(new Date(System.currentTimeMillis()));

        this.save(book);

        return(book);
    }

    public Book getOrCreate(Long ownerId, String isbn)
    {
        Book b = this.get(ownerId, isbn);
        if (b != null)
            return(b);

        // else
        return(
            this.create(ownerId, isbn)
        );
    }

    public void remove(Long ownerId, String isbn)
    {
        Book b = this.get(ownerId, isbn);

        this.delete(b);

        return;
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
        return(book.isOwnedBy(userId));
    }

}
