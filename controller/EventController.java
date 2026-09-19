package com.example.cs360_projectthree.controller;

import android.content.Context;
import android.text.TextUtils;
import android.telephony.SmsManager;

import com.example.cs360_projectthree.model.Event;
import com.example.cs360_projectthree.model.EventDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EventController {
    private EventDBHelper DB;
    private EventView view;
    private Context context;

    // Interface for the Controller to communicate back with the View (EventDataActivity)
    public interface EventView {
        void showEventsList(ArrayList<Event> events);
        void showToast(String message);
        void clearInputFields();
    }

    public EventController(Context context, EventView view) {
        this.context = context;
        this.DB = new EventDBHelper(context);
        this.view = view;
    }

    public void loadEvents() {
        ArrayList<Event> events = DB.getEventsList();
        view.showEventsList(events);
    }

    public void handleAddEvent(String date, String title, String description, String phone, boolean isSmsPermissionGranted) {
        // 1. Checks for empty fields initially
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(phone)) {
            view.showToast("Please fill in all fields.");
            return;
        }

        // 2. Format Verification: Date must be strict MM/dd/yyyy
        if (!isValidDateFormat(date)) {
            view.showToast("Invalid date format. Please use MM/dd/yyyy.");
            return;
        }

        // 3. Format Verification: Date cannot be in the past
        Date parsedDate = parseEventDate(date);
        if (parsedDate != null && isDateInThePast(parsedDate)) {
            view.showToast("Cannot enter past dates. Please choose today or a future date.");
            return;
        }

        // 4. Format Verification: Phone number must contain only numbers and 10 digits
        if (!phone.matches("^[0-9]{10}$")) {
            view.showToast("Please enter a valid 10-digit phone number.");
            return;
        }

        // Checks the title and description lengths
        if (title.length() > 50) {
            view.showToast("Title must be under 50 characters.");
            return;
        }

        if (description.length() > 250) {
            view.showToast("Description must be under 250 characters.");
            return;
        }

        // If all checks pass, insert the event into the database
        long insertedId = DB.insertEvent(date, title, description, phone);
        if (insertedId != -1) {
            view.showToast("Event added.");
            view.clearInputFields();
            loadEvents();

            if (isSmsPermissionGranted) {
                checkUpcomingEvents();
            }
        } else {
            view.showToast("Failed to add event.");
        }
    }

    // Helper method that uses Java's SimpleDateFormat to validate the date format strictly
    private boolean isValidDateFormat(String dateStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.US);
            sdf.setLenient(false);
            sdf.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Checks if the given date is before today (ignoring hours/minutes/seconds)
    private boolean isDateInThePast(Date date) {
        // Gets the current date to variable "today"
        Calendar today = Calendar.getInstance();
        // clears the time components
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        //sets the inputted date to variable "eventCal"
        Calendar eventCal = Calendar.getInstance();
        eventCal.setTime(date);
        eventCal.set(Calendar.HOUR_OF_DAY, 0);
        eventCal.set(Calendar.MINUTE, 0);
        eventCal.set(Calendar.SECOND, 0);
        eventCal.set(Calendar.MILLISECOND, 0);

        // Returns true if the inputted date is strictly before the current date
        return eventCal.before(today);
    }

    public void handleDeleteEvent(Event event) {
        boolean deleted = DB.deleteEvent(event.getId());
        if (deleted) {
            view.showToast("Event deleted.");
            loadEvents();
        } else {
            view.showToast("Failed to delete event.");
        }
    }

    public void handleUpdateEvent(int id, String date, String title, String description) {
        // 1. Checks for empty fields initially
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            view.showToast("Please fill in all fields.");
            return;
        }

        // 2. Format Verification: Date must be strict MM/dd/yyyy
        if (!isValidDateFormat(date)) {
            view.showToast("Invalid date format. Please use MM/dd/yyyy.");
            return;
        }

        // 3. Format Verification: Date cannot be in the past
        Date parsedDate = parseEventDate(date);
        if (parsedDate != null && isDateInThePast(parsedDate)) {
            view.showToast("Cannot enter past dates. Please choose today or a future date.");
            return;
        }

        // Check title and description lengths
        if (title.length() > 50) {
            view.showToast("Title must be under 50 characters.");
            return;
        }

        if (description.length() > 250) {
            view.showToast("Description must be under 250 characters.");
            return;
        }

        // If all checks pass, insert the event into the database
        boolean updated = DB.updateEvent(id, date, title, description);
        if (updated) {
            view.showToast("Event updated.");
            loadEvents();
        } else {
            view.showToast("Failed to update event.");
        }
    }

    public void checkUpcomingEvents() {
        ArrayList<Event> events = DB.getEventsList();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        for (Event event : events) {
            try {
                if (event.getDate() == null || event.getDate().isEmpty()) {
                    continue;
                }

                Date eventDate = parseEventDate(event.getDate());
                if (eventDate == null) continue;

                Calendar eventCal = Calendar.getInstance();
                eventCal.setTime(eventDate);

                long diff = eventCal.getTimeInMillis() - today.getTimeInMillis();
                long days = TimeUnit.MILLISECONDS.toDays(diff);

                if (days >= 0 && days <= 7 && event.getSmsSent() == 0) {
                    sendSmsReminder(event);
                    DB.markSmsSent(event.getId());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendSmsReminder(Event event) {
        String phoneNumber = "5556";
        String message = "Heads up!: \"" + event.getTitle()
                + "\" is coming up on " + event.getDate() + ".";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            view.showToast("SMS sent");
        } catch (Exception e) {
            view.showToast("SMS failed");
        }
    }

    private Date parseEventDate(String dateString) {
        String[] formats = {"MM/dd/yyyy"};
        for (String format : formats) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format, java.util.Locale.US);
                sdf.setLenient(false);
                return sdf.parse(dateString);
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}