package ru.intcode.repostme.webapp.services;

import com.showvars.fugaframework.FugaApp;

abstract public class Task {
    private final FugaApp app;
    
    public Task(FugaApp app) {
        this.app = app;
        
    }
    
    abstract public void run(); 
}
