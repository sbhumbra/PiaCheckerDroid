package shub.piachecker;

/**
 * Created by Ceasar on 07/09/2016.
 */
public class EventHandler implements IEventHandler {

    final Runnable _action;

    public EventHandler(Runnable action){
        _action = action;
    }

    public void onEvent(){
        if(_action != null)
            _action.run();
    }
}
