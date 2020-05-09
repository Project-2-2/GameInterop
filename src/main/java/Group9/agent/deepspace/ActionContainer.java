package Group9.agent.deepspace;

import Interop.Action.Action;

import java.util.HashMap;
import java.util.Map;

public class ActionContainer<T extends Action> {

    private final String source;
    private final Input input;
    private final T action;

    public ActionContainer(String source, T action, Input input)
    {
        this.source = source;
        this.action = action;
        this.input = input;
    }

    public String getSource() {
        return this.source;
    }

    public T getAction() {
        return this.action;
    }

    public Input getData() {
        return this.input;
    }


    public static <E extends Action> ActionContainer<E> of(Object source, E action, Input input) {
        return new ActionContainer<>(source.getClass().getSimpleName(), action, input);
    }

    public static <E extends Action> ActionContainer<E> of(Object source, E action) {
        return of(source.getClass(), action);
    }

    public static <E extends Action> ActionContainer<E> of(Class<?> source, E action) {
        return of(source.getName(), action);
    }

    public static <E extends Action> ActionContainer<E> of(String source, E action) {
        return new ActionContainer<>(source, action, null);
    }

    public static class Input {

        private HashMap<String, Object> inputs = new HashMap<>();

        private Input() {}

        public Input i(String key, Object value)
        {
            this.inputs.put(key, value);
            return this;
        }

        public Map<String, Object> build()
        {
            return this.inputs;
        }

        public static Input create() {
            return new Input();
        }

        public Input clone()
        {
            Input input = new Input();
            input.inputs = (HashMap<String, Object>) this.inputs.clone();
            return input;
        }

    }
}
