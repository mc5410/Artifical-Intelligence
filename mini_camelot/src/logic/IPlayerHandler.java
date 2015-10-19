
/**
 * This class handles the player handler and makes sure that the game state and the active player handler is correct
 */
package logic;

public interface IPlayerHandler {

	public ThisMove getMove();

	public void moveSuccessfullyExecuted(ThisMove thisMove);

}
