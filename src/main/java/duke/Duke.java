package duke;

public class Duke {

    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    public Duke() {
        ui = new Ui();
        storage = new Storage();
        try {
            taskList = new TaskList(storage.load(ui));
        } catch (DukeException e) {
            ui.printLine(e.getMessage());
            taskList = new TaskList();
        }
    }

    public void run() {
        ui.greet();
        takeCommands(ui, taskList, storage);
        ui.exit();
    }

    public static void main(String[] args) {
        new Duke().run();
    }

    private static void takeCommands(Ui ui, TaskList taskList, Storage storage) {
        String command;
        String input;
        boolean isExiting = false;
        while(!isExiting) {
            try {
                input = ui.readCommand();
                command = Parser.parse(input, taskList);
                isExiting = executeCommand(command, ui, taskList, storage);
            } catch (DukeException e) {
                ui.printFormattedMessage(e.getMessage());
            } catch (NumberFormatException e) {
                ui.printFormattedMessage(Ui.NUM_FORMAT_ERROR);
            }
        }
    }

    private static boolean executeCommand(String command, Ui ui, TaskList taskList, Storage storage) {
        String[] commandSubstrings = command.split("\\s+");
        if (command.equals("bye")) {
            return true;
        }
        if (command.equals("list")) {
            taskList.listTasks(ui);
        } else if (command.equals("thanks")) {
            ui.printFormattedMessage(Ui.THANKS_RESPONSE);
        } else if (commandSubstrings[0].equals("done")) {
            taskList.checkOffTask(ui, Integer.parseInt(commandSubstrings[1]));
            storage.saveTaskstoDisk(ui, taskList);
        } else if (commandSubstrings[0].equals("delete")) {
            taskList.deleteTask(ui, Integer.parseInt(commandSubstrings[1]));
            storage.saveTaskstoDisk(ui, taskList);
        } else if (commandSubstrings[0].equals("todo")) {
            taskList.addTodo(command.substring(5), ui);
            storage.saveTaskstoDisk(ui, taskList);
        } else if (commandSubstrings[0].equals("deadline")) {
            taskList.addDeadline(command.substring(9), ui);
            storage.saveTaskstoDisk(ui, taskList);
        } else {
            taskList.addEvent(command.substring(6), ui);
            storage.saveTaskstoDisk(ui, taskList);
        }
        return false;
    }
}
