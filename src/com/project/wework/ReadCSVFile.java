package com.project.wework;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.YearMonth;

public class ReadCSVFile {

    public static int[] getRevenueAndCapacityFromCSVFile(String csvLocation, String dateInputString) {
	int[] result = new int[2];
	double rent = 0;
	int capacity = 0;
	BufferedReader br = null;
	String line;
	int yearInput = Integer.valueOf(dateInputString.trim().substring(0, 4));
	int monthInput = Integer.valueOf(dateInputString.trim().substring(5, 7));
	YearMonth yearMonthObject = YearMonth.of(yearInput, monthInput);
	int daysInMonth = yearMonthObject.lengthOfMonth();
	int[] dateInput = { yearInput, monthInput, daysInMonth };
	try {
	    // Load the file in a BufferedReader
	    br = new BufferedReader(new FileReader(csvLocation));
	    br.readLine();
	    while ((line = br.readLine()) != null) {
		String[] officeDataLine = line.split(",");
		String startDateOfTheFile = officeDataLine[2].trim();
		int[] startDate = { Integer.valueOf(startDateOfTheFile.substring(0, 4)),
			Integer.valueOf(startDateOfTheFile.substring(5, 7)),
			Integer.valueOf(startDateOfTheFile.substring(8, 10)) };
		int[] endDate = new int[3];
		// Make a difference if it has an end date or not
		if (officeDataLine.length == 3) {
		    endDate[0] = startDate[0];
		    endDate[1] = startDate[1];
		    endDate[2] = daysInMonth;
		} else {
		    String endDateString = officeDataLine[3].trim();
		    endDate[0] = Integer.valueOf(endDateString.substring(0, 4));
		    endDate[1] = Integer.valueOf(endDateString.substring(5, 7));
		    endDate[2] = Integer.valueOf(endDateString.substring(8, 10));
		}
		if (compareDate(startDate, endDate, yearInput, monthInput)) {
		    rent += calculateRevenueForTheMonth(Integer.valueOf(officeDataLine[1].trim()), startDate, endDate,
			    dateInput);
		} else {
		    capacity += Integer.valueOf(officeDataLine[0].trim());
		}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (br != null) {
		try {
		    br.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	//The revenue is truncated
	result[0] = (int) rent;
	result[1] = capacity;
	return result;
    }

    // Compute the revenue for the month
    private static double calculateRevenueForTheMonth(int price, int[] startDate, int[] endDate, int[] dateInput) {
	int numberOfDays;
	// Compute the number of days that must be paid depending on the start date, the
	// end date and the input date.
	if (endDate[0] == startDate[0] && endDate[1] == startDate[1]) {
	    numberOfDays = endDate[2] - startDate[2] + 1;
	} else if (startDate[0] == dateInput[0] && startDate[1] == dateInput[1]) {
	    numberOfDays = dateInput[2] - startDate[2] + 1;
	} else if (endDate[0] == dateInput[0] && endDate[1] == dateInput[1]) {
	    numberOfDays = endDate[2];
	} else {
	    numberOfDays = dateInput[2];
	}
	// General formula that compute the price depending on the number of days that
	// must be paid, the price of the rent for the month
	// and the number of days in the month. price/dateInput[2] represent the price
	// for one day.
	return (double) numberOfDays * price / dateInput[2];
    }

    // Check if the input date is between the start date and the end date (or the
    // end of the month if there is no end date)
    private static boolean compareDate(int[] startDate, int[] endDate, int yearInput, int monthInput) {
	if (yearInput < startDate[0]) {
	    return false;
	} else if (yearInput == startDate[0] && monthInput < startDate[1]) {
	    return false;
	} else if (yearInput > endDate[0]) {
	    return false;
	} else if (yearInput == endDate[0] && monthInput > endDate[1]) {
	    return false;
	}
	return true;
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
	Window window = new Window();
    }
}
