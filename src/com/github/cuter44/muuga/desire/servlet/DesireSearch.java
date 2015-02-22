package com.github.cuter44.muuga.desire.servlet;

import java.io.*;
import java.util.List;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.*;
import org.hibernate.*;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.desire.core.*;
import com.github.cuter44.muuga.conf.Configurator;

/** 搜索心愿/需求
 * <pre style="font-size:12px">

   <strong>请求</strong>
   GET/POST /desire/search.api

   <strong>参数</strong>
   id           :long[]             , 逗号分隔, id
   isbn         :string             , ISBN-13
   originator   :long               , 需求的从属者
   expense      :double[2]          , 逗号分隔, 表示一个左开右闭区间, 单值时逗号不可省略, 期望价.
   <del>expenseSt    :double        , 价格, 左开区间匹配</del> 已被 expense 取代
   <del>expenseEd    :double        , 价格, 右闭区间匹配</del> 已被 expense 取代
   tm           :unix-time-ms[2]    , 逗号分隔, 表示一个左开右闭区间, 单值时逗号不可省略, 时间戳.
   <del>tmSt         :long          , 时间戳, 左开区间匹配</del>
   <del>tmEd         :long          , 时间戳, 右闭区间匹配</del>
   pos          :geohash            , 地理位置标签, 作前缀匹配(LIKE :pos%)
   posExd       :geohash            , 地理位置标签, 作除外前缀匹配(NOT LIKE :posExd%)
   (使用 pos 和 posExd 逐渐缩短参数长度可以进行渐进的附近搜索)
   clazz        :string             , 需求类型, 可选值如下:
   <i>分页</i>
   start    :int                , 返回结果的起始笔数, 缺省从 0 开始
   size     :int                , 返回结果的最大笔数, 缺省使用服务器配置
   <i>排序</i>
   by       :string             , 按该字段...
   order    :string=asc|desc    , 顺序|逆序排列

   <strong>响应</strong>
   application/json; array; class={@link Json#jsonizeDesire(Desire, JSONObject) desire.model.LendDesire}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>
 * </pre>
 *
 */
@WebServlet("/desire/search.api")
public class DesireSearch extends HttpServlet
{
    private static final String ID          = "id";
    private static final String ISBN        = "isbn";
    private static final String ORIGINATOR  = "originator";
    private static final String EXPENSE     = "expense";
    //private static final String EXPENSE_ST  = "expenseSt";
    //private static final String EXPENSE_ED  = "expenseEd";
    private static final String TM          = "tm";
    //private static final String TM_ST       = "tmSt";
    //private static final String TM_ED       = "tmEd";
    private static final String POS         = "pos";
    private static final String POS_EXD     = "posExd";
    private static final String CLAZZ       = "clazz";

    private static final String START   = "start";
    private static final String SIZE    = "size";

    private static final String ORDER   = "order";
    private static final String BY      = "by";

    private static final Integer defaultPageSize = Configurator.getInstance().getInt("nyafx.search.defaultpagesize", 20);

    protected DesireDao desireDao = DesireDao.getInstance();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            DetachedCriteria dc = DetachedCriteria.forClass(Desire.class);

            List<Long>  id          = getLongList(req, ID);
            String      isbn        = getString(req, ISBN);
            Long        originator  = getLong(req, ORIGINATOR);
            Double[]    expense     = getDoubleArray(req, EXPENSE);
            Date[]      tm          = getDateArray(req, TM);
            String      pos         = getString(req, POS);
            String      posExd      = getString(req, POS_EXD);
            String      clazz       = getString(req, CLAZZ);

            Integer start   = getInt(req, START);
            Integer size    = getInt(req, SIZE);
                    size    = size!=null?size:defaultPageSize;
            String  order   = getString(req, ORDER);
            String  by      = getString(req, BY);

            if (id != null)
                dc.add(Restrictions.in("id", id));

            if (isbn != null)
                dc.add(Restrictions.eq("isbn", isbn));

            if (originator != null)
                dc.createCriteria("originator")
                    .add(Restrictions.eq("id", originator));

            if (expense != null)
            {
                if (expense[0] != null)
                    dc.add(Restrictions.gt("expense", expense[0]));
                if (expense[1] != null)
                    dc.add(Restrictions.le("expense", expense[1]));
            }

            if (tm != null)
            {
                if (expense[0] != null)
                    dc.add(Restrictions.gt("tm", tm[0]));
                if (expense[1] != null)
                    dc.add(Restrictions.le("tm", tm[1]));
            }

            if (pos != null)
                dc.add(Restrictions.like("pos", pos+"%"));

            if (posExd != null)
                dc.add(
                    Restrictions.not(
                        Restrictions.like("pos", posExd+"%")
                ));

            if (clazz != null)
                dc.add(Restrictions.eq("clazz", clazz));


            if ("asc".equals(order))
                dc.addOrder(Order.asc(by));
            if ("desc".equals(order))
                dc.addOrder(Order.desc(by));

            this.desireDao.begin();

            List<Desire> l = (List<Desire>)this.desireDao.search(dc, start, size);

            Json.writeDesire(l, resp);

            this.desireDao.commit();
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
