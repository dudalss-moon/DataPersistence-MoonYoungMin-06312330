package org.example;

import org.example.db.DatabaseManager;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryImpl;

public class Main {

    public static void main(String[] args) {
        DatabaseManager.initialize();
        UserRepository repo = new UserRepositoryImpl();

        // Create
        User alice = repo.save(new User("Alice", "alice@example.com", 30));
        User bob   = repo.save(new User("Bob",   "bob@example.com",   25));
        System.out.println("[저장] " + alice);
        System.out.println("[저장] " + bob);

        // Read
        repo.findById(alice.getId()).ifPresent(u -> System.out.println("[조회] " + u));

        // Update
        alice.setAge(31);
        repo.update(alice);
        System.out.println("[수정] " + repo.findById(alice.getId()).orElseThrow());

        // Read All
        System.out.println("[전체 조회]");
        repo.findAll().forEach(u -> System.out.println("  " + u));

        // Delete
        repo.deleteById(bob.getId());
        System.out.println("[삭제 후 전체 조회]");
        repo.findAll().forEach(u -> System.out.println("  " + u));
    }
}
