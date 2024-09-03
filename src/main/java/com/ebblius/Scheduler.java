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

    // Yapıcı metot: FileSynchronizer'ı kullanarak Scheduler oluşturur
    public Scheduler(FileSynchronizer fileSynchronizer) {
        this.fileSynchronizer = fileSynchronizer;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void scheduleSync(Date time) {
        long delay = time.getTime() - System.currentTimeMillis();
        if (delay <= 0) {
            throw new IllegalArgumentException("Zamanlanmış saat gelecekte olmalıdır.");
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    fileSynchronizer.startSynch();
                } catch (Exception e) {
                    Logger.getInstance().error("Zamanlanmış senkronizasyon sırasında hata: " + e.getMessage());
                }
            }
        }, delay);
        Logger.getInstance().info("Senkronizasyon şu saatte zamanlandı: " + time);
    }

    public void startScheduledSync(long period, TimeUnit timeUnit) {

        scheduler.scheduleAtFixedRate(() -> {
            try {
                fileSynchronizer.startSynch();
            } catch (Exception e) {
                Logger.getInstance().error("Zamanlanmış senkronizasyon sırasında hata: " + e.getMessage());
            }
        }, 0, period, timeUnit);

        Logger.getInstance().info("Zamanlanmış senkronizasyon başlatıldı ve her " + period + " dakikada bir tekrarlanacak.");
    }

    public void stopScheduler() {
        if (timer != null) {
            timer.cancel();
        }
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        Logger.getInstance().info("Tüm zamanlanmış görevler durduruldu.");
    }


}
