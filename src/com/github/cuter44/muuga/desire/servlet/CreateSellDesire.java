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
import static com.github.cuter44.nyafx.servlet.Params.needDouble;
import static com.github.cuter44.nyafx.servlet.Params.getInt;
import static com.github.cuter44.nyafx.servlet.Params.getString;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.desire.core.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

/** 增加出售意愿
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /desire/sell/create.api

   <strong>参数</strong>
   uid      :long           , 必需, 出售意愿的发起者
   isbn     :string         , 必需, 出售的 isbn
   expense  :double(0.2)    , 必需, 出售的价格
   qty      :integer        , 购买量
   ps       :string(255)    , 附言
   pos      :geohash(24)    , 地理位置标签
   <i>鉴权</i>
   uid  :long   , 必需, uid
   s    :hex    , 必需, session key

   <strong>响应</strong>
   application/json class={@link Json#jsonizeDesire(Desire, JSONObject) desire.model.LendDesire}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 *
 */
@WebServlet("/desire/sell/create.api")
public class CreateSellDesire extends HttpServlet
{
    private static final String UID     = "uid";
    private static final String ISBN    = "isbn";
    private static final String EXPENSE = "expense";
    private static final String QTY     = "qty";
    private static final String PS      = "ps";
    private static final String POS     = "pos";

    protected SellDesireDao desireDao = SellDesireDao.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long    uid     = needLong(req, UID);
            String  isbn    = needString(req, ISBN);
            Double  expense = needDouble(req, EXPENSE);
            Integer qty     = getInt(req, QTY);
            String  ps      = getString(req, PS);
            String  pos     = getString(req, POS);

            this.desireDao.begin();

            SellDesire  d   = this.desireDao.create(uid, isbn, expense, qty, ps, pos);

            this.desireDao.commit();

            Json.writeDesire(d, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(KEY_EXCEPTION, ex);
            req.getRequestDispatcher(URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.desireDao.close();
        }

        return;
    }
}
