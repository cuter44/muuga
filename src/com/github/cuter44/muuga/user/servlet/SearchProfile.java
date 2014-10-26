package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.util.dao.*;
import com.github.cuter44.util.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.notNull;
import static com.github.cuter44.nyafx.servlet.Params.getString;
import static com.github.cuter44.nyafx.servlet.Params.getInt;
import static com.github.cuter44.nyafx.servlet.Params.getLongList;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.user.dao.*;
import com.github.cuter44.muuga.user.dao.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.conf.Configurator;

/** 搜索用户资料
 * <pre style="font-size:12px">

   <strong>请求</strong>
   GET/POST /profile/search.api

   <strong>参数</strong>
   <i>以下的其中一种</i>
   <i>精确匹配</i>
   id:long, 指定uid, 多值时以逗号分隔;
   <i>任意匹配</i>
   q:string, 在 mail, uname, dname, tname 上运行包含匹配(%key%)
   <i>分页</i>
   start:int, 返回结果的起始笔数, 缺省从 1 开始
   size:int, 返回结果的最大笔数, 缺省使用服务器配置

   <strong>响应</strong>
   application/json array class=profile.dao.Profile
   @see J#write

   <strong>例外</strong>
    通用, @see com.github.cuter44.muuga.sys.servlet.ExceptionHandler

   <strong>样例</strong>
 * </pre>
 *
 */
@WebServlet("/profile/search.api")
public class SearchProfile extends HttpServlet
{
    private static final String START = "start";
    private static final String SIZE = "size";
    private static final String ID = "id";
    private static final String Q = "q";

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
            Integer start   = getInt(req, START);
            Integer size    = getInt(req, SIZE);
            size = size!=null?size:defaultPageSize;

            this.profileDao.begin();

            List<Long> uids = getLongList(req, ID);
            if (uids != null)
            {
                DetachedCriteria dc = DetachedCriteria.forClass(Profile.class)
                    .add(Restrictions.in("id", uids));

                List<Profile> l = (List<Profile>)this.profileDao.search(dc);

                this.profileDao.commit();

                J.writeProfile(l, resp);

                return;
            }

            String q = getString(req, Q);
            if (q != null && q.length() > 0)
            {
                q = "%"+q+"%";
                List<Profile> l = (List<Profile>)this.profileDao.createQuery(
                    "SELECT p FROM Profile p INNER JOIN p.user u "+
                        "WHERE p.dname LIKE :q OR p.tname LIKE :q "+
                        "OR    u.uname LIKE :q OR u.mail LIKE :q")
                    .setString("q", q)
                    .list();

                this.profileDao.commit();

                J.writeProfile(l, resp);

                return;
            }

            throw(new MissingParameterException());
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
