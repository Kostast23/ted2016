package gr.uoa.di.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    @Autowired
    SuggestionService suggestionService;

    /* run algorithm every day at midnight */
    @Scheduled(cron = "0 0 0 * * *")
    public void doAutosuggestions() {
        suggestionService.runAutosuggestions();
    }
}
