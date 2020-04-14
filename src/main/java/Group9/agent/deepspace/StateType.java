package Group9.agent.deepspace;

enum StateType {
    INITIAL(new StateHandlerInitial()),
    EXPLORE_360(new StateHandlerExplore360()),
    FIND_NEW_TARGET(new StateHandlerFindNewTarget());

    private StateHandler stateHandlerClass;

    StateType(StateHandler stateHandlerClass) {
        this.stateHandlerClass = stateHandlerClass;
    }

    public StateHandler getStateHandlerClass() {
        return stateHandlerClass;
    }
}
