package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.notNull;
import static com.github.cuter44.nyafx.servlet.Params.getString;
import static com.github.cuter44.nyafx.servlet.Params.getInt;
import static com.github.cuter44.nyafx.servlet.Params.getLongList;
import org.hibernate.*;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.conf.Configurator;

/** 搜索用户资料
 * <pre style="font-size:12px">

   <strong>请求</strong>
   GET/POST /profile/search.api

   <strong>参数</strong>
   id       :long[]     , 逗号分隔, uid
   q        :string     , 在 mail, uname, dname, <del>tname</del> 上运行模糊匹配(%key%)
   <i>分页</i>
   start    :int        , 返回结果的起始笔数, 缺省从 0 开始
   size     :int        , 返回结果的最大笔数, 缺省使用服务器配置
   <i>排序</i>
   by       :string             , 按该字段...
   order    :string=asc|desc    , 顺序|逆序排列


   <strong>响应</strong>
   application/json array class=user.model.Profile
   attributes refer to {@link Json#jsonizeProfile(Profile) Json}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>
 * </pre>
 *
 */
@WebServlet("/profile/search.api")
public class ProfileSearch extends HttpServlet
{
    private static final String START = "start";
    private static final String SIZE = "size";
    private static final String ID = "id";
    private static final String Q = "q";

    private static final String ORDER = "order";
    private static final String BY = "by";

    private static final Integer defaultPageSize = Configurator.getInstance().getInt("nyafx.search.defaultpagesize", 20);

    protected ProfileDao profileDao = ProfileDao.getInstance();

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
            String      q       = getString(req, Q);
            List<Long>  uids    = getLongList(req, ID);

            Integer start   = getInt(req, START);
            Integer size    = getInt(req, SIZE);
                    size    = size!=null?size:defaultPageSize;
            String  order   = getString(req, ORDER);
            String  by      = getString(req, BY);

            DetachedCriteria dc = DetachedCriteria.forClass(Profile.class);

            if (uids != null)
                dc.add(Restrictions.in("id", uids));

            if (q != null)
            {
                q = "%"+q+"%";

                dc.createCriteria("user", "user")
                    .add(Restrictions.like("dname", q))
                    //.add(Restrictions.like("tname", q))
                    .add(Restrictions.like("user.uname", q))
                    .add(Restrictions.like("user.mail", q));
            }

            if ("asc".equals(order))
                dc.addOrder(Order.asc(by));
            if ("desc".equals(order))
                dc.addOrder(Order.desc(by));

            this.profileDao.begin();

            List<Profile> l = (List<Profile>)this.profileDao.search(dc);

            this.profileDao.commit();

            Json.writeProfile(l, resp);

            return;
        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.profileDao.close();
        }

        return;
    }
}
