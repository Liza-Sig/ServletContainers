package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong nextID = new AtomicLong(0);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long id = nextID.incrementAndGet();
            post.setId(id);
            posts.put(id, post);
            return post;
        }
         if (!posts.containsKey(post.getId())) {
             throw new NotFoundException();
         }
         posts.put(post.getId(), post);
         return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}