package gr.uoa.di.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    @Autowired
    SuggestionService suggestionService;

    @Autowired
    ItemService itemService;

    /* run algorithm every day at midnight */
    @Scheduled(cron = "0 0 0 * * *")
    public void doAutosuggestions() {
        suggestionService.runAutosuggestions();
    }

    /* finalize finished auctions every minute */
    @Scheduled(cron = "10 * * * * *")
    public void finalizeAuctions() {
        int finished = itemService.finalizeAuctions();
        if (finished > 0) {
            System.out.println(finished + " auctions have been finalized");
        }
    }
}
