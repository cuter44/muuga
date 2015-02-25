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
import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.contract.core.*;
import com.github.cuter44.muuga.desire.core.*;

/** (借阅方)要求归还书籍
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /contract/loan/return.api
   POST /contract/loan/return-req.api

   <strong>参数</strong>
   uid      :long, 自己的 uid, 作为交易的参与方
   id      :long, 借阅的 id
   <i>鉴权</i>
   uid  :long   , 必需, uid
   s    :hex    , 必需, session key

   <strong>响应</strong>
   application/json, class={@link Json#jsonizeContractBase(ContractBase, JSONObject) contract.model.LoanContract}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet({
    "/contract/loan/return.api",
    "/contract/loan/return-req.api"
})
public class LoanReturn extends HttpServlet
{
    private static final String UID     = "uid";
    private static final String ID      = "id";

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

            this.loanDao.begin();

            LoanContract loan = this.loanCtl.returnReq(id, uid);

            this.loanDao.commit();

            Json.writeContractBase(loan, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(KEY_EXCEPTION, ex);
            req.getRequestDispatcher(URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.loanDao.close();
        }

        return;
    }
}
