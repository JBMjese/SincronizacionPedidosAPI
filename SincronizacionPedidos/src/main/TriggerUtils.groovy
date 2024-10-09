package sincronizacionpedidos.utils

import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.quartz.SimpleScheduleBuilder
import org.quartz.Scheduler
import org.quartz.SchedulerException
import org.quartz.StdSchedulerFactory

class TriggerUtils {

     // Metodo para programar un trabajo
    static void scheduleJob(Class jobClass, int seconds, int minutes, int hours, int dayOfMonth, int month, int dayOfWeek) {
       
        def job = jobClass.newInstance()
        def trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobClass.simpleName, "triggers")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(minutes)
                        .repeatForever())
                .build()
        def scheduler = new StdSchedulerFactory().getScheduler()
        scheduler.scheduleJob(job, trigger)
    }
}