package com.github.cuter44.muuga.user.model;

import java.io.Serializable;

public class IndividualUser extends User
    implements Serializable
{
    public static final long serialVersionUID = 1L;

  // CONSTRUCT
    public IndividualUser()
    {
        super();
    }

    public IndividualUser(String mail)
    {
        super(mail);
    }

  // HASH
    @Override
    public int hashCode()
    {
        int hash = 17;

        if (this.id != null)
            hash = hash * 31 + this.id.hashCode();

        return(hash);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return(true);

        if (o==null || !this.getClass().equals(o.getClass()))
            return(false);

        IndividualUser u = (IndividualUser)o;

        return(
            (this.id == u.id) ||
            (this.id != null && this.id.equals(u.id))
        );
    }
}
