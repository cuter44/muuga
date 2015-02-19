package com.github.cuter44.muuga.contract.servlet;

import java.util.List;
import java.util.Date;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.*;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.contract.core.*;
import com.github.cuter44.muuga.conf.Configurator;

/** 搜索交易
 * <pre style="font-size:12px">

   <strong>请求</strong>
   GET/POST /contract/search.api

   <strong>参数</strong>
   id           :long[]         , 逗号分隔, ID
   isbn         :string         , ISBN
   supply       :long           , 供应方 uid, 借出/卖出
   consume      :long           , 消耗方 uid, 借入/买入
   book         :long           , 关联到该交易的书籍的 id
   expense      :double[2]      , 逗号分隔, 表示一个左开右闭区间, 单值时逗号不可省略, 期望价.
   tmCreate     :unix-time-ms[2] , 逗号分隔, 表示一个左开右闭区间, 单值时逗号不可省略, 时间戳.
   tmStatus     :unix-time-ms[2] , 逗号分隔, 表示一个左开右闭区间, 单值时逗号不可省略, 时间戳.
   clazz         :string     , 需求类型, 可选值如下:
   <i>分页</i>
   start    :int        , 返回结果的起始笔数, 缺省从 0 开始
   size     :int        , 返回结果的最大笔数, 缺省使用服务器配置
   <i>排序</i>
   by       :string             , 按该字段...
   order    :string=asc|desc    , 顺序|逆序排列

   <strong>响应</strong>
   application/json; array; class={@link Json#jsonizeContractBase(ContractBase, JSONObject) desire.model.LendContractBase}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>
 * </pre>
 *
 */
@WebServlet("/contract/search.api")
public class ContractSearch extends HttpServlet
{
    private static final String ID          = "id";
    private static final String ISBN        = "isbn";
    private static final String SUPPLY      = "supply";
    private static final String CONSUME     = "consume";
    private static final String BOOK        = "book";
    private static final String EXPENSE     = "expense";
    private static final String TM_CREATE   = "tmCreate";
    private static final String TM_STATUS   = "tmStatus";
    private static final String CLAZZ       = "clazz";

    private static final String START   = "start";
    private static final String SIZE    = "size";
    private static final String ORDER   = "order";
    private static final String BY      = "by";

    private static final Integer defaultPageSize = Configurator.getInstance().getInt("nyafx.search.defaultpagesize", 20);

    protected ContractBaseDao contractDao = ContractBaseDao.getInstance();

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
            List<Long>  id          = getLongList(req, ID);
            String      isbn        = getString(req, ISBN);
            Long        supply      = getLong(req, SUPPLY);
            Long        consume     = getLong(req, CONSUME);
            Long        book        = getLong(req, BOOK);
            Double[]    expense     = getDoubleArray(req, EXPENSE);
            Date[]      tmCreate    = getDateArray(req, TM_CREATE);
            Date[]      tmStatus    = getDateArray(req, TM_STATUS);
            String      clazz       = getString(req, CLAZZ);

            Integer start   = getInt(req, START);
            Integer size    = getInt(req, SIZE);
                    size    = size!=null?size:defaultPageSize;
            String  order   = getString(req, ORDER);
            String  by      = getString(req, BY);

            DetachedCriteria dc = DetachedCriteria.forClass(ContractBase.class);

            if (id != null)
                dc.add(Restrictions.in("id", id));

            if (isbn != null)
                dc.add(Restrictions.eq("isbn", isbn));

            if (supply != null)
                dc.createCriteria("supply")
                    .add(Restrictions.eq("id", supply));

            if (consume != null)
                dc.createCriteria("consume")
                    .add(Restrictions.eq("id", consume));

            if (book != null)
                dc.createCriteria("book")
                    .add(Restrictions.eq("id", book));

            if (expense != null)
            {
                if (expense[0] != null)
                    dc.add(Restrictions.gt("expense", expense[0]));
                if (expense[1] != null)
                    dc.add(Restrictions.le("expense", expense[1]));
            }

            if (tmCreate != null)
            {
                if (tmCreate[0] != null)
                    dc.add(Restrictions.gt("tmCreate", tmCreate[0]));
                if (tmCreate[1] != null)
                    dc.add(Restrictions.le("tmCreate", tmCreate[1]));
            }

            if (tmStatus != null)
            {
                if (tmStatus[0] != null)
                    dc.add(Restrictions.gt("tmStatus", tmStatus[0]));
                if (tmStatus[1] != null)
                    dc.add(Restrictions.le("tmStatus", tmStatus[1]));
            }

            if (clazz != null)
                dc.add(Restrictions.eq("clazz", clazz));


            if ("asc".equals(order))
                dc.addOrder(Order.asc(by));
            if ("desc".equals(order))
                dc.addOrder(Order.desc(by));

            this.contractDao.begin();

            List<ContractBase> l = (List<ContractBase>)this.contractDao.search(dc, start, size);

            Json.writeContractBase(l, resp);

            this.contractDao.commit();
        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.contractDao.close();
        }

        return;
    }
}
