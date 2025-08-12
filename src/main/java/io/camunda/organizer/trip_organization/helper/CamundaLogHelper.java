package io.camunda.organizer.trip_organization.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CamundaLogHelper {

    private static void logToCsv(
            String fileName,
            long processInstanceKey,
            String activityName,
            String timestamp,
            String resource
    ) {
        if (timestamp == null || timestamp.isEmpty()) {
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        if (resource == null || resource.isEmpty()) {
            resource = "backend";
        }

        File file = new File(fileName + ".csv");
        try {
            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("Case ID,Activity,Timestamp,Resource\n");
                }
            }
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(processInstanceKey + "," + activityName + "," + timestamp + "," + resource + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logToCsvPrep(
            long processInstanceKey,
            String activityName,
            String timestamp,
            String resource
    ) {
        logToCsv("tripPreparation", processInstanceKey, activityName, timestamp, resource);
    }

    public static void logToCsvApplication(
            long processInstanceKey,
            String activityName,
            String timestamp,
            String resource
    ) {
        logToCsv("tripApplication", processInstanceKey, activityName, timestamp, resource);
    }
}
