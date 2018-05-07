package shub.piachecker;

import java.util.concurrent.Callable;

/**
 * Created by Ceasar on 07/09/2016.
 */
public class TaskRunner<T> {

    public void runAynchronously(
            final Callable<T> task,
            final Result<T> result,
            final IEventHandler completionEvent) throws Exception {
        if(result == null)
            throw new Exception("result cannot be null");

        Thread t = new Thread(new Runnable() {
            public void run()
            {
                try {
                    result.result = task.call();
                }catch (Exception e) {
                    result.exception = e;
                }finally {
                    completionEvent.onEvent();
                }
            }
        });

        t.start();
    }
}
