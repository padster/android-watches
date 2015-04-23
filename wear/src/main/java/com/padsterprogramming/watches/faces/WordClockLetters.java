package com.padsterprogramming.watches.faces;

/** Calculation utilities for the word clock. */
public class WordClockLetters {
  private static final String[] TEXT = {
      "HELLOWITXIS",
      "ORLDQUARTER",
      "TWENTYFIVEX",
      "HALFBTENFTO",
      ">PASTANINE<",
      "ONESIXTHREE",
      "FOURFIVETWO",
      "EIGHTELEVEN",
      "SEVENTWELVE",
      "TENSO'CLOCK"
  };

  static final int ROWS = TEXT.length;
  static final int COLS = TEXT[0].length(); // Assume all the same.

  static final String charAt(int row, int col) {
    return TEXT[row].substring(col, col + 1);
  }

  /**
   * Given a time (as hr:min, where min is a multiple of 5), and a position in the grid,
   * calculate whether the position is hilighted.
   * Done per row, by checking whether the position is within certain needed words.
   */
  static boolean isActive(int hour, int mins, int row, int column) {
    boolean isTo =  (35 <= mins && mins <= 55);
    boolean isPast = (5 <= mins && mins <= 30);
    int visMins = Math.min(mins, 60 - mins);
    int visHour = hour + (isTo ? 1 : 0);
    visHour = (visHour + 11) % 12 + 1; // 12 hour, and use '12' instead of '0'.

    switch (row) {
      case 0: // Fow 1: always have "...IT_IS"
        if (column == 6 || column == 7 || column == 9 || column == 10) {
          return true;
        }
        break;
      case 1: // Row 2: 3 -> 9 on for "QUARTER" to/past.
        if (visMins == 15) {
          return 4 <= column && column <= 10;
        }
        break;
      case 2: // Row 3: "TWENTY" or "TWENTYFIVE" at the start.
        if ((visMins == 20 || visMins == 25) && 0 <= column && column <= 5) {
          return true;
        }
        if ((visMins ==  5 || visMins == 25) && 6 <= column && column <= 9) {
          return true;
        }
        break;
      case 3: // Row 4: "HALF" at the start, "TEN", and "TO" available.
        if (visMins == 30 && 0 <= column && column <= 3) {
          return true;
        }
        if (visMins == 10 && 5 <= column && column <= 7) {
          return true;
        }
        if (isTo && 9 <= column && column <= 10) {
          return true;
        }
        break;
      case 4: // Row 5: "PAST" at the start, "NINE" at the end
        if (isPast && 1 <= column && column <= 4) {
          return true;
        }
        if (visHour == 9 && 6 <= column && column <= 9) {
          return true;
        }
        break;
      case 5: // Row 6: "ONE", "SIX", then "THREE"
        if (visHour == 1 && 0 <= column && column <= 2) {
          return true;
        }
        if (visHour == 6 && 3 <= column && column <= 5) {
          return true;
        }
        if (visHour == 3 && 6 <= column && column <= 10) {
          return true;
        }
        break;
      case 6: // Row 7: "FOUR", "FIVE" then "TWO"
        if (visHour == 4 && 0 <= column && column <= 3) {
          return true;
        }
        if (visHour == 5 && 4 <= column && column <= 7) {
          return true;
        }
        if (visHour == 2 && 8 <= column && column <= 10) {
          return true;
        }
        break;
      case 7: // Row 8: "EIGHT" then "ELEVEN"
        if (visHour == 8 && 0 <= column && column <= 4) {
          return true;
        }
        if (visHour == 11 && 5 <= column && column <= 10) {
          return true;
        }
        break;
      case 8: // Row 9: "SEVEN" then "TWELVE"
        if (visHour == 7 && 0 <= column && column <= 4) {
          return true;
        }
        if (visHour == 12 && 5 <= column && column <= 10) {
          return true;
        }
        break;
      case 9: // Row 10: "TEN" at the start, "O'CLOCK" at the end
        if (visHour == 10 && 0 <= column && column <= 2) {
          return true;
        }
        if (visMins == 0 && 4 <= column && column <= 10) {
          return true;
        }
        break;
    }
    return false; // Default to off.
  }
}
