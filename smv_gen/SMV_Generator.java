import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SMV_Generator {
    public static class Triplet {
        String Event;
        boolean bool;
        String State;

        public Triplet(String Event, boolean bool, String State) {
            this.Event = Event;
            this.bool = bool;
            this.State = State;
        }

        @Override
        public String toString() {
            return "(" + Event + "," + bool + "," + State + ")";
        }
    }

    public static boolean isUpperCase(String s) {
        if (s.length() == 0) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isUpperCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getEventString(String s) {
        if (!s.contains("\"")) {
            return null;
        }
        int start_index = s.indexOf('\"');
        int end_index = s.indexOf('\"', start_index + 1);
        return s.substring(start_index + 1, end_index);
    }

    public static Boolean isCondition(String s) {
        if (s.contains("!isTrue")) {
            return false;
        } else if (s.contains("isTrue")) {
            return true;
        }
        return null;
    }

    static ArrayList<String> States = new ArrayList<String>();
    static ArrayList<String> Events = new ArrayList<String>();
    static ArrayList<Triplet> Transitions = new ArrayList<Triplet>();
    static String init_State;
    static String current_event;
    static Boolean current_bool = null;

    public static void main(String[] args) {
        String path = args[0];
        System.out.println(parse(path));
    }

    public static void parseStateLine(String strLine) {
        String[] words_arr = strLine.split(" ");
        for (String word : words_arr) {
            word = word.trim();
            if (word.contains("{") || word.contains("}")) { // return if entering new depth
                return;
            } else if (isUpperCase(word)) {
                States.add(word);
                return;
            }
        }
        return;
    }

    public static void parseTransitionOuter(String strLine) {
        String[] words_arr = strLine.split(" ");
        for (String word : words_arr) {
            word = word.trim();
            String event = getEventString(word);
            if (word.contains("{") || word.contains("}")) { // return if entering new depth
                return;
            } else if (event != null) {
                current_event = event;
                if (!Events.contains(event)) {
                    Events.add(event);
                    current_event = event;
                }
                return;
            }
        }
        return;
    }

    public static void parseTransitionInner(String strLine) {
        String[] words_arr = strLine.split(" ");
        for (String word : words_arr) {
            word = word.trim();
            if (word.contains("{") || word.contains("}")) { // return if entering new depth
                return;
            } else if (isCondition(word) != null) {
                current_bool = isCondition(word);
            } else if (isUpperCase(word.substring(0, word.length() - 1))) {
                String state = word.substring(0, word.length() - 1);
                Triplet transition = new Triplet(current_event, current_bool, state);
                Transitions.add(transition);
                return;
            }
        }
        return;
    }

    public static String parse(String path) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String strLine;
        int depth = 0;
        int state_depth = -1;
        boolean in_state = false;
        try {
            while ((strLine = reader.readLine()) != null) {
                String[] words_arr = strLine.split(" ");
                List<String> words = Arrays.asList(words_arr);
                if (strLine.contains("enum State")) {
                    state_depth = depth + 1;
                    in_state = true;
                }
                if (in_state && depth == state_depth) { // if currently in enum State
                    parseStateLine(strLine);
                } else if (depth == state_depth + 2) { // if currently in event function
                    parseTransitionOuter(strLine);
                } else if (depth == state_depth + 3) {
                    parseTransitionInner(strLine);
                }
                if (strLine.contains("{")) {
                    depth++;
                }
                if (strLine.contains("}")) {
                    if (in_state && depth == state_depth) {
                        break;
                    }
                    depth--;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String output = "MODULE main\n\tVAR\n\t\tstate:{";
        for (String state : States) {
            output += state + ",";
        }
        output = output.substring(0, output.length() - 1);
        output += "};\n";
        for (String event : Events) {
            output += "\t\t" + event + ": boolean;\n";
        }
        output += "\tASSIGN\n";
        output += "\t\tinit(state):=" + States.get(0) + ";\n";
        for (String event : Events) {
            output += "\t\tinit(" + event + "):= FALSE;\n";
        }
        for (String event : Events) {
            output += "\t\tnext(" + event + "):=\n\t\t\tcase\n\t\t\t\tTRUE:{FALSE,TRUE};\n\t\t\tesac;\n";
        }
        output += "\t\tnext(state):=\n\t\t\tcase\n";
        for (Triplet transition : Transitions) {
            if (transition.bool) {
                output += "\t\t\t\t" + transition.Event + " : " + transition.State + ";\n";
            }
        }
        output += "\t\t\t\tTRUE : " + States.get(0) + ";\n";
        output += "\t\t\tesac;";
        return output;
    }

}