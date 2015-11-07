package ru.intcode.repostme.webapp.services;

import com.showvars.fugaframework.services.Service;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueService extends Service {

    private final Queue<Task> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        Task task = queue.poll();

        if (task != null) {
            task.run();
        }
    }

    public void addTask(Task task) {
        queue.add(task);
    }

}
