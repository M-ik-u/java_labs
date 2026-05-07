package servlet;

import java.io.IOException;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.ChatMessage;
import entity.ChatUser;

public class NewMessageServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        System.out.println("Message had been sent");
        request.setCharacterEncoding("UTF-8");

        String message = request.getParameter("message");

        System.out.println("message = " + message + request.getSession().getAttribute("name"));

        if (message != null && !"".equals(message.trim())) {
            ChatUser author = activeUsers.get((String) request.getSession().getAttribute("name"));

            System.out.println("Author had been initilized " + author);
            if (author != null) {
                synchronized (messages) {
                    messages.add(new ChatMessage(
                            message.trim(),
                            author,
                            Calendar.getInstance().getTimeInMillis()
                    ));
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/composemessage.htm");
    }
}