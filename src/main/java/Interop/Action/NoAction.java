package Interop.Action;

import Interop.Percept.Percepts;

/**
 * Represents an intention to make no action.
 * An agent may decide to issue NoAction while in a cool down period.
 *
 * NoAction must be always "executed" by the game controller.
 * Specifically, agent issuing NoAction will perceive the action as executed on the next turn.
 * @see Percepts#wasLastActionExecuted()
 */
public final class NoAction implements Action, IntruderAction, GuardAction {
}
