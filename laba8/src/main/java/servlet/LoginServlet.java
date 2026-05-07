package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.ChatMessage;
import entity.ChatUser;

public class LoginServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;

    private int sessionTimeout = 600;

    @Override
    public void init() throws ServletException {
        super.init();

        String value = getServletConfig().getInitParameter("SESSION_TIMEOUT");
        if (value != null) {
            sessionTimeout = Integer.parseInt(value);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        String name = (String) request.getSession().getAttribute("name");
        System.out.println("Name before login " + name);
        String errorMessage = (String) request.getSession().getAttribute("error");
        String previousSessionId = null;

        if (name == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie aCookie : cookies) {
                    if ("sessionId".equals(aCookie.getName())) {
                        previousSessionId = aCookie.getValue();
                        break;
                    }
                }
            }

            if (previousSessionId != null) {
                for (ChatUser aUser : activeUsers.values()) {
                    if (aUser.getSessionId().equals(previousSessionId)) {
                        name = aUser.getName();
                        aUser.setSessionId(request.getSession().getId());
                        break;
                    }
                }
            }
        }

        if (name != null && !"".equals(name)) {
            errorMessage = processLogonAttempt(name, request, response);
            if (errorMessage == null) {
                return;
            }
        }

        response.setCharacterEncoding("UTF-8");
        PrintWriter pw = response.getWriter();

        pw.println("<html><head><title>Мега-чат!</title>"
                + "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>"
                + "</head><body>");

        if (errorMessage != null) {
            pw.println("<p><font color='red'>" + errorMessage + "</font></p>");
        }

        pw.println("<form action='/chat/' method='post'>");
        pw.println("Введите имя: <input type='text' name='name' value=''>");
        pw.println("<input type='submit' value='Войти в чат'>");
        pw.println("</form></body></html>");

        request.getSession().setAttribute("error", null);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String errorMessage = null;

        if (name == null || "".equals(name.trim())) {
            errorMessage = "Имя пользователя не может быть пустым!";
        } else {
            name = name.trim();
            errorMessage = processLogonAttempt(name, request, response);
            if (errorMessage == null) {
                return;
            }
        }

        request.getSession().setAttribute("name", null);
        request.getSession().setAttribute("error", errorMessage);
        response.sendRedirect(response.encodeRedirectURL("/chat/"));
    }

    private String processLogonAttempt(String name,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws IOException {

        String sessionId = request.getSession().getId();
        ChatUser aUser = activeUsers.get(name);
        boolean isNewUser = false;

        if (aUser == null) {
            aUser = new ChatUser(name,
                    Calendar.getInstance().getTimeInMillis(),
                    sessionId);

            synchronized (activeUsers) {
                activeUsers.put(aUser.getName(), aUser);
            }

            isNewUser = true;
        }

        if (aUser.getSessionId().equals(sessionId) ||
                aUser.getLastInteractionTime() <
                        (Calendar.getInstance().getTimeInMillis() - sessionTimeout * 1000L)) {

            request.getSession().setAttribute("name", name);
            aUser.setLastInteractionTime(Calendar.getInstance().getTimeInMillis());

            Cookie sessionIdCookie = new Cookie("sessionId", sessionId);
            sessionIdCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
            sessionIdCookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(sessionIdCookie);

            if (isNewUser) {
                ChatMessage serviceMessage = new ChatMessage(
                        "Пользователь " + name + " пришёл в чат",
                        aUser,
                        Calendar.getInstance().getTimeInMillis()
                );

                synchronized (messages) {
                    messages.add(serviceMessage);
                }
            }

            response.sendRedirect(response.encodeRedirectURL("/chat/view.htm"));
            return null;
        }

        return "Извините, но имя <strong>" + name
                + "</strong> уже кем-то занято. Пожалуйста, выберите другое имя!";
    }
}