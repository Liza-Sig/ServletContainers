package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    private static final String POSTS_PATH = "/api/posts";
    private static final String POSTS_ID_PATH = "/api/posts/\\d+";

    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        final var path = req.getRequestURI();
        final var method = req.getMethod();
        try {
            if (GET.equals(method) && POST.equals(path)) {
                controller.all(resp);
                return;
            }
            if (GET.equals(method) && path.matches(POSTS_ID_PATH)) {
                controller.getById(getID(path), resp);
                return;
            }
            if (POST.equals(method) && POSTS_PATH.equals(path)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (DELETE.equals(method) && path.matches(POSTS_ID_PATH)) {
                controller.removeById(getID(path), resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    private long getID(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}