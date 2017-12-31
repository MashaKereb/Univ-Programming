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

        if (checkContextTypeIsNotHtml(request) || checkPathIsAuth(request)) {
            chain.doFilter(request, response);
        } else {
            if (((HttpServletRequest) request).getSession().getAttribute("user") == null) {
                ((HttpServletResponse) response).sendRedirect("/authorization.jsp");
            }
            chain.doFilter(request, response);
        }
    }

    private boolean checkContextTypeIsNotHtml(ServletRequest request) {
        String ct = request.getContentType();
        return ct == null || !ct.equals("text/html");
    }

    private boolean checkPathIsAuth(ServletRequest request) {
        String path = ((HttpServletRequest) request).getRequestURI();
        return path.startsWith("/Authorization") || path.startsWith("/authorization.jsp");
    }

    @Override
    public void destroy() {

    }
}
