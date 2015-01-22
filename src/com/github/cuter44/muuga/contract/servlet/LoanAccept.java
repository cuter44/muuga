package com.github.cuter44.muuga.contract.servlet;

import java.io.*;
import java.security.PrivateKey;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.notNull;
import static com.github.cuter44.nyafx.servlet.Params.getLong;
import static com.github.cuter44.nyafx.servlet.Params.needLong;

//import com.github.cuter44.muuga.util.conf.*;
import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.contract.core.*;

/**接受发起的借阅
 * 借阅从该步骤之后无法被撤销
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /contract/loan/accept.api

   <strong>参数</strong>
   uid      :long, 自己的 uid, 作为交易的参与方
   id       :long, 接受的交易 id
   book     :long, 在应答一个借阅方发起交易时可选, 要出借的书的id, 用于标记为出借
   <i>鉴权</i>
   uid  :long   , 必需, uid

   <strong>响应</strong>
   application/json, class=contract.model.LoanContract
   attributes refer to {@link Json#jsonizeUserPrivate(ContractBase) Json}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/contract/loan/accept.api")
public class LoanAccept extends HttpServlet
{
    private static final String UID     = "uid";
    private static final String ID      = "id";
    private static final String BOOK    = "book";

    protected LoanContractDao loanDao = LoanContractDao.getInstance();
    protected LoanController loanCtl  = LoanController.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long    uid     = needLong(req, UID);
            Long    id      = needLong(req, ID);
            Long    book    = getLong(req, BOOK);

            this.loanDao.begin();

            LoanContract trade = this.loanCtl.accept(id, uid, book);

            this.loanDao.commit();

            Json.writeContractBase(trade, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.loanDao.close();
        }

        return;
    }
}
