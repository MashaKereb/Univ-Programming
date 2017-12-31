package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getRequestURI();
        if (path.startsWith("/Authorization")||path.startsWith("/web/authorization.jsp")) {
            System.out.println("=================================================");
            chain.doFilter(request, response);
            System.out.println("---------------------------");

        } else {
            System.out.println("******************************************");
            if (((HttpServletRequest) request).getSession().getAttribute("User")==null)  {
                ((HttpServletResponse)response).sendRedirect("/web/authorization.jsp");
                System.out.println("....................................................");
                return;
            }
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }
}
