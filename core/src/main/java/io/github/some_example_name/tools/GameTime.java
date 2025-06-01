package io.github.some_example_name.tools;

public class GameTime {
    private float timeAccumulator = 0f;
    private int minutes = 0;
    private int hours = 6; // Start at 6 AM
    private int day = 1;
    private int month = 1;
    private int year = 1;

    private boolean paused = false;

    private final int MINUTES_IN_HOUR = 60;
    private final int HOURS_IN_DAY = 24;
    private final int DAYS_IN_MONTH = 20;
    private final int MONTHS_IN_YEAR = 4;

    // Time passes faster: 10 real seconds = 1 in-game minute
    private final float SECONDS_PER_INGAME_MINUTE = 0.5f;

    public void update(float delta) {
        if (!paused) {
            timeAccumulator += delta;

            while (timeAccumulator >= SECONDS_PER_INGAME_MINUTE) {
                timeAccumulator -= SECONDS_PER_INGAME_MINUTE;
                advanceTime();
            }
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private void advanceTime() {
        minutes++;
        if (minutes >= MINUTES_IN_HOUR) {
            minutes = 0;
            hours++;
            if (hours >= HOURS_IN_DAY) {
                hours = 0;
                day++;
                if (day > DAYS_IN_MONTH) {
                    day = 1;
                    month++;
                    if (month > MONTHS_IN_YEAR) {
                        month = 1;
                        year++;
                    }
                }
            }
        }
    }

    public String getFormattedTime() {
        return String.format("Day %02d, Month %02d - %02d:%02d", day, month, hours, minutes);
    }

    // Optionally: Getters
    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getHours() { return hours; }
    public int getMinutes() { return minutes; }
}
