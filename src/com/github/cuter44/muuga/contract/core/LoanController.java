package com.github.cuter44.muuga.contract.core;

import com.github.cuter44.nyafx.dao.EntityNotFoundException;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.contract.core.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.desire.core.*;
import com.github.cuter44.muuga.shelf.model.*;
import com.github.cuter44.muuga.shelf.core.*;
import com.github.cuter44.muuga.user.exception.*;

public class LoanController
{

  // CONSTRUCT
    protected LoanContractDao        loanDao;
    protected BorrowerInitedLoanDao bLoanDao;
    protected LenderInitedLoanDao   lLoanDao;
    protected LoanDesireDao          desireDao;
    protected BorrowDesireDao       bDesireDao;
    protected LendDesireDao         lDesireDao;
    protected BookDao               bookDao;

    public LoanController()
    {
        this.loanDao     = LoanContractDao.getInstance();
        this.bLoanDao    = BorrowerInitedLoanDao.getInstance();
        this.lLoanDao    = LenderInitedLoanDao.getInstance();
        this.desireDao   = LoanDesireDao.getInstance();
        this.bDesireDao  = BorrowDesireDao.getInstance();
        this.lDesireDao  = LendDesireDao.getInstance();
        this.bookDao     = BookDao.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static LoanController instance = new LoanController();
    }

    public static LoanController getInstance()
    {
        return(
            Singleton.instance
        );
    }

  // CREATE
    public LoanContract create(Long desireId, Long uid, Long bookId)
    {
        LoanDesire desire  = (LoanDesire)entFound(this.desireDao.get(desireId));

        LoanContract loan = null;

        //if (this.bDesireDao.get(desire) != null)
        if (desire instanceof BorrowDesire)
            loan    = this.lenderInitLoan(desireId, uid, bookId);

        //if (this.sDesireDao.get(desire) != null)
        if (desire instanceof LendDesire)
            loan    = this.borrowerInitLoan(desireId, uid);

        return(loan);
    }

    protected LenderInitedLoan lenderInitLoan(Long desireId, Long uid, Long bookId)
    {
        LenderInitedLoan loan = this.lLoanDao.create(desireId, uid, bookId);

        // TODO notify

        return(loan);
    }

    protected BorrowerInitedLoan borrowerInitLoan(Long desireId, Long uid)
    {
        BorrowerInitedLoan loan = this.bLoanDao.create(desireId, uid);

        // TODO notify

        return(loan);
    }

  // ACCEPT
    public LoanContract accept(Long loanId, Long uid, Long bookId)
        throws IllegalStateException, UnauthorizedException
    {
        LoanContract loan = (LoanContract)entFound(this.loanDao.get(loanId));

        if (!LoanContract.STATUS_INIT.equals(loan.getStatus()))
            throw(new IllegalStateException("Loan not on STATUS_INIT:loanId="+loanId));

        if (loan instanceof BorrowerInitedLoan)
            this.lenderAccept(loanId, uid, bookId);

        if (loan instanceof LenderInitedLoan)
            this.borrowerAccept(loanId, uid);

        return(loan);
    }

    protected BorrowerInitedLoan lenderAccept(Long loanId, Long uid, Long bookId)
        throws IllegalStateException, UnauthorizedException
    {
        BorrowerInitedLoan loan = (BorrowerInitedLoan)entFound(this.bLoanDao.get(loanId));

        if (!BorrowerInitedLoan.STATUS_INIT.equals(loan.getStatus()))
            throw(new IllegalStateException("Loan not on STATUS_INIT:loanId="+loanId));

        if (!loan.isSuppliedBy(uid))
            throw(new UnauthorizedException("Loan not supplied by current user:loanId="+loanId+",uid="+uid));

        Book book = bookId!=null ? this.bookDao.get(bookId) : null;
        if ((book!=null) && !book.isOwnedBy(uid))
            throw(new UnauthorizedException("Book not owned by current user:bookId="+bookId+",uid="+uid));

        loan.setStatus(BorrowerInitedLoan.STATUS_ACKED);
        loan.setBook(book);

        this.bLoanDao.update(loan);

        // TODO notify

        return(loan);
    }

