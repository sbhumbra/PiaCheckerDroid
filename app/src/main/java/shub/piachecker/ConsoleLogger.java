package shub.piachecker;

/**
 * Created by Ceasar on 07/09/2016.
 */
public class ConsoleLogger implements ILogger {

    public void Log(String message){
        System.out.println(message);
    }

}
