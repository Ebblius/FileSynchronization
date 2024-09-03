package com.ebblius;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    private final FileSynchronizer fileSynchronizer;
    private final ScheduledExecutorService scheduler;
    private Timer timer;

    public Scheduler(FileSynchronizer fileSynchronizer) {
        this.fileSynchronizer = fileSynchronizer;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void scheduleSync(Date time) {
        long delay = time.getTime() - System.currentTimeMillis();
        if (delay <= 0) {
            throw new IllegalArgumentException("The scheduled time must be in the future.");
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    fileSynchronizer.startSynch();
                } catch (Exception e) {
                    Logger.getInstance().error("Error during scheduled sync: " + e.getMessage());
                }
            }
        }, delay);
        Logger.getInstance().info("Sync is completed at: " + time);
    }

    public void startScheduledSync(long period, TimeUnit timeUnit) {

        scheduler.scheduleAtFixedRate(() -> {
            try {
                fileSynchronizer.startSynch();
            } catch (Exception e) {
                Logger.getInstance().error("Error during scheduled sync: " + e.getMessage());
            }
        }, 0, period, timeUnit);

        Logger.getInstance().info("Scheduled synchronization has been started and will repeat every " + period + " time intervals.");
    }

    public void stopScheduler() {
        if (timer != null) {
            timer.cancel();
        }
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        Logger.getInstance().info("All scheduled sync operations have been stopped.");
    }


}
