import java.util.Scanner;

public class Duke {
    private static final String FOUR_SPACE_INDENT = "    ";
    private static final String SIX_SPACE_INDENT = "      ";
    private static Task[] tasks = new Task[100];
    private static int taskCounter = 0;
    private static int taskIndex;
    private static String command;
    private static Scanner in = new Scanner(System.in);
    private static boolean isExiting = false;

    public static void main(String[] args) {
        greet();
        takeCommands();
        exit();
    }

    private static void takeCommands() {
        while(!isExiting) {
            try {
                command = getCommand();
                executeCommand(command);
            } catch (InvalidCommandException e) {
                System.out.print("\n" + FOUR_SPACE_INDENT);
                System.out.println("Sorry, me don't know what that means :-(");
                System.out.print(FOUR_SPACE_INDENT);
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.print("\n" + FOUR_SPACE_INDENT);
                System.out.println("Sorry, me don't know what that means :-(");
                System.out.print(FOUR_SPACE_INDENT);
                System.out.println("Format: \"done <task index>\"");
            } catch (TaskIndexOutOfBoundsException e) {
                System.out.print("\n" + FOUR_SPACE_INDENT);
                System.out.println("I need a valid task index!");
            }
        }
    }


    private static void executeCommand(String command) {
        if (command.equals("bye")) {
            isExiting = true;
        } else if (command.equals("list")) {
            listTasks();
        } else if (command.substring(0,5).equals("done ")) {
            checkOffTask(command);
        } else if (command.substring(0,5).equals("todo ")) {
            addTodo(command.substring(5));
        } else if (command.substring(0,9).equals("deadline ")) {
            addDeadline(command.substring(9));
        } else if (command.substring(0,6).equals("event ")) {
            addEvent(command.substring(6));
        }
    }

    private static void addEvent(String info) {
        int dividerIndex = info.indexOf("/");
        String description = info.substring(0, (dividerIndex - 1));
        String at = info.substring(dividerIndex + 4);
        tasks[taskCounter] = new Event(description, at);
        System.out.print("\n" + FOUR_SPACE_INDENT);
        System.out.println("Got it. I've added this task:");
        System.out.print(SIX_SPACE_INDENT);
        System.out.println(tasks[taskCounter].toString());
        taskCounter++;
        System.out.print(FOUR_SPACE_INDENT);
        System.out.println("Now you have " + taskCounter + " tasks in the list.");
    }

    private static void addDeadline(String info) {
        int dividerIndex = info.indexOf("/");
        String description = info.substring(0, (dividerIndex - 1));
        String by = info.substring(dividerIndex + 4);
        tasks[taskCounter] = new Deadline(description, by);
        System.out.print("\n" + FOUR_SPACE_INDENT);
        System.out.println("Got it. I've added this task:");
        System.out.print(SIX_SPACE_INDENT);
        System.out.println(tasks[taskCounter].toString());
        taskCounter++;
        System.out.print(FOUR_SPACE_INDENT);
        System.out.println("Now you have " + taskCounter + " tasks in the list.");
    }

    private static void addTodo(String description) {
        tasks[taskCounter] = new ToDo(description);
        System.out.print("\n" + FOUR_SPACE_INDENT);
        System.out.println("Got it. I've added this task:");
        System.out.print(SIX_SPACE_INDENT);
        System.out.println(tasks[taskCounter].toString());
        taskCounter++;
        System.out.print(FOUR_SPACE_INDENT);
        System.out.println("Now you have " + taskCounter + " tasks in the list.");
    }

    private static void checkOffTask(String command) {
        taskIndex = Integer.parseInt(command.substring(5)) - 1;
        tasks[taskIndex].markAsDone();
        System.out.print("\n" + FOUR_SPACE_INDENT);
        System.out.println("Nice! I've marked this task as done:");
        System.out.print(SIX_SPACE_INDENT);
        System.out.println(tasks[taskIndex].toString());
    }

    private static void listTasks() {
        int bulletNum;
        System.out.print("\n" + FOUR_SPACE_INDENT);
        System.out.println("Here are the tasks in your list:");
        for (taskIndex = 0; taskIndex < taskCounter; taskIndex++) {
            bulletNum = taskIndex + 1;
            System.out.print(FOUR_SPACE_INDENT);
            System.out.println(bulletNum + "." + tasks[taskIndex].toString());
        }
    }

    private static String getCommand() throws InvalidCommandException, TaskIndexOutOfBoundsException {
        String input = "";
        while (input.isEmpty()) {
            input = in.nextLine();
            input = input.trim();
        }
        // Input is not empty
        String[] inputSubstrings = input.split("\\s");
        if (inputSubstrings.length == 1) {
            if (!inputSubstrings[0].equals("list") && !inputSubstrings[0].equals("bye")) {
                throw new InvalidCommandException();
            }
        } else if (!inputSubstrings[0].equals("done") && !inputSubstrings[0].equals("todo") &&
                !inputSubstrings[0].equals("deadline") && !inputSubstrings[0].equals("event")) {
            throw new InvalidCommandException();
        } else if (inputSubstrings[0].equals("done")) {
            if (inputSubstrings.length > 2) {
                throw new InvalidCommandException("Format: \"done <task index>\"");
            } else {
               taskIndex = Integer.parseInt(inputSubstrings[1]);
               if (taskIndex < 1 || taskIndex > taskCounter) {
                   throw new TaskIndexOutOfBoundsException();
               }
            }
        } else if (inputSubstrings[0].equals("deadline")) {
            if (inputSubstrings.length < 4) {
                throw new InvalidCommandException("Format: \"deadline <description> /by <deadline>\"");
            }
            for (int i = 1; i < inputSubstrings.length; i++) {
                if (i == (inputSubstrings.length - 1)) {
                    throw new InvalidCommandException("Format: \"deadline <description> /by <deadline>\"");
                }
                if (inputSubstrings[i].equals("/by")) {
                    if (i == 1) {
                        throw new InvalidCommandException("Format: \"deadline <description> /by <deadline>\"");
                    } else {
                        break;
                    }
                }
            }
        } else if (inputSubstrings[0].equals("event")) {
            if (inputSubstrings.length < 4) {
                throw new InvalidCommandException("Format: \"event <description> /at <date/time>\"");
            }
            for (int i = 1; i < inputSubstrings.length; i++) {
                if (i == (inputSubstrings.length - 1)) {
                    throw new InvalidCommandException("Format: \"event <description> /at <date/time>\"");
                }
                if (inputSubstrings[i].equals("/at")) {
                    if (i == 1) {
                        throw new InvalidCommandException("Format: \"event <description> /at <date/time>\"");
                    } else {
                        break;
                    }
                }
            }
        }
        return input;
    }

    private static void exit() {
        System.out.print("\n" + FOUR_SPACE_INDENT);
        System.out.println("Bye. Hope to see you again soon!");
    }

    private static void greet() {
        System.out.print(FOUR_SPACE_INDENT);
        System.out.println("Hello, I'm Taskmaster Yipyap.");
        System.out.print(FOUR_SPACE_INDENT);
        System.out.println("What can I do for you?");
    }
}