    protected LenderInitedLoan borrowerAccept(Long loanId, Long uid)
        throws IllegalStateException, UnauthorizedException
    {
        LenderInitedLoan loan = (LenderInitedLoan)entFound(this.lLoanDao.get(loanId));

        if (!LenderInitedLoan.STATUS_INIT.equals(loan.getStatus()))
            throw(new IllegalStateException("Loan not on STATUS_INIT:loanId="+loanId));

        if (!loan.isConsumedBy(uid))
            throw(new UnauthorizedException("Loan not consumed by current user:loanId="+loanId+",uid="+uid));

        loan.setStatus(LenderInitedLoan.STATUS_ACKED);

        this.lLoanDao.update(loan);

        // TODO notify

        return(loan);
    }

  // QUIT
    public LoanContract quit(Long loanId, Long uid)
        throws IllegalStateException
    {
        LoanContract loan = (LoanContract)entFound(this.loanDao.get(loanId));

        if (!LoanContract.STATUS_INIT.equals(loan.getStatus()))
            throw(new IllegalStateException("Loan not on STATUS_INIT:loanId="+loanId));

        if (loan.isConsumedBy(uid))
        {
            loan.setStatus(LoanContract.STATUS_CONSUME_QUIT);
            // TODO notify
        }

        if (loan.isSuppliedBy(uid))
        {
            loan.setStatus(LoanContract.STATUS_SUPPLY_QUIT);
            // TODO notify
        }

        this.loanDao.update(loan);

        return(loan);
    }

  // DELIVERED
    public LoanContract deliever(Long loanId, Long uid)
        throws UnauthorizedException, IllegalStateException
    {
        LoanContract loan = (LoanContract)entFound(this.loanDao.get(loanId));

        if (!loan.isSuppliedBy(uid))
            throw(new UnauthorizedException("Loan not supplied by current user:loanId="+loanId+",uid="+uid));

        if (!LoanContract.STATUS_ACKED.equals(loan.getStatus()))
            throw(new IllegalStateException("Loan not on STATUS_ACKED or STATUS_PAYED:loanId="+loanId));

        loan.setStatus(LoanContract.STATUS_DELIEVERED);

        this.loanDao.update(loan);

        // TODO notify

        return(loan);
    }

  // DELIVERE_ACK
    public LoanContract delieverAck(Long loanId, Long uid)
        throws UnauthorizedException, IllegalStateException
    {
        LoanContract loan = (LoanContract)entFound(this.loanDao.get(loanId));

        if (!loan.isConsumedBy(uid))
            throw(new UnauthorizedException("Loan not consumed by current user:loanId="+loanId+",uid="+uid));

        if (!LoanContract.STATUS_DELIEVERED.equals(loan.getStatus()))
            throw(new IllegalStateException("Loan not on STATUS_DELIEVERED:loanId="+loanId));

        loan.setStatus(LoanContract.STATUS_DELIEVER_ACK);

        this.loanDao.update(loan);

        // TODO notify

        return(loan);
    }

  // RETURN
    public LoanContract returnReq(Long loanId, Long uid)
        throws UnauthorizedException, IllegalStateException
    {
        LoanContract loan = (LoanContract)entFound(this.loanDao.get(loanId));

        if (!loan.isConsumedBy(uid))
            throw(new UnauthorizedException("Loan not consumed by current user:loanId="+loanId+",uid="+uid));

        if (!LoanContract.STATUS_DELIEVER_ACK.equals(loan.getStatus()))
            throw(new IllegalStateException("Loan not on STATUS_DELIEVER_ACK:loanId="+loanId));

        loan.setStatus(LoanContract.STATUS_RETURN_REQ);

        this.loanDao.update(loan);

        // TODO notify

        return(loan);
    }

  // FINISH
    public LoanContract returnAck(Long loanId, Long uid)
    {
        return(
            this.finish(loanId, uid)
        );
    }
    public LoanContract finish(Long loanId, Long uid)
        throws UnauthorizedException, IllegalStateException
    {
        LoanContract loan = (LoanContract)entFound(this.loanDao.get(loanId));

        if (!loan.isSuppliedBy(uid))
            throw(new UnauthorizedException("Loan not supplied by current user:loanId="+loanId+",uid="+uid));

        Byte status = loan.getStatus();
        if (!LoanContract.STATUS_RETURN_REQ.equals(loan.getStatus()))
            throw(new IllegalStateException("Loan not on STATUS_RETURN_REQ:loanId="+loanId));

        loan.setStatus(LoanContract.STATUS_FINISH);

        this.loanDao.update(loan);

        // TODO notify

        return(loan);
    }
}
