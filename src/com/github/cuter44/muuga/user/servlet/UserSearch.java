package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import org.hibernate.criterion.*;
import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.servlet.Params.getLongList;
import static com.github.cuter44.nyafx.servlet.Params.getInt;
import static com.github.cuter44.nyafx.servlet.Params.getString;
import static com.github.cuter44.nyafx.servlet.Params.getStringList;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.conf.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

/** 搜索用户, 主要用于用户 uid, mail, uname.
 * <pre style="font-size:12px">

   <strong>请求</strong>
   GET/POST /user/search.api

   <strong>参数</strong>
   id       :long[]     , 逗号分隔, uid
   uid      :long[]     , 逗号分隔, uid, 与 id 同义, 仅用于兼容旧的客户端
   mail     :string[]   , 逗号分隔, 邮件地址
   uname    :string[]   , 逗号分隔, 用户名字, 不包含显示名
   <i>分页</i>
   start    :int        , 返回结果的起始笔数, 缺省从 0 开始
   size     :int        , 返回结果的最大笔数, 缺省使用服务器配置
   <i>排序</i>
   by       :string             , 按该字段...
   order    :string=asc|desc    , 顺序|逆序排列

   <strong>响应</strong>
   application/json array class=user.model.User(public)
   attributes refer to {@link Json#jsonizeUser(User) Json}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/user/search.api")
public class UserSearch extends HttpServlet
{
    private static final String UID = "uid";
    private static final String ID = "id";
    private static final String MAIL = "mail";
    private static final String UNAME = "uname";

    private static final String START = "start";
    private static final String SIZE = "size";
    private static final String ORDER = "order";
    private static final String BY = "by";

    private static final Integer defaultPageSize = Configurator.getInstance().getInt("nyafx.search.defaultpagesize", 20);

    protected UserDao userDao = UserDao.getInstance();

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
            List<Long>      uids    = getLongList(req, UID);
            List<Long>      ids     = getLongList(req, ID);
            List<String>    mails   = getStringList(req, MAIL);
            List<String>    unames  = getStringList(req, UNAME);

            Integer     start   = getInt(req, START);
            Integer     size    = getInt(req, SIZE);
                        size    = size!=null?size:defaultPageSize;
            String      order   = getString(req, ORDER);
            String      by      = getString(req, BY);

            DetachedCriteria dc = DetachedCriteria.forClass(User.class);

            if (ids!=null)
                dc.add(Restrictions.in("id", ids));

            if (uids!=null)
                dc.add(Restrictions.in("id", uids));

            if (mails!=null)
                dc.add(Restrictions.in("mail", mails));

            if (unames!=null)
                dc.add(Restrictions.in("uname", unames));

            if ("asc".equals(order))
                dc.addOrder(Order.asc(by));
            if ("desc".equals(order))
                dc.addOrder(Order.desc(by));

            this.userDao.begin();

            List<User> l = (List<User>)this.userDao.search(dc, start, size);

            this.userDao.commit();

            Json.writeUser(l, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(KEY_EXCEPTION, ex);
            req.getRequestDispatcher(URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.userDao.close();
        }

        return;
    }
}
