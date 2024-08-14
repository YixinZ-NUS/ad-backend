package sg.nus.iss.javaspring.adprojrct.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession(false);
//        if (session != null && session.getAttribute("user") != null) {
//
//            return true;
//        }
//
//        String requestURI = request.getRequestURI();
//        if (requestURI.equals("/api/login") || requestURI.equals("/api/register") || requestURI.equals("/policy")) {
//            return true;
//        }
//
//
//        String ajaxHeader = request.getHeader("X-Requested-With");
//        if ("XMLHttpRequest".equals(ajaxHeader)) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//            return false;
//        }
//
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.sendRedirect("/login");
//        return false;
//    }
}
