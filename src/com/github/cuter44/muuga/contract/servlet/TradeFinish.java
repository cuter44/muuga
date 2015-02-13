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
import com.github.cuter44.muuga.desire.core.*;

/** 确认归还书籍
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /contract/trade/finish.api

   <strong>参数</strong>
   uid      :long, 自己的 uid, 作为交易的参与方
   id       :long, 应答的 desire id,
   <i>鉴权</i>
   uid  :long   , 必需, uid
   s    :hex    , 必需, session key

   <strong>响应</strong>
   application/json, class={@link Json#jsonizeContractBase(ContractBase, JSONObject) contract.model.TeadeContract}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/contract/trade/finish.api")
public class TradeFinish extends HttpServlet
{
    private static final String UID     = "uid";
    private static final String ID      = "id";
    private static final String BOOK    = "book";

    protected TradeContractDao tradeDao = TradeContractDao.getInstance();
    protected TradeController tradeCtl  = TradeController.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long    uid     = needLong(req, UID);
            Long    id      = needLong(req, ID);

            this.tradeDao.begin();

            TradeContract trade = this.tradeCtl.finish(id, uid);

            this.tradeDao.commit();

            Json.writeContractBase(trade, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.tradeDao.close();
        }

        return;
    }
}
