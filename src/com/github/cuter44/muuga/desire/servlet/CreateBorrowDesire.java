package com.github.cuter44.muuga.desire.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.needLong;
import static com.github.cuter44.nyafx.servlet.Params.needString;
import static com.github.cuter44.nyafx.servlet.Params.getDouble;
import static com.github.cuter44.nyafx.servlet.Params.getInt;
import static com.github.cuter44.nyafx.servlet.Params.getString;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.desire.core.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

/** 增加借入意愿
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /desire/borrow/create.api

   <strong>参数</strong>
   uid      :long           , 必需, 购买意愿的发起者
   isbn     :string         , 必需, 购买意愿的 isbn
   expense  :double(0.2)    , 接受的押金(由于无法使用电子支付, 该值暂不起作用)
   qty      :integer        , 需求量
   ps       :string(255)    , 附言
   pos      :geohash(24)    , 地理位置标签
   <i>鉴权</i>
   uid  :long   , 必需, uid
   s    :hex    , 必需, session key

   <strong>响应</strong>
   application/json class=desire.model.LendDesire
   rendered by Json#jsonizeBuyDesire

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 *
 */
@WebServlet("/desire/borrow/create.api")
public class CreateBorrowDesire extends HttpServlet
{
    private static final String UID     = "uid";
    private static final String ISBN    = "isbn";
    private static final String EXPENSE = "expense";
    private static final String QTY     = "qty";
    private static final String PS      = "ps";
    private static final String POS     = "pos";

    protected BorrowDesireDao desireDao = BorrowDesireDao.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long    uid     = needLong(req, UID);
            String  isbn    = needString(req, ISBN);
            Double  expense = getDouble(req, EXPENSE);
            Integer qty     = getInt(req, QTY);
            String  ps      = getString(req, PS);
            String  pos     = getString(req, POS);

            this.desireDao.begin();

            BorrowDesire   d   = this.desireDao.create(uid, isbn, expense, qty, ps, pos);

            this.desireDao.commit();

            Json.writeDesire(d, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.desireDao.close();
        }

        return;
    }
}
