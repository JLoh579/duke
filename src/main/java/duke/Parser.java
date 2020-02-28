package duke;

/**
 * Deals with making sense of the user command.
 */
public class Parser {

    /**
     * This method checks if an input is a valid command. It also formats the
     * input to make processing easier later on.
     * @param input The input given.
     * @param taskList The task list given.
     * @return Formatted and valid command.
     * @throws DukeException If the command is invalid.
     * @throws NumberFormatException If the index given cannot be parsed to an Integer.
     */
    public static String parse(String input, TaskList taskList) throws DukeException, NumberFormatException{
        int taskIndex;
        String[] inputSubstrings = input.split("\\s+");
        inputSubstrings[0] = inputSubstrings[0].toLowerCase();
        if (inputSubstrings.length == 1) {
            if (!inputSubstrings[0].equals("list") && !inputSubstrings[0].equals("bye") &&
                    !inputSubstrings[0].equals("thanks")) {
                throw new DukeException("Invalid command.");
            }
        } else if (!inputSubstrings[0].equals("done") && !inputSubstrings[0].equals("todo") &&
                !inputSubstrings[0].equals("deadline") && !inputSubstrings[0].equals("event") &&
                !inputSubstrings[0].equals("delete")) {
            throw new DukeException("Invalid command.");
        } else if (inputSubstrings[0].equals("done")) {
            if (inputSubstrings.length > 2) {
                throw new DukeException("Invalid command. Format: \"done <task index>\"");
            } else {
                taskIndex = Integer.parseInt(inputSubstrings[1]);
                if (taskIndex < 1 || taskIndex > taskList.getTaskList().size()) {
                    throw new DukeException("That index is outta range!");
                }
            }
        } else if (inputSubstrings[0].equals("delete")) {
            if (inputSubstrings.length > 2) {
                throw new DukeException("Invalid command. Format: \"delete <task index>\"");
            } else {
                taskIndex = Integer.parseInt(inputSubstrings[1]);
                if (taskIndex < 1 || taskIndex > taskList.getTaskList().size()) {
                    throw new DukeException("That index is outta range!");
                }
            }
        } else if (inputSubstrings[0].equals("deadline")) {
            if (inputSubstrings.length < 4) {
                throw new DukeException("Invalid command. Format: \"deadline <description> /by <deadline>\"");
            }
            for (int i = 1; i < inputSubstrings.length; i++) {
                if (i == (inputSubstrings.length - 1)) {
                    throw new DukeException("Invalid command. Format: \"deadline <description> /by <deadline>\"");
                }
                if (inputSubstrings[i].toLowerCase().equals("/by")) {
                    if (i == 1) {
                        throw new DukeException("Invalid command. Format: \"deadline <description> /by <deadline>\"");
                    } else {
                        inputSubstrings[i] = inputSubstrings[i].toLowerCase();
                        break;
                    }
                }
            }
        } else if (inputSubstrings[0].equals("event")) {
            if (inputSubstrings.length < 4) {
                throw new DukeException("Invalid command. Format: \"event <description> /at <date/time>\"");
            }
            for (int i = 1; i < inputSubstrings.length; i++) {
                if (i == (inputSubstrings.length - 1)) {
                    throw new DukeException("Invalid command. Format: \"event <description> /at <date/time>\"");
                }
                if (inputSubstrings[i].toLowerCase().equals("/at")) {
                    if (i == 1) {
                        throw new DukeException("Invalid command. Format: \"event <description> /at <date/time>\"");
                    } else {
                        inputSubstrings[i] = inputSubstrings[i].toLowerCase();
                        break;
                    }
                }
            }
        }

        StringBuilder comm = new StringBuilder();
        for (String substring : inputSubstrings) {
            comm.append(substring).append(" ");
        }
        String command = comm.toString().trim();
        return command;
    }
}