package com.github.cuter44.muuga.sys.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.servlet.*;
//import com.github.cuter44.nyafx.user.exception.*;

/**
 * 公共错误解析接口
 * 该接口接受从其他接口转发的请求, 并试图为其抛出的异常作出解释, 以便客户端解析
 *
 * <pre style="font-size:12px">

   <strong>请求</strong>
   /sys/exception.api
   <i>仅接受从其他接口转发的请求</i>

   <strong>响应</strong>
   application/json:
   error    :string , 机器可读的错误原因
   msg      :string , 用于调试的错误信息, 大部分情况下返回 Exception.getMessage();
   <i>error字段可能的取值如下
   <ul>
   <li>ok           : 参见<i>异常</i>部分
   <li>!parameter   : MissingParameterException, 缺少必需的参数, 通常在 msg 中指出缺少的参数名
   <li>!notfound    : EntityNotFoundException, id 类参数指定的实体不存在, 可能在 msg 中指出类名及 id, 也可能指出抛出该异常的代码位置
   <li>!refered     : EntityReferedException, 删除操作时实体无法被解引用, 例如删除书籍时所删除的书籍正处于交易当中
   <li>!duplicated  : EntityDuplicatedException, 创建操作违反唯一约束, 例如注册时使用了重合的用户名
   <li>!state       : IlllegalStateException, 试图在不符合操作状态的情况下操作, 比如未付款但确认收款
   <li>!error       : 无法解析的错误
   </ul>
   </i>

   <strong>异常</strong>
   直接调用该接口时, 返回 {"error":"ok", "msg":"(´☉ ∀ ☉)ﾉ ?"}

 * </pre>
 */
@WebServlet("/sys/exception.api")
public class ExceptionHandler extends HttpServlet
{
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        Exception ex = (Exception)req.getAttribute("exception");
        this.getServletContext().log("", ex);

        String error = "ok";
        String msg = "(´☉ ∀ ☉)ﾉ ?";

        if (ex != null)
        {
            error = "!error";
            msg = ex.getMessage();

            if (ex instanceof EntityNotFoundException)
                error = "!notfound";
            if (ex instanceof EntityReferencedException)
                error = "!refered";
            if (ex instanceof EntityDuplicatedException)
                error = "!duplicated";
            if (ex instanceof MissingParameterException)
                error = "!parameter";
            //if (ex instanceof UnauthorizedException)
                //error = "!authorize";
            //if (ex instanceof LoginBlockedException)
                //error = "!login-block";

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        resp.setContentType("application/json; charset=utf-8");
        PrintWriter out = resp.getWriter();

        out.println("{\"error\":\""+error+"\",\"msg\":\""+msg+"\"}");

        return;
    }
}
